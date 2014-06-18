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

import static org.jboss.jdeparser.FormatPreferences.Space.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Properties;

import javax.annotation.processing.Filer;
import javax.tools.StandardLocation;

/**
 * Formatter preferences.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class FormatPreferences {

    private static final String PROPERTIES_FILE_NAME = "jdeparser.properties";

    private static final EnumIntMap<Indentation> DEFAULT_INDENTS;
    private static final EnumSet<Indentation> DEFAULT_ABS_INDENTS;
    private static final EnumMap<Space, SpaceType> DEFAULT_SPACE_TYPES;
    private static final EnumSet<Opt> DEFAULT_OPTS;
    private static final EnumMap<Wrapping, WrappingMode> DEFAULT_WRAPPING;

    static {
        EnumIntMap<Indentation> ind = new EnumIntMap<>(Indentation.class, 0);
        ind.put(Indentation.LINE, 4);
        ind.put(Indentation.LINE_CONTINUATION, 4);
        ind.put(Indentation.MEMBERS_TOP_LEVEL, 4);
        DEFAULT_INDENTS = ind;

        DEFAULT_ABS_INDENTS = EnumSet.noneOf(Indentation.class);

        EnumMap<Space, SpaceType> spaceTypes = new EnumMap<>(Space.class);
        spaceTypes.put(AFTER_COMMA, SpaceType.SPACE);
        spaceTypes.put(AFTER_COMMA_TYPE_ARGUMENT, SpaceType.SPACE);

        spaceTypes.put(BEFORE_PAREN_IF, SpaceType.SPACE);
        spaceTypes.put(BEFORE_PAREN_FOR, SpaceType.SPACE);
        spaceTypes.put(BEFORE_PAREN_WHILE, SpaceType.SPACE);
        spaceTypes.put(BEFORE_PAREN_SWITCH, SpaceType.SPACE);
        spaceTypes.put(BEFORE_PAREN_TRY, SpaceType.SPACE);
        spaceTypes.put(BEFORE_PAREN_CATCH, SpaceType.SPACE);
        spaceTypes.put(BEFORE_PAREN_SYNCHRONIZED, SpaceType.SPACE);
        spaceTypes.put(BEFORE_PAREN_CAST, SpaceType.SPACE);

        spaceTypes.put(BEFORE_BRACE, SpaceType.SPACE);
        spaceTypes.put(BEFORE_BRACE_ANNOTATION_ARRAY_INIT, SpaceType.NONE);
        spaceTypes.put(BEFORE_BRACE_CLASS, SpaceType.SPACE);

        spaceTypes.put(BEFORE_KEYWORD_ELSE, SpaceType.SPACE);
        spaceTypes.put(BEFORE_KEYWORD_WHILE, SpaceType.SPACE);
        spaceTypes.put(BEFORE_KEYWORD_CATCH, SpaceType.SPACE);
        spaceTypes.put(BEFORE_KEYWORD_FINALLY, SpaceType.SPACE);

        spaceTypes.put(AROUND_ADDITIVE, SpaceType.SPACE);
        spaceTypes.put(AROUND_MULTIPLICATIVE, SpaceType.SPACE);
        spaceTypes.put(AROUND_SHIFT, SpaceType.SPACE);
        spaceTypes.put(AROUND_BITWISE, SpaceType.SPACE);
        spaceTypes.put(AROUND_ASSIGN, SpaceType.SPACE);
        spaceTypes.put(AROUND_LOGICAL, SpaceType.SPACE);
        spaceTypes.put(AROUND_EQUALITY, SpaceType.SPACE);
        spaceTypes.put(AROUND_RANGE, SpaceType.SPACE);
        spaceTypes.put(AROUND_ARROW, SpaceType.SPACE);

        spaceTypes.put(WITHIN_BRACES_CODE, SpaceType.NEWLINE);
        spaceTypes.put(WITHIN_BRACES_ARRAY_INIT, SpaceType.SPACE);

        spaceTypes.put(BEFORE_TERNARY_Q, SpaceType.SPACE);
        spaceTypes.put(AFTER_TERNARY_Q, SpaceType.SPACE);
        spaceTypes.put(BEFORE_TERNARY_COLON, SpaceType.SPACE);
        spaceTypes.put(AFTER_TERNARY_COLON, SpaceType.SPACE);
        DEFAULT_SPACE_TYPES = spaceTypes;

        DEFAULT_OPTS = EnumSet.allOf(Opt.class);

        EnumMap<Wrapping, WrappingMode> wm = new EnumMap<>(Wrapping.class);
        wm.put(Wrapping.EXCEPTION_LIST, WrappingMode.WRAP_ONLY_IF_LONG);
        DEFAULT_WRAPPING = wm;
    }

    private final EnumIntMap<Indentation> indents;
    private final EnumSet<Indentation> absoluteIndents;
    private final EnumMap<Space, SpaceType> spaceTypes;
    private final EnumSet<Opt> optimizations;
    private final EnumMap<Wrapping, WrappingMode> wrapping;

    private int lineLength = 140;

    // =====================================================================

    public FormatPreferences() {
        indents = new EnumIntMap<>(DEFAULT_INDENTS);
        absoluteIndents = EnumSet.copyOf(DEFAULT_ABS_INDENTS);
        spaceTypes = new EnumMap<>(DEFAULT_SPACE_TYPES);
        optimizations = EnumSet.copyOf(DEFAULT_OPTS);
        wrapping = new EnumMap<>(DEFAULT_WRAPPING);
    }

    public FormatPreferences(final Properties properties) {
        // initialize
        this();
        final ArrayList<String> l = new ArrayList<>();
        for (String name : properties.stringPropertyNames()) {
            split(l, '.', name);
            // unknown properties are ignored for forward/backward compat
            final String value = properties.getProperty(name);
            switch (l.size()) {
                case 1: {
                    switch (l.get(0)) {
                        case "line-length": {
                            try {
                                lineLength = Integer.parseInt(value);
                            } catch (IllegalArgumentException ignored) {
                            }
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    switch (l.get(0)) {
                        case "indent": {
                            try {
                                final Indentation i = Indentation.valueOf(xf(l.get(1)));
                                final int v = Integer.parseInt(value);
                                indents.put(i, v);
                            } catch (IllegalArgumentException ignored) {
                            }
                            break;
                        }
                        case "wrapping": {
                            try {
                                final Wrapping w = Wrapping.valueOf(xf(l.get(1)));
                                final WrappingMode m = WrappingMode.valueOf(xf(value));
                                wrapping.put(w, m);
                            } catch (IllegalArgumentException ignored) {
                            }
                            break;
                        }
                        case "space": {
                            try {
                                final Space s = Space.valueOf(xf(l.get(1)));
                                final SpaceType t = SpaceType.valueOf(xf(value));
                                spaceTypes.put(s, t);
                            } catch (IllegalArgumentException ignored) {
                            }
                            break;
                        }
                        case "optimization": {
                            try {
                                final Opt o = Opt.valueOf(xf(l.get(1)));
                                final boolean v = Boolean.parseBoolean(value);
                                if (v) optimizations.add(o); else optimizations.remove(o);
                            } catch (IllegalArgumentException ignored) {
                            }
                            break;
                        }
                    }
                    break;
                }
                case 3: {
                    switch (l.get(0)) {
                        case "indent": {
                            switch (l.get(2)) {
                                case "absolute": {
                                    try {
                                        final Indentation i = Indentation.valueOf(xf(l.get(1)));
                                        final boolean v = Boolean.parseBoolean(value);
                                        if (v) absoluteIndents.add(i); else absoluteIndents.remove(i);
                                    } catch (IllegalArgumentException ignored) {
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public FormatPreferences(final ClassLoader classLoader) throws IOException {
        this(fnf(classLoader.getResourceAsStream(PROPERTIES_FILE_NAME)));
    }

    public FormatPreferences(final Filer filer, final String name) throws IOException {
        this(filer.getResource(StandardLocation.ANNOTATION_PROCESSOR_PATH, "", name).openInputStream());
    }

    public FormatPreferences(final Filer filer) throws IOException {
        this(filer, PROPERTIES_FILE_NAME);
    }

    public FormatPreferences(final File file) throws IOException {
        this(new FileInputStream(file));
    }

    public FormatPreferences(final InputStream inputStream) throws IOException {
        this(new InputStreamReader(inputStream, "utf-8"));
    }

    public FormatPreferences(final Reader reader) throws IOException {
        this(load(reader));
    }

    // =====================================================================

    private static InputStream fnf(InputStream stream) throws IOException {
        if (stream == null) throw new FileNotFoundException(PROPERTIES_FILE_NAME);
        return stream;
    }

    private static String xf(String name) {
        return name.toUpperCase(Locale.US).replace('-', '_');
    }

    private static void split(ArrayList<String> dest, char delim, String s) {
        dest.clear();
        int st = 0, i;
        i = s.indexOf(delim);
        for (;;) {
            if (i == -1) {
                dest.add(s.substring(st));
                return;
            }
            dest.add(s.substring(st, i));
            st = i + 1;
            i = s.indexOf(delim, st);
        }
    }

    private static Properties load(Reader reader) throws IOException {
        final Properties properties = new Properties();
        properties.load(reader);
        return properties;
    }

    // =====================================================================

    private <T> T def(T val, T def) {
        return val == null ? def : val;
    }

    // =====================================================================

    public int getLineLength() {
        return lineLength;
    }

    // ---------------------------------------------------------------------

    public int getIndent(Indentation indentation) {
        return indents.get(indentation);
    }

    public int setIndent(Indentation indentation, int value) {
        return indents.put(indentation, value);
    }

    // ---------------------------------------------------------------------

    public boolean isIndentAbsolute(Indentation indentation) {
        return absoluteIndents.contains(indentation);
    }

    public void setIndentAbsolute(Indentation indentation) {
        absoluteIndents.add(indentation);
    }
    
    public void clearIndentAbsolute(Indentation indentation) {
        absoluteIndents.remove(indentation);
    }
    
    // ---------------------------------------------------------------------

    public SpaceType setSpaceType(Space space, SpaceType spaceType) {
        if (space == null) {
            throw new IllegalArgumentException("space is null");
        }
        if (spaceType == null) {
            throw new IllegalArgumentException("spaceType is null");
        }
        return def(spaceTypes.put(space, spaceType), SpaceType.NONE);
    }

    public void setAllSpaceTypes(SpaceType toType, Space... spaces) {
        if (toType == null) {
            throw new IllegalArgumentException("toType is null");
        }
        if (spaces != null) for (Space space : spaces) {
            def(spaceTypes.put(space, toType), SpaceType.NONE);
        }
    }

    public SpaceType getSpaceType(Space space) {
        if (space == null) {
            throw new IllegalArgumentException("space is null");
        }
        return def(spaceTypes.get(space), SpaceType.NONE);
    }

    // ---------------------------------------------------------------------

    public WrappingMode getWrapMode(Wrapping wrapping) {
        return def(this.wrapping.get(wrapping), WrappingMode.WRAP_ONLY_IF_LONG);
    }

    public WrappingMode setWrapMode(Wrapping wrapping, WrappingMode mode) {
        return def(this.wrapping.put(wrapping, mode), WrappingMode.WRAP_ONLY_IF_LONG);
    }

    // ---------------------------------------------------------------------

    public void addOptimization(Opt... opts) {
        Collections.addAll(this.optimizations, opts);
    }

    public void removeOptimization(Opt... opts) {
        for (Opt opt : opts) {
            this.optimizations.remove(opt);
        }
    }

    public boolean hasOptimization(Opt opt) {
        return optimizations.contains(opt);
    }

    // =====================================================================

    public enum SpaceType {
        NONE,
        SPACE,
        NEWLINE,
    }

    public enum Space {
        // default for all parens
        BEFORE_PAREN,

        // paren sites
        BEFORE_PAREN_METHOD_CALL,
        BEFORE_PAREN_METHOD_DECLARATION,
        BEFORE_PAREN_IF,
        BEFORE_PAREN_FOR,
        BEFORE_PAREN_WHILE,
        BEFORE_PAREN_SWITCH,
        BEFORE_PAREN_TRY,
        BEFORE_PAREN_CATCH,
        BEFORE_PAREN_SYNCHRONIZED,
        BEFORE_PAREN_ANNOTATION_PARAM,
        BEFORE_PAREN_CAST,

        // default for all binary operators
        AROUND_OPERATORS,

        // specific operator categories
        AROUND_ASSIGN,
        AROUND_LOGICAL,
        AROUND_EQUALITY,
        AROUND_RANGE,
        AROUND_BITWISE,
        AROUND_ADDITIVE,
        AROUND_MULTIPLICATIVE,
        AROUND_SHIFT,
        AROUND_ARROW,
        AROUND_METHOD_REF,

        // default for all unary operators
        AT_UNARY,

        // default for all braces
        BEFORE_BRACE,

        // specific braces sites
        BEFORE_BRACE_CLASS,
        BEFORE_BRACE_METHOD,
        BEFORE_BRACE_IF,
        BEFORE_BRACE_ELSE,
        BEFORE_BRACE_FOR,
        BEFORE_BRACE_WHILE,
        BEFORE_BRACE_DO,
        BEFORE_BRACE_SWITCH,
        BEFORE_BRACE_TRY,
        BEFORE_BRACE_CATCH,
        BEFORE_BRACE_FINALLY,
        BEFORE_BRACE_SYNCHRONIZE,
        BEFORE_BRACE_ARRAY_INIT,
        BEFORE_BRACE_ANNOTATION_ARRAY_INIT,

        BEFORE_KEYWORD_ELSE,
        BEFORE_KEYWORD_WHILE,
        BEFORE_KEYWORD_CATCH,
        BEFORE_KEYWORD_FINALLY,

        // within...
        WITHIN_BRACES_CODE,
        WITHIN_BRACES_EMPTY,
        WITHIN_BRACES_ARRAY_INIT,

        WITHIN_BRACKETS,

        WITHIN_PAREN_EXPR,
        WITHIN_PAREN_METHOD_CALL,
        WITHIN_PAREN_METHOD_CALL_EMPTY,
        WITHIN_PAREN_METHOD_DECLARATION,
        WITHIN_PAREN_METHOD_DECLARATION_EMPTY,
        WITHIN_PAREN_IF,
        WITHIN_PAREN_FOR,
        WITHIN_PAREN_WHILE,
        WITHIN_PAREN_SWITCH,
        WITHIN_PAREN_TRY,
        WITHIN_PAREN_CATCH,
        WITHIN_PAREN_SYNCHRONIZED,
        WITHIN_PAREN_CAST,
        WITHIN_PAREN_ANNOTATION,

        // ternary
        BEFORE_TERNARY_Q,
        AFTER_TERNARY_Q,
        BEFORE_TERNARY_COLON,
        AFTER_TERNARY_COLON,

        // type arguments
        AFTER_COMMA_TYPE_ARGUMENT,

        // comma
        BEFORE_COMMA,
        AFTER_COMMA,

        // semi
        BEFORE_SEMICOLON,
        AFTER_SEMICOLON,

        // cast
        AFTER_CAST,
        ;
    }

    // ---------------------------------------------------------------------

    public enum Indentation {
        MEMBERS_TOP_LEVEL,
        LABELS,
        CASE_LABELS,
        LINE_CONTINUATION,
        LINE,

    }

    // ---------------------------------------------------------------------

    public enum Wrapping {
        EXCEPTION_LIST,

    }

    // ---------------------------------------------------------------------

    public enum WrappingMode {
        ALWAYS_WRAP,
        WRAP_ONLY_IF_LONG,
        NEVER,
        ;
    }

    // ---------------------------------------------------------------------

    public enum Opt {
        ELIMINATE_EXPRESSIONS,

    }



}
