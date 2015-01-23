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
abstract class AbstractJMethodDef extends AbstractJGeneric implements JMethodDef, ClassContent {

    private final AbstractJClassDef clazz;
    private int mods;
    private ArrayList<ImplJParamDeclaration> params;
    private ArrayList<AbstractJType> _throws;
    private BasicJBlock body;

    AbstractJMethodDef(final AbstractJClassDef clazz, final int mods) {
        this.clazz = clazz;
        this.mods = mods;
    }

    public JBlock _default() {
        throw new UnsupportedOperationException("Default method implementation");
    }

    public JMethodDef _default(final JExpr expr) {
        throw new UnsupportedOperationException("Default method value");
    }

    public JBlock body() {
        if (! writeBody()) {
            throw new UnsupportedOperationException("Method body on abstract method");
        }
        if (body == null) {
            body = new BasicJBlock(null, JBlock.Braces.REQUIRED);
        }
        return body;
    }

    public JComment returnsDoc() {
        return null;
    }

    private ImplJParamDeclaration add(ImplJParamDeclaration item) {
        if (params == null) {
            params = new ArrayList<>();
        }
        params.add(item);
        return item;
    }

    public JParamDeclaration param(final int mods, final JType type, final String name) {
        if (JMod.anyAreSet(this.mods, JMod.VARARGS)) {
            throw new IllegalStateException("Vararg parameter already added");
        }
        return add(new ImplJParamDeclaration(mods, type, name));
    }

    public JParamDeclaration param(final JType type, final String name) {
        return param(0, type, name);
    }

    public JParamDeclaration param(final int mods, final String type, final String name) {
        return param(mods, JTypes.typeNamed(type), name);
    }

    public JParamDeclaration param(final String type, final String name) {
        return param(JTypes.typeNamed(type), name);
    }

    public JParamDeclaration param(final int mods, final Class<?> type, final String name) {
        return param(mods, JTypes.typeOf(type), name);
    }

    public JParamDeclaration param(final Class<?> type, final String name) {
        return param(JTypes.typeOf(type), name);
    }

    public JParamDeclaration varargParam(final int mods, final JType type, final String name) {
        if (JMod.anyAreSet(this.mods, JMod.VARARGS)) {
            throw new IllegalStateException("Vararg parameter already added");
        }
        this.mods |= JMod.VARARGS;
        return add(new ImplJParamDeclaration(mods | JMod.VARARGS, type, name));
    }

    public JParamDeclaration varargParam(final JType type, final String name) {
        return varargParam(0, type, name);
    }

    public JParamDeclaration varargParam(final int mods, final String type, final String name) {
        return varargParam(mods, JTypes.typeNamed(type), name);
    }

    public JParamDeclaration varargParam(final String type, final String name) {
        return varargParam(JTypes.typeNamed(type), name);
    }

    public JParamDeclaration varargParam(final int mods, final Class<?> type, final String name) {
        return varargParam(mods, JTypes.typeOf(type), name);
    }

    public JParamDeclaration varargParam(final Class<?> type, final String name) {
        return varargParam(JTypes.typeOf(type), name);
    }

    public JParamDeclaration[] params() {
        return params.toArray(new JParamDeclaration[params.size()]);
    }

    public JComment _throws(final String type) {
        return _throws(JTypes.typeNamed(type));
    }

    public JComment _throws(final JType type) {
        if (_throws == null) {
            _throws = new ArrayList<>();
        }
        _throws.add(AbstractJType.of(type));
        // todo
        return null;
    }

    public JComment _throws(final Class<? extends Throwable> type) {
        return _throws(JTypes.typeOf(type));
    }

    int getModifiers() {
        return mods;
    }

    boolean writeBody() {
        return clazz.methodCanHaveBody(mods);
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writer.write(FormatPreferences.Space.BEFORE_PAREN_METHOD_DECLARATION);
        writer.write($PUNCT.PAREN.OPEN);
        if (params != null) {
            writer.write(FormatPreferences.Space.WITHIN_PAREN_METHOD_DECLARATION);
            final Iterator<ImplJParamDeclaration> iterator = params.iterator();
            if (iterator.hasNext()) {
                iterator.next().write(writer);
                while (iterator.hasNext()) {
                    writer.write(FormatPreferences.Space.BEFORE_COMMA);
                    writer.write($PUNCT.COMMA);
                    writer.write(FormatPreferences.Space.AFTER_COMMA);
                    iterator.next().write(writer);
                }
            }
        } else {
            writer.write(FormatPreferences.Space.WITHIN_PAREN_METHOD_DECLARATION_EMPTY);
        }
        writer.write($PUNCT.PAREN.CLOSE);
        if (_throws != null) {
            final Iterator<AbstractJType> iterator = _throws.iterator();
            if (iterator.hasNext()) {
                writer.sp();
                writer.write($KW.THROWS);
                writer.write(iterator.next());
                while (iterator.hasNext()) {
                    writer.write(FormatPreferences.Space.BEFORE_COMMA);
                    writer.write($PUNCT.COMMA);
                    writer.write(FormatPreferences.Space.AFTER_COMMA);
                    writer.write(iterator.next());
                }
            }
        }
        writePostfix(writer);
        if (! writeBody()) {
            writer.write($PUNCT.SEMI);
        } else {
            writer.write(FormatPreferences.Space.BEFORE_BRACE_METHOD);
            if (body == null) {
                writer.write($PUNCT.BRACE.OPEN);
                writer.write(FormatPreferences.Space.WITHIN_BRACES_EMPTY);
                writer.write($PUNCT.BRACE.CLOSE);
            } else {
                body.write(writer);
            }
        }
    }

    void writePostfix(final SourceFileWriter writer) throws IOException {
    }

    AbstractJClassDef clazz() {
        return clazz;
    }
}
