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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJSources implements JSources {

    private final JFiler filer;
    private final FormatPreferences format;

    private final List<ImplJSourceFile> classFiles = new ArrayList<>();
    private final Map<String, AbstractJClassDef> classes = new HashMap<>();
    private final Map<AbstractJClassDef, String> qualifiedNames = new IdentityHashMap<>();

    ImplJSources(final JFiler filer, final FormatPreferences format) {
        this.filer = filer;
        this.format = format;
    }

    void addClassDef(String qualifiedName, AbstractJClassDef classDef) {
        classes.put(qualifiedName, classDef);
        qualifiedNames.put(classDef, qualifiedName);
    }

    boolean hasClass(String qualifiedName) {
        return classes.containsKey(qualifiedName);
    }

    String qualifiedNameOf(AbstractJClassDef classDef) {
        return qualifiedNames.get(classDef);
    }

    public JSourceFile createSourceFile(final String packageName, final String fileName) {
        final ImplJSourceFile classFile = new ImplJSourceFile(this, packageName, fileName);
        classFiles.add(classFile);
        return classFile;
    }

    public JPackageInfoFile createPackageInfoFile(final String packageName) {
        throw new UnsupportedOperationException("package-info.java");
    }

    public void writeSources() throws IOException {
        for (ImplJSourceFile classFile : classFiles) {
            try (Writer writer = filer.openWriter(classFile.getPackageName(), classFile.getFileName())) {
                try (SourceFileWriter sourceFileWriter = new SourceFileWriter(format, writer)) {
                    classFile.write(sourceFileWriter);
                }
            }
        }
    }
}
