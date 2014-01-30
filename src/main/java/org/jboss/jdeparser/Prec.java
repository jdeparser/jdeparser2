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
final class Prec {
    static final int PAREN = 0;
    static final int ARRAY_ACCESS = 1;
    static final int MEMBER_ACCESS = 1;
    static final int METHOD_CALL = 1;
    static final int POST_INC_DEC = 1;
    static final int PRE_INC_DEC = 2;
    static final int UNARY = 2;
    static final int CAST = 3;
    static final int NEW = 3;
    static final int MULTIPLICATIVE = 4;
    static final int ADDITIVE = 5;
    static final int SHIFT = 6;
    static final int RELATIONAL = 7;
    static final int INSTANCEOF = 7;
    static final int EQUALITY = 8;
    static final int BIT_AND = 9;
    static final int BIT_XOR = 10;
    static final int BIT_OR = 11;
    static final int LOG_AND = 12;
    static final int LOG_OR = 13;
    static final int COND = 14;
    static final int ASSIGN = 15;
}
