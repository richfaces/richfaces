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
package org.richfaces.context;


/**
 * Helper class for parsing ids.
 *
 * @author Nick Belaevski
 */
final class IdParser {

    private String id;

    private String componentId;

    private String metadataComponentId;

    private int idx;

    private final char namingContainerSeparator;

    private final char subComponentSeparator;

    public IdParser(char namingContainerSeparator, char subComponentSeparator) {
        super();

        this.namingContainerSeparator = namingContainerSeparator;
        this.subComponentSeparator = subComponentSeparator;
    }

    private void reset() {
        this.id = null;

        this.componentId = null;
        this.metadataComponentId = null;

        this.idx = 0;
    }

    public void setId(String id) {
        reset();
        this.id = id;
    }

    public boolean findNext() {
        componentId = null;
        metadataComponentId = null;

        if (id == null) {
            return false;
        }

        int idLength = id.length();

        if (idx < idLength) {
            boolean foundSeparator = false;

            for (int i = idx; i < idLength && !foundSeparator; i++) {
                char c = id.charAt(i);

                if (c == subComponentSeparator) {
                    if (componentId == null) {
                        componentId = id.substring(idx, i);
                        idx = i;
                    }
                } else if (c == namingContainerSeparator) {
                    String idSegment = id.substring(idx, i);

                    if (componentId == null) {
                        componentId = idSegment;
                    } else {
                        metadataComponentId = idSegment;
                    }

                    idx = i + 1;
                    foundSeparator = true;
                }
            }

            if (!foundSeparator) {
                String idSegment = id.substring(idx, idLength);

                if (componentId == null) {
                    componentId = idSegment;
                } else {
                    metadataComponentId = idSegment;
                }

                idx = idLength;
            }

            return true;
        } else {
            reset();
            return false;
        }
    }

    public String getComponentId() {
        return componentId;
    }

    public String getMetadataComponentId() {
        return metadataComponentId;
    }

}