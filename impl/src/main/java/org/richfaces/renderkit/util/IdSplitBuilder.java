/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Nick Belaevski
 */
final class IdSplitBuilder {

    private static final int INITIAL_SPLIT_LIST_SIZE = 3;

    private enum State {
        IN_ID (true) {

            @Override
            public State getNextState(char c) {
                if (c == '[') {
                    return State.IN_ID_INSIDE_BRACKETS;
                } else if (isSeparator(c)) {
                    return State.OUTSIDE_ID;
                } else {
                    return this;
                }
            }
        },
        IN_ID_INSIDE_BRACKETS (true) {

            @Override
            public State getNextState(char c) {
                if (c == ']') {
                    return State.IN_ID;
                } else {
                    return this;
                }
            }
        },
        OUTSIDE_ID (false) {

            @Override
            public State getNextState(char c) {
                if (!isSeparator(c)) {
                    if (c == '[') {
                        return State.IN_ID_INSIDE_BRACKETS;
                    } else {
                        return State.IN_ID;
                    }
                }

                return this;
            }
        };

        private final boolean idSegment;

        private State(boolean idSegment) {
            this.idSegment = idSegment;
        }

        private static boolean isSeparator(char c) {
            return c == ',' || Character.isWhitespace(c);
        }

        public abstract State getNextState(char c);

        public boolean isIdSegment() {
            return idSegment;
        }

        public void processChar(IdSplitBuilder builder, char c, int charIdx) {
            State nextState = getNextState(c);

            if (nextState.isIdSegment() ^ isIdSegment()) {
                if (nextState.isIdSegment()) {
                    builder.setStartIndex(charIdx);
                } else {
                    builder.flushBuilder(charIdx);
                    builder.setStartIndex(-1);
                }
            }

            builder.state = nextState;
        }
    }

    private int startIdx = -1;

    private String sourceString;

    private List<String> result = new ArrayList<String>(INITIAL_SPLIT_LIST_SIZE);

    private State state = State.OUTSIDE_ID;

    private IdSplitBuilder(String sourceString) {
        super();
        this.sourceString = sourceString;
    }

    private void setStartIndex(int idx) {
        startIdx = idx;
    }

    private void flushBuilder(int endIdx) {
        if (startIdx >= 0 && endIdx > startIdx) {
            String id = sourceString.substring(startIdx, endIdx);
            result.add(id);
        }
    }

    private void build() {
        int length = sourceString.length();
        for (int i = 0; i < length; i++) {
            char c = sourceString.charAt(i);
            state.processChar(this, c, i);
        }
        flushBuilder(length);
    }

    private String[] getSplit() {
        return result.toArray(new String[result.size()]);
    }

    public static String[] split(String s) {
        IdSplitBuilder splitBuilder = new IdSplitBuilder(s);
        splitBuilder.build();
        return splitBuilder.getSplit();
    }

}