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
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJDocComment extends AbstractJDocComment implements JDocComment {

    private static final Indent escIndent = new Indent() {
        public void addIndent(final Indent next, final FormatPreferences preferences, final StringBuilder lineBuffer) {
            final int idx = lineBuffer.length();
            next.addIndent(next, preferences, lineBuffer);
            next.escape(next, lineBuffer, idx);
        }

        public void escape(final Indent next, final StringBuilder b, final int idx) {
            int c;
            int end;
            for (int i = idx; i < b.length();) {
                c = b.codePointAt(i);
                end = b.offsetByCodePoints(i, 1);
                switch (c) {
                    // only use entities where necessary or very common practice
                    case '<':    b.replace(i, end, "&lt;"); i += 4; break;
                    case '>':    b.replace(i, end, "&gt;"); i += 4; break;
                    case '&':    b.replace(i, end, "&amp;"); i += 5; break;
                    case '@':    b.replace(i, end, "&#64;"); i += 5; break;
                    case 0xAD:   b.replace(i, end, "&shy;"); i += 5; break;
                    case 0x200D: b.replace(i, end, "&zwj;"); i += 5; break;
                    case 0x200E: b.replace(i, end, "&lrm;"); i += 5; break;
                    case 0x200F: b.replace(i, end, "&rlm;"); i += 5; break;
                    case 0xA0:   b.replace(i, end, "&nbsp;"); i += 6; break;
                    case 0x2002: b.replace(i, end, "&ensp;"); i += 6; break;
                    case 0x2003: b.replace(i, end, "&emsp;"); i += 6; break;
                    case 0x200C: b.replace(i, end, "&zwnj;"); i += 6; break;
                    case 0x2009: b.replace(i, end, "&thisp;"); i += 7; break;
                    // special cases
                    case ' ': i++; break;
                    case '\n': i++; break;
                    // otherwise escape it
                    default: {
                        switch (Character.getType(c)) {
                            case Character.UNASSIGNED:
                            case Character.CONTROL:
                            case Character.FORMAT:
                            case Character.SURROGATE:
                            case Character.NON_SPACING_MARK:
                            case Character.COMBINING_SPACING_MARK: // also DIRECTIONALITY_NONSPACING_MARK
                            case Character.ENCLOSING_MARK:
                            case Character.LINE_SEPARATOR:
                            case Character.PARAGRAPH_SEPARATOR:
                            case Character.SPACE_SEPARATOR:
                            {
                                b.replace(i, end, "&#x");
                                final String hs = Integer.toHexString(c);
                                b.insert(i += 3, hs);
                                b.insert(i += hs.length(), ';');
                                break;
                            }
                            default: {
                                i ++;
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            next.escape(next, b, idx);
        }

        public void unescaped(final Indent next, final StringBuilder b, final int idx) {
            // escape at next level
            next.escape(next, b, idx);
        }
    };

    public void write(final SourceFileWriter writer) throws IOException {
        writer.addIndent();
        writer.writeEscaped("/**");
        writer.pushIndent(CommentIndentation.BLOCK);
        try {
            writer.pushIndent(escIndent);
            try {
                final List<Writable> contents = getContent();
                if (contents != null && ! contents.isEmpty()) {
                    writer.nl();
                    for (Writable content : contents) {
                        content.write(writer);
                    }
                }
                final Map<String, List<DocTagJHtmlComment>> docTags = getDocTags();
                if (docTags != null) {
                    for (Map.Entry<String, List<DocTagJHtmlComment>> entry : docTags.entrySet()) {
                        final List<DocTagJHtmlComment> values = entry.getValue();
                        if (values != null) for (DocTagJHtmlComment value : values) {
                            value.write(writer);
                        }
                    }
                }
            } finally {
                writer.popIndent(escIndent);
            }
        } finally {
            writer.popIndent(CommentIndentation.BLOCK);
        }
        writer.nl();
        writer.addIndent();
        writer.writeEscaped(" */");
        writer.nl();
    }
}
