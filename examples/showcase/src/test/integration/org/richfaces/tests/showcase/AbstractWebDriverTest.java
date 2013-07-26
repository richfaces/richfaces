/**
 * *****************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2010-2013, Red Hat, Inc.
 * and individual contributors by the
 *
 * @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 * *****************************************************************************
 */
package org.richfaces.tests.showcase;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.interactions.Action;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc and Juraj Huska</a>
 * @version $Revision$
 */
public class AbstractWebDriverTest extends AbstractShowcaseTest {

    @Drone
    protected WebDriver webDriver;

    @Before
    public void loadPage() {
        String addition = getAdditionToContextRoot();
        this.contextRoot = getContextRoot();
        ShowcaseLayout layout = loadLayout();
        if (runInPortalEnv) {
            webDriver.get(format("{0}://{1}:{2}/{3}",
                    contextRoot.getProtocol(), contextRoot.getHost(), contextRoot.getPort(), "portal/classic/showcase"));
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            String setTextQuery = "document.querySelector(\"input[id$='portalForm:{0}']\").value = '{1}';";
            js.executeScript(format(setTextQuery, "seleniumTestDemo", getDemoName()));
            js.executeScript(format(setTextQuery, "seleniumTestSample", getSampleName()));
            js.executeScript("document.querySelector(\"a[id$='portalForm:redirectToPortlet']\").click()");
        } else {
            if (layout == ShowcaseLayout.MOBILE) {
                webDriver.get(URLUtils.buildUrl(this.contextRoot, "mobile/").toExternalForm()); // because of '#' in URLs
            }
            webDriver.get(URLUtils.buildUrl(contextRoot, addition).toExternalForm());
            if (layout == ShowcaseLayout.MOBILE) {
                Graphene.waitAjax()
                        .until()
                        .element(By.className("sourceView"))
                        .is().visible();
            }
        }
    }

    @Override
    protected URL getContextRoot() {
        URL contextRootFromParent = super.getContextRoot();
        if (webDriver instanceof AndroidDriver) {
            try {
                return new URL(contextRootFromParent.toExternalForm().replace(contextRootFromParent.getHost(), "10.0.2.2"));
            } catch (MalformedURLException ex) {
                Logger.getLogger(AbstractWebDriverTest.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } else {
            return contextRootFromParent;
        }
    }

    protected Action fireEventAction(final WebElement element, final String event) {
        return new EventAction(webDriver, element, event);
    }

    protected void fireEvent(WebElement element, String event) {
        fireEventAction(element, event).perform();
    }

    public static class EventAction implements Action {

        private final WebDriver driver;
        private final String event;
        private final WebElement element;

        public EventAction(WebDriver driver, WebElement element, String event) {
            this.driver = driver;
            this.element = element;
            this.event = event;
        }

        @Override
        public void perform() {
            String jQueryCmd = String.format("jQuery(arguments[0]).trigger('%s')", event);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(jQueryCmd, element);
        }
    }

    /**
     * Works only with injected elements.
     */
    public boolean isElementPresent(WebElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }
}
