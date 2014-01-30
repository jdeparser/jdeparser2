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

import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class JTypes {
    private JTypes() {}

    public static JType typeVariable(String name) {
        return new TypeVarJType(name);
    }

    public static JType typeOf(Class<?> clazz) {
        return new ClassJType(clazz.getName());
    }

    public static JType typeNamed(String name) {
        return new NameJType(name);
    }

    /**
     * Get a {@code JType} that corresponds to the given {@code TypeMirror} for annotation processors.
     *
     * @param typeMirror the type mirror
     * @return the {@code JType}
     */
    public static JType typeOf(TypeMirror typeMirror) {
        if (typeMirror instanceof ArrayType) {
            return typeOf(((ArrayType) typeMirror).getComponentType()).array();
        } else if (typeMirror instanceof WildcardType) {
            final WildcardType wildcardType = (WildcardType) typeMirror;
            final TypeMirror extendsBound = wildcardType.getExtendsBound();
            final TypeMirror superBound = wildcardType.getSuperBound();
            return extendsBound != null ? typeOf(extendsBound).wildcardExtends() : superBound != null ? typeOf(superBound).wildcardSuper() : JType.WILDCARD;
        } else if (typeMirror instanceof TypeVariable) {
            final TypeVariable typeVariable = (TypeVariable) typeMirror;
            final String name = typeVariable.asElement().getSimpleName().toString();
            return JTypes.typeVariable(name);
        } else if (typeMirror instanceof DeclaredType) {
            final DeclaredType declaredType = (DeclaredType) typeMirror;
            final TypeElement typeElement = (TypeElement) declaredType.asElement();
            final String name = typeElement.getQualifiedName().toString();
            final JType rawType = new ClassJType(name);
            final List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            if (typeArguments.isEmpty()) {
                return rawType;
            }
            JType[] args = new JType[typeArguments.size()];
            for (int i = 0; i < typeArguments.size(); i++) {
                final TypeMirror argument = typeArguments.get(i);
                args[i] = typeOf(argument);
            }
            return rawType.typeArg(args);
        } else if (typeMirror instanceof NoType) {
            switch (typeMirror.getKind()) {
                case VOID:      return JType.VOID;
            }
            // fall out
        } else if (typeMirror instanceof PrimitiveType) {
            switch (typeMirror.getKind()) {
                case BOOLEAN:   return JType.BOOLEAN;
                case BYTE:      return JType.BYTE;
                case SHORT:     return JType.SHORT;
                case INT:       return JType.INT;
                case LONG:      return JType.LONG;
                case CHAR:      return JType.CHAR;
                case FLOAT:     return JType.FLOAT;
                case DOUBLE:    return JType.DOUBLE;
            }
            // fall out
        }
        throw new IllegalArgumentException(String.format("Cannot find equivalent type to %s", typeMirror));
    }
}
