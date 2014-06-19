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

import java.lang.annotation.Annotation;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class SuccessorJVarDeclaration implements JVarDeclaration {

    private final FirstJVarDeclaration first;
    private final String name;
    private final JExpr value;

    SuccessorJVarDeclaration(final FirstJVarDeclaration first, final String name, final JExpr value) {
        this.first = first;
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    JExpr getValue() {
        return value;
    }

    public JComment blockComment() {
        return first.blockComment();
    }

    public JComment lineComment() {
        return first.lineComment();
    }

    public JComment deprecated() {
        return first.deprecated();
    }

    public JDocComment docComment() {
        return first.docComment();
    }

    public JAnnotation annotate(final Class<? extends Annotation> type) {
        return first.annotate(type);
    }

    public JAnnotation annotate(final JType type) {
        return first.annotate(type);
    }

    public JAnnotation annotate(final String type) {
        return first.annotate(type);
    }

    public JVarDeclaration add(final String name) {
        return first.add(name);
    }

    public JVarDeclaration add(final String name, final JExpr init) {
        return first.add(name, init);
    }

    public JType type() {
        return first.type();
    }
}
