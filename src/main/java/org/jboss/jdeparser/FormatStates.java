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
import java.util.Locale;

/**
* @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
*/
enum FormatStates implements FormatState {
    /**
     * Cursor is at a new line.
     */
    $START(true),
    ;
    private final boolean indentNeeded;

    FormatStates(final boolean indentNeeded) {
        this.indentNeeded = indentNeeded;
    }

    public boolean needsIndentBefore(final SourceFileWriter writer) {
        return indentNeeded;
    }

    public boolean needsSpaceBefore(final SourceFileWriter writer, final $KW next) {
        return false;
    }

    public boolean needsSpaceBefore(final SourceFileWriter writer, final $PUNCT next) {
        return false;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        // n/a
    }

    enum $PUNCT implements FormatState {
        SEMI(';'),

        DOT('.'),

        Q('?'),
        COLON(':'),

        AT('@'),
        COMMA(','),
        ;
        private final char ch;

        $PUNCT(final char ch) {
            this.ch = ch;
        }

        public boolean needsIndentBefore(final SourceFileWriter writer) {
            return true;
        }

        public boolean needsSpaceBefore(final SourceFileWriter writer, final $KW next) {
            // todo derive from prefs
            return false;
        }

        public boolean needsSpaceBefore(final SourceFileWriter writer, final $PUNCT next) {
            return true;
        }

        public void write(final SourceFileWriter writer) throws IOException {
            writer.writeRaw(ch);
            if (this == SEMI) {
                writer.nl();
            }
        }

        public char getChar() {
            return ch;
        }

        enum COMMENT implements FormatState {
            LINE("//"),
            OPEN("/*"),
            CLOSE("*/"),
            ;
            private final String str;

            COMMENT(final String str) {
                this.str = str;
            }

            public boolean needsIndentBefore(final SourceFileWriter writer) {
                return true;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $KW next) {
                return false;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $PUNCT next) {
                return false;
            }

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeIdentifier(str);
            }
        }

        enum UNOP implements FormatState {
            COMP("~"),
            NOT("!"),
            MINUS("-"),
            PP("++"),
            MM("--"),
            ;

            private final String str;

            UNOP(final String str) {
                this.str = str;
            }

            public boolean needsIndentBefore(final SourceFileWriter writer) {
                return true;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $KW next) {
                // todo from prefs
                return true;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $PUNCT next) {
                return true;
            }

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeIdentifier(str);
            }
        }

        enum BINOP implements FormatState {
            PLUS("+"),
            MINUS("-"),
            TIMES("*"),
            DIV("/"),
            MOD("%"),

            BAND("&"),
            BOR("|"),
            BXOR("^"),

            LAND("&&"),
            LOR("||"),

            SHR(">>"),
            LSHR(">>>"),
            SHL("<<"),

            EQ("=="),
            NE("!="),

            LT("<"),
            GT(">"),
            LE("<="),
            GE(">="),

            ASSIGN("="),

            ASSIGN_PLUS("+="),
            ASSIGN_MINUS("-="),
            ASSIGN_TIMES("*="),
            ASSIGN_DIV("/="),
            ASSIGN_MOD("%="),

            ASSIGN_BAND("&="),
            ASSIGN_BOR("|="),
            ASSIGN_BXOR("^="),

            ASSIGN_SHR(">>="),
            ASSIGN_LSHR(">>>="),
            ASSIGN_SHL("<<="),
            ;

            private final String str;

            BINOP(final String str) {
                this.str = str;
            }

            public boolean needsIndentBefore(final SourceFileWriter writer) {
                return true;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $KW next) {
                // todo from prefs
                return true;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $PUNCT next) {
                return true;
            }

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeIdentifier(str);
            }
        }

        enum PAREN implements FormatState {
            OPEN('('),
            CLOSE(')'),
            ;
            private final char ch;

            PAREN(final char ch) {
                this.ch = ch;
            }

            public boolean needsIndentBefore(final SourceFileWriter writer) {
                return true;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $KW next) {
                return this == CLOSE;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $PUNCT next) {
                return false;
            }

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeRaw(ch);
            }
        }

        enum ANGLE implements FormatState {
            OPEN('<'),
            CLOSE('>'),
            ;

            private final char ch;

            ANGLE(final char ch) {
                this.ch = ch;
            }

            public boolean needsIndentBefore(final SourceFileWriter writer) {
                return false;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $KW next) {
                return false;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $PUNCT next) {
                return false;
            }

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeRaw(ch);
            }
        }

        enum BRACE implements FormatState {
            OPEN('{'),
            CLOSE('}'),
            ;

            private final char ch;

            BRACE(final char ch) {
                this.ch = ch;
            }

            public boolean needsIndentBefore(final SourceFileWriter writer) {
                return true;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $KW next) {
                // todo derive from prefs
                return false;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $PUNCT next) {
                return false;
            }

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeRaw(ch);
            }
        }

        enum BRACKET implements FormatState {
            OPEN('['),
            CLOSE(']'),
            ;
            private final char ch;

            BRACKET(final char ch) {
                this.ch = ch;
            }

            public boolean needsIndentBefore(final SourceFileWriter writer) {
                return true;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $KW next) {
                return true;
            }

            public boolean needsSpaceBefore(final SourceFileWriter writer, final $PUNCT next) {
                return false;
            }

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeRaw(ch);
            }
        }
    }

    enum $KW implements FormatState {
        ABSTRACT(),
        ASSERT(),
        BOOLEAN(),
        BREAK(),
        BYTE(),
        CASE(),
        CATCH(),
        CHAR(),
        CLASS(),
        CONST(),
        CONTINUE(),
        DEFAULT(),
        DO(),
        DOUBLE(),
        ELSE(),
        ENUM(),
        EXTENDS(),
        FINAL(),
        FINALLY(),
        FLOAT(),
        FOR(),
        GOTO(),
        IF(),
        IMPLEMENTS(),
        IMPORT(),
        INSTANCEOF(),
        INT(),
        INTERFACE(),
        LONG(),
        NATIVE(),
        NEW(),
        PACKAGE(),
        PRIVATE(),
        PROTECTED(),
        PUBLIC(),
        RETURN(),
        SHORT(),
        STATIC(),
        STRICTFP(),
        SUPER(),
        SWITCH(),
        SYNCHRONIZED(),
        THIS(),
        THROW(),
        THROWS(),
        TRANSIENT(),
        TRY(),
        VOID(),
        VOLATILE(),
        WHILE(),

        FALSE(),
        TRUE(),

        NULL(),
        ;

        private final String kw;

        $KW() {
            this.kw = name().toLowerCase(Locale.US);
        }

        String getKeyword() {
            return kw;
        }

        public boolean needsIndentBefore(final SourceFileWriter writer) {
            return false;
        }

        public boolean needsSpaceBefore(final SourceFileWriter writer, final $KW next) {
            return true;
        }

        public boolean needsSpaceBefore(final SourceFileWriter writer, final $PUNCT next) {
            return false;
        }

        public void write(final SourceFileWriter writer) throws IOException {
            writer.writeIdentifier(kw);
        }

        static FormatState forName(final String keyword) {
            return valueOf(keyword.toUpperCase(Locale.US));
        }
    }
}
