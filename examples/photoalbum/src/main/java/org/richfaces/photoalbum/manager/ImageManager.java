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
package org.richfaces.photoalbum.manager;

/**
 * Class encapsulated all functionality, related to working with image.
 *
 * @author Andrey Markhel
 */
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.richfaces.photoalbum.model.Comment;
import org.richfaces.photoalbum.model.Image;
import org.richfaces.photoalbum.model.MetaTag;
import org.richfaces.photoalbum.model.User;
import org.richfaces.photoalbum.model.actions.IImageAction;
import org.richfaces.photoalbum.model.event.ErrorEvent;
import org.richfaces.photoalbum.model.event.EventType;
import org.richfaces.photoalbum.model.event.Events;
import org.richfaces.photoalbum.model.event.ImageEvent;
import org.richfaces.photoalbum.model.event.NavEvent;
import org.richfaces.photoalbum.model.event.SimpleEvent;
import org.richfaces.photoalbum.util.Constants;
import org.richfaces.photoalbum.util.Preferred;

@Named
@RequestScoped
public class ImageManager {

    private static final String IMAGE_DIRECT_LINK = "/includes/directImage.seam?imageId=";

    @Inject
    IImageAction imageAction;

    @Inject
    @Preferred
    User user;

    @Inject
    @EventType(Events.ADD_ERROR_EVENT)
    Event<ErrorEvent> error;

    @Inject
    @Any
    Event<SimpleEvent> event;
    @Inject
    @EventType(Events.UPDATE_MAIN_AREA_EVENT)
    Event<NavEvent> navEvent;
    @Inject
    @EventType(Events.IMAGE_DELETED_EVENT)
    Event<ImageEvent> imageEvent;

    private String message = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Method, that invoked when user click 'Delete image' button. Only registered users can delete images.
     *
     * @param image - image to delete
     *
     */
    public void deleteImage(Image image) {
        if (user == null) {
            return;
        }
        String pathToDelete = image.getFullPath();
        try {
            imageAction.deleteImage(image);
        } catch (Exception e) {
            error.fire(new ErrorEvent("Error", Constants.IMAGE_DELETING_ERROR + " <br/>" + e.getMessage()));
            return;
        }
        // Raise 'imageDeleted' event, parameter path - path of file to delete
        imageEvent.fire(new ImageEvent(image, pathToDelete));
    }

    /**
     * Method, that invoked when user click 'Edit image' button. Only registered users can edit images.
     *
     * @param image - image to edit
     * @param editFromInplace - indicate whether edit process was initiated by inplaceInput component
     */
    public void editImage(Image image, boolean editFromInplace) {
        if (user == null) {
            return;
        }
        try {
            if (user.hasImageWithName(image)) {
                error.fire(new ErrorEvent("Error", Constants.SAME_IMAGE_EXIST_ERROR));
                imageAction.resetImage(image);
                return;
            }
            if (editFromInplace) {
                // We need validate image name manually
                Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                Set<ConstraintViolation<Image>> constraintViolations = validator.validate(image);
                if (constraintViolations.size() > 0) {
                    for (ConstraintViolation<Image> cv : constraintViolations) {
                        error.fire(new ErrorEvent("Constraint violation", cv.getMessage()));
                    }
                    // If error occurred we need refresh album to display correct value in inplaceInput
                    imageAction.resetImage(image);
                    return;
                }
            }
            imageAction.editImage(image, !editFromInplace);
        } catch (Exception e) {
            error.fire(new ErrorEvent("Error", Constants.IMAGE_SAVING_ERROR + " <br/>" + e.getMessage()));
            imageAction.resetImage(image);
            return;
        }
        navEvent.fire(new NavEvent(NavigationEnum.ALBUM_IMAGE_PREVIEW));
    }

    /**
     * Method, that invoked when user add comment to image. Only registered users can add comments to image.
     *
     * @param image - image
     * @param message - comment text
     *
     */
    public void addComment(Image image) {
        if (user == null) {
            return;
        }
        if (null == user.getLogin()) {
            error.fire(new ErrorEvent(Constants.ADDING_COMMENT_ERROR));
            return;
        }
        if (message.trim().equals("")) {
            error.fire(new ErrorEvent(Constants.NULL_COMMENT_ERROR));
            return;
        }
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setImage(image);
        comment.setDate(new Date());
        comment.setMessage(message);
        try {
            imageAction.addComment(comment);
        } catch (Exception e) {
            error.fire(new ErrorEvent("Error", Constants.SAVE_COMMENT_ERROR + " <br/>" + e.getMessage()));
            return;
        }
        message = "";
    }

    /**
     * Method, that invoked when user delete comment. Only registered users can delete comments.
     *
     * @param comment - comment to delete
     *
     */
    public void deleteComment(Comment comment) {
        if (user == null) {
            return;
        }
        try {
            imageAction.deleteComment(comment);
        } catch (Exception e) {
            error.fire(new ErrorEvent("Error", Constants.DELETE_COMMENT_ERROR + " <br/>" + e.getMessage()));
            return;
        }
    }

    /**
     * Method, that invoked to retrieve most popular metatags.
     *
     * @return List of most popular metatags
     *
     */
    public List<MetaTag> popularTags() {
        return imageAction.getPopularTags();
    }

    /**
     * Method, that used to autocomplete 'metatags' field while typing.
     *
     * @param suggest - text to autocomplete
     * @return List of similar metatags
     *
     */
    public List<MetaTag> autoComplete(Object suggest) {
        String temp = (String) suggest;
        if (temp == null || temp.trim().equals("")) {
            return null;
        }
        return imageAction.getTagsLikeString(temp);
    }

    /**
     * Method, that invoked to retrieve direct link to image, to represent in UI.
     *
     * @param image - image to get direct link
     * @return List of similar metatags
     *
     */
    public String getImageDirectLink(Image image) {
        String directLink = null;

        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            return null;
        }

        String value = context.getApplication().getViewHandler().getResourceURL(context, IMAGE_DIRECT_LINK + image.getId());

        ExternalContext externalContext = context.getExternalContext();
        String relativeURL = externalContext.encodeResourceURL(value);
        Object request = externalContext.getRequest();

        if (request instanceof HttpServletRequest) {
            directLink = createServerURL((HttpServletRequest) request) + relativeURL;
        }

        return directLink;

    }

    private String createServerURL(HttpServletRequest request) {
        StringBuffer url = new StringBuffer();

        if (request != null) {
            String name = request.getServerName();
            String protocol = (request.getProtocol().split(Constants.SLASH))[0].toLowerCase();

            int port = request.getServerPort();

            url.append(protocol);
            url.append("://");
            url.append(name);
            url.append(":");
            url.append(Integer.toString(port));
        }

        return url.toString();
    }
}