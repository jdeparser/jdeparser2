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

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJParamDef extends BasicJAnnotatable implements JParamDef {

    private final JType type;
    private final String name;
    private final int mods;

    ImplJParamDef(final int mods, final JType type, final String name) {
        this.mods = mods;
        this.type = type;
        this.name = name;
    }

    public String name() {
        return null;
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
        writeAnnotations(writer);
        JMod.write(writer, mods());
        writer.write(type);
        writer.writeIdentifier(name);
    }
}
