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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.annotation.processing.Filer;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public abstract class JFiler {

    public static JFiler newInstance(final Filer filer) {
        return new JFiler() {
            public OutputStream openStream(final String packageName, final String fileName) throws IOException {
                // Create the FQCN
                final StringBuilder sb = new StringBuilder(packageName);
                if (sb.charAt(sb.length() -1) != '.') {
                    sb.append('.');
                }
                sb.append(fileName);
                return filer.createSourceFile(sb).openOutputStream();
            }
        };
    }

    public static JFiler newInstance(final File target) {
        return new JFiler() {
            public OutputStream openStream(final String packageName, final String fileName) throws IOException {
                final File dir = new File(target, packageName.replace('.', File.separatorChar));
                dir.mkdirs();
                return new FileOutputStream(new File(dir, fileName));
            }
        };
    }

    protected JFiler() {
    }

    private String encoding = "utf-8";

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(final String encoding) {
        this.encoding = encoding;
    }

    public abstract OutputStream openStream(String packageName, String fileName) throws IOException;

    public Writer openWriter(String packageName, String fileName) throws IOException {
        return new OutputStreamWriter(openStream(packageName, fileName), encoding);
    }
}
