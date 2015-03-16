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

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ForEachJBlock extends BasicJBlock {

    private final int mods;
    private final JType type;
    private final String name;
    private final JExpr iterable;

    ForEachJBlock(final BasicJBlock parent, final int mods, final JType type, final String name, final JExpr iterable) {
        super(parent, Braces.IF_MULTILINE);
        this.mods = mods;
        this.type = type;
        this.name = name;
        this.iterable = iterable;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writeComments(writer);
        writer.write($KW.FOR);
        writer.write(FormatPreferences.Space.BEFORE_PAREN_FOR);
        writer.write($PUNCT.PAREN.OPEN);
        writer.write(FormatPreferences.Space.WITHIN_PAREN_FOR);
        JMod.write(writer, mods);
        writer.write(type);
        writer.sp();
        writer.writeEscaped(name);
        writer.write(FormatPreferences.Space.BEFORE_COLON);
        writer.write($PUNCT.COLON);
        writer.write(FormatPreferences.Space.AFTER_COLON);
        writer.write(iterable);
        writer.write(FormatPreferences.Space.WITHIN_PAREN_FOR);
        writer.write($PUNCT.PAREN.CLOSE);
        super.write(writer, FormatPreferences.Space.BEFORE_BRACE_FOR);
    }
}
