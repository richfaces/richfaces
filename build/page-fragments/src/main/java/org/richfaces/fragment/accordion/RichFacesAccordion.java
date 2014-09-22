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
package org.richfaces.fragment.accordion;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.switchable.AbstractSwitchableComponent;

import com.google.common.base.Predicate;

public class RichFacesAccordion extends AbstractSwitchableComponent<RichFacesAccordionItem> implements Accordion {

    @FindBy(className = "rf-ac-itm-hdr")
    private List<WebElement> accordionHeaders;

    @FindBy(className = "rf-ac-itm")
    private List<RichFacesAccordionItem> accordionItems;

    @FindByJQuery(".rf-ac-itm-cnt:visible")
    private WebElement visibleContent;

    private final AdvancedAccordionInteractions advancedInteractions = new AdvancedAccordionInteractions();

    @Override
    public AdvancedAccordionInteractions advanced() {
        return advancedInteractions;
    }

    @Override
    public int getNumberOfAccordionItems() {
        return advanced().getAccordionItems().size();
    }

    public class AdvancedAccordionInteractions extends AbstractSwitchableComponent<RichFacesAccordionItem>.AdvancedSwitchableComponentInteractions {

        public List<RichFacesAccordionItem> getAccordionItems() {
            return Collections.unmodifiableList(accordionItems);
        }

        public RichFacesAccordionItem getActiveItem() {
            for (RichFacesAccordionItem item : getAccordionItems()) {
                if (item.advanced().isActive()) {
                    return item;
                }
            }
            return null;
        }

        @Override
        protected Predicate<WebDriver> getConditionForContentSwitched(final String textToContain) {
            return new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver input) {
                    return getActiveItem().advanced().getActiveHeaderElement().getText().contains(textToContain);
                }
            };
        }

        public List<WebElement> getAccordionHeaders() {
            return accordionHeaders;
        }

        @Override
        protected List<WebElement> getSwitcherControllerElements() {
            return getAccordionHeaders();
        }

        @Override
        protected WebElement getRootOfContainerElement() {
            return visibleContent;
        }
    }
}
