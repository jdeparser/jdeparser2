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

import java.io.IOException;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class NewDimJArrayExpr extends AbstractJExpr {

    private final ArrayJType type;
    private final AbstractJExpr dim;

    NewDimJArrayExpr(final ArrayJType type, final AbstractJExpr dim) {
        super(Prec.METHOD_CALL);
        this.type = type;
        this.dim = dim;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writer.write($KW.NEW);
        writer.write(type.elementType());
        writer.write($PUNCT.BRACKET.OPEN);
        writer.write(FormatPreferences.Space.WITHIN_BRACKETS);
        writer.write(dim);
        writer.write(FormatPreferences.Space.WITHIN_BRACKETS);
        writer.write($PUNCT.BRACKET.CLOSE);
    }

    public String toString() {
        return "new " + type.elementType() + "[" + dim + "]";
    }
}
