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
 * A {@code for} loop.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JFor extends JBlock {

    /**
     * Add a loop initializer.
     *
     * @param mods the modifiers for the initializer variable declaration
     * @param type the type of the initializer variables
     * @param name the name of the first initializer variable
     * @param value the initial value for the first initializer variable
     * @return the initializer variable declaration
     */
    JVarDeclaration init(int mods, String type, String name, JExpr value);

    /**
     * Add a loop initializer.
     *
     * @param mods the modifiers for the initializer variable declaration
     * @param type the type of the initializer variables
     * @param name the name of the first initializer variable
     * @param value the initial value for the first initializer variable
     * @return the initializer variable declaration
     */
    JVarDeclaration init(int mods, JType type, String name, JExpr value);

    /**
     * Add a loop initializer.
     *
     * @param mods the modifiers for the initializer variable declaration
     * @param type the type of the initializer variables
     * @param name the name of the first initializer variable
     * @param value the initial value for the first initializer variable
     * @return the initializer variable declaration
     */
    JVarDeclaration init(int mods, Class<?> type, String name, JExpr value);

    /**
     * Add a test expression.
     *
     * @param expr the test expression
     * @return this {@code for} loop
     */
    JFor test(JExpr expr);

    /**
     * Add an update expression.
     *
     * @param updateExpr the update expression
     * @return this {@code for} loop
     */
    JFor update(JExpr updateExpr);
}
