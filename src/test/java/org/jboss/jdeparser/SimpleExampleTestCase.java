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

import static org.jboss.jdeparser.JExprs.$;
import static org.jboss.jdeparser.JMod.FINAL;
import static org.jboss.jdeparser.JMod.PUBLIC;
import static org.jboss.jdeparser.JMod.STATIC;
import static org.jboss.jdeparser.JTypes._;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.junit.Test;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public class SimpleExampleTestCase extends AbstractGeneratingTestCase {

    @Test
    public void testSimple() throws IOException {
        final JSources sources = JDeparser.createSources(getFiler(), new FormatPreferences(new Properties()));
        final JClassFile bazFile = sources.createSourceFile("org.foo.bar", "Baz.java");
        final JClassDef baz = bazFile._class(PUBLIC | FINAL, "Baz");
        final JMethodDef getString = baz.method(PUBLIC | FINAL, String.class, "getString");
        getString.docComment().text("Hello\nthere!");
        getString.docComment().htmlTag("ul", true).attribute("class", "banana").htmlTag("li", false).text("A line item");
        getString.docComment().docTag("author", "Someone");
        getString._throws(IllegalStateException.class);
        getString.typeParam("T")._extends(JTypes.typeOf(String.class));
        baz.field(PUBLIC | STATIC | FINAL, JType.INT, "_foo", JExpr.ONE);
        final JVarDeclaration field = baz.field(PUBLIC | FINAL, JType.INT, "localVar");
        baz.constructor(JMod.PUBLIC).body().assign(JExprs.$(field), JExpr.ONE);
        final JParamDeclaration theTee = getString.param(PUBLIC | FINAL, "T", "theTee");
        final JBlock body = getString.body();
        body.call(_(System.class).$("out"), "println").arg($(theTee.name()));
        body.add(_(System.class).$("out").call("println").arg($("theTee"))).blockComment().text("Hello\nagain!");
        final JExpr tmp1 = body.tempVar(_(int.class), JExprs.decimal(123).add(JExprs.decimal(456)).mul(JExprs.decimal(246)));
        body.var(FINAL, _(String.class), "foo", JExprs.str("boo")).add("bar", JExprs.str("zoo")). add("baz");
        body.add(_(System.class).$("out").call("println").arg(tmp1));
        final JAnonymousClassDef anon = _(Runnable.class)._newAnon();
        anon.init().add(_(System.class).$("out").call("println").arg(JExprs.str("Bananas!")));
        body.add(anon);
        sources.writeSources();
        final ByteArrayInputStream inputStream = openFile("org.foo.bar", "Baz.java");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
