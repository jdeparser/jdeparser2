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

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class StringJExpr extends AbstractJExpr {

    private final String val;

    StringJExpr(final String val) {
        super(0);
        this.val = val;
    }

    static final Indent ESC = new Indent() {
        public void addIndent(final Indent next, final FormatPreferences preferences, final StringBuilder lineBuffer) {
            next.addIndent(next, preferences, lineBuffer);
            lineBuffer.append('"');
        }

        public void escape(final Indent next, final StringBuilder b, final int idx) {
            char c;
            int i = idx;
            while (i < b.length()) {
                c = b.charAt(i);
                switch (c) {
                    case '"':
                    case '\\': {
                        b.insert(i, '\\');
                        i += 2;
                        break;
                    }
                    case '\n': {
                        b.replace(i, i + 1, "\\n");
                        i += 2;
                        break;
                    }
                    case '\r': {
                        b.replace(i, i + 1, "\\r");
                        i += 2;
                        break;
                    }
                    case '\t': {
                        b.replace(i, i + 1, "\\t");
                        i += 2;
                        break;
                    }
                    case '\b': {
                        b.replace(i, i + 1, "\\b");
                        i += 2;
                        break;
                    }
                    case '\f': {
                        b.replace(i, i + 1, "\\f");
                        i += 2;
                        break;
                    }
                    case 0: {
                        b.replace(i, i + 1, "\\0");
                        i += 2;
                        break;
                    }
                    default: {
                        i++;
                    }
                }
            }
        }

        public void unescaped(final Indent next, final StringBuilder b, final int idx) {
            // next is escaped
            next.escape(next, b, idx);
        }
    };

    public void write(final SourceFileWriter writer) throws IOException {
        writer.addWordSpace();
        writer.writeEscaped('"');
        writer.pushIndent(ESC);
        try {
            writer.writeEscaped(val);
        } finally {
            writer.popIndent(ESC);
        }
        writer.writeEscaped('"');
    }
}
