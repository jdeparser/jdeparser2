/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015 Red Hat, Inc., and individual contributors
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

import java.util.ArrayList;

interface Sectionable {

    JBlock init(ArrayList<ClassContent> content);

    JBlock staticInit(ArrayList<ClassContent> content);

    JVarDeclaration field(ArrayList<ClassContent> content, int mods, JType type, String name, JExpr init);

    JMethodDef method(ArrayList<ClassContent> content, int mods, JType returnType, String name);

    JMethodDef constructor(ArrayList<ClassContent> content, int mods);

    JClassDef _class(ArrayList<ClassContent> content, int mods, String name);

    JClassDef _enum(ArrayList<ClassContent> content, int mods, String name);

    JClassDef _interface(ArrayList<ClassContent> content, int mods, String name);

    JClassDef annotationInterface(ArrayList<ClassContent> content, int mods, String name);
}
