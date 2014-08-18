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
 * A block of code, to which statements may be added.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JBlock extends JStatement, JCommentable {

    /**
     * Insert a blank line at this point.
     *
     * @return this block
     */
    JBlock blankLine();

    /**
     * Create a nested sub-block at this point.
     *
     * @param braces the rule for adding braces
     * @return the nested block
     */
    JBlock block(Braces braces);

    // program flow

    /**
     * Insert an {@code if} statement at this point.
     *
     * @param cond the {@code if} condition
     * @return the {@code if} statement
     */
    JIf _if(JExpr cond);

    /**
     * Insert a {@code while} statement at this point.
     *
     * @param cond the {@code while} condition
     * @return the {@code while} statement
     */
    JBlock _while(JExpr cond);

    /**
     * Insert a {@code do}/{@code while} statement at this point.
     *
     * @param cond the {@code while} condition
     * @return the {@code while} statement
     */
    JBlock _do(JExpr cond);

    /**
     * Add a label at this point, which may be used for future branch instructions.
     *
     * @param name the label name
     * @return the label
     */
    JLabel label(String name);

    /**
     * Add a label at this point whose unique name is automatically generated.
     *
     * @return the label
     */
    JLabel anonLabel();

    /**
     * Create a forward label that can be named and attached later.
     *
     * @return the forward label
     */
    JLabel forwardLabel();

    /**
     * Name and attach a forward label.
     *
     * @param label the label to name and attach
     * @param name the label name
     * @return the attached label
     */
    JLabel label(JLabel label, String name);

    /**
     * Name and attach a forward label as anonymous.
     *
     * @param label the label to name and attach
     * @return the attached label
     */
    JLabel anonLabel(JLabel label);

    /**
     * Insert a {@code continue} statement at this point.
     *
     * @return the statement
     */
    JStatement _continue();

    /**
     * Insert a labelled {@code continue} statement at this point.
     *
     * @param label the label
     * @return the statement
     */
    JStatement _continue(JLabel label);

    /**
     * Insert a {@code break} statement at this point.
     *
     * @return the statement
     */
    JStatement _break();

    /**
     * Insert a labelled {@code break} statement at this point.
     *
     * @param label the label
     * @return the statement
     */
    JStatement _break(JLabel label);

    /**
     * Insert a "for-each" style {@code for} loop at this point.
     *
     * @param mods the item variable modifiers
     * @param type the item variable type
     * @param name the item variable name
     * @param iterable the iterable or array expression
     * @return the body of the {@code for} loop
     */
    JBlock forEach(int mods, String type, String name, JExpr iterable);

    /**
     * Insert a "for-each" style {@code for} loop at this point.
     *
     * @param mods the item variable modifiers
     * @param type the item variable type
     * @param name the item variable name
     * @param iterable the iterable or array expression
     * @return the body of the {@code for} loop
     */
    JBlock forEach(int mods, JType type, String name, JExpr iterable);

    /**
     * Insert a "for-each" style {@code for} loop at this point.
     *
     * @param mods the item variable modifiers
     * @param type the item variable type
     * @param name the item variable name
     * @param iterable the iterable or array expression
     * @return the body of the {@code for} loop
     */
    JBlock forEach(int mods, Class<?> type, String name, JExpr iterable);

    /**
     * Insert a {@code for} loop at this point.
     *
     * @return the {@code for} loop
     */
    JFor _for();

    /**
     * Insert a {@code switch} statement at this point.
     *
     * @param expr the {@code switch} expression
     * @return the {@code switch} statement
     */
    JSwitch _switch(JExpr expr);

    /**
     * Insert a {@code return} statement at this point.
     *
     * @param expr the expression to return
     * @return the statement
     */
    JStatement _return(JExpr expr);

    /**
     * Insert a {@code void} {@code return} statement at this point.
     *
     * @return the statement
     */
    JStatement _return();

    // assert

    /**
     * Insert an {@code assert} statement at this point.
     *
     * @param expr the expression to assert
     * @return the statement
     */
    JStatement _assert(JExpr expr);

    /**
     * Insert an {@code assert} statement at this point with a message.
     *
     * @param expr the expression to assert
     * @param message the assertion message
     * @return the statement
     */
    JStatement _assert(JExpr expr, JExpr message);

    // constructor invocations

    /**
     * Insert a {@code this()} call at this point.
     *
     * @return the call
     */
    JCall callThis();

    /**
     * Insert a {@code super()} call at this point.
     *
     * @return the call
     */
    JCall callSuper();

    // expression

    /**
     * Insert an expression statement at this point.  Expressions which are invalid statements may generate an
     * error at the time this method is called, or at compile time.
     *
     * @param expr the expression to add
     * @return the statement
     */
    JStatement add(JExpr expr);

    // invocations

    /**
     * Insert a method invocation at this point.
     *
     * Note that these two invocations are identical:
     *
     * <pre>{@code
    block.call(element);
    block.add(JExprs.call(element));
}</pre>
     *
     * @param element the program element whose name to use
     * @return the method call
     */
    JCall call(ExecutableElement element);

    /**
     * Insert a method invocation at this point.
     *
     * @param obj the expression upon which to invoke
     * @param element the program element whose name to use
     * @return the method call
     */
    JCall call(JExpr obj, ExecutableElement element);

    /**
     * Insert a method invocation at this point.
     *
     * Note that these two invocations are identical:
     *
     * <pre>{@code
    block.call(methodName);
    block.add(JExprs.call(methodName));
}</pre>
     *
     * @param name the method name
     * @return the method call
     */
    JCall call(String name);

    /**
     * Insert a method invocation at this point.
     *
     * @param obj the expression upon which to invoke
     * @param name the method name
     * @return the method call
     */
    JCall call(JExpr obj, String name);

    /**
     * Insert a type-qualified static method invocation at this point.
     *
     * @param element the program element whose name and type to use
     * @return the method call
     */
    JCall callStatic(ExecutableElement element);

    /**
     * Insert a type-qualified static method invocation at this point.
     *
     * @param type the type upon which to invoke
     * @param name the method name
     * @return the method call
     */
    JCall callStatic(String type, String name);

    /**
     * Insert a type-qualified static method invocation at this point.
     *
     * @param type the type upon which to invoke
     * @param name the method name
     * @return the method call
     */
    JCall callStatic(JType type, String name);

    /**
     * Insert a type-qualified static method invocation at this point.
     *
     * @param type the type upon which to invoke
     * @param name the method name
     * @return the method call
     */
    JCall callStatic(Class<?> type, String name);

    // raw construction

    /**
     * Insert an object construction statement at this point.
     *
     * @param type the type to instantiate
     * @return the constructor call
     */
    JCall _new(String type);

    /**
     * Insert an object construction statement at this point.
     *
     * @param type the type to instantiate
     * @return the constructor call
     */
    JCall _new(JType type);

    /**
     * Insert an object construction statement at this point.
     *
     * @param type the type to instantiate
     * @return the constructor call
     */
    JCall _new(Class<?> type);

    /**
     * Insert an object construction statement for an anonymous class at this point.
     *
     * @param type the type to instantiate
     * @return the anonymous class definition
     */
    JAnonymousClassDef _newAnon(String type);

    /**
     * Insert an object construction statement for an anonymous class at this point.
     *
     * @param type the type to instantiate
     * @return the anonymous class definition
     */
    JAnonymousClassDef _newAnon(JType type);

    /**
     * Insert an object construction statement for an anonymous class at this point.
     *
     * @param type the type to instantiate
     * @return the anonymous class definition
     */
    JAnonymousClassDef _newAnon(Class<?> type);

    // local class definition

    /**
     * Insert a local class definition at this point.
     *
     * @param mods the class modifiers
     * @param name the local class name
     * @return the local class definition
     */
    JClassDef _class(int mods, String name);

    // synch

    /**
     * Insert a {@code synchronized} block at this point.
     *
     * @param synchExpr the lock expression
     * @return the {@code synchronized} block
     */
    JBlock _synchronized(JExpr synchExpr);

    // assignment

    /**
     * Insert an assignment ({@code =}) expression at this point.
     *
     * @param target the assignment target
     * @param e1 the expression to apply
     * @return the statement
     */
    JStatement assign(JAssignableExpr target, JExpr e1);

    /**
     * Insert an assignment ({@code +=}) expression at this point.
     *
     * @param target the assignment target
     * @param e1 the expression to apply
     * @return the statement
     */
    JStatement addAssign(JAssignableExpr target, JExpr e1);

    /**
     * Insert an assignment ({@code -=}) expression at this point.
     *
     * @param target the assignment target
     * @param e1 the expression to apply
     * @return the statement
     */
    JStatement subAssign(JAssignableExpr target, JExpr e1);

    /**
     * Insert an assignment ({@code *=}) expression at this point.
     *
     * @param target the assignment target
     * @param e1 the expression to apply
     * @return the statement
     */
    JStatement mulAssign(JAssignableExpr target, JExpr e1);

    /**
     * Insert an assignment ({@code /=}) expression at this point.
     *
     * @param target the assignment target
     * @param e1 the expression to apply
     * @return the statement
     */
    JStatement divAssign(JAssignableExpr target, JExpr e1);

    /**
     * Insert an assignment ({@code %=}) expression at this point.
     *
     * @param target the assignment target
     * @param e1 the expression to apply
     * @return the statement
     */
    JStatement modAssign(JAssignableExpr target, JExpr e1);

    /**
     * Insert an assignment ({@code &=}) expression at this point.
     *
     * @param target the assignment target
     * @param e1 the expression to apply
     * @return the statement
     */
    JStatement andAssign(JAssignableExpr target, JExpr e1);

    /**
     * Insert an assignment ({@code |=}) expression at this point.
     *
     * @param target the assignment target
     * @param e1 the expression to apply
     * @return the statement
     */
    JStatement orAssign(JAssignableExpr target, JExpr e1);

    /**
     * Insert an assignment ({@code ^=}) expression at this point.
     *
     * @param target the assignment target
     * @param e1 the expression to apply
     * @return the statement
     */
    JStatement xorAssign(JAssignableExpr target, JExpr e1);

    /**
     * Insert an assignment ({@code >>=}) expression at this point.
     *
     * @param target the assignment target
     * @param e1 the expression to apply
     * @return the statement
     */
    JStatement shrAssign(JAssignableExpr target, JExpr e1);

    /**
     * Insert an assignment ({@code >>>=}) expression at this point.
     *
     * @param target the assignment target
     * @param e1 the expression to apply
     * @return the statement
     */
    JStatement lshrAssign(JAssignableExpr target, JExpr e1);

    /**
     * Insert an assignment ({@code <<=}) expression at this point.
     *
     * @param target the assignment target
     * @param e1 the expression to apply
     * @return the statement
     */
    JStatement shlAssign(JAssignableExpr target, JExpr e1);

    // inc/dec

    /**
     * Insert a postfix {@code ++} expression at this point.
     *
     * @param target the target expression
     * @return the statement
     */
    JStatement postInc(JAssignableExpr target);

    /**
     * Insert a postfix {@code --} expression at this point.
     *
     * @param target the target expression
     * @return the statement
     */
    JStatement postDec(JAssignableExpr target);

    /**
     * Insert a prefix {@code ++} expression at this point.
     *
     * @param target the target expression
     * @return the statement
     */
    JStatement preInc(JAssignableExpr target);

    /**
     * Insert a prefix {@code --} expression at this point.
     *
     * @param target the target expression
     * @return the statement
     */
    JStatement preDec(JAssignableExpr target);

    // empty

    /**
     * Insert an empty statement at this point (just a semicolon).
     *
     * @return the statement
     */
    JStatement empty();

    // exceptions

    /**
     * Insert a {@code throw} statement at this point.
     *
     * @param expr the expression to throw
     * @return the statement
     */
    JStatement _throw(JExpr expr);

    /**
     * Insert a {@code try} block at this point.
     *
     * @return the {@code try} block
     */
    JTry _try();

    // declarations

    /**
     * Insert a local variable declaration at this point.
     *
     * @param mods the variable modifiers
     * @param type the local variable type
     * @param name the local variable name
     * @param value the local variable's initializer expression
     * @return the local variable declaration
     */
    JVarDeclaration var(int mods, String type, String name, JExpr value);

    /**
     * Insert a local variable declaration at this point.
     *
     * @param mods the variable modifiers
     * @param type the local variable type
     * @param name the local variable name
     * @param value the local variable's initializer expression
     * @return the local variable declaration
     */
    JVarDeclaration var(int mods, JType type, String name, JExpr value);

    /**
     * Insert a local variable declaration at this point.
     *
     * @param mods the variable modifiers
     * @param type the local variable type
     * @param name the local variable name
     * @param value the local variable's initializer expression
     * @return the local variable declaration
     */
    JVarDeclaration var(int mods, Class<?> type, String name, JExpr value);

    /**
     * Insert a local variable declaration at this point.
     *
     * @param mods the variable modifiers
     * @param type the local variable type
     * @param name the local variable name
     * @return the local variable declaration
     */
    JVarDeclaration var(int mods, String type, String name);

    /**
     * Insert a local variable declaration at this point.
     *
     * @param mods the variable modifiers
     * @param type the local variable type
     * @param name the local variable name
     * @return the local variable declaration
     */
    JVarDeclaration var(int mods, JType type, String name);

    /**
     * Insert a local variable declaration at this point.
     *
     * @param mods the variable modifiers
     * @param type the local variable type
     * @param name the local variable name
     * @return the local variable declaration
     */
    JVarDeclaration var(int mods, Class<?> type, String name);

    /**
     * Insert a local variable declaration at this point with a generated name.
     *
     * @param type the local variable type
     * @param value the local variable's initializer expression
     * @return the local variable expression
     */
    JExpr tempVar(String type, JExpr value);

    /**
     * Insert a local variable declaration at this point with a generated name.
     *
     * @param type the local variable type
     * @param value the local variable's initializer expression
     * @return the local variable expression
     */
    JExpr tempVar(JType type, JExpr value);

    /**
     * Insert a local variable declaration at this point with a generated name.
     *
     * @param type the local variable type
     * @param value the local variable's initializer expression
     * @return the local variable expression
     */
    JExpr tempVar(Class<?> type, JExpr value);

    /**
     * Generate a temporary variable name.
     *
     * @return the generated name
     */
    String tempName();

    /**
     * Braces mode.
     */
    enum Braces {
        /**
         * Braces are optional and won't be rendered unless forced.
         */
        OPTIONAL,
        /**
         * Braces are only required if the block contains more or less than one statement.
         */
        IF_MULTILINE,
        /**
         * Braces should always be used.
         */
        REQUIRED,
        ;
    }
}
