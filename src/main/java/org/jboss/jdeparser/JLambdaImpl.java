/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015 Red Hat, Inc., and individual contributors
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
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class JLambdaImpl extends AbstractJExpr implements JLambda {
    private BasicJBlock blockBody;
    private AbstractJExpr exprBody;
    private boolean hasInferred;
    private ArrayList<Param> params;

    JLambdaImpl() {
        super(Prec.METHOD_CALL);
    }

    public JLambda param(final JType type, final String name) {
        if (name != null) {
            if (type == null) {
                hasInferred = true;
            }
            if (params == null) {
                params = new ArrayList<>();
            }
            params.add(new Param(type, name));
        }
        return this;
    }

    public JLambda param(final String typeName, final String name) {
        return param(JTypes.typeNamed(typeName), name);
    }

    public JLambda param(final Class<?> type, final String name) {
        return param(JTypes.typeOf(type), name);
    }

    public JLambda param(final String name) {
        return param((JType) null, name);
    }

    public JBlock body() {
        exprBody = null;
        if (blockBody == null) {
            blockBody = new BasicJBlock(null, JBlock.Braces.REQUIRED);
        }
        return blockBody;
    }

    public JLambda body(JExpr expression) {
        blockBody = null;
        exprBody = AbstractJExpr.of(expression);
        return this;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        if (params == null) {
            writer.write(Tokens.$PUNCT.PAREN.OPEN);
            writer.write(FormatPreferences.Space.WITHIN_PAREN_METHOD_CALL_EMPTY);
            writer.write(Tokens.$PUNCT.PAREN.CLOSE);
        } else {
            final boolean singleParam = params.size() == 1 && hasInferred;
            if (! singleParam) {
                writer.write(Tokens.$PUNCT.PAREN.OPEN);
                writer.write(FormatPreferences.Space.WITHIN_PAREN_METHOD_CALL);
            }
            final Iterator<Param> iterator = params.iterator();
            while (iterator.hasNext()) {
                final Param param = iterator.next();
                if (!hasInferred) {
                    writer.write(param.type);
                    writer.sp();
                }
                writer.writeEscaped(param.name);
                if (iterator.hasNext()) {
                    writer.write(Tokens.$PUNCT.COMMA);
                    writer.write(FormatPreferences.Space.AFTER_COMMA);
                }
            }
            if (! singleParam) {
                writer.write(FormatPreferences.Space.WITHIN_PAREN_METHOD_CALL);
                writer.write(Tokens.$PUNCT.PAREN.CLOSE);
            }
        }
        writer.write(FormatPreferences.Space.AROUND_ARROW);
        writer.write(Tokens.$PUNCT.BINOP.ARROW);
        writer.write(FormatPreferences.Space.AROUND_ARROW);
        if (exprBody != null) {
            exprBody.write(writer);
        } else if (blockBody != null) {
            blockBody.write(writer, null);
        } else {
            writer.write(Tokens.$PUNCT.BRACE.OPEN);
            writer.write(FormatPreferences.Space.WITHIN_BRACES_EMPTY);
            writer.write(Tokens.$PUNCT.BRACE.CLOSE);
        }
    }

    static class Param {
        private final JType type;
        private final String name;

        public Param(final JType type, final String name) {
            this.type = type;
            this.name = name;
        }

        public JType getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }
}
