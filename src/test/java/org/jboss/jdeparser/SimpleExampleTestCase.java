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

import static org.jboss.jdeparser.JMod.FINAL;
import static org.jboss.jdeparser.JMod.PUBLIC;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public class SimpleExampleTestCase {

    @Test
    public void testSimple() throws IOException {
        final JSources sources = JDeparser.createSources(JFiler.newInstance(new File("/home/david")), new FormatPreferences(new Properties()));
        final JClassFile bazFile = sources.createSourceFile("org.foo.bar", "Baz.java");
        final JClassDef baz = bazFile._class(PUBLIC | FINAL, "Baz");
        final JMethodDef getString = baz.method(PUBLIC | FINAL, JTypes.typeOf(String.class), "getString");
        getString._throws(IllegalStateException.class);
        getString.typeParam("T")._extends(JTypes.typeOf(String.class));
        getString.param(PUBLIC | FINAL, JTypes.typeNamed("T"), "theTee");
        final JBlock body = getString.body();
        body.call(JExprs.name("System").field("out"), "println").arg(JExprs.name("theTee"));
        sources.writeSources();
    }
}
