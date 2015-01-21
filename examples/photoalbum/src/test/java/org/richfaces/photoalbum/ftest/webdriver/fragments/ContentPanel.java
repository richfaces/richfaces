/*
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
 */
package org.richfaces.photoalbum.ftest.webdriver.fragments;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AddImagesView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AlbumView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AllAlbumsView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.AllImagesView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.EditUserProfileView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.GroupsView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.PhotoView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.SearchView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.UserProfileView;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ContentPanel {

    @Root
    private WebElement root;

    public AddImagesView addImagesView() {
        return Graphene.createPageFragment(AddImagesView.class, root);
    }

    public AlbumView albumView() {
        return Graphene.createPageFragment(AlbumView.class, root);
    }

    public AllAlbumsView allAlbumsView() {
        return Graphene.createPageFragment(AllAlbumsView.class, root);
    }

    public AllImagesView allImagesView() {
        return Graphene.createPageFragment(AllImagesView.class, root);
    }

    public EditUserProfileView editUserProfileView() {
        return Graphene.createPageFragment(EditUserProfileView.class, root);
    }

    public PhotoView photoView() {
        return Graphene.createPageFragment(PhotoView.class, root);
    }

    public SearchView searchView() {
        return Graphene.createPageFragment(SearchView.class, root);
    }

    public GroupView groupView() {
        return Graphene.createPageFragment(GroupView.class, root);
    }

    public GroupsView groupsView() {
        return Graphene.createPageFragment(GroupsView.class, root);
    }

    public UserProfileView userProfileView() {
        return Graphene.createPageFragment(UserProfileView.class, root);
    }
}
