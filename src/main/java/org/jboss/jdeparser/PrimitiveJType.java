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
class PrimitiveJType extends AbstractJType {
    private final String simpleName;
    private final ReferenceJType boxed;
    private StaticRefJExpr classExpr;

    PrimitiveJType(final String simpleName, final String boxed) {
        this.simpleName = simpleName;
        this.boxed = boxed == null ? null : new ReferenceJType("java.lang", boxed, this);
    }

    @Override
    public JType box() {
        return boxed;
    }

    @Override
    void writeDirect(final SourceFileWriter sourceFileWriter) throws IOException {
        sourceFileWriter.write(switch (simpleName) {
            case "boolean" -> $KW.BOOLEAN;
            case "byte" -> $KW.BYTE;
            case "short" -> $KW.SHORT;
            case "int" -> $KW.INT;
            case "long" -> $KW.LONG;
            case "char" -> $KW.CHAR;
            case "float" -> $KW.FLOAT;
            case "double" -> $KW.DOUBLE;
            case "void" -> $KW.VOID;
            default -> throw new IllegalStateException();
        });
    }

    @Override
    public JExpr _class() {
        StaticRefJExpr expr = classExpr;
        if (expr == null) {
            expr = classExpr = new StaticRefJExpr(this, "class");
        }
        return expr;
    }

    @Override
    boolean equals(final AbstractJType other) {
        return other instanceof PrimitiveJType pt && simpleName.equals(pt.simpleName);
    }

    @Override
    public int hashCode() {
        return simpleName.hashCode();
    }

    @Override
    public String simpleName() {
        return simpleName;
    }

    @Override
    public String toString() {
        return simpleName;
    }
}
