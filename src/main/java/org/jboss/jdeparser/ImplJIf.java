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
class ImplJIf extends ConditionJBlock implements JIf {
    private ElseJBlock _else;

    ImplJIf(final BasicJBlock parent, final JExpr cond) {
        super(parent, Braces.IF_MULTILINE, cond);
    }

    public JBlock _else() {
        // todo: analyze tree to see if we need to force brackets on our main block
        // e.g. if (cond) if (cond2) abc else xyz
        // ---> if (cond) { if (cond2) abc } else xyz
        // ---> if (cond) { if (cond2) abc else xyz }
        if (_else == null) {
            return _else = new ElseJBlock(this);
        }
        throw new IllegalStateException("else block already added");
    }

    public JIf elseIf(final JExpr cond) {
        return _else()._if(cond);
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writeComments(writer);
        writer.write($KW.IF);
        writer.write(FormatPreferences.Space.BEFORE_PAREN_IF);
        writer.write($PUNCT.PAREN.OPEN);
        writer.write(FormatPreferences.Space.WITHIN_PAREN_IF);
        writer.write(getCondition());
        writer.write(FormatPreferences.Space.WITHIN_PAREN_IF);
        writer.write($PUNCT.PAREN.CLOSE);
        if (! hasSingleItemOfType(EmptyJStatement.class)) {
            writer.sp();
        }
        super.write(writer);
        if (_else != null) {
            if (hasSingleItemOfType(ImplJIf.class)) {
                _else.write(writer, null, Braces.REQUIRED);
            } else {
                _else.write(writer);
            }
        }
    }
}
