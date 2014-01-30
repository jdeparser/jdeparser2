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
public interface JClassFile extends JInlineCommentable {
    JClassFile _import(String type);

    JClassFile _import(JType type);

    JClassFile _import(Class<?> type);

    JClassFile importStatic(String type, String member);

    JClassFile importStatic(JType type, String member);

    JClassFile importStatic(Class<?> type, String member);

    JClassDef _class(int mods, String name);

    JClassDef _enum(int mods, String name);

    JClassDef _interface(int mods, String name);

    JClassDef annotationInterface(int mods, String name);
}
