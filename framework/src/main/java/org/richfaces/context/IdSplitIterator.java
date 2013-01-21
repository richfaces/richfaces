/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import static org.richfaces.util.Util.NamingContainerDataHolder.SEPARATOR_CHAR;

import com.google.common.base.Strings;
import com.google.common.collect.AbstractIterator;

final class IdSplitIterator extends AbstractIterator<String> {
    private String s;
    private String subtreeId;
    private int idx;

    public IdSplitIterator(String s) {
        super();
        this.s = s;
        this.idx = s.length();
    }

    @Override
    protected String computeNext() {
        int oldSepIdx = idx;
        idx = s.lastIndexOf(SEPARATOR_CHAR, idx - 1);

        String result;

        if (idx >= 0) {
            result = s.substring(idx + 1, oldSepIdx);

            subtreeId = s.substring(0, idx);
        } else {
            if (oldSepIdx < 0) {
                oldSepIdx = 0;
            }
            result = s.substring(0, oldSepIdx);

            subtreeId = null;
        }

        if (Strings.isNullOrEmpty(result)) {
            endOfData();
        }

        return result;
    }

    public String getSubtreeId() {
        return subtreeId;
    }
}