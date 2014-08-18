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
 * An expression which is assignable (that is, is a valid "lvalue").
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JAssignableExpr extends JExpr, JStatement {
    // assign

    /**
     * Combine this expression with another using the binary {@code =} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr assign(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code +=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr addAssign(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code -=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr subAssign(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code *=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr mulAssign(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code /=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr divAssign(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code %=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr modAssign(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code &=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr andAssign(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code |=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr orAssign(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code ^=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr xorAssign(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code >>=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr shrAssign(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code >>>=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr lshrAssign(JExpr e1);

    /**
     * Combine this expression with another using the binary {@code <<=} operator.
     *
     * @param e1 the other expression
     * @return the combined expression
     */
    JExpr shlAssign(JExpr e1);

    // inc/dec

    /**
     * Apply the postfix {@code ++} operator to this expression.
     *
     * @return the new expression
     */
    JExpr postInc();

    /**
     * Apply the postfix {@code --} operator to this expression.
     *
     * @return the new expression
     */
    JExpr postDec();

    /**
     * Apply the prefix {@code ++} operator to this expression.
     *
     * @return the new expression
     */
    JExpr preInc();

    /**
     * Apply the prefix {@code --} operator to this expression.
     *
     * @return the new expression
     */
    JExpr preDec();
}
