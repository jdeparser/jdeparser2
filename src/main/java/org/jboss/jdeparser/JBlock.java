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
public interface JBlock extends JStatement, JInlineCommentable {

    JBlock blankLine();

    JBlock block(Braces braces);

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

    JBlock forEach(int mods, String type, String name, JExpr iterable);

    JBlock forEach(int mods, JType type, String name, JExpr iterable);

    JBlock forEach(int mods, Class<?> type, String name, JExpr iterable);

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

    // expression

    JStatement add(JExpr expr);

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

    // local class definition

    JClassDef _class(int mods, String name);

    // synch

    JBlock _synchronized(JExpr synchExpr);

    // assignment

    JStatement assign(JAssignableExpr target, JExpr e1);

    JStatement addAssign(JAssignableExpr target, JExpr e1);

    JStatement subAssign(JAssignableExpr target, JExpr e1);

    JStatement mulAssign(JAssignableExpr target, JExpr e1);

    JStatement divAssign(JAssignableExpr target, JExpr e1);

    JStatement modAssign(JAssignableExpr target, JExpr e1);

    JStatement andAssign(JAssignableExpr target, JExpr e1);

    JStatement orAssign(JAssignableExpr target, JExpr e1);

    JStatement xorAssign(JAssignableExpr target, JExpr e1);

    JStatement shrAssign(JAssignableExpr target, JExpr e1);

    JStatement lshrAssign(JAssignableExpr target, JExpr e1);

    JStatement shlAssign(JAssignableExpr target, JExpr e1);

    // inc/dec

    JStatement postInc(JAssignableExpr target);

    JStatement postDec(JAssignableExpr target);

    JStatement preInc(JAssignableExpr target);

    JStatement preDec(JAssignableExpr target);

    // empty

    JStatement empty();

    // exceptions

    JStatement _throw(JExpr expr);

    JTry _try();

    // declarations

    JVarDeclaration var(int mods, String type, String name, JExpr value);

    JVarDeclaration var(int mods, JType type, String name, JExpr value);

    JVarDeclaration var(int mods, Class<?> type, String name, JExpr value);

    JVarDeclaration var(int mods, String type, String name);

    JVarDeclaration var(int mods, JType type, String name);

    JVarDeclaration var(int mods, Class<?> type, String name);

    JExpr tempVar(String type, JExpr value);

    JExpr tempVar(JType type, JExpr value);

    JExpr tempVar(Class<?> type, JExpr value);

    String tempName();

    // local classes

    JClassDef localEnum(int mods, String name);

    JClassDef localClass(int mods, String name);

    JClassDef localInterface(int mods, String name);

    /**
     * Braces mode.
     */
    enum Braces {
        OPTIONAL,
        IF_MULTILINE,
        REQUIRED,
        ;
    }
}
