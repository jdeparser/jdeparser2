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
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JHtmlComment extends JComment {

    JHtmlComment block();

    JHtmlComment text(String text);

    JHtmlComment inlineDocTag(String tag, String body);

    JHtmlComment sp();

    JHtmlComment nl();

    JHtmlComment typeName(JType type);

    JHtmlComment docRoot();

    JHtmlComment p();

    JHtmlComment br();

    JHtmlComment value(JType type, String fieldName);

    JHtmlTag htmlLink(String url);

    JHtmlTag htmlTag(String tag, boolean newLine);

    /**
     * Add an inline {@code @code} tag within a {@code <pre></pre>} block.
     *
     * @return the preformatted code block content
     */
    JComment preformattedCode();
}
