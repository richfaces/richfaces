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
package org.richfaces.fragment.tabPanel;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.switchable.AbstractSwitchableComponent;

import com.google.common.base.Predicate;

public class RichFacesTabPanel extends AbstractSwitchableComponent<RichFacesTab> implements TabPanel<RichFacesTab> {

    @FindByJQuery(".rf-tab-hdr")
    private List<WebElement> tabHeaders;

    @FindByJQuery(".rf-tab:visible")
    private WebElement visibleContent;

    @FindByJQuery(".rf-tab-hdr-act")
    private WebElement activeHeader;

    @FindBy(className = "rf-tab-hdr-inact")
    private List<WebElement> allInactiveHeaders;

    @FindBy(className = "rf-tab-hdr-dis")
    private List<WebElement> allDisabledHeaders;

    @FindByJQuery("> div:gt(1)")
    private List<WebElement> allTabContents;

    @FindByJQuery(".rf-tab-hdr:visible")
    private List<WebElement> allVisibleHeaders;

    private final AdvancedTabPanelInteractions advancedInteractions = new AdvancedTabPanelInteractions();

    @Override
    public AdvancedTabPanelInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedTabPanelInteractions extends AbstractSwitchableComponent<RichFacesTab>.AdvancedSwitchableComponentInteractions {

        public WebElement getActiveHeaderElement() {
            return activeHeader;
        }

        public List<WebElement> getTabHeaders() {
            return getSwitcherControllerElements();
        }

        public WebElement getVisibleContent() {
            return getRootOfContainerElement();
        }

        public List<WebElement> getAllInactiveHeadersElements() {
            return Collections.unmodifiableList(allInactiveHeaders);
        }

        public List<WebElement> getAllDisabledHeadersElements() {
            return Collections.unmodifiableList(allDisabledHeaders);
        }

        public List<WebElement> getAllTabContentsElements() {
            return Collections.unmodifiableList(allTabContents);
        }

        public List<WebElement> getAllVisibleHeadersElements() {
            return Collections.unmodifiableList(allVisibleHeaders);
        }

        @Override
        protected List<WebElement> getSwitcherControllerElements() {
            return Collections.unmodifiableList(tabHeaders);
        }

        @Override
        protected WebElement getRootOfContainerElement() {
            return visibleContent;
        }

        @Override
        protected Predicate<WebDriver> getConditionForContentSwitched(final String textToContain) {
            return new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver input) {
                    return getActiveHeaderElement().getText().contains(textToContain);
                }
            };
        }
    }

    @Override
    public int getNumberOfTabs() {
        return advanced().getSwitcherControllerElements().size();
    }
}
