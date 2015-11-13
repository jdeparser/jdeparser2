/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.jdeparser;

import static org.jboss.jdeparser.Tokens.*;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class SourceFileWriter implements Flushable, Closeable {

    private final FormatPreferences format;
    private final CountingWriter countingWriter;
    private final StringBuilder lineBuffer = new StringBuilder();
    private final String lineSep;
    private final ArrayDeque<AbstractJType> thisTypeStack = new ArrayDeque<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") // IDEA bug http://youtrack.jetbrains.com/issue/IDEA-128168
    private final ArrayList<Indent> indentStack = new ArrayList<>();
    private final ListIterator<Indent> stackIterator = indentStack.listIterator(0);
    private final Indent nextIndent = new Indent() {

        public void addIndent(final Indent next, final FormatPreferences preferences, final StringBuilder lineBuffer) {
            if (stackIterator.hasPrevious()) {
                final Indent n = stackIterator.previous();
                try {
                    n.addIndent(next, preferences, lineBuffer);
                } finally {
                    stackIterator.next();
                }
            }
        }

        public void escape(final Indent next, final StringBuilder b, final int idx) {
            if (stackIterator.hasPrevious()) {
                final Indent n = stackIterator.previous();
                try {
                    n.escape(this, b, idx);
                } finally {
                    stackIterator.next();
                }
            }
        }

        public void unescaped(final Indent next, final StringBuilder b, final int idx) {
            if (stackIterator.hasPrevious()) {
                final Indent n = stackIterator.previous();
                try {
                    n.unescaped(this, b, idx);
                } finally {
                    stackIterator.next();
                }
            }
        }
    };
    private Token state = $START;
    private int spaceState;
    private ImplJSourceFile classFile;

    private static final int SS_NONE = 0;
    private static final int SS_NEEDED = 1;
    private static final int SS_ADDED = 2;
    private static final int SS_NEW_LINE = 3;
    private static final int SS_2_NEW_LINE = 4;

    SourceFileWriter(final FormatPreferences format, final Writer writer) {
        this.format = format;
        this.countingWriter = new CountingWriter(writer);
        // todo use preferences/config
        lineSep = System.lineSeparator();
    }

    void nl() throws IOException {
        countingWriter.write(lineBuffer);
        countingWriter.write(lineSep);
        lineBuffer.setLength(0);
        spaceState = spaceState == SS_NEW_LINE ? SS_2_NEW_LINE : SS_NEW_LINE;
    }

    /**
     * Force a space if one hasn't already been added.
     *
     * @throws IOException etc.
     */
    void sp() throws IOException {
        if (spaceState == SS_NEW_LINE || spaceState == SS_2_NEW_LINE) {
            addIndent();
        } else if (spaceState != SS_ADDED) {
            spaceState = SS_ADDED;
            lineBuffer.append(' ');
        }
    }

    /**
     * A non-trailing space.
     */
    void ntsp() throws IOException {
        if (spaceState == SS_NONE) spaceState = SS_NEEDED;
    }

    int getLine() {
        return countingWriter.getLine();
    }

    int getColumn() {
        return countingWriter.getColumn();
    }

    void processSpacing() throws IOException {
        switch (spaceState) {
            case SS_2_NEW_LINE:
            case SS_NEW_LINE: {
                nextIndent.addIndent(nextIndent, format, lineBuffer);
                spaceState = SS_ADDED;
                break;
            }
            case SS_NEEDED: {
                sp();
                break;
            }
        }
    }

    void addIndent() throws IOException {
        assert spaceState == SS_NEW_LINE || spaceState == SS_2_NEW_LINE; // it was a new line
        nextIndent.addIndent(nextIndent, format, lineBuffer);
        spaceState = SS_ADDED;
    }

    void writeEscaped(String item) throws IOException {
        processSpacing();
        final int idx = lineBuffer.length();
        lineBuffer.append(item);
        nextIndent.escape(nextIndent, lineBuffer, idx);
        spaceState = SS_NONE;
    }

    void writeEscaped(final char item) throws IOException {
        processSpacing();
        final int idx = lineBuffer.length();
        lineBuffer.append(item);
        nextIndent.escape(nextIndent, lineBuffer, idx);
        spaceState = SS_NONE;
    }

    void writeUnescaped(String item) throws IOException {
        processSpacing();
        final int idx = lineBuffer.length();
        lineBuffer.append(item);
        nextIndent.unescaped(nextIndent, lineBuffer, idx);
        spaceState = SS_NONE;
    }

    void writeUnescaped(char item) throws IOException {
        processSpacing();
        final int idx = lineBuffer.length();
        lineBuffer.append(item);
        nextIndent.unescaped(nextIndent, lineBuffer, idx);
        spaceState = SS_NONE;
    }

    void write(FormatPreferences.Space rule) throws IOException {
        if (rule == null) {
            return;
        }
        if (format.getSpaceType(rule) == FormatPreferences.SpaceType.NEWLINE) {
            if (spaceState != SS_2_NEW_LINE) {
                // must not be directly after a 2-newline
                nl();
            }
        } else {
            if (format.getSpaceType(rule) == FormatPreferences.SpaceType.SPACE) {
                ntsp();
            }
        }
    }

    void writeClass(final String nameToWrite) throws IOException {
        processSpacing();
        addWordSpace();
        writeEscaped(nameToWrite);
        this.state = $WORD;
        spaceState = SS_NONE;
    }

    void addWordSpace() throws IOException {
        if (state instanceof $KW || state == $WORD || state == $NUMBER) {
            ntsp();
        }
    }

    void write(final Token state) throws IOException {
        processSpacing();
        state.write(this);
        this.state = state;
        spaceState = SS_NONE;
    }

    void writeEscapedWord(final String rawText) throws IOException {
        writeEscaped(rawText);
        this.state = $WORD;
    }

    public void flush() throws IOException {
        countingWriter.flush();
    }

    public void close() throws IOException {
        countingWriter.close();
    }

    void write(final JType type) throws IOException {
        if (type != null) AbstractJType.of(type).writeDirect(this);
    }

    void write(final AbstractJType type) throws IOException {
        if (type != null) type.writeDirect(this);
    }

    void write(final JExpr expr) throws IOException {
        if (expr != null) AbstractJExpr.of(expr).write(this);
    }

    void write(final AbstractJExpr expr) throws IOException {
        if (expr != null) expr.write(this);
    }

    void pushIndent(FormatPreferences.Indentation indentation) {
        pushIndent(indentation.getIndent());
    }

    void pushIndent(Indent indent) {
        stackIterator.add(indent);
    }

    void popIndent(FormatPreferences.Indentation indentation) {
        popIndent(indentation.getIndent());
    }

    void popIndent(Indent indent) {
        final Indent pop = stackIterator.previous();
        stackIterator.remove();
        assert pop == indent;
    }

    AbstractJType getThisType() {
        return thisTypeStack.peek();
    }

    void pushThisType(final AbstractJType thisType) {
        thisTypeStack.push(thisType);
    }

    void popThisType(final AbstractJType thisType) {
        final AbstractJType pop = thisTypeStack.pop();
        assert pop == thisType;
    }

    void setClassFile(final ImplJSourceFile classFile) {
        this.classFile = classFile;
    }

    Token getState() {
        return state;
    }

    ImplJSourceFile getClassFile() {
        return classFile;
    }

    FormatPreferences getFormat() {
        return format;
    }
}
