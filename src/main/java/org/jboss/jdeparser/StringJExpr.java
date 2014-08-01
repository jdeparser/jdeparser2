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

    void writeDirect(final SourceFileWriter writer) throws IOException {
        writer.addWordSpace();
        writer.writeEscaped('"');
        char c;
        for (int i = 0; i < val.length(); i ++) {
            c = val.charAt(i);
            switch (c) {
                case '"':
                case '\\': {
                    writer.writeEscaped('\\');
                    writer.writeEscaped(c);
                    break;
                }
                case '\n': {
                    writer.writeEscaped('\\');
                    writer.writeEscaped('n');
                    break;
                }
                case '\r': {
                    writer.writeEscaped('\\');
                    writer.writeEscaped('\r');
                    break;
                }
                case '\t': {
                    writer.writeEscaped('\\');
                    writer.writeEscaped('\t');
                    break;
                }
                case '\b': {
                    writer.writeEscaped('\\');
                    writer.writeEscaped('\b');
                    break;
                }
                case '\f': {
                    writer.writeEscaped('\\');
                    writer.writeEscaped('\f');
                    break;
                }
                case 0: {
                    writer.writeEscaped('\\');
                    writer.writeEscaped('\0');
                    break;
                }
                default: {
                    writer.writeEscaped(c);
                }
            }
        }
        writer.writeEscaped('"');
    }
}
