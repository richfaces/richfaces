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

import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupView.AlbumPreview;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class AllAlbumsView {

    @FindBy(className = "album-header-table")
    private WebElement headerInfo;
    @FindBy(css = "div.preview_box_album_120")
    private List<AlbumPreview> albumPreviews;

    public void checkAlbumsHeader(String headerInfo) {
        assertEquals(headerInfo, this.headerInfo.getText().trim());
    }

    public AlbumPreview getAlbumPreview(int index) {
        return albumPreviews.get(index);
    }

    public List<AlbumPreview> getAlbumPreviews() {
        return Collections.unmodifiableList(albumPreviews);
    }
}
