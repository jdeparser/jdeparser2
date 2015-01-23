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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A layout of class members and content.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class JLayout {

    private final List<JSectionImpl> sections;
    private final JSectionImpl defaultSection;

    public JLayout() {
        sections = new ArrayList<>();
        defaultSection = new JSectionImpl(MemberMatcher.ALL);
    }

    /**
     * Add a section to this layout.  Sections are rendered in order.  The section will include all members which match
     * the given matcher.
     *
     * @param matcher the matcher for the section
     * @return the new section
     */
    public JSection addSection(MemberMatcher matcher) {
        final JSectionImpl section = new JSectionImpl(matcher);
        sections.add(section);
        return section;
    }

    /**
     * Get the default section.  This section is where all members are added which do not match anything.
     *
     * @return the section
     */
    public JSection getDefaultSection() {
        return defaultSection;
    }

    @SuppressWarnings("unchecked")
    void write(List<ClassContent> content, SourceFileWriter writer) throws IOException {
        for (JSectionImpl section : sections) {
            for (JClassItem item : section.apply((List<JClassItem>) (List) content)) {
                ((Writable) item).write(writer);
            }
        }
    }

    static final JLayout DEFAULT = new JLayout();

    public interface MemberMatcher {

        /**
         * Determine whether this matcher matches an item.
         *
         * @param item the item to match
         * @return {@code true} if the item is matched, {@code false} otherwise
         */
        boolean matches(Object item);

        MemberMatcher ALL = new MemberMatcher() {
            public boolean matches(final Object item) {
                return true;
            }
        };

        MemberMatcher NONE = new MemberMatcher() {
            public boolean matches(final Object item) {
                return false;
            }
        };
    }
}
