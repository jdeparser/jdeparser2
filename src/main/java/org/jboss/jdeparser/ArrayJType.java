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
class ArrayJType extends AbstractJType {

    private final AbstractJType elementType;
    private StaticRefJExpr classExpr;

    ArrayJType(final AbstractJType elementType) {
        this.elementType = elementType;
    }

    public JType elementType() {
        return elementType;
    }

    void writeDirect(final SourceFileWriter sourceFileWriter) throws IOException {
        sourceFileWriter.write(elementType);
        sourceFileWriter.write($PUNCT.BRACKET.OPEN);
        sourceFileWriter.write($PUNCT.BRACKET.CLOSE);
    }

    String qualifiedName() {
        return elementType.qualifiedName();
    }

    public int hashCode() {
        return elementType.hashCode() ^ 0xee55ee55 + 7;
    }

    boolean equals(final AbstractJType other) {
        return other instanceof ArrayJType && elementType.equals(((ArrayJType) other).elementType);
    }

    public JExpr _new(final JExpr dim) {
        return new NewDimJArrayExpr(this, AbstractJExpr.of(dim));
    }

    public JArrayExpr _newArray() {
        return new NewUndimJArrayExpr(this);
    }

    public String simpleName() {
        return elementType.simpleName();
    }

    public String toString() {
        return elementType.toString() + "[]";
    }

    public JExpr _class() {
        StaticRefJExpr expr = classExpr;
        if (expr == null) {
            expr = classExpr = new StaticRefJExpr(this, "class");
        }
        return expr;
    }
}
