/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.photoalbum.manager;
/**
 * Class encapsulated all functionality, related to working with image.
 *
 * @author Andrey Markhel
 */
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.core.Events;
import org.richfaces.photoalbum.domain.Comment;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.domain.MetaTag;
import org.richfaces.photoalbum.domain.User;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IImageAction;

@Name("imageManager")
@Scope(ScopeType.EVENT)
@AutoCreate
public class ImageManager {

	private static final String IMAGE_DIRECT_LINK = "/includes/directImage.seam?imageId=";
	
    @In IImageAction imageAction;

    @In User user;

    /**
	 * Method, that invoked when user click 'Delete image' button. Only registered users can delete images.
	 * @param image - image to delete
	 *
	 */
    @Restrict("#{s:hasRole('admin')}")
    public void deleteImage(Image image) {
    	String pathToDelete = image.getFullPath();
    	try{
    		imageAction.deleteImage(image);
		}catch(Exception e){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.IMAGE_DELETING_ERROR);
			return;
		}
		//Raise 'imageDeleted' event, parameter path - path of file to delete
        Events.instance().raiseEvent(Constants.IMAGE_DELETED_EVENT, image, pathToDelete);
    }

    /**
	 * Method, that invoked when user click 'Edit image' button. Only registered users can edit images.
	 * @param image - image to edit
	 * @param editFromInplace - indicate whether edit process was initiated by inplaceInput component
	 */
    @Restrict("#{s:hasRole('admin')}")
    public void editImage(Image image, boolean editFromInplace) {
    	try{
    		if(user.hasImageWithName(image)){
	    		Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.SAME_IMAGE_EXIST_ERROR);
    				imageAction.resetImage(image);
	    		return;
	    	}
    		if(editFromInplace){
    			//We need validate image name manually
    			ClassValidator<Image> shelfValidator = new ClassValidator<Image>(Image.class );
        		InvalidValue[] validationMessages = shelfValidator.getInvalidValues(image, "name");
        		if(validationMessages.length > 0 ){
        			for(InvalidValue i : validationMessages){
        				Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, i.getMessage());
        			}
        			//If error occured we need refresh image to display correct value in inplaceInput
        			imageAction.resetImage(image);
    				return;
        		}
			}
    		imageAction.editImage(image, !editFromInplace);
		}catch(Exception e){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.IMAGE_SAVING_ERROR);
			imageAction.resetImage(image);
			return;
		}
        Events.instance().raiseEvent(Constants.UPDATE_MAIN_AREA_EVENT, NavigationEnum.ALBUM_IMAGE_PREVIEW);
    }

    /**
	 * Method, that invoked when user add comment to image. Only registered users can add comments to image.
	 * @param image - image
	 * @param message - comment text
	 *
	 */
    @Restrict("#{s:hasRole('admin')}")
    public void addComment(Image image, String message) {
        if (null == user.getLogin()) {
            Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.ADDING_COMMENT_ERROR);
            return;
        }if(message.trim().equals("")){
        	Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.NULL_COMMENT_ERROR);
            return;
        }
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setImage(image);
        comment.setDate(new Date());
        comment.setMessage(message);
        try{
        	 imageAction.addComment(comment);
		}catch(Exception e){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.SAVE_COMMENT_ERROR);
			return;
		}
		//Clear rich:editor component
        Events.instance().raiseEvent(Constants.CLEAR_EDITOR_EVENT, "");
    }

    /**
	 * Method, that invoked when user delete comment. Only registered users can delete comments.
	 * @param comment - comment to delete
	 *
	 */
    @Restrict("#{s:hasRole('admin')}")
    public void deleteComment(Comment comment) {
    	try{
    		imageAction.deleteComment(comment);
		}catch(Exception e){
			Events.instance().raiseEvent(Constants.ADD_ERROR_EVENT, Constants.DELETE_COMMENT_ERROR);
			return;
		}
    }

    /**
	 * Method, that invoked to retrieve most popular metatags.
	 * @return List of most popular metatags
	 *
	 */
    public List<MetaTag> popularTags() {
        return imageAction.getPopularTags();
    }
    
    /**
	 * Method, that used to autocomplete 'metatags' field while typing.
	 * @param suggest - text to autocomplete
	 * @return List of similar metatags
	 *
	 */
    public List<MetaTag> autoComplete(Object suggest) {
        String temp = (String) suggest;
        if (temp.trim().equals("")) {
            return null;
        }
        return imageAction.getTagsLikeString((String)suggest);
    }
    
    /**
	 * Method, that invoked to retrieve direct link to image, to represent in UI.
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
		
		if(request instanceof HttpServletRequest) {
			directLink = createServerURL((HttpServletRequest)request) + relativeURL;
		}
	
		return directLink;

    }

    private String createServerURL(HttpServletRequest request) {
		StringBuffer url = new StringBuffer();
	
		if(request != null) {
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