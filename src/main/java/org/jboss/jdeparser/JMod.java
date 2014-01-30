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

import static java.lang.Integer.bitCount;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class JMod {
    public static final int ABSTRACT    = 1 << 0;
    public static final int FINAL       = 1 << 1;
    public static final int NATIVE      = 1 << 2;
    public static final int PRIVATE     = 1 << 3;
    public static final int PROTECTED   = 1 << 4;
    public static final int PUBLIC      = 1 << 5;
    public static final int STATIC      = 1 << 6;
    public static final int STRICTFP    = 1 << 7;
    public static final int SYNCHRONIZED= 1 << 8;
    public static final int TRANSIENT   = 1 << 9;
    public static final int VOLATILE    = 1 << 10;

    static final int PRIVATE_BITS       = (1 << 11) - 1;

    static final int INNER              = 1 << 30;

    public static boolean oneIsSet(int set, int test) {
        return bitCount(set & test) == 1;
    }

    public static boolean allAreSet(int set, int test) {
        return (set & test) == test;
    }

    public static boolean allAreClear(int set, int test) {
        return (set & test) == 0;
    }

    public static boolean anyAreSet(int set, int test) {
        return (set & test) != 0;
    }

    public static boolean anyAreClear(int set, int test) {
        return (set & test) != test;
    }
}
