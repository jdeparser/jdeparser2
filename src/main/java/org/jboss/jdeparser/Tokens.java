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

import static org.jboss.jdeparser.FormatPreferences.Space;

import java.io.IOException;
import java.util.Locale;

/**
* @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
*/
enum Tokens implements Token {
    /**
     * Cursor is at a new file.
     */
    $START,
    $COMMENT,
    $STRING_LIT,
    $WORD,
    $NUMBER,
    ;

    public void write(final SourceFileWriter writer) throws IOException {
        // n/a
    }

    enum $PUNCT implements Token {
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

        public void write(final SourceFileWriter writer) throws IOException {
            writer.writeEscaped(ch);
        }

        public char getChar() {
            return ch;
        }

        enum COMMENT implements Token {
            LINE("//"),
            OPEN("/*"),
            CLOSE("*/"),
            ;
            private final String str;

            COMMENT(final String str) {
                this.str = str;
            }

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeEscaped(str);
            }
        }

        enum UNOP implements Token {
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

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeEscaped(str);
            }
        }

        enum BINOP implements Token {
            PLUS("+", Space.AROUND_ADDITIVE),
            MINUS("-", Space.AROUND_ADDITIVE),
            TIMES("*", Space.AROUND_MULTIPLICATIVE),
            DIV("/", Space.AROUND_MULTIPLICATIVE),
            MOD("%", Space.AROUND_MULTIPLICATIVE),

            BAND("&", Space.AROUND_BITWISE),
            BOR("|", Space.AROUND_BITWISE),
            BXOR("^", Space.AROUND_BITWISE),

            LAND("&&", Space.AROUND_LOGICAL),
            LOR("||", Space.AROUND_LOGICAL),

            SHR(">>", Space.AROUND_SHIFT),
            LSHR(">>>", Space.AROUND_SHIFT),
            SHL("<<", Space.AROUND_SHIFT),

            EQ("==", Space.AROUND_EQUALITY),
            NE("!=", Space.AROUND_EQUALITY),

            LT("<", Space.AROUND_RANGE),
            GT(">", Space.AROUND_RANGE),
            LE("<=", Space.AROUND_RANGE),
            GE(">=", Space.AROUND_RANGE),

            ASSIGN("=", Space.AROUND_ASSIGN),

            ASSIGN_PLUS("+=", Space.AROUND_ASSIGN),
            ASSIGN_MINUS("-=", Space.AROUND_ASSIGN),
            ASSIGN_TIMES("*=", Space.AROUND_ASSIGN),
            ASSIGN_DIV("/=", Space.AROUND_ASSIGN),
            ASSIGN_MOD("%=", Space.AROUND_ASSIGN),

            ASSIGN_BAND("&=", Space.AROUND_ASSIGN),
            ASSIGN_BOR("|=", Space.AROUND_ASSIGN),
            ASSIGN_BXOR("^=", Space.AROUND_ASSIGN),

            ASSIGN_SHR(">>=", Space.AROUND_ASSIGN),
            ASSIGN_LSHR(">>>=", Space.AROUND_ASSIGN),
            ASSIGN_SHL("<<=", Space.AROUND_ASSIGN),

            DBL_COLON("::", Space.AROUND_METHOD_REF),
            ARROW("->", Space.AROUND_ARROW),
            ;

            private final String str;
            private final Space spacingRule;

            BINOP(final String str, final Space spacingRule) {
                this.str = str;
                this.spacingRule = spacingRule;
            }

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeEscaped(str);
            }

            public Space getSpacingRule() {
                return spacingRule;
            }
        }

        enum PAREN implements Token {
            OPEN('('),
            CLOSE(')'),
            ;
            private final char ch;

            PAREN(final char ch) {
                this.ch = ch;
            }

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeEscaped(ch);
            }
        }

        enum ANGLE implements Token {
            OPEN('<'),
            CLOSE('>'),
            ;

            private final char ch;

            ANGLE(final char ch) {
                this.ch = ch;
            }

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeEscaped(ch);
            }
        }

        enum BRACE implements Token {
            OPEN('{'),
            CLOSE('}'),
            ;

            private final char ch;

            BRACE(final char ch) {
                this.ch = ch;
            }

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeEscaped(ch);
            }
        }

        enum BRACKET implements Token {
            OPEN('['),
            CLOSE(']'),
            ;
            private final char ch;

            BRACKET(final char ch) {
                this.ch = ch;
            }

            public void write(final SourceFileWriter writer) throws IOException {
                writer.writeEscaped(ch);
            }
        }
    }

    enum $KW implements Token {
        AT_INTERFACE("@interface", null, null),

        ABSTRACT(null, null),
        ASSERT(null, null),
        BOOLEAN(null, null),
        BREAK(null, null),
        BYTE(null, null),
        CASE(null, null),
        CATCH(Space.BEFORE_KEYWORD_CATCH, null),
        CHAR(null, null),
        CLASS(null, null),
        CONST(null, null),
        CONTINUE(null, null),
        DEFAULT(null, null),
        DO(null, null),
        DOUBLE(null, null),
        ELSE(Space.BEFORE_KEYWORD_ELSE, null),
        ENUM(null, null),
        EXTENDS(null, null),
        FINAL(null, null),
        FINALLY(Space.BEFORE_KEYWORD_FINALLY, null),
        FLOAT(null, null),
        FOR(null, null),
        GOTO(null, null),
        IF(null, null),
        IMPLEMENTS(null, null),
        IMPORT(null, null),
        INSTANCEOF(null, null),
        INT(null, null),
        INTERFACE(null, null),
        LONG(null, null),
        NATIVE(null, null),
        NEW(null, null),
        PACKAGE(null, null),
        PRIVATE(null, null),
        PROTECTED(null, null),
        PUBLIC(null, null),
        RETURN(null, null),
        SHORT(null, null),
        STATIC(null, null),
        STRICTFP(null, null),
        SUPER(null, null),
        SWITCH(null, null),
        SYNCHRONIZED(null, null),
        THIS(null, null),
        THROW(null, null),
        THROWS(null, null),
        TRANSIENT(null, null),
        TRY(null, null),
        VOID(null, null),
        VOLATILE(null, null),
        WHILE(Space.BEFORE_KEYWORD_WHILE, null),

        FALSE(null, null),
        TRUE(null, null),

        NULL(null, null),
        ;

        private final String kw;
        private final Space before;
        private final Space after;

        $KW(final Space before, final Space after) {
            this.before = before;
            this.after = after;
            this.kw = name().toLowerCase(Locale.US);
        }

        $KW(final String name, final Space before, final Space after) {
            this.before = before;
            this.after = after;
            this.kw = name;
        }

        String getKeyword() {
            return kw;
        }

        public void write(final SourceFileWriter writer) throws IOException {
            final Token writerState = writer.getState();
            if (writerState == $WORD || writerState == $NUMBER || writerState instanceof $KW) {
                writer.sp();
            }
            if (before != null) writer.write(before);
            writer.writeEscapedWord(kw);
            if (after != null) writer.write(after);
        }

        static Token forName(final String keyword) {
            switch (keyword) {
                case "@interface": return AT_INTERFACE;
                default: return valueOf(keyword.toUpperCase(Locale.US));
            }
        }
    }
}
