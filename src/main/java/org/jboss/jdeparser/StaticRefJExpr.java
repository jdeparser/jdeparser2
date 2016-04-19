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
class StaticRefJExpr extends AbstractJAssignableExpr {

    private final AbstractJType type;
    private final String refName;

    StaticRefJExpr(final AbstractJType type, final String refName) {
        super(Prec.MEMBER_ACCESS);
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        }
        if (refName == null) {
            throw new IllegalArgumentException("refName is null");
        }
        this.type = type;
        this.refName = refName;
    }

    AbstractJType getType() {
        return type;
    }

    String getRefName() {
        return refName;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        if (! writer.getClassFile().hasStaticImport(refName, type)) {
            type.writeDirect(writer);
            writer.write($PUNCT.DOT);
        }
        writer.writeEscapedWord(refName);
    }

    void writeForImport(final SourceFileWriter writer) throws IOException {
        writer.processSpacing();
        writer.writeClass(type.qualifiedName());
        writer.write($PUNCT.DOT);
        writer.writeEscapedWord(refName);
    }

    String getName() {
        return refName;
    }
}
