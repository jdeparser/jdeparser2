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

import static org.jboss.jdeparser.Tokens.*;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class PlainJClassDef extends AbstractJClassDef implements BlockContent, JClassItem {

    PlainJClassDef(final int mods, final ImplJSourceFile classFile, final String name) {
        super(mods, classFile, name);
    }

    PlainJClassDef(final int mods, final AbstractJClassDef enclosingClass, final String name) {
        super(mods, enclosingClass, name);
    }

    $KW designation() {
        return $KW.CLASS;
    }

    public Kind getItemKind() {
        return Kind.CLASS;
    }

    public String getName() {
        return super.getName();
    }
}
