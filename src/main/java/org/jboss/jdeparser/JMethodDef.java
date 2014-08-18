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
 * A method or constructor definition.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JMethodDef extends JGenericDef, JAnnotatable, JDocCommentable {

    /**
     * A default method body for a JDK 8+ interface method.
     *
     * @return the method body
     */
    JBlock _default();

    /**
     * A default value for an annotation method.
     *
     * @param expr the value
     * @return this method definition
     */
    JMethodDef _default(JExpr expr);

    /**
     * Get the method body.
     *
     * @return the method body
     */
    JBlock body();

    /**
     * Get the {@code @return} doc comment block.
     *
     * @return the comment block
     */
    JComment returnsDoc();

    /**
     * Add a parameter to this method.
     *
     * @param mods the parameter modifiers
     * @param type the parameter type
     * @param name the parameter name
     * @return the parameter declaration
     */
    JParamDeclaration param(int mods, JType type, String name);

    /**
     * Add a parameter to this method.
     *
     * @param type the parameter type
     * @param name the parameter name
     * @return the parameter declaration
     */
    JParamDeclaration param(JType type, String name);

    /**
     * Add a parameter to this method.
     *
     * @param mods the parameter modifiers
     * @param type the parameter type
     * @param name the parameter name
     * @return the parameter declaration
     */
    JParamDeclaration param(int mods, String type, String name);

    /**
     * Add a parameter to this method.
     *
     * @param type the parameter type
     * @param name the parameter name
     * @return the parameter declaration
     */
    JParamDeclaration param(String type, String name);

    /**
     * Add a parameter to this method.
     *
     * @param mods the parameter modifiers
     * @param type the parameter type
     * @param name the parameter name
     * @return the parameter declaration
     */
    JParamDeclaration param(int mods, Class<?> type, String name);

    /**
     * Add a parameter to this method.
     *
     * @param type the parameter type
     * @param name the parameter name
     * @return the parameter declaration
     */
    JParamDeclaration param(Class<?> type, String name);

    /**
     * Add a vararg parameter to this method.
     *
     * @param mods the parameter modifiers
     * @param type the parameter type
     * @param name the parameter name
     * @return the parameter declaration
     */
    JParamDeclaration varargParam(int mods, JType type, String name);

    /**
     * Add a vararg parameter to this method.
     *
     * @param type the parameter type
     * @param name the parameter name
     * @return the parameter declaration
     */
    JParamDeclaration varargParam(JType type, String name);

    /**
     * Add a vararg parameter to this method.
     *
     * @param mods the parameter modifiers
     * @param type the parameter type
     * @param name the parameter name
     * @return the parameter declaration
     */
    JParamDeclaration varargParam(int mods, String type, String name);

    /**
     * Add a vararg parameter to this method.
     *
     * @param type the parameter type
     * @param name the parameter name
     * @return the parameter declaration
     */
    JParamDeclaration varargParam(String type, String name);

    /**
     * Add a vararg parameter to this method.
     *
     * @param mods the parameter modifiers
     * @param type the parameter type
     * @param name the parameter name
     * @return the parameter declaration
     */
    JParamDeclaration varargParam(int mods, Class<?> type, String name);

    /**
     * Add a vararg parameter to this method.
     *
     * @param type the parameter type
     * @param name the parameter name
     * @return the parameter declaration
     */
    JParamDeclaration varargParam(Class<?> type, String name);

    /**
     * Get the list of parameters defined thus far.
     *
     * @return the parameter list
     */
    JParamDeclaration[] params();

    /**
     * Get a {@code @throws} doc comment block.
     *
     * @param type the exception type
     * @return the doc comment block
     */
    JComment _throws(String type);

    /**
     * Get a {@code @throws} doc comment block.
     *
     * @param type the exception type
     * @return the doc comment block
     */
    JComment _throws(JType type);

    /**
     * Get a {@code @throws} doc comment block.
     *
     * @param type the exception type
     * @return the doc comment block
     */
    JComment _throws(Class<? extends Throwable> type);
}
