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

import javax.lang.model.element.ExecutableElement;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JType {

    /**
     * An empty array of types.
     */
    JType[] NONE = new JType[0];

    /**
     * A special type that always renders to the type of the class it is encountered in.
     */
    JType THIS = new ThisJType();

    JType VOID = new PrimitiveJType("void", "Void");

    JType BOOLEAN = new PrimitiveJType("boolean", "Boolean");

    JType FLOAT = new PrimitiveJType("float", "Float");

    JType DOUBLE = new PrimitiveJType("double", "Double");

    JType CHAR = new PrimitiveJType("char", "Char");

    JType BYTE = new PrimitiveJType("byte", "Byte");

    JType SHORT = new PrimitiveJType("short", "Short");

    JType INT = new PrimitiveJType("int", "Integer");

    JType LONG = new PrimitiveJType("long", "Long");

    JType OBJECT = new ReferenceJType("java.lang", "Object");

    JType WILDCARD = OBJECT.wildcardExtends();

    String simpleName();

    /**
     * An expression of the form {@code ThisType.class}.
     *
     * @return the expression
     */
    JExpr _class();

    /**
     * An expression of the form {@code ThisType.this}.  If the type is an array type, an exception is thrown.
     *
     * @return the expression
     */
    JExpr _this();

    /**
     * An expression of the form {@code ThisType.super}.  If the type is an array type, an exception is thrown.
     *
     * @return the expression
     */
    JExpr _super();

    /**
     * An array of this type.
     *
     * @return the array type
     */
    JType array();

    /**
     * Construct a new instance of this non-array type.  If the type is an array type, an exception is thrown.
     *
     * @return the construction call
     */
    JCall _new();

    /**
     * Construct a new instance of this array type.  If the type is not an array type, an exception is thrown.
     *
     * @param dim the array size
     * @return the construction call
     */
    JExpr _new(JExpr dim);

    /**
     * Construct a new instance of this array type.  If the type is not an array type, an exception is thrown.
     *
     * @param dim the array size
     * @return the construction call
     */
    JExpr _new(int dim);

    /**
     * Construct a new anonymous subclass of this type.
     *
     * @return the anonymous subclass definition
     */
    JAnonymousClassDef _newAnon();

    /**
     * This type, with the given generic type arguments.
     *
     * @param args the type arguments
     * @return the generic type
     */
    JType typeArg(String... args);

    /**
     * This type, with the given generic type arguments.
     *
     * @param args the type arguments
     * @return the generic type
     */
    JType typeArg(JType... args);

    /**
     * This type, with the given generic type arguments.
     *
     * @param args the type arguments
     * @return the generic type
     */
    JType typeArg(Class<?>... args);

    /**
     * Get the type arguments of this type.
     *
     * @return the type arguments of this type
     */
    JType[] typeArgs();

    /**
     * The primitive-boxed version of this type.
     *
     * @return the boxed version of this type
     */
    JType box();

    /**
     * The primitive-unboxed version of this type.
     *
     * @return the unboxed version of this type
     */
    JType unbox();

    /**
     * The erasure of this type.
     *
     * @return the erasure of this type
     */
    JType erasure();

    /**
     * The element type, if this an array (otherwise {@code null}).
     *
     * @return the element type, or {@code null} if it is not an array
     */
    JType elementType();

    /**
     * Get a wildcard that extends this type.
     *
     * @return the wildcard
     */
    JType wildcardExtends();

    /**
     * Get a wildcard that this type extends.
     *
     * @return the wildcard
     */
    JType wildcardSuper();

    /**
     * Get a nested type within this reference type.
     *
     * @param name the name of the nested type
     * @return the nested type
     */
    JType nestedType(String name);

    /**
     * Get a nested type within this reference type.
     *
     * @param name the name of the nested type
     * @return the nested type
     */
    JType _(String name);

    /**
     * Look up a static field on this type.
     *
     * @param name the field name
     * @return the field expression
     */
    JAssignableExpr field(String name);

    /**
     * Look up a static field on this type.
     *
     * @param name the field name
     * @return the field expression
     */
    JAssignableExpr $(String name);

    /**
     * Call a static method on this type.
     *
     * @param name the method to call
     * @return the method call
     */
    JCall call(String name);

    /**
     * Call a static method on this type.
     *
     * @param method the method to call
     * @return the method call
     */
    JCall call(ExecutableElement method);

    JExpr methodRef(String name);

    JExpr methodRef(ExecutableElement method);
}
