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
class ForJBlock extends BasicJBlock implements JFor {

    private FirstJVarDeclaration init;
    private JExpr test;
    private JExpr update;

    ForJBlock(final BasicJBlock parent) {
        super(parent, Braces.IF_MULTILINE);
    }

    public JVarDeclaration init(final int mods, final String type, final String name, final JExpr value) {
        return init(mods, JTypes.typeNamed(type), name, value);
    }

    public JVarDeclaration init(final int mods, final JType type, final String name, final JExpr value) {
        return init = new FirstJVarDeclaration(mods, type, name, value);
    }

    public JVarDeclaration init(final int mods, final Class<?> type, final String name, final JExpr value) {
        return init(mods, JTypes.typeOf(type), name, value);
    }

    public JFor test(final JExpr expr) {
        test = expr;
        return this;
    }

    public JFor update(final JExpr updateExpr) {
        update = updateExpr;
        return this;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writeComments(writer);
        writer.write($KW.FOR);
        writer.write(FormatPreferences.Space.BEFORE_PAREN_FOR);
        writer.write($PUNCT.PAREN.OPEN);
        writer.write(FormatPreferences.Space.WITHIN_PAREN_FOR);
        if (init != null) init.writeNoSemi(writer);
        writer.write(FormatPreferences.Space.BEFORE_SEMICOLON);
        writer.write($PUNCT.SEMI);
        writer.write(FormatPreferences.Space.AFTER_SEMICOLON);
        writer.write(test);
        writer.write(FormatPreferences.Space.BEFORE_SEMICOLON);
        writer.write($PUNCT.SEMI);
        writer.write(FormatPreferences.Space.AFTER_SEMICOLON);
        writer.write(update);
        writer.write(FormatPreferences.Space.WITHIN_PAREN_FOR);
        writer.write($PUNCT.PAREN.CLOSE);
        super.write(writer, FormatPreferences.Space.BEFORE_BRACE_FOR);
    }
}
