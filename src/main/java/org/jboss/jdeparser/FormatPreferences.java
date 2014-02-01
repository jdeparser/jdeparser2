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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Formatter preferences.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class FormatPreferences {

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

        ;
    }

    public enum Indentation {
        MEMBERS_TOP_LEVEL,
        LABELS,
        CASE_LABELS,
        LINE_CONTINUATION,
        
    }

    public enum Wrapping {

    }

    public enum WrappingMode {
        ALWAYS_WRAP,
        WRAP_ONLY_IF_LONG,
        NEVER,
        ;
    }



    private final EnumSet<Space> spaces = EnumSet.of(
        BEFORE_PAREN_IF,
        BEFORE_PAREN_FOR,
        BEFORE_PAREN_WHILE,
        BEFORE_PAREN_SWITCH,
        BEFORE_PAREN_TRY,
        BEFORE_PAREN_CATCH,

        BEFORE_BRACE,

        BEFORE_KEYWORD_ELSE,
        BEFORE_KEYWORD_WHILE,
        BEFORE_KEYWORD_CATCH,
        BEFORE_KEYWORD_FINALLY
    );

    public void addSpaces(Space... spaces) {
        Collections.addAll(this.spaces, spaces);
    }

    public void removeSpaces(Space... spaces) {
        for (Space space : spaces) {
            this.spaces.remove(space);
        }
    }

    public boolean isSpace(Space space) {
        return spaces.contains(space);
    }


    private final String prefix = "format.";




    private final Map<String, String> properties;



    // =====================================================================

    public FormatPreferences(final Properties properties) {
        this(copy(properties));
    }

    public FormatPreferences(final InputStream inputStream) throws IOException {
        this(new InputStreamReader(inputStream, "utf-8"));
    }

    public FormatPreferences(final Reader reader) throws IOException {
        this(load(reader));
    }

    public FormatPreferences(final Map<String, String> map) {
        this.properties = new HashMap<>(map);
    }

    // =====================================================================

    private static HashMap<String, String> copy(Properties properties) {
        final HashMap<String, String> map = new HashMap<>();
        for (String name : properties.stringPropertyNames()) {
            map.put(name, properties.getProperty(name));
        }
        return map;
    }

    private static Properties load(Reader reader) throws IOException {
        final Properties properties = new Properties();
        properties.load(reader);
        return properties;
    }

    // =====================================================================

    private int getInt(String key, int def) {
        final String str = properties.get(key);
        return str == null ? def : Integer.parseInt(str);
    }

    private boolean getBoolean(String key, boolean def) {
        final String str = properties.get(key);
        return str == null ? def : Boolean.parseBoolean(str);
    }

    private WrappingMode getWrapMode(String key, WrappingMode def) {
        final String str = properties.get(key);
        return str == null ? def : WrappingMode.valueOf(str.replace('-', '_').toUpperCase(Locale.US));
    }

    // =====================================================================

    public int getLineLength() {
        return getInt("line.length", 140);
    }

    public int getIndentSize() {
        return getInt("indent", 4);
    }

    public int getLabelIndent() {
        return getInt("indent.label", 0);
    }

    public WrappingMode getDefaultWrapMode() {
        return getWrapMode("wrap", WrappingMode.WRAP_ONLY_IF_LONG);
    }

    public WrappingMode getExceptionListWrapMode() {
        return getWrapMode("wrap.method.exception-list", getDefaultWrapMode());
    }

    public boolean isLabelIndentAbsolute() {
        return getBoolean("indent.label.absolute", false);
    }

    public boolean isCuddledOpenBrace() {
        return getBoolean("braces.open.cuddled", true);
    }

    public boolean isEliminateUselessExpressions() {
        return getBoolean("optimize.expressions", true);
    }

}
