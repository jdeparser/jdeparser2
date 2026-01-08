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

import static org.jboss.jdeparser.Tokens.$PUNCT;

import java.io.IOException;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class NestedJType extends AbstractJType {

    private final AbstractJType enclosingType;
    private final String name;
    private StaticRefJExpr classExpr;
    private StaticRefJExpr thisExpr;
    private StaticRefJExpr superExpr;
    private CachingLinkedHashMap<String, NestedJType> nestedTypes;

    NestedJType(final AbstractJType enclosingType, final String name) {
        this.enclosingType = enclosingType;
        this.name = name;
    }

    @Override
    String qualifiedName() {
        return enclosingType.qualifiedName() + "." + name;
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
    public JExpr _this() {
        StaticRefJExpr expr = thisExpr;
        if (expr == null) {
            expr = thisExpr = new StaticRefJExpr(this, "this");
        }
        return expr;
    }

    @Override
    public JExpr _super() {
        StaticRefJExpr expr = superExpr;
        if (expr == null) {
            expr = superExpr = new StaticRefJExpr(this, "super");
        }
        return expr;
    }

    @Override
    public JCall _new(final JExpr dim) {
        return new NewJCall(this);
    }

    @Override
    public JAnonymousClassDef _newAnon() {
        return new ImplJAnonymousClassDef(this);
    }

    @Override
    boolean equals(final AbstractJType other) {
        return other instanceof NestedJType njt && equals(njt);
    }

    private boolean equals(final NestedJType other) {
        return enclosingType.equals(other.enclosingType) && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return enclosingType.hashCode() * 17 + name.hashCode();
    }

    @Override
    public String simpleName() {
        return name;
    }

    @Override
    public JType typeArg(final JType... args) {
        return new NarrowedJType(this, args);
    }

    @Override
    public JType nestedType(final String name) {
        CachingLinkedHashMap<String, NestedJType> nestedTypes = this.nestedTypes;
        if (nestedTypes == null) {
            nestedTypes = this.nestedTypes = new CachingLinkedHashMap<>();
        }
        NestedJType nestedType = nestedTypes.get(name);
        if (nestedType == null) {
            nestedTypes.put(name, nestedType = new NestedJType(this, name));
        }
        return nestedType;
    }

    @Override
    public String toString() {
        return "Nested type " + name + " of " + enclosingType;
    }

    @Override
    void writeDirect(final SourceFileWriter writer) throws IOException {
        if (! writer.getClassFile().hasStaticImport(name, enclosingType) && ! writer.getClassFile().hasImport(this)) {
            enclosingType.writeDirect(writer);
            writer.write($PUNCT.DOT);
        }
        writer.writeClass(name);
    }
}
