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
class ImplJClassFile extends BasicJCommentable implements JClassFile {
    private final ArrayList<ReferenceJType> imports = new ArrayList<>();
    private final ArrayList<TypeRefJExpr> staticImports = new ArrayList<>();
    private final ArrayList<ClassFileContent> content = new ArrayList<>();

    private <C extends ClassFileContent> C add(C item) {
        content.add(item);
        return item;
    }

    public JClassFile _import(final String type) {
        return null;
    }

    public JClassFile _import(final JType type) {
        return null;
    }

    public JClassFile _import(final Class<?> type) {
        return null;
    }

    public JClassFile importStatic(final String type, final String member) {
        return null;
    }

    public JClassFile importStatic(final JType type, final String member) {
        return null;
    }

    public JClassFile importStatic(final Class<?> type, final String member) {
        return null;
    }

    public JClassDef _class(final int mods, final String name) {
        return add(new PlainJClassDef(classFile, mods, name));
    }

    public JClassDef _enum(final int mods, final String name) {
        return null;
    }

    public JClassDef _interface(final int mods, final String name) {
        return null;
    }

    public JClassDef annotationInterface(final int mods, final String name) {
        return null;
    }

    public JComment inlineLineComment() {
        return add(new LineJComment());
    }

    public JComment inlineBlockComment() {
        return add(new BlockJComment());
    }
}
