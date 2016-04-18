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
class ReferenceJType extends AbstractJType {

    private final PrimitiveJType unboxed;
    private final String packageName;
    private final String simpleName;
    private StaticRefJExpr classExpr;
    private StaticRefJExpr thisExpr;
    private StaticRefJExpr superExpr;
    private CachingLinkedHashMap<String, NestedJType> nestedTypes;

    ReferenceJType(final String packageName, final String simpleName) {
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.unboxed = null;
    }

    ReferenceJType(final String packageName, final String simpleName, final PrimitiveJType unboxed) {
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.unboxed = unboxed;
    }

    static ReferenceJType of(JType type) {
        final AbstractJType type1 = AbstractJType.of(type);
        if (type1 instanceof ReferenceJType) {
            return (ReferenceJType) type1;
        }
        throw new IllegalArgumentException("Expected a reference type");
    }

    @Override
    String qualifiedName() {
        if (packageName.isEmpty()) {
            return simpleName;
        }
        return packageName + "." + simpleName;
    }

    public String simpleName() {
        return simpleName;
    }

    public JExpr _class() {
        StaticRefJExpr expr = classExpr;
        if (expr == null) {
            expr = classExpr = new StaticRefJExpr(this, "class");
        }
        return expr;
    }

    public JExpr _this() {
        StaticRefJExpr expr = thisExpr;
        if (expr == null) {
            expr = thisExpr = new StaticRefJExpr(this, "this");
        }
        return expr;
    }

    public JExpr _super() {
        StaticRefJExpr expr = superExpr;
        if (expr == null) {
            expr = superExpr = new StaticRefJExpr(this, "super");
        }
        return expr;
    }

    public JCall _new() {
        return new NewJCall(this);
    }

    public JAnonymousClassDef _newAnon() {
        return new ImplJAnonymousClassDef(this);
    }

    public JType unbox() {
        return unboxed == null ? this : unboxed;
    }

    void writeDirect(final SourceFileWriter sourceFileWriter) throws IOException {
        final ImplJSourceFile cf = sourceFileWriter.getClassFile();
        final String currentPackageName = cf.getPackageName();
        final boolean packageMatches = currentPackageName.equals(packageName);
        if (packageMatches && cf.hasImport(simpleName())) {
            // an explicit import masks the implicit import
            sourceFileWriter.writeClass(qualifiedName());
        } else if (packageName.equals("java.lang") && ! sourceFileWriter.getClassFile().getSources().hasClass(currentPackageName + "." + simpleName()) || packageMatches) {
            // implicit import
            sourceFileWriter.writeClass(simpleName());
        } else if (cf.hasImport(this)) {
            // explicit import
            sourceFileWriter.writeClass(simpleName());
        } else {
            sourceFileWriter.writeClass(qualifiedName());
        }
    }

    public JType typeArg(final JType... args) {
        if (unboxed != null) return super.typeArg(args);
        return new NarrowedJType(this, args);
    }

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

    boolean equals(final AbstractJType other) {
        return other instanceof ReferenceJType && equals((ReferenceJType) other);
    }

    private boolean equals(final ReferenceJType other) {
        return packageName.equals(other.packageName) && simpleName.equals(other.simpleName);
    }

    public int hashCode() {
        return packageName.hashCode() * 17 + simpleName.hashCode();
    }

    public String toString() {
        return "Reference of type " + simpleName();
    }
}
