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

import java.util.ArrayList;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJTry extends BasicJBlock implements JTry {

    private ArrayList<ImplJCatch> catches = new ArrayList<>();
    private FinallyJBlock finallyBlock;

    ImplJTry(final BasicJBlock parent) {
        super(parent);
    }

    public JTry with(final int mods, final JType type, final String var, final JExpr init) {
        // todo add resource to list
        return this;
    }

    public JCatch _catch(final int mods, final String type, final String var) {
        return _catch(mods, JTypes.typeNamed(type), var);
    }

    public JCatch _catch(final int mods, final Class<? extends Throwable> type, final String var) {
        return _catch(mods, JTypes.typeOf(type), var);
    }

    private <T extends ImplJCatch> T add(T item) {
        catches.add(item);
        return item;
    }

    public JCatch _catch(final int mods, final JType type, final String var) {
        return add(new ImplJCatch(this, mods, type, var));
    }

    public JBlock _finally() {
        if (finallyBlock == null) {
            finallyBlock = new FinallyJBlock(this);
        }
        return finallyBlock;
    }
}
