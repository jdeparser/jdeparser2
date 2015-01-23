/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015 Red Hat, Inc., and individual contributors
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

import java.util.Comparator;

/**
 * A section of a {@linkplain JLayout layout}.  Comments and blank lines can be added before, between, and/or after
 * each member that is matched for this section.  Items will be rendered in the order they were added.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JSection {

    /**
     * The flag indicating that the item should be located before the first member.
     */
    int BEFORE = 1 << 0;
    /**
     * The flag indicating that the item should be located between multiple members.
     */
    int BETWEEN = 1 << 1;
    /**
     * The flag indicating that the item should be located after the last member.
     */
    int AFTER = 1 << 2;
    /**
     * The flag indicating that the item should be printed when the section is empty.
     */
    int EMPTY = 1 << 3;

    /**
     * Add a line comment to this section.  The location must be a bitwise-OR merging of one or more of
     * {@link #BEFORE}, {@link #BETWEEN}, and/or {@link #AFTER}.
     *
     * @param where the location(s) of the comment
     * @return the comment body
     */
    JComment addLineComment(int where);

    /**
     * Add a block comment to this section.  The location must be a bitwise-OR merging of one or more of
     * {@link #BEFORE}, {@link #BETWEEN}, and/or {@link #AFTER}.
     *
     * @param where the location(s) of the comment
     * @return the comment body
     */
    JComment addBlockComment(int where);

    /**
     * Add a blank line to this section.  The location must be a bitwise-OR merging of one or more of
     * {@link #BEFORE}, {@link #BETWEEN}, and/or {@link #AFTER}.
     *
     * @param where the location(s) of the blank line
     * @return this section
     */
    JSection addBlankLine(int where);

    /**
     * Set the sort for this section.  If set to {@code null} (the default), then no sorting will take place.
     *
     * @param comparator the comparator to sort with
     */
    void setSort(Comparator<JClassItem> comparator);
}
