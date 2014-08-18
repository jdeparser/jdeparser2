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
 * A program element which can be annotated.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JAnnotatable {

    /**
     * Add an annotation.
     *
     * @param type the type of the annotation to add
     * @return the new annotation
     */
    JAnnotation annotate(String type);

    /**
     * Add an annotation.
     *
     * @param type the type of the annotation to add
     * @return the new annotation
     */
    JAnnotation annotate(JType type);

    /**
     * Add an annotation.
     *
     * @param type the type of the annotation to add
     * @return the new annotation
     */
    JAnnotation annotate(Class<? extends Annotation> type);
}
