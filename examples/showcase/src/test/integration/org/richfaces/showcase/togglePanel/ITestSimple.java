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
package org.richfaces.showcase.togglePanel;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.togglePanel.page.SimplePage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestSimple extends AbstractWebDriverTest {

    private static final String CONTENT_OF_TAB1 = "For now you are at Panel 1";
    private static final String CONTENT_OF_TAB2 = "For now you are at Panel 2";

    @Page
    private SimplePage page;

    @Test
    public void testTogglePanelItem1() {
        checkToggleTab(page.getToggleTab1(), CONTENT_OF_TAB1);
    }

    @Test
    public void testTogglePanelItem2() {
        checkToggleTab(page.getToggleTab2(), CONTENT_OF_TAB2);
    }

    private void checkToggleTab(WebElement button, String expectedContent) {
        guardAjax(button).click();
        assertTrue("The content of panel is diferent!", page.getPanelContent().getText().contains(expectedContent));
    }
}
