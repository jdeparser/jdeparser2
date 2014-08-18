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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
abstract class AbstractJComment implements JComment, Writable {

    static final CommentContent NL_CONTENT = new CommentContent() {
        public void write(final SourceFileWriter writer) throws IOException {
            writer.nl();
        }
    };
    static final CommentTextContent HASH_CONTENT = new CommentTextContent("#");
    static final CommentTextContent OPEN_PAREN_CONTENT = new CommentTextContent("(");
    static final CommentTextContent COMMA_CONTENT = new CommentTextContent(",");
    static final CommentTextContent CLOSE_PAREN_CONTENT = new CommentTextContent(")");
    static final InlineDocTagCommentContent DOC_ROOT_CONTENT = new InlineDocTagCommentContent("docRoot");

    private List<Writable> content;

    void addItemDirectly(Writable item) {
        if (item != null) {
            List<Writable> content = this.content;
            if (content == null) {
                content = this.content = new ArrayList<>();
            }
            content.add(item);
        }
    }

    protected <T extends CommentContent> T add(T item) {
        addItemDirectly(item);
        return item;
    }

    public JComment text(final String text) {
        int c;
        int s = 0;
        int i;
        for (i = 0; i < text.length(); i = text.offsetByCodePoints(i, 1)) {
            c = text.codePointAt(i);
            if (c == '\n') {
                if (i - s > 0) {
                    add(new CommentTextContent(text.substring(s, i)));
                }
                add(NL_CONTENT);
                s = i + 1;
            } else if (Character.isWhitespace(c)) {
                if (i - s > 0) {
                    add(new CommentTextContent(text.substring(s, i)));
                }
                add(NonTrailingSpaceContent.getInstance());
                s = i + 1;
            }
        }
        if (i - s > 0) {
            add(new CommentTextContent(text.substring(s, i)));
        }
        return this;
    }

    public JComment sp() {
        add(NonTrailingSpaceContent.getInstance());
        return this;
    }

    public JComment typeName(final JType type) {
        add(new JTypeCommentContent(type));
        return this;
    }

    public JComment block() {
        return this.add(new NestedCommentContent());
    }

    public JComment inlineDocTag(final String tag, final String body) {
        InlineDocTagCommentContent content = new InlineDocTagCommentContent(tag);
        add(content).text(body);
        return this;
    }

    public JComment inlineDocTag(final String tag) {
        return add(new InlineDocTagCommentContent(tag));
    }

    public JComment linkType(boolean plain, final JType targetType) {
        InlineDocTagCommentContent link = new InlineDocTagCommentContent(plain ? "linkplain" : "link");
        link.typeName(targetType);
        link.sp();
        return add(link);
    }

    public JComment linkField(boolean plain, final JType targetType, final String targetField) {
        InlineDocTagCommentContent item = new InlineDocTagCommentContent(plain ? "linkplain" : "link");
        item.typeName(targetType);
        item.add(HASH_CONTENT);
        item.text(targetField);
        item.sp();
        return add(item);
    }

    public JComment linkConstructor(boolean plain, final JType targetType, final JType... params) {
        InlineDocTagCommentContent item = new InlineDocTagCommentContent(plain ? "linkplain" : "link");
        item.typeName(targetType);
        item.add(HASH_CONTENT);
        item.typeName(targetType);
        item.add(OPEN_PAREN_CONTENT);
        for (int i = 0; i < params.length;) {
            item.add(new JTypeCommentContent(params[i].erasure()));
            for (; i < params.length; i ++) {
                item.add(COMMA_CONTENT);
                item.add(new JTypeCommentContent(params[i].erasure()));
            }
        }
        item.add(CLOSE_PAREN_CONTENT);
        item.sp();
        return add(item);
    }

    public JComment linkMethod(boolean plain, final JType targetType, final String targetMethod, final JType... params) {
        InlineDocTagCommentContent item = new InlineDocTagCommentContent(plain ? "linkplain" : "link");
        item.typeName(targetType);
        item.add(HASH_CONTENT);
        item.text(targetMethod);
        item.add(OPEN_PAREN_CONTENT);
        for (int i = 0; i < params.length;) {
            item.add(new JTypeCommentContent(params[i].erasure()));
            for (; i < params.length; i ++) {
                item.add(COMMA_CONTENT);
                item.add(new JTypeCommentContent(params[i].erasure()));
            }
        }
        item.add(CLOSE_PAREN_CONTENT);
        item.sp();
        return add(item);
    }

    public JComment linkMethod(boolean plain, final JMethodDef methodDef) {
        if (methodDef instanceof AbstractJMethodDef) {
            AbstractJMethodDef abstractJMethodDef = (AbstractJMethodDef) methodDef;
            AbstractJClassDef clazz = abstractJMethodDef.clazz();
            JTypeCommentContent typeContent = new JTypeCommentContent(clazz.erasedType());
            CommentContent name;
            if (methodDef instanceof MethodJMethodDef) {
                name = new CommentTextContent(((MethodJMethodDef) methodDef).getName());
            } else {
                assert methodDef instanceof ConstructorJMethodDef;
                name = typeContent;
            }
            InlineDocTagCommentContent item = new InlineDocTagCommentContent(plain ? "linkplain" : "link");
            item.add(typeContent);
            item.add(HASH_CONTENT);
            item.add(name);
            item.add(OPEN_PAREN_CONTENT);
            JParamDeclaration[] params = abstractJMethodDef.params();
            if (params.length > 0) {
                item.add(new JTypeCommentContent(params[0].type().erasure()));
                for (int i = 1; i < params.length; i ++) {
                    item.add(COMMA_CONTENT);
                    item.add(new JTypeCommentContent(params[i].type().erasure()));
                }
            }
            item.add(CLOSE_PAREN_CONTENT);
            item.sp();
            return add(item);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public JComment code() {
        return inlineDocTag("code");
    }

    public JComment docRoot() {
        add(DOC_ROOT_CONTENT);
        return this;
    }

    public JComment nl() {
        add(NL_CONTENT);
        return this;
    }

    public void write(final SourceFileWriter writer) throws IOException {
        for (Writable item : content) {
            item.write(writer);
        }
    }

    List<Writable> getContent() {
        return content;
    }
}
