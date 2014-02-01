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
import static org.jboss.jdeparser.FormatStates.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJVarDeclaration extends BasicJAnnotatable implements JVarDeclaration, BlockContent {

    private final int mods;
    private final JType type;
    private final ArrayList<Var> vars = new ArrayList<>(1);

    ImplJVarDeclaration(final int mods, final JType type, final String name, final JExpr value) {
        this.mods = mods;
        this.type = type;
        vars.add(new Var(name, value));
    }

    public void write(final SourceFileWriter writer) throws IOException {
        super.writeAnnotations(writer);
        JMod.write(writer, mods);
        writer.write(type);
        final Iterator<Var> iterator = vars.iterator();
        if (alwaysTrue(iterator.hasNext())) {
            Var var;
            JExpr value;
            var = iterator.next();
            writer.writeIdentifier(var.getName());
            value = var.getValue();
            if (value != null) {
                writer.write($PUNCT.BINOP.ASSIGN);
                writer.write(value);
            }
            while (iterator.hasNext()) {
                writer.write($PUNCT.COMMA);
                var = iterator.next();
                writer.writeIdentifier(var.getName());
                value = var.getValue();
                if (value != null) {
                    writer.write($PUNCT.BINOP.ASSIGN);
                    writer.write(value);
                }
            }
        }
        writer.write($PUNCT.SEMI);
    }

    public JType type() {
        return type;
    }

    public JVarDeclaration add(final String name, final JExpr init) {
        vars.add(new Var(name, init));
        return this;
    }

    public JVarDeclaration add(final String name) {
        vars.add(new Var(name, null));
        return this;
    }

    static class Var {
        private String name;
        private JExpr value;

        Var(final String name, final JExpr value) {
            this.name = name;
            this.value = value;
        }

        String getName() {
            return name;
        }

        JExpr getValue() {
            return value;
        }
    }
}
