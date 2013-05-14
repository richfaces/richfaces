/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.showcase.tabPanel;

import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.showcase.panel.AbstractPanelTest;
import org.richfaces.tests.showcase.tabPanel.page.SimplePage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITSimple extends AbstractPanelTest {

    @Page
    private SimplePage page;

    @Test
    public void testTabsAreNotEmpty() {
        checkToogleTab(page.firstTabButton, RICH_FACES_INFO);
        checkToogleTab(page.secondTabButton, RICH_FACES_JSF_INFO);
    }

    private void checkToogleTab(WebElement button, String expectedContent) {
        button.click();
        assertTrue("Content of the panel is different!", page.panelBody.getText().contains(expectedContent));
    }

}
