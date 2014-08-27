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
import java.util.ArrayList;

import org.jboss.jdeparser.FormatPreferences.Space;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class BasicJAnnotatable extends AbstractJDocCommentable implements JAnnotatable {
    private ArrayList<ImplJAnnotation> annotations;

    private ImplJAnnotation add(ImplJAnnotation item) {
        if (annotations == null) {
            annotations = new ArrayList<>(3);
        }
        annotations.add(item);
        return item;
    }

    public JAnnotation annotate(final String type) {
        return annotate(JTypes.typeNamed(type));
    }

    public JAnnotation annotate(final JType type) {
        return add(new ImplJAnnotation(type));
    }

    public JAnnotation annotate(final Class<? extends Annotation> type) {
        return annotate(JTypes.typeOf(type));
    }

    void writeAnnotations(final SourceFileWriter writer, final Space space) throws IOException {
        if (annotations != null) for (ImplJAnnotation annotation : annotations) {
            annotation.write(writer);
            writer.write(space);
        }
    }
}
