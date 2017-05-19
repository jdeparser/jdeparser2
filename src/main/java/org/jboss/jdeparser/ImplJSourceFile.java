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

import static org.jboss.jdeparser.Tokens.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJSourceFile extends BasicJCommentable implements JSourceFile {
    private final ImplJSources sources;
    private final Map<String, AbstractJType> imports = new HashMap<>();
    private final Map<String, StaticRefJExpr> staticImports = new HashMap<>();
    private final ArrayList<ClassFileContent> content = new ArrayList<>();
    private final String packageName;
    private final String fileName;
    private boolean packageWritten;

    ImplJSourceFile(final ImplJSources sources, final String packageName, final String fileName) {
        this.sources = sources;
        this.packageName = packageName;
        this.fileName = fileName;
    }

    private <C extends ClassFileContent> C add(C item) {
        content.add(item);
        return item;
    }

    private void checkPackage() {
        if (! packageWritten) {
            content.add(new ClassFileContent() {
                public void write(final SourceFileWriter writer) throws IOException {
                    writer.write($KW.PACKAGE);
                    writer.sp();
                    writer.writeEscaped(packageName);
                    writer.write($PUNCT.SEMI);
                    writer.nl();
                    final Map<String, AbstractJType> imports = ImplJSourceFile.this.imports;
                    final Iterator<AbstractJType> iterator = imports.values().iterator();
                    if (iterator.hasNext()) {
                        writer.nl();
                        do {
                            final AbstractJType _import = iterator.next();
                            writer.write($KW.IMPORT);
                            writer.writeClass(_import.qualifiedName());
                            writer.write($PUNCT.SEMI);
                            writer.nl();
                        } while (iterator.hasNext());
                        writer.nl();
                    }
                    final Map<String, StaticRefJExpr> staticImports = ImplJSourceFile.this.staticImports;
                    final Iterator<StaticRefJExpr> staticIterator = staticImports.values().iterator();
                    if (staticIterator.hasNext()) {
                        writer.nl();
                        do {
                            final StaticRefJExpr staticImport = staticIterator.next();
                            writer.write($KW.IMPORT);
                            writer.write($KW.STATIC);
                            staticImport.writeForImport(writer);
                            writer.write($PUNCT.SEMI);
                            writer.nl();
                        } while (staticIterator.hasNext());
                        writer.nl();
                    }
                }
            });
            packageWritten = true;
        }
    }

    boolean hasImport(final String name) {
        return imports.containsKey(name);
    }

    boolean hasImport(final AbstractJType type) {
        return type.equals(imports.get(type.simpleName()));
    }

    boolean hasStaticImport(final String name) {
        return staticImports.containsKey(name);
    }

    boolean hasStaticImport(final String name, final AbstractJType enclosingType) {
        final StaticRefJExpr expr = staticImports.get(name);
        return expr != null && enclosingType.equals(expr.getType());
    }

    public JSourceFile _import(final String type) {
        return _import(JTypes.typeNamed(type));
    }

    public JSourceFile _import(final JType type) {
        if (! (type instanceof ReferenceJType) && ! (type instanceof NestedJType) && ! (type instanceof NarrowedJType)) {
            // can't import this type
            return this;
        }
        if (imports.containsKey(type.simpleName())) {
            // already imported
            return this;
        }
        checkPackage();
        imports.put(type.simpleName(), (AbstractJType) type.erasure());
        return this;
    }

    public JSourceFile _import(final Class<?> type) {
        return _import(JTypes.typeOf(type));
    }

    public JSourceFile importStatic(final String type, final String member) {
        return importStatic(JTypes.typeNamed(type), member);
    }

    public JSourceFile importStatic(final JType type, final String member) {
        if (! (type instanceof ReferenceJType) && ! (type instanceof NestedJType)) {
            // no static members
            return this;
        }
        if (staticImports.containsKey(member)) {
            // already imported
            return this;
        }
        checkPackage();
        staticImports.put(member, new StaticRefJExpr(AbstractJType.of(type), member));
        return this;
    }

    public JSourceFile importStatic(final Class<?> type, final String member) {
        return importStatic(JTypes.typeOf(type), member);
    }

    public JSourceFile blankLine() {
        checkPackage();
        add(BlankLine.getInstance());
        return this;
    }

    public JClassDef _class(final int mods, final String name) {
        checkPackage();
        return add(new PlainJClassDef(mods, this, name));
    }

    public JClassDef _enum(final int mods, final String name) {
        checkPackage();
        return add(new EnumJClassDef(mods, this, name));
    }

    public JClassDef _interface(final int mods, final String name) {
        checkPackage();
        return add(new InterfaceJClassDef(mods, this, name));
    }

    public JClassDef annotationInterface(final int mods, final String name) {
        checkPackage();
        return add(new AnnotationJClassDef(mods, this, name));
    }

    String getPackageName() {
        return packageName;
    }

    String getFileName() {
        return fileName;
    }

    void write(final SourceFileWriter sourceFileWriter) throws IOException {
        sourceFileWriter.setClassFile(this);
        for (ClassFileContent item : content) {
            item.write(sourceFileWriter);
        }
        sourceFileWriter.setClassFile(null);
    }

    ImplJSources getSources() {
        return sources;
    }
}
