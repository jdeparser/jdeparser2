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

import java.util.LinkedHashMap;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;

/**
 * The factory for generating simple expressions.
 */
public final class JExprs {
    private JExprs() {}

    /**
     * Generate an {@code int} value in decimal base.
     *
     * @param val the value
     * @return the value expression
     */
    public static JExpr decimal(int val) {
        return new IntegerJExpr(val, 10);
    }

    /**
     * Generate a {@code long} value in decimal base.
     *
     * @param val the value
     * @return the value expression
     */
    public static JExpr decimal(long val) {
        return new LongJExpr(val, 10);
    }

    /**
     * Generate a {@code float} value in decimal base.
     *
     * @param val the value
     * @return the value expression
     */
    public static JExpr decimal(float val) {
        return new DecimalFloatJExpr(val);
    }

    /**
     * Generate a {@code double} value in decimal base.
     *
     * @param val the value
     * @return the value expression
     */
    public static JExpr decimal(double val) {
        return new DecimalDoubleJExpr(val);
    }

    /**
     * Generate an {@code int} value in hexadecimal base.
     *
     * @param val the value
     * @return the value expression
     */
    public static JExpr hex(int val) {
        return new IntegerJExpr(val, 16);
    }

    /**
     * Generate a {@code long} value in hexadecimal base.
     *
     * @param val the value
     * @return the value expression
     */
    public static JExpr hex(long val) {
        return new LongJExpr(val, 16);
    }

    /**
     * Generate a {@code float} value in hexadecimal base.
     *
     * @param val the value
     * @return the value expression
     */
    public static JExpr hex(float val) {
        return new HexFloatJExpr(val);
    }

    /**
     * Generate a {@code double} value in hexadecimal base.
     *
     * @param val the value
     * @return the value expression
     */
    public static JExpr hex(double val) {
        return new HexDoubleJExpr(val);
    }

    /**
     * Generate an {@code int} value in binary base.
     *
     * @param val the value
     * @return the value expression
     */
    public static JExpr binary(int val) {
        return new IntegerJExpr(val, 2);
    }

    /**
     * Generate a {@code long} value in binary base.
     *
     * @param val the value
     * @return the value expression
     */
    public static JExpr binary(long val) {
        return new LongJExpr(val, 2);
    }

    /**
     * Generate a string constant value.
     *
     * @param string the string
     * @return the string expression
     */
    public static JExpr str(String string) {
        return new StringJExpr(string);
    }

    /**
     * Generate a {@code char} value constant.
     *
     * @param val the value
     * @return the value expression
     */
    public static JExpr ch(int val) {
        return new CharJExpr(val);
    }

    /**
     * Generate a method call expression to the given element.
     *
     * @param element the method to call
     * @return the method call
     */
    public static JCall call(final ExecutableElement element) {
        final ElementKind kind = element.getKind();
        if (kind == ElementKind.METHOD) {
            final String name = element.getSimpleName().toString();
            return call(name);
        }
        throw new IllegalArgumentException("Unsupported element for call: " + element);
    }

    /**
     * Generate a method call expression to the given method name.
     *
     * @param name the name of the method to call
     * @return the method call
     */
    public static JCall call(String name) {
        return new DirectJCall(name);
    }

    /**
     * Generate a method call expression to a method on the given static type.
     *
     * @param type the type to call against
     * @param name the name of the method to call
     * @return the method call
     */
    public static JCall callStatic(final String type, final String name) {
        return callStatic(JTypes.typeNamed(type), name);
    }

    /**
     * Generate a method call expression to a method on the given static type.
     *
     * @param type the type to call against
     * @param name the name of the method to call
     * @return the method call
     */
    public static JCall callStatic(final JType type, final String name) {
        return new StaticJCall(AbstractJType.of(type), name);
    }

    /**
     * Generate a method call expression to a method on the given static type.
     *
     * @param type the type to call against
     * @param name the name of the method to call
     * @return the method call
     */
    public static JCall callStatic(final Class<?> type, final String name) {
        return callStatic(JTypes.typeOf(type), name);
    }

    /**
     * Return a name expression.  This method is a shortcut for {@link #name(String)}.
     *
     * @param name the name
     * @return the expression
     */
    public static JAssignableExpr $v(String name) {
        return name(name);
    }

    /**
     * Return a name expression from an annotation processor parameter declaration.
     *
     * @param paramDeclaration the method parameter
     * @return the expression
     */
    public static JAssignableExpr $v(JParamDeclaration paramDeclaration) {
        return name(paramDeclaration.name());
    }

    /**
     * Return a name expression from an annotation processor variable declaration.
     *
     * @param varDeclaration the variable declaration
     * @return the expression
     */
    public static JAssignableExpr $v(JVarDeclaration varDeclaration) {
        return name(varDeclaration.name());
    }

    static final ThreadLocal<LinkedHashMap<String, JAssignableExpr>> cache = new CachingThreadLocal<>();

    /**
     * Return a name expression.
     *
     * @param name the name
     * @return the expression
     */
    public static JAssignableExpr name(String name) {
        final LinkedHashMap<String, JAssignableExpr> map = cache.get();
        JAssignableExpr ret = map.get(name);
        if (ret == null) {
            map.put(name, ret = new NameJExpr(name));
        }
        return ret;
    }

    /**
     * Return a name expression from an annotation processor parameter declaration.
     *
     * @param paramDeclaration the method parameter
     * @return the expression
     */
    public static JAssignableExpr name(JParamDeclaration paramDeclaration) {
        return name(paramDeclaration.name());
    }

    /**
     * Return a name expression from an annotation processor variable declaration.
     *
     * @param varDeclaration the variable declaration
     * @return the expression
     */
    public static JAssignableExpr name(JVarDeclaration varDeclaration) {
        return name(varDeclaration.name());
    }

    /**
     * Return a new array expression.  The array is initially empty.
     *
     * @return an array expression
     */
    public static JArrayExpr array() {
        return new PlainJArrayExpr();
    }

    /**
     * Return a new array expression.  The array is initially filled with the given members.
     *
     * @param members the initial members of the array
     * @return an array expression
     */
    public static JArrayExpr array(JExpr... members) {
        return new PlainJArrayExpr(members);
    }

    /**
     * Return a new array expression.  The array is initially filled with the given strings.
     *
     * @param members the initial members of the array
     * @return an array expression
     */
    public static JArrayExpr array(String... members) {
        final JExpr[] exprs = new JExpr[members.length];
        for (int i = 0; i < members.length; i++) {
            exprs[i] = str(members[i]);
        }
        return new PlainJArrayExpr(exprs);
    }

    /**
     * Return a new array expression.  The array is initially filled with the given integers.
     *
     * @param members the initial members of the array
     * @return an array expression
     */
    public static JArrayExpr array(int... members) {
        final JExpr[] exprs = new JExpr[members.length];
        for (int i = 0; i < members.length; i++) {
            exprs[i] = decimal(members[i]);
        }
        return new PlainJArrayExpr(exprs);
    }

    /**
     * Return a new array expression.  The array is initially filled with the given integers.
     *
     * @param members the initial members of the array
     * @return an array expression
     */
    public static JArrayExpr array(long... members) {
        final JExpr[] exprs = new JExpr[members.length];
        for (int i = 0; i < members.length; i++) {
            exprs[i] = decimal(members[i]);
        }
        return new PlainJArrayExpr(exprs);
    }

    /**
     * Return a lambda expression.  The expression is initially empty.
     *
     * @return the lambda expression
     */
    public static JLambda lambda() {
        return new JLambdaImpl();
    }
}
