package org.richfaces.photoalbum.ui;

import java.io.IOException;
import java.io.OutputStream;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.richfaces.photoalbum.domain.Image;
import org.richfaces.photoalbum.service.Constants;
/**
 * Convenience UI class for 'directLink' functionality.
 *
 * @author Andrey Markhel
 */
@Name("directLink")
@Scope(ScopeType.EVENT)
@AutoCreate
public class DirectLinkHelper {

	@In(value="entityManager") EntityManager em;
	
	@In ImageLoader imageLoader;
	
	@In Identity identity;
	
	@In Credentials credentials;
	/**
	 * Convenience method to paint full-sized image in new tab or window
	 *
	 * @param out - OutputStream to write image
	 * @param data - relative path of the image
	 */
	public void paintImage(OutputStream out, Object data)
			throws IOException {
		Long id = Long.valueOf(data.toString());
		Image im = em.find(Image.class, id);
		if(isImageRecentlyRemoved(im)){
			imageLoader.paintImage(out, Constants.DEFAULT_PICTURE);
			return;
		}
		if(isImageSharedOrBelongsToUser(im)){
			imageLoader.paintImage(out, im.getFullPath());
		}else{
			return;
		}
	}

	private boolean isImageSharedOrBelongsToUser(Image im) {
		return im.getAlbum().getShelf().isShared() || (identity.hasRole(Constants.ADMIN_ROLE) && im.getAlbum().getOwner().getLogin().equals(credentials.getUsername()));
	}

	private boolean isImageRecentlyRemoved(Image im) {
		return im == null || im.getAlbum() == null || im.getAlbum().getShelf() == null;
	}
}