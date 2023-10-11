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
class ThisJType extends AbstractJType {

    ThisJType() {
    }

    @Override
    boolean equals(final AbstractJType other) {
        return other instanceof ThisJType;
    }

    @Override
    public int hashCode() {
        return 23;
    }

    @Override
    public String simpleName() {
        return "<<THIS>>";
    }

    @Override
    public String toString() {
        return "<<THIS>>";
    }

    @Override
    public JExpr _class() {
        return new StaticRefJExpr(this, "class");
    }

    @Override
    public JExpr _this() {
        return new StaticRefJExpr(this, "this");
    }

    @Override
    public JExpr _super() {
        return new StaticRefJExpr(this, "super");
    }

    @Override
    public JCall _new() {
        return new NewJCall(this);
    }

    @Override
    public JAnonymousClassDef _newAnon() {
        return new ImplJAnonymousClassDef(this);
    }

    @Override
    void writeDirect(final SourceFileWriter sourceFileWriter) throws IOException {
        sourceFileWriter.write(sourceFileWriter.getThisType());
    }
}
