package org.richfaces.photoalbum.service;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = false)
public class PhotoAlbumException extends Exception {

	private static final long serialVersionUID = -305899531752889504L;

	public PhotoAlbumException() {
		super();
	}

	public PhotoAlbumException(String message) {
		super(message);
	}

	public PhotoAlbumException(String message, Throwable cause) {
		super(message, cause);
	}
}
