/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.renderkit.html;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author akolonitsky
 * @since 2010-10-25
 */
public class PanelMenuGroupRendererTest extends RendererTestBase {
    @Test
    public void testEmpty() throws IOException, SAXException {
        doTest("panelMenuGroup", "f:panelMenuGroup");
    }

    @Test
    public void testExpanded() throws IOException, SAXException {
        doTest("panelMenuGroup-expanded", "f:panelMenuGroup");
    }

    @Test
    public void testTopGroup() throws IOException, SAXException {
        doTest("panelMenuGroup-topGroup", "f:panelMenuGroup");
    }

    @Test
    public void testIconsInheritanceTopGroup() throws IOException, SAXException {
        doTest("panelMenu-icons-inheritance", "panelMenu-icons-inheritance-topGroup", "f:panelMenuGroup-top");
        doTest("panelMenu-icons-inheritance", "panelMenu-icons-inheritance-topGroupDis", "f:panelMenuGroup-topDis");
    }

    @Test
    public void testIconsInheritanceTopItem() throws IOException, SAXException {
        doTest("panelMenu-icons-inheritance", "panelMenu-icons-inheritance-topItem", "f:panelMenuItem-top");
        doTest("panelMenu-icons-inheritance", "panelMenu-icons-inheritance-topItemDis", "f:panelMenuItem-topDis");
    }

    @Test
    public void testIconsInheritanceItem() throws IOException, SAXException {
        doTest("panelMenu-icons-inheritance", "panelMenu-icons-inheritance-item", "f:panelMenuItem");
        doTest("panelMenu-icons-inheritance", "panelMenu-icons-inheritance-itemDis", "f:panelMenuItem-dis");
    }

    @Test
    public void testIconsInheritanceGroup() throws IOException, SAXException {
        doTest("panelMenu-icons-inheritance", "panelMenu-icons-inheritance-group", "f:panelMenuGroup");
        doTest("panelMenu-icons-inheritance", "panelMenu-icons-inheritance-groupDis", "f:panelMenuGroup-dis");
    }
}
