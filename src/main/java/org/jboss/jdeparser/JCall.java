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
 * A method or constructor call.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JCall extends JExpr, JSimpleArgs {

    /**
     * Use the "diamond" {@code <>} syntax to specify inferred type arguments.
     *
     * @return this call
     */
    JCall diamond();

    /**
     * Add a type argument to this call.
     *
     * @param type the type to add
     * @return this call
     */
    JCall typeArg(JType type);

    /**
     * Add a type argument to this call.
     *
     * @param type the type to add
     * @return this call
     */
    JCall typeArg(String type);

    /**
     * Add a type argument to this call.
     *
     * @param type the type to add
     * @return this call
     */
    JCall typeArg(Class<?> type);

    /**
     * Add an actual parameter argument to this call.
     *
     * @param expr the argument expression
     * @return this call
     */
    JCall arg(JExpr expr);

    /**
     * Get the type arguments defined thus far.
     *
     * @return the type arguments
     */
    JType[] typeArguments();

    JExpr[] arguments();
}
