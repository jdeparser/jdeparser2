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
class InterfaceJClassDef extends AbstractJClassDef implements JClassItem {

    InterfaceJClassDef(final int mods, final ImplJSourceFile classFile, final String name) {
        super(mods, classFile, name);
    }

    InterfaceJClassDef(final int mods, final AbstractJClassDef enclosingClass, final String name) {
        super(mods, enclosingClass, name);
    }

    Tokens.$KW designation() {
        return Tokens.$KW.INTERFACE;
    }

    public JClassDef _extends(final String name) {
        return super._implements(name);
    }

    public JClassDef _extends(final JType type) {
        return super._implements(type);
    }

    public JClassDef _extends(final Class<?> clazz) {
        return super._implements(clazz);
    }

    boolean hasInterfaceStyleExtends() {
        return true;
    }

    public JBlock init(final ArrayList<ClassContent> content) {
        throw new UnsupportedOperationException("Interfaces cannot have init blocks");
    }

    public JMethodDef constructor(final ArrayList<ClassContent> content, final int mods) {
        throw new UnsupportedOperationException("Interfaces cannot have constructors");
    }

    boolean supportsCompactInitOnly() {
        return false;
    }

    boolean methodCanHaveBody(final int mods) {
        return false;
    }

    public Kind getItemKind() {
        return Kind.INTERFACE;
    }

    public String getName() {
        return super.getName();
    }
}
