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
package org.richfaces.photoalbum.ui;

import java.io.IOException;
import java.io.OutputStream;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.richfaces.photoalbum.model.Image;
import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.Preferred;

/**
 * Convenience UI class for 'directLink' functionality.
 *
 * @author Andrey Markhel
 */

@Named
@RequestScoped
public class DirectLinkHelper {

    @Inject
    EntityManager em;

    @Inject
    ImageLoader imageLoader;

    @Inject
    @Preferred
    User user;

    /**
     * Convenience method to paint full-sized image in new tab or window
     *
     * @param out - OutputStream to write image
     * @param data - relative path of the image
     */
    public void paintImage(OutputStream out, Object data) throws IOException {
        Long id = Long.valueOf(data.toString());
        Image im = em.find(Image.class, id);
        if (isImageRecentlyRemoved(im)) {
            imageLoader.paintImage(out, Constants.DEFAULT_PICTURE);
            return;
        }
        if (isImageSharedOrBelongsToUser(im)) {
            imageLoader.paintImage(out, im.getFullPath());
        } else {
            return;
        }
    }

    private boolean isImageSharedOrBelongsToUser(Image im) {
        return  im.getAlbum() != null &&  (im.getAlbum().getShelf().isShared()
            || (user != null) && im.getAlbum().getOwner().getLogin()
                .equals(user.getLogin()));
    }

    private boolean isImageRecentlyRemoved(Image im) {
        return im == null || im.getAlbum() == null || (im.getAlbum().getShelf() == null);
    }
}