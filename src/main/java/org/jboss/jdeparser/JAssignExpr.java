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
public interface JAssignExpr extends JExpr, JStatement {
    // assign

    JExpr assign(JExpr e1);

    JExpr addAssign(JExpr e1);

    JExpr subAssign(JExpr e1);

    JExpr mulAssign(JExpr e1);

    JExpr divAssign(JExpr e1);

    JExpr modAssign(JExpr e1);

    JExpr andAssign(JExpr e1);

    JExpr orAssign(JExpr e1);

    JExpr xorAssign(JExpr e1);

    JExpr shrAssign(JExpr e1);

    JExpr lshrAssign(JExpr e1);

    JExpr shlAssign(JExpr e1);

    // inc/dec

    JExpr postInc();

    JExpr postDec();

    JExpr preInc();

    JExpr preDec();

}
