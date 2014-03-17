/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.renderkit.html;

import java.util.Map;
import java.util.Map.Entry;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.google.common.collect.Maps;

/**
 * @author akolonitsky
 * @since Oct 22, 2010
 */
public class IgnoreScriptsContent implements DifferenceListener {

    private Map<String, String> idMapping = Maps.newLinkedHashMap();

    public int differenceFound(Difference difference) {
        switch (difference.getId()) {

            case DifferenceConstants.TEXT_VALUE_ID:
                if (!"script".equalsIgnoreCase(difference.getTestNodeDetail().getNode().getLocalName())) {
                    return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
                }
                break;

            case DifferenceConstants.ELEMENT_NUM_ATTRIBUTES_ID:
                return ignoreEmptyValueDifference(difference);
            case DifferenceConstants.ATTR_NAME_NOT_FOUND_ID:
                return ignoreEmptyValueDifference(difference);
            case DifferenceConstants.ATTR_VALUE_ID:
                return ignoreGeneratedIdDifference(difference);
        }
        return RETURN_ACCEPT_DIFFERENCE;
    }

    private int ignoreGeneratedIdDifference(Difference difference) {
        Attr attr1 = (Attr) difference.getControlNodeDetail().getNode();
        Attr attr2 = (Attr) difference.getTestNodeDetail().getNode();

        if ((attr1.getLocalName().equals("id") && attr2.getLocalName().equals("id") || attr1.getLocalName().equals("name")
                && attr2.getLocalName().equals("name"))) {
            String id1 = normalizeId(attr1.getValue());
            String id2 = normalizeId(attr2.getValue());
            String mappedId = lookupIdMapping(id1, id2);
            if (mappedId.equals(id2)) {
                return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
            }
        }

        return RETURN_ACCEPT_DIFFERENCE;
    }

    private String normalizeId(String id) {
        return id;
        // return id.replaceAll("j_idt", "").replaceAll("j_id_", "");
    }

    private String lookupIdMapping(String id, String newId) {

        // look for most recent mapping match or prefix-match
        String mapping = null;
        for (Entry<String, String> entry : idMapping.entrySet()) {
            final String referenceId = entry.getKey();
            final String mappedId = entry.getValue();

            if (id.equals(referenceId)) {
                return entry.getValue();
            }

            final String referenceIdPrefix = referenceId + ":";
            final String mappedIdPrefix = mappedId + ":";
            if (id.startsWith(referenceIdPrefix)) {
                mapping = id.replaceFirst("^" + referenceIdPrefix, mappedIdPrefix);
            }
        }

        if (mapping == null) {
            mapping = newId;
        }

        idMapping.put(id, newId);

        return mapping;
    }

    private int ignoreEmptyValueDifference(Difference difference) {
        Attr value1 = (Attr) difference.getControlNodeDetail().getNode().getAttributes().getNamedItem("value");
        Attr value2 = (Attr) difference.getTestNodeDetail().getNode().getAttributes().getNamedItem("value");
        if (value1 == null && value2 != null && value2.getValue().equals("")) {
            return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
        }
        if (value2 == null && value1 != null && value1.getValue().equals("")) {
            return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
        }
        return RETURN_ACCEPT_DIFFERENCE;
    }

    public void skippedComparison(Node node, Node node1) {

    }
}
