/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");a
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
abstract class AbstractJDocComment extends AbstractJHtmlComment implements JDocComment {

    private Map<String, List<DocTagJHtmlComment>> docTags;

    @Override
    public JDocComment block() {
        return (JDocComment) super.block();
    }

    @Override
    public JDocComment sp() {
        super.sp();
        return this;
    }

    @Override
    public JDocComment nl() {
        super.nl();
        return this;
    }

    @Override
    public JDocComment typeName(final JType type) {
        super.typeName(type);
        return this;
    }

    @Override
    public JDocComment text(final String text) {
        super.text(text);
        return this;
    }

    @Override
    public JDocComment inlineDocTag(final String tag, final String body) {
        super.inlineDocTag(tag, body);
        return this;
    }

    @Override
    public JDocComment docRoot() {
        super.docRoot();
        return this;
    }

    @Override
    public JDocComment p() {
        super.p();
        return this;
    }

    @Override
    public JDocComment br() {
        super.br();
        return this;
    }

    @Override
    public JDocComment value(final JType type, final String fieldName) {
        super.value(type, fieldName);
        return this;
    }

    @Override
    public JHtmlComment docTag(final String tag) {
        Map<String, List<DocTagJHtmlComment>> docTags = this.docTags;
        if (docTags == null) {
            docTags = this.docTags = new LinkedHashMap<>();
        }
        List<DocTagJHtmlComment> tagList = docTags.get(tag);
        if (tagList == null) {
            docTags.put(tag, tagList = new ArrayList<DocTagJHtmlComment>());
        }
        DocTagJHtmlComment tagComment = new DocTagJHtmlComment(tag);
        tagList.add(tagComment);
        return tagComment;
    }

    @Override
    public JHtmlComment docTag(final String tag, final String firstWord) {
        JHtmlComment docTag = docTag(tag);
        docTag.text(firstWord).sp();
        return docTag;
    }

    @Override
    public JHtmlComment _return() {
        return docTag("return");
    }

    @Override
    public JHtmlComment _throws(final JType exceptionType) {
        return docTag("throws");
    }

    @Override
    public JHtmlComment param(final String name) {
        return docTag("param", name);
    }

    Map<String, List<DocTagJHtmlComment>> getDocTags() {
        return docTags;
    }
}
