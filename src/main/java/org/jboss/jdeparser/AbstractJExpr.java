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

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
abstract class AbstractJExpr implements JExpr, Writable {

    private final int prec;

    protected AbstractJExpr(final int prec) {
        this.prec = prec;
    }

    static AbstractJExpr of(final JExpr expr) {
        if (expr instanceof AbstractJExpr) {
            return (AbstractJExpr) expr;
        }
        throw new IllegalArgumentException("Expression from different implementation");
    }

    public JExpr plus(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.PLUS, this, (AbstractJExpr) e1, Prec.ADDITIVE);
    }

    public JExpr minus(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.MINUS, this, (AbstractJExpr) e1, Prec.ADDITIVE);
    }

    public JExpr times(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.TIMES, this, (AbstractJExpr) e1, Prec.MULTIPLICATIVE);
    }

    public JExpr div(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.DIV, this, (AbstractJExpr) e1, Prec.MULTIPLICATIVE);
    }

    public JExpr mod(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.MOD, this, (AbstractJExpr) e1, Prec.MULTIPLICATIVE);
    }

    public JExpr neg() {
        return new UnaryJExpr($PUNCT.UNOP.MINUS, this);
    }

    public JExpr band(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.BAND, this, (AbstractJExpr) e1, Prec.BIT_AND);
    }

    public JExpr bor(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.BOR, this, (AbstractJExpr) e1, Prec.BIT_OR);
    }

    public JExpr bxor(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.BXOR, this, (AbstractJExpr) e1, Prec.BIT_XOR);
    }

    public JExpr shr(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.SHR, this, (AbstractJExpr) e1, Prec.SHIFT);
    }

    public JExpr lshr(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.LSHR, this, (AbstractJExpr) e1, Prec.SHIFT);
    }

    public JExpr shl(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.SHL, this, (AbstractJExpr) e1, Prec.SHIFT);
    }

    public JExpr comp() {
        return new UnaryJExpr($PUNCT.UNOP.COMP, this);
    }

    public JExpr and(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.LAND, this, (AbstractJExpr) e1, Prec.LOG_AND);
    }

    public JExpr or(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.LOR, this, (AbstractJExpr) e1, Prec.LOG_OR);
    }

    public JExpr not() {
        return new UnaryJExpr($PUNCT.UNOP.NOT, this);
    }

    public JExpr eq(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.EQ, this, (AbstractJExpr) e1, Prec.EQUALITY);
    }

    public JExpr ne(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.NE, this, (AbstractJExpr) e1, Prec.EQUALITY);
    }

    public JExpr lt(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.LT, this, (AbstractJExpr) e1, Prec.RELATIONAL);
    }

    public JExpr gt(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.GT, this, (AbstractJExpr) e1, Prec.RELATIONAL);
    }

    public JExpr le(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.LE, this, (AbstractJExpr) e1, Prec.RELATIONAL);
    }

    public JExpr ge(final JExpr e1) {
        return new BinaryJExpr($PUNCT.BINOP.GE, this, (AbstractJExpr) e1, Prec.RELATIONAL);
    }

    public JExpr cond(final JExpr ifTrue, final JExpr ifFalse) {
        return new CondJExpr(this, (AbstractJExpr) ifTrue, (AbstractJExpr) ifFalse);
    }

    public JExpr paren() {
        return new ParenJExpr(this);
    }

    public JExpr _instanceof(final String type) {
        return _instanceof(JTypes.typeNamed(type));
    }

    public JExpr _instanceof(final JType type) {
        return new InstanceOfJExpr(this, type);
    }

    public JExpr _instanceof(final Class<?> type) {
        return _instanceof(JTypes.typeOf(type));
    }

    public JExpr cast(final String type) {
        return new CastJExpr(this, JTypes.typeNamed(type));
    }

    public JExpr cast(final JType type) {
        return new CastJExpr(this, type);
    }

    public JExpr cast(final Class<?> type) {
        return new CastJExpr(this, JTypes.typeOf(type));
    }

    public JCall call(final String name) {
        return new InstanceJCall(this, name);
    }

    public JCall _new(final String type) {
        return new InnerNewJCall(this, JTypes.typeNamed(type));
    }

    public JCall _new(final JType type) {
        return new InnerNewJCall(this, type);
    }

    public JCall _new(final Class<?> type) {
        return new InnerNewJCall(this, JTypes.typeOf(type));
    }

    public JAnonymousClassDef _newAnon(final String type) {
        return new InnerJAnonymousClassDef(this, JTypes.typeNamed(type));
    }

    public JAnonymousClassDef _newAnon(final JType type) {
        return new InnerJAnonymousClassDef(this, type);
    }

    public JAnonymousClassDef _newAnon(final Class<?> type) {
        return new InnerJAnonymousClassDef(this, JTypes.typeOf(type));
    }

    private CachingLinkedHashMap<String, JAssignableExpr> fieldCache;

    public JAssignableExpr field(final String name) {
        CachingLinkedHashMap<String, JAssignableExpr> map = fieldCache;
        if (map == null) {
            map = fieldCache = new CachingLinkedHashMap<>();
        }
        JAssignableExpr expr = map.get(name);
        if (expr == null) {
            map.put(name, expr = new FieldRefJExpr(this, name));
        }
        return expr;
    }

    public JAssignableExpr $v(final String name) {
        return field(name);
    }

    public JAssignableExpr idx(final JExpr idx) {
        return new ArrayLookupJExpr(this, (AbstractJExpr) idx);
    }

    public JExpr idx(final int idx) {
        return new ArrayLookupJExpr(this, (AbstractJExpr) JExprs.decimal(idx));
    }

    private JExpr length;

    public JExpr length() {
        JExpr length = this.length;
        if (length == null) {
            length = this.length = new FieldRefJExpr(this, "length");
        }
        return length;
    }

    public int prec() {
        return prec;
    }
}
