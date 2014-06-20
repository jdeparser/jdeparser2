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
import java.util.Map;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJClassFile extends BasicJCommentable implements JClassFile {
    private final ImplJSources sources;
    private final Map<String, ReferenceJType> imports = new HashMap<>();
    private final Map<String, StaticRefJExpr> staticImports = new HashMap<>();
    private final ArrayList<ClassFileContent> content = new ArrayList<>();
    private final String packageName;
    private final String fileName;
    private boolean packageWritten;

    ImplJClassFile(final ImplJSources sources, final String packageName, final String fileName) {
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
                public void write(final SourceFileWriter sourceFileWriter) throws IOException {
                    sourceFileWriter.write($KW.PACKAGE);
                    sourceFileWriter.sp();
                    sourceFileWriter.writeRaw(packageName);
                    sourceFileWriter.write($PUNCT.SEMI);
                    sourceFileWriter.nl();
                }
            });
            packageWritten = true;
        }
    }

    boolean hasImport(final String name) {
        return imports.containsKey(name);
    }

    boolean hasImport(final JType type) {
        return type instanceof ReferenceJType && imports.containsKey(type.simpleName()) && imports.get(type.simpleName()).qualifiedName().equals(type.qualifiedName());
    }

    boolean hasStaticImport(final String name) {
        return staticImports.containsKey(name);
    }

    boolean hasStaticImport(final JExpr expr) {
        if (! (expr instanceof StaticRefJExpr)) return false;
        final StaticRefJExpr staticRefJExpr = (StaticRefJExpr) expr;
        final String refName = staticRefJExpr.getRefName();
        return staticImports.containsKey(refName) && staticImports.get(refName).getType().qualifiedName().equals(staticRefJExpr.getType().qualifiedName());
    }

    public JClassFile _import(final String type) {
        return _import(JTypes.typeNamed(type));
    }

    public JClassFile _import(final JType type) {
        checkPackage();
        if (imports.isEmpty()) {
            content.add(new ClassFileContent() {
                public void write(final SourceFileWriter sourceFileWriter) throws IOException {
                    for (ReferenceJType _import : imports.values()) {
                        sourceFileWriter.write($KW.IMPORT);
                        sourceFileWriter.writeClass(_import.qualifiedName());
                        sourceFileWriter.write($PUNCT.SEMI);
                        sourceFileWriter.nl();
                    }
                }
            });
        }
        if (! (type instanceof ReferenceJType)) {
            return this;
        }
        if (imports.containsKey(type.qualifiedName())) {
            return this;
        }
        imports.put(type.qualifiedName(), (ReferenceJType) type);
        return this;
    }

    public JClassFile _import(final Class<?> type) {
        return _import(JTypes.typeOf(type));
    }

    public JClassFile importStatic(final String type, final String member) {
        return importStatic(JTypes.typeNamed(type), member);
    }

    public JClassFile importStatic(final JType type, final String member) {
        checkPackage();
        if (staticImports.isEmpty()) {
            content.add(new ClassFileContent() {
                public void write(final SourceFileWriter sourceFileWriter) throws IOException {
                    for (StaticRefJExpr staticImport : staticImports.values()) {
                        sourceFileWriter.write($KW.IMPORT);
                        sourceFileWriter.write($KW.STATIC);
                        sourceFileWriter.write(staticImport);
                        sourceFileWriter.write($PUNCT.SEMI);
                        sourceFileWriter.nl();
                    }
                }
            });
        }
        staticImports.put(member, new StaticRefJExpr(AbstractJType.of(type), member));
        return this;
    }

    public JClassFile importStatic(final Class<?> type, final String member) {
        return importStatic(JTypes.typeOf(type), member);
    }

    public JClassDef _class(final int mods, final String name) {
        checkPackage();
        return add(new PlainJClassDef(this, mods, name));
    }

    public JClassDef _enum(final int mods, final String name) {
        checkPackage();
        return add(new EnumJClassDef(this, mods, name));
    }

    public JClassDef _interface(final int mods, final String name) {
        checkPackage();
        return add(new InterfaceJClassDef(this, mods, name));
    }

    public JClassDef annotationInterface(final int mods, final String name) {
        checkPackage();
        return null;
    }

    public JComment inlineLineComment() {
        return add(new LineJComment());
    }

    public JComment inlineBlockComment() {
        return add(new BlockJComment());
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
