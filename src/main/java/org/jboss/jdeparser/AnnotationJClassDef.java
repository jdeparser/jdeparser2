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
class AnnotationJClassDef extends AbstractJClassDef implements JClassItem {

    AnnotationJClassDef(final int mods, final ImplJSourceFile classFile, final String name) {
        super(mods, classFile, name);
    }

    AnnotationJClassDef(final int mods, final AbstractJClassDef enclosingClass, final String name) {
        super(mods, enclosingClass, name);
    }

    Tokens.$KW designation() {
        return Tokens.$KW.AT_INTERFACE;
    }

    public JClassDef _extends(final String name) {
        throw new UnsupportedOperationException("extends on @interface");
    }

    public JClassDef _extends(final JType type) {
        throw new UnsupportedOperationException("extends on @interface");
    }

    public JClassDef _extends(final Class<?> clazz) {
        throw new UnsupportedOperationException("extends on @interface");
    }

    public JClassDef _implements(final String... names) {
        throw new UnsupportedOperationException("implements on @interface");
    }

    public JClassDef _implements(final JType... types) {
        throw new UnsupportedOperationException("implements on @interface");
    }

    public JClassDef _implements(final Class<?>... classes) {
        throw new UnsupportedOperationException("implements on @interface");
    }

    public JBlock init(final ArrayList<ClassContent> content) {
        throw new UnsupportedOperationException("init block on @interface");
    }

    boolean methodCanHaveBody(final int mods) {
        return false;
    }

    public JMethodDef constructor(final ArrayList<ClassContent> content, final int mods) {
        throw new UnsupportedOperationException("constructor on @interface");
    }

    public JTypeParamDef typeParam(final String name) {
        throw new UnsupportedOperationException("type parameters on @interface");
    }

    public JMethodDef method(final ArrayList<ClassContent> content, final int mods, final JType returnType, final String name) {
        return add(content, new AnnotationJMethodDef(this, mods, returnType, name));
    }

    public Kind getItemKind() {
        return Kind.ANNOTATION_INTERFACE;
    }

    public String getName() {
        return super.getName();
    }
}
