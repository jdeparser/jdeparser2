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
class ImplJAnnotation implements JAnnotation {

    public JAnnotation value(final JExpr expr) {
        return null;
    }

    public JAnnotation value(final String literal) {
        return value(JExprs.str(literal));
    }

    public JAnnotation annotationValue(final String type) {
        return null;
    }

    public JAnnotation annotationValue(final JType type) {
        return null;
    }

    public JAnnotation annotationValue(final Class<? extends Annotation> type) {
        return null;
    }

    public JAnnotationArray annotationArrayValue(final String type) {
        return null;
    }

    public JAnnotationArray annotationArrayValue(final JType type) {
        return null;
    }

    public JAnnotationArray annotationArrayValue(final Class<? extends Annotation> type) {
        return null;
    }

    public JAnnotation value(final String name, final JExpr expr) {
        return null;
    }

    public JAnnotationArray annotationArrayValue(final String name, final String type) {
        return null;
    }

    public JAnnotationArray annotationArrayValue(final String name, final JType type) {
        return null;
    }

    public JAnnotationArray annotationArrayValue(final String name, final Class<? extends Annotation> type) {
        return null;
    }
}
