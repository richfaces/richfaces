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

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.switchable.AbstractComponentContainer;

public class RichFacesAccordionItem extends AbstractComponentContainer implements AdvancedInteractions<RichFacesAccordionItem.AdvancedAccordionItemInteractions> {

    @FindBy(className = "rf-ac-itm-lbl-act")
    private GrapheneElement activeHeader;

    @FindBy(className = "rf-ac-itm-lbl-dis")
    private GrapheneElement disabledHeader;

    @FindBy(className = "rf-ac-itm-lbl-inact")
    private GrapheneElement inactiveHeader;

    @FindBy(className = "rf-ac-itm-cnt")
    private GrapheneElement content;

    @FindBy(className = "rf-ac-itm-hdr")
    private GrapheneElement toActivate;

    private final AdvancedAccordionItemInteractions advancedInteractions = new AdvancedAccordionItemInteractions();

    @Override
    public AdvancedAccordionItemInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedAccordionItemInteractions {

        public String getHeader() {
            if (isActive()) {
                return getActiveHeaderElement().getText();
            } else if (isEnabled()) {
                return getInactiveHeaderElement().getText();
            } else {
                return getDisabledHeaderElement().getText();
            }
        }

        public WebElement getHeaderElement() {
            if (isActive()) {
                return getActiveHeaderElement();
            } else if (isEnabled()) {
                return getInactiveHeaderElement();
            } else {
                return getDisabledHeaderElement();
            }
        }

        public boolean isActive() {
            return getActiveHeaderElement().isPresent() && getActiveHeaderElement().isDisplayed() && getContentElement().isDisplayed();
        }

        public boolean isEnabled() {
            return !getDisabledHeaderElement().isPresent();
        }

        public WebElement getContentElement() {
            return content;
        }

        public WebElement getToActivateElement() {
            return toActivate;
        }

        protected GrapheneElement getActiveHeaderElement() {
            return activeHeader;
        }

        protected GrapheneElement getDisabledHeaderElement() {
            return disabledHeader;
        }

        protected WebElement getInactiveHeaderElement() {
            return inactiveHeader;
        }
    }
}
