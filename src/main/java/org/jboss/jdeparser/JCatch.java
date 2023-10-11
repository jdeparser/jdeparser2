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
 * A {@code catch} branch for a {@code try} block.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JCatch extends JTry {

    /**
     * Add another type option to this catch branch.
     *
     * @param orType the alternative type
     * @return this catch block
     */
    JCatch or(JType orType);

    /**
     * Add another type option to this catch branch.
     *
     * @param orType the alternative type
     * @return this catch block
     */
    JCatch or(String orType);

    /**
     * Add another type option to this catch branch.
     *
     * @param orType the alternative type
     * @return this catch block
     */
    JCatch or(Class<? extends Throwable> orType);

    /** {@inheritDoc} */
    @Override
    JCatch with(JExpr var);

    /** {@inheritDoc} */
    @Override
    JCatch ignore(String type);

    /** {@inheritDoc} */
    @Override
    JCatch ignore(Class<? extends Throwable> type);

    /** {@inheritDoc} */
    @Override
    JCatch ignore(JType type);
}
