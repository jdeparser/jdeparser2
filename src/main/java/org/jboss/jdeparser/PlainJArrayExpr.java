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

import static org.jboss.jdeparser.FormatPreferences.Space;
import static org.jboss.jdeparser.Tokens.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class PlainJArrayExpr extends AbstractJExpr implements JArrayExpr {

    private ArrayList<JExpr> members;

    PlainJArrayExpr() {
        super(0);
    }

    PlainJArrayExpr(final JExpr... members) {
        super(0);
        if (members != null) {
            final ArrayList<JExpr> list = new ArrayList<>(members.length);
            for (JExpr expr : members) {
                if (expr != null) {
                    list.add(expr);
                }
            }
            if (list.size() > 0) {
                this.members = list;
            }
        }
    }

    public JArrayExpr add(final JExpr value) {
        if (value != null) {
            ArrayList<JExpr> members = this.members;
            if (members == null) {
                this.members = members = new ArrayList<>();
            }
            members.add(value);
        }
        return this;
    }

    public int elementCount() {
        final ArrayList<JExpr> members = this.members;
        return members == null ? 0 : members.size();
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writer.write($PUNCT.BRACE.OPEN);
        final ArrayList<JExpr> members = this.members;
        if (members != null) {
            final Iterator<JExpr> iterator = members.iterator();
            if (iterator.hasNext()) {
                writer.write(Space.WITHIN_BRACES_ARRAY_INIT);
                writer.write(iterator.next());
                while (iterator.hasNext()) {
                    writer.write($PUNCT.COMMA);
                    writer.write(iterator.next());
                }
                writer.write(Space.WITHIN_BRACES_ARRAY_INIT);
            }
        }
        writer.write($PUNCT.BRACE.CLOSE);
    }
}
