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

import static org.jboss.jdeparser.FormatStates.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
abstract class AbstractJMethodDef extends AbstractJGeneric implements JMethodDef, ClassContent {

    private final AbstractJClassDef clazz;
    private int mods;
    private ArrayList<ImplJParamDef> params;
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
            final JMethodDef enclosingMethod = clazz.enclosingMethod();
            body = new BasicJBlock(enclosingMethod == null ? null : (BasicJBlock) enclosingMethod.body());
        }
        return body;
    }

    public JComment returnsDoc() {
        return null;
    }

    private ImplJParamDef add(ImplJParamDef item) {
        if (params == null) {
            params = new ArrayList<>();
        }
        params.add(item);
        return item;
    }

    public JParamDef param(final int mods, final JType type, final String name) {
        if (JMod.anyAreSet(mods, JMod.VARARGS)) {
            throw new IllegalStateException("Vararg parameter already added");
        }
        return add(new ImplJParamDef(mods, type, name));
    }

    public JParamDef param(final JType type, final String name) {
        if (JMod.anyAreSet(mods, JMod.VARARGS)) {
            throw new IllegalStateException("Vararg parameter already added");
        }
        return add(new ImplJParamDef(0, type, name));
    }

    public JParamDef varargParam(final int mods, final JType type, final String name) {
        if (JMod.anyAreSet(mods, JMod.VARARGS)) {
            throw new IllegalStateException("Vararg parameter already added");
        }
        this.mods |= JMod.VARARGS;
        return add(new ImplJParamDef(mods | JMod.VARARGS, type, name));
    }

    public JParamDef varargParam(final JType type, final String name) {
        if (JMod.anyAreSet(mods, JMod.VARARGS)) {
            throw new IllegalStateException("Vararg parameter already added");
        }
        mods |= JMod.VARARGS;
        return add(new ImplJParamDef(JMod.VARARGS, type, name));
    }

    public JParamDef[] params() {
        return params.toArray(new JParamDef[params.size()]);
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

    int mods() {
        return mods;
    }

    boolean writeBody() {
        return JMod.allAreClear(mods, JMod.ABSTRACT | JMod.NATIVE);
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writer.write($PUNCT.PAREN.OPEN);
        if (params != null) {
            writer.pushStateContext(FormatStateContext.METHOD_PARAMS);
            try {
                final Iterator<ImplJParamDef> iterator = params.iterator();
                if (iterator.hasNext()) {
                    iterator.next().write(writer);
                    while (iterator.hasNext()) {
                        writer.write($PUNCT.COMMA);
                        iterator.next().write(writer);
                    }
                }
            } finally {
                writer.popStateContext(FormatStateContext.METHOD_PARAMS);
            }
        }
        writer.write($PUNCT.PAREN.CLOSE);
        if (_throws != null) {
            final Iterator<AbstractJType> iterator = _throws.iterator();
            if (iterator.hasNext()) {
                writer.write($KW.THROWS);
                writer.write(iterator.next());
                while (iterator.hasNext()) {
                    writer.write($PUNCT.COMMA);
                    iterator.next().write(writer);
                }
            }
        }
        if (! writeBody()) {
            writer.write($PUNCT.SEMI);
        } else {
            writer.pushStateContext(FormatStateContext.METHOD);
            try {
                if (body == null) {
                    writer.write($PUNCT.BRACE.OPEN);
                    writer.write($PUNCT.BRACE.CLOSE);
                } else {
                    body.write(writer);
                }
            } finally {
                writer.popStateContext(FormatStateContext.METHOD);
            }
        }
    }
}
