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
package org.richfaces.component;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;

import java.util.List;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author akolonitsky
 * @since 2010-08-14
 */
public class AbstractAccordionTest {
    private static final String ITEM1 = "item1";
    private static final String ITEM2 = "item2";
    private static final String ITEM3 = "item3";
    private AbstractAccordion accordion;
    private AbstractAccordionItem item1;
    private AbstractAccordionItem item2;
    private AbstractAccordionItem item3;

    @Before
    public void setUp() {
        accordion = new AbstractAccordion() {
            @Override
            public String getItemActiveLeftIcon() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getItemInactiveLeftIcon() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getItemDisabledLeftIcon() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getItemActiveRightIcon() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getItemInactiveRightIcon() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getItemDisabledRightIcon() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getWidth() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getHeight() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getItemActiveHeaderClass() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getItemDisabledHeaderClass() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getItemInactiveHeaderClass() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getItemContentClass() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getItemHeaderClass() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getOnitemchange() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getOnbeforeitemchange() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean isLimitRender() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean isCycledSwitching() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Object getData() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getStatus() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Object getExecute() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Object getRender() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public MethodExpression getItemChangeListener() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getLang() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getDir() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getTitle() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getStyle() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getStyleClass() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getOnclick() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getOndblclick() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getOnmousedown() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getOnmousemove() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getOnmouseout() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getOnmouseover() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getOnmouseup() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Converter getConverter() {
                return null;
            }
        };
        List<UIComponent> children = accordion.getChildren();

        item1 = createItem(ITEM1);
        children.add(item1);

        item2 = createItem(ITEM2);
        children.add(item2);

        item3 = createItem(ITEM3);
        children.add(item3);
    }

    @Test
    @Ignore
    public void testDefaultActiveItem() {
        Assert.assertNotNull(accordion);
        Assert.assertEquals(ITEM1, accordion.getActiveItem());

        accordion.setActiveItem(ITEM2);
        Assert.assertEquals(ITEM2, accordion.getActiveItem());
    }

    private static AbstractAccordionItem createItem(String name) {
        AbstractAccordionItem item = createNiceMock(AbstractAccordionItem.class);
        expect(item.getName()).andReturn(name);

        return item;
    }
}
