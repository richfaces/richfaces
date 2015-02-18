/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
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
package org.richfaces.photoalbum.ftest.webdriver.fragments.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.common.Utils;
import org.richfaces.fragment.dataScroller.RichFacesDataScroller;
import org.richfaces.fragment.inputNumberSlider.RichFacesInputNumberSlider;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AlbumView.PhotoInfo;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AllImagesView {

    @FindBy(className = "image-header-table")
    private WebElement headerInfo;

    @FindBy(className = "rf-insl")
    private RichFacesInputNumberSlider slider;
    @FindByJQuery("div a:contains(?)")
    private WebElement sliderHelpLink;

    @FindBy(css = "a.slideshow-link")
    private WebElement slideShowLink;

    @FindBy(className = "rf-ds")
    private RichFacesDataScroller dataScroller;

    @FindByJQuery("div[class^='preview_box_photo_']:visible")
    private List<PhotoInfo> photos;

    public void checkSliderVisible() {
        assertTrue(Utils.isVisible(slider.advanced().getRootElement()));
    }

    public void checkScrollerVisible() {
        assertTrue(Utils.isVisible(dataScroller.advanced().getRootElement()));
    }

    public void checkHeader(String headerInfo) {
        assertEquals(headerInfo, this.headerInfo.getText().trim());
    }

    public RichFacesDataScroller getDataScroller() {
        return dataScroller;
    }

    public List<PhotoInfo> getPhotos() {
        return Collections.unmodifiableList(photos);
    }

    public WebElement getSlideShowLink() {
        return slideShowLink;
    }

    public RichFacesInputNumberSlider getSlider() {
        return slider;
    }

    public WebElement getSliderHelpLink() {
        return sliderHelpLink;
    }

    public void openSlideShow() {
        Graphene.guardAjax(getSlideShowLink()).click();
    }
}
