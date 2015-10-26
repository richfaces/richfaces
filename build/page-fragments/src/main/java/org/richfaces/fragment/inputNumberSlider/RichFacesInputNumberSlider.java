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
package org.richfaces.fragment.inputNumberSlider;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;

import com.google.common.base.Preconditions;

public class RichFacesInputNumberSlider extends AbstractNumberInput implements InputNumberSlider,
    AdvancedVisibleComponentIteractions<RichFacesInputNumberSlider.AdvancedInputNumberSliderInteractions> {

    @FindBy(className = "rf-insl-inc")
    private WebElement arrowIncrease;

    @FindBy(className = "rf-insl-dec")
    private WebElement arrowDecrease;

    @FindBy(css = "span.rf-insl-inp-cntr > input.rf-insl-inp")
    private TextInputComponentImpl input;

    @FindBy(className = "rf-insl-hnd-cntr")
    private WebElement handleContainer;

    @FindBy(className = "rf-insl-hnd")
    private WebElement handle;

    @FindBy(className = "rf-insl-hnd-dis")
    private WebElement disabledHandle;

    @FindBy(className = "rf-insl-mn")
    private WebElement min;

    @FindBy(className = "rf-insl-mx")
    private WebElement max;

    @FindBy(css = "span.rf-insl-trc")
    private WebElement trackComponent;

    @FindBy(className = "rf-insl-tt")
    private WebElement tooltip;

    @FindBy(css = "span.rf-insl-trc")
    private WebElement sliderElement;

    @Drone
    private WebDriver browser;

    @Root
    private WebElement root;

    private final AdvancedInputNumberSliderInteractions advancedInteractons = new AdvancedInputNumberSliderInteractions();

    @Override
    public AdvancedInputNumberSliderInteractions advanced() {
        return advancedInteractons;
    }

    @Override
    public void slideToValue(double n) {
        advanced().dragHandleToPointInTrace((int) (n * advanced().getWidth()));
    }

    protected void scrollToView() {
        new Actions(browser).moveToElement(advanced().getRootElement()).perform();
    }

    public class AdvancedInputNumberSliderInteractions extends AbstractNumberInput.AdvancedNumberInputInteractions
        implements VisibleComponentInteractions {

        public void dragHandleToPointInTrace(int pixelInTrace) {
            Preconditions.checkArgument(pixelInTrace >= 0 && pixelInTrace <= getWidth(),
                "Cannot slide outside the trace.");
            if (!Utils.isVisible(advanced().getRootElement())) {
                throw new RuntimeException("Trace is not visible.");
            }
            scrollToView();
            // clickAndHold(element) replaced by moveToElement with offset + clickAndHold (RF-14183)
            Actions actions = new Actions(browser).moveToElement(advanced().getHandleElement(), 0, 0).clickAndHold();
            actions.moveToElement(advanced().getRootElement(), pixelInTrace, 0);
            actions.release(advanced().getHandleElement()).build().perform();
        }

        public int getWidth() {
            return Utils.getLocations(getHandleContainer()).getWidth();
        }

        @Override
        public WebElement getArrowIncreaseElement() {
            return arrowIncrease;
        }

        @Override
        public WebElement getArrowDecreaseElement() {
            return arrowDecrease;
        }

        @Override
        public TextInputComponentImpl getInput() {
            return input;
        }

        public WebElement getRootElement() {
            return root;
        }

        public WebElement getDisabledHandleElement() {
            return disabledHandle;
        }

        protected WebElement getHandleContainer() {
            return handleContainer;
        }

        public WebElement getHandleElement() {
            return handle;
        }

        public WebElement getMinimumElement() {
            return min;
        }

        public WebElement getMaximumElement() {
            return max;
        }

        public WebElement getTrackElement() {
            return trackComponent;
        }

        public WebElement getTooltipElement() {
            return tooltip;
        }

        public WebElement getSliderElement() {
            return sliderElement;
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }
    }
}
