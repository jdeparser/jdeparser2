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

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class BlankLine implements ClassFileContent, ClassContent, BlockContent, JClassItem {

    private static final BlankLine INSTANCE = new BlankLine();

    private BlankLine() {
    }

    static BlankLine getInstance() {
        return INSTANCE;
    }

    @Override
    public void write(final SourceFileWriter writer) throws IOException {
        writer.nl();
    }

    @Override
    public Kind getItemKind() {
        return Kind.BLANK_LINE;
    }

    @Override
    public int getModifiers() {
        return 0;
    }

    @Override
    public boolean hasAllModifiers(final int mods) {
        return false;
    }

    @Override
    public boolean hasAnyModifier(final int mods) {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }
}
