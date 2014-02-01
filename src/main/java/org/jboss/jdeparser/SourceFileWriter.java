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

import static org.jboss.jdeparser.FormatStates.*;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class SourceFileWriter implements Flushable, Closeable {

    private final FormatPreferences format;
    private final CountingWriter writer;
    private final String lineSep;
    private final ArrayDeque<FormatStateContext> stateStack = new ArrayDeque<>();
    private final ArrayDeque<AbstractJType> thisTypeStack = new ArrayDeque<>();
    private FormatState state = $START;

    SourceFileWriter(final FormatPreferences format, final Writer writer) {
        this.format = format;
        this.writer = new CountingWriter(writer);
        // todo use preferences/config
        lineSep = PLATFORM_LINE_SEP;
    }

    private int indent = 0;

    private static final String PLATFORM_LINE_SEP = String.format("%n");

    void nl() throws IOException {
        writer.write(lineSep);
    }

    int getIndent() {
        return indent;
    }

    int getLine() {
        return writer.getLine();
    }

    int getColumn() {
        return writer.getColumn();
    }

    private void doIndent(int column) throws IOException {
        for (int i = this.getColumn(); i < column; i ++) {
            write(' ');
        }
    }

    private void doIndent() throws IOException {
        if (state.needsIndentBefore(this)) {
            doIndent(indent * format.getIndentSize());
        }
    }

    private void write(final char ch) throws IOException {
        writer.write(ch);
    }

    void writeClass(final String nameToWrite) throws IOException {
        doIndent();
        writer.write(nameToWrite);

    }

    void write(final FormatState state) throws IOException {
        doIndent();
        state.write(this);
        this.state = state;
//        if (state.needsNewLine(this)) {
//            nl();
//        }
    }

    void writeRaw(final char ch) throws IOException {
        writer.write(ch);
    }

    void writeIdentifier(final String rawText) throws IOException {
        writer.write(rawText);
    }

    public void flush() throws IOException {
        writer.flush();
    }

    public void close() throws IOException {
        writer.close();
    }

    void write(final JType type) throws IOException {
        if (type != null) AbstractJType.of(type).write(this);
    }

    void write(final AbstractJType type) throws IOException {
        if (type != null) type.write(this);
    }

    void write(final JExpr expr) throws IOException {
        if (expr != null) AbstractJExpr.of(expr).write(this);
    }

    void write(final AbstractJExpr expr) throws IOException {
        if (expr != null) expr.write(this);
    }

    void pushStateContext(final FormatStateContext stateContext) {
        stateStack.push(stateContext);
        indent ++;
    }

    void popStateContext(final FormatStateContext expected) {
        final FormatStateContext pop = stateStack.pop();
        assert pop == expected;
        indent --;
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
}
