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
package org.richfaces.showcase.notify;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.notify.NotifyMessage;
import org.richfaces.showcase.AbstractWebDriverTest;
import org.richfaces.showcase.notify.page.NotifyAttributesPage;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @version $Revision$
 */
public class ITestNotifyAttributes extends AbstractWebDriverTest {

    @Page
    private NotifyAttributesPage page;

    @ArquillianResource
    private Actions actions;

    @Test
    public void testStayTime() {
        checkStayTime(3500);
        checkStayTime(500);
        checkStayTime(1500);
    }

    @Test
    public void testSticky() {
        page.waitUntilThereIsNoNotify();
        page.setSticky(true);
        page.showNotification();

        try {
            page.waitUntilThereIsNoNotify();
            fail("The notify message should not dissapear until closed manually, when @sticky=true.");
        } catch (RuntimeException ex) {
        }

        page.getNotify().getItem(0).close();
        page.waitUntilThereIsNoNotify();
    }

    @Test
    public void testNonBlockingOpacity() {
        checkNonBlockingOpacity(0.5);
        checkNonBlockingOpacity(0);
        checkNonBlockingOpacity(0.9);
    }

    @Test
    public void testShowShadow() {
        page.waitUntilThereIsNoNotify();
        page.setShowShadow(true);
        page.showNotification();

        assertTrue("The shadow should be presented!", isElementPresent(page.getNotify().getItem(0).advanced()
            .getShadowElement()));

        page.waitUntilThereIsNoNotify();

        page.setShowShadow(false);
        page.showNotification();

        assertFalse("The shadow should not be presented!", isElementPresent(page.getNotify().getItem(0).advanced()
            .getShadowElement()));
    }

    @Test
    public void testShowCloseButton() {
        page.waitUntilThereIsNoNotify();
        page.setShowCloseButton(true);
        page.showNotification();

        NotifyMessage message = page.getNotify().getItem(0);

        actions.moveToElement(message.advanced().getSummaryElement()).perform();
        assertTrue("The close button should be visible!", message.advanced().getCloseIconElement().isDisplayed());
        page.waitUntilThereIsNoNotify();

        page.setShowCloseButton(false);

        page.showNotification();

        message = page.getNotify().getItem(0);

        actions.moveToElement(message.advanced().getSummaryElement()).perform();
        assertFalse("The close button should not be visible!", message.advanced().getCloseIconElement().isDisplayed());
    }

    private void checkNonBlockingOpacity(final double opacity) {
        loadPage();
        page.setNonBlockingOpacity(opacity);
        page.waitUntilThereIsNoNotify();
        page.showNotification();

        final NotifyMessage message = page.getNotify().getItem(0);
        actions.moveToElement(message.advanced().getSummaryElement()).perform();

        Graphene.waitAjax().withMessage("The notify should has opacity " + opacity + ".")
            .pollingEvery(50, TimeUnit.MILLISECONDS).until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver input) {
                    double actualOpacity = Double.valueOf(message.advanced().getRootElement().getCssValue("opacity"));
                    boolean succcess = Math.abs(opacity - actualOpacity) <= 0.2;
                    if (!succcess) {
                        Utils.triggerJQ("mouseover", message.advanced().getSummaryElement());
                    }
                    return succcess;
                }
            });
    }

    private void checkStayTime(long stayTime) {
        page.setStayTime(stayTime);
        page.waitUntilThereIsNoNotify();
        page.showNotification();
        long timeWhenNotifyIsRendered = System.currentTimeMillis();
        page.waitUntilThereIsNoNotify();
        long timeWhenNotifyDisappeared = System.currentTimeMillis();
        long delta = timeWhenNotifyDisappeared - timeWhenNotifyIsRendered;

        // the time should be measured when the notify started to disappear,
        // however
        // it is measured from the time it fully disappears, therefore there is
        // added delay
        long moreThan = stayTime;
        long lessThan = stayTime + NotifyAttributesPage.NOTIFY_DISAPPEAR_DELAY;

        assertTrue("The notify message should stay on the screen more than: " + moreThan + " and less than: " + lessThan
            + " milisec, but was: " + delta + " milisec", (delta > moreThan) && (delta < lessThan));
    }

}
