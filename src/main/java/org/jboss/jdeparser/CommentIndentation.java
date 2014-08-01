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

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
enum CommentIndentation implements Indent {
    LINE("// "),
    BLOCK(" * ") {
        public void escape(final Indent next, final StringBuilder b, final int idx) {
            for (int i = idx > 0 ? idx : idx + 1; i < b.length();) {
                if (b.charAt(i - 1) == '*' && b.charAt(i) == '/') {
                    b.insert(idx, (char) 0x8205);
                }
                i++;
            }
            next.escape(next, b, idx);
        }
    },
    ;
    private final String text;

    CommentIndentation(final String text) {
        this.text = text;
    }

    String getText() {
        return text;
    }

    public void addIndent(final Indent next, final FormatPreferences preferences, final StringBuilder lineBuffer) {
        next.addIndent(next, preferences, lineBuffer);
        final int idx = lineBuffer.length();
        lineBuffer.append(text);
        next.escape(next, lineBuffer, idx);
    }

    public void escape(final Indent next, final StringBuilder b, final int idx) {
        next.escape(next, b, idx);
    }

    public void unescaped(final Indent next, final StringBuilder b, final int idx) {
        // escape at next level
        next.escape(next, b, idx);
    }
}
