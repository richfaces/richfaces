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

package org.richfaces.ui.tabPanel;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DynamicTabTestHelper {

    public void check_tab_switch(WebElement tabPanel, List<WebElement> tabs, WebElement ajaxCreateTabButton) {
        Assert.assertEquals(6, tabs.size());
        Assert.assertEquals("content of tab 0", getTabContent(tabPanel).getText());

        guardAjax(tabs.get(2)).click();
        Assert.assertEquals("content of tab 2", getTabContent(tabPanel).getText());

        guardAjax(tabs.get(4)).click();
        Assert.assertEquals("content of tab 4", getTabContent(tabPanel).getText());

        guardAjax(tabs.get(5)).click();
        Assert.assertEquals("content of tab 5", getTabContent(tabPanel).getText());

        guardAjax(tabs.get(0)).click();
        Assert.assertEquals("content of tab 0", getTabContent(tabPanel).getText());

        guardAjax(ajaxCreateTabButton).click();
        Assert.assertEquals(7, tabs.size());

        guardAjax(tabs.get(6)).click();
        Assert.assertEquals("content of tab 6", getTabContent(tabPanel).getText());

        guardAjax(tabs.get(0)).click();
        Assert.assertEquals("content of tab 0", getTabContent(tabPanel).getText());

        WebElement removeLink =tabs.get(6).findElement(By.tagName("a"));
        guardAjax(removeLink).click();
        Assert.assertEquals(6, tabs.size());
    }

    public WebElement getTabContent(WebElement tabPanel) {
        for (WebElement tabContent : tabPanel.findElements(By.className("rf-tab"))) {
            if (tabContent.isDisplayed()) {
                return tabContent;
            }
        }
        return null;
    }

    public void check_row_removal(WebElement tabPanel, List<WebElement> tabs, WebElement ajaxCreateTabButton) {
        Assert.assertEquals(6, tabs.size());

        guardAjax(ajaxCreateTabButton).click();
        guardAjax(ajaxCreateTabButton).click();
        guardAjax(ajaxCreateTabButton).click();

        Assert.assertEquals(9, tabs.size());

        WebElement removeLink =tabs.get(8).findElement(By.tagName("a"));
        guardAjax(removeLink).click();
        Assert.assertEquals(8, tabs.size());

        removeLink =tabs.get(7).findElement(By.tagName("a"));
        guardAjax(removeLink).click();
        Assert.assertEquals(7, tabs.size());

        removeLink =tabs.get(6).findElement(By.tagName("a"));
        guardAjax(removeLink).click();
        Assert.assertEquals(6, tabs.size());
    }
}
