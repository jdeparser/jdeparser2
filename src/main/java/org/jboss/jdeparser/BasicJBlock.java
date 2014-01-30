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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class BasicJBlock extends BasicJCommentable implements JBlock {
    private final BasicJBlock parent;
    private final ArrayList<JStatement> content = new ArrayList<>();
    private boolean forceBrackets;
    private int tmpId = 1;

    BasicJBlock(final BasicJBlock parent) {
        this(parent, true);
    }

    BasicJBlock(final BasicJBlock parent, final boolean forceBrackets) {
        this.parent = parent;
        this.forceBrackets = forceBrackets;
    }

    private <T extends JStatement> T add(T s) {
        content.add(s);
        return s;
    }

    public JStatement[] content() {
        return content.toArray(new JStatement[content.size()]);
    }

    public JBlock block(final boolean forceBrackets) {
        return add(new BasicJBlock(this, forceBrackets));
    }

    public JIf _if(final JExpr cond) {
        return add(new ImplJIf(this, cond));
    }

    public JBlock _while(final JExpr cond) {
        return add(new WhileJBlock(this, cond));
    }

    public JBlock _do(final JExpr cond) {
        return add(new DoJBlock(this, cond));
    }

    public JLabel label(final String name) {
        return add(new ImplJLabel(name));
    }

    public JLabel anonLabel() {
        return add(new ImplJLabel(tempName()));
    }

    public JLabel forwardLabel() {
        return new ImplJLabel();
    }

    public JStatement label(final JLabel label, final String name) {
        ImplJLabel label1 = (ImplJLabel) label;
        if (label1.isResolved()) {
            throw new IllegalStateException("Label " + label + " is already resolved");
        }
        label1.setName(name);
        return add(label1);
    }

    public JStatement anonLabel(final JLabel label) {
        ImplJLabel label1 = (ImplJLabel) label;
        if (label1.isResolved()) {
            throw new IllegalStateException("Label " + label + " is already resolved");
        }
        label1.setName(tempName());
        return add(label1);
    }

    public JStatement _continue() {
        return add(new KeywordJStatement("continue"));
    }

    public JStatement _continue(final JLabel label) {
        return add(new GotoJStatement("continue", label));
    }

    public JStatement _break() {
        return add(new KeywordJStatement("break"));
    }

    public JStatement _break(final JLabel label) {
        return add(new GotoJStatement("break", label));
    }

    public JBlock forEach(final int mods, final JType type, final String name, final JExpr iterable) {
        return add(new ForEachJBlock(this, mods, type, name, iterable));
    }

    public JFor _for() {
        return add(new ForJBlock(this));
    }

    public JSwitch _switch(final JExpr expr) {
        return add(new ImplJSwitch(parent, expr));
    }

    public JStatement _return(final JExpr expr) {
        return add(new KeywordExprJStatement("return", expr));
    }

    public JStatement _return() {
        return add(new KeywordJStatement("return"));
    }

    public JStatement _assert(final JExpr expr) {
        return add(new KeywordExprJStatement("assert", expr));
    }

    public JStatement _assert(final JExpr expr, final JExpr message) {
        return add(new AssertMessageJStatement(expr, message));
    }

    public JCall callThis() {
        return add(new DirectJCall("this"));
    }

    public JCall callSuper() {
        return add(new DirectJCall("super"));
    }

    public JCall call(final ExecutableElement element) {
        final ElementKind kind = element.getKind();
        if (kind == ElementKind.METHOD && ! element.getModifiers().contains(Modifier.STATIC)) {
            final String name = element.getSimpleName().toString();
            return call(name);
        }
        throw new IllegalArgumentException("Unsupported element for call: " + element);
    }

    public JCall call(final JExpr obj, final ExecutableElement element) {
        final ElementKind kind = element.getKind();
        if (kind == ElementKind.METHOD && ! element.getModifiers().contains(Modifier.STATIC)) {
            final String name = element.getSimpleName().toString();
            return call(obj, name);
        }
        throw new IllegalArgumentException("Unsupported element for call: " + element);
    }

    public JCall call(final String name) {
        return add(new DirectJCall(name));
    }

    public JCall call(final JExpr obj, final String name) {
        return add(new InstanceJCall(obj, name));
    }

    public JCall callStatic(final ExecutableElement element) {
        final ElementKind kind = element.getKind();
        if (kind == ElementKind.METHOD && element.getModifiers().contains(Modifier.STATIC)) {
            final String name = element.getSimpleName().toString();
            final JType enclosingType = JTypes.typeOf(element.getEnclosingElement().asType());
            return callStatic(enclosingType, name);
        }
        throw new IllegalArgumentException("Unsupported element for callStatic: " + element);
    }

    public JCall callStatic(final String type, final String name) {
        return callStatic(JTypes.typeNamed(type), name);
    }

    public JCall callStatic(final JType type, final String name) {
        return add(new StaticJCall(type, name));
    }

    public JCall callStatic(final Class<?> type, final String name) {
        return callStatic(JTypes.typeOf(type), name);
    }

    public JCall _new(final String type) {
        return _new(JTypes.typeNamed(type));
    }

    public JCall _new(final JType type) {
        return add(new NewJCall((ReferenceJType) type));
    }

    public JCall _new(final Class<?> type) {
        return _new(JTypes.typeOf(type));
    }

    public JAnonymousClassDef _newAnon(final String type) {
        return null;
    }

    public JAnonymousClassDef _newAnon(final JType type) {
        return null;
    }

    public JAnonymousClassDef _newAnon(final Class<?> type) {
        return null;
    }

    public JBlock _synchronized(final JExpr synchExpr) {
        return add(new SynchJBlock(this, synchExpr));
    }

    public JStatement assign(final JAssignExpr target, final JExpr e1) {
        return add(new AssignmentJExpr("=", (AbstractJExpr) target, (AbstractJExpr) e1));
    }

    public JStatement addAssign(final JAssignExpr target, final JExpr e1) {
        return add(new AssignmentJExpr("+=", (AbstractJExpr) target, (AbstractJExpr) e1));
    }

    public JStatement subAssign(final JAssignExpr target, final JExpr e1) {
        return add(new AssignmentJExpr("-=", (AbstractJExpr) target, (AbstractJExpr) e1));
    }

    public JStatement mulAssign(final JAssignExpr target, final JExpr e1) {
        return add(new AssignmentJExpr("*=", (AbstractJExpr) target, (AbstractJExpr) e1));
    }

    public JStatement divAssign(final JAssignExpr target, final JExpr e1) {
        return add(new AssignmentJExpr("/=", (AbstractJExpr) target, (AbstractJExpr) e1));
    }

    public JStatement modAssign(final JAssignExpr target, final JExpr e1) {
        return add(new AssignmentJExpr("%=", (AbstractJExpr) target, (AbstractJExpr) e1));
    }

    public JStatement andAssign(final JAssignExpr target, final JExpr e1) {
        return add(new AssignmentJExpr("&=", (AbstractJExpr) target, (AbstractJExpr) e1));
    }

    public JStatement orAssign(final JAssignExpr target, final JExpr e1) {
        return add(new AssignmentJExpr("|=", (AbstractJExpr) target, (AbstractJExpr) e1));
    }

    public JStatement xorAssign(final JAssignExpr target, final JExpr e1) {
        return add(new AssignmentJExpr("^=", (AbstractJExpr) target, (AbstractJExpr) e1));
    }

    public JStatement shrAssign(final JAssignExpr target, final JExpr e1) {
        return add(new AssignmentJExpr(">>=", (AbstractJExpr) target, (AbstractJExpr) e1));
    }

    public JStatement lshrAssign(final JAssignExpr target, final JExpr e1) {
        return add(new AssignmentJExpr(">>>=", (AbstractJExpr) target, (AbstractJExpr) e1));
    }

    public JStatement shlAssign(final JAssignExpr target, final JExpr e1) {
        return add(new AssignmentJExpr("<<=", (AbstractJExpr) target, (AbstractJExpr) e1));
    }

    public JStatement postInc(final JAssignExpr target) {
        return add(new IncDecJExpr("++", (AbstractJExpr) target, Prec.POST_INC_DEC, true));
    }

    public JStatement postDec(final JAssignExpr target) {
        return add(new IncDecJExpr("--", (AbstractJExpr) target, Prec.POST_INC_DEC, true));
    }

    public JStatement preInc(final JAssignExpr target) {
        return add(new IncDecJExpr("++", (AbstractJExpr) target, Prec.PRE_INC_DEC, false));
    }

    public JStatement preDec(final JAssignExpr target) {
        return add(new IncDecJExpr("--", (AbstractJExpr) target, Prec.PRE_INC_DEC, false));
    }

    public JStatement empty() {
        return add(new EmptyJStatement());
    }

    public JStatement _throw(final JExpr expr) {
        return add(new KeywordExprJStatement("throw", expr));
    }

    public JTry _try() {
        return add(new ImplJTry(this));
    }

    public JVarDeclaration var(final int mods, final JType type, final String name, final JExpr value) {
        return null;
    }

    public JVarDeclaration var(final int mods, final JType type, final String name) {
        return null;
    }

    public JExpr tempVar(final JType type, final JExpr value) {
        final String name = tempName();
        final NameJExpr expr = new NameJExpr(name);
        var(0, type, name, value);
        return expr;
    }

    public String tempName() {
        final BasicJBlock parent = getParent();
        return parent != null ? parent.tempName() : "anon$$$" + tmpId++;
    }

    public JClassDef localEnum(final int mods, final String name) {
        return null;
    }

    public JClassDef localClass(final int mods, final String name) {
        return null;
    }

    public JClassDef localInterface(final int mods, final String name) {
        return null;
    }

    public Iterator<JStatement> iterator() {
        return Collections.unmodifiableList(content).iterator();
    }

    public JComment inlineLineComment() {
        return add(new LineJComment());
    }

    public JComment inlineBlockComment() {
        return add(new BlockJComment());
    }

    BasicJBlock getParent() {
        return parent;
    }

    boolean isForceBrackets() {
        return forceBrackets;
    }

    void setForceBrackets(final boolean forceBrackets) {
        this.forceBrackets = forceBrackets;
    }
}
