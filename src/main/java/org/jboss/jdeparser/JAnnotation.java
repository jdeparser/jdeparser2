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
 * An annotation.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JAnnotation {

    /**
     * Set the "value" property of this annotation.  To set an array of values, see {@link JExprs#array(JExpr...)}.
     *
     * @param expr the annotation value
     * @return this annotation
     */
    JAnnotation value(JExpr expr);

    /**
     * Set the "value" property of this annotation to a string.
     *
     * @param literal the annotation value string
     * @return this annotation
     */
    JAnnotation value(String literal);

    /**
     * Set the "value" property of this annotation to a nested annotation of the given type.
     *
     * @param type the annotation type
     * @return the nested annotation
     */
    JAnnotation annotationValue(String type);

    /**
     * Set the "value" property of this annotation to a nested annotation of the given type.
     *
     * @param type the annotation type
     * @return the nested annotation
     */
    JAnnotation annotationValue(JType type);

    /**
     * Set the "value" property of this annotation to a nested annotation of the given type.
     *
     * @param type the annotation type
     * @return the nested annotation
     */
    JAnnotation annotationValue(Class<? extends Annotation> type);

    /**
     * Set the "value" property of this annotation to an array of nested annotations of the given type.
     *
     * @param type the annotation array element type
     * @return the nested annotation
     */
    JAnnotationArray annotationArrayValue(String type);

    /**
     * Set the "value" property of this annotation to an array of nested annotations of the given type.
     *
     * @param type the annotation array element type
     * @return the nested annotation
     */
    JAnnotationArray annotationArrayValue(JType type);

    /**
     * Set the "value" property of this annotation to an array of nested annotations of the given type.
     *
     * @param type the annotation array element type
     * @return the nested annotation
     */
    JAnnotationArray annotationArrayValue(Class<? extends Annotation> type);

    /**
     * Set the named property of this annotation.  To set an array of values, see {@link JExprs#array(JExpr...)}.
     *
     * @param name the annotation property name
     * @param expr the annotation value
     * @return this annotation
     */
    JAnnotation value(String name, JExpr expr);

    /**
     * Set the named property of this annotation to a string.
     *
     * @param name the annotation property name
     * @param literal the annotation value string
     * @return this annotation
     */
    JAnnotation value(String name, String literal);

    /**
     * Set the named property of this annotation to a nested annotation of the given type.
     *
     * @param type the annotation type
     * @return the nested annotation
     */
    JAnnotation annotationValue(String name, String type);

    /**
     * Set the named property of this annotation to a nested annotation of the given type.
     *
     * @param type the annotation type
     * @return the nested annotation
     */
    JAnnotation annotationValue(String name, JType type);

    /**
     * Set the named property of this annotation to a nested annotation of the given type.
     *
     * @param type the annotation type
     * @return the nested annotation
     */
    JAnnotation annotationValue(String name, Class<? extends Annotation> type);

    /**
     * Set the named property of this annotation to an array of nested annotations of the given type.
     *
     * @param type the annotation array element type
     * @return the nested annotation
     */
    JAnnotationArray annotationArrayValue(String name, String type);

    /**
     * Set the named property of this annotation to an array of nested annotations of the given type.
     *
     * @param type the annotation array element type
     * @return the nested annotation
     */
    JAnnotationArray annotationArrayValue(String name, JType type);

    /**
     * Set the named property of this annotation to an array of nested annotations of the given type.
     *
     * @param type the annotation array element type
     * @return the nested annotation
     */
    JAnnotationArray annotationArrayValue(String name, Class<? extends Annotation> type);
}
