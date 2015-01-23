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

import org.jboss.jdeparser.FormatPreferences.Space;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class MethodJMethodDef extends AbstractJMethodDef implements JClassItem {

    private final JType returnType;
    private final String name;

    MethodJMethodDef(final AbstractJClassDef clazz, final int mods, final JType returnType, final String name) {
        super(clazz, mods);
        this.returnType = returnType;
        this.name = name;
    }

    JType getReturnType() {
        return returnType;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writeDocComments(writer);
        writeComments(writer);
        writeAnnotations(writer, Space.AFTER_ANNOTATION);
        JMod.write(writer, getModifiers());
        writeTypeParams(writer);
        writer.write(returnType);
        writer.sp();
        writer.writeEscapedWord(name);
        super.write(writer);
    }

    public String getName() {
        return name;
    }

    public int getModifiers() {
        return super.getModifiers();
    }

    public Kind getItemKind() {
        return Kind.METHOD;
    }

    public boolean hasAllModifiers(final int mods) {
        return (getModifiers() & mods) == mods;
    }

    public boolean hasAnyModifier(final int mods) {
        return (getModifiers() & mods) != 0;
    }
}
