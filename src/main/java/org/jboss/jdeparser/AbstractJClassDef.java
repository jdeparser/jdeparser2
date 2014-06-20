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

import static java.lang.Integer.bitCount;
import static org.jboss.jdeparser.Tokens.*;
import static org.jboss.jdeparser.JMod.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
abstract class AbstractJClassDef extends AbstractJGeneric implements JClassDef, ClassFileContent {

    private final int mods;
    private final String name;

    private final ArrayList<ClassContent> content = new ArrayList<>();
    private JType _extends;
    private ArrayList<JType> _implements;
    private JType erased;
    private JType generic;

    AbstractJClassDef(final int mods, final String name) {
        this.mods = mods;
        this.name = name;
    }

    AbstractJClassDef(final int mods, final AbstractJClassDef enclosingClass, final String name) {
        this.mods = mods;
        this.name = name;
    }

    AbstractJClassDef(final int mods, final AbstractJMethodDef enclosingMethod, final String name) {
        this.mods = mods;
        this.name = name;
    }

    int getMods() {
        return mods;
    }

    String getName() {
        return name;
    }

    private <C extends ClassContent> C add(C item) {
        content.add(item);
        return item;
    }

    public JClassDef _extends(final String name) {
        return _extends(JTypes.typeNamed(name));
    }

    public JClassDef _extends(final JType type) {
        _extends = type;
        return this;
    }

    public JClassDef _extends(final Class<?> clazz) {
        return _extends(JTypes.typeOf(clazz));
    }

    public JClassDef _implements(final String... names) {
        if (_implements == null) {
            _implements = new ArrayList<>(names.length);
        }
        for (String name : names) {
            _implements.add(JTypes.typeNamed(name));
        }
        return this;
    }

    public JClassDef _implements(final JType... types) {
        if (_implements == null) {
            _implements = new ArrayList<>(types.length);
        }
        Collections.addAll(_implements, types);
        return this;
    }

    public JClassDef _implements(final Class<?>... classes) {
        if (_implements == null) {
            _implements = new ArrayList<>(classes.length);
        }
        for (Class<?> clazz : classes) {
            _implements.add(JTypes.typeOf(clazz));
        }
        return this;
    }

    public JType erasedType() {
        if (erased == null) {
            erased = JTypes.typeNamed(name);
        }
        return erased;
    }

    public JType genericType() {
        if (generic == null) {
            generic = erasedType().typeArg(typeParamsToArgs());
        }
        return generic;
    }

    public JTypeParamDef typeParam(final String name) {
        generic = null;
        return super.typeParam(name);
    }

    public JBlock init() {
        return add(new InitJBlock());
    }

    public JBlock staticInit() {
        if (allAreSet(mods, JMod.INNER)) {
            throw new UnsupportedOperationException("Inner classes cannot have static init blocks");
        }
        return add(new StaticInitJBlock());
    }

    public JEnumConstant _enum(final String name) {
        throw new UnsupportedOperationException("Enum constants may only be added to enums");
    }

    public JVarDeclaration field(final int mods, final JType type, final String name) {
        if (allAreSet(this.mods, JMod.INNER) && allAreSet(mods, JMod.STATIC)) {
            throw new UnsupportedOperationException("Inner classes cannot have static members");
        }
        if (anyAreSet(mods, STRICTFP | ABSTRACT | PRIVATE_BITS)) {
            throw new IllegalArgumentException("Invalid field modifier(s) given");
        }
        if (bitCount(mods & (PUBLIC | PROTECTED | PRIVATE)) > 1) {
            throw new IllegalArgumentException("Only one of 'public', 'protected', or 'private' may be given");
        }
        return null;
    }

    public JVarDeclaration field(final int mods, final JType type, final String name, final JExpr init) {
        if (allAreSet(this.mods, JMod.INNER) && allAreSet(mods, JMod.STATIC)) {
            throw new UnsupportedOperationException("Inner classes cannot have static members");
        }
        if (anyAreSet(mods, STRICTFP | ABSTRACT | PRIVATE_BITS)) {
            throw new IllegalArgumentException("Invalid field modifier(s) given");
        }
        if (bitCount(mods & (PUBLIC | PROTECTED | PRIVATE)) > 1) {
            throw new IllegalArgumentException("Only one of 'public', 'protected', or 'private' may be given");
        }
        return null;
    }

    public JVarDeclaration field(final int mods, final Class<?> type, final String name) {
        return field(mods, JTypes.typeOf(type), name);
    }

    public JVarDeclaration field(final int mods, final Class<?> type, final String name, final JExpr init) {
        return field(mods, JTypes.typeOf(type), name, init);
    }

    public JVarDeclaration field(final int mods, final String type, final String name) {
        return field(mods, JTypes.typeNamed(type), name);
    }

    public JVarDeclaration field(final int mods, final String type, final String name, final JExpr init) {
        return field(mods, JTypes.typeNamed(type), name, init);
    }

