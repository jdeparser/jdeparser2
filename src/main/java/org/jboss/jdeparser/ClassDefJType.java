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
class ClassDefJType extends AbstractJType {

    private final JClassDef classDef;

    ClassDefJType(final JClassDef classDef) {
        this.classDef = classDef;
    }

    public JExpr _class() {
        return new StaticRefJExpr(this, "class");
    }

    public JExpr _this() {
        return new StaticRefJExpr(this, "this");
    }

    public JExpr _super() {
        return new StaticRefJExpr(this, "super");
    }

    public JCall _new(final JExpr dim) {
        return new NewJCall(this);
    }

    public JAnonymousClassDef _newAnon() {
        return new ImplJAnonymousClassDef(this);
    }

    public String simpleName() {
        return ((AbstractJClassDef) classDef).getName();
    }

    public JType typeArg(final JType... args) {
        return new NarrowedJType(this, args);
    }

    public JType nestedType(final String name) {
        return new NestedJType(this, name);
    }

    public String toString() {
        return "Anonymous instance of " + simpleName();
    }

    void writeDirect(final SourceFileWriter sourceFileWriter) throws IOException {
        sourceFileWriter.write(classDef.erasedType());
    }
}
