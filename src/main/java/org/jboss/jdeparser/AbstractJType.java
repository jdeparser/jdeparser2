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

    @Override
    public abstract int hashCode();

    @Override
    public final boolean equals(Object other) {
        return other instanceof AbstractJType && equals((AbstractJType) other);
    }

    abstract boolean equals(AbstractJType other);

    @Override
    public abstract String simpleName();

    @Override
    public abstract String toString();

    @Override
    public JExpr _class() {
        throw new UnsupportedOperationException("Adding .class to " + this);
    }

    @Override
    public JExpr _this() {
        throw new UnsupportedOperationException("Adding .this to " + this);
    }

    @Override
    public JExpr _super() {
        throw new UnsupportedOperationException("Adding .super to " + this);
    }

    @Override
    public JType array() {
        if (array == null) {
            array = new ArrayJType(this);
        }
        return array;
    }

    @Override
    public JCall _new() {
        throw new UnsupportedOperationException("Instantiating a " + this + " as a class");
    }

    @Override
    public JExpr _new(final JExpr dim) {
        throw new UnsupportedOperationException("Instantiating a " + this + " as an array");
    }

    @Override
    public JExpr _new(final int dim) {
        return _new(JExprs.decimal(dim));
    }

    @Override
    public JArrayExpr _newArray() {
        throw new UnsupportedOperationException("Instantiating a " + this + " as an array");
    }

    @Override
    public JAnonymousClassDef _newAnon() {
        throw new UnsupportedOperationException("Instantiating a " + this + " as a class");
    }

    @Override
    public JType typeArg(final String... args) {
        final JType[] types = new JType[args.length];
        for (int i = 0, argsLength = args.length; i < argsLength; i++) {
            types[i] = JTypes.typeNamed(args[i]);
        }
        return typeArg(types);
    }

    @Override
    public JType typeArg(final JType... args) {
        throw new UnsupportedOperationException("Adding type arguments to " + this);
    }

    @Override
    public JType typeArg(final Class<?>... args) {
        final JType[] types = new JType[args.length];
        for (int i = 0, argsLength = args.length; i < argsLength; i++) {
            types[i] = JTypes.typeOf(args[i]);
        }
        return typeArg(types);
    }

    @Override
    public JType[] typeArgs() {
        return NONE;
    }

    @Override
    public JType erasure() {
        return this;
    }

    @Override
    public JType elementType() {
        throw new UnsupportedOperationException("Getting element type from " + this);
    }

    @Override
    public JType box() {
        return this;
    }

    @Override
    public JType unbox() {
        return this;
    }

    @Override
    public JType wildcardExtends() {
        if (wildcardExtends == null) {
            wildcardExtends = new WildcardJType(this, true);
        }
        return wildcardExtends;
    }

    @Override
    public JType wildcardSuper() {
        if (wildcardSuper == null) {
            wildcardSuper = new WildcardJType(this, false);
        }
        return wildcardSuper;
    }

    @Override
    public JType nestedType(final String name) {
        throw new UnsupportedOperationException("Lookup of nested type on " + this);
    }

    @Override
    public JType $t(final String name) {
        return nestedType(name);
    }

    private CachingLinkedHashMap<String, JAssignableExpr> staticRefs;

    @Override
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

    @Override
    public JAssignableExpr field(final CharSequence name) {
        return field(name.toString());
    }

    @Override
    public JAssignableExpr $v(final String name) {
        return field(name);
    }

    @Override
    public JAssignableExpr $v(final CharSequence name) {
        return field(name);
    }

    @Override
    public JCall call(final String name) {
        return new StaticJCall(this, name);
    }

    @Override
    public JCall call(final ExecutableElement method) {
        final ElementKind kind = method.getKind();
        if (kind == ElementKind.METHOD && ! method.getModifiers().contains(Modifier.STATIC)) {
            final String name = method.getSimpleName().toString();
            return call(name);
        }
        throw new IllegalArgumentException("Unsupported element for call: " + method);
    }

    @Override
    public JExpr methodRef(final String name) {
        return new MethodRefJExpr(this, name);
    }

    @Override
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
