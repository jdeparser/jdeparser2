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

import java.util.Arrays;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class NarrowedJType extends AbstractJType {

    private final AbstractJType erased;
    private final JType[] args;

    public NarrowedJType(final AbstractJType erased, final JType[] args) {
        this.erased = erased;
        this.args = args;
    }

    public String simpleName() {
        return erased.simpleName();
    }

    public String binaryName() {
        return erased.binaryName();
    }

    public String qualifiedName() {
        return erased.qualifiedName();
    }

    public JExpr _class() {
        return erased._class();
    }

    public JExpr _this() {
        return erased._this();
    }

    public JExpr _super() {
        return erased._super();
    }

    public JCall _new() {
        final JCall call = erasure()._new();
        for (JType arg : args) {
            call.typeArg(arg);
        }
        return call;
    }

    public JType typeArg(final JType... args) {
        return new NarrowedJType(this, args);
    }

    private static JType[] concat(JType[] a, JType[] b) {
        if (a.length == 0) return b;
        final int al = a.length;
        final int bl = b.length;
        final JType[] c = Arrays.copyOf(a, al + bl);
        System.arraycopy(b, 0, c, al, bl);
        return c;
    }

    public JType[] typeArgs() {
        return concat(erased.typeArgs(), args);
    }

    public JType erasure() {
        return erased.erasure();
    }

    public String toString() {
        final StringBuilder b = new StringBuilder(erased.toString());
        b.append('<');
        for (int i = 0; i < args.length; i++) {
            JType arg = args[i];
            b.append(arg);
            if (i < args.length - 1) {
                b.append(",");
            }
        }
        b.append('>');
        return b.toString();
    }
}
