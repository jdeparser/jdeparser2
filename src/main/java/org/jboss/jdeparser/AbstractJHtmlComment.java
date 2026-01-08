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

import java.util.Locale;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
abstract class AbstractJHtmlComment extends AbstractJComment implements JHtmlComment {

    protected <T extends HtmlCommentContent> T add(final T item) {
        addItemDirectly(item);
        return item;
    }

    @Override
    public JHtmlComment block() {
        return this.add(new NestedHtmlCommentContent());
    }

    @Override
    public JHtmlComment sp() {
        super.sp();
        return this;
    }

    @Override
    public JHtmlComment nl() {
        super.nl();
        return this;
    }

    @Override
    public JHtmlComment typeName(final JType type) {
        super.typeName(type);
        return this;
    }

    @Override
    public JHtmlComment text(final String text) {
        super.text(text);
        return this;
    }

    @Override
    public JHtmlComment inlineDocTag(final String tag, final String body) {
        super.inlineDocTag(tag, body);
        return this;
    }

    @Override
    public JHtmlComment docRoot() {
        super.docRoot();
        return this;
    }

    @Override
    public JHtmlComment p() {
        nl();
        htmlTag("p", false);
        nl();
        return this;
    }

    @Override
    public JHtmlComment br() {
        htmlTag("br", false);
        nl();
        return this;
    }

    @Override
    public JHtmlComment value(final JType type, final String fieldName) {
        inlineDocTag("value").typeName(type).text("#").text(fieldName);
        return this;
    }

    @Override
    public JHtmlTag htmlLink(final String url) {
        return htmlTag("a", false).attribute("href", url);
    }

    @Override
    public JHtmlTag htmlTag(final String tag, final boolean newLine) {
        boolean writeClose;
        writeClose = switch (tag.toLowerCase(Locale.US)) {
            case /* content forbidden */
                 "area", "base", "basefont", "br", "col", "frame",
                 "hr", "img", "input", "isindex", "link", "meta", "param",
                 /* end tag optional */
                 "body", "colgroup", "dd", "dt", "head", "html", "li",
                 "option", "p", "tbody", "td", "textarea", "tfoot", "th",
                 "thead", "tr" -> false;
            default -> true;
        };
        final ImplJHtmlTag htmlTag = new ImplJHtmlTag(tag, newLine, writeClose);
        return add(htmlTag);
    }

    @Override
    public JComment preformattedCode() {
        JComment main = htmlTag("pre", false).code().nl();
        JComment block = main.block();
        main.nl();
        return block;
    }
}
