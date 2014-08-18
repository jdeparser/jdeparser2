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
import java.util.ArrayList;
import java.util.Iterator;

import static org.jboss.jdeparser.FormatPreferences.Indentation;
import static org.jboss.jdeparser.FormatPreferences.Space;
import static org.jboss.jdeparser.Tokens.$PUNCT;

class ImplJAnnotationArray implements JAnnotationArray, Writable {

    private final JType type;
    private ArrayList<ImplJAnnotation> list;

    ImplJAnnotationArray(final JType type) {
        this.type = type;
    }

    public JAnnotation add() {
        ArrayList<ImplJAnnotation> list = this.list;
        if (list == null) {
            list = this.list = new ArrayList<>();
        }
        final ImplJAnnotation annotation = new ImplJAnnotation(type);
        list.add(annotation);
        return annotation;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writer.write($PUNCT.BRACE.OPEN);
        if (size() > 0) {
            writer.pushIndent(Indentation.LINE);
            try {
                writer.write(Space.WITHIN_BRACES_ARRAY_INIT);
                writeBare(writer);
            } finally {
                writer.popIndent(Indentation.LINE);
            }
            writer.write(Space.WITHIN_BRACES_ARRAY_INIT);
        }
        writer.write($PUNCT.BRACE.CLOSE);
    }

    int size() {
        final ArrayList<ImplJAnnotation> list = this.list;
        return list == null ? 0 : list.size();
    }

    void writeBare(final SourceFileWriter writer) throws IOException {
        final ArrayList<ImplJAnnotation> list = this.list;
        if (list != null) {
            final Iterator<ImplJAnnotation> iterator = list.iterator();
            if (iterator.hasNext()) {
                iterator.next().write(writer);
                while (iterator.hasNext()) {
                    writer.write(Space.BEFORE_COMMA);
                    writer.write($PUNCT.COMMA);
                    writer.write(Space.AFTER_COMMA);
                    iterator.next().write(writer);
                }
            }
        }
    }
}
