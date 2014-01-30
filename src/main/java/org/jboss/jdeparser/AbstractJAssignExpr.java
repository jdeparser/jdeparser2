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
abstract class AbstractJAssignExpr extends AbstractJExpr implements JAssignExpr {

    private BasicJCommentable commentable;

    protected AbstractJAssignExpr(final int prec) {
        super(prec);
    }

    public JExpr preDec() {
        return new IncDecJExpr("--", this, Prec.PRE_INC_DEC);
    }

    public JExpr preInc() {
        return new IncDecJExpr("++", this, Prec.PRE_INC_DEC);
    }

    public JExpr postDec() {
        return new IncDecJExpr("--", this, Prec.POST_INC_DEC, true);
    }

    public JExpr postInc() {
        return new IncDecJExpr("++", this, Prec.POST_INC_DEC, true);
    }

    public JExpr shlAssign(final JExpr e1) {
        return new AssignmentJExpr("<<=", this, (AbstractJExpr) e1);
    }

    public JExpr lshrAssign(final JExpr e1) {
        return new AssignmentJExpr("<<<=", this, (AbstractJExpr) e1);
    }

    public JExpr shrAssign(final JExpr e1) {
        return new AssignmentJExpr(">>=", this, (AbstractJExpr) e1);
    }

    public JExpr xorAssign(final JExpr e1) {
        return new AssignmentJExpr("^=", this, (AbstractJExpr) e1);
    }

    public JExpr orAssign(final JExpr e1) {
        return new AssignmentJExpr("|=", this, (AbstractJExpr) e1);
    }

    public JExpr andAssign(final JExpr e1) {
        return new AssignmentJExpr("&=", this, (AbstractJExpr) e1);
    }

    public JExpr modAssign(final JExpr e1) {
        return new AssignmentJExpr("%=", this, (AbstractJExpr) e1);
    }

    public JExpr divAssign(final JExpr e1) {
        return new AssignmentJExpr("/=", this, (AbstractJExpr) e1);
    }

    public JExpr mulAssign(final JExpr e1) {
        return new AssignmentJExpr("*=", this, (AbstractJExpr) e1);
    }

    public JExpr subAssign(final JExpr e1) {
        return new AssignmentJExpr("-=", this, (AbstractJExpr) e1);
    }

    public JExpr addAssign(final JExpr e1) {
        return new AssignmentJExpr("+=", this, (AbstractJExpr) e1);
    }

    public JExpr assign(final JExpr e1) {
        return new AssignmentJExpr("=", this, (AbstractJExpr) e1);
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

    public Iterable<JComment> comments() {
        return commentable().comments();
    }
}
