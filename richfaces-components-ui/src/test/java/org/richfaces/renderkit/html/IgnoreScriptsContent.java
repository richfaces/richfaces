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

/*
 *
 */

// Remove after test moved to the test-jsf project
package org.richfaces.renderkit.html;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.w3c.dom.Node;

/**
 * @author akolonitsky
 * @since Oct 22, 2010
 */
public class IgnoreScriptsContent implements DifferenceListener {
    public int differenceFound(Difference difference) {
        if (DifferenceConstants.TEXT_VALUE_ID == difference.getId()
            && !"script".equalsIgnoreCase(difference.getTestNodeDetail().getNode().getLocalName())) {

            return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
        }
        return RETURN_ACCEPT_DIFFERENCE;
    }

    public void skippedComparison(Node node, Node node1) {

    }
}
