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

import java.io.IOException;
import java.io.Writer;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class CountingWriter extends Writer {

    private final char[] buffer = new char[4096];
    private final Writer out;
    private int line = 1;
    private int column = 1;
    private int bsz;

    CountingWriter(final Writer out) {
        this.out = out;
    }

    public void write(final char[] chars, final int off, final int len) throws IOException {
        int line = this.line, column = this.column, bsz = this.bsz;
        final char[] buffer = this.buffer;
        try {
            char ch;
            for (int i = 0; i < len; i ++) {
                ch = chars[off + i];
                if (ch == '\n') {
                    line++;
                    column = 1;
                } else if (ch == '\r') {
                    column = 1;
                } else {
                    column ++;
                }
                buffer[bsz++] = ch;
                if (bsz == buffer.length) {
                    out.write(buffer, 0, bsz);
                    bsz = 0;
                }
            }
        } finally {
            this.line = line;
            this.column = column;
            this.bsz = bsz;
        }
    }

    public void write(final int c) throws IOException {
        if (c == '\n') {
            line ++;
            column = 1;
        } else {
            column ++;
        }
        buffer[bsz++] = (char) c;
        if (bsz == buffer.length) {
            out.write(buffer, 0, bsz);
            bsz = 0;
        }
    }

    public void write(final char[] cbuf) throws IOException {
        write(cbuf, 0, cbuf.length);
    }

    public void write(final String str, final int off, final int len) throws IOException {
        int line = this.line, column = this.column, bsz = this.bsz;
        final char[] buffer = this.buffer;
        try {
            char ch;
            for (int i = 0; i < len; i ++) {
                ch = str.charAt(off + i);
                if (ch == '\n') {
                    line ++;
                    column = 1;
                } else if (ch == '\r') {
                    column = 1;
                } else {
                    column ++;
                }
                buffer[bsz++] = ch;
                if (bsz == buffer.length) {
                    out.write(buffer, 0, bsz);
                    bsz = 0;
                }
            }
        } finally {
            this.line = line;
            this.column = column;
            this.bsz = bsz;
        }
    }

    public void write(final String str) throws IOException {
        write(str, 0, str.length());
    }

    public void write(final StringBuilder b, final int off, final int len) throws IOException {
        int line = this.line, column = this.column, bsz = this.bsz;
        final char[] buffer = this.buffer;
        try {
            char ch;
            for (int i = 0; i < len; i ++) {
                ch = b.charAt(off + i);
                if (ch == '\n') {
                    line ++;
                    column = 1;
                } else if (ch == '\r') {
                    column = 1;
                } else {
                    column ++;
                }
                buffer[bsz++] = ch;
                if (bsz == buffer.length) {
                    out.write(buffer, 0, bsz);
                    bsz = 0;
                }
            }
        } finally {
            this.line = line;
            this.column = column;
            this.bsz = bsz;
        }
    }

    public void write(final StringBuilder b) throws IOException {
        write(b, 0, b.length());
    }

    public void flush() throws IOException {
        int bsz = this.bsz;
        this.bsz = 0;
        out.write(buffer, 0, bsz);
        out.flush();
    }

    public void close() throws IOException {
        int bsz = this.bsz;
        this.bsz = 0;
        out.write(buffer, 0, bsz);
        out.close();
    }

    int getLine() {
        return line;
    }

    int getColumn() {
        return column;
    }
}
