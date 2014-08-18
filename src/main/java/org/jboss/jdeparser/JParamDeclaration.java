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
 * A parameter declaration.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JParamDeclaration extends JAnnotatable {

    /**
     * Get the parameter type.
     *
     * @return the parameter type
     */
    JType type();

    /**
     * Get the parameter name.
     *
     * @return the parameter name
     */
    String name();

    /**
     * Get the parameter modifiers.
     *
     * @return the parameter modifiers
     */
    int mods();

    /**
     * Determine whether the parameter is a vararg parameter.
     *
     * @return {@code true} if the parameter is vararg, {@code false} otherwise
     */
    boolean varargs();

    /**
     * Get the {@code @param} JavaDoc block for this parameter.
     *
     * @return the comment block
     */
    JComment doc();
}
