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
 * A modelled expression.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JExpr {

    JExpr FALSE = new BooleanJExpr(false);

    JExpr TRUE = new BooleanJExpr(true);

    JExpr THIS = new NameJExpr("this");

    JExpr NULL = new NameJExpr("null");

    // arithmetic

    JExpr add(JExpr e1);

    JExpr sub(JExpr e1);

    JExpr mul(JExpr e1);

    JExpr div(JExpr e1);

    JExpr mod(JExpr e1);

    JExpr neg();

    // bitwise

    JExpr band(JExpr e1);

    JExpr bor(JExpr e1);

    JExpr bxor(JExpr e1);

    JExpr shr(JExpr e1);

    JExpr lshr(JExpr e1);

    JExpr shl(JExpr e1);

    JExpr comp();

    // logic

    JExpr and(JExpr e1);

    JExpr or(JExpr e1);

    JExpr not();

    // equality

    JExpr eq(JExpr e1);

    JExpr ne(JExpr e1);

    // range

    JExpr lt(JExpr e1);

    JExpr gt(JExpr e1);

    JExpr le(JExpr e1);

    JExpr ge(JExpr e1);

    // ternary

    JExpr cond(JExpr ifTrue, JExpr ifFalse);

    // paren

    JExpr paren();

    // instance

    JExpr _instanceof(JType type);

    // cast

    JExpr cast(String type);

    JExpr cast(JType type);

    JExpr cast(Class<?> type);

    // invoke

    JCall call(String name);

    JCall _new(String className);

    JAnonymousClassDef _newAnon(String className);

    // field

    JAssignExpr field(String name);

    // array

    JExpr idx(JExpr idx);

    JExpr idx(int idx);

    JExpr length();
}
