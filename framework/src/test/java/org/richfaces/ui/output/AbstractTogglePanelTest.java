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
package org.richfaces.ui.output;

import java.util.List;

import javax.faces.component.UIComponent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.ui.toggle.togglePanel.UITogglePanel;
import org.richfaces.ui.toggle.togglePanel.UITogglePanelItem;

/**
 * @author akolonitsky
 * @since Jul 20, 2010
 */
public class AbstractTogglePanelTest {
    private static final String ITEM1 = "item1";
    private static final String ITEM2 = "item2";
    private static final String ITEM3 = "item3";
    private UITogglePanel panel;
    private UITogglePanelItem item1;
    private UITogglePanelItem item2;
    private UITogglePanelItem item3;

    @Before
    public void setUp() {
        panel = new UITogglePanel();
        List<UIComponent> children = panel.getChildren();

        item1 = createItem(ITEM1);
        children.add(item1);

        item2 = createItem(ITEM2);
        children.add(item2);

        item3 = createItem(ITEM3);
        children.add(item3);
    }

    @Test
    public void testDefaultActiveItem() {
        Assert.assertNotNull(panel);
        Assert.assertEquals(null, panel.getActiveItem());

        panel.setActiveItem(ITEM2);
        Assert.assertEquals(ITEM2, panel.getActiveItem());
    }

//    @Test
//    public void testGetItemByIndex() {
//        panel.setActiveItem(ITEM1);
//
//        Assert.assertEquals(item1, panel.getItemByIndex(0));
//        Assert.assertEquals(item2, panel.getItemByIndex(1));
//        Assert.assertEquals(item3, panel.getItemByIndex(2));
//        Assert.assertEquals(null, panel.getItemByIndex(3));
//
//        item1.setRendered(false);
//
//        Assert.assertEquals(item2, panel.getItemByIndex(0));
//        Assert.assertEquals(item3, panel.getItemByIndex(1));
//        Assert.assertEquals(null, panel.getItemByIndex(2));
//        Assert.assertEquals(null, panel.getItemByIndex(3));
//
//        item2.setRendered(false);
//        item3.setRendered(false);
//        Assert.assertEquals(null, panel.getItemByIndex(0));
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testGetItem() {
//        Assert.assertEquals(item1, panel.getItem(ITEM1));
//        Assert.assertEquals(item2, panel.getItem(ITEM2));
//        Assert.assertEquals(item3, panel.getItem(ITEM3));
//        Assert.assertEquals(null, panel.getItem("123"));
//
//        panel.getItem(null);
//    }
//
//    @Test
//    public void testItemOrder() {
//        panel.setActiveItem(ITEM1);
//        Assert.assertEquals(item1, panel.getFirstItem());
//        Assert.assertEquals(null, panel.getPrevItem());
//        Assert.assertEquals(item2, panel.getNextItem());
//        Assert.assertEquals(item3, panel.getLastItem());
//
//        panel.setActiveItem(ITEM2);
//        Assert.assertEquals(item1, panel.getFirstItem());
//        Assert.assertEquals(item1, panel.getPrevItem());
//        Assert.assertEquals(item3, panel.getNextItem());
//        Assert.assertEquals(item3, panel.getLastItem());
//
//        panel.setActiveItem(ITEM3);
//        Assert.assertEquals(item1, panel.getFirstItem());
//        Assert.assertEquals(item2, panel.getPrevItem());
//        Assert.assertEquals(null, panel.getNextItem());
//        Assert.assertEquals(item3, panel.getLastItem());
//
//        item1.setRendered(false);
//
//        panel.setActiveItem(ITEM1);
//        Assert.assertEquals(item2, panel.getFirstItem());
//        Assert.assertEquals(null, panel.getPrevItem());
//        Assert.assertEquals(null, panel.getNextItem());
//        Assert.assertEquals(item3, panel.getLastItem());
//
//        panel.setActiveItem(ITEM2);
//        Assert.assertEquals(item2, panel.getFirstItem());
//        Assert.assertEquals(null, panel.getPrevItem());
//        Assert.assertEquals(item3, panel.getNextItem());
//        Assert.assertEquals(item3, panel.getLastItem());
//
//        panel.setActiveItem(ITEM3);
//        Assert.assertEquals(item2, panel.getFirstItem());
//        Assert.assertEquals(item2, panel.getPrevItem());
//        Assert.assertEquals(null, panel.getNextItem());
//        Assert.assertEquals(item3, panel.getLastItem());
//    }

    public void isActiveItem() {
        panel.setActiveItem(ITEM1);
        Assert.assertTrue(panel.isActiveItem(item1));
        Assert.assertFalse(panel.isActiveItem(item2));
        Assert.assertFalse(panel.isActiveItem(null));
        Assert.assertFalse(panel.isActiveItem(panel));
    }

/*  Test commented out, as getName now requires an active FacesContext

    @Test(expected = IllegalArgumentException.class)
    public void testGetChildName() {
        Assert.assertEquals(item1.getName(), panel.getChildName(item1));
        Assert.assertEquals(item2.getName(), panel.getChildName(item2));
        Assert.assertNull(panel.getChildName(null));

        panel.getChildName(panel);
    }
*/

    private static UITogglePanelItem createItem(String name) {
        UITogglePanelItem item = new UITogglePanelItem();
        item.setName(name);

        return item;
    }
}
