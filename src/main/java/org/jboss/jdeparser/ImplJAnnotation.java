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
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.jboss.jdeparser.FormatPreferences.Space;
import static org.jboss.jdeparser.Tokens.$PUNCT;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ImplJAnnotation implements JAnnotation, Writable {

    private final JType type;
    private LinkedHashMap<String, Writable> properties;

    ImplJAnnotation(final JType type) {
        this.type = type;
    }

    public JAnnotation value(final JExpr expr) {
        return value("value", expr);
    }

    public JAnnotation value(final String literal) {
        return value(JExprs.str(literal));
    }

    public JAnnotation annotationValue(final String type) {
        return annotationValue(JTypes.typeNamed(type));
    }

    public JAnnotation annotationValue(final JType type) {
        return annotationValue("value", type);
    }

    public JAnnotation annotationValue(final Class<? extends Annotation> type) {
        return annotationValue(JTypes.typeOf(type));
    }

    public JAnnotationArray annotationArrayValue(final String type) {
        return annotationArrayValue(JTypes.typeNamed(type));
    }

    public JAnnotationArray annotationArrayValue(final JType type) {
        return annotationArrayValue("value", type);
    }

    public JAnnotationArray annotationArrayValue(final Class<? extends Annotation> type) {
        return annotationArrayValue(JTypes.typeOf(type));
    }

    public JAnnotation value(final String name, final String literal) {
        return value(name, JExprs.str(literal));
    }

    public JAnnotation value(final String name, final JExpr expr) {
        LinkedHashMap<String, Writable> properties = this.properties;
        if (properties == null) {
            properties = this.properties = new LinkedHashMap<>();
        }
        properties.put(name, (AbstractJExpr) expr);
        return this;
    }

    public JAnnotation annotationValue(final String name, final String type) {
        return annotationValue(name, JTypes.typeNamed(type));
    }

    public JAnnotation annotationValue(final String name, final JType type) {
        LinkedHashMap<String, Writable> properties = this.properties;
        if (properties == null) {
            properties = this.properties = new LinkedHashMap<>();
        }
        final ImplJAnnotation annotation = new ImplJAnnotation(type);
        properties.put(name, annotation);
        return annotation;
    }

    public JAnnotation annotationValue(final String name, final Class<? extends Annotation> type) {
        return annotationValue(name, JTypes.typeOf(type));
    }

    public JAnnotationArray annotationArrayValue(final String name, final String type) {
        return annotationArrayValue(name, JTypes.typeNamed(type));
    }

    public JAnnotationArray annotationArrayValue(final String name, final JType type) {
        LinkedHashMap<String, Writable> properties = this.properties;
        if (properties == null) {
            properties = this.properties = new LinkedHashMap<>();
        }
        final ImplJAnnotationArray array = new ImplJAnnotationArray(type);
        properties.put(name, array);
        return array;
    }

    public JAnnotationArray annotationArrayValue(final String name, final Class<? extends Annotation> type) {
        return annotationArrayValue(name, JTypes.typeOf(type));
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writer.write($PUNCT.AT);
        writer.write(type);
        final LinkedHashMap<String, Writable> properties = this.properties;
        if (properties != null && ! properties.isEmpty()) {
            writer.write($PUNCT.PAREN.OPEN);
            writer.pushIndent(FormatPreferences.Indentation.LINE);
            try {
                writer.write(Space.WITHIN_PAREN_ANNOTATION);
                if (properties.size() == 1 && properties.containsKey("value")) {
                    properties.get("value").write(writer);
                } else {
                    Iterator<Map.Entry<String, Writable>> iterator = properties.entrySet().iterator();
                    while (iterator.hasNext()) {
                        final Map.Entry<String, Writable> entry = iterator.next();
                        writer.writeEscapedWord(entry.getKey());
                        writer.write(Space.AROUND_ASSIGN);
                        writer.write($PUNCT.BINOP.ASSIGN);
                        writer.write(Space.AROUND_ASSIGN);
                        entry.getValue().write(writer);
                        if (iterator.hasNext()) {
                            writer.write(Space.BEFORE_COMMA);
                            writer.write($PUNCT.COMMA);
                            writer.write(Space.AFTER_COMMA);
                        }
                    }
                }
            } finally {
                writer.popIndent(FormatPreferences.Indentation.LINE);
            }
            writer.write(Space.WITHIN_PAREN_ANNOTATION);
            writer.write($PUNCT.PAREN.CLOSE);
        }
    }
}
