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
class ConstructorJMethodDef extends AbstractJMethodDef implements JClassItem {

    ConstructorJMethodDef(final AbstractJClassDef classDef, final int mods) {
        super(classDef, mods);
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writeDocComments(writer);
        writeComments(writer);
        writeAnnotations(writer, Space.AFTER_ANNOTATION);
        JMod.write(writer, getModifiers());
        writeTypeParams(writer);
        writer.write(clazz().erasedType());
        super.write(writer);
    }

    public Kind getItemKind() {
        return Kind.METHOD;
    }

    public int getModifiers() {
        return super.getModifiers();
    }

    public boolean hasAllModifiers(final int mods) {
        return (getModifiers() & mods) == mods;
    }

    public boolean hasAnyModifier(final int mods) {
        return (getModifiers() & mods) != 0;
    }

    public String getName() {
        return null;
    }
}
