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
abstract class AbstractJCall extends AbstractJExpr implements JCall, AllowedStatementExpression {

    private ArrayList<AbstractJType> typeArgs;
    private ArrayList<AbstractJExpr> args;
    private BasicJCommentable commentable;

    AbstractJCall(final int prec) {
        super(prec);
    }

    public JCall diamond() {
        throw new UnsupportedOperationException("Adding diamond to method call");
    }

    public JCall typeArg(final JType type) {
        if (typeArgs == null) {
            typeArgs = new ArrayList<>();
        }
        typeArgs.add((AbstractJType) type);
        return this;
    }

    public JCall typeArg(final String type) {
        return typeArg(JTypes.typeNamed(type));
    }

    public JCall typeArg(final Class<?> type) {
        return typeArg(JTypes.typeOf(type));
    }

    public JCall arg(final JExpr expr) {
        if (args == null) {
            args = new ArrayList<>();
        }
        args.add((AbstractJExpr) expr);
        return this;
    }

    public JType[] typeArguments() {
        return typeArgs.toArray(new JType[typeArgs.size()]);
    }

    public JExpr[] arguments() {
        return args.toArray(new JExpr[args.size()]);
    }

    public JComment lineComment() {
        if (commentable == null) {
            commentable = new BasicJCommentable();
        }
        return commentable.lineComment();
    }

    public JComment blockComment() {
        if (commentable == null) {
            commentable = new BasicJCommentable();
        }
        return commentable.blockComment();
    }

    void writeTypeArgs(final SourceFileWriter writer) throws IOException {
        if (typeArgs != null) {
            final Iterator<AbstractJType> iterator = typeArgs.iterator();
            if (iterator.hasNext()) {
                writer.write($PUNCT.ANGLE.OPEN);
                writer.write(iterator.next());
                while (iterator.hasNext()) {
                    writer.write($PUNCT.COMMA);
                    writer.write(FormatPreferences.Space.AFTER_COMMA_TYPE_ARGUMENT);
                    writer.write(iterator.next());
                }
                writer.write($PUNCT.ANGLE.CLOSE);
            }
        }
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writer.write(FormatPreferences.Space.BEFORE_PAREN_METHOD_CALL);
        writer.write($PUNCT.PAREN.OPEN);
        writer.write(FormatPreferences.Space.WITHIN_PAREN_METHOD_DECLARATION);
        if (args != null) {
            final Iterator<AbstractJExpr> iterator = args.iterator();
            if (iterator.hasNext()) {
                writer.write(iterator.next());
                while (iterator.hasNext()) {
                    writer.write($PUNCT.COMMA);
                    writer.write(FormatPreferences.Space.AFTER_COMMA);
                    writer.write(iterator.next());
                }
            }
        }
        writer.write(FormatPreferences.Space.WITHIN_PAREN_METHOD_DECLARATION);
        writer.write($PUNCT.PAREN.CLOSE);
    }
}
