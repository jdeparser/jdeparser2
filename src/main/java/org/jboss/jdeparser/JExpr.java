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
 * A modelled expression.  Constructed complex expressions are reusable (their contents will be copied at every
 * use site).  The minimum number of parentheses will automatically be added as needed.  For convenience methods to
 * construct common expressions, also see the {@link JExprs} class.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JExpr {

    /**
     * The constant expression for {@code false}.
     */
    JExpr FALSE = new BooleanJExpr(false);

    /**
     * The constant expression for {@code true}.
     */
    JExpr TRUE = new BooleanJExpr(true);

    /**
     * The constant expression for {@code this}.
     */
    JExpr THIS = new KeywordJExpr($KW.THIS);

    /**
     * The constant expression for {@code null}.
     */
    JExpr NULL = new KeywordJExpr($KW.NULL);

    /**
     * The constant expression for the integer zero.
     */
    JExpr ZERO = new IntegerJExpr(0, 10);

    /**
     * The constant expression for the integer one.
     */
    JExpr ONE = new IntegerJExpr(1, 10);

    // arithmetic

    /**
     * Combine this expression with another using the binary {@code +} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr plus(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code -} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr minus(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code *} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr times(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code /} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr div(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code %} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr mod(JExpr e1);

    /**
     * Negate this expression using the unary {@code -} operator.
     *
     * @return the new expression
     */
    JExpr neg();

    // bitwise

    /**
     * Combine this expression with another using the binary {@code &} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr band(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code |} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr bor(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code ^} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr bxor(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code >>} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr shr(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code >>>} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr lshr(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code <<} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr shl(JExpr e1);

    /**
     * Invert this expression using the unary {@code ~} operator.
     *
     * @return the new expression
     */
    JExpr comp();

    // logic

    /**
     * Combine this expression with another using the binary {@code &&} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr and(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code ||} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr or(JExpr e1);

    /**
     * Invert this expression using the unary {@code !} operator.
     *
     * @return the new expression
     */
    JExpr not();

    // equality

    /**
     * Combine this expression with another using the binary {@code ==} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr eq(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code !=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr ne(JExpr e1);

    // range

    /**
     * Combine this expression with another using the binary {@code <} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr lt(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code >} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr gt(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code <=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr le(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code >=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr ge(JExpr e1);

    // ternary

    /**
     * Combine this expression with two others using the ternary {@code ? :} operator.
     *
     * @param ifTrue the {@code true} expression branch
     * @param ifFalse the {@code false} expression branch
     * @return the combined expression
     */
    JExpr cond(JExpr ifTrue, JExpr ifFalse);

    // paren

    /**
     * Explicitly wrap this expression in parentheses.
     *
     * @return the wrapped expression
     */
    JExpr paren();

    // instance

    /**
     * Get a type-testing expression using the {@code instanceof} operator.
     *
     * @param type the type to test
     * @return the expression
     */
    JExpr _instanceof(String type);

    /**
     * Get a type-testing expression using the {@code instanceof} operator.
     *
     * @param type the type to test
     * @return the expression
     */
    JExpr _instanceof(JType type);

    /**
     * Get a type-testing expression using the {@code instanceof} operator.
     *
     * @param type the type to test
     * @return the expression
     */
    JExpr _instanceof(Class<?> type);

    // cast

    /**
     * Get an expression which is a cast of this expression to the given type.
     *
     * @param type the type to cast to
     * @return the expression
     */
    JExpr cast(String type);

    /**
     * Get an expression which is a cast of this expression to the given type.
     *
     * @param type the type to cast to
     * @return the expression
     */
    JExpr cast(JType type);

    /**
     * Get an expression which is a cast of this expression to the given type.
     *
     * @param type the type to cast to
     * @return the expression
     */
    JExpr cast(Class<?> type);

    // invoke

    /**
     * Call the given method on this expression.
     *
     * @param name the method name
     * @return the method call
     */
    JCall call(String name);

    // construct inner

    /**
     * Get an expression to construct a new inner class instance of this instance expression.
     *
     * @param type the inner class type to construct
     * @return the {@code new} constructor call
     */
    JCall _new(String type);

    /**
     * Get an expression to construct a new inner class instance of this instance expression.
     *
     * @param type the inner class type to construct
     * @return the {@code new} constructor call
     */
    JCall _new(JType type);

    /**
     * Get an expression to construct a new inner class instance of this instance expression.
     *
     * @param type the inner class type to construct
     * @return the {@code new} constructor call
     */
    JCall _new(Class<?> type);

    /**
     * Construct a new anonymous subclass of the given type, which must be an inner class of the type of this
     * expression.
     *
     * @param type the type of object to construct
     * @return the anonymous subclass definition
     */
    JAnonymousClassDef _newAnon(String type);

    /**
     * Construct a new anonymous subclass of the given type, which must be an inner class of the type of this
     * expression.
     *
     * @param type the type of object to construct
     * @return the anonymous subclass definition
     */
    JAnonymousClassDef _newAnon(JType type);

    /**
     * Construct a new anonymous subclass of the given type, which must be an inner class of the type of this
     * expression.
     *
     * @param type the type of object to construct
     * @return the anonymous subclass definition
     */
    JAnonymousClassDef _newAnon(Class<?> type);

    // field

    /**
     * Get a field of this object instance.
     *
     * @param name the field name
     * @return the expression
     */
    JAssignableExpr field(String name);

    /**
     * Get a field of this object instance (shorthand for {@link #field(String)}.
     *
     * @param name the field name
     * @return the expression
     */
    JAssignableExpr $v(String name);

    // array

    /**
     * Get an element of this array expression.
     *
     * @param idx the array index expression
     * @return the array dereference expression
     */
    JAssignableExpr idx(JExpr idx);

    /**
     * Get an element of this array expression.
     *
     * @param idx the array index
     * @return the array dereference expression
     */
    JExpr idx(int idx);

    /**
     * Get the {@code length} expression of this array expression.
     *
     * @return the {@code length} expression
     */
    JExpr length();
}
