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

import static org.jboss.jdeparser.Assertions.*;
import static org.jboss.jdeparser.Tokens.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJCatch extends BasicJBlock implements JCatch {

    private final ImplJTry _try;
    private final int mods;
    private final String var;
    private final ArrayList<JType> types = new ArrayList<>(1);

    ImplJCatch(final ImplJTry _try, final int mods, final JType type, final String var) {
        super(_try.getParent(), Braces.REQUIRED);
        this.mods = mods;
        this.var = var;
        types.add(type);
        this._try = _try;
    }

    public JCatch or(final JType orType) {
        types.add(orType);
        return this;
    }

    public JCatch or(final String orType) {
        return or(JTypes.typeNamed(orType));
    }

    public JCatch or(final Class<? extends Throwable> orType) {
        return or(JTypes.typeOf(orType));
    }

    public JVarDeclaration with(final int mods, final String type, final String var, final JExpr init) {
        return _try.with(mods, type, var, init);
    }

    public JVarDeclaration with(final int mods, final JType type, final String var, final JExpr init) {
        return _try.with(mods, type, var, init);
    }

    public JVarDeclaration with(final int mods, final Class<? extends AutoCloseable> type, final String var, final JExpr init) {
        return _try.with(mods, type, var, init);
    }

    public JCatch _catch(final int mods, final String type, final String var) {
        return _try._catch(mods, type, var);
    }

    public JCatch _catch(final int mods, final Class<? extends Throwable> type, final String var) {
        return _try._catch(mods, type, var);
    }

    public JCatch _catch(final int mods, final JType type, final String var) {
        return _try._catch(mods, type, var);
    }

    public JTry ignore(final String type) {
        _try.ignore(type);
        return this;
    }

    public JTry ignore(final Class<? extends Throwable> type) {
        _try.ignore(type);
        return this;
    }

    public JTry ignore(final JType type) {
        _try.ignore(type);
        return this;
    }

    public JBlock _finally() {
        return _try._finally();
    }

    ImplJTry getTry() {
        return _try;
    }

    ArrayList<JType> getTypes() {
        return types;
    }

    int getMods() {
        return mods;
    }

    String getVar() {
        return var;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writeComments(writer);
        writer.write($KW.CATCH);
        writer.write(FormatPreferences.Space.BEFORE_PAREN_CATCH);
        writer.write($PUNCT.PAREN.OPEN);
        writer.write(FormatPreferences.Space.WITHIN_PAREN_CATCH);
        JMod.write(writer, mods);
        final Iterator<JType> iterator = types.iterator();
        if (alwaysTrue(iterator.hasNext())) {
            writer.write(iterator.next());
            while (iterator.hasNext()) {
                writer.write($PUNCT.BINOP.BOR);
                writer.write(iterator.next());
            }
        }
        writer.writeEscaped(var);
        writer.write(FormatPreferences.Space.WITHIN_PAREN_CATCH);
        writer.write($PUNCT.PAREN.CLOSE);
        super.write(writer, FormatPreferences.Space.BEFORE_BRACE_CATCH);
    }
}
