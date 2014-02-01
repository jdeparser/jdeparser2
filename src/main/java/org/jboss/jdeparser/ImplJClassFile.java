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

import static org.jboss.jdeparser.FormatStates.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJClassFile extends BasicJCommentable implements JClassFile {
    private final ArrayList<ReferenceJType> imports = new ArrayList<>();
    private final ArrayList<TypeRefJExpr> staticImports = new ArrayList<>();
    private final ArrayList<ClassFileContent> content = new ArrayList<>();
    private final String packageName;
    private final String fileName;
    private boolean packageWritten;

    ImplJClassFile(final String packageName, final String fileName) {
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
                    sourceFileWriter.writeIdentifier(packageName);
                    sourceFileWriter.write($PUNCT.SEMI);
                }
            });
            packageWritten = true;
        }
    }

    public JClassFile _import(final String type) {
        return _import(JTypes.typeNamed(type));
    }

    public JClassFile _import(final JType type) {
        checkPackage();
        if (imports.isEmpty()) {
            content.add(new ClassFileContent() {
                public void write(final SourceFileWriter sourceFileWriter) throws IOException {
                    for (ReferenceJType _import : imports) {
                        sourceFileWriter.write($KW.IMPORT);
                        sourceFileWriter.writeClass(_import.qualifiedName());
                        sourceFileWriter.write($PUNCT.SEMI);
                    }
                }
            });
        }
        imports.add((ReferenceJType) type);
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
                    for (TypeRefJExpr staticImport : staticImports) {
                        sourceFileWriter.write($KW.IMPORT);
                        sourceFileWriter.write($KW.STATIC);
                        sourceFileWriter.write(staticImport);
                        sourceFileWriter.write($PUNCT.SEMI);
                    }
                }
            });
        }
        staticImports.add(new TypeRefJExpr(AbstractJType.of(type), member));
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
        return null;
    }

    public JClassDef _interface(final int mods, final String name) {
        checkPackage();
        return null;
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
        for (ClassFileContent item : content) {
            item.write(sourceFileWriter);
        }
    }
}
