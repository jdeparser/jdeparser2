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
abstract class AbstractJClassDef extends AbstractJGeneric implements JClassDef, ClassFileContent, ClassContent, Sectionable {

    private final int mods;
    private final String name;
    private final AbstractJClassDef enclosingClass;
    private final ImplJSourceFile classFile;

    private final ArrayList<ClassContent> content = new ArrayList<>();
    private JType _extends;
    private ArrayList<JType> _implements;
    private JType erased;
    private JType generic;

    AbstractJClassDef(final int mods, final String name) {
        this.mods = mods;
        this.name = name;
        this.enclosingClass = null;
        this.classFile = null;
    }

    AbstractJClassDef(final int mods, final AbstractJClassDef enclosingClass, final String name) {
        this.mods = mods;
        this.name = name;
        this.enclosingClass = enclosingClass;
        this.classFile = enclosingClass.classFile;
    }

    AbstractJClassDef(final int mods, final ImplJSourceFile classFile, final String name) {
        this.mods = mods;
        this.name = name;
        this.enclosingClass = null;
        this.classFile = classFile;
    }

    AbstractJClassDef getEnclosingClass() {
        return enclosingClass;
    }

    ImplJSourceFile getClassFile() {
        return classFile;
    }

    int getMods() {
        return mods;
    }

    String getName() {
        return name;
    }

    <C extends ClassContent> C add(C item) {
        return add(content, item);
    }

    <C extends ClassContent> C add(ArrayList<ClassContent> content, C item) {
        content.add(item);
        return item;
    }

    public JComment lineComment() {
        return add(new LineJComment());
    }

    public JComment blockComment() {
        return add(new BlockJComment());
    }

