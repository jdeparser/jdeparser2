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
import java.util.Iterator;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJTry extends BasicJBlock implements JTry {

    private ArrayList<ImplJCatch> catches;
    private ArrayList<FirstJVarDeclaration> resources;
    private FinallyJBlock finallyBlock;

    ImplJTry(final BasicJBlock parent) {
        super(parent, Braces.REQUIRED);
    }

    public JVarDeclaration with(final int mods, final String type, final String var, final JExpr init) {
        return with(mods, JTypes.typeNamed(type), var, init);
    }

    public JVarDeclaration with(final int mods, final JType type, final String var, final JExpr init) {
        return add(new TryJVarDeclaration(mods, type, var, init, this));
    }

    public JVarDeclaration with(final int mods, final Class<? extends AutoCloseable> type, final String var, final JExpr init) {
        return with(mods, JTypes.typeOf(type), var, init);
    }

    private <T extends FirstJVarDeclaration> T add(T item) {
        if (resources == null) resources = new ArrayList<>();
        resources.add(item);
        return item;
    }

    public JCatch _catch(final int mods, final String type, final String var) {
        return _catch(mods, JTypes.typeNamed(type), var);
    }

    public JCatch _catch(final int mods, final Class<? extends Throwable> type, final String var) {
        return _catch(mods, JTypes.typeOf(type), var);
    }

    public JCatch _catch(final int mods, final JType type, final String var) {
        return add(new ImplJCatch(this, mods, type, var));
    }

    private <T extends ImplJCatch> T add(T item) {
        if (catches == null) catches = new ArrayList<>();
        catches.add(item);
        return item;
    }

    public JTry ignore(final String type) {
        return ignore(JTypes.typeNamed(type));
    }

    public JTry ignore(final Class<? extends Throwable> type) {
        return ignore(JTypes.typeOf(type));
    }

    public JTry ignore(final JType type) {
        _catch(0, type, "ignored");
        return this;
    }

    public JBlock _finally() {
        if (finallyBlock == null) {
            finallyBlock = new FinallyJBlock(this);
        }
        return finallyBlock;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writeComments(writer);
        if ((catches == null || catches.isEmpty()) && (resources == null || resources.isEmpty()) && finallyBlock == null) {
            super.write(writer, null);
        } else {
            writer.write($KW.TRY);
            if (resources != null) {
                final Iterator<FirstJVarDeclaration> iterator = resources.iterator();
                if (iterator.hasNext()) {
                    writer.write(FormatPreferences.Space.BEFORE_PAREN_TRY);
                    writer.write($PUNCT.PAREN.OPEN);
                    writer.write(FormatPreferences.Space.WITHIN_PAREN_TRY);
                    iterator.next().writeNoSemi(writer);
                    while (iterator.hasNext()) {
                        writer.write(FormatPreferences.Space.BEFORE_SEMICOLON);
                        writer.write($PUNCT.SEMI);
                        writer.write(FormatPreferences.Space.AFTER_SEMICOLON);
                        iterator.next().writeNoSemi(writer);
                    }
                }
                writer.write(FormatPreferences.Space.WITHIN_PAREN_TRY);
                writer.write($PUNCT.PAREN.CLOSE);
            }
            super.write(writer, FormatPreferences.Space.BEFORE_BRACE_TRY);
            if (catches != null) for (ImplJCatch _catch : catches) {
                _catch.write(writer);
            }
            if (finallyBlock != null) {
                finallyBlock.write(writer);
            }
        }
    }
}
