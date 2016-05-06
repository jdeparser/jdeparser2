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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class BasicJBlock extends BasicJCommentable implements JBlock, BlockContent {
    private final BasicJBlock parent;
    private final ArrayList<BlockContent> content = new ArrayList<>();
    private final Braces braces;
    private int tmpId = 1;

    BasicJBlock(final BasicJBlock parent, final Braces braces) {
        this.parent = parent;
        this.braces = braces;
    }

    private <T extends BlockContent> T add(T s) {
        content.add(s);
        return s;
    }

    private <T extends AbstractJExpr> ExpressionJStatement add(T item) {
        final ExpressionJStatement statement = new ExpressionJStatement(item);
        content.add(statement);
        return statement;
    }

    private <T extends AbstractJCall> T add(T item) {
        content.add(new ExpressionJStatement(item));
        return item;
    }

    public JBlock blankLine() {
        add(BlankLine.getInstance());
        return this;
    }

    public JBlock block(final Braces braces) {
        return add(new BasicJBlock(this, braces));
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

    public JLabel label(final JLabel label, final String name) {
        ImplJLabel label1 = (ImplJLabel) label;
        if (label1.isResolved()) {
            throw new IllegalStateException("Label " + label + " is already resolved");
        }
        label1.setName(name);
        return add(label1);
    }

    public JLabel anonLabel(final JLabel label) {
        ImplJLabel label1 = (ImplJLabel) label;
        if (label1.isResolved()) {
            throw new IllegalStateException("Label " + label + " is already resolved");
        }
        label1.setName(tempName());
        return add(label1);
    }

    public JStatement _continue() {
        return add(new KeywordJStatement($KW.CONTINUE));
    }

    public JStatement _continue(final JLabel label) {
        return add(new GotoJStatement($KW.CONTINUE, label));
    }

    public JStatement _break() {
        return add(new KeywordJStatement($KW.BREAK));
    }

    public JStatement _break(final JLabel label) {
        return add(new GotoJStatement($KW.BREAK, label));
    }

    public JBlock forEach(final int mods, final String type, final String name, final JExpr iterable) {
        return forEach(mods, JTypes.typeNamed(type), name, iterable);
    }

    public JBlock forEach(final int mods, final JType type, final String name, final JExpr iterable) {
        return add(new ForEachJBlock(this, mods, type, name, iterable));
    }

    public JBlock forEach(final int mods, final Class<?> type, final String name, final JExpr iterable) {
        return forEach(mods, JTypes.typeOf(type), name, iterable);
    }

    public JFor _for() {
        return add(new ForJBlock(this));
    }

    public JSwitch _switch(final JExpr expr) {
        return add(new ImplJSwitch(parent, expr));
    }

    public JStatement _return(final JExpr expr) {
        return add(new KeywordExprJStatement($KW.RETURN, expr));
    }

    public JStatement _return() {
        return add(new KeywordJStatement($KW.RETURN));
    }

    public JStatement _assert(final JExpr expr) {
        return add(new KeywordExprJStatement($KW.ASSERT, expr));
    }

    public JStatement _assert(final JExpr expr, final JExpr message) {
        return add(new AssertMessageJStatement(expr, message));
    }

    public JCall callThis() {
        return add(new KeywordJCall($KW.THIS));
    }

    public JCall callSuper() {
        return add(new KeywordJCall($KW.SUPER));
    }

    public JStatement add(final JExpr expr) {
        if (expr instanceof AllowedStatementExpression) {
            return add(new ExpressionJStatement(AbstractJExpr.of(expr)));
        } else {
            throw new IllegalArgumentException("Expression <<" + expr + ">> is not a valid statement");
        }
    }

    public JCall call(final ExecutableElement element) {
        final ElementKind kind = element.getKind();
        if (kind == ElementKind.METHOD) {
            final String name = element.getSimpleName().toString();
            return call(name);
        }
        throw new IllegalArgumentException("Unsupported element for call: " + element);
    }

    public JCall call(final JExpr obj, final ExecutableElement element) {
        final ElementKind kind = element.getKind();
        if (kind == ElementKind.METHOD) {
            final String name = element.getSimpleName().toString();
            return call(obj, name);
        }
        throw new IllegalArgumentException("Unsupported element for call: " + element);
    }

    public JCall call(final String name) {
        return add(new DirectJCall(name));
    }

    public JCall call(final JExpr obj, final String name) {
        return add(new InstanceJCall(AbstractJExpr.of(obj), name));
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
        return add(new StaticJCall(AbstractJType.of(type), name));
    }

    public JCall callStatic(final Class<?> type, final String name) {
        return callStatic(JTypes.typeOf(type), name);
    }

    public JCall _new(final String type) {
        return _new(JTypes.typeNamed(type));
    }

    public JCall _new(final JType type) {
        return add(new NewJCall(AbstractJType.of(type)));
    }

    public JCall _new(final Class<?> type) {
        return _new(JTypes.typeOf(type));
    }

    public JAnonymousClassDef _newAnon(final String type) {
        return _newAnon(JTypes.typeNamed(type));
    }

    public JAnonymousClassDef _newAnon(final JType type) {
        return add(new ImplJAnonymousClassDef(type));
    }

    public JAnonymousClassDef _newAnon(final Class<?> type) {
        return _newAnon(JTypes.typeOf(type));
    }

    public JClassDef _class(final int mods, final String name) {
        return add(new PlainJClassDef(mods, (ImplJSourceFile) null, name));
    }

    public JBlock _synchronized(final JExpr synchExpr) {
        return add(new SynchJBlock(this, PlainJArrayExpr.of(synchExpr)));
    }

    public JStatement assign(final JAssignableExpr target, final JExpr e1) {
        return add(new AssignmentJExpr($PUNCT.BINOP.ASSIGN, AbstractJExpr.of(target), AbstractJExpr.of(e1)));
    }

    public JStatement addAssign(final JAssignableExpr target, final JExpr e1) {
        return add(new AssignmentJExpr($PUNCT.BINOP.ASSIGN_PLUS, AbstractJExpr.of(target), AbstractJExpr.of(e1)));
    }

    public JStatement subAssign(final JAssignableExpr target, final JExpr e1) {
        return add(new AssignmentJExpr($PUNCT.BINOP.ASSIGN_MINUS, AbstractJExpr.of(target), AbstractJExpr.of(e1)));
    }

    public JStatement mulAssign(final JAssignableExpr target, final JExpr e1) {
        return add(new AssignmentJExpr($PUNCT.BINOP.ASSIGN_TIMES, AbstractJExpr.of(target), AbstractJExpr.of(e1)));
    }

    public JStatement divAssign(final JAssignableExpr target, final JExpr e1) {
        return add(new AssignmentJExpr($PUNCT.BINOP.ASSIGN_DIV, AbstractJExpr.of(target), AbstractJExpr.of(e1)));
    }

    public JStatement modAssign(final JAssignableExpr target, final JExpr e1) {
        return add(new AssignmentJExpr($PUNCT.BINOP.ASSIGN_MOD, AbstractJExpr.of(target), AbstractJExpr.of(e1)));
    }

    public JStatement andAssign(final JAssignableExpr target, final JExpr e1) {
        return add(new AssignmentJExpr($PUNCT.BINOP.ASSIGN_BAND, AbstractJExpr.of(target), AbstractJExpr.of(e1)));
    }

    public JStatement orAssign(final JAssignableExpr target, final JExpr e1) {
        return add(new AssignmentJExpr($PUNCT.BINOP.ASSIGN_BOR, AbstractJExpr.of(target), AbstractJExpr.of(e1)));
    }

    public JStatement xorAssign(final JAssignableExpr target, final JExpr e1) {
        return add(new AssignmentJExpr($PUNCT.BINOP.ASSIGN_BXOR, AbstractJExpr.of(target), AbstractJExpr.of(e1)));
    }

    public JStatement shrAssign(final JAssignableExpr target, final JExpr e1) {
        return add(new AssignmentJExpr($PUNCT.BINOP.ASSIGN_SHR, AbstractJExpr.of(target), AbstractJExpr.of(e1)));
    }

    public JStatement lshrAssign(final JAssignableExpr target, final JExpr e1) {
        return add(new AssignmentJExpr($PUNCT.BINOP.ASSIGN_LSHR, AbstractJExpr.of(target), AbstractJExpr.of(e1)));
    }

    public JStatement shlAssign(final JAssignableExpr target, final JExpr e1) {
        return add(new AssignmentJExpr($PUNCT.BINOP.ASSIGN_SHL, AbstractJExpr.of(target), AbstractJExpr.of(e1)));
    }

    public JStatement postInc(final JAssignableExpr target) {
        return add(new IncDecJExpr($PUNCT.UNOP.PP, AbstractJExpr.of(target), Prec.POST_INC_DEC, true));
    }

    public JStatement postDec(final JAssignableExpr target) {
        return add(new IncDecJExpr($PUNCT.UNOP.MM, AbstractJExpr.of(target), Prec.POST_INC_DEC, true));
    }

    public JStatement preInc(final JAssignableExpr target) {
        return add(new IncDecJExpr($PUNCT.UNOP.PP, AbstractJExpr.of(target), Prec.PRE_INC_DEC, false));
    }

    public JStatement preDec(final JAssignableExpr target) {
        return add(new IncDecJExpr($PUNCT.UNOP.MM, AbstractJExpr.of(target), Prec.PRE_INC_DEC, false));
    }

    public JStatement empty() {
        return add(new EmptyJStatement());
    }

    public JStatement _throw(final JExpr expr) {
        return add(new KeywordExprJStatement($KW.THROW, expr));
    }

    public JTry _try() {
        return add(new ImplJTry(this));
    }

    public JVarDeclaration var(final int mods, final String type, final String name, final JExpr value) {
        return var(mods, JTypes.typeNamed(type), name, value);
    }

    public JVarDeclaration var(final int mods, final JType type, final String name, final JExpr value) {
        return add(new FirstJVarDeclaration(mods, type, name, value));
    }

    public JVarDeclaration var(final int mods, final Class<?> type, final String name, final JExpr value) {
        return var(mods, JTypes.typeOf(type), name, value);
    }

    public JVarDeclaration var(final int mods, final String type, final String name) {
        return var(mods, JTypes.typeNamed(type), name);
    }

    public JVarDeclaration var(final int mods, final JType type, final String name) {
        return add(new FirstJVarDeclaration(mods, type, name, null));
    }

    public JVarDeclaration var(final int mods, final Class<?> type, final String name) {
        return var(mods, JTypes.typeOf(type), name);
    }

    public JExpr tempVar(final String type, final JExpr value) {
        return tempVar(JTypes.typeNamed(type), value);
    }

    public JExpr tempVar(final JType type, final JExpr value) {
        final String name = tempName();
        final NameJExpr expr = new NameJExpr(name);
        var(0, type, name, value);
        return expr;
    }

    public JExpr tempVar(final Class<?> type, final JExpr value) {
        return tempVar(JTypes.typeOf(type), value);
    }

    public String tempName() {
        final BasicJBlock parent = getParent();
        return parent != null ? parent.tempName() : "anon$$$" + tmpId++;
    }

    BasicJBlock getParent() {
        return parent;
    }

    int size() {
        return content.size();
    }

    BlockContent get(int idx) {
        return content.get(idx);
    }

    void write(final SourceFileWriter writer, final FormatPreferences.Space beforeBrace) throws IOException {
        write(writer, beforeBrace, braces);
    }

    void write(final SourceFileWriter writer, final FormatPreferences.Space beforeBrace, Braces braces) throws IOException {
        if (braces == Braces.REQUIRED || braces == Braces.IF_MULTILINE && content.size() != 1) {
            writer.write(beforeBrace);
            writer.write($PUNCT.BRACE.OPEN);
            writer.pushIndent(FormatPreferences.Indentation.LINE);
            try {
                final Iterator<BlockContent> iterator = content.iterator();
                if (iterator.hasNext()) {
                    writer.write(FormatPreferences.Space.WITHIN_BRACES_CODE);
                    if (iterator.hasNext()) {
                        BlockContent statement = iterator.next();
                        statement.write(writer);
                        while (iterator.hasNext()) {
                            writer.nl();
                            statement = iterator.next();
                            statement.write(writer);
                        }
                    }
                    writer.write(FormatPreferences.Space.WITHIN_BRACES_CODE);
                } else {
                    writer.write(FormatPreferences.Space.WITHIN_BRACES_EMPTY);
                }
            } finally {
                writer.popIndent(FormatPreferences.Indentation.LINE);
            }
            writer.write($PUNCT.BRACE.CLOSE);
        } else {
            Iterator<BlockContent> iterator = content.iterator();
            if (iterator.hasNext()) {
                iterator.next().write(writer);
                while (iterator.hasNext()) {
                    writer.nl();
                    iterator.next().write(writer);
                }
            }
        }
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writeComments(writer);
        write(writer, FormatPreferences.Space.BEFORE_BRACE);
    }

    boolean hasSingleItemOfType(Class<? extends BlockContent> type) {
        return size() == 1 && type.isInstance(get(0));
    }
}
