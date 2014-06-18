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

import static org.jboss.jdeparser.FormatPreferences.Space;
import static org.jboss.jdeparser.Tokens.*;

import java.io.IOException;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ArrayJExpr extends AbstractJExpr implements JExpr {

    private final JExpr[] members;

    ArrayJExpr(final JExpr... members) {
        super(0);
        this.members = members;
    }

    void writeDirect(final SourceFileWriter writer) throws IOException {
        writer.write($PUNCT.BRACE.OPEN);
        writer.write(Space.WITHIN_BRACES_ARRAY_INIT);
        final JExpr[] members = this.members;
        final int len = members.length;
        if (len > 0) {
            writer.write(members[0]);
            for (int i = 1; i < len; i ++) {
                writer.write($PUNCT.COMMA);
                writer.write(members[i]);
            }
        }
        writer.write(Space.WITHIN_BRACES_ARRAY_INIT);
        writer.write($PUNCT.BRACE.CLOSE);
    }
}
