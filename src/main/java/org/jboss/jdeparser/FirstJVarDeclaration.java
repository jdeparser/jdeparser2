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

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class FirstJVarDeclaration extends BasicJAnnotatable implements JVarDeclaration, BlockContent, ClassContent, JClassItem {

    private final int mods;
    private final JType type;
    private final String name;
    private final JExpr value;
    private ArrayList<SuccessorJVarDeclaration> successors;

    FirstJVarDeclaration(final int mods, final JType type, final String name, final JExpr value) {
        this.mods = mods;
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        write(writer, null);
    }

    void write(final SourceFileWriter writer, final FormatPreferences.Space beforeSemicolon) throws IOException {
        writeNoSemi(writer);
        writer.write(beforeSemicolon);
        writer.write($PUNCT.SEMI);
    }

    void writeNoSemi(final SourceFileWriter writer) throws IOException {
        writeComments(writer);
        super.writeAnnotations(writer, FormatPreferences.Space.AFTER_ANNOTATION);
        JMod.write(writer, mods);
        writer.write(type);
        writer.sp();
        String name = this.name;
        JExpr value = this.value;
        writer.writeEscapedWord(name);
        if (value != null) {
            writer.write($PUNCT.BINOP.ASSIGN.getSpacingRule());
            writer.write($PUNCT.BINOP.ASSIGN);
            writer.write($PUNCT.BINOP.ASSIGN.getSpacingRule());
            writer.write(value);
        }
        if (successors != null) for (SuccessorJVarDeclaration successor : successors) {
            writer.write(FormatPreferences.Space.BEFORE_COMMA);
            writer.write($PUNCT.COMMA);
            writer.write(FormatPreferences.Space.AFTER_COMMA);
            writer.writeEscapedWord(successor.name());
            value = successor.getValue();
            if (value != null) {
                writer.write($PUNCT.BINOP.ASSIGN.getSpacingRule());
                writer.write($PUNCT.BINOP.ASSIGN);
                writer.write($PUNCT.BINOP.ASSIGN.getSpacingRule());
                writer.write(value);
            }
        }
    }

    public JType type() {
        return type;
    }

    public String name() {
        return name;
    }

    int mods() {
        return mods;
    }

    public JVarDeclaration add(final String name, final JExpr init) {
        if (successors == null) successors = new ArrayList<>();
        final SuccessorJVarDeclaration s = new SuccessorJVarDeclaration(this, name, init);
        successors.add(s);
        return s;
    }

    public JVarDeclaration add(final String name) {
        if (successors == null) successors = new ArrayList<>();
        final SuccessorJVarDeclaration s = new SuccessorJVarDeclaration(this, name, null);
        successors.add(s);
        return s;
    }

    public Kind getItemKind() {
        return Kind.FIELD;
    }

    public int getModifiers() {
        return mods;
    }

    public boolean hasAllModifiers(final int mods) {
        return (this.mods & mods) == mods;
    }

    public boolean hasAnyModifier(final int mods) {
        return (this.mods & mods) != 0;
    }

    public String getName() {
        return name;
    }
}
