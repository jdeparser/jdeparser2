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

/**
 * A type definition, which can be a class, interface, annotation type, etc.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JClassDef extends JAnnotatable, JDocCommentable, JGenericDef {

    /**
     * Add an {@code extends} type to this type.
     *
     * @param name the type name
     * @return this type definition
     */
    JClassDef _extends(String name);

    /**
     * Add an {@code extends} type to this type.
     *
     * @param type the type
     * @return this type definition
     */
    JClassDef _extends(JType type);

    /**
     * Add an {@code extends} type to this type.
     *
     * @param clazz the type
     * @return this type definition
     */
    JClassDef _extends(Class<?> clazz);

    /**
     * Add one or more {@code implements} type(s) to this type.
     *
     * @param name the type name
     * @return this type definition
     */
    JClassDef _implements(String... name);

    /**
     * Add one or more {@code implements} type(s) to this type.
     *
     * @param type the type
     * @return this type definition
     */
    JClassDef _implements(JType... type);

    /**
     * Add one or more {@code implements} type(s) to this type.
     *
     * @param clazz the type
     * @return this type definition
     */
    JClassDef _implements(Class<?>... clazz);

    /**
     * Add a blank line at this point of the type.  Blank lines are lost if members are sorted.
     *
     * @return this type definition
     */
    JClassDef blankLine();

    /**
     * Get the erased type corresponding to this type definition.
     *
     * @return the erased type
     */
    JType erasedType();

    /**
     * Get a generic type for this type definition, where the type arguments are the same as the type parameters of this
     * type (as defined at the time this method is called).
     *
     * @return the generic type
     */
    JType genericType();

    /**
     * Add a "raw" initialization block to this type definition.
     *
     * @return the initialization block
     */
    JBlock init();

    /**
     * Add a static initialization block to this type definition.
     *
     * @return the static initialization block
     */
    JBlock staticInit();

    /**
     * Add an enum constant.  If the class being defined is not an enum, an exception is thrown.
     *
     * @param name the constant name
     * @return the call for enum construction
     */
    JEnumConstant _enum(String name);

    /**
     * Add a field to this type.
     *
     * @param mods the modifiers
     * @param type the field type
     * @param name the field name
     * @return the field declaration
     */
    JVarDeclaration field(int mods, JType type, String name);

    /**
     * Add a field to this type.
     *
     * @param mods the modifiers
     * @param type the field type
     * @param name the field name
     * @param init the field assigned value
     * @return the field declaration
     */
    JVarDeclaration field(int mods, JType type, String name, JExpr init);

    /**
     * Add a field to this type.
     *
     * @param mods the modifiers
     * @param type the field type
     * @param name the field name
     * @return the field declaration
     */
    JVarDeclaration field(int mods, Class<?> type, String name);

    /**
     * Add a field to this type.
     *
     * @param mods the modifiers
     * @param type the field type
     * @param name the field name
     * @param init the field assigned value
     * @return the field declaration
     */
    JVarDeclaration field(int mods, Class<?> type, String name, JExpr init);

    /**
     * Add a field to this type.
     *
     * @param mods the modifiers
     * @param type the field type
     * @param name the field name
     * @return the field declaration
     */
    JVarDeclaration field(int mods, String type, String name);

    /**
     * Add a field to this type.
     *
     * @param mods the modifiers
     * @param type the field type
     * @param name the field name
     * @param init the field assigned value
     * @return the field declaration
     */
    JVarDeclaration field(int mods, String type, String name, JExpr init);

    /**
     * Add a method to this type.
     *
     * @param mods the modifiers
     * @param returnType the method return type
     * @param name the method name
     * @return the method definition
     */
    JMethodDef method(int mods, JType returnType, String name);

    /**
     * Add a method to this type.
     *
     * @param mods the modifiers
     * @param returnType the method return type
     * @param name the method name
     * @return the method definition
     */
    JMethodDef method(int mods, Class<?> returnType, String name);

    /**
     * Add a method to this type.
     *
     * @param mods the modifiers
     * @param returnType the method return type
     * @param name the method name
     * @return the method definition
     */
    JMethodDef method(int mods, String returnType, String name);

    /**
     * Add a constructor to this type.
     *
     * @param mods the modifiers
     * @return the constructor definition
     */
    JMethodDef constructor(int mods);

    /**
     * Add a nested class to this type.
     *
     * @param mods the class modifiers
     * @param name the class name
     * @return the nested class
     */
    JClassDef _class(int mods, String name);

    /**
     * Add a nested enum to this type.
     *
     * @param mods the enum modifiers
     * @param name the enum name
     * @return the nested enum
     */
    JClassDef _enum(int mods, String name);

    /**
     * Add a nested interface to this type.
     *
     * @param mods the interface modifiers
     * @param name the interface name
     * @return the nested interface
     */
    JClassDef _interface(int mods, String name);

    /**
     * Add a nested annotation interface to this type.
     *
     * @param mods the annotation interface modifiers
     * @param name the annotation interface name
     * @return the nested annotation interface
     */
    JClassDef annotationInterface(int mods, String name);
}
