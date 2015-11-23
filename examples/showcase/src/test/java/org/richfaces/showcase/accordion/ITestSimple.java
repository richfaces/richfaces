/*******************************************************************************
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
 *******************************************************************************/
package org.richfaces.showcase.accordion;

import static org.junit.Assert.assertFalse;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.richfaces.showcase.accordion.page.SimplePage;
import org.richfaces.showcase.panel.AbstractPanelTest;
/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestSimple extends AbstractPanelTest {

    @Page
    private SimplePage page;

    @Test
    public void testAccordionAndContent() {
        if (!page.getFirstPanelContent().isDisplayed()) {
            page.getFirstPanel().click();
        }
        checkContentOfPanel(page.getFirstPanelContent(), RICH_FACES_INFO);
        page.getSecondPanel().click();
        assertFalse("The body of the first panel should not be visible, since the panel is hidden!", page.getFirstPanelContent().isDisplayed());
        checkContentOfPanel(page.getSecondPanelContent(), RICH_FACES_JSF_INFO);
        page.getFirstPanel().click();
        assertFalse("The body of the second panel should not be visible, since the panel is hidden!", page.getSecondPanelContent().isDisplayed());

    }

}
