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

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
abstract class AbstractJType implements JType {

    private ArrayJType array;
    private WildcardJType wildcardExtends;
    private WildcardJType wildcardSuper;

    static AbstractJType of(JType type) {
        if (type instanceof AbstractJType) {
            return (AbstractJType) type;
        }
        throw new IllegalArgumentException("Using a JType from a different implementation");
    }

    String qualifiedName() {
        return simpleName();
    }

    public abstract int hashCode();

    public final boolean equals(Object other) {
        return other instanceof AbstractJType && equals((AbstractJType) other);
    }

    abstract boolean equals(AbstractJType other);

    public abstract String simpleName();

    public abstract String toString();

    public JExpr _class() {
        throw new UnsupportedOperationException("Adding .class to " + this);
    }

    public JExpr _this() {
        throw new UnsupportedOperationException("Adding .this to " + this);
    }

    public JExpr _super() {
        throw new UnsupportedOperationException("Adding .super to " + this);
    }

    public JType array() {
        if (array == null) {
            array = new ArrayJType(this);
        }
        return array;
    }

    public JCall _new() {
        throw new UnsupportedOperationException("Instantiating a " + this + " as a class");
    }

    public JExpr _new(final JExpr dim) {
        throw new UnsupportedOperationException("Instantiating a " + this + " as an array");
    }

    public JExpr _new(final int dim) {
        return _new(JExprs.decimal(dim));
    }

    public JArrayExpr _newArray() {
        throw new UnsupportedOperationException("Instantiating a " + this + " as an array");
    }

    public JAnonymousClassDef _newAnon() {
        throw new UnsupportedOperationException("Instantiating a " + this + " as a class");
    }

    public JType typeArg(final String... args) {
        final JType[] types = new JType[args.length];
        for (int i = 0, argsLength = args.length; i < argsLength; i++) {
            types[i] = JTypes.typeNamed(args[i]);
        }
        return typeArg(types);
    }

    public JType typeArg(final JType... args) {
        throw new UnsupportedOperationException("Adding type arguments to " + this);
    }

    public JType typeArg(final Class<?>... args) {
        final JType[] types = new JType[args.length];
        for (int i = 0, argsLength = args.length; i < argsLength; i++) {
            types[i] = JTypes.typeOf(args[i]);
        }
        return typeArg(types);
    }

    public JType[] typeArgs() {
        return NONE;
    }

    public JType erasure() {
        return this;
    }

    public JType elementType() {
        throw new UnsupportedOperationException("Getting element type from " + this);
    }

    public JType box() {
        return this;
    }

    public JType unbox() {
        return this;
    }

    public JType wildcardExtends() {
        if (wildcardExtends == null) {
            wildcardExtends = new WildcardJType(this, true);
        }
        return wildcardExtends;
    }

    public JType wildcardSuper() {
        if (wildcardSuper == null) {
            wildcardSuper = new WildcardJType(this, false);
        }
        return wildcardSuper;
    }

    public JType nestedType(final String name) {
        throw new UnsupportedOperationException("Lookup of nested type on " + this);
    }

    public JType $t(final String name) {
        return nestedType(name);
    }

    private CachingLinkedHashMap<String, JAssignableExpr> staticRefs;

    public JAssignableExpr field(final String name) {
        CachingLinkedHashMap<String, JAssignableExpr> map = staticRefs;
        if (map == null) {
            map = staticRefs = new CachingLinkedHashMap<>();
        }
        JAssignableExpr expr = map.get(name);
        if (expr == null) {
            map.put(name, expr = new StaticRefJExpr(this, name));
        }
        return expr;
    }

    public JAssignableExpr $v(final String name) {
        return field(name);
    }

    public JCall call(final String name) {
        return new StaticJCall(this, name);
    }

    public JCall call(final ExecutableElement method) {
        final ElementKind kind = method.getKind();
        if (kind == ElementKind.METHOD && ! method.getModifiers().contains(Modifier.STATIC)) {
            final String name = method.getSimpleName().toString();
            return call(name);
        }
        throw new IllegalArgumentException("Unsupported element for call: " + method);
    }

    public JExpr methodRef(final String name) {
        return new MethodRefJExpr(this, name);
    }

    public JExpr methodRef(final ExecutableElement method) {
        final ElementKind kind = method.getKind();
        if (kind == ElementKind.METHOD && ! method.getModifiers().contains(Modifier.STATIC)) {
            final String name = method.getSimpleName().toString();
            return methodRef(name);
        }
        throw new IllegalArgumentException("Unsupported element for method ref: " + method);
    }

    abstract void writeDirect(SourceFileWriter sourceFileWriter) throws IOException;
}
