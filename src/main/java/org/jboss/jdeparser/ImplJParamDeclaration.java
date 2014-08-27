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
class ImplJParamDeclaration extends BasicJAnnotatable implements JParamDeclaration {

    private final JType type;
    private final String name;
    private final int mods;

    ImplJParamDeclaration(final int mods, final JType type, final String name) {
        this.mods = mods;
        this.type = type;
        this.name = name;
    }

    public JType type() {
        return type;
    }

    public String name() {
        return name;
    }

    public int mods() {
        return mods & ~JMod.PRIVATE_BITS;
    }

    public boolean varargs() {
        return JMod.allAreSet(mods, JMod.VARARGS);
    }

    public JComment doc() {
        return null;
    }

    void write(final SourceFileWriter writer) throws IOException {
        writeComments(writer);
        writeAnnotations(writer, Space.AFTER_PARAM_ANNOTATION);
        JMod.write(writer, mods());
        writer.write(type);
        if (varargs()) {
            // todo maybe a separate token type
            writer.write(Tokens.$PUNCT.DOT);
            writer.write(Tokens.$PUNCT.DOT);
            writer.write(Tokens.$PUNCT.DOT);
        }
        writer.sp();
        writer.writeEscapedWord(name);
    }
}
