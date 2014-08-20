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
package org.richfaces.showcase.tabPanel.page;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.tabPanel.RichFacesTabPanel;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class DynamicTabsPage {

    @FindByJQuery(".rf-tbp:eq(1)")
    private RichFacesTabPanel tabPanel;

    public RichFacesTabPanel getTabPanel() {
        return tabPanel;
    }

    @FindByJQuery("input[type='submit']:visible")
    private WebElement submitButton;

    public static final int NUM_OF_TABS = 8;

    public void iterateOverTabsAndAssert() {
        for (int i = 1; i < tabPanel.getNumberOfTabs(); i++) {
            tabPanel.switchTo(i);

            String headerText = tabPanel.advanced().getActiveHeaderElement().getText();

            waitGui().until().element(submitButton).is().visible();
            waitGui().withTimeout(2, TimeUnit.SECONDS).withMessage("The tab was not switched correctly!").until()
                    .element(submitButton).attribute("value").contains(headerText);
        }
    }

    public WebElement getSubmitButton() {
        return submitButton;
    }

    public void setSubmitButton(WebElement submitButton) {
        this.submitButton = submitButton;
    }
}