    public JMethodDef method(final int mods, final JType returnType, final String name) {
        if (allAreSet(this.mods, JMod.INNER) && allAreSet(mods, JMod.STATIC)) {
            throw new UnsupportedOperationException("Inner classes cannot have static members");
        }
        if (bitCount(mods & (ABSTRACT | FINAL)) > 1) {
            throw new IllegalArgumentException("Only one of 'abstract' or 'final' may be given");
        }
        if (bitCount(mods & (PUBLIC | PROTECTED | PRIVATE)) > 1) {
            throw new IllegalArgumentException("Only one of 'public', 'protected', or 'private' may be given");
        }
        if (anyAreSet(mods, TRANSIENT | VOLATILE | PRIVATE_BITS)) {
            throw new IllegalArgumentException("Invalid method modifier(s) given");
        }
        return add(new MethodJMethodDef(this, mods, returnType, name));
    }

    public JMethodDef method(final int mods, final Class<?> returnType, final String name) {
        return method(mods, JTypes.typeOf(returnType), name);
    }

    public JMethodDef method(final int mods, final String returnType, final String name) {
        return method(mods, JTypes.typeNamed(returnType), name);
    }

    boolean methodCanHaveBody(final int mods) {
        return allAreClear(mods, ABSTRACT | NATIVE);
    }

    boolean hasInterfaceStyleExtends() {
        return false;
    }

    boolean supportsCompactInitOnly() {
        return true;
    }

    public JMethodDef constructor(final int mods) {
        if (bitCount(mods & (PUBLIC | PROTECTED | PRIVATE)) > 1) {
            throw new IllegalArgumentException("Only one of 'public', 'protected', or 'private' may be given");
        }
        if (anyAreSet(mods, TRANSIENT | VOLATILE | ABSTRACT | FINAL | PRIVATE_BITS)) {
            throw new IllegalArgumentException("Invalid constructor modifier(s) given");
        }
        return add(new ConstructorJMethodDef(this, mods));
    }

    public JComment inlineLineComment() {
        return add(new LineJComment());
    }

    public JComment inlineBlockComment() {
        return add(new BlockJComment());
    }

    Iterable<ClassContent> getContent() {
        return content;
    }

    JType getExtends() {
        return _extends;
    }

    Iterable<JType> getImplements() {
        return _implements;
    }

    abstract $KW designation();

    FormatPreferences.Indentation getMemberIndentation() {
        return FormatPreferences.Indentation.MEMBERS_TOP_LEVEL;
    }

    public void write(final SourceFileWriter sourceFileWriter) throws IOException {
        writeAnnotations(sourceFileWriter);
        sourceFileWriter.pushThisType(AbstractJType.of(genericType()));
        try {
            writeClassHeader(sourceFileWriter);
            writeContentBlock(sourceFileWriter);
        } finally {
            sourceFileWriter.popThisType(AbstractJType.of(genericType()));
        }
    }

    void writeContentBlock(final SourceFileWriter sourceFileWriter) throws IOException {
        sourceFileWriter.write(FormatPreferences.Space.BEFORE_BRACE_CLASS);
        sourceFileWriter.write($PUNCT.BRACE.OPEN);
        final boolean hasOption = sourceFileWriter.getFormat().hasOption(FormatPreferences.Opt.COMPACT_INIT_ONLY_CLASS);
        if (supportsCompactInitOnly() && hasOption && content.size() == 1 && content.get(0) instanceof InitJBlock) {
            writeContent(sourceFileWriter);
            sourceFileWriter.write($PUNCT.BRACE.CLOSE);
        } else {
            sourceFileWriter.pushIndent(getMemberIndentation());
            try {
                sourceFileWriter.nl();
                writeContent(sourceFileWriter);
            } finally {
                sourceFileWriter.popIndent(getMemberIndentation());
            }
            sourceFileWriter.nl();
            sourceFileWriter.write($PUNCT.BRACE.CLOSE);
        }
    }

    void writeClassHeader(final SourceFileWriter sourceFileWriter) throws IOException {
        JMod.write(sourceFileWriter, mods);
        sourceFileWriter.write(designation());
        sourceFileWriter.writeClass(name);
        writeTypeParams(sourceFileWriter);
        final boolean ifExt = hasInterfaceStyleExtends();
        if (! ifExt && _extends != null) {
            sourceFileWriter.write($KW.EXTENDS);
            sourceFileWriter.write(_extends);
        }
        if (_implements != null) {
            final Iterator<JType> iterator = _implements.iterator();
            if (iterator.hasNext()) {
                sourceFileWriter.write(ifExt ? $KW.EXTENDS : $KW.IMPLEMENTS);
                sourceFileWriter.write(iterator.next());
                while (iterator.hasNext()) {
                    sourceFileWriter.write($PUNCT.COMMA);
                    sourceFileWriter.write(iterator.next());
                }
            }
        }
    }

    void writeContent(final SourceFileWriter sourceFileWriter) throws IOException {
        for (ClassContent classContent : content) {
            classContent.write(sourceFileWriter);
        }
    }
}
