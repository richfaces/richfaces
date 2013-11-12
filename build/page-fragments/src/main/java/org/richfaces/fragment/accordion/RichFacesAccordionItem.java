/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.richfaces.fragment.accordion;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedInteractions;
import org.richfaces.fragment.switchable.AbstractComponentContainer;

public class RichFacesAccordionItem extends AbstractComponentContainer implements AdvancedInteractions<RichFacesAccordionItem.AdvancedAccordionItemInteractions> {

    private static final String ACTIVE_HEADER_CLASS = "rf-ac-itm-lbl-act";
    private static final String DISABLED_HEADER_CLASS = "rf-ac-itm-lbl-dis";
    private static final String INACTIVE_HEADER_CLASS = "rf-ac-itm-lbl-inact";
    private static final String CONTENT_CLASS = "rf-ac-itm-cnt";
    private static final String TO_ACTIVATE_CLASS = "rf-ac-itm-hdr";

    @FindBy(className = ACTIVE_HEADER_CLASS)
    private GrapheneElement activeHeader;

    @FindBy(className = DISABLED_HEADER_CLASS)
    private GrapheneElement disabledHeader;

    @FindBy(className = INACTIVE_HEADER_CLASS)
    private GrapheneElement inactiveHeader;

    @FindBy(className = CONTENT_CLASS)
    private GrapheneElement content;

    @FindBy(className = TO_ACTIVATE_CLASS)
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

        public final WebElement getHeaderElement() {
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

        public final WebElement getContentElement() {
            return content;
        }

        public final WebElement getToActivateElement() {
            return toActivate;
        }
    }

    protected final GrapheneElement getActiveHeaderElement() {
        return activeHeader;
    }

    protected final GrapheneElement getDisabledHeaderElement() {
        return disabledHeader;
    }

    protected final WebElement getInactiveHeaderElement() {
        return inactiveHeader;
    }
}
