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
class WildcardJType extends AbstractJType {

    private final AbstractJType targetType;
    private final boolean extendsNotSuper;

    public WildcardJType(final AbstractJType targetType, final boolean extendsNotSuper) {
        this.targetType = targetType;
        this.extendsNotSuper = extendsNotSuper;
    }

    boolean equals(final AbstractJType other) {
        return other instanceof WildcardJType && equals((WildcardJType) other);
    }

    private boolean equals(final WildcardJType other) {
        return extendsNotSuper == other.extendsNotSuper && targetType.equals(other.targetType);
    }

    public int hashCode() {
        return targetType.hashCode() ^ (extendsNotSuper ? 0 : 1);
    }

    public String simpleName() {
        return targetType.simpleName();
    }

    public JType array() {
        throw new UnsupportedOperationException("array of wildcard type not allowed");
    }

    public JType wildcardExtends() {
        if (extendsNotSuper) return this;
        throw new UnsupportedOperationException("wildcard extends of wildcard super not allowed");
    }

    public JType wildcardSuper() {
        if (! extendsNotSuper) return this;
        throw new UnsupportedOperationException("wildcard super of wildcard extends not allowed");
    }

    void writeDirect(final SourceFileWriter sourceFileWriter) throws IOException {
        sourceFileWriter.write($PUNCT.Q);
        final AbstractJType targetType = getTargetType();
        if (! targetType.equals(JType.OBJECT)) {
            sourceFileWriter.sp();
            sourceFileWriter.write(extendsNotSuper ? $KW.EXTENDS : $KW.SUPER);
            sourceFileWriter.write(targetType);
        }
    }

    public String toString() {
        if (extendsNotSuper && targetType.equals(JType.OBJECT)) {
            return "?";
        } else {
            return "? " + (extendsNotSuper ? "extends " : "super ") + targetType;
        }
    }

    AbstractJType getTargetType() {
        return targetType;
    }

    boolean isExtendsNotSuper() {
        return extendsNotSuper;
    }
}
