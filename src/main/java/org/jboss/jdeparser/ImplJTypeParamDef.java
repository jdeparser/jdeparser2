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
class ImplJTypeParamDef implements JTypeParamDef {

    private final String name;
    private ArrayList<JType> _extends;
    private ArrayList<JType> _super;

    ImplJTypeParamDef(final String name) {
        this.name = name;
    }

    public JTypeParamDef _extends(final String type) {
        return _extends(JTypes.typeNamed(type));
    }

    public JTypeParamDef _extends(final JType type) {
        if (_super != null) {
            throw new IllegalArgumentException("Cannot mix extends and super bounds");
        }
        if (_extends == null) {
            _extends = new ArrayList<>();
        }
        _extends.add(type);
        return this;
    }

    public JTypeParamDef _extends(final Class<?> type) {
        return _extends(JTypes.typeOf(type));
    }

    public JTypeParamDef _super(final String type) {
        return _super(JTypes.typeNamed(type));
    }

    public JTypeParamDef _super(final JType type) {
        if (_extends != null) {
            throw new IllegalArgumentException("Cannot mix extends and super bounds");
        }
        if (_super == null) {
            _super = new ArrayList<>();
        }
        _super.add(type);
        return this;
    }

    public JTypeParamDef _super(final Class<?> type) {
        return _super(JTypes.typeOf(type));
    }

    String getName() {
        return name;
    }

    Iterable<JType> getExtends() {
        return _extends;
    }

    Iterable<JType> getSuper() {
        return _super;
    }

    private void writeList(final SourceFileWriter sourceFileWriter, ArrayList<JType> list, $KW keyword) throws IOException {
        if (list == null) {
            return;
        }
        final Iterator<JType> iterator = list.iterator();
        if (! iterator.hasNext()) {
            return;
        }
        JType type = iterator.next();
        sourceFileWriter.write(keyword);
        sourceFileWriter.write(type);
        while (iterator.hasNext()) {
            type = iterator.next();
            sourceFileWriter.write(FormatPreferences.Space.AROUND_BITWISE);
            sourceFileWriter.write($PUNCT.BINOP.BAND);
            sourceFileWriter.write(FormatPreferences.Space.AROUND_BITWISE);
            sourceFileWriter.write(type);
        }
    }

    void write(final SourceFileWriter sourceFileWriter) throws IOException {
        sourceFileWriter.writeClass(name);
        writeList(sourceFileWriter, _extends, $KW.EXTENDS);
        writeList(sourceFileWriter, _super, $KW.SUPER);
    }
}
