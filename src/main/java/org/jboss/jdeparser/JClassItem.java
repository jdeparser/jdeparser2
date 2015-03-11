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

import static java.lang.Integer.signum;

import java.util.Comparator;
import java.util.EnumSet;

/**
 * An item contained within a class.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
@Deprecated
public interface JClassItem {

    /**
     * Get the kind of item.
     *
     * @return the item kind
     */
    @Deprecated
    Kind getItemKind();

    /**
     * Get the modifiers of this item.
     *
     * @return the modifiers
     * @see JMod
     */
    @Deprecated
    int getModifiers();

    /**
     * Determine whether this item has all of the modifiers specified by the given bitwise-OR-joined argument.
     *
     * @param mods the modifier(s)
     * @return {@code true} if all of the modifiers are present, {@code false} otherwise
     */
    @Deprecated
    boolean hasAllModifiers(int mods);

    /**
     * Determine whether this item has any of the modifiers specified by the given bitwise-OR-joined argument.
     *
     * @param mods the modifier(s)
     * @return {@code true} if any if the modifiers are present, {@code false} otherwise
     */
    @Deprecated
    boolean hasAnyModifier(int mods);

    /**
     * Get the name of this element, if there is one.
     *
     * @return the name, or {@code null} if there is none
     */
    @Deprecated
    String getName();

    @Deprecated
    Comparator<JClassItem> SMART_NAME_SORT = new Comparator<JClassItem>() {
        private int rankOf(JClassItem item) {
            return item.getItemKind().ordinal();
        }

        public int compare(final JClassItem o1, final JClassItem o2) {
            int r1, r2;
            r1 = rankOf(o1);
            r2 = rankOf(o2);
            if (r1 != r2) return signum(r2 - r1);
            String n1, n2;
            n1 = o1.getName();
            n2 = o2.getName();
            if (o1.getItemKind() == Kind.METHOD) {
                boolean s1 = o1.hasAllModifiers(JMod.STATIC);
                boolean s2 = o2.hasAllModifiers(JMod.STATIC);
                if (s1 != s2) {
                    return s1 ? -1 : 1;
                }
                String p1, p2;
                int v1, v2;
                boolean f1 = true, f2 = true;
                if (n1.startsWith("get")) {
                    v1 = 0;
                    p1 = n1.substring(3);
                } else if (n1.startsWith("is")) {
                    v1 = 0;
                    p1 = n1.substring(2);
                } else if (n1.startsWith("set")) {
                    v1 = 1;
                    p1 = n1.substring(3);
                } else {
                    f1 = false;
                    v1 = 0;
                    p1 = n1;
                }
                if (n2.startsWith("get")) {
                    v2 = 0;
                    p2 = n2.substring(3);
                } else if (n2.startsWith("is")) {
                    v2 = 0;
                    p2 = n2.substring(2);
                } else if (n2.startsWith("set")) {
                    v2 = 2;
                    p2 = n2.substring(3);
                } else {
                    f2 = false;
                    v2 = 0;
                    p2 = n2;
                }
                if (f1 != f2) {
                    return f1 ? -1 : 1;
                }
                int m = p1.compareTo(p2);
                return m != 0 ? m : signum(v2 - v1);
            } else if (o1.getItemKind() == Kind.FIELD) {
                boolean s1 = o1.hasAllModifiers(JMod.STATIC);
                boolean s2 = o2.hasAllModifiers(JMod.STATIC);
                return s1 != s2 ? s1 ? -1 : 1 : n1.compareTo(n2);
            }
            if (n1 != null && n2 != null) {
                return n1.compareTo(n2);
            }
            return 0;
        }
    };

    /**
     * The kind of class item.
     */
    @Deprecated
    enum Kind {
        /**
         * A line comment.  The item will implement {@link JComment}.
         */
        LINE_COMMENT,
        /**
         * A block comment.  The item will implement {@link JComment}.
         */
        BLOCK_COMMENT,
        /**
         * A blank line.
         */
        BLANK_LINE,
        /**
         * An enum constant.  The item will implement {@link JEnumConstant}.
         */
        ENUM_CONSTANT,
        /**
         * A field.  The item will implement {@link JVarDeclaration}.
         */
        FIELD,
        /**
         * An initialization block.  The item will implement {@link JBlock}.  The block may be static; examine
         * the modifiers to make this determination.
         */
        INIT_BLOCK,
        /**
         * A constructor.  The item will implement {@link JMethodDef}.
         */
        CONSTRUCTOR,
        /**
         * A constructor.  The item will implement {@link JMethodDef}.
         */
        METHOD,
        /**
         * A nested enum.  The item will implement {@link JClassDef}.
         */
        ENUM,
        /**
         * A nested annotation interface.  The item will implement {@link JClassDef}.
         */
        ANNOTATION_INTERFACE,
        /**
         * A nested interface.  The item will implement {@link JClassDef}.
         */
        INTERFACE,
        /**
         * A nested class.  The item will implement {@link JClassDef}.
         */
        CLASS,
        ;

        private static final int fullSize = Kind.values().length;

        /**
         * Determine whether the given set is fully populated (or "full"), meaning it contains all possible values.
         *
         * @param set the set
         *
         * @return {@code true} if the set is full, {@code false} otherwise
         */
        public static boolean isFull(final EnumSet<Kind> set) {
            return set != null && set.size() == fullSize;
        }

        /**
         * Determine whether this instance is equal to one of the given instances.
         *
         * @param v1 the first instance
         *
         * @return {@code true} if one of the instances matches this one, {@code false} otherwise
         */
        public boolean in(final Kind v1) {
            return this == v1;
        }

        /**
         * Determine whether this instance is equal to one of the given instances.
         *
         * @param v1 the first instance
         * @param v2 the second instance
         *
         * @return {@code true} if one of the instances matches this one, {@code false} otherwise
         */
        public boolean in(final Kind v1, final Kind v2) {
            return this == v1 || this == v2;
        }

        /**
         * Determine whether this instance is equal to one of the given instances.
         *
         * @param v1 the first instance
         * @param v2 the second instance
         * @param v3 the third instance
         *
         * @return {@code true} if one of the instances matches this one, {@code false} otherwise
         */
        public boolean in(final Kind v1, final Kind v2, final Kind v3) {
            return this == v1 || this == v2 || this == v3;
        }

        /**
         * Determine whether this instance is equal to one of the given instances.
         *
         * @param v1 the first instance
         * @param v2 the second instance
         * @param v3 the third instance
         * @param v4 the fourth instance
         *
         * @return {@code true} if one of the instances matches this one, {@code false} otherwise
         */
        public boolean in(final Kind v1, final Kind v2, final Kind v3, final Kind v4) {
            return this == v1 || this == v2 || this == v3 || this == v4;
        }

        /**
         * Determine whether this instance is equal to one of the given instances.
         *
         * @param values the possible values
         *
         * @return {@code true} if one of the instances matches this one, {@code false} otherwise
         */
        public boolean in(final Kind... values) {
            if (values != null) for (Kind value : values) {
                if (this == value) return true;
            }
            return false;
        }
    }
}
