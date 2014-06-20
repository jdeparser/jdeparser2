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

import java.util.Arrays;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class EnumIntMap<E extends Enum<E>> {
    private final Class<E> type;
    private final E[] keys;
    private final int[] values;

    private static final ClassValue<Object> cvo = new ClassValue<Object>() {
        protected Object computeValue(final Class<?> type) {
            return type.getEnumConstants();
        }
    };

    @SuppressWarnings("unchecked")
    EnumIntMap(Class<E> type, int defaultVal) {
        this.type = type;
        keys = (E[]) cvo.get(type);
        values = new int[keys.length];
        Arrays.fill(values, defaultVal);
    }

    EnumIntMap(EnumIntMap<E> orig) {
        this.type = orig.type;
        keys = orig.keys;
        values = orig.values.clone();
    }

    public int get(E key) {
        return values[type.cast(key).ordinal()];
    }

    public int put(E key, int val) {
        final int idx = type.cast(key).ordinal();
        try {
            return values[idx];
        } finally {
            values[idx] = val;
        }
    }

    public static <E extends Enum<E>> EnumIntMap<E> of(final Class<E> enumClass) {
        return new EnumIntMap<>(enumClass, 0);
    }
}
