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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Nick Belaevski
 *
 */
public class ComponentMatcherNodeTest {

    private ComponentMatcherNode rootNode;

    @Before
    public void setUp() throws Exception {
        rootNode = new ComponentMatcherNode();
    }

    @After
    public void tearDown() throws Exception {
        rootNode = null;
    }

    private ComponentMatcherNode createNode(String source, boolean isPattern) {
        ComponentMatcherNode node = new ComponentMatcherNode();
        node.setSource(source);
        node.setPatternNode(isPattern);

        return node;
    }

    @Test
    public void testIdChild() throws Exception {
        ComponentMatcherNode idNode = createNode("table", false);

        assertNull(rootNode.getChild("table", false));
        assertNull(rootNode.getChild("table2", false));

        rootNode.addChild(idNode);

        assertSame(rootNode, idNode.getParentNode());
        assertNotNull(rootNode.getIdChildren());
        assertTrue(rootNode.hasDirectIdChildren());
        assertTrue(rootNode.hasDirectChildren());
        assertFalse(rootNode.hasDirectPatternChildren());
        assertFalse(rootNode.hasKidPatternNodes());

        assertSame(idNode, rootNode.getChild("table", false));
        assertSame(idNode, rootNode.getMatchedChild("table"));

        assertNull(rootNode.getChild("table2", false));
        assertNull(rootNode.getChild("xChild", false));
        assertNull(rootNode.getMatchedChild("xChild"));

        ComponentMatcherNode id2Node = createNode("table2", false);
        rootNode.addChild(id2Node);
        assertSame(idNode, rootNode.getChild("table", false));
        assertSame(id2Node, rootNode.getChild("table2", false));
        assertSame(id2Node, rootNode.getMatchedChild("table2"));

        rootNode.removeChild(idNode);
        rootNode.removeChild(id2Node);

        assertNull(idNode.getParentNode());
        assertFalse(rootNode.hasDirectIdChildren());
        assertFalse(rootNode.hasDirectChildren());
        assertFalse(rootNode.hasDirectPatternChildren());
        assertFalse(rootNode.hasKidPatternNodes());

        assertNull(rootNode.getChild("table", false));
        assertNull(rootNode.getMatchedChild("table"));
    }

    @Test
    public void testMarkAddedRemoved() throws Exception {
        ComponentMatcherNode idNode = createNode("table", false);
        ComponentMatcherNode patternNode = createNode("*", true);

        assertFalse(idNode.isAdded());
        assertFalse(patternNode.isAdded());

        rootNode.addChild(idNode);
        assertFalse(idNode.isAdded());
        assertFalse(rootNode.isAdded());

        idNode.markAdded();
        assertSame(idNode, rootNode.getChild("table", false));
        assertTrue(idNode.isAdded());
        assertFalse(rootNode.isAdded());

        idNode.markRemoved();
        assertSame(idNode, rootNode.getChild("table", false));
        assertFalse(idNode.isAdded());
        assertFalse(rootNode.isAdded());

        rootNode.addChild(patternNode);
        assertFalse(patternNode.isAdded());
        assertFalse(rootNode.isAdded());

        patternNode.markAdded();
        assertSame(patternNode, rootNode.getChild("*", true));
        assertTrue(patternNode.isAdded());
        assertFalse(rootNode.isAdded());

        patternNode.markRemoved();
        assertSame(patternNode, rootNode.getChild("*", true));
        assertFalse(patternNode.isAdded());
        assertFalse(rootNode.isAdded());
    }

    @Test
    public void testPatternChild() throws Exception {
        ComponentMatcherNode patternNode = createNode("*", true);

        assertNull(rootNode.getChild("*", true));

        rootNode.addChild(patternNode);

        assertSame(rootNode, patternNode.getParentNode());
        assertNotNull(rootNode.getPatternChildren());
        assertFalse(rootNode.hasDirectIdChildren());
        assertTrue(rootNode.hasDirectChildren());
        assertTrue(rootNode.hasDirectPatternChildren());
        assertTrue(rootNode.hasKidPatternNodes());

        assertSame(patternNode, rootNode.getChild("*", true));
        assertSame(patternNode, rootNode.getMatchedChild("anyId"));
        assertSame(patternNode, rootNode.getMatchedChild("justAnotherId"));

        rootNode.removeChild(patternNode);
        assertNull(patternNode.getParentNode());
        assertFalse(rootNode.hasDirectIdChildren());
        assertFalse(rootNode.hasDirectChildren());
        assertFalse(rootNode.hasDirectPatternChildren());
        assertFalse(rootNode.hasKidPatternNodes());

        assertNull(rootNode.getChild("*", true));
        assertNull(rootNode.getMatchedChild("anyId"));
        assertNull(rootNode.getMatchedChild("justAnotherId"));
    }

    private boolean hasPatternParent(ComponentMatcherNode node) {
        ComponentMatcherNode pNode = node;
        while ((pNode = pNode.getParentNode()) != null) {
            if (pNode.isPatternNode()) {
                return true;
            }
        }

        return false;
    }

    private boolean hasPatternKids(Map<String, ComponentMatcherNode> nodesMap) {
        if (nodesMap == null) {
            return false;
        }

        for (ComponentMatcherNode node : nodesMap.values()) {
            if (node.isPatternNode()) {
                return true;
            }

            if (hasPatternKids(node.getIdChildren())) {
                return true;
            }

            if (hasPatternKids(node.getPatternChildren())) {
                return true;
            }
        }

        return false;
    }

