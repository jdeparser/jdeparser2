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

import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
final class JSectionImpl implements JSection {

    private final JLayout.MemberMatcher matcher;

    private final List<JClassItem> before = new ArrayList<>();
    private final List<JClassItem> between = new ArrayList<>();
    private final List<JClassItem> after = new ArrayList<>();
    private final List<JClassItem> empty = new ArrayList<>();

    private Comparator<JClassItem> comparator;

    JSectionImpl(final JLayout.MemberMatcher matcher) {
        this.matcher = matcher;
    }

    private <T extends JClassItem> T doAdd(T item, int where) {
        if ((where & BEFORE) != 0) before.add(item);
        if ((where & BETWEEN) != 0) between.add(item);
        if ((where & AFTER) != 0) after.add(item);
        if ((where & EMPTY) != 0) empty.add(item);
        return item;
    }

    public JComment addLineComment(final int where) {
        return doAdd(new LineJComment(), where);
    }

    public JComment addBlockComment(final int where) {
        return doAdd(new BlockJComment(), where);
    }

    public JSection addBlankLine(final int where) {
        doAdd(BlankLine.getInstance(), where);
        return this;
    }

    List<JClassItem> apply(List<JClassItem> orig) {
        // stage one - filter and sort
        List<JClassItem> filtered = new ArrayList<>();
        for (JClassItem item : orig) {
            if (matcher.matches(item)) {
                filtered.add(item);
            }
        }
        if (comparator != null) {
            Collections.sort(filtered, comparator);
        }
        // stage two - emit
        List<JClassItem> finalList = new ArrayList<>((filtered.size() + 1) * max(max(before.size(), between.size()), max(after.size(), empty.size())));
        final Iterator<JClassItem> iterator = filtered.iterator();
        if (iterator.hasNext()) {
            finalList.addAll(before);
            finalList.add(iterator.next());
            if (iterator.hasNext()) {
                do {
                    finalList.addAll(between);
                    finalList.add(iterator.next());
                } while (iterator.hasNext());
            }
            finalList.addAll(after);
        } else {
            finalList.addAll(empty);
        }
        return finalList;
    }

    public void setSort(final Comparator<JClassItem> comparator) {
        this.comparator = comparator;
    }
}
