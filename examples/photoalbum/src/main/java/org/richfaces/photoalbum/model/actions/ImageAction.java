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
package org.richfaces.photoalbum.model.actions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.richfaces.photoalbum.model.Album;
import org.richfaces.photoalbum.model.Comment;
import org.richfaces.photoalbum.model.Image;
import org.richfaces.photoalbum.model.MetaTag;
import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.PhotoAlbumException;

/**
 * Class for manipulating with image entity. Analogous to DAO pattern. EJB3 Bean
 *
 * @author Andrey Markhel
 */
@Stateless
public class ImageAction implements IImageAction {

    @Inject
    EntityManager em;

    /**
     * Remove image entity from database
     *
     * @param image - image to delete
     * @throws PhotoAlbumException
     */
    public void deleteImage(Image image) throws PhotoAlbumException {
        Album parentAlbum = em.find(Album.class, image.getAlbum().getId());
        try {
            parentAlbum.removeImage(image);
            em.merge(parentAlbum);
            em.flush();

            // the image will be sent in an event
            image.setAlbum(parentAlbum);
        } catch (Exception e) {
            parentAlbum.addImage(image);
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Synchronize state of image entity with database
     *
     * @param image - image to Synchronize
     * @param metatagsChanged - boolean value, that indicates is metatags of this image were changed(add new or delete older)
     * @throws PhotoAlbumException
     */
    public void editImage(Image image, boolean metatagsChanged) throws PhotoAlbumException {
        try {
            if (metatagsChanged) {
                String tagsByImageId = "select m from MetaTag m join m.images i where i.id = :id"; // all Metatags associated
                                                                                                   // with given Image
                List<MetaTag> pointsToImage = em.createQuery(tagsByImageId, MetaTag.class).setParameter("id", image.getId())
                    .getResultList();
                List<MetaTag> imageTags = image.getImageTags();

                for (MetaTag m : pointsToImage) {
                    if (!imageTags.contains(m)) { // deleted tag that still points to the Image
                        removeMetaTag(image, m);
                    }
                }

                for (MetaTag m : imageTags) {
                    if (!m.getImages().contains(image)) { // newly added tag that doesn't yet point to the Image
                        addMetaTag(image, m);
                    }
                }

                // // Create cash of metatags early associated with image
                // final List<MetaTag> removals = new ArrayList<MetaTag>(image.getImageTags());
                //
                // Get string representation of current metatgs, associated with image and split them by comma
                final String[] tokens = image.getMetaString().split(Constants.COMMA);

                // Populate set of tokens - 'candidates to metatags'
                final Set<String> toks = new HashSet<String>();
                for (String s : tokens) {
                    if (!"".equals(s)) {
                        toks.add(s.trim());
                    }
                }

                for (String s : toks) {
                    // Find metatag in early associated tags
                    MetaTag t = image.getTagByName(s);

                    // Find metatag in database
                    t = getTagByName(s);
                    if (t != null) {
                        // If found simple add reference to it
                        image.addMetaTag(t);
                    } else {
                        // Create new metatag
                        t = new MetaTag();
                        t.setTag(s);
                        image.addMetaTag(t);
                        // Persist to database to prevent concurrent creation of other metatags with given name
                        em.persist(t);
                    }
                }
                //
                // for (MetaTag tag : removals) {
                // // If metatag in that collection, we need remove them
                // image.removeMetaTag(tag);
                // }
                // If this image is covering for album, break the reference
                if (image.isCovering()) {
                    if (!image.equals(image.getAlbum().getCoveringImage())) {
                        image.getAlbum().setCoveringImage(image);
                    }
                } else {
                    if (image.equals(image.getAlbum().getCoveringImage())) {
                        image.getAlbum().setCoveringImage(image.getAlbum().getImages().get(0));
                    }
                }

            }
            em.merge(image);
            em.flush();
        } catch (Exception e) {
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Persist image entity to database
     *
     * @param image - image to add
     * @throws PhotoAlbumException
     */
    public void addImage(Image image) throws PhotoAlbumException {
        try {
            em.persist(image);
            image.getAlbum().addImage(image);
            em.flush();
        } catch (Exception e) {
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Remove comment from image
     *
     * @param comment - comment to remove
     * @throws PhotoAlbumException
     */
    public void deleteComment(Comment comment) throws PhotoAlbumException {
        try {
            Image image = comment.getImage();
            image.removeComment(comment);
            em.remove(em.merge(comment));
            em.flush();
        } catch (Exception e) {
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Add comment from image
     *
     * @param comment - comment to add
     * @throws PhotoAlbumException
     */
    public void addComment(Comment comment) throws PhotoAlbumException {
        Image image = comment.getImage();
        if (!image.isAllowComments()) {
            throw new PhotoAlbumException("Cannot add comments to this image");
        }
        try {
            image.addComment(comment);
            em.persist(comment);
            em.flush();
        } catch (Exception e) {
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Add MetaTag to image
     *
     * @param image - image to which the tag is added
     * @param metaTage - metaTag to be added
     * @throws PhotoAlbumException
     */
    public void addMetaTag(Image image, MetaTag metaTag) throws PhotoAlbumException {
        try {
            if (getTagByName(metaTag.getTag()) == null) {
                em.persist(metaTag);
            }
            image.addMetaTag(metaTag);
            metaTag.addImage(image);
            em.flush();
        } catch (Exception e) {
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Remove MetaTag to image
     *
     * @param image - image to which the tag is added
     * @param metaTage - metaTag to be added
     * @throws PhotoAlbumException
     */
    public void removeMetaTag(Image image, MetaTag metaTag) throws PhotoAlbumException {
        try {
            image.removeMetaTag(metaTag);
            metaTag.removeImage(image);
            em.flush();
        } catch (Exception e) {
            throw new PhotoAlbumException(e.getMessage());
        }
    }

    /**
     * Find metatag object by its string representation
     *
     * @param tag - string representation of metatag
     * @return metatag object or null
     */
    public MetaTag getTagByName(String tag) {
        final MetaTag t;
        try {
            t = (MetaTag) em.createNamedQuery(Constants.TAG_BY_NAME_QUERY).setParameter(Constants.TAG_PARAMETER, tag)
                .getSingleResult();
        } catch (NoResultException nre) {
            // If not found
            return null;
        }
        return t;
    }

    /**
     * Find most-popular metatags
     *
     * @return list of most-popular metatags
     */
    @SuppressWarnings("unchecked")
    public List<MetaTag> getPopularTags() {
        return em.createNamedQuery(Constants.TAG_POPULAR_QUERY).setMaxResults(Constants.MAX_RESULTS).getResultList();
    }

    /**
     * Find List of metatags, similar to specified string. Used in autosuggect
     *
     * @param suggest - string to search; has to be set to lowerCase since this cannot be done inside a query
     * @return list of most-popular metatags
     */
    @SuppressWarnings("unchecked")
    public List<MetaTag> getTagsLikeString(String suggest) {
        return (List<MetaTag>) em.createNamedQuery(Constants.TAG_SUGGEST_QUERY)
            .setParameter(Constants.TAG_PARAMETER, suggest.toLowerCase() + Constants.PERCENT).getResultList();
    }

    /**
     * Check if image with specified path already exist in specified album
     *
     * @param album - album to check
     * @param path - path to check
     * @return is image with specified path already exist
     */
    public boolean isImageWithThisPathExist(Album album, String path) {
        return em.createNamedQuery(Constants.IMAGE_PATH_EXIST_QUERY).setParameter(Constants.PATH_PARAMETER, path)
            .setParameter(Constants.ALBUM_PARAMETER, album).getResultList().size() != 0;
    }

    /**
     * Return count of images with path, that started from specified path already exist in specified album
     *
     * @param album - album to check
     * @param path - path to check
     * @return count of images
     */
    public Long getCountIdenticalImages(Album album, String path) {
        return (Long) em.createNamedQuery(Constants.IMAGE_IDENTICAL_QUERY)
            .setParameter(Constants.PATH_PARAMETER, path + Constants.PERCENT).setParameter(Constants.ALBUM_PARAMETER, album)
            .getSingleResult();
    }

    /**
     * Retrieve all cooments posted by given user.
     *
     * @return list of comments
     */
    @SuppressWarnings("unchecked")
    public List<Comment> findAllUserComments(User user) {
        return (List<Comment>) em.createNamedQuery(Constants.USER_COMMENTS_QUERY)
            .setParameter(Constants.AUTHOR_PARAMETER, user).getResultList();
    }

    /**
     * Refresh state of given image
     *
     * @param image - image to Synchronize
     */
    public void resetImage(Image image) {
        em.refresh(image);
    }
}
