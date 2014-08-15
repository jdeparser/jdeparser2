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

import static org.jboss.jdeparser.FormatPreferences.Space;
import static org.jboss.jdeparser.Tokens.$KW;

class NewUndimJArrayExpr extends PlainJArrayExpr {
    private final ArrayJType type;

    NewUndimJArrayExpr(final ArrayJType type) {
        this.type = type;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writer.write($KW.NEW);
        writer.write(type);
        writer.write(Space.BEFORE_BRACE_ARRAY_INIT);
        super.write(writer);
    }
}
