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

    public JHtmlComment block() {
        return this.add(new NestedHtmlCommentContent());
    }

    public JHtmlComment sp() {
        super.sp();
        return this;
    }

    public JHtmlComment nl() {
        super.nl();
        return this;
    }

    public JHtmlComment typeName(final JType type) {
        super.typeName(type);
        return this;
    }

    public JHtmlComment text(final String text) {
        super.text(text);
        return this;
    }

    public JHtmlComment inlineDocTag(final String tag, final String body) {
        super.inlineDocTag(tag, body);
        return this;
    }

    public JHtmlComment docRoot() {
        super.docRoot();
        return this;
    }

    public JHtmlComment p() {
        nl();
        htmlTag("p", false);
        nl();
        return this;
    }

    public JHtmlComment br() {
        htmlTag("br", false);
        nl();
        return this;
    }

    public JHtmlComment value(final JType type, final String fieldName) {
        inlineDocTag("value").typeName(type).text("#").text(fieldName);
        return this;
    }

    public JHtmlTag htmlLink(final String url) {
        return htmlTag("a", false).attribute("href", url);
    }

    public JHtmlTag htmlTag(final String tag, final boolean newLine) {
        boolean writeClose;
        switch (tag.toLowerCase(Locale.US)) {
            // content forbidden
            case "area":
            case "base":
            case "basefont":
            case "br":
            case "col":
            case "frame":
            case "hr":
            case "img":
            case "input":
            case "isindex":
            case "link":
            case "meta":
            case "param":

            // end tag optional
            case "body":
            case "colgroup":
            case "dd":
            case "dt":
            case "head":
            case "html":
            case "li":
            case "option":
            case "p":
            case "tbody":
            case "td":
            case "textarea":
            case "tfoot":
            case "th":
            case "thead":
            case "tr": {
                writeClose = false;
                break;
            }
            default: {
                writeClose = true;
                break;
            }
        }
        final ImplJHtmlTag htmlTag = new ImplJHtmlTag(tag, newLine, writeClose);
        return add(htmlTag);
    }

    public JComment preformattedCode() {
        JComment main = htmlTag("pre", false).code().nl();
        JComment block = main.block();
        main.nl();
        return block;
    }
}
