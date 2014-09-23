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
package org.richfaces.fragment.popupPanel;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.Locations;
import org.richfaces.fragment.common.TypeResolver;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;
import org.richfaces.fragment.common.WaitingWrapper;
import org.richfaces.fragment.common.WaitingWrapperImpl;
import org.richfaces.fragment.panel.AbstractPanel;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class RichFacesPopupPanel<HEADER, HEADERCONTROLS, BODY> extends AbstractPanel<HEADER, BODY> implements PopupPanel<HEADER, HEADERCONTROLS, BODY>, AdvancedVisibleComponentIteractions<RichFacesPopupPanel<HEADER, HEADERCONTROLS, BODY>.AdvancedPopupPanelInteractions> {

    @Drone
    private WebDriver driver;
    @FindBy(css = "div.rf-pp-hndlr-t")
    private GrapheneElement resizerN;
    @FindBy(css = "div.rf-pp-hndlr-r")
    private GrapheneElement resizerE;
    @FindBy(css = "div.rf-pp-hndlr-b")
    private GrapheneElement resizerS;
    @FindBy(css = "div.rf-pp-hndlr-l")
    private GrapheneElement resizerW;
    @FindBy(css = "div.rf-pp-hndlr-tr")
    private GrapheneElement resizerNE;
    @FindBy(css = "div.rf-pp-hndlr-tl")
    private GrapheneElement resizerNW;
    @FindBy(css = "div.rf-pp-hndlr-br")
    private GrapheneElement resizerSE;
    @FindBy(css = "div.rf-pp-hndlr-bl")
    private GrapheneElement resizerSW;
    @FindBy(css = "div.rf-pp-hdr")
    private GrapheneElement headerElement;
    @FindBy(css = "div.rf-pp-hdr-cnt")
    private GrapheneElement headerContentElement;
    @FindBy(css = "div.rf-pp-hdr-cntrls")
    private GrapheneElement headerControlsElement;
    @FindBy(css = "div.rf-pp-cnt-scrlr")
    private GrapheneElement contentScrollerElement;
    @FindBy(css = "div.rf-pp-cnt")
    private GrapheneElement contentElement;
    @FindBy(css = "div.rf-pp-shdw")
    private GrapheneElement shadowElement;

    private final AdvancedPopupPanelInteractions interactions = new AdvancedPopupPanelInteractions();

    private final Class<HEADERCONTROLS> headerControlsClass = (Class<HEADERCONTROLS>) TypeResolver.resolveRawArguments(RichFacesPopupPanel.class, getClass())[1];

    @Override
    public AdvancedPopupPanelInteractions advanced() {
        return interactions;
    }

    @Override
    public HEADERCONTROLS getHeaderControlsContent() {
        return Graphene.createPageFragment(getHeaderControlsClass(), advanced().getHeaderControlsElement());
    }

    /**
     * @return the headerClass
     */
    protected Class<HEADERCONTROLS> getHeaderControlsClass() {
        return headerControlsClass;
    }

    public class AdvancedPopupPanelInteractions extends AdvancedPanelInteractions implements VisibleComponentInteractions {

        @Override
        protected WebElement getBodyElement() {
            return contentElement;
        }

        public WebElement getContentElement() {
            return getBodyElement();
        }

        public WebElement getContentScrollerElement() {
            return contentScrollerElement;
        }

        public WebElement getHeaderContentElement() {
            return headerContentElement;
        }

        public WebElement getHeaderControlsElement() {
            return headerControlsElement;
        }

        @Override
        public GrapheneElement getHeaderElement() {
            return headerElement;
        }

        public Locations getLocations() {
            return Utils.getLocations(getRootElement());
        }

        public WebElement getResizerElement(ResizerLocation resizerLocation) {
            switch (resizerLocation) {
                case N:
                    return resizerN;
                case E:
                    return resizerE;
                case S:
                    return resizerS;
                case W:
                    return resizerW;
                case NE:
                    return resizerNE;
                case SE:
                    return resizerSE;
                case SW:
                    return resizerSW;
                case NW:
                    return resizerNW;
                default:
                    throw new UnsupportedOperationException("Unknown switch " + resizerLocation);
            }
        }

        public WebElement getShadowElement() {
            return shadowElement;
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }

        public AdvancedPopupPanelInteractions moveByOffset(int xOffset, int yOffset) {
            new Actions(driver).dragAndDropBy(getHeaderElement(), xOffset, yOffset).perform();
            return this;
        }

        public AdvancedPopupPanelInteractions resizeFromLocation(ResizerLocation location, int byXPixels, int byYPixels) {
            new Actions(driver).dragAndDropBy(getResizerElement(location), byXPixels, byYPixels).perform();
            return this;
        }

        public WaitingWrapper waitUntilPopupIsNotVisible() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until().element(getRootElement()).is().not().visible();
                }
            }.withMessage("Waiting for popup to be not visible.");
        }

        public WaitingWrapper waitUntilPopupIsVisible() {
            return new WaitingWrapperImpl() {

                @Override
                protected void performWait(FluentWait<WebDriver, Void> wait) {
                    wait.until().element(getRootElement()).is().visible();
                }
            }.withMessage("Waiting for popup to be visible.");
        }
    }
}
