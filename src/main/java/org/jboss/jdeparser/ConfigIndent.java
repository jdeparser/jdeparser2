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
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ConfigIndent implements Indent {

    private final FormatPreferences.Indentation indentation;

    ConfigIndent(final FormatPreferences.Indentation indentation) {
        this.indentation = indentation;
    }

    FormatPreferences.Indentation getIndentation() {
        return indentation;
    }

    public void addIndent(final Indent next, final FormatPreferences preferences, final StringBuilder lineBuffer) {
        if (! preferences.isIndentAbsolute(indentation)) next.addIndent(next, preferences, lineBuffer);
        final int indent = preferences.getIndent(indentation);
        for (int i = 0; i < indent; i ++) {
            lineBuffer.append(' ');
        }
    }

    public void escape(final Indent next, final StringBuilder b, final int idx) {
        next.escape(next, b, idx);
    }

    public void unescaped(final Indent next, final StringBuilder b, final int idx) {
        next.unescaped(next, b, idx);
    }
}
