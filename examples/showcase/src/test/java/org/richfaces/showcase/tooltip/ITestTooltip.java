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
package org.richfaces.showcase.tooltip;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.tooltip.page.TooltipPage;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class ITestTooltip extends AbstractWebDriverTest {

    private static final String TOOLTIP_TEXT_AJAX_CLICK = "This tool-tip content was rendered on server\ntooltips requested: 0";
    private static final String TOOLTIP_TEXT_AJAX = "This tool-tip content was rendered on the server\ntooltips requested: 0";
    private static final String TOOLTIP_CLIENT_AJAX = "This tool-tip content was pre-rendered to the page.\nThe tool-tip is also following mouse around.";
    private static final String TOOLTIP_CLIENT_AJAX_DELAY = "This tool-tip content is also pre-rendered to the page.";

    @Page
    private TooltipPage page;

    @ArquillianResource
    private Actions actions;

    @Test
    public void testClientTooltip() {
        actions.moveToElement(page.getClientTooltipActivatingArea()).build().perform();
        waitForTooltipText(TOOLTIP_CLIENT_AJAX);
    }

    @Test
    public void testClientWithDelayTooltip() {
        actions.moveToElement(page.getClientWithDelayTooltipActivatingArea()).build().perform();
        waitForTooltipText(TOOLTIP_CLIENT_AJAX_DELAY);
    }

    @Test
    public void testAjaxTooltip() {
        actions.moveToElement(page.getAjaxTooltipActivatingArea()).build().perform();
        waitForTooltipText(TOOLTIP_TEXT_AJAX);
    }

    @Test
    public void testAjaxClickTooltip() {
        Graphene.guardAjax(page.getAjaxClickTooltipActivatingArea()).click();
        waitForTooltipText(TOOLTIP_TEXT_AJAX_CLICK);
    }

    private void waitForTooltipText(String message) {
        waitAjax(webDriver).withTimeout(5, TimeUnit.SECONDS)
            .until("The tool tip text is different!")
            .element(page.getTooltip())
            .text()
            .equalTo(message);
    }
}
