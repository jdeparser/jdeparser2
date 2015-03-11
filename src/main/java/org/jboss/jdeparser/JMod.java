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
import static org.jboss.jdeparser.Tokens.*;

import java.io.IOException;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

/**
 * The set of modifiers for types and members.
 */
public final class JMod {

    /**
     * The {@code abstract} modifier.
     */
    public static final int ABSTRACT    = 1 << 0;
    /**
     * The {@code final} modifier.
     */
    public static final int FINAL       = 1 << 1;
    /**
     * The {@code native} modifier.
     */
    public static final int NATIVE      = 1 << 2;
    /**
     * The {@code private} modifier.
     */
    public static final int PRIVATE     = 1 << 3;
    /**
     * The {@code protected} modifier.
     */
    public static final int PROTECTED   = 1 << 4;
    /**
     * The {@code public} modifier.
     */
    public static final int PUBLIC      = 1 << 5;
    /**
     * The {@code static} modifier.
     */
    public static final int STATIC      = 1 << 6;
    /**
     * The {@code strictfp} modifier.
     */
    public static final int STRICTFP    = 1 << 7;
    /**
     * The {@code synchronized} modifier.
     */
    public static final int SYNCHRONIZED= 1 << 8;
    /**
     * The {@code transient} modifier.
     */
    public static final int TRANSIENT   = 1 << 9;
    /**
     * The {@code volatile} modifier.
     */
    public static final int VOLATILE    = 1 << 10;
    /**
     * The {@code default} modifier found in Java 8 and later.
     */
    public static final int DEFAULT     = 1 << 11;

    static final int PRIVATE_BITS       = ~((1 << 12) - 1);

    static final int INNER              = 1 << 30;
    static final int VARARGS            = 1 << 31;

    /**
     * Test to see if exactly one modifier of the given set is present in the test value.
     *
     * @param set the set of modifiers to test against
     * @param test the modifier set to test
     * @return {@code true} if exactly one of {@code set} is present in {@code test}, {@code false} otherwise
     */
    public static boolean oneIsSet(int set, int test) {
        return bitCount(set & test) == 1;
    }

    /**
     * Test to see if all modifiers in the given set are present in the test value.
     *
     * @param set the set of modifiers to test against
     * @param test the modifier set to test
     * @return {@code true} if all the modifiers in {@code set} are present in {@code test}, {@code false} otherwise
     */
    public static boolean allAreSet(int set, int test) {
        return (set & test) == test;
    }

    /**
     * Test to see if all modifiers in the given set are absent in the test value.
     *
     * @param set the set of modifiers to test against
     * @param test the modifier set to test
     * @return {@code true} if all the modifiers in {@code set} are absent in {@code test}, {@code false} otherwise
     */
    public static boolean allAreClear(int set, int test) {
        return (set & test) == 0;
    }

    /**
     * Test to see if any of the modifiers in the given set are present in the test value.
     *
     * @param set the set of modifiers to test against
     * @param test the modifier set to test
     * @return {@code true} if any of the modifiers in {@code set} are present in {@code test}, {@code false} otherwise
     */
    public static boolean anyAreSet(int set, int test) {
        return (set & test) != 0;
    }

    /**
     * Test to see if any of the modifiers in the given set are absent in the test value.
     *
     * @param set the set of modifiers to test against
     * @param test the modifier set to test
     * @return {@code true} if any of the modifiers in {@code set} are absent in {@code test}, {@code false} otherwise
     */
    public static boolean anyAreClear(int set, int test) {
        return (set & test) != test;
    }

    /**
     * Returns an integer which results in the appropriate modifier based on the element.
     *
     * @param element the element to check the modifiers on
     *
     * @return an integer representing the modifiers
     */
    public static int of(final Element element) {
        int result = 0;
        for (Modifier modifier : element.getModifiers()) {
            switch (modifier.name()) {
                case "ABSTRACT": {
                    result = result | ABSTRACT;
                    break;
                }
                case "FINAL": {
                    result = result | FINAL;
                    break;
                }
                case "NATIVE": {
                    result = result | NATIVE;
                    break;
                }
                case "PRIVATE": {
                    result = result | PRIVATE;
                    break;
                }
                case "PROTECTED": {
                    result = result | PROTECTED;
                    break;
                }
                case "PUBLIC": {
                    result = result | PUBLIC;
                    break;
                }
                case "STATIC": {
                    result = result | STATIC;
                    break;
                }
                case "STRICTFP": {
                    result = result | STRICTFP;
                    break;
                }
                case "SYNCHRONIZED": {
                    result = result | SYNCHRONIZED;
                    break;
                }
                case "TRANSIENT": {
                    result = result | TRANSIENT;
                    break;
                }
                case "VOLATILE": {
                    result = result | VOLATILE;
                    break;
                }
                case "DEFAULT": {
                    result = result | DEFAULT;
                    break;
                }
            }
        }
        if (element instanceof ExecutableElement && ((ExecutableElement) element).isVarArgs()) {
            result = result | VARARGS;
        }
        return result;
    }

    static void write(final SourceFileWriter writer, final int mods) throws IOException {
        if (allAreSet(mods, PUBLIC)) {
            writer.write($KW.PUBLIC);
        }
        if (allAreSet(mods, PROTECTED)) {
            writer.write($KW.PROTECTED);
        }
        if (allAreSet(mods, PRIVATE)) {
            writer.write($KW.PRIVATE);
        }
        if (allAreSet(mods, ABSTRACT)) {
            writer.write($KW.ABSTRACT);
        }
        if (allAreSet(mods, STATIC)) {
            writer.write($KW.STATIC);
        }
        if (allAreSet(mods, FINAL)) {
            writer.write($KW.FINAL);
        }
        if (allAreSet(mods, TRANSIENT)) {
            writer.write($KW.TRANSIENT);
        }
        if (allAreSet(mods, VOLATILE)) {
            writer.write($KW.VOLATILE);
        }
        if (allAreSet(mods, SYNCHRONIZED)) {
            writer.write($KW.SYNCHRONIZED);
        }
        if (allAreSet(mods, NATIVE)) {
            writer.write($KW.NATIVE);
        }
        if (allAreSet(mods, STRICTFP)) {
            writer.write($KW.STRICTFP);
        }
        if (allAreSet(mods, DEFAULT)) {
            writer.write($KW.DEFAULT);
        }
    }
}
