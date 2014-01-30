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

import javax.lang.model.element.ExecutableElement;

/**
 * A block of code.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JBlock extends JStatement, JInlineCommentable, Iterable<JStatement> {
    JStatement[] content();

    JBlock block(boolean forceBrackets);

    // program flow

    JIf _if(JExpr cond);

    JBlock _while(JExpr cond);

    JBlock _do(JExpr cond);

    JLabel label(String name);

    JLabel anonLabel();

    JLabel forwardLabel();

    JStatement label(JLabel label, String name);

    JStatement anonLabel(JLabel label);

    JStatement _continue();

    JStatement _continue(JLabel label);

    JStatement _break();

    JStatement _break(JLabel label);

    JBlock forEach(int mods, JType type, String name, JExpr iterable);

    JFor _for();

    JSwitch _switch(JExpr expr);

    JStatement _return(JExpr expr);

    JStatement _return();

    // assert

    JStatement _assert(JExpr expr);

    JStatement _assert(JExpr expr, JExpr message);

    // constructor invocations

    JCall callThis();

    JCall callSuper();

    // invocations

    JCall call(ExecutableElement element);

    JCall call(JExpr obj, ExecutableElement element);

    JCall call(String name);

    JCall call(JExpr obj, String name);

    JCall callStatic(ExecutableElement element);

    JCall callStatic(String type, String name);

    JCall callStatic(JType type, String name);

    JCall callStatic(Class<?> type, String name);

    // raw construction

    JCall _new(String type);

    JCall _new(JType type);

    JCall _new(Class<?> type);

    JAnonymousClassDef _newAnon(String type);

    JAnonymousClassDef _newAnon(JType type);

    JAnonymousClassDef _newAnon(Class<?> type);

    // synch

    JBlock _synchronized(JExpr synchExpr);

    // assignment

    JStatement assign(JAssignExpr target, JExpr e1);

    JStatement addAssign(JAssignExpr target, JExpr e1);

    JStatement subAssign(JAssignExpr target, JExpr e1);

    JStatement mulAssign(JAssignExpr target, JExpr e1);

    JStatement divAssign(JAssignExpr target, JExpr e1);

    JStatement modAssign(JAssignExpr target, JExpr e1);

    JStatement andAssign(JAssignExpr target, JExpr e1);

    JStatement orAssign(JAssignExpr target, JExpr e1);

    JStatement xorAssign(JAssignExpr target, JExpr e1);

    JStatement shrAssign(JAssignExpr target, JExpr e1);

    JStatement lshrAssign(JAssignExpr target, JExpr e1);

    JStatement shlAssign(JAssignExpr target, JExpr e1);

    // inc/dec

    JStatement postInc(JAssignExpr target);

    JStatement postDec(JAssignExpr target);

    JStatement preInc(JAssignExpr target);

    JStatement preDec(JAssignExpr target);

    // empty

    JStatement empty();

    // exceptions

    JStatement _throw(JExpr expr);

    JTry _try();

    // declarations

    JVarDeclaration var(int mods, JType type, String name, JExpr value);

    JVarDeclaration var(int mods, JType type, String name);

    JExpr tempVar(JType type, JExpr value);

    String tempName();

    // local classes

    JClassDef localEnum(int mods, String name);

    JClassDef localClass(int mods, String name);

    JClassDef localInterface(int mods, String name);
}
