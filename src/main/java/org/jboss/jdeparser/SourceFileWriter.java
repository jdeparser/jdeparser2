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
import java.util.Arrays;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class SourceFileWriter implements Flushable, Closeable {

    private static final char[] SPACES;

    static {
        char[] spaces = new char[128];
        Arrays.fill(spaces, ' ');
        SPACES = spaces;
    }

    private final FormatPreferences format;
    private final CountingWriter writer;
    private final String lineSep;
    private final ArrayDeque<AbstractJType> thisTypeStack = new ArrayDeque<>();
    private final ArrayDeque<FormatPreferences.Indentation> indentStack = new ArrayDeque<>();
    private Token state = $START;
    private boolean addedSpace;
    private boolean addedNewLine;

    SourceFileWriter(final FormatPreferences format, final Writer writer) {
        this.format = format;
        this.writer = new CountingWriter(writer);
        // todo use preferences/config
        lineSep = System.lineSeparator();
    }

    void nl() throws IOException {
        writer.write(lineSep);
        addedSpace = true;
        addedNewLine = true;
    }

    /**
     * Force a space if one hasn't already been added.
     *
     * @throws IOException etc.
     */
    void sp() throws IOException {
        if (! addedSpace) {
            assert ! addedNewLine;
            addedSpace = true;
            writer.write(' ');
        }
    }

    int getLine() {
        return writer.getLine();
    }

    int getColumn() {
        return writer.getColumn();
    }

    private void addIndent() throws IOException {
        assert addedNewLine;
        assert addedSpace;
        int total = 0;
        for (FormatPreferences.Indentation indentation : indentStack) {
            if (indentation == null) {
                // no-indent states
                continue;
            }
            final int i = format.getIndent(indentation);
            if (format.isIndentAbsolute(indentation)) {
                total = i;
            } else {
                total += i;
            }
        }
        if (total > 0) {
            while (total > 128) {
                writer.write(SPACES, 0, 128);
                total -= 128;
            }
            writer.write(SPACES, 0, total);
        }
        addedNewLine = false;
        addedSpace = false;
    }

    void write(FormatPreferences.Space rule) throws IOException {
        if (rule == null) {
            return;
        }
        if (addedNewLine) {
            addIndent();
        }
        if (format.getSpaceType(rule) == FormatPreferences.SpaceType.NEWLINE) {
            if (! addedNewLine) {
                nl();
            }
        } else if (format.getSpaceType(rule) == FormatPreferences.SpaceType.SPACE) {
            if (! addedSpace) {
                sp();
            }
        }
    }

    void writeClass(final String nameToWrite) throws IOException {
        if (addedNewLine) {
            addIndent();
        } else if (state instanceof $KW || state == $WORD || state == $NUMBER) {
            sp();
        }
        writer.write(nameToWrite);
        this.state = $WORD;
        addedSpace = addedNewLine = false;
    }

    void write(final Token state) throws IOException {
        if (addedNewLine) {
            addIndent();
        }
        state.write(this);
        this.state = state;
    }

    void writeRaw(final char ch) throws IOException {
        assert ! Character.isWhitespace(ch);
        writer.write(ch);
        addedSpace = addedNewLine = false;
    }

    void writeRaw(final String rawText) throws IOException {
        // todo not comprehensive
        assert rawText.indexOf(' ') == -1 && rawText.indexOf('\r') == -1 && rawText.indexOf('\n') == -1;
        writer.write(rawText);
        addedNewLine = addedSpace = false;
    }

    void writeRawWord(final String rawText) throws IOException {
        if (addedNewLine) {
            addIndent();
        }
        writeRaw(rawText);
    }

    public void flush() throws IOException {
        writer.flush();
    }

    public void close() throws IOException {
        writer.close();
    }

    void write(final JType type) throws IOException {
        if (addedNewLine) {
            addIndent();
        }
        if (type != null) AbstractJType.of(type).writeDirect(this);
    }

    void write(final AbstractJType type) throws IOException {
        if (addedNewLine) {
            addIndent();
        }
        if (type != null) type.writeDirect(this);
    }

    void write(final JExpr expr) throws IOException {
        if (addedNewLine) {
            addIndent();
        }
        if (expr != null) AbstractJExpr.of(expr).writeDirect(this);
    }

    void write(final AbstractJExpr expr) throws IOException {
        if (addedNewLine) {
            addIndent();
        }
        if (expr != null) expr.writeDirect(this);
    }

    void pushIndent(FormatPreferences.Indentation indent) {
        indentStack.push(indent);
    }

    void popIndent(FormatPreferences.Indentation indent) {
        final FormatPreferences.Indentation pop = indentStack.pop();
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

    Token getState() {
        return state;
    }
}
