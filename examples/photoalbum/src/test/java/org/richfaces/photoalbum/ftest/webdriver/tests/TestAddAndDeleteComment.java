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
package org.richfaces.photoalbum.ftest.webdriver.tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.PhotoView;
import org.richfaces.photoalbum.ftest.webdriver.fragments.view.PhotoView.CommentsPanel;

import category.FailingOnPhantomJS;

/**
 * Every method starts with login(), cannot put it in @BeforeMethod because of https://issues.jboss.org/browse/ARQGRA-309
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestAddAndDeleteComment extends AbstractPhotoalbumTest {

    private final DateTime dt = new DateTime();
    private final DateTimeFormatter pattern = DateTimeFormat.forPattern("MMM d, YYYY");
    private final String comment = "new comment";

    private void addSingleComment() {
        PhotoView photoView = getView(PhotoView.class);
        CommentsPanel commentPanel = photoView.getCommentPanel();
        if (commentPanel.getComments().size() != 4) {
            commentPanel.addComment(comment);
        }
    }

    @Test
    @Category(FailingOnPhantomJS.class)
    public void addComment() {
        login();

        // open first photo in album 'Monuments and just buildings'
        PhotoView photoView = getPage().getLeftPanel().openAlbumInPredefinedGroup("Monuments and just buildings", "Monuments")
            .getPhotos().get(0).open();
        CommentsPanel commentPanel = photoView.getCommentPanel();

        // check previous comments
        List<PhotoView.CommentsPanel.Comment> comments = commentPanel.getComments();
        assertEquals(3, comments.size());
        comments.get(0).checkAll(JAN_DATE_85, "Superb Shot and so beautiful Colors !!!", "avatar_w_default.png", "Noname");
        comments.get(1).checkAll(JAN_DATE_85, "really pretty. it looks like there is a lady in the _center_, blowing kisses!!", "avatar_w_default.png", "Noname");
        comments.get(2).checkAll(JAN_DATE_85, "that is a beautiful flower with great colours", "avatar_default.png", "amarkhel");

        // add comment
        addSingleComment();

        // check comments
        comments = commentPanel.getComments();
        assertEquals(4, comments.size());
        comments.get(0).checkAll(JAN_DATE_85, "Superb Shot and so beautiful Colors !!!", "avatar_w_default.png", "Noname");
        comments.get(1).checkAll(JAN_DATE_85, "really pretty. it looks like there is a lady in the _center_, blowing kisses!!", "avatar_w_default.png", "Noname");
        comments.get(2).checkAll(JAN_DATE_85, "that is a beautiful flower with great colours", "avatar_default.png", "amarkhel");
        comments.get(3).checkAll(dt.toString(pattern), comment, "avatar_default.png", "amarkhel");
        comments.get(3).checkIfUsersComment();

        // move to second image in album and back
        photoView.getImagesScroller().getPreviews().get(1).open();
        photoView.getImagesScroller().getPreviews().get(0).open();

        // check comments again
        comments = commentPanel.getComments();
        assertEquals(4, comments.size());

        comments.get(0).checkAll(JAN_DATE_85, "Superb Shot and so beautiful Colors !!!", "avatar_w_default.png", "Noname");
        comments.get(1).checkAll(JAN_DATE_85, "really pretty. it looks like there is a lady in the _center_, blowing kisses!!", "avatar_w_default.png", "Noname");
        comments.get(2).checkAll(JAN_DATE_85, "that is a beautiful flower with great colours", "avatar_default.png", "amarkhel");
        comments.get(3).checkAll(dt.toString(pattern), comment, "avatar_default.png", "amarkhel");
        comments.get(3).checkIfUsersComment();
    }

    @Test
    @Category(FailingOnPhantomJS.class)
    public void deleteComment() {
        // firstly need to add comment if its not already present (includes login)
        addSingleComment();

        PhotoView photoView = getView(PhotoView.class);
        CommentsPanel commentPanel = photoView.getCommentPanel();

        List<PhotoView.CommentsPanel.Comment> comments = photoView.getCommentPanel().getComments();
        // delete
        comments.get(3).delete();
        // check if deleted
        comments = commentPanel.getComments();
        assertEquals(3, comments.size());
        comments.get(0).checkAll(JAN_DATE_85, "Superb Shot and so beautiful Colors !!!", "avatar_w_default.png", "Noname");
        comments.get(1).checkAll(JAN_DATE_85, "really pretty. it looks like there is a lady in the _center_, blowing kisses!!", "avatar_w_default.png", "Noname");
        comments.get(2).checkAll(JAN_DATE_85, "that is a beautiful flower with great colours", "avatar_default.png", "amarkhel");
    }
}
