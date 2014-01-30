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
abstract class AbstractJMethodDef extends AbstractJGeneric implements JMethodDef, ClassContent {

    protected final AbstractJClassDef clazz;
    protected final int mods;
    protected final String name;

    AbstractJMethodDef(final AbstractJClassDef clazz, final String name, final int mods) {
        this.clazz = clazz;
        this.name = name;
        this.mods = mods;
    }

    public JBlock _default() {
        return null;
    }

    public JMethodDef _default(final JExpr expr) {
        return null;
    }

    public JType returns() {
        return null;
    }

    public JBlock body() {
        return null;
    }

    public JComment returnsDoc() {
        return null;
    }

    public JParamDef param(final int mods, final String name) {
        return null;
    }

    public JParamDef param(final String name) {
        return null;
    }

    public JParamDef varargParam(final int mods, final String name) {
        return null;
    }

    public JParamDef varargParam(final String name) {
        return null;
    }

    public JParamDef[] params() {
        return new JParamDef[0];
    }

    public JComment _throws(final String type) {
        return _throws(JTypes.typeNamed(type));
    }

    public JComment _throws(final JType type) {
        return null;
    }

    public JComment _throws(final Class<? extends Throwable> type) {
        return _throws(JTypes.typeOf(type));
    }

    String getName() {
        return name;
    }

    int mods() {
        return mods;
    }
}
