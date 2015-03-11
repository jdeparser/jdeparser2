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
public interface JClassDef extends JAnnotatable, JDocCommentable, JGenericDef, JClassDefSection {

    /**
     * Add a blank line at this point of the type.
     *
     * @return this type definition
     */
    JClassDef blankLine();

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
     * Add an enum constant.  If the class being defined is not an enum, an exception is thrown.
     *
     * @param name the constant name
     * @return the call for enum construction
     */
    JEnumConstant _enum(String name);
}
