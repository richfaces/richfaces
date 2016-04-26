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
package org.richfaces.fragment.common;

import java.util.Iterator;
import java.util.List;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.WebElementUtils;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public final class Utils {

    public static final By BY_BODY = By.tagName("body");
    public static final By BY_HTML = By.tagName("html");

    public static JavascriptExecutor getExecutorFromElement(WebElement element) {
        Preconditions.checkNotNull(element, "The element cannot be null.");
        if (element instanceof GrapheneProxyInstance) {
            GrapheneContext context = ((GrapheneProxyInstance) element).getGrapheneContext();
            return (JavascriptExecutor) context.getWebDriver(JavascriptExecutor.class);
        }
        throw new RuntimeException(
            "Cannot obtain JavascriptExecutor from element which is not an instance of GrapheneProxyInstance.");
    }

    /**
     * Returns the result of invocation of jQuery function <code>index()</code> on given element.
     *
     * @param element element on which the command will be executed, has to be instance of
     * org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance
     */
    public static int getIndexOfElement(WebElement element) {
        return Integer.valueOf(returningJQ(getExecutorFromElement(element), "index()", element));
    }

    /**
     * Returns the closest ancestor from the element.
     *
     * @param element element from which will be the ancestor found
     */
    public static WebElement getAncestorOfElement(WebElement element) {
        return getAncestorOfElement(element, "*");
    }

    /**
     * Returns the closest ancestor with tagName from the element.
     *
     * @param element element from which will be the ancestor found
     * @param tagNameOfAncestorElement tagName of the ancestor to be found
     */
    public static WebElement getAncestorOfElement(WebElement element, String tagNameOfAncestorElement) {
        return getXpathLocatedElement(element, tagNameOfAncestorElement, "ancestor", false);
    }

    /**
     * Returns the closest following sibling from the element.
     *
     * @param element element from which will be the sibling found
     */
    public static WebElement getNextSiblingOfElement(WebElement element) {
        return getNextSiblingOfElement(element, "*");
    }

    /**
     * Returns the closest following sibling with tagName from the element.
     *
     * @param element element from which will be the sibling found
     * @param tagNameofSibling tagName of the sibling to be found
     */
    public static WebElement getNextSiblingOfElement(WebElement element, String tagNameofSibling) {
        return getXpathLocatedElement(element, tagNameofSibling, "following-sibling", true);
    }

    /**
     * Returns the closest preceding sibling from the element.
     *
     * @param element element from which will be the sibling found
     */
    public static WebElement getPreviousSiblingOfElement(WebElement element) {
        return getPreviousSiblingOfElement(element, "*");
    }

    /**
     * Returns the closest preceding sibling with tagName from the element.
     *
     * @param element element from which will be the sibling found
     * @param tagNameofSibling tagName of the sibling to be found
     */
    public static WebElement getPreviousSiblingOfElement(WebElement element, String tagNameofSibling) {
        return getXpathLocatedElement(element, tagNameofSibling, "preceding-sibling", false);
    }

    private static WebElement getXpathLocatedElement(WebElement fromElement, String tagnameOfSearchedElement, String searchFunction, boolean isFirst) {
        List<WebElement> elements = WebElementUtils.findElementsLazily(By.xpath(String.format("./%s::%s", searchFunction, tagnameOfSearchedElement)), fromElement);
        return elements.get(isFirst ? 0 : elements.size() - 1);
    }

    /**
     * Returns the given option of the component determined by its root element.
     *
     * For example <tt>getComponentOption(rootOfHotKey, "key")</tt> will return concrete value of the hotkey's option
     * <tt>key</tt>
     *
     * @param rootOfComponent
     * @param option
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getComponentOption(WebElement rootOfComponent, String option) {
        return Optional.of((T) invokeRichFacesJSAPIFunction(rootOfComponent, "options." + option));
    }

    /**
     * Returns the given option of the component determined by its root element in a document object safe way.
     *
     * It provides a safe way to retrieve an option if the option value can be equal to the JavaScript object <tt>document</tt>.
     *
     * @param rootOfComponent
     * @param option
     * @return actual component option value, or null if it is equal to document object
     */
    public static <T> Optional<T> getComponentOptionDocumentObjectSafe(WebElement rootOfComponent, String option) {
        if (!(Boolean) invokeRichFacesJSAPIFunction(rootOfComponent, "options." + option + " == document")) {
            return getComponentOption(rootOfComponent, option);
        } else {
            return Optional.fromNullable(null);
        }
    }

    /**
     * Returns Locations of input element.
     *
     * @see Locations
     */
    public static Locations getLocations(WebElement root) {
        Preconditions.checkNotNull(root, "The element cannot be null.");
        Point topLeft = root.getLocation();
        Dimension dimension = root.getSize();
        Point topRight = topLeft.moveBy(dimension.getWidth(), 0);
        Point bottomRight = topRight.moveBy(0, dimension.getHeight());
        Point bottomLeft = topLeft.moveBy(0, dimension.getHeight());
        return new Locations(topLeft, topRight, bottomLeft, bottomRight);
    }

    /**
     * Returns text from given hidden element. WebDriver, in this case, returns empty String.
     *
     * @param element not visible element
     */
    public static String getTextFromHiddenElement(JavascriptExecutor executor, WebElement element) {
        return returningJQ(executor, "text()", element);
    }

    /**
     * Returns text from given hidden element. WebDriver, in this case, returns empty String.
     *
     * @param element not visible element
     */
    public static String getTextFromHiddenElement(WebElement element) {
        return getTextFromHiddenElement(getExecutorFromElement(element), element);
    }

    /**
     * Invokes RF JS API function on given root of RF component. The root must be set correctly.
     *
     * @param componentRoot root of the RF component
     * @param functionWithParams JS API function with params (e.g. <code>setValue(new Date('October 10, 2012 12:00:00'))</code>
     * )
     */
    public static <T> T invokeRichFacesJSAPIFunction(WebElement componentRoot, String functionWithParams) {
        return (T) getExecutorFromElement(componentRoot).executeScript(
            "return RichFaces.component(arguments[0])." + functionWithParams, componentRoot);
    }

    public static boolean isVisible(WebElement e) {
        try {
            boolean result = false;
            result = e.isDisplayed();
            return result;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public static boolean isVisible(SearchContext searchContext, By by) {
        try {
            boolean result = false;
            result = searchContext.findElement(by).isDisplayed();
            return result;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    /**
     * Executes jQuery command on input element. E.g. to trigger click use jQ("click()", element).
     *
     * @param cmd command to be executed
     * @param element element on which the command will be executed
     */
    public static void jQ(JavascriptExecutor executor, String cmd, WebElement element) {
        Preconditions.checkNotNull(executor, "The executor cannot be null.");
        Preconditions.checkNotNull(cmd, "The command cannot be null.");
        Preconditions.checkNotNull(element, "The element cannot be null.");
        String jQueryCmd = String.format("jQuery(arguments[0]).%s", cmd);
        executor.executeScript(jQueryCmd, unwrap(element));
    }

    /**
     * Executes jQuery command on input element. E.g. to trigger click use jQ("click()", element).
     *
     * @param cmd command to be executed
     * @param element element on which the command will be executed
     */
    public static void jQ(String cmd, WebElement element) {
        jQ(getExecutorFromElement(element), cmd, element);
    }

    /**
     * When using Firefox and body element is focused, focus on the page from url/address bar, otherwise nothing happens.
     * Workaround for Selenium issue
     * <a href="https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/7937">#7937</a>
     */
    public static void performFirefoxKeyboardWorkaround(WebDriver browser) {
        if (browser instanceof FirefoxDriver) {
            FirefoxKeyboardWorkaround fkw = JSInterfaceFactory.create(GrapheneContext.lastContext(), FirefoxKeyboardWorkaround.class);
            // is body element focused and workaround was not invoked?
            if (!fkw.isInvoked() && fkw.getActiveElementTagName().equalsIgnoreCase("body")) {
                // insert the button element
                fkw.insertButtonWorkaround();
                // click on the inserted button to focus on the page and remove the button from DOM (after click)
                fkw.getButtonElement().click();
            }
        }
    }

    /**
     * Creates and inserts a new button to DOM, clicks on it and removes it.
     *
     * @param browser
     */
    public static void performUniversalBlur(WebDriver browser) {
        // reuse FirefoxKeyboardWorkaround
        FirefoxKeyboardWorkaround fkw = JSInterfaceFactory.create(GrapheneContext.lastContext(), FirefoxKeyboardWorkaround.class);
        fkw.insertButtonWorkaround();
        new Actions(browser).moveToElement(fkw.getButtonElement()).click().perform();
    }

    /**
     * Executes returning jQuery command on input element. E.g. to get a position of element from top of the page use
     * returningjQ("position().top", element).
     *
     * @param cmd command to be executed
     * @param element element on which the command will be executed
     */
    public static String returningJQ(JavascriptExecutor executor, String cmd, WebElement element) {
        Preconditions.checkNotNull(executor, "The executor cannot be null.");
        Preconditions.checkNotNull(cmd, "The command cannot be null.");
        Preconditions.checkNotNull(element, "The element cannot be null.");
        String jQueryCmd = String.format("return jQuery(arguments[0]).%s;", cmd);
        return String.valueOf(executor.executeScript(jQueryCmd, unwrap(element)));
    }

    /**
     * Executes returning jQuery command on input element. E.g. to get a position of element from top of the page use
     * returningjQ("position().top", element).
     *
     * @param cmd command to be executed
     * @param element element on which the command will be executed
     */
    public static String returningJQ(String cmd, WebElement element) {
        return returningJQ(getExecutorFromElement(element), cmd, element);
    }

    private static boolean _tolerantAssertPointEquals(Point p1, Point p2, int xTolerance, int yTolerance) {
        return (Math.abs(p1.x - p2.x) <= xTolerance && Math.abs(p1.y - p2.y) <= yTolerance);
    }

    /**
     * Asserts that two points are equal with some allowed tolerance.
     */
    public static void tolerantAssertPointEquals(Point p1, Point p2, int xTolerance, int yTolerance, String message) {
        if (!_tolerantAssertPointEquals(p1, p2, xTolerance, yTolerance)) {
            throw new AssertionError("The points are not equal or not in tolerance.\n" + " The tolerance for x axis was: "
                + xTolerance + ". The tolerance for y axis was: " + yTolerance + ".\n" + "First point: " + p1 + "\n"
                + "Second point: " + p2 + ".\n" + message);
        }
    }

    /**
     * Asserts that two locations are equal with some allowed tolerance.
     */
    public static void tolerantAssertLocationsEquals(Locations l1, Locations l2, int xTolerance, int yTolerance, String message) {
        Iterator<Point> it1 = l1.iterator();
        Iterator<Point> it2 = l2.iterator();
        Point p1, p2;
        while (it1.hasNext()) {
            p1 = it1.next();
            p2 = it2.next();
            if (!_tolerantAssertPointEquals(p1, p2, xTolerance, yTolerance)) {
                throw new AssertionError("The locations are not equal or are not in tolerance.\n" + "First location: " + l1
                    + ".\n" + "Second location: " + l2 + ".\n" + "Diverging point: " + p1 + " (first), " + p2 + " (second).\n"
                    + message);
            }
        }
    }

    /**
     * Asserts that elements locations and some other locations are equal with some allowed tolerance.
     */
    public static void tolerantAssertLocationsEquals(WebElement element, Locations l2, int xTolerance, int yTolerance,
        String message) {
        tolerantAssertLocationsEquals(Utils.getLocations(element), l2, xTolerance, yTolerance, message);
    }

    /**
     * Executes jQuery trigger command on input element. Useful for easy triggering of JavaScript events like click, dblclick,
     * mouseout...
     *
     * @param executor JavascriptExecutor
     * @param event event to be triggered
     * @param element element on which the command will be executed
     */
    public static void triggerJQ(JavascriptExecutor executor, String event, WebElement element) {
        jQ(executor, String.format("trigger('%s')", event), element);
    }

    /**
     * Executes jQuery trigger command on input element. Useful for easy triggering of JavaScript events like click, dblclick,
     * mouseout...
     *
     * @param event event to be triggered
     * @param element element on which the command will be executed, has to be instance of
     * org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance
     */
    public static void triggerJQ(String event, WebElement element) {
        triggerJQ(getExecutorFromElement(element), event, element);
    }

    public static WebElement unwrap(WebElement element) {
        Preconditions.checkNotNull(element, "The element cannot be null.");
        WebElement result = element;
        while (GrapheneProxy.isProxyInstance(result)) {
            result = ((GrapheneProxyInstance) result).unwrap();
        }
        return result;
    }

    /**
     * Returns the default timeout for GUI waiting in milliseconds.
     *
     * @param browser
     * @return
     */
    public static long getWaitGUIDefaultTimeout(WebDriver browser) {
        return 1000 * ((GrapheneProxyInstance) browser).getGrapheneContext().getConfiguration().getWaitGuiInterval();
    }

    /**
     * Returns the default timeout for Ajax waiting in milliseconds.
     *
     * @param browser
     * @return
     */
    public static long getWaitAjaxDefaultTimeout(WebDriver browser) {
        return 1000 * ((GrapheneProxyInstance) browser).getGrapheneContext().getConfiguration().getWaitAjaxInterval();
    }

}
