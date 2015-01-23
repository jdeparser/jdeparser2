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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class EnumJClassDef extends AbstractJClassDef implements JClassItem {
    private final Map<String, ImplJEnumConstant> constants = new LinkedHashMap<>();

    EnumJClassDef(final int mods, final ImplJSourceFile classFile, final String name) {
        super(mods, classFile, name);
    }

    EnumJClassDef(final int mods, final AbstractJClassDef enclosingClass, final String name) {
        super(mods, enclosingClass, name);
    }

    Tokens.$KW designation() {
        return Tokens.$KW.ENUM;
    }

    public JEnumConstant _enum(final String name) {
        final ImplJEnumConstant c = new ImplJEnumConstant(this, name);
        constants.put(name, c);
        return c;
    }

    public JClassDef _extends(final String name) {
        throw new UnsupportedOperationException("extends on enum");
    }

    public JClassDef _extends(final JType type) {
        throw new UnsupportedOperationException("extends on enum");
    }

    public JClassDef _extends(final Class<?> clazz) {
        throw new UnsupportedOperationException("extends on enum");
    }

    void writeContent(final SourceFileWriter sourceFileWriter) throws IOException {
        final Iterator<ImplJEnumConstant> iterator = constants.values().iterator();
        if (iterator.hasNext()) {
            iterator.next().writeDirect(sourceFileWriter);
            while (iterator.hasNext()) {
                sourceFileWriter.write(Tokens.$PUNCT.COMMA);
                sourceFileWriter.write(FormatPreferences.Space.AFTER_COMMA_ENUM_CONSTANT);
                iterator.next().writeDirect(sourceFileWriter);
            }
            if (sourceFileWriter.getFormat().hasOption(FormatPreferences.Opt.ENUM_TRAILING_COMMA)) {
                sourceFileWriter.write(Tokens.$PUNCT.COMMA);
                sourceFileWriter.write(FormatPreferences.Space.AFTER_COMMA_ENUM_CONSTANT);
            }
        }
        sourceFileWriter.write(Tokens.$PUNCT.SEMI);
        sourceFileWriter.write(FormatPreferences.Space.AFTER_SEMICOLON);
        super.writeContent(sourceFileWriter);
    }

    public Kind getItemKind() {
        return Kind.ENUM;
    }

    public String getName() {
        return super.getName();
    }
}
