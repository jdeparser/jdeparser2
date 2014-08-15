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

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class BinaryJExpr extends AbstractJExpr {
    private final $PUNCT.BINOP op;
    private final AbstractJExpr e1;
    private final AbstractJExpr e2;
    private final Assoc assoc;

    BinaryJExpr(final $PUNCT.BINOP op, final AbstractJExpr e1, final AbstractJExpr e2, final int prec) {
        this(op, e1, e2, prec, Assoc.LEFT);
    }

    BinaryJExpr(final $PUNCT.BINOP op, final AbstractJExpr e1, final AbstractJExpr e2, final int prec, final Assoc assoc) {
        super(prec);
        this.op = op;
        this.e1 = e1.prec() > prec ? new ParenJExpr(e1) : e1;
        this.e2 = e2.prec() > prec ? new ParenJExpr(e2) : e2;
        this.assoc = assoc;
    }

    AbstractJExpr getExpr1() {
        return e1;
    }

    AbstractJExpr getExpr2() {
        return e2;
    }

    Assoc getAssoc() {
        return assoc;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        writer.write(e1);
        writer.write(op.getSpacingRule());
        writer.write(op);
        writer.write(op.getSpacingRule());
        writer.write(e2);
    }
}
