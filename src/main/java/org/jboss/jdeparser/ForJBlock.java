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
class ForJBlock extends BasicJBlock implements JFor {

    private JExpr test;

    ForJBlock(final BasicJBlock parent) {
        super(parent);
    }

    public JVarDeclaration init(final int mods, final JType type, final JExpr value) {
        return null;
    }

    public JVarDeclaration init(final int mods, final String type, final JExpr value) {
        return null;
    }

    public JVarDeclaration init(final int mods, final Class<?> type, final JExpr value) {
        return null;
    }

    public JFor test(final JExpr expr) {
        test = expr;
        return this;
    }

    public JFor update(final JExpr updateExpr) {
        return this;
    }

    JExpr getTest() {
        return test;
    }
}
