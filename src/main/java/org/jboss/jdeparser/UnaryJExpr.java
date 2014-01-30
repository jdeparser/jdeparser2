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
class UnaryJExpr extends AbstractJExpr {

    private final String string;
    private final AbstractJExpr expr;
    private final boolean postfix;

    UnaryJExpr(final String string, final AbstractJExpr expr) {
        this(string, expr, Prec.UNARY, false);
    }

    UnaryJExpr(final String string, final AbstractJExpr expr, final int prec) {
        this(string, expr, prec, false);
    }

    UnaryJExpr(final String string, final AbstractJExpr expr, final int prec, boolean postfix) {
        super(prec);
        this.string = string;
        this.expr = expr.prec() < prec ? new ParenJExpr(expr) : expr;
        this.postfix = postfix;
    }

    AbstractJExpr getExpression() {
        return expr;
    }

    String getString() {
        return string;
    }

    boolean isPostfix() {
        return postfix;
    }
}
