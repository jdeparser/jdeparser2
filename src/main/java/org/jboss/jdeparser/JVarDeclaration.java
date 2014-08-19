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
 * A variable declaration.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JVarDeclaration extends JAnnotatable, JDocCommentable {

    /**
     * Get the variable type.
     *
     * @return the variable type
     */
    JType type();

    /**
     * Get the variable name.
     *
     * @return the variable name
     */
    String name();

    /**
     * Add another item to this declaration.  Subsequent items always have the same type as this item.
     *
     * @param name the variable name
     * @param init the variable initializer
     * @return the subsequent declaration
     */
    JVarDeclaration add(String name, JExpr init);

    /**
     * Add another item to this declaration.  Subsequent items always have the same type as this item.
     *
     * @param name the variable name
     * @return the subsequent declaration
     */
    JVarDeclaration add(String name);
}
