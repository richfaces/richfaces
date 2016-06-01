/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.showcase.inplaceSelect;

import static org.jboss.arquillian.graphene.Graphene.waitModel;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.inplaceSelect.RichFacesInplaceSelect;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.inplaceSelect.page.SimplePage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ITestInplaceSelect extends AbstractWebDriverTest {

    @Page
    private SimplePage page;

    @Test
    public void testCustomizationSelect() {
        checkSelect(page.getCustomSelect(), "Alabama", Event.DBLCLICK);
        checkSelect(page.getCustomSelect(), "Florida", Event.DBLCLICK);
        checkSelect(page.getCustomSelect(), "California", Event.DBLCLICK);
    }

    @Test
    public void testSimpleSelect() {
        for (int i = 1; i <= 5; i++) {
            checkSelect(page.getSimpleSelect(), "Option " + i, Event.CLICK);
        }
    }

    /**
     * Checks the select, when it is select which is activated by double click, then also there is need for click on accept
     * button.
     */
    private void checkSelect(RichFacesInplaceSelect select, String option, Event event) {
        if (event == Event.DBLCLICK) {
            select.advanced().setEditByEvent(event);
            select.select(option).confirmByControlls();
        } else {
            select.select(option).confirm();
        }
        waitModel().until().element(select.advanced().getLabelInputElement()).text().equalTo(option);
    }
}
