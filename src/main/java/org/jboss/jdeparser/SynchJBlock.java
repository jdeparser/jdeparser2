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
class SynchJBlock extends BasicJBlock {

    private final AbstractJExpr synchExpr;

    SynchJBlock(final BasicJBlock parent, final AbstractJExpr synchExpr) {
        super(parent, Braces.REQUIRED);
        this.synchExpr = synchExpr;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writer.write($KW.SYNCHRONIZED);
        writer.write(FormatPreferences.Space.BEFORE_PAREN_SYNCHRONIZED);
        writer.write($PUNCT.PAREN.OPEN);
        writer.write(synchExpr);
        writer.write($PUNCT.PAREN.CLOSE);
        super.write(writer, FormatPreferences.Space.BEFORE_BRACE_SYNCHRONIZE);
    }
}
