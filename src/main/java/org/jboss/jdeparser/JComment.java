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
 * A source comment or tag body.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JComment {

    /**
     * Add some text to the end of this comment.  No formatting or line breaks are inserted.
     *
     * @param text the text to add
     * @return this comment
     */
    JComment text(String text);

    /**
     * Add a non-trailing space.  If no content follows, the space will be omitted.
     *
     * @return this comment
     */
    JComment sp();

    /**
     * Add a newline.
     *
     * @return this comment
     */
    JComment nl();

    /**
     * Add a type name to the end of this comment.  If the type is imported, it will emit as a simple name, otherwise
     * it will emit as a qualified name.
     *
     * @param type the type name to add
     * @return this comment
     */
    JComment typeName(JType type);

    /**
     * Add a comment sub-block at this location.  The block has no visual representation but allows text to be inserted
     * at the point of the block even after more content was appended after it.
     *
     * @return the comment sub-block
     */
    JComment block();

    /**
     * Add an inline doc tag with simple content.
     *
     * @param tag the tag name (without the leading {@code @} sign)
     * @param body the complete tag body
     * @return this comment
     */
    JComment inlineDocTag(String tag, String body);

    /**
     * Add an inline doc tag.
     *
     * @param tag the tag name (without the leading {@code @} sign)
     * @return the body of the doc tag
     */
    JComment inlineDocTag(String tag);

    /**
     * Add an inline code tag.
     *
     * @return the code tag content
     */
    JComment code();

    /**
     * Add the {@code {&#64;docRoot}} tag at this position.
     *
     * @return this comment
     */
    JComment docRoot();

    /**
     * Add an inline {@code @link} to a type.
     *
     * @param plain {@code true} to render in plain font, {@code false} to render in {@code monospace} font
     * @param targetType the target type to link to
     * @return the body of the link tag
     */
    JComment linkType(boolean plain, JType targetType);

    /**
     * Add an inline {@code @link} to a field of a type.
     *
     * @param plain {@code true} to render in plain font, {@code false} to render in {@code monospace} font
     * @param targetType the target type to link to
     * @param targetField the target field to link to
     * @return the body of the link tag
     */
    JComment linkField(boolean plain, JType targetType, String targetField);

    /**
     * Add an inline {@code @link} to a constructor.
     *
     * @param plain {@code true} to render in plain font, {@code false} to render in {@code monospace} font
     * @param targetType the target type to link to
     * @param targetConstructorArgumentTypes the argument types of the constructor to link to
     * @return the body of the link tag
     */
    JComment linkConstructor(boolean plain, JType targetType, JType... targetConstructorArgumentTypes);

    /**
     * Add an inline {@code @link} to a method.
     *
     * @param plain {@code true} to render in plain font, {@code false} to render in {@code monospace} font
     * @param targetType the target type to link to
     * @param targetMethod the name of the method to link to
     * @param targetMethodArgumentTypes the argument types of the method to link to
     * @return the body of the link tag
     */
    JComment linkMethod(boolean plain, JType targetType, String targetMethod, JType... targetMethodArgumentTypes);

    /**
     * Add an inline {@code @link} to a method.
     *
     * @param plain {@code true} to render in plain font, {@code false} to render in {@code monospace} font
     * @param methodDef the method to link to
     * @return the body of the link tag
     */
    JComment linkMethod(boolean plain, JMethodDef methodDef);
}
