/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
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
package org.richfaces.photoalbum.ftest.webdriver.fragments;

import static org.junit.Assert.assertTrue;

import java.util.EnumSet;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Actions;
import org.richfaces.fragment.common.CheckboxInputComponentImpl;
import org.richfaces.fragment.common.TextInputComponentImpl;
import org.richfaces.fragment.common.Utils;
import org.richfaces.photoalbum.ftest.webdriver.utils.PhotoalbumUtils;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class SearchPanel {

    @ArquillianResource
    private WebDriver browser;

    @FindBy(css = "div.search-div")
    private SearchDiv searchDiv;
    @FindBy(css = "div.search-options")
    private SearchOptions searchOptions;
    @FindBy(css = "a.search-option-link")
    private WebElement searchOptionsLink;
    @FindBy(css = "a.search-option-link > img")
    private WebElement searchOptionsLinkImage;
    @FindBy(css = "a.search-hide-options-link ")
    private WebElement searchHideOptionsLink;
    @FindBy(css = "a.search-hide-options-link > img")
    private WebElement searchHideOptionsLinkImage;

    private static final EnumSet<SearchOptionsEnum> IN = EnumSet.of(SearchOptionsEnum.IN_OWN, SearchOptionsEnum.IN_SHARED);

    public void check(boolean loggedIn) {
        searchDiv.check();
        checkOptionsHidden();
        openSearchOptions();
        checkOptionsDisplayed();
        searchOptions.check();
        hideSearchOptions();
        checkOptionsHidden();
    }

    private void checkOptionsHidden() {
        assertTrue(searchOptionsLinkImage.getAttribute("src").contains("img/search/bull_arr_down.gif"));
    }

    private void checkOptionsDisplayed() {
        assertTrue(searchHideOptionsLinkImage.getAttribute("src").contains("img/search/bull_arr_up.gif"));
    }

    public SearchDiv getSearchDiv() {
        return searchDiv;
    }

    public SearchOptions getSearchOptions() {
        return searchOptions;
    }

    private void hideSearchOptions() {
        if (Utils.isVisible(searchHideOptionsLink)) {
            PhotoalbumUtils.scrollToElement(searchHideOptionsLink);
            searchHideOptionsLink.click();
        }
        Graphene.waitGui().until().element(searchHideOptionsLink).is().not().visible();
        Graphene.waitGui().until().element(searchOptionsLink).is().visible();
        Graphene.waitGui().until().element(searchOptions.getRoot()).is().not().visible();
    }

    private void openSearchOptions() {
        Graphene.waitAjax().until().element(searchOptionsLink).is().visible();
        PhotoalbumUtils.scrollToElement(searchOptionsLink);
        new Actions(browser).moveToElement(searchOptionsLink).click().perform();
        Graphene.waitAjax().until().element(searchOptionsLink).is().not().visible();
        Graphene.waitGui().until().element(searchHideOptionsLink).is().visible();
        Graphene.waitGui().until().element(searchOptions.getRoot()).is().visible();
    }

    /**
     * Search with all options.
     *
     * @param key
     *
     */
    public void searchFor(String key) {
        searchFor(key, EnumSet.allOf(SearchOptionsEnum.class));
    }

    public void searchFor(String key, EnumSet<SearchOptionsEnum> options) {
        setOptions(options);
        searchDiv.getSearchInput().clear().sendKeys(key);
        Graphene.guardAjax(searchDiv.getFindButton()).click();
    }

    public void setOptions(EnumSet<SearchOptionsEnum> options) {
        openSearchOptions();
        List<CheckboxInputComponentImpl> forCheckboxes = searchOptions.getAllCheckboxesFOR();
        List<CheckboxInputComponentImpl> inCheckboxes = searchOptions.getAllCheckboxesIN();
        // if the user is not logged, than there are fewer options and the indexes won't match
        int indexModifier = (inCheckboxes.size() == 2 ? 0 : -1);
        // check options
        for (SearchOptionsEnum searchOption : options) {
            if (IN.contains(searchOption)) {
                if (searchOption.equals(SearchOptionsEnum.IN_SHARED) || indexModifier != -1) {
                    inCheckboxes.get(searchOption.getIndex() + indexModifier).check();
                }// skip setting of IN_SHARED when there is no such option (indexModifier == -1)
            } else {
                forCheckboxes.get(searchOption.getIndex()).check();
            }
        }
        // uncheck the rest
        for (SearchOptionsEnum searchOption : EnumSet.complementOf(options)) {
            if (IN.contains(searchOption)) {
                if (searchOption.equals(SearchOptionsEnum.IN_SHARED) || indexModifier != -1) {
                    inCheckboxes.get(searchOption.getIndex() + indexModifier).uncheck();
                }// skip unsetting of IN_SHARED when there is no such option (indexModifier == -1)
            } else {
                forCheckboxes.get(searchOption.getIndex()).uncheck();
            }
        }
    }

    public void setOptions(SearchOptionsEnum... options) {
        setOptions(EnumSet.copyOf(Lists.newArrayList(options)));
    }

    public static enum SearchOptionsEnum {

        IN_OWN(0), IN_SHARED(1), SHELVES(0), ALBUMS(1), IMAGES(2), USERS(3), TAGS(4);

        private final int index;

        private SearchOptionsEnum(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public static class SearchDiv {

        @Root
        private WebElement root;

        @FindBy(css = "img.search-img-bg")
        private WebElement bgImage;
        @FindBy(css = "div.search-label")
        private WebElement searchLabel;
        @FindBy(css = "a.search-find-button")
        private WebElement findButton;
        @FindBy(css = "input.search-input")
        private TextInputComponentImpl searchInput;

        public void check() {
            PhotoalbumUtils.checkVisible(bgImage, searchLabel, findButton, searchInput.advanced().getInputElement());
            assertTrue(bgImage.getAttribute("src").contains("img/search/search_bg.png"));
        }

        public WebElement getBgImage() {
            return bgImage;
        }

        public WebElement getFindButton() {
            return findButton;
        }

        public WebElement getRoot() {
            return root;
        }

        public TextInputComponentImpl getSearchInput() {
            return searchInput;
        }

        public WebElement getSearchLabel() {
            return searchLabel;
        }
    }

    public static class SearchOptions {

        @Root
        private WebElement root;

        @FindBy(css = "img.search-option-img")
        private WebElement bgImage;
        @FindBy(css = "div.search-options-div1")
        private WebElement searchINDiv;
        @FindBy(css = "div.search-options-div2")
        private WebElement searchFORDiv;
        @FindBy(css = "div.search-options-div1 input")
        private List<CheckboxInputComponentImpl> allCheckboxesIN;
        @FindBy(css = "div.search-options-div2 input")
        private List<CheckboxInputComponentImpl> allCheckboxesFOR;

        public void check() {
            PhotoalbumUtils.checkVisible(bgImage, searchINDiv, searchFORDiv);
            assertTrue(bgImage.getAttribute("src").contains("img/search/search_option_bg.png"));
            assertTrue(searchINDiv.getText().trim().contains("Shared"));
            assertTrue(searchFORDiv.getText().trim().contains("Groups"));
            assertTrue(searchFORDiv.getText().trim().contains("Albums"));
            assertTrue(searchFORDiv.getText().trim().contains("Images"));
            assertTrue(searchFORDiv.getText().trim().contains("Users"));
            assertTrue(searchFORDiv.getText().trim().contains("Tags"));
        }

        public List<CheckboxInputComponentImpl> getAllCheckboxesFOR() {
            return allCheckboxesFOR;
        }

        public List<CheckboxInputComponentImpl> getAllCheckboxesIN() {
            return allCheckboxesIN;
        }

        public WebElement getBgImage() {
            return bgImage;
        }

        public WebElement getRoot() {
            return root;
        }

        public WebElement getSearchFORDiv() {
            return searchFORDiv;
        }

        public WebElement getSearchINDiv() {
            return searchINDiv;
        }

        public boolean isVisible() {
            try {
                PhotoalbumUtils.checkVisible(bgImage, searchINDiv, searchFORDiv);
                return true;
            } catch (AssertionError e) {
                return false;
            }
        }
    }
}
