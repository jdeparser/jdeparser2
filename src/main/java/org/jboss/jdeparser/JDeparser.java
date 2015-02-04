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
 * The main entry point for this library.  Use this class to construct a collection of source files which can be
 * generated and stored.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class JDeparser {

    private JDeparser() {
    }

    /**
     * Create a new source generation collection.
     *
     * @param filer the filer to use to store generated sources
     * @param format the formatting preferences to use for these sources
     * @return the source collection
     */
    public static JSources createSources(final JFiler filer, final FormatPreferences format) {
        return new ImplJSources(filer, format);
    }

    /**
     * Drop all thread-local caches.  This can be done to save memory or avoid GC problems after source generation
     * has been completed.  Call within a {@code finally} block to ensure that resources are released regardless of
     * the outcome of intervening operations.
     */
    public static void dropCaches() {
        JExprs.cache.remove();
        JTypes.cache.remove();
    }
}
