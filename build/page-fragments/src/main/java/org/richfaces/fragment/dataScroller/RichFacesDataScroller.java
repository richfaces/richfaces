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
package org.richfaces.fragment.dataScroller;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.AdvancedVisibleComponentIteractions;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.common.VisibleComponentInteractions;

/**
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
public class RichFacesDataScroller implements DataScroller, AdvancedVisibleComponentIteractions<RichFacesDataScroller.AdvancedDataScrollerInteractions> {

    @Root
    private WebElement root;
    @FindBy(className = "rf-ds-btn-first")
    private GrapheneElement firstBtn;
    @FindBy(className = "rf-ds-btn-fastrwd")
    private GrapheneElement fastRewindBtn;
    @FindBy(className = "rf-ds-btn-prev")
    private GrapheneElement previousBtn;
    @FindBy(className = "rf-ds-btn-next")
    private GrapheneElement nextBtn;
    @FindBy(className = "rf-ds-btn-fastfwd")
    private GrapheneElement fastForwardBtn;
    @FindBy(className = "rf-ds-btn-last")
    private GrapheneElement lastBtn;
    @FindBy(className = "rf-ds-nmb-btn")
    private List<GrapheneElement> numberedPages;
    @FindBy(className = "rf-ds-act")
    private GrapheneElement activePage;

    private final AdvancedDataScrollerInteractions advancedInteractions = new AdvancedDataScrollerInteractions();

    @Override
    public int getActivePageNumber() {
        return Integer.valueOf(advanced().getActivePageElement().getText());
    }

    private void switchTo(By by) {
        WebElement element = advanced().getRootElement().findElement(by);
        element.click();
        Graphene.waitModel().until().element(element).attribute("class").contains(advanced().getActiveClass());
    }

    private boolean checkIfScrollingByFastButtonIsQuickerThanScrollingByPages(int pageNumberBefore) {
        return Math.abs(pageNumberBefore - getActivePageNumber()) > (int) (advanced().getCountOfVisiblePages() / 2);
    }

    @Override
    public void switchTo(int pageNumber) {
        int counter = 50; // to prevent infinite loops
        int lastPageNumber = getActivePageNumber();
        boolean isScrollingByFastButtonQuicker = true;
        boolean scrollingByButtonChecked = false;
        while (pageNumber > advanced().getLastVisiblePageNumber() && counter > 0) {
            if (isScrollingByFastButtonQuicker) {
                switchTo(DataScrollerSwitchButton.FAST_FORWARD);
            } else {
                switchTo(advanced().getLastVisiblePageNumber());
            }
            if (!scrollingByButtonChecked) {
                isScrollingByFastButtonQuicker = checkIfScrollingByFastButtonIsQuickerThanScrollingByPages(lastPageNumber);
                scrollingByButtonChecked = true;
            }
            counter--;
        }
        if (counter == 0) {
            throw new RuntimeException("Scroller doesn't change pages.");
        }

        counter = 50; // to prevent inifinite loops
        while (pageNumber < advanced().getFirstVisiblePageNumber() && counter > 0) {
            if (isScrollingByFastButtonQuicker) {
                switchTo(DataScrollerSwitchButton.FAST_REWIND);
            } else {
                switchTo(advanced().getFirstVisiblePageNumber());
            }
            if (!scrollingByButtonChecked) {
                isScrollingByFastButtonQuicker = checkIfScrollingByFastButtonIsQuickerThanScrollingByPages(lastPageNumber);
                scrollingByButtonChecked = true;
            }
            counter--;
        }
        if (counter == 0) {
            throw new RuntimeException("Scroller doesn't change pages.");
        }

        if (pageNumber == getActivePageNumber()) {
            return;
        }
        switchTo(advanced().getCssSelectorForPageNumber(pageNumber));
    }

    @Override
    public void switchTo(DataScrollerSwitchButton btn) {
        String prevPageText = advanced().getActivePageElement().getText();
        advanced().getButtonElement(btn).click();
        Graphene.waitModel().until().element(advanced().getActivePageElement()).text().not().equalTo(prevPageText);
    }

    @Override
    public boolean hasPages() {
        return advanced().getCountOfVisiblePages() > 0;
    }

    @Override
    public AdvancedDataScrollerInteractions advanced() {
        return advancedInteractions;
    }

    public class AdvancedDataScrollerInteractions implements VisibleComponentInteractions {

        private static final String CSS_PAGE_SELECTOR_TEMPLATE = "[id$='ds_%d'].rf-ds-nmb-btn";
        private static final String CLASS_DISABLED = "rf-ds-dis";
        private static final String CLASS_ACTIVE = "rf-ds-act";

        public WebElement getRootElement() {
            return root;
        }

        public WebElement getButtonElement(DataScrollerSwitchButton btn) {
            return getButtonGrapheneElement(btn);
        }

        private GrapheneElement getButtonGrapheneElement(DataScrollerSwitchButton btn) {
            switch (btn) {
                case FAST_FORWARD:
                    return fastForwardBtn;
                case FAST_REWIND:
                    return fastRewindBtn;
                case FIRST:
                    return firstBtn;
                case LAST:
                    return lastBtn;
                case NEXT:
                    return nextBtn;
                case PREVIOUS:
                    return previousBtn;
                default:
                    throw new UnsupportedOperationException("Unknown button " + btn);
            }
        }

        protected String getActiveClass() {
            return CLASS_ACTIVE;
        }

        protected String getCssPageSelectorTemplate() {
            return CSS_PAGE_SELECTOR_TEMPLATE;
        }

        protected String getDisabledClass() {
            return CLASS_DISABLED;
        }

        private By getCssSelectorForPageNumber(int pageNumber) {
            return By.cssSelector(String.format(getCssPageSelectorTemplate(), pageNumber));
        }

        public List<? extends WebElement> getAllPagesElements() {
            return Collections.unmodifiableList(numberedPages);
        }

        public WebElement getFirstVisiblePageElement() {
            return getAllPagesElements().get(0);
        }

        public int getFirstVisiblePageNumber() {
            return Integer.valueOf(getFirstVisiblePageElement().getText());
        }

        public WebElement getLastVisiblePageElement() {
            return getAllPagesElements().get(getCountOfVisiblePages() - 1);
        }

        public int getLastVisiblePageNumber() {
            return Integer.valueOf(getLastVisiblePageElement().getText());
        }

        public WebElement getActivePageElement() {
            return activePage;
        }

        public int getCountOfVisiblePages() {
            return getAllPagesElements().size();
        }

        public boolean isButtonDisabled(DataScrollerSwitchButton btn) {
            return getButtonElement(btn).getAttribute("class").contains(getDisabledClass());
        }

        public boolean isButtonPresent(DataScrollerSwitchButton btn) {
            return getButtonGrapheneElement(btn).isPresent();
        }

        public boolean isFirstPage() {
            return Integer.valueOf(getActivePageElement().getText()).equals(1);
        }

        public boolean isLastPage() {
            return getActivePageElement().getText().equals(advanced().getLastVisiblePageElement().getText());
        }

        @Override
        public boolean isVisible() {
            return Utils.isVisible(getRootElement());
        }
    }
}
