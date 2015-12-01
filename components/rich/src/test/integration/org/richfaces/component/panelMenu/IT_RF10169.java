/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and individual contributors
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
package org.richfaces.component.panelMenu;

import static java.text.MessageFormat.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class IT_RF10169 {

    @FindByJQuery(".rf-pm-gr:has('>.rf-pm-hdr-colps'):not('.rf-pm-gr-dis')")
    private List<Group> allCollapsedGroupsInnerNotDisabled;
    @FindByJQuery(".rf-pm-top-gr:has('>.rf-pm-hdr-colps'):not('.rf-pm-top-gr-dis'), .rf-pm-gr:has('>.rf-pm-hdr-colps'):not('.rf-pm-gr-dis')")
    private List<Group> allCollapsedGroupsNotDisabled;
    @FindByJQuery(".rf-pm-top-gr:has('>.rf-pm-hdr-colps'):not('.rf-pm-top-gr-dis')")
    private List<Group> allCollapsedGroupsTopNotDisabled;
    @FindByJQuery(".rf-pm-top-gr-dis,.rf-pm-gr-dis")
    private List<Group> allDisabledGroups;
    @FindByJQuery(value = ".rf-pm-gr-dis")
    private List<Group> allDisabledGroupsInner;
    @FindByJQuery(value = ".rf-pm-top-gr-dis")
    private List<Group> allDisabledGroupsTop;
    @FindByJQuery(value = ".rf-pm-gr:not('.rf-pm-gr-dis')")
    private List<Group> allGroupsInnerNotDisabled;
    @FindByJQuery(value = ".rf-pm-top-gr:not('.rf-pm-top-gr-dis'), .rf-pm-gr:not('.rf-pm-gr-dis')")
    private List<Group> allGroupsNotDisabled;
    @FindByJQuery(value = ".rf-pm-top-gr:not('.rf-pm-top-gr-dis')")
    private List<Group> allGroupsTopNotDisabled;
    @FindByJQuery(".rf-pm-top-itm-dis, .rf-pm-itm-dis")
    private List<Item> allItemsDisabled;
    @FindByJQuery(".rf-pm-itm-dis")
    private List<Item> allItemsInnerDisabled;
    @FindByJQuery(value = ".rf-pm-itm:not('.rf-pm-itm-dis')")
    private List<Item> allItemsInnerNotDisabled;
    @FindByJQuery(value = ".rf-pm-top-itm:not('.rf-pm-top-itm-dis'), .rf-pm-itm:not('.rf-pm-itm-dis')")
    private List<Item> allItemsNotDisabled;
    @FindByJQuery(".rf-pm-top-itm-dis")
    private List<Item> allItemsTopDisabled;
    @FindByJQuery(value = ".rf-pm-top-itm:not('.rf-pm-top-itm-dis')")
    private List<Item> allItemsTopNotDisabled;

    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return IT_RF10169_PagesGenerator.createDeployment();
    }

    private void assertClassContains(WebElement e, String toContain) {
        String klasses = e.getAttribute("class");
        try {
            assertTrue(klasses.contains(toContain));
        } catch (AssertionError ae) {
            // to not perform expensive getting of text in each assertion, code was put in catch block
            throw new AssertionError(format("Element ({0}) should contain value \"{1}\" in attribute \"class\". Had: \"{2}\"", getTextFromElement(e), toContain, klasses));
        }
    }

    private void assertClassNotContains(WebElement e, String toNotContain) {
        String klasses = e.getAttribute("class");
        try {
            assertFalse(klasses.contains(toNotContain));
        } catch (AssertionError ae) {
            // to not perform expensive getting of text in each assertion, code was put in catch block
            throw new AssertionError(format("Element ({0}) should not contain value \"{1}\" in attribute \"class\". Had: \"{2}\"", getTextFromElement(e), toNotContain, klasses));
        }
    }

    private void assertCounts(List<? extends HasIcon> all) {
        SampleProps p = new SampleProps();

        for (String att : IT_RF10169_PagesGenerator.ALL_ATTRIBUTES) {
            if (p.getSample().contains(att)) {
                String selector = p.isClassCheck()
                    ? "." + IT_RF10169_PagesGenerator.DEFAULT_CLASS
                    : "[class*=" + IT_RF10169_PagesGenerator.DEFAULT_ICON + ']';

                int count = p.isGroupClassOrItemClass()
                    ? p.isGroupCheck()
                        ? IT_RF10169_PagesGenerator.COUNT_ALL_DISABLED_GROUPS + IT_RF10169_PagesGenerator.COUNT_ALL_NOT_DISABLED_GROUPS
                        : IT_RF10169_PagesGenerator.COUNT_ALL_DISABLED_ITEMS + IT_RF10169_PagesGenerator.COUNT_ALL_NOT_DISABLED_ITEMS
                    : p.isDisabled()
                        ? p.isGroupCheck()
                            ? IT_RF10169_PagesGenerator.COUNT_ALL_DISABLED_GROUPS
                            : IT_RF10169_PagesGenerator.COUNT_ALL_DISABLED_ITEMS
                        : p.isGroupCheck()
                            ? IT_RF10169_PagesGenerator.COUNT_ALL_NOT_DISABLED_GROUPS
                            : IT_RF10169_PagesGenerator.COUNT_ALL_NOT_DISABLED_ITEMS;
                assertEquals("Count of all checked items should match with expected.", count, all.size());
                assertEquals("Count of all items found by JQuery selector (\"" + selector + "\") is not equal to expected count.",
                    p.isDisabledGroupIconCheck() ? count * 2 : count,
                    browser.findElements(ByJQuery.selector(selector)).size()
                );
                return;
            }
        }
        throw new IllegalStateException();
    }

    private void assertCounts(List<? extends HasIcon> inner, List<? extends HasIcon> top) {
        SampleProps p = new SampleProps();

        for (String att : IT_RF10169_PagesGenerator.ALL_ATTRIBUTES) {
            if (p.getSample().contains(att)) {
                String selector = p.isClassCheck()
                    ? "." + IT_RF10169_PagesGenerator.DEFAULT_CLASS
                    : "[class*=" + IT_RF10169_PagesGenerator.DEFAULT_ICON + ']';

                int count = p.isDisabled()
                    ? p.isGroupCheck()
                        ? IT_RF10169_PagesGenerator.COUNT_INNER_DISABLED_GROUPS
                        : IT_RF10169_PagesGenerator.COUNT_INNER_DISABLED_ITEMS
                    : p.isGroupCheck()
                        ? IT_RF10169_PagesGenerator.COUNT_INNER_NOT_DISABLED_GROUPS
                        : IT_RF10169_PagesGenerator.COUNT_INNER_NOT_DISABLED_ITEMS;
                count += p.isGroupClassOrItemClass()
                    ? p.isGroupCheck()
                        ? IT_RF10169_PagesGenerator.COUNT_INNER_DISABLED_GROUPS
                        : IT_RF10169_PagesGenerator.COUNT_INNER_DISABLED_ITEMS
                    : 0;
                assertEquals("Count of all inner items should match with expected.", count, inner.size());
                assertEquals("Count of all inner items found by JQuery selector (\"" + selector + "\") is not equal to expected count.",
                    p.isDisabledGroupIconCheck() ? count * 2 : count,
                    browser.findElements(ByJQuery.selector(selector)).size()
                );

                selector = p.isClassCheck()
                    ? "." + IT_RF10169_PagesGenerator.DEFAULT_TOP_CLASS
                    : "[class*=" + IT_RF10169_PagesGenerator.DEFAULT_TOP_ICON + ']';

                count = p.isDisabled()
                    ? p.isGroupCheck()
                        ? IT_RF10169_PagesGenerator.COUNT_TOP_DISABLED_GROUPS
                        : IT_RF10169_PagesGenerator.COUNT_TOP_DISABLED_ITEMS
                    : p.isGroupCheck()
                        ? IT_RF10169_PagesGenerator.COUNT_TOP_NOT_DISABLED_GROUPS
                        : IT_RF10169_PagesGenerator.COUNT_TOP_NOT_DISABLED_ITEMS;
                count += p.isGroupClassOrItemClass()
                    ? p.isGroupCheck()
                        ? IT_RF10169_PagesGenerator.COUNT_TOP_DISABLED_GROUPS
                        : IT_RF10169_PagesGenerator.COUNT_TOP_DISABLED_ITEMS
                    : 0;
                if (p.isEmptyTopClass()) {
                    assertEquals("Count of all top items should match with expected.", count, top.size());
                    assertEquals("Count of all top items found by JQuery selector (\"" + selector + "\") is not equal to expected count.",
                        0,
                        browser.findElements(ByJQuery.selector(selector)).size()
                    );
                } else {
                    assertEquals("Count of all top items should match with expected.", count, top.size());
                    assertEquals("Count of all top items found by JQuery selector (\"" + selector + "\") is not equal to expected count.",
                        p.isDisabledGroupIconCheck() ? count * 2 : count,
                        browser.findElements(p.isClassCheck()
                                ? By.className(IT_RF10169_PagesGenerator.DEFAULT_TOP_CLASS)
                                : ByJQuery.selector("[class*=" + IT_RF10169_PagesGenerator.DEFAULT_TOP_ICON + ']')
                        ).size()
                    );
                }
                return;
            }
        }
        throw new IllegalStateException();
    }

    private void checkForClass(List<? extends HasIcon> all) {
        for (HasIcon i : all) {
            assertClassContains(i.getRootElement(), IT_RF10169_PagesGenerator.DEFAULT_CLASS);
        }
        assertCounts(all);
    }

    private void checkForClassWithEmptyTopClass(List<? extends HasIcon> inner, List<? extends HasIcon> top) {
        for (HasIcon i : inner) {
            assertClassContains(i.getRootElement(), IT_RF10169_PagesGenerator.DEFAULT_CLASS);
        }
        for (HasIcon i : top) {
            assertClassNotContains(i.getRootElement(), IT_RF10169_PagesGenerator.DEFAULT_CLASS);
        }
        assertCounts(inner, top);
    }

    private void checkForClassWithTopClass(List<? extends HasIcon> inner, List<? extends HasIcon> top) {
        for (HasIcon i : inner) {
            assertClassContains(i.getRootElement(), IT_RF10169_PagesGenerator.DEFAULT_CLASS);
            assertClassNotContains(i.getRootElement(), IT_RF10169_PagesGenerator.DEFAULT_TOP_CLASS);
        }
        for (HasIcon i : top) {
            assertClassContains(i.getRootElement(), IT_RF10169_PagesGenerator.DEFAULT_TOP_CLASS);
            assertClassNotContains(i.getRootElement(), IT_RF10169_PagesGenerator.DEFAULT_CLASS);
        }
        assertCounts(inner, top);
    }

    private void checkForIcon(List<? extends HasIcon> all, boolean isRight, boolean isCollapsedOrLeftIcon) {
        WebElement element;
        Icon ic;
        for (HasIcon i : all) {
            ic = (isRight ? i.getRightIcon() : i.getLeftIcon());
            element = isCollapsedOrLeftIcon ? ic.getCollapsedElement() : ic.getExpandedElement();
            assertClassContains(element, IT_RF10169_PagesGenerator.DEFAULT_ICON);
        }
        assertCounts(all);
    }

    private void checkForIconWithEmptyTopClass(List<? extends HasIcon> inner, List<? extends HasIcon> top, boolean isRight, boolean isCollapsedOrLeftIcon) {
        WebElement element;
        Icon ic;
        for (HasIcon i : inner) {
            ic = (isRight ? i.getRightIcon() : i.getLeftIcon());
            element = isCollapsedOrLeftIcon ? ic.getCollapsedElement() : ic.getExpandedElement();
            assertClassContains(element, IT_RF10169_PagesGenerator.DEFAULT_ICON);
        }
        for (HasIcon i : top) {
            ic = (isRight ? i.getRightIcon() : i.getLeftIcon());
            element = isCollapsedOrLeftIcon ? ic.getCollapsedElement() : ic.getExpandedElement();
            assertClassNotContains(element, IT_RF10169_PagesGenerator.DEFAULT_ICON);
        }
        assertCounts(inner, top);
    }

    private void checkForIconWithTopClass(List<? extends HasIcon> inner, List<? extends HasIcon> top, boolean isRight, boolean isCollapsedOrLeftIcon) {
        WebElement element;
        Icon ic;
        for (HasIcon i : inner) {
            ic = (isRight ? i.getRightIcon() : i.getLeftIcon());
            element = isCollapsedOrLeftIcon ? ic.getCollapsedElement() : ic.getExpandedElement();
            assertClassContains(element, IT_RF10169_PagesGenerator.DEFAULT_ICON);
            assertClassNotContains(element, IT_RF10169_PagesGenerator.DEFAULT_TOP_ICON);
        }
        for (HasIcon i : top) {
            ic = (isRight ? i.getRightIcon() : i.getLeftIcon());
            element = isCollapsedOrLeftIcon ? ic.getCollapsedElement() : ic.getExpandedElement();
            assertClassContains(element, IT_RF10169_PagesGenerator.DEFAULT_TOP_ICON);
            assertClassNotContains(element, IT_RF10169_PagesGenerator.DEFAULT_ICON);
        }
        assertCounts(inner, top);
    }

    private String getTextFromElement(WebElement e) {
        String text = Utils.getTextFromHiddenElement(e.getAttribute("class").contains("ico") ? Utils.getAncestorOfElement(e, "tr") : e);
        if (text.length() > 10 && text.startsWith("Group")) {
            // trim text after Group x[.y]
            text = text.substring(0, text.indexOf("Item"));
        }
        return text;
    }

    @Test
    @InSequence(1)
    public void testGroupClass() {
        browser.get(contextPath.toExternalForm() + "groupClass.jsf");
        List<Group> allGroups = Lists.newArrayList(allDisabledGroups);
        allGroups.addAll(allGroupsNotDisabled);
        checkForClass(allGroups);
    }

    @Test
    @InSequence(2)
    public void testGroupClassWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "groupClassWithEmptyTopClass.jsf");
        List<Group> allGroupsInner = Lists.newArrayList(allDisabledGroupsInner);
        allGroupsInner.addAll(allGroupsInnerNotDisabled);
        List<Group> allGroupsTop = Lists.newArrayList(allDisabledGroupsTop);
        allGroupsTop.addAll(allGroupsTopNotDisabled);
        checkForClassWithEmptyTopClass(allGroupsInner, allGroupsTop);
    }

    @Test
    @InSequence(3)
    public void testGroupClassWithTopClass() {
        browser.get(contextPath.toExternalForm() + "groupClassWithTopClass.jsf");
        List<Group> allGroupsInner = Lists.newArrayList(allDisabledGroupsInner);
        allGroupsInner.addAll(allGroupsInnerNotDisabled);
        List<Group> allGroupsTop = Lists.newArrayList(allDisabledGroupsTop);
        allGroupsTop.addAll(allGroupsTopNotDisabled);
        checkForClassWithTopClass(allGroupsInner, allGroupsTop);
    }

    @Test
    @InSequence(4)
    public void testGroupCollapsedLeftIcon() {
        browser.get(contextPath.toExternalForm() + "groupCollapsedLeftIcon.jsf");
        checkForIcon(allCollapsedGroupsNotDisabled, false, true);
    }

    @Test
    @InSequence(5)
    public void testGroupCollapsedLeftIconWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "groupCollapsedLeftIconWithEmptyTopClass.jsf");
        checkForIconWithEmptyTopClass(allCollapsedGroupsInnerNotDisabled, allCollapsedGroupsTopNotDisabled, false, true);
    }

    @Test
    @InSequence(6)
    public void testGroupCollapsedLeftIconWithTopClass() {
        browser.get(contextPath.toExternalForm() + "groupCollapsedLeftIconWithTopClass.jsf");
        checkForIconWithTopClass(allCollapsedGroupsInnerNotDisabled, allCollapsedGroupsTopNotDisabled, false, true);
    }

    @Test
    @InSequence(7)
    public void testGroupCollapsedRightIcon() {
        browser.get(contextPath.toExternalForm() + "groupCollapsedRightIcon.jsf");
        checkForIcon(allCollapsedGroupsNotDisabled, true, true);
    }

    @Test
    @InSequence(8)
    public void testGroupCollapsedRightIconWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "groupCollapsedRightIconWithEmptyTopClass.jsf");
        checkForIconWithEmptyTopClass(allCollapsedGroupsInnerNotDisabled, allCollapsedGroupsTopNotDisabled, true, true);
    }

    @Test
    @InSequence(9)
    public void testGroupCollapsedRightIconWithTopClass() {
        browser.get(contextPath.toExternalForm() + "groupCollapsedRightIconWithTopClass.jsf");
        checkForIconWithTopClass(allCollapsedGroupsInnerNotDisabled, allCollapsedGroupsTopNotDisabled, true, true);
    }

    @Test
    @InSequence(10)
    public void testGroupDisabledClass() {
        browser.get(contextPath.toExternalForm() + "groupDisabledClass.jsf");
        checkForClass(allDisabledGroups);
    }

    @Test
    @InSequence(11)
    public void testGroupDisabledClassWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "groupDisabledClassWithEmptyTopClass.jsf");
        checkForClassWithEmptyTopClass(allDisabledGroupsInner, allDisabledGroupsTop);
    }

    @Test
    @InSequence(12)
    public void testGroupDisabledClassWithTopClass() {
        browser.get(contextPath.toExternalForm() + "groupDisabledClassWithTopClass.jsf");
        checkForClassWithTopClass(allDisabledGroupsInner, allDisabledGroupsTop);
    }

    @Test
    @InSequence(13)
    public void testGroupDisabledLeftIcon() {
        browser.get(contextPath.toExternalForm() + "groupDisabledLeftIcon.jsf");
        checkForIcon(allDisabledGroups, false, true);
    }

    @Test
    @InSequence(14)
    public void testGroupDisabledLeftIconWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "groupDisabledLeftIconWithEmptyTopClass.jsf");
        checkForIconWithEmptyTopClass(allDisabledGroupsInner, allDisabledGroupsTop, false, true);
    }

    @Test
    @InSequence(15)
    public void testGroupDisabledLeftIconWithTopClass() {
        browser.get(contextPath.toExternalForm() + "groupDisabledLeftIconWithTopClass.jsf");
        checkForIconWithTopClass(allDisabledGroupsInner, allDisabledGroupsTop, false, true);
    }

    @Test
    @InSequence(16)
    public void testGroupDisabledRightIcon() {
        browser.get(contextPath.toExternalForm() + "groupDisabledRightIcon.jsf");
        checkForIcon(allDisabledGroups, true, true);
    }

    @Test
    @InSequence(17)
    public void testGroupDisabledRightIconWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "groupDisabledRightIconWithEmptyTopClass.jsf");
        checkForIconWithEmptyTopClass(allDisabledGroupsInner, allDisabledGroupsTop, true, true);
    }

    @Test
    @InSequence(18)
    public void testGroupDisabledRightIconWithTopClass() {
        browser.get(contextPath.toExternalForm() + "groupDisabledRightIconWithTopClass.jsf");
        checkForIconWithTopClass(allDisabledGroupsInner, allDisabledGroupsTop, true, true);
    }

    @Test
    @InSequence(19)
    public void testGroupExpandedLeftIcon() {
        browser.get(contextPath.toExternalForm() + "groupExpandedLeftIcon.jsf");
        checkForIcon(allGroupsNotDisabled, false, false);
    }

    @Test
    @InSequence(20)
    public void testGroupExpandedLeftIconWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "groupExpandedLeftIconWithEmptyTopClass.jsf");
        checkForIconWithEmptyTopClass(allGroupsInnerNotDisabled, allGroupsTopNotDisabled, false, false);
    }

    @Test
    @InSequence(21)
    public void testGroupExpandedLeftIconWithTopClass() {
        browser.get(contextPath.toExternalForm() + "groupExpandedLeftIconWithTopClass.jsf");
        checkForIconWithTopClass(allGroupsInnerNotDisabled, allGroupsTopNotDisabled, false, false);
    }

    @Test
    @InSequence(22)
    public void testGroupExpandedRightIcon() {
        browser.get(contextPath.toExternalForm() + "groupExpandedRightIcon.jsf");
        checkForIcon(allGroupsNotDisabled, true, false);
    }

    @Test
    @InSequence(23)
    public void testGroupExpandedRightIconWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "groupExpandedRightIconWithEmptyTopClass.jsf");
        checkForIconWithEmptyTopClass(allGroupsInnerNotDisabled, allGroupsTopNotDisabled, true, false);
    }

    @Test
    @InSequence(24)
    public void testGroupExpandedRightIconWithTopClass() {
        browser.get(contextPath.toExternalForm() + "groupExpandedRightIconWithTopClass.jsf");
        checkForIconWithTopClass(allGroupsInnerNotDisabled, allGroupsTopNotDisabled, true, false);
    }

    @Test
    @InSequence(25)
    public void testItemClass() {
        browser.get(contextPath.toExternalForm() + "itemClass.jsf");
        List<Item> allItems = Lists.newArrayList(allItemsDisabled);
        allItems.addAll(allItemsNotDisabled);
        checkForClass(allItems);
    }

    @Test
    @InSequence(26)
    public void testItemClassWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "itemClassWithEmptyTopClass.jsf");
        List<Item> allItemsInner = Lists.newArrayList(allItemsInnerDisabled);
        allItemsInner.addAll(allItemsInnerNotDisabled);
        List<Item> allItemsTop = Lists.newArrayList(allItemsTopDisabled);
        allItemsTop.addAll(allItemsTopNotDisabled);
        checkForClassWithEmptyTopClass(allItemsInner, allItemsTop);
    }

    @Test
    @InSequence(27)
    public void testItemClassWithTopClass() {
        browser.get(contextPath.toExternalForm() + "itemClassWithTopClass.jsf");
        List<Item> allItemsInner = Lists.newArrayList(allItemsInnerDisabled);
        allItemsInner.addAll(allItemsInnerNotDisabled);
        List<Item> allItemsTop = Lists.newArrayList(allItemsTopDisabled);
        allItemsTop.addAll(allItemsTopNotDisabled);
        checkForClassWithTopClass(allItemsInner, allItemsTop);
    }

    @Test
    @InSequence(28)
    public void testItemDisabledClass() {
        browser.get(contextPath.toExternalForm() + "itemDisabledClass.jsf");
        checkForClass(allItemsDisabled);
    }

    @Test
    @InSequence(29)
    public void testItemDisabledClassWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "itemDisabledClassWithEmptyTopClass.jsf");
        checkForClassWithEmptyTopClass(allItemsInnerDisabled, allItemsTopDisabled);
    }

    @Test
    @InSequence(30)
    public void testItemDisabledClassWithTopClass() {
        browser.get(contextPath.toExternalForm() + "itemDisabledClassWithTopClass.jsf");
        checkForClassWithTopClass(allItemsInnerDisabled, allItemsTopDisabled);
    }

    @Test
    @InSequence(31)
    public void testItemDisabledLeftIcon() {
        browser.get(contextPath.toExternalForm() + "itemDisabledLeftIcon.jsf");
        checkForIcon(allItemsDisabled, false, true);
    }

    @Test
    @InSequence(32)
    public void testItemDisabledLeftIconWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "itemDisabledLeftIconWithEmptyTopClass.jsf");
        checkForIconWithEmptyTopClass(allItemsInnerDisabled, allItemsTopDisabled, false, true);
    }

    @Test
    @InSequence(33)
    public void testItemDisabledLeftIconWithTopClass() {
        browser.get(contextPath.toExternalForm() + "itemDisabledLeftIconWithTopClass.jsf");
        checkForIconWithTopClass(allItemsInnerDisabled, allItemsTopDisabled, false, true);
    }

    @Test
    @InSequence(34)
    public void testItemDisabledRightIcon() {
        browser.get(contextPath.toExternalForm() + "itemDisabledRightIcon.jsf");
        checkForIcon(allItemsDisabled, true, false);
    }

    @Test
    @InSequence(35)
    public void testItemDisabledRightIconWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "itemDisabledRightIconWithEmptyTopClass.jsf");
        checkForIconWithEmptyTopClass(allItemsInnerDisabled, allItemsTopDisabled, true, false);
    }

    @Test
    @InSequence(36)
    public void testItemDisabledRightIconWithTopClass() {
        browser.get(contextPath.toExternalForm() + "itemDisabledRightIconWithTopClass.jsf");
        checkForIconWithTopClass(allItemsInnerDisabled, allItemsTopDisabled, true, false);
    }

    @Test
    @InSequence(37)
    public void testItemLeftIcon() {
        browser.get(contextPath.toExternalForm() + "itemLeftIcon.jsf");
        checkForIcon(allItemsNotDisabled, false, true);
    }

    @Test
    @InSequence(38)
    public void testItemLeftIconWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "itemLeftIconWithEmptyTopClass.jsf");
        checkForIconWithEmptyTopClass(allItemsInnerNotDisabled, allItemsTopNotDisabled, false, true);
    }

    @Test
    @InSequence(39)
    public void testItemLeftIconWithTopClass() {
        browser.get(contextPath.toExternalForm() + "itemLeftIconWithTopClass.jsf");
        checkForIconWithTopClass(allItemsInnerNotDisabled, allItemsTopNotDisabled, false, true);
    }

    @Test
    @InSequence(40)
    public void testItemRightIcon() {
        browser.get(contextPath.toExternalForm() + "itemRightIcon.jsf");
        checkForIcon(allItemsNotDisabled, true, false);
    }

    @Test
    @InSequence(41)
    public void testItemRightIconWithEmptyTopClass() {
        browser.get(contextPath.toExternalForm() + "itemRightIconWithEmptyTopClass.jsf");
        checkForIconWithEmptyTopClass(allItemsInnerNotDisabled, allItemsTopNotDisabled, true, false);
    }

    @Test
    @InSequence(42)
    public void testItemRightIconWithTopClass() {
        browser.get(contextPath.toExternalForm() + "itemRightIconWithTopClass.jsf");
        checkForIconWithTopClass(allItemsInnerNotDisabled, allItemsTopNotDisabled, true, false);
    }

    public static interface HasIcon {

        public Icon getLeftIcon();

        public Icon getRightIcon();

        public WebElement getRootElement();
    }

    public static interface Icon {

        WebElement getCollapsedElement();

        WebElement getExpandedElement();
    }

    public static class Group implements HasIcon {

        @FindByJQuery(">div>table>tbody>tr>td[class*=gr-ico]")
        private GroupIcon leftIcon;
        @FindByJQuery(">div>table>tbody>tr>td[class*=gr-exp-ico]")
        private GroupIcon rightIcon;
        @Root
        private WebElement rootElement;

        public Icon getLeftIcon() {
            return leftIcon;
        }

        public Icon getRightIcon() {
            return rightIcon;
        }

        public WebElement getRootElement() {
            return rootElement;
        }
    }

    public static class GroupIcon implements Icon {

        @FindBy(css = ".rf-pm-ico-colps")
        private WebElement collapsedElement;
        @FindBy(css = ".rf-pm-ico-exp")
        private WebElement expandedElement;

        public WebElement getCollapsedElement() {
            return collapsedElement;
        }

        public WebElement getExpandedElement() {
            return expandedElement;
        }
    }

    public static class Item implements HasIcon {

        @FindByJQuery(">table>tbody>tr")
        private ItemIcon leftIcon;
        @FindByJQuery(">table>tbody>tr")
        private ItemIcon rightIcon;
        @Root
        private WebElement rootElement;

        public Icon getLeftIcon() {
            return leftIcon;
        }

        public Icon getRightIcon() {
            return rightIcon;
        }

        public WebElement getRootElement() {
            return rootElement;
        }
    }

    public static class ItemIcon implements Icon {

        @FindByJQuery("[class*=-itm-ico]")
        private WebElement collapsedElement;
        @FindByJQuery("[class*=-itm-exp-ico]")
        private WebElement expandedElement;

        public WebElement getCollapsedElement() {
            return collapsedElement;
        }

        public WebElement getExpandedElement() {
            return expandedElement;
        }
    }

    /**
     * Class for storing various properties of currently browsed (IT_RF10169) page.
     */
    private class SampleProps {

        private static final String NAME = "IT_RF10169/";

        private final boolean isClassCheck;
        private final boolean isDisabled;
        private final boolean isDisabledGroupIconCheck;
        private final boolean isEmptyTopClass;
        private final boolean isGroupCheck;
        private final boolean isGroupClassOrItemClass;
        private final String sample;

        private SampleProps() {
            sample = getSampleNameFromCurrentUrl();
            isEmptyTopClass = sample.endsWith("EmptyTopClass");
            isClassCheck = !sample.contains("Icon");
            isDisabled = sample.contains("Disabled");
            isGroupCheck = sample.toLowerCase().contains("group");
            isDisabledGroupIconCheck = isDisabled && isGroupCheck && !isClassCheck;
            isGroupClassOrItemClass = sample.startsWith("groupClass") || sample.startsWith("itemClass");
        }

        public String getSample() {
            return sample;
        }

        private String getSampleNameFromCurrentUrl() {
            String currentUrl = browser.getCurrentUrl().replace(".jsf", "");
            return currentUrl.substring(currentUrl.indexOf(NAME) + NAME.length());
        }

        public boolean isClassCheck() {
            return isClassCheck;
        }

        public boolean isDisabled() {
            return isDisabled;
        }

        public boolean isDisabledGroupIconCheck() {
            return isDisabledGroupIconCheck;
        }

        public boolean isEmptyTopClass() {
            return isEmptyTopClass;
        }

        public boolean isGroupCheck() {
            return isGroupCheck;
        }

        public boolean isGroupClassOrItemClass() {
            return isGroupClassOrItemClass;
        }
    }
}
