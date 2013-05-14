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
package org.richfaces.tests.showcase.tabPanel.page;

import static org.jboss.arquillian.graphene.Graphene.attribute;
import static org.jboss.arquillian.graphene.Graphene.element;
import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.jboss.arquillian.graphene.Graphene.waitGui;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class DynamicTabsPage {

    @FindBy(jquery = ".example-cnt td[id*='header:active']:visible")
    public WebElement activeTabHeader;

    @FindBy(jquery = ".example-cnt td[id*='header:inactive']:visible")
    public List<WebElement> inactiveTabsHeaders;

    @FindBy(jquery = "input[type='submit']:visible")
    public WebElement submitButton;

    public static final int NUM_OF_TABS = 8;

    public void iterateOverTabsAndAssert() {
        for (WebElement i : inactiveTabsHeaders) {
            String headerText = i.getText();

            guardXhr(i).click();
            waitGui().until(element(submitButton).isVisible());
            waitGui().withTimeout(2, TimeUnit.SECONDS).withMessage("The tab was not switched correctly!")
                .until(attribute(submitButton, "value").contains(headerText));
        }
    }
}