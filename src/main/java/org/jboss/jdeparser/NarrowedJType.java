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

import java.io.IOException;
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

    boolean equals(final AbstractJType other) {
        return other instanceof NarrowedJType && equals((NarrowedJType) other);
    }

    private boolean equals(final NarrowedJType other) {
        return erased.equals(other.erased) && Arrays.equals(args, other.args);
    }

    public int hashCode() {
        return erased.hashCode() * 17 + Arrays.hashCode(args);
    }

    public String simpleName() {
        return erased.simpleName();
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
        return new NewJCall(this);
    }

    public JCall _new(final JExpr dim) {
        final JCall call = erasure()._new();
        for (JType arg : args) {
            call.typeArg(arg);
        }
        return call;
    }

    public JType typeArg(final JType... args) {
        return new NarrowedJType(erased, concat(this.args, args));
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
        return args;
    }

    public JType erasure() {
        return erased;
    }

    public JCall call(final String name) {
        return erasure().call(name);
    }

    public JType nestedType(final String name) {
        return erasure().nestedType(name);
    }

    void writeDirect(final SourceFileWriter sourceFileWriter) throws IOException {
        sourceFileWriter.write(erasure());
        final JType[] args = this.args;
        final int len = args.length;
        if (len > 0) {
            sourceFileWriter.write($PUNCT.ANGLE.OPEN);
            JType type = args[0];
            sourceFileWriter.write(type);
            for (int i = 1; i < len; i ++) {
                sourceFileWriter.write(FormatPreferences.Space.BEFORE_COMMA);
                sourceFileWriter.write($PUNCT.COMMA);
                sourceFileWriter.write(FormatPreferences.Space.AFTER_COMMA_TYPE_ARGUMENT);
                type = args[i];
                sourceFileWriter.write(type);
            }
            sourceFileWriter.write($PUNCT.ANGLE.CLOSE);
        }
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
