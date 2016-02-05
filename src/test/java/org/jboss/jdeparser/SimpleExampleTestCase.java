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

import static org.jboss.jdeparser.JExprs.$v;
import static org.jboss.jdeparser.JExprs.str;
import static org.jboss.jdeparser.JMod.FINAL;
import static org.jboss.jdeparser.JMod.PUBLIC;
import static org.jboss.jdeparser.JMod.STATIC;
import static org.jboss.jdeparser.JMod.VARARGS;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public class SimpleExampleTestCase extends AbstractGeneratingTestCase {

    @Test
    public void testSimple() throws IOException {
        final JSources sources = JDeparser.createSources(getFiler(), new FormatPreferences(new Properties()));
        final JSourceFile bazFile = sources.createSourceFile("org.foo.bar", "Baz");
        bazFile._import(BigDecimal.class);
        final JClassDef baz = bazFile._class(PUBLIC | FINAL, "Baz");
        final JMethodDef getString = baz.method(PUBLIC | FINAL, String.class, "getString");
        getString.docComment().text("Hello\nthere!");
        getString.docComment().htmlTag("ul", true).attribute("class", "banana").htmlTag("li", false).text("A line item");
        getString.docComment().docTag("author", "Someone");
        getString._throws(IllegalStateException.class);
        getString.typeParam("T")._extends(JTypes.typeOf(String.class));
        baz.field(PUBLIC | STATIC | FINAL, JType.INT, "_foo", JExpr.ONE);
        final JVarDeclaration field = baz.field(PUBLIC | FINAL, JType.INT, "localVar");
        baz.constructor(JMod.PUBLIC).body().assign(JExprs.$v(field), JExpr.ONE);
        final JParamDeclaration theTee = getString.param(FINAL, "T", "theTee");
        JParamDeclaration vap = getString.param(FINAL | VARARGS, Object.class, "whatever");
        final JBlock body = getString.body();
        JVarDeclaration t = body.var(0, String.class, "test", JExpr.NULL);
        JIf jIf = body._if(JExprs.$v(t).eq(JExpr.NULL));
        jIf.assign(JExprs.$v(t), str("new Value"));
        jIf._else().assign(JExprs.$v(t), str("other value"));

        // static import and reference
        body.call(JTypes.$t(Thread.State.class).$v("NEW"), "toString");
        bazFile.importStatic(Thread.State.class, "NEW");

        // Reference an enclosing class
        body.var(0, JTypes.$t(Map.Entry.class).typeArg(String.class, Object.class), "mapEntry", JExpr.NULL);
        bazFile._import(Map.class);

        body.call(JTypes.$t(System.class).$v("out"), "println").arg(JExprs.$v(theTee));
        body.add(JTypes.$t(System.class).$v("out").call("println").arg($v("theTee"))).blockComment().text("Hello\nagain!");
        final JExpr tmp1 = body.tempVar(JTypes.$t(int.class), JExprs.decimal(123).plus(JExprs.decimal(456)).times(JExprs.decimal(246)));
        body.var(FINAL, JTypes.$t(String.class), "foo", JExprs.str("boo")).add("bar", JExprs.str("zoo")). add("baz");
        body.add(JTypes.$t(System.class).$v("out").call("println").arg(tmp1));
        final JAnonymousClassDef anon = JTypes.$t(Runnable.class)._newAnon();
        anon.init().add(JTypes.$t(System.class).$v("out").call("println").arg(JExprs.str("Bananas!")));
        body.add(anon);
        body.add(JTypes.$t(System.class).$v("out").call("println").arg(JExprs.lambda().param("foo").param("bar").body(JExprs.str("Super bananas!"))));
        body.add(JTypes.$t(System.class).$v("out").call("println").arg(JExprs.lambda().param(int.class, "foo").param(int.class, "bar").body(JExprs.str("Super duper bananas!"))));
        final JLambda lambda = JExprs.lambda().param(int.class, "foo").param(int.class, "bar");
        final JBlock lambdaBody = lambda.body();
        lambdaBody.add(JTypes.$t(System.class).$v("out").call("println").arg(JExprs.str("Bananamus Maximus!")));
        body.add(JTypes.$t(System.class).$v("out").call("println").arg(lambda));
        sources.writeSources();
        final ByteArrayInputStream inputStream = openFile("org.foo.bar", "Baz.java");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
