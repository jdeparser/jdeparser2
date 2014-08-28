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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public abstract class AbstractGeneratingTestCase {
    private final ConcurrentMap<Key, ByteArrayOutputStream> sourceFiles = new ConcurrentHashMap<>();

    private final JFiler filer = new JFiler() {
        public OutputStream openStream(final String packageName, final String fileName) throws IOException {
            final Key key = new Key(packageName, fileName + ".java");
            if (! sourceFiles.containsKey(key)) {
                final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (sourceFiles.putIfAbsent(key, stream) == null) {
                    return stream;
                }
            }
            throw new IOException("Already exists");
        }
    };

    public JFiler getFiler() {
        return filer;
    }

    public ByteArrayInputStream openFile(String packageName, String fileName) throws FileNotFoundException {
        final ByteArrayOutputStream out = sourceFiles.get(new Key(packageName, fileName));
        if (out == null) throw new FileNotFoundException("No file found for package " + packageName + " file " + fileName);
        return new ByteArrayInputStream(out.toByteArray());
    }

    static final class Key {
        private final String packageName;
        private final String fileName;

        Key(final String packageName, final String fileName) {
            this.packageName = packageName;
            this.fileName = fileName;
        }

        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final Key key = (Key) o;

            return fileName.equals(key.fileName) && packageName.equals(key.packageName);
        }

        public int hashCode() {
            int result = packageName.hashCode();
            result = 31 * result + fileName.hashCode();
            return result;
        }
    }
}
