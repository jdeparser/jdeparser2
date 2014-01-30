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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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

    private WrapMode getWrapMode(String key, WrapMode def) {
        final String str = properties.get(key);
        return str == null ? def : WrapMode.valueOf(str.replace('-', '_').toUpperCase(Locale.US));
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

    public WrapMode getDefaultWrapMode() {
        return getWrapMode("wrap", WrapMode.WRAP_ONLY_IF_LONG);
    }

    public WrapMode getExceptionListWrapMode() {
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

    public enum WrapMode {
        ALWAYS_WRAP,
        WRAP_ONLY_IF_LONG,
        NEVER,
        ;
    }
}
