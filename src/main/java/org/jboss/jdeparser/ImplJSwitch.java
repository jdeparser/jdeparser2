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
import java.util.ArrayList;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJSwitch extends BasicJCommentable implements JSwitch, BlockContent {

    private final BasicJBlock parent;
    private final JExpr expr;

    private final ArrayList<CaseJBlock> cases = new ArrayList<>();
    private DefaultJBlock _default;

    ImplJSwitch(final BasicJBlock parent, final JExpr expr) {
        this.parent = parent;
        this.expr = expr;
    }

    private <T extends CaseJBlock> T add(T item) {
        cases.add(item);
        return item;
    }

    public JBlock _case(final JExpr expr) {
        return add(new CaseJBlock(this, expr));
    }

    public JBlock _case(final String constName) {
        return _case(JExprs.name(constName));
    }

    public JBlock _default() {
        if (_default == null) {
            _default = new DefaultJBlock(this);
        }
        return _default;
    }

    JExpr getExpression() {
        return expr;
    }

    BasicJBlock getParent() {
        return parent;
    }

    ArrayList<CaseJBlock> getCases() {
        return cases;
    }

    DefaultJBlock getDefault() {
        return _default;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writeComments(writer);
        writer.write($KW.SWITCH);
        writer.write(FormatPreferences.Space.BEFORE_PAREN_SWITCH);
        writer.write($PUNCT.PAREN.OPEN);
        writer.write(FormatPreferences.Space.WITHIN_PAREN_SWITCH);
        writer.write(expr);
        writer.write($PUNCT.PAREN.CLOSE);
        writer.write(FormatPreferences.Space.BEFORE_BRACE_SWITCH);
        writer.write($PUNCT.BRACE.OPEN);
        writer.pushIndent(FormatPreferences.Indentation.LINE);
        try {
            writer.write(FormatPreferences.Space.WITHIN_BRACES_CODE);
            for (CaseJBlock _case : cases) {
                _case.write(writer);
            }
            if (_default != null) {
                _default.write(writer);
            }
        } finally {
            writer.popIndent(FormatPreferences.Indentation.LINE);
        }
        writer.write(FormatPreferences.Space.WITHIN_BRACES_CODE);
        writer.write($PUNCT.BRACE.CLOSE);
    }
}
