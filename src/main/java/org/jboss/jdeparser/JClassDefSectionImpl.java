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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class JClassDefSectionImpl implements JClassDefSection, ClassContent {

    private final Sectionable sectionable;
    private final ArrayList<ClassContent> content = new ArrayList<>();

    JClassDefSectionImpl(final Sectionable sectionable) {
        this.sectionable = sectionable;
    }

    <C extends ClassContent> C add(C item) {
        content.add(item);
        return item;
    }

    @Override
    public JClassDefSection section() {
        return add(new JClassDefSectionImpl(sectionable));
    }

    @Override
    public JClassDefSection blankLine() {
        add(BlankLine.getInstance());
        return this;
    }

    @Override
    public JBlock init() {
        return sectionable.init(content);
    }

    @Override
    public JBlock staticInit() {
        return sectionable.staticInit(content);
    }

    @Override
    public JVarDeclaration field(final int mods, final JType type, final String name) {
        return sectionable.field(content, mods, type, name, null);
    }

    @Override
    public JVarDeclaration field(final int mods, final JType type, final String name, final JExpr init) {
        return sectionable.field(content, mods, type, name, init);
    }

    @Override
    public JVarDeclaration field(final int mods, final Class<?> type, final String name) {
        return field(mods, JTypes.typeOf(type), name);
    }

    @Override
    public JVarDeclaration field(final int mods, final Class<?> type, final String name, final JExpr init) {
        return field(mods, JTypes.typeOf(type), name, init);
    }

    @Override
    public JVarDeclaration field(final int mods, final String type, final String name) {
        return field(mods, JTypes.typeNamed(type), name);
    }

    @Override
    public JVarDeclaration field(final int mods, final String type, final String name, final JExpr init) {
        return field(mods, JTypes.typeNamed(type), name, init);
    }

    @Override
    public JMethodDef method(final int mods, final JType returnType, final String name) {
        return sectionable.method(content, mods, returnType, name);
    }

    @Override
    public JMethodDef method(final int mods, final Class<?> returnType, final String name) {
        return method(mods, JTypes.typeOf(returnType), name);
    }

    @Override
    public JMethodDef method(final int mods, final String returnType, final String name) {
        return method(mods, JTypes.typeNamed(returnType), name);
    }

    @Override
    public JMethodDef constructor(final int mods) {
        return sectionable.constructor(content, mods);
    }

    @Override
    public JClassDef _class(final int mods, final String name) {
        return sectionable._class(content, mods, name);
    }

    @Override
    public JClassDef _enum(final int mods, final String name) {
        return sectionable._enum(content, mods, name);
    }

    @Override
    public JClassDef _interface(final int mods, final String name) {
        return sectionable._interface(content, mods, name);
    }

    @Override
    public JClassDef annotationInterface(final int mods, final String name) {
        return sectionable.annotationInterface(content, mods, name);
    }

    @Override
    public JComment lineComment() {
        return add(new LineJComment());
    }

    @Override
    public JComment blockComment() {
        return add(new BlockJComment());
    }

    @Override
    public void write(final SourceFileWriter writer) throws IOException {
        Iterator<ClassContent> iterator = content.iterator();
        if (iterator.hasNext()) {
            iterator.next().write(writer);
            while (iterator.hasNext()) {
                writer.nl();
                iterator.next().write(writer);
            }
        }
    }
}
