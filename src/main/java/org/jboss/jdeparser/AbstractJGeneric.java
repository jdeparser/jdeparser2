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
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
abstract class AbstractJGeneric extends BasicJAnnotatable implements JGenericDef {
    private ArrayList<ImplJTypeParamDef> typeParamDefs;

    private ImplJTypeParamDef add(ImplJTypeParamDef item) {
        if (typeParamDefs == null) {
            typeParamDefs = new ArrayList<>();
        }
        typeParamDefs.add(item);
        return item;
    }

    public JTypeParamDef typeParam(final String name) {
        return add(new ImplJTypeParamDef(name));
    }

    public JTypeParamDef[] typeParams() {
        return typeParamDefs.toArray(new JTypeParamDef[typeParamDefs.size()]);
    }

    JType[] typeParamsToArgs() {
        final ArrayList<ImplJTypeParamDef> typeParamDefs = this.typeParamDefs;
        if (typeParamDefs == null) {
            return JType.NONE;
        }
        final JType[] jTypes = new JType[typeParamDefs.size()];
        int i = 0;
        for (ImplJTypeParamDef paramDef : typeParamDefs) {
            jTypes[i++] = JTypes.typeNamed(paramDef.getName());
        }
        return jTypes;
    }

    void writeTypeParams(final SourceFileWriter sourceFileWriter) throws IOException {
        if (typeParamDefs != null) {
            final Iterator<ImplJTypeParamDef> iterator = typeParamDefs.iterator();
            if (iterator.hasNext()) {
                if (sourceFileWriter.getState() instanceof $KW) {
                    sourceFileWriter.sp();
                }
                sourceFileWriter.write($PUNCT.ANGLE.OPEN);
                ImplJTypeParamDef def = iterator.next();
                def.write(sourceFileWriter);
                while (iterator.hasNext()) {
                    sourceFileWriter.write($PUNCT.COMMA);
                    def = iterator.next();
                    def.write(sourceFileWriter);
                }
                sourceFileWriter.write($PUNCT.ANGLE.CLOSE);
                sourceFileWriter.sp();
            }
        }
    }
}
