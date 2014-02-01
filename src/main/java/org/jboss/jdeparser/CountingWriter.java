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

    private final Writer out;
    private int line = 1;
    private int column = 1;

    private final char nl = '\n';

    CountingWriter(final Writer out) {
        this.out = out;
    }

    public void write(final char[] chars, final int off, final int len) throws IOException {
        int line = this.line, column = this.column;
        try {
            for (int i = 0; i < len; i ++) {
                if (chars[off + i] == nl) {
                    line ++;
                    column = 1;
                } else {
                    column ++;
                }
            }
        } finally {
            this.line = line;
            this.column = column;
        }
        out.write(chars, off, len);
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void close() throws IOException {
        out.close();
    }

    int getLine() {
        return line;
    }

    int getColumn() {
        return column;
    }
}
