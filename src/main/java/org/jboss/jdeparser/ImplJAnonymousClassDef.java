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

import static org.jboss.jdeparser.Tokens.$KW;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJAnonymousClassDef extends AbstractJCall implements JAnonymousClassDef {
    private final AnonymousJClassDef classDef;
    private final JType type;

    ImplJAnonymousClassDef(final JType type) {
        super(Prec.METHOD_CALL);
        this.type = type;
        classDef = new AnonymousJClassDef(this);
    }

    public JType type() {
        return type;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writer.write($KW.NEW);
        writer.write(type);
        super.write(writer);
        writer.write(FormatPreferences.Space.BEFORE_BRACE_CLASS);
        classDef.write(writer);
    }

    // delegation

    public JClassDefSection section() {
        return classDef.section();
    }

    public JClassDef _extends(final String name) {
        return classDef._extends(name);
    }

    public JClassDef _extends(final JType type) {
        return classDef._extends(type);
    }

    public JClassDef _extends(final Class<?> clazz) {
        return classDef._extends(clazz);
    }

    public JClassDef _implements(final String... names) {
        return classDef._implements(names);
    }

    public JClassDef _implements(final JType... types) {
        return classDef._implements(types);
    }

    public JClassDef _implements(final Class<?>... classes) {
        return classDef._implements(classes);
    }

    public JType erasedType() {
        return classDef.erasedType();
    }

    public JClassDef blankLine() {
        return classDef.blankLine();
    }

    public JType genericType() {
        return classDef.genericType();
    }

    public JTypeParamDef typeParam(final String name) {
        return classDef.typeParam(name);
    }

    public JBlock init() {
        return classDef.init();
    }

    public JBlock staticInit() {
        return classDef.staticInit();
    }

    public JEnumConstant _enum(final String name) {
        return classDef._enum(name);
    }

    public JVarDeclaration field(final int mods, final JType type, final String name) {
        return classDef.field(mods, type, name);
    }

    public JVarDeclaration field(final int mods, final JType type, final String name, final JExpr init) {
        return classDef.field(mods, type, name, init);
    }

    public JVarDeclaration field(final int mods, final Class<?> type, final String name) {
        return classDef.field(mods, type, name);
    }

    public JVarDeclaration field(final int mods, final Class<?> type, final String name, final JExpr init) {
        return classDef.field(mods, type, name, init);
    }

    public JVarDeclaration field(final int mods, final String type, final String name) {
        return classDef.field(mods, type, name);
    }

    public JVarDeclaration field(final int mods, final String type, final String name, final JExpr init) {
        return classDef.field(mods, type, name, init);
    }

    public JMethodDef method(final int mods, final JType returnType, final String name) {
        return classDef.method(mods, returnType, name);
    }

    public JMethodDef method(final int mods, final Class<?> returnType, final String name) {
        return classDef.method(mods, returnType, name);
    }

    public JMethodDef method(final int mods, final String returnType, final String name) {
        return classDef.method(mods, returnType, name);
    }

    public JMethodDef constructor(final int mods) {
        throw new UnsupportedOperationException("constructor on anonymous class");
    }

    public JTypeParamDef[] typeParams() {
        return classDef.typeParams();
    }

    public JAnnotation annotate(final String type) {
        return classDef.annotate(type);
    }

    public JAnnotation annotate(final JType type) {
        return classDef.annotate(type);
    }

    public JAnnotation annotate(final Class<? extends Annotation> type) {
        return classDef.annotate(type);
    }

    public JDocComment docComment() {
        return classDef.docComment();
    }

    public JComment deprecated() {
        return classDef.deprecated();
    }

    public JComment lineComment() {
        return classDef.lineComment();
    }

    public JComment blockComment() {
        return classDef.blockComment();
    }

    public JClassDef _class(final int mods, final String name) {
        return classDef._class(mods, name);
    }

    public JClassDef _enum(final int mods, final String name) {
        return classDef._enum(mods, name);
    }

    public JClassDef _interface(final int mods, final String name) {
        return classDef._interface(mods, name);
    }

    public JClassDef annotationInterface(final int mods, final String name) {
        return classDef._interface(mods, name);
    }
}