    public JClassDefSection section() {
        return add(new JClassDefSectionImpl(this));
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

    public JClassDef blankLine() {
        add(BlankLine.getInstance());
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

    public final JBlock init() {
        return init(content);
    }

    public JBlock init(final ArrayList<ClassContent> content) {
        return add(content, new InitJBlock());
    }

    public final JBlock staticInit() {
        return staticInit(content);
    }

    public JBlock staticInit(final ArrayList<ClassContent> content) {
        if (allAreSet(mods, JMod.INNER)) {
            throw new UnsupportedOperationException("Inner classes cannot have static init blocks");
        }
        return add(content, new StaticInitJBlock());
    }

    public JEnumConstant _enum(final String name) {
        throw new UnsupportedOperationException("Enum constants may only be added to enums");
    }

    public JVarDeclaration field(final ArrayList<ClassContent> content, final int mods, final JType type, final String name, final JExpr init) {
        if (allAreSet(this.mods, JMod.INNER) && allAreSet(mods, JMod.STATIC)) {
            throw new UnsupportedOperationException("Inner classes cannot have static members");
        }
        if (anyAreSet(mods, STRICTFP | ABSTRACT | PRIVATE_BITS)) {
            throw new IllegalArgumentException("Invalid field modifier(s) given");
        }
        if (bitCount(mods & (PUBLIC | PROTECTED | PRIVATE)) > 1) {
            throw new IllegalArgumentException("Only one of 'public', 'protected', or 'private' may be given");
        }
        return add(content, new FirstJVarDeclaration(mods, type, name, init));
    }

    public final JVarDeclaration field(final int mods, final JType type, final String name) {
        return field(mods, type, name, null);
    }

    public final JVarDeclaration field(final int mods, final JType type, final String name, final JExpr init) {
        return field(content, mods, type, name, init);
    }

    public final JVarDeclaration field(final int mods, final Class<?> type, final String name) {
        return field(mods, JTypes.typeOf(type), name);
    }

    public final JVarDeclaration field(final int mods, final Class<?> type, final String name, final JExpr init) {
        return field(mods, JTypes.typeOf(type), name, init);
    }

    public final JVarDeclaration field(final int mods, final String type, final String name) {
        return field(mods, JTypes.typeNamed(type), name);
    }

    public final JVarDeclaration field(final int mods, final String type, final String name, final JExpr init) {
        return field(mods, JTypes.typeNamed(type), name, init);
    }

    public JMethodDef method(final ArrayList<ClassContent> content, final int mods, final JType returnType, final String name) {
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
        return add(content, new MethodJMethodDef(this, mods, returnType, name));
    }

    public final JMethodDef method(final int mods, final JType returnType, final String name) {
        return method(content, mods, returnType, name);
    }

    public final JMethodDef method(final int mods, final Class<?> returnType, final String name) {
        return method(mods, JTypes.typeOf(returnType), name);
    }

    public final JMethodDef method(final int mods, final String returnType, final String name) {
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

    public JMethodDef constructor(final ArrayList<ClassContent> content, final int mods) {
        if (bitCount(mods & (PUBLIC | PROTECTED | PRIVATE)) > 1) {
            throw new IllegalArgumentException("Only one of 'public', 'protected', or 'private' may be given");
        }
        if (anyAreSet(mods, TRANSIENT | VOLATILE | ABSTRACT | FINAL | PRIVATE_BITS)) {
            throw new IllegalArgumentException("Invalid constructor modifier(s) given");
        }
        return add(content, new ConstructorJMethodDef(this, mods));
    }

    public final JMethodDef constructor(final int mods) {
        return constructor(content, mods);
    }

    public JClassDef _class(final ArrayList<ClassContent> content, final int mods, final String name) {
        return add(content, new PlainJClassDef(mods, this, name));
    }

    public JClassDef _enum(final ArrayList<ClassContent> content, final int mods, final String name) {
        return add(content, new EnumJClassDef(mods, this, name));
    }

    public JClassDef _interface(final ArrayList<ClassContent> content, final int mods, final String name) {
        return add(content, new InterfaceJClassDef(mods, this, name));
    }

    public JClassDef annotationInterface(final ArrayList<ClassContent> content, final int mods, final String name) {
        return add(content, new AnnotationJClassDef(mods, this, name));
    }

    public final JClassDef _class(final int mods, final String name) {
        return _class(content, mods, name);
    }

    public final JClassDef _enum(final int mods, final String name) {
        return _enum(content, mods, name);
    }

    public final JClassDef _interface(final int mods, final String name) {
        return _interface(content, mods, name);
    }

    public final JClassDef annotationInterface(final int mods, final String name) {
        return annotationInterface(content, mods, name);
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

    public void write(final SourceFileWriter writer) throws IOException {
        writeDocComments(writer);
        writeComments(writer);
        writeAnnotations(writer, FormatPreferences.Space.AFTER_ANNOTATION);
        writer.pushThisType(AbstractJType.of(genericType()));
        try {
            writeClassHeader(writer);
            writeContentBlock(writer);
        } finally {
            writer.popThisType(AbstractJType.of(genericType()));
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
            sourceFileWriter.nl();
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
                    sourceFileWriter.write(FormatPreferences.Space.BEFORE_COMMA);
                    sourceFileWriter.write($PUNCT.COMMA);
                    sourceFileWriter.write(FormatPreferences.Space.AFTER_COMMA);
                    sourceFileWriter.write(iterator.next());
                }
            }
        }
    }

    void writeContent(final SourceFileWriter sourceFileWriter) throws IOException {
        Iterator<ClassContent> iterator = content.iterator();
        if (iterator.hasNext()) {
            iterator.next().write(sourceFileWriter);
            while (iterator.hasNext()) {
                sourceFileWriter.nl();
                iterator.next().write(sourceFileWriter);
            }
        }
    }

    public int getModifiers() {
        return mods;
    }

    public boolean hasAllModifiers(final int mods) {
        return (this.mods & mods) == mods;
    }

    public boolean hasAnyModifier(final int mods) {
        return (this.mods & mods) != 0;
    }
}
