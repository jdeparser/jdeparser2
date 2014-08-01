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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJHtmlTag extends AbstractJHtmlComment implements JHtmlTag, HtmlCommentContent {

    private final String tag;
    private final boolean newLine;
    private final boolean writeClose;
    private Map<String, String> attributes;

    ImplJHtmlTag(final String tag, final boolean newLine, final boolean writeClose) {
        this.tag = tag;
        this.newLine = newLine;
        this.writeClose = writeClose;
    }

    String getTag() {
        return tag;
    }

    boolean isNewLine() {
        return newLine;
    }

    public JHtmlTag attribute(final String name) {
        doAdd(name, null);
        return this;
    }

    public JHtmlTag attribute(final String name, final String value) {
        doAdd(name, value);
        return this;
    }

    private void doAdd(final String name, final String value) {
        Map<String, String> attributes = this.attributes;
        if (attributes == null) {
            this.attributes = attributes = new LinkedHashMap<>();
        }
        attributes.put(name, value);
    }

    Iterable<Map.Entry<String, String>> attributes() {
        Map<String, String> attributes = this.attributes;
        return attributes == null ? Collections.<Map.Entry<String, String>>emptySet() : attributes.entrySet();
    }

    void writeOpenTag(SourceFileWriter writer) throws IOException {
        if (newLine) writer.nl();
        writer.writeUnescaped('<');
        writer.writeEscaped(tag);
        final Map<String, String> attributes = this.attributes;
        if (attributes != null) for (final Map.Entry<String, String> entry : attributes.entrySet()) {
            writer.sp();
            writer.writeEscaped(entry.getKey());
            final String value = entry.getValue();
            if (value != null) {
                writer.writeUnescaped('=');
                writer.writeUnescaped('"');
                writer.writeEscaped(value);
                writer.writeUnescaped('"');
            }
        }
        writer.writeUnescaped('>');
        if (newLine) {
            writer.nl();
        }
    }

    void writeCloseTag(SourceFileWriter writer) throws IOException {
        if (writeClose) {
            if (newLine) writer.nl();
            writer.writeEscaped('<');
            writer.writeEscaped('/');
            writer.writeEscaped(tag);
            writer.writeEscaped('>');
        }
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writeOpenTag(writer);
        writer.pushIndent(FormatPreferences.Indentation.HTML_TAG);
        try {
            super.write(writer);
        } finally {
            writer.popIndent(FormatPreferences.Indentation.HTML_TAG);
        }
    }
}
