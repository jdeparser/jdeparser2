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
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JClassDef extends JAnnotatable, JDocCommentable, JInlineCommentable, JGeneric {

    JClassDef _extends(String name);

    JClassDef _extends(JType type);

    JClassDef _extends(Class<?> clazz);

    JClassDef _implements(String... name);

    JClassDef _implements(JType... type);

    JClassDef _implements(Class<?>... clazz);

    JClassDef blankLine();

    JType erasedType();

    JType genericType();

    JBlock init();

    JBlock staticInit();

    /**
     * Add an enum constant.  If the class being defined is not an enum, an exception is thrown.
     *
     * @param name the constant name
     * @return the call for enum construction
     */
    JEnumConstant _enum(String name);

    JVarDeclaration field(int mods, JType type, String name);

    JVarDeclaration field(int mods, JType type, String name, JExpr init);

    JVarDeclaration field(int mods, Class<?> type, String name);

    JVarDeclaration field(int mods, Class<?> type, String name, JExpr init);

    JVarDeclaration field(int mods, String type, String name);

    JVarDeclaration field(int mods, String type, String name, JExpr init);

    JMethodDef method(int mods, JType returnType, String name);

    JMethodDef method(int mods, Class<?> returnType, String name);

    JMethodDef method(int mods, String returnType, String name);

    JMethodDef constructor(int mods);

    JClassDef _class(int mods, String name);

    JClassDef _enum(int mods, String name);

    JClassDef _interface(int mods, String name);

    JClassDef annotationInterface(int mods, String name);
}
