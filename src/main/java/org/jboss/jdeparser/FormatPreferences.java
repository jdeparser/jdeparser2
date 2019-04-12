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
        ind.put(Indentation.LABELS, 0);
        ind.put(Indentation.CASE_LABELS, 0);
        ind.put(Indentation.HTML_TAG, 2);
        DEFAULT_INDENTS = ind;

        DEFAULT_ABS_INDENTS = EnumSet.noneOf(Indentation.class);

        EnumMap<Space, SpaceType> spaceTypes = new EnumMap<>(Space.class);
        spaceTypes.put(AFTER_COMMA, SpaceType.SPACE);
        spaceTypes.put(AFTER_COMMA_TYPE_ARGUMENT, SpaceType.SPACE);
        spaceTypes.put(AFTER_COMMA_ENUM_CONSTANT, SpaceType.NEWLINE);

        spaceTypes.put(AFTER_LABEL, SpaceType.SPACE);

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
        spaceTypes.put(BEFORE_BRACE_ARRAY_INIT, SpaceType.SPACE);
        spaceTypes.put(BEFORE_BRACE_CATCH, SpaceType.SPACE);
        spaceTypes.put(BEFORE_BRACE_CLASS, SpaceType.SPACE);
        spaceTypes.put(BEFORE_BRACE_DO, SpaceType.SPACE);
        spaceTypes.put(BEFORE_BRACE_ELSE, SpaceType.SPACE);
        spaceTypes.put(BEFORE_BRACE_FINALLY, SpaceType.SPACE);
        spaceTypes.put(BEFORE_BRACE_FOR, SpaceType.SPACE);
        spaceTypes.put(BEFORE_BRACE_IF, SpaceType.SPACE);
        spaceTypes.put(BEFORE_BRACE_METHOD, SpaceType.SPACE);
        spaceTypes.put(BEFORE_BRACE_SWITCH, SpaceType.SPACE);
        spaceTypes.put(BEFORE_BRACE_SYNCHRONIZE, SpaceType.SPACE);
        spaceTypes.put(BEFORE_BRACE_TRY, SpaceType.SPACE);
        spaceTypes.put(BEFORE_BRACE_WHILE, SpaceType.SPACE);

        spaceTypes.put(AFTER_SEMICOLON, SpaceType.SPACE);

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

        spaceTypes.put(BEFORE_COLON, SpaceType.SPACE);
        spaceTypes.put(AFTER_COLON, SpaceType.SPACE);

        spaceTypes.put(AFTER_ANNOTATION, SpaceType.NEWLINE);
        spaceTypes.put(AFTER_PARAM_ANNOTATION, SpaceType.SPACE);

        DEFAULT_SPACE_TYPES = spaceTypes;

        DEFAULT_OPTS = EnumSet.of(Opt.COMPACT_INIT_ONLY_CLASS);

        EnumMap<Wrapping, WrappingMode> wm = new EnumMap<>(Wrapping.class);
        wm.put(Wrapping.EXCEPTION_LIST, WrappingMode.WRAP_ONLY_IF_LONG);
        DEFAULT_WRAPPING = wm;
    }

    private final EnumIntMap<Indentation> indents;
    private final EnumSet<Indentation> absoluteIndents;
    private final EnumMap<Space, SpaceType> spaceTypes;
    private final EnumSet<Opt> options;
    private final EnumMap<Wrapping, WrappingMode> wrapping;

    private int lineLength = 140;

    // =====================================================================

    /**
     * Construct a new instance using default values.
     */
    public FormatPreferences() {
        indents = new EnumIntMap<>(DEFAULT_INDENTS);
        absoluteIndents = EnumSet.copyOf(DEFAULT_ABS_INDENTS);
        spaceTypes = new EnumMap<>(DEFAULT_SPACE_TYPES);
        options = EnumSet.copyOf(DEFAULT_OPTS);
        wrapping = new EnumMap<>(DEFAULT_WRAPPING);
    }

    /**
     * Construct a new instance, mapping the given properties to the formatter configurations.
     *
     * @param properties the properties to map
     */
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
                                if (v) options.add(o); else options.remove(o);
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

    /**
     * Construct a new instance using a properties file loaded from the given class loader.
     *
     * @param classLoader the class loader
     * @throws IOException if an error occurs while reading the properties
     */
    public FormatPreferences(final ClassLoader classLoader) throws IOException {
        this(fnf(classLoader.getResourceAsStream(PROPERTIES_FILE_NAME)));
    }

    /**
     * Construct a new instance using a properties file loaded from the given annotation processing filer.
     *
     * @param filer the filer to read from
     * @param name the name of the properties file to read
     * @throws IOException if an error occurs while reading the properties
     */
    public FormatPreferences(final Filer filer, final String name) throws IOException {
        this(filer.getResource(StandardLocation.ANNOTATION_PROCESSOR_PATH, "", name).openInputStream());
    }

    /**
     * Construct a new instance using a properties file loaded from the given annotation processing filer.
     *
     * @param filer the filer to read from
     * @throws IOException if an error occurs while reading the properties
     */
    public FormatPreferences(final Filer filer) throws IOException {
        this(filer, PROPERTIES_FILE_NAME);
    }

    /**
     * Construct a new instance using a properties file loaded from the given file name.
     *
     * @param file the name of the properties file to read
     * @throws IOException if an error occurs while reading the properties
     */
    public FormatPreferences(final File file) throws IOException {
        this(new FileInputStream(file));
    }

    /**
     * Construct a new instance using a properties read from the given stream.
     *
     * @param inputStream the stream to read properties from
     * @throws IOException if an error occurs while reading the properties
     */
    public FormatPreferences(final InputStream inputStream) throws IOException {
        this(new InputStreamReader(inputStream, "utf-8"));
    }

    /**
     * Construct a new instance using a properties read from the given stream.
     *
     * @param reader the stream to read properties from
     * @throws IOException if an error occurs while reading the properties
     */
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

    /**
     * Get the configured line length.
     *
     * @return the configured line length
     */
    public int getLineLength() {
        return lineLength;
    }

    // ---------------------------------------------------------------------

    /**
     * Get the configured indentation for the given context.
     *
     * @param indentation the indentation context
     * @return the indentation
     */
    public int getIndent(Indentation indentation) {
        return indents.get(indentation);
    }

    /**
     * Set the configured indentation for the given context.
     *
     * @param indentation the indentation context
     * @param value the indentation
     * @return the previous indentation
     */
    public int setIndent(Indentation indentation, int value) {
        return indents.put(indentation, value);
    }

    // ---------------------------------------------------------------------

    /**
     * Determine whether the indentation for the given context is absolute or relative.
     *
     * @param indentation the indentation context
     * @return {@code true} if absolute, {@code false} if relative
     */
    public boolean isIndentAbsolute(Indentation indentation) {
        return absoluteIndents.contains(indentation);
    }

    /**
     * Set absolute indentation for the given context.
     *
     * @param indentation the indentation context
     */
    public void setIndentAbsolute(Indentation indentation) {
        absoluteIndents.add(indentation);
    }

    /**
     * Clear absolute indentation for the given context.
     *
     * @param indentation the indentation context
     */
    public void clearIndentAbsolute(Indentation indentation) {
        absoluteIndents.remove(indentation);
    }

    // ---------------------------------------------------------------------

    /**
     * Set the spacing type for the given space context.
     *
     * @param space the space context
     * @param spaceType the space type
     * @return the previous space type
     */
    public SpaceType setSpaceType(Space space, SpaceType spaceType) {
        if (space == null) {
            throw new IllegalArgumentException("space is null");
        }
        if (spaceType == null) {
            throw new IllegalArgumentException("spaceType is null");
        }
        return def(spaceTypes.put(space, spaceType), SpaceType.NONE);
    }

    /**
     * Set several space contexts to the same spacing type.
     *
     * @param toType the type to set to
     * @param spaces the space contexts
     */
    public void setAllSpaceTypes(SpaceType toType, Space... spaces) {
        if (toType == null) {
            throw new IllegalArgumentException("toType is null");
        }
        if (spaces != null) for (Space space : spaces) {
            def(spaceTypes.put(space, toType), SpaceType.NONE);
        }
    }

    /**
     * Get the spacing type for a given space context.
     *
     * @param space the space context
     * @return the spacing type
     */
    public SpaceType getSpaceType(Space space) {
        if (space == null) {
            throw new IllegalArgumentException("space is null");
        }
        return def(spaceTypes.get(space), SpaceType.NONE);
    }

    // ---------------------------------------------------------------------

    /**
     * Get the wrapping mode for the given wrapping context.
     *
     * @param wrapping the wrapping context
     * @return the current wrapping mode
     */
    public WrappingMode getWrapMode(Wrapping wrapping) {
        return def(this.wrapping.get(wrapping), WrappingMode.WRAP_ONLY_IF_LONG);
    }

    /**
     * Set the wrapping mode for the given wrapping context.
     *
     * @param wrapping the wrapping context
     * @param mode the wrapping mode
     * @return the previous wrapping mode
     */
    public WrappingMode setWrapMode(Wrapping wrapping, WrappingMode mode) {
        return def(this.wrapping.put(wrapping, mode), WrappingMode.WRAP_ONLY_IF_LONG);
    }

    // ---------------------------------------------------------------------

    /**
     * Add option flags to these preferences.
     *
     * @param opts the flags to add
     */
    public void addOption(Opt... opts) {
        Collections.addAll(this.options, opts);
    }

    /**
     * Remove option flags from these preferences.
     *
     * @param opts the flags to remove
     */
    public void removeOption(Opt... opts) {
        for (Opt opt : opts) {
            this.options.remove(opt);
        }
    }

    /**
     * Determine whether the given option flag is set on these preferences.
     *
     * @param opt the flag to check
     * @return {@code true} if the flag is present, {@code false} if it is absent
     */
    public boolean hasOption(Opt opt) {
        return options.contains(opt);
    }

    // =====================================================================

    /**
     * The type of space to apply.
     */
    public enum SpaceType {
        NONE,
        SPACE,
        NEWLINE,
    }

    /**
     * The location or position of a space.
     */
    public enum Space {
        // default for all parens
        @Deprecated
        BEFORE_PAREN,

        // single-line statements
        BEFORE_STATEMENT,

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
        @Deprecated
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

        // colon (assert, foreach)
        BEFORE_COLON,
        AFTER_COLON,

        // enum
        AFTER_COMMA_ENUM_CONSTANT,

        // semi
        BEFORE_SEMICOLON,
        AFTER_SEMICOLON,

        // cast
        AFTER_CAST,

        // colon (label, case, default)
        AFTER_LABEL,

        // annotations
        AFTER_ANNOTATION,
        AFTER_PARAM_ANNOTATION,
        ;
    }

    // ---------------------------------------------------------------------

    /**
     * A category of indentation.
     */
    public enum Indentation {
        MEMBERS_TOP_LEVEL,
        LABELS,
        CASE_LABELS,
        LINE_CONTINUATION,
        LINE,
        HTML_TAG,
        ;
        private final Indent indent;

        Indentation() {
            indent = new ConfigIndent(this);
        }

        Indent getIndent() {
            return indent;
        }
    }

    // ---------------------------------------------------------------------

    /**
     * Categories for wrapping rules.
     */
    public enum Wrapping {
        EXCEPTION_LIST,

    }

    // ---------------------------------------------------------------------

    /**
     * The wrapping mode.
     */
    public enum WrappingMode {
        ALWAYS_WRAP,
        WRAP_ONLY_IF_LONG,
        NEVER,
        ;
    }

    // ---------------------------------------------------------------------

    /**
     * Option flags.
     */
    public enum Opt {
        ENUM_TRAILING_COMMA,
        ENUM_EMPTY_PARENS,
        COMPACT_INIT_ONLY_CLASS,
        DROP_UNUSED_LABELS,
    }



}
