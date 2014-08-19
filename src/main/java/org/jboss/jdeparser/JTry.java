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
 * A {@code try} block.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JTry extends JBlock {

    /**
     * Add a resource for {@code try}-with-resources constructs.
     *
     * @param mods the resource variable modifiers
     * @param type the resource variable type
     * @param var the resource variable name
     * @param init the resource variable initialization value
     * @return the variable declaration
     */
    JVarDeclaration with(int mods, String type, String var, JExpr init);

    /**
     * Add a resource for {@code try}-with-resources constructs.
     *
     * @param mods the resource variable modifiers
     * @param type the resource variable type
     * @param var the resource variable name
     * @param init the resource variable initialization value
     * @return the variable declaration
     */
    JVarDeclaration with(int mods, JType type, String var, JExpr init);

    /**
     * Add a resource for {@code try}-with-resources constructs.
     *
     * @param mods the resource variable modifiers
     * @param type the resource variable type
     * @param var the resource variable name
     * @param init the resource variable initialization value
     * @return the variable declaration
     */
    JVarDeclaration with(int mods, Class<? extends AutoCloseable> type, String var, JExpr init);

    /**
     * Add a {@code catch} block.
     *
     * @param mods the catch block modifiers
     * @param type the exception type
     * @param var the exception variable name
     * @return the {@code catch} sub-block
     */
    JCatch _catch(int mods, String type, String var);

    /**
     * Add a {@code catch} block.
     *
     * @param mods the catch block modifiers
     * @param type the exception type
     * @param var the exception variable name
     * @return the {@code catch} sub-block
     */
    JCatch _catch(int mods, Class<? extends Throwable> type, String var);

    /**
     * Add a {@code catch} block.
     *
     * @param mods the catch block modifiers
     * @param type the exception type
     * @param var the exception variable name
     * @return the {@code catch} sub-block
     */
    JCatch _catch(int mods, JType type, String var);

    /**
     * Add a {@code catch} for an ignored exception.
     *
     * @param type the exception type
     * @return this {@code try} block
     */
    JTry ignore(String type);

    /**
     * Add a {@code catch} for an ignored exception.
     *
     * @param type the exception type
     * @return this {@code try} block
     */
    JTry ignore(Class<? extends Throwable> type);

    /**
     * Add a {@code catch} for an ignored exception.
     *
     * @param type the exception type
     * @return this {@code try} block
     */
    JTry ignore(JType type);

    /**
     * Add the {@code finally} block for this {@code try}.
     *
     * @return the {@code finally} sub-block
     */
    JBlock _finally();
}
