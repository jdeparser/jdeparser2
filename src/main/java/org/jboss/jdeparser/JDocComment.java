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
 * A JavaDoc comment.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JDocComment extends JHtmlComment {

    /** {@inheritDoc} */
    @Override
    JDocComment text(String text);

    /** {@inheritDoc} */
    @Override
    JDocComment block();

    /** {@inheritDoc} */
    @Override
    JDocComment inlineDocTag(String tag, String body);

    /** {@inheritDoc} */
    @Override
    JComment inlineDocTag(String tag);

    /** {@inheritDoc} */
    @Override
    JDocComment sp();

    /** {@inheritDoc} */
    @Override
    JDocComment nl();

    /** {@inheritDoc} */
    @Override
    JDocComment p();

    /** {@inheritDoc} */
    @Override
    JDocComment br();

    /** {@inheritDoc} */
    @Override
    JDocComment typeName(JType type);

    /** {@inheritDoc} */
    @Override
    JDocComment docRoot();

    /** {@inheritDoc} */
    @Override
    JDocComment value(JType type, String fieldName);

    /**
     * Add a block tag.
     *
     * @param tag the tag name
     * @return the block tag comment block
     */
    JHtmlComment docTag(String tag);

    /**
     * Add a block tag.
     *
     * @param tag the tag name
     * @param firstWord the tag body first word
     * @return the block tag comment block
     */
    JHtmlComment docTag(String tag, String firstWord);

    /**
     * Add a {@code @return} tag.
     *
     * @return the tag body
     */
    JHtmlComment _return();

    /**
     * Add a {@code @throws} tag.
     *
     * @param exceptionType the type of exception
     * @return the tag body
     */
    JHtmlComment _throws(JType exceptionType);

    /**
     * Add a {@code @param} tag.
     *
     * @param name the parameter name
     * @return the tag body
     */
    JHtmlComment param(String name);
}
