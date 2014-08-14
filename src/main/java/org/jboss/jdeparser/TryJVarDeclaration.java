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

class TryJVarDeclaration extends FirstJVarDeclaration {

    private final ImplJTry owner;

    TryJVarDeclaration(final int mods, final JType type, final String name, final JExpr value, final ImplJTry owner) {
        super(mods, type, name, value);
        this.owner = owner;
    }

    public JVarDeclaration add(final String name, final JExpr init) {
        return owner.with(mods(), type(), name, init);
    }

    public JVarDeclaration add(final String name) {
        throw new IllegalArgumentException("Cannot add uninitialized variable to try-with-resources");
    }
}
