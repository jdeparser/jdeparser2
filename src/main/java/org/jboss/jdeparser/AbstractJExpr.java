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

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
abstract class AbstractJExpr implements JExpr {

    private final int prec;

    protected AbstractJExpr(final int prec) {
        this.prec = prec;
    }

    public JExpr add(final JExpr e1) {
        return new BinaryJExpr("+", this, (AbstractJExpr) e1, Prec.ADDITIVE);
    }

    public JExpr sub(final JExpr e1) {
        return new BinaryJExpr("-", this, (AbstractJExpr) e1, Prec.ADDITIVE);
    }

    public JExpr mul(final JExpr e1) {
        return new BinaryJExpr("*", this, (AbstractJExpr) e1, Prec.MULTIPLICATIVE);
    }

    public JExpr div(final JExpr e1) {
        return new BinaryJExpr("/", this, (AbstractJExpr) e1, Prec.MULTIPLICATIVE);
    }

    public JExpr mod(final JExpr e1) {
        return new BinaryJExpr("%", this, (AbstractJExpr) e1, Prec.MULTIPLICATIVE);
    }

    public JExpr neg() {
        return new UnaryJExpr("-", this);
    }

    public JExpr band(final JExpr e1) {
        return new BinaryJExpr("&", this, (AbstractJExpr) e1, Prec.BIT_AND);
    }

    public JExpr bor(final JExpr e1) {
        return new BinaryJExpr("|", this, (AbstractJExpr) e1, Prec.BIT_OR);
    }

    public JExpr bxor(final JExpr e1) {
        return new BinaryJExpr("^", this, (AbstractJExpr) e1, Prec.BIT_XOR);
    }

    public JExpr shr(final JExpr e1) {
        return new BinaryJExpr(">>", this, (AbstractJExpr) e1, Prec.SHIFT);
    }

    public JExpr lshr(final JExpr e1) {
        return new BinaryJExpr(">>>", this, (AbstractJExpr) e1, Prec.SHIFT);
    }

    public JExpr shl(final JExpr e1) {
        return new BinaryJExpr("<<", this, (AbstractJExpr) e1, Prec.SHIFT);
    }

    public JExpr comp() {
        return new UnaryJExpr("~", this);
    }

    public JExpr and(final JExpr e1) {
        return new BinaryJExpr("&&", this, (AbstractJExpr) e1, Prec.LOG_AND);
    }

    public JExpr or(final JExpr e1) {
        return new BinaryJExpr("||", this, (AbstractJExpr) e1, Prec.LOG_OR);
    }

    public JExpr not() {
        return new UnaryJExpr("!", this);
    }

    public JExpr eq(final JExpr e1) {
        return new BinaryJExpr("==", this, (AbstractJExpr) e1, Prec.EQUALITY);
    }

    public JExpr ne(final JExpr e1) {
        return new BinaryJExpr("!=", this, (AbstractJExpr) e1, Prec.EQUALITY);
    }

    public JExpr lt(final JExpr e1) {
        return new BinaryJExpr("<", this, (AbstractJExpr) e1, Prec.RELATIONAL);
    }

    public JExpr gt(final JExpr e1) {
        return new BinaryJExpr(">", this, (AbstractJExpr) e1, Prec.RELATIONAL);
    }

    public JExpr le(final JExpr e1) {
        return new BinaryJExpr("<=", this, (AbstractJExpr) e1, Prec.RELATIONAL);
    }

    public JExpr ge(final JExpr e1) {
        return new BinaryJExpr(">=", this, (AbstractJExpr) e1, Prec.RELATIONAL);
    }

    public JExpr cond(final JExpr ifTrue, final JExpr ifFalse) {
        return new CondJExpr(this, (AbstractJExpr) ifTrue, (AbstractJExpr) ifFalse);
    }

    public JExpr paren() {
        return new ParenJExpr(this);
    }

    public JExpr _instanceof(final JType type) {
        return new InstanceOfJExpr(this, type);
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

    public JCall _new(final String className) {
        return null;
    }

    public JAnonymousClassDef _newAnon(final String className) {
        return null;
    }

    public JAssignExpr field(final String name) {
        return new RefJExpr(this, name);
    }

    public JExpr idx(final JExpr idx) {
        return new ArrayLookupJExpr(this, (AbstractJExpr) idx);
    }

    public JExpr idx(final int idx) {
        return new ArrayLookupJExpr(this, (AbstractJExpr) JExprs.decimal(idx));
    }

    public JExpr length() {
        return new RefJExpr(this, "length");
    }

    public int prec() {
        return prec;
    }
}
