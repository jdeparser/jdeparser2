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
class PrimitiveJType extends AbstractJType {
    private final String simpleName;
    private final ReferenceJType boxed;

    PrimitiveJType(final String simpleName, final String boxed) {
        this.simpleName = simpleName;
        this.boxed = boxed == null ? null : new ReferenceJType(null, "java.lang", boxed, this);
    }

    public JType box() {
        return boxed;
    }

    public JExpr _class() {
        return new TypeRefJExpr(this, "class");
    }

    public String simpleName() {
        return simpleName;
    }

    public String toString() {
        return simpleName;
    }
}
