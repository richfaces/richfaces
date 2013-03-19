package org.richfaces.arquillian.page.source;

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
