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
package org.richfaces.fragment.collapsibleSubTableToggler;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.Event;
import org.richfaces.fragment.common.Utils;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesCollapsibleSubTableToggler {

    @Root
    private WebElement root;

    @ArquillianResource
    private WebDriver browser;

    @FindByJQuery(value = ".rf-csttg > span:visible")
    private WebElement elementWithStyleClass;
    @FindByJQuery(value = ".rf-csttg > span:visible > img")
    private WebElement image;
    @FindByJQuery(value = ".rf-csttg > span:visible > a.rf-csttg-lnk")
    private WebElement label;

    private static final Event DEFAULT_EVENT = Event.CLICK;
    private Event toggleEvent = DEFAULT_EVENT;

    private static final boolean DEFAULT_TOGGLE_BY = true;
    private boolean isToggleByImage = DEFAULT_TOGGLE_BY;

    public WebElement getRoot() {
        return root;
    }

    public WebElement getVisibleImage() {
        return image;
    }

    public WebElement getVisibleLabel() {
        return label;
    }

    public boolean isExpanded() {
        return getElementWithStyleClass().getAttribute("class").equals("rf-csttg-exp");
    }

    public boolean isVisible() {
        return Utils.isVisible(getElementWithStyleClass());
    }

    public void setToggleBy() {
        isToggleByImage = DEFAULT_TOGGLE_BY;
    }

    public void setToggleByImage() {
        isToggleByImage = true;
    }

    public void setToggleByLabel() {
        isToggleByImage = false;
    }

    public void setToggleEvent() {
        toggleEvent = DEFAULT_EVENT;
    }

    public void setToggleEvent(Event e) {
        toggleEvent = e;
    }

    public void toggle() {
        new Actions(browser).triggerEventByJS(toggleEvent, isToggleByImage ? getVisibleImage() : getVisibleLabel()).perform();
    }

    protected WebElement getElementWithStyleClass() {
        return elementWithStyleClass;
    }
}
