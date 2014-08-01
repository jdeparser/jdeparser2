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

import static org.jboss.jdeparser.Tokens.*;

import java.io.IOException;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class DoJBlock extends ConditionJBlock {

    DoJBlock(final BasicJBlock parent, final JExpr cond) {
        super(parent, Braces.REQUIRED, cond);
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writeComments(writer);
        writer.write($KW.DO);
        super.write(writer, FormatPreferences.Space.BEFORE_BRACE_DO);
        writer.write(FormatPreferences.Space.BEFORE_KEYWORD_WHILE);
        writer.write($KW.WHILE);
        writer.write(FormatPreferences.Space.BEFORE_PAREN_WHILE);
        writer.write($PUNCT.PAREN.OPEN);
        writer.write(FormatPreferences.Space.WITHIN_PAREN_WHILE);
        writer.write(getCondition());
        writer.write(FormatPreferences.Space.WITHIN_PAREN_WHILE);
        writer.write($PUNCT.PAREN.CLOSE);
        writer.write($PUNCT.SEMI);
    }
}
