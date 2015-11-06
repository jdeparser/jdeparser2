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

    public JType box() {
        return boxed;
    }

    void writeDirect(final SourceFileWriter sourceFileWriter) throws IOException {
        switch (simpleName) {
            case "boolean": sourceFileWriter.write($KW.BOOLEAN); return;
            case "byte": sourceFileWriter.write($KW.BYTE); return;
            case "short": sourceFileWriter.write($KW.SHORT); return;
            case "int": sourceFileWriter.write($KW.INT); return;
            case "long": sourceFileWriter.write($KW.LONG); return;
            case "char": sourceFileWriter.write($KW.CHAR); return;
            case "float": sourceFileWriter.write($KW.FLOAT); return;
            case "double": sourceFileWriter.write($KW.DOUBLE); return;
            case "void": sourceFileWriter.write($KW.VOID); return;
            default: throw new IllegalStateException();
        }
    }

    public JExpr _class() {
        StaticRefJExpr expr = classExpr;
        if (expr == null) {
            expr = classExpr = new StaticRefJExpr(this, "class");
        }
        return expr;
    }

    boolean equals(final AbstractJType other) {
        return other instanceof PrimitiveJType && simpleName.equals(((PrimitiveJType) other).simpleName);
    }

    public int hashCode() {
        return simpleName.hashCode();
    }

    public String simpleName() {
        return simpleName;
    }

    public String toString() {
        return simpleName;
    }
}
