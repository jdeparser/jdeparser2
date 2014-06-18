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
        writer.writeRaw('"');
        char c;
        for (int i = 0; i < val.length(); i ++) {
            c = val.charAt(i);
            switch (c) {
                case '"':
                case '\\': {
                    writer.writeRaw('\\');
                    writer.writeRaw(c);
                    break;
                }
                case '\n': {
                    writer.writeRaw('\\');
                    writer.writeRaw('n');
                    break;
                }
                case '\r': {
                    writer.writeRaw('\\');
                    writer.writeRaw('\r');
                    break;
                }
                case '\t': {
                    writer.writeRaw('\\');
                    writer.writeRaw('\t');
                    break;
                }
                case '\b': {
                    writer.writeRaw('\\');
                    writer.writeRaw('\b');
                    break;
                }
                case '\f': {
                    writer.writeRaw('\\');
                    writer.writeRaw('\f');
                    break;
                }
                case 0: {
                    writer.writeRaw('\\');
                    writer.writeRaw('\0');
                    break;
                }
                default: {
                    writer.writeRaw(c);
                }
            }
        }
        writer.writeRaw('"');
    }
}
