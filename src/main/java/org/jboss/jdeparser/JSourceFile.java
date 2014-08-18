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
 * A source file.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JSourceFile extends JCommentable {

    /**
     * Add a type import to this source file.
     *
     * @param type the type to import
     * @return this source file
     */
    JSourceFile _import(String type);

    /**
     * Add a type import to this source file.
     *
     * @param type the type to import
     * @return this source file
     */
    JSourceFile _import(JType type);

    /**
     * Add a type import to this source file.
     *
     * @param type the type to import
     * @return this source file
     */
    JSourceFile _import(Class<?> type);

    /**
     * Add a static member import to this source file.
     *
     * @param type the type to import from
     * @param member the member name
     * @return this source file
     */
    JSourceFile importStatic(String type, String member);

    /**
     * Add a static member import to this source file.
     *
     * @param type the type to import from
     * @param member the member name
     * @return this source file
     */
    JSourceFile importStatic(JType type, String member);

    /**
     * Add a static member import to this source file.
     *
     * @param type the type to import from
     * @param member the member name
     * @return this source file
     */
    JSourceFile importStatic(Class<?> type, String member);

    /**
     * Add a blank line to this source file.  If sorting is enabled, blank lines may be lost.
     *
     * @return this source file
     */
    JSourceFile blankLine();

    /**
     * Add a class definition to this source file.
     *
     * @param mods the modifiers
     * @param name the class name
     * @return the class definition
     */
    JClassDef _class(int mods, String name);

    /**
     * Add an enum definition to this source file.
     *
     * @param mods the modifiers
     * @param name the enum name
     * @return the enum definition
     */
    JClassDef _enum(int mods, String name);

    /**
     * Add an interface definition to this source file.
     *
     * @param mods the modifiers
     * @param name the interface name
     * @return the interface definition
     */
    JClassDef _interface(int mods, String name);

    /**
     * Add an annotation interface definition to this source file.
     *
     * @param mods the modifiers
     * @param name the annotation interface name
     * @return the annotation interface definition
     */
    JClassDef annotationInterface(int mods, String name);
}
