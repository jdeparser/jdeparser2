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
abstract class AbstractJAssignableExpr extends AbstractJExpr implements JAssignableExpr {

    private BasicJCommentable commentable;

    protected AbstractJAssignableExpr(final int prec) {
        super(prec);
    }

    public JExpr preDec() {
        return new IncDecJExpr($PUNCT.UNOP.MM, this, Prec.PRE_INC_DEC);
    }

    public JExpr preInc() {
        return new IncDecJExpr($PUNCT.UNOP.PP, this, Prec.PRE_INC_DEC);
    }

    public JExpr postDec() {
        return new IncDecJExpr($PUNCT.UNOP.MM, this, Prec.POST_INC_DEC, true);
    }

    public JExpr postInc() {
        return new IncDecJExpr($PUNCT.UNOP.PP, this, Prec.POST_INC_DEC, true);
    }

    public JExpr shlAssign(final JExpr e1) {
        return new AssignmentJExpr($PUNCT.BINOP.ASSIGN_SHL, this, (AbstractJExpr) e1);
    }

    public JExpr lshrAssign(final JExpr e1) {
        return new AssignmentJExpr($PUNCT.BINOP.ASSIGN_LSHR, this, (AbstractJExpr) e1);
    }

    public JExpr shrAssign(final JExpr e1) {
        return new AssignmentJExpr($PUNCT.BINOP.ASSIGN_SHR, this, (AbstractJExpr) e1);
    }

    public JExpr xorAssign(final JExpr e1) {
        return new AssignmentJExpr($PUNCT.BINOP.ASSIGN_BXOR, this, (AbstractJExpr) e1);
    }

    public JExpr orAssign(final JExpr e1) {
        return new AssignmentJExpr($PUNCT.BINOP.ASSIGN_BOR, this, (AbstractJExpr) e1);
    }

    public JExpr andAssign(final JExpr e1) {
        return new AssignmentJExpr($PUNCT.BINOP.ASSIGN_BAND, this, (AbstractJExpr) e1);
    }

    public JExpr modAssign(final JExpr e1) {
        return new AssignmentJExpr($PUNCT.BINOP.ASSIGN_MOD, this, (AbstractJExpr) e1);
    }

    public JExpr divAssign(final JExpr e1) {
        return new AssignmentJExpr($PUNCT.BINOP.ASSIGN_DIV, this, (AbstractJExpr) e1);
    }

    public JExpr mulAssign(final JExpr e1) {
        return new AssignmentJExpr($PUNCT.BINOP.ASSIGN_TIMES, this, (AbstractJExpr) e1);
    }

    public JExpr subAssign(final JExpr e1) {
        return new AssignmentJExpr($PUNCT.BINOP.ASSIGN_MINUS, this, (AbstractJExpr) e1);
    }

    public JExpr addAssign(final JExpr e1) {
        return new AssignmentJExpr($PUNCT.BINOP.ASSIGN_PLUS, this, (AbstractJExpr) e1);
    }

    public JExpr assign(final JExpr e1) {
        return new AssignmentJExpr($PUNCT.BINOP.ASSIGN, this, (AbstractJExpr) e1);
    }

    private BasicJCommentable commentable() {
        if (commentable == null) {
            commentable = new BasicJCommentable();
        }
        return commentable;
    }

    public JComment lineComment() {
        return commentable().lineComment();
    }

    public JComment blockComment() {
        return commentable().blockComment();
    }
}
