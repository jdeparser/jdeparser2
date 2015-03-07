/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015 Red Hat, Inc., and individual contributors
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
 * A Java 8 lambda.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JLambda extends JExpr {

    /**
     * Add a declared-type parameter to this lambda.
     *
     * @param type the parameter type
     * @param name the parameter name
     * @return this lambda
     */
    JLambda param(JType type, String name);

    /**
     * Add a declared-type parameter to this lambda.
     *
     * @param typeName the parameter type name
     * @param name the parameter name
     * @return this lambda
     */
    JLambda param(String typeName, String name);

    /**
     * Add a declared-type parameter to this lambda.
     *
     * @param type the parameter type
     * @param name the parameter name
     * @return this lambda
     */
    JLambda param(Class<?> type, String name);

    /**
     * Add an inferred-type parameter to this lambda.  If one parameter is inferred, all will be, despite any declared
     * parameter type.
     *
     * @param name the parameter name
     * @return this lambda
     */
    JLambda param(String name);

    /**
     * Get the lambda body as a block.  Clears any expression body.
     *
     * @return the lambda body
     */
    JBlock body();

    /**
     * Set the lambda body as an expression.  Clears any block body or previously set expression body.
     *
     * @param expr the expression body of this lambda
     * @return this lambda
     */
    JLambda body(JExpr expr);
}
