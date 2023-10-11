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

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
abstract class AbstractJDocCommentable extends BasicJCommentable implements JDocCommentable {
    private ImplJDocComment docComment;

    @Override
    public JDocComment docComment() {
        if (docComment == null) {
            docComment = new ImplJDocComment();
        }
        return docComment;
    }

    @Override
    public JComment deprecated() {
        return docComment().docTag("deprecated");
    }

    void writeDocComments(SourceFileWriter writer) throws IOException {
        final ImplJDocComment docComment = this.docComment;
        if (docComment != null) {
            docComment.write(writer);
        }
    }
}
