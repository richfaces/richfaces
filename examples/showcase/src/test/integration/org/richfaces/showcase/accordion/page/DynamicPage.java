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
package org.richfaces.showcase.accordion.page;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class DynamicPage {

    @FindBy(css = ".rf-ac-itm-lbl")
    private List<WebElement> accordionExpanders;

    @FindByJQuery("input:visible:eq(1)")
    private WebElement skinSwitcherButton;

    static final int NUMBER_OF_ACCORDION_PANELS = 7;

    public void interateOverAccordionPanelsAndAssert() {
        for (int i = 0; i < NUMBER_OF_ACCORDION_PANELS; i++) {
            if (i != 0) {
                guardAjax(accordionExpanders.get(i)).click();
            }
            String valueOfSkinSwitcher = skinSwitcherButton.getAttribute("value");
            String valueOfPanelHeader = accordionExpanders.get(i).getText().split(":")[1].trim();
            assertTrue("The accordion panel was not switched correctly!", valueOfSkinSwitcher.contains(valueOfPanelHeader));
        }
    }

    public List<WebElement> getAccordionExpanders() {
        return accordionExpanders;
    }

    public WebElement getSkinSwitcherButton() {
        return skinSwitcherButton;
    }
}
