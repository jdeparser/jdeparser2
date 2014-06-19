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

    private final StaticRef staticRef;

    StaticRefJExpr(final AbstractJType type, final String refName) {
        super(Prec.MEMBER_ACCESS);
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        }
        if (refName == null) {
            throw new IllegalArgumentException("refName is null");
        }
        staticRef = new StaticRef(type, refName);
    }

    StaticRef getStaticRef() {
        return staticRef;
    }

    void writeDirect(final SourceFileWriter writer) throws IOException {
        writer.write(staticRef.getType());
        writer.write($PUNCT.DOT);
        writer.writeRaw(staticRef.getRefName());
    }

    static final class StaticRef {
        private final AbstractJType type;
        private final String refName;

        StaticRef(final AbstractJType type, final String refName) {
            this.type = type;
            this.refName = refName;
        }

        public AbstractJType getType() {
            return type;
        }

        public String getRefName() {
            return refName;
        }

        public boolean equals(Object other) {
            return other instanceof StaticRef && equals((StaticRef) other);
        }

        public boolean equals(StaticRef other) {
            return other != null && type.qualifiedName().equals(other.type.qualifiedName()) && refName.equals(other.refName);
        }

        public int hashCode() {
            return type.qualifiedName().hashCode() * 31 + refName.hashCode();
        }
    }
}
