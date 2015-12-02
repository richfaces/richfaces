/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.context;

import static org.richfaces.component.MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR;

import java.util.List;

import org.richfaces.util.SeparatorChar;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

/**
 * Helper class for parsing ids.
 *
 * @author Nick Belaevski
 */
final class IdParser {
    public static final class Node {
        private String image;
        private String function;

        Node(String image) {
            this(image, null);
        }

        Node(String image, String function) {
            super();
            this.image = image;
            this.function = function;
        }

        public String getImage() {
            return image;
        }

        public String getFunction() {
            return function;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("image", image).add("function", function).toString();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((function == null) ? 0 : function.hashCode());
            result = prime * result + ((image == null) ? 0 : image.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Node other = (Node) obj;
            if (function == null) {
                if (other.function != null) {
                    return false;
                }
            } else if (!function.equals(other.function)) {
                return false;
            }
            if (image == null) {
                if (other.image != null) {
                    return false;
                }
            } else if (!image.equals(other.image)) {
                return false;
            }
            return true;
        }
    }

    private static final char FUNCTION_IMAGE_START_TOKEN = '(';
    private static final char FUNCTION_IMAGE_END_TOKEN = ')';
    private static final Node[] EMPTY_NODES_ARRAY = new Node[0];

    private IdParser() {
    }

    public static Node[] parse(String id) {
        if (id.length() == 0) {
            return EMPTY_NODES_ARRAY;
        }

        List<Node> result = Lists.newArrayList();

        Iterable<String> split = SeparatorChar.SPLITTER.split(id);
        for (String s : split) {
            if (s.charAt(0) == META_COMPONENT_SEPARATOR_CHAR) {
                int startImageIdx = s.indexOf(FUNCTION_IMAGE_START_TOKEN);

                if (startImageIdx < 0) {
                    result.add(new Node(s));
                } else {
                    if (s.charAt(s.length() - 1) != FUNCTION_IMAGE_END_TOKEN) {
                        throw new IllegalArgumentException(id);
                    }

                    if (startImageIdx + 1 > s.length() - 1) {
                        throw new IllegalArgumentException(id);
                    }

                    String image = s.substring(startImageIdx + 1, s.length() - 1);
                    String functionName = s.substring(1, startImageIdx);

                    result.add(new Node(image, functionName));
                }
            } else {
                result.add(new Node(s));
            }
        }

        return result.toArray(new Node[result.size()]);
    }
}