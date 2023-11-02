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

import java.io.IOException;
import java.util.Set;

/**
 * A repository of source files.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface JSources {

    /**
     * Create a source file.
     *
     * @param packageName the package name of the source file
     * @param fileName the source file name (excluding {@code .java} suffix)
     * @return the source file
     */
    JSourceFile createSourceFile(String packageName, String fileName);

    /**
     * Create a {@code package-info.java} file.
     *
     * @param packageName the package name
     * @return the source file
     */
    JDocCommentable createPackageInfoFile(String packageName);

    /**
     * Write the source files.
     *
     * @throws IOException if a write operation fails
     */
    void writeSources() throws IOException;
}
