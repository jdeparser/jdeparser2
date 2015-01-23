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

import static org.jboss.jdeparser.Tokens.$PUNCT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJEnumConstant extends BasicJAnnotatable implements JEnumConstant, JClassItem {

    private final EnumJClassDef classDef;
    private final String name;
    private ArrayList<AbstractJExpr> args;
    private EnumConstantJClassDef body;

    ImplJEnumConstant(final EnumJClassDef classDef, final String name) {
        this.classDef = classDef;
        this.name = name;
    }

    EnumJClassDef getClassDef() {
        return classDef;
    }

    public String getName() {
        return name;
    }

    public JEnumConstant arg(final JExpr expr) {
        if (args == null) {
            args = new ArrayList<>();
        }
        args.add((AbstractJExpr) expr);
        return this;
    }

    public JClassDef body() {
        return body != null ? body : (body = new EnumConstantJClassDef(this));
    }

    public JExpr[] arguments() {
        return args.toArray(new JExpr[args.size()]);
    }

    void writeDirect(final SourceFileWriter writer) throws IOException {
        writeComments(writer);
        if (args == null || args.isEmpty()) {
            if (writer.getFormat().hasOption(FormatPreferences.Opt.ENUM_EMPTY_PARENS)) {
                writer.write($PUNCT.PAREN.OPEN);
                writer.write(FormatPreferences.Space.WITHIN_PAREN_METHOD_CALL_EMPTY);
                writer.write($PUNCT.PAREN.CLOSE);
            }
        } else {
            writer.write($PUNCT.PAREN.OPEN);
            writer.write(FormatPreferences.Space.WITHIN_PAREN_METHOD_DECLARATION);
            final Iterator<AbstractJExpr> iterator = args.iterator();
            if (iterator.hasNext()) {
                writer.write(iterator.next());
                while (iterator.hasNext()) {
                    writer.write($PUNCT.COMMA);
                    writer.write(FormatPreferences.Space.AFTER_COMMA);
                    writer.write(iterator.next());
                }
            }
            writer.write(FormatPreferences.Space.WITHIN_PAREN_METHOD_DECLARATION);
            writer.write($PUNCT.PAREN.CLOSE);
        }
        if (body != null) {
            writer.write(FormatPreferences.Space.BEFORE_BRACE_CLASS);
            body.write(writer);
        }
    }

    public Kind getItemKind() {
        return Kind.ENUM_CONSTANT;
    }

    public int getModifiers() {
        return 0;
    }

    public boolean hasAllModifiers(final int mods) {
        return false;
    }

    public boolean hasAnyModifier(final int mods) {
        return false;
    }
}