    private boolean hasPatternKids(ComponentMatcherNode node) {
        if (hasPatternKids(node.getIdChildren())) {
            return true;
        }

        if (hasPatternKids(node.getPatternChildren())) {
            return true;
        }

        return false;
    }

    private void verifyNodeChildren(Map<String, ComponentMatcherNode> nodesMap) {
        if (nodesMap != null) {
            for (ComponentMatcherNode childNode : nodesMap.values()) {
                verifyNode(childNode);
            }
        }
    }

    private void verifyNode(ComponentMatcherNode node) {
        assertTrue(hasPatternParent(node) == node.hasParentPatternNode());
        assertTrue(hasPatternKids(node) == node.hasKidPatternNodes());

        verifyNodeChildren(node.getIdChildren());
        verifyNodeChildren(node.getPatternChildren());
    }

    @Test
    public void testAddChild() throws Exception {
        verifyNode(rootNode);

        ComponentMatcherNode tableNode = createNode("table", false);
        rootNode.addChild(tableNode);
        verifyNode(rootNode);
        assertSame(tableNode, rootNode.getChild("table", false));
        assertSame(tableNode, rootNode.getMatchedChild("table"));

        ComponentMatcherNode tablePatternNode = createNode("table-*", true);
        rootNode.addChild(tablePatternNode);
        verifyNode(rootNode);
        assertSame(tablePatternNode, rootNode.getChild("table-*", true));
        assertSame(tablePatternNode, rootNode.getMatchedChild("anyChild"));

        ComponentMatcherNode rowNode = createNode("row", false);
        tableNode.addChild(rowNode);
        verifyNode(rootNode);
        assertSame(rowNode, tableNode.getChild("row", false));
        assertSame(rowNode, tableNode.getMatchedChild("row"));

        ComponentMatcherNode cellPatternNode = createNode("cell-*", true);
        rowNode.addChild(cellPatternNode);
        verifyNode(rootNode);
        assertSame(cellPatternNode, rowNode.getChild("cell-*", true));
        assertSame(cellPatternNode, rowNode.getMatchedChild("anyCell"));

        ComponentMatcherNode secondRowNode = createNode("row", false);
        tablePatternNode.addChild(secondRowNode);
        verifyNode(rootNode);
        assertSame(secondRowNode, tablePatternNode.getChild("row", false));
        assertSame(secondRowNode, tablePatternNode.getMatchedChild("row"));

        ComponentMatcherNode secondCellPatternNode = createNode("cell-*", true);
        secondRowNode.addChild(secondCellPatternNode);
        verifyNode(rootNode);
        assertSame(secondCellPatternNode, secondRowNode.getChild("cell-*", true));
        assertSame(secondCellPatternNode, secondRowNode.getMatchedChild("anyCell"));
    }

    @Test
    public void testRemoveChild() throws Exception {
        ComponentMatcherNode tableNode = createNode("table", false);
        rootNode.addChild(tableNode);

        ComponentMatcherNode tablePatternNode = createNode("table-*", true);
        rootNode.addChild(tablePatternNode);

        ComponentMatcherNode rowNode = createNode("row", false);
        tableNode.addChild(rowNode);

        ComponentMatcherNode cellPatternNode = createNode("cell-*", true);
        rowNode.addChild(cellPatternNode);

        ComponentMatcherNode secondRowNode = createNode("row", false);
        tablePatternNode.addChild(secondRowNode);

        ComponentMatcherNode secondCellPatternNode = createNode("cell-*", true);
        secondRowNode.addChild(secondCellPatternNode);

        verifyNode(rootNode);

        assertSame(secondCellPatternNode, secondRowNode.getChild("cell-*", true));
        assertSame(secondCellPatternNode, secondRowNode.getMatchedChild("anyCell"));
        secondRowNode.removeChild(secondCellPatternNode);
        assertNull(secondRowNode.getChild("cell-*", true));
        assertNull(secondRowNode.getMatchedChild("anyCell"));

        verifyNode(rootNode);

        assertSame(rowNode, tableNode.getChild("row", false));
        assertSame(rowNode, tableNode.getMatchedChild("row"));
        tableNode.removeChild(rowNode);
        assertNull(tableNode.getChild("row", false));
        assertNull(tableNode.getMatchedChild("row"));

        verifyNode(rootNode);

        assertSame(secondRowNode, tablePatternNode.getChild("row", false));
        assertSame(secondRowNode, tablePatternNode.getMatchedChild("row"));
        tablePatternNode.removeChild(secondRowNode);
        assertNull(tablePatternNode.getChild("row", false));
        assertNull(tablePatternNode.getMatchedChild("row"));

        verifyNode(rootNode);

        assertSame(tablePatternNode, rootNode.getChild("table-*", true));
        assertSame(tablePatternNode, rootNode.getMatchedChild("anyChild"));
        rootNode.removeChild(tablePatternNode);
        assertNull(rootNode.getChild("table-*", true));
        assertNull(rootNode.getMatchedChild("anyChild"));

        verifyNode(rootNode);

        assertSame(tableNode, rootNode.getChild("table", false));
        assertSame(tableNode, rootNode.getMatchedChild("table"));
        rootNode.removeChild(tableNode);
        assertNull(rootNode.getChild("table", false));
        assertNull(rootNode.getMatchedChild("table"));

        verifyNode(rootNode);
    }
}
