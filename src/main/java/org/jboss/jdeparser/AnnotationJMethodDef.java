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

import static org.jboss.jdeparser.Tokens.$KW;

import java.io.IOException;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class AnnotationJMethodDef extends MethodJMethodDef {

    private JExpr defaultVal;

    AnnotationJMethodDef(final AbstractJClassDef enclosingClass, final int mods, final JType returnType, final String name) {
        super(enclosingClass, mods, returnType, name);
    }

    public JBlock body() {
        throw new UnsupportedOperationException("body on annotation interface method");
    }

    public JParamDeclaration param(final int mods, final JType type, final String name) {
        throw new UnsupportedOperationException("param on annotation interface method");
    }

    public JParamDeclaration varargParam(final int mods, final JType type, final String name) {
        throw new UnsupportedOperationException("param on annotation interface method");
    }

    public JComment _throws(final JType type) {
        throw new UnsupportedOperationException("throws on annotation interface method");
    }

    public JMethodDef _default(final JExpr defaultVal) {
        this.defaultVal = defaultVal;
        return this;
    }

    boolean writeBody() {
        return false;
    }

    void writePostfix(final SourceFileWriter writer) throws IOException {
        if (defaultVal != null) {
            writer.write($KW.DEFAULT);
            writer.write(defaultVal);
        }
    }
}
