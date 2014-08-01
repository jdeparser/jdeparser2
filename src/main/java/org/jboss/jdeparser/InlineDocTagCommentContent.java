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

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class InlineDocTagCommentContent extends AbstractJComment implements CommentContent {

    static final Indent INDENT = new Indent() {
        public void addIndent(final Indent next, final FormatPreferences preferences, final StringBuilder lineBuffer) {
            next.addIndent(next, preferences, lineBuffer);
        }

        public void escape(final Indent next, final StringBuilder b, final int idx) {
            char ch;
            for (int i = idx; i < b.length();) {
                ch = b.charAt(i);
                // not pretty but always renders correctly
                switch (ch) {
                    case '}':
                        b.replace(i, i + 1, "&#125;");
                        i += 6;
                        break;
                    case '{':
                        b.replace(i, i + 1, "&#123;");
                        i += 6;
                        break;
                    default:
                        i++;
                        break;
                }
            }
        }

        public void unescaped(final Indent next, final StringBuilder b, final int idx) {
            // next level is escaped
            next.escape(next, b, idx);
        }
    };
    private final String tagName;

    InlineDocTagCommentContent(final String tagName) {
        this.tagName = tagName;
    }

    String getTagName() {
        return tagName;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writer.writeUnescaped("{@");
        writer.pushIndent(INDENT);
        try {
            writer.writeEscaped(tagName);
            writer.sp();
            super.write(writer);
        } finally {
            writer.popIndent(INDENT);
        }
        writer.writeUnescaped("}");
    }
}
