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
 * The factory for generating simple expressions.
 */
public final class JExprs {
    private JExprs() {}

    public static JExpr decimal(int val) {
        return null;
    }

    public static JExpr decimal(long val) {
        return null;
    }

    public static JExpr decimal(float val) {
        return null;
    }

    public static JExpr decimal(double val) {
        return null;
    }

    public static JExpr decimal(int val, String format) {
        return null;
    }

    public static JExpr decimal(long val, String format) {
        return null;
    }

    public static JExpr decimal(float val, String format) {
        return null;
    }

    public static JExpr decimal(double val, String format) {
        return null;
    }

    public static JExpr decimal(int val, int sepInterval) {
        return null;
    }

    public static JExpr decimal(long val, int sepInterval) {
        return null;
    }

    public static JExpr decimal(float val, int sepInterval) {
        return null;
    }

    public static JExpr decimal(double val, int sepInterval) {
        return null;
    }

    public static JExpr hex(int val) {
        return null;
    }

    public static JExpr hex(long val) {
        return null;
    }

    public static JExpr hex(float val) {
        return null;
    }

    public static JExpr hex(double val) {
        return null;
    }

    public static JExpr hex(int val, String format) {
        return null;
    }

    public static JExpr hex(long val, String format) {
        return null;
    }

    public static JExpr hex(float val, String format) {
        return null;
    }

    public static JExpr hex(double val, String format) {
        return null;
    }

    public static JExpr hex(int val, int sepInterval) {
        return null;
    }

    public static JExpr hex(long val, int sepInterval) {
        return null;
    }

    public static JExpr hex(float val, int sepInterval) {
        return null;
    }

    public static JExpr hex(double val, int sepInterval) {
        return null;
    }

    public static JExpr binary(int val) {
        return null;
    }

    public static JExpr binary(long val) {
        return null;
    }

    public static JExpr binary(int val, String format) {
        return null;
    }

    public static JExpr binary(long val, String format) {
        return null;
    }

    public static JExpr binary(int val, int sepInterval) {
        return null;
    }

    public static JExpr binary(long val, int sepInterval) {
        return null;
    }

    public static JExpr str(String string) {
        return null;
    }

    public static JExpr ch(int val) {
        return null;
    }

    public static JExpr name(String name) {
        return new NameJExpr(name);
    }

    public static JExpr array(JExpr... members) {
        return new ArrayJExpr(members);
    }

    public static JExpr array(String... members) {
        final JExpr[] exprs = new JExpr[members.length];
        for (int i = 0; i < members.length; i++) {
            exprs[i] = str(members[i]);
        }
        return new ArrayJExpr(exprs);
    }

    public static JExpr array(int... members) {
        final JExpr[] exprs = new JExpr[members.length];
        for (int i = 0; i < members.length; i++) {
            exprs[i] = decimal(members[i]);
        }
        return new ArrayJExpr(exprs);
    }

    public static JExpr array(long... members) {
        final JExpr[] exprs = new JExpr[members.length];
        for (int i = 0; i < members.length; i++) {
            exprs[i] = decimal(members[i]);
        }
        return new ArrayJExpr(exprs);
    }
}
