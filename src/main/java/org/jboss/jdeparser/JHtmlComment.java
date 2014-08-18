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

/**
 * A comment that supports HTML content.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JHtmlComment extends JComment {

    /** {@inheritDoc} */
    JHtmlComment block();

    /** {@inheritDoc} */
    JHtmlComment text(String text);

    /** {@inheritDoc} */
    JHtmlComment inlineDocTag(String tag, String body);

    /** {@inheritDoc} */
    JHtmlComment sp();

    /** {@inheritDoc} */
    JHtmlComment nl();

    /** {@inheritDoc} */
    JHtmlComment typeName(JType type);

    /** {@inheritDoc} */
    JHtmlComment docRoot();

    /**
     * Add a paragraph separator.
     *
     * @return this HTML comment
     */
    JHtmlComment p();

    /**
     * Add a line separator.
     *
     * @return this HTML comment
     */
    JHtmlComment br();

    /**
     * Add a {@code @value} inline tag.
     *
     * @param type the value type
     * @param fieldName the value field name
     * @return this HTML comment
     */
    JHtmlComment value(JType type, String fieldName);

    /**
     * Add an HTML link ({@code &lt;a&gt;} tag).
     *
     * @param url the URL to link to
     * @return the link tag body
     */
    JHtmlTag htmlLink(String url);

    /**
     * Add an HTML tag.  The given tag should be a valid HTML 4 or 5 tag.
     *
     * @param tag the HTML tag
     * @param newLine {@code true} to add a newline after the opening tag and before the closing tag
     * @return the tag body
     */
    JHtmlTag htmlTag(String tag, boolean newLine);

    /**
     * Add an inline {@code @code} tag within a {@code <pre></pre>} block.
     *
     * @return the preformatted code block content
     */
    JComment preformattedCode();
}
