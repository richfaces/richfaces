/**
 * 
 */
package org.richfaces.photoalbum.ui;

import java.io.File;
import java.io.Serializable;

import javax.faces.model.SelectItem;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.richfaces.photoalbum.domain.Sex;
import org.richfaces.photoalbum.service.Constants;
import org.richfaces.photoalbum.service.IUserAction;

/**
 * Convenience UI class for userPrefs page
 *
 * @author Andrey Markhel
 */

@Name("userPrefsBean")
@Scope(ScopeType.EVENT)
public class UserPrefsHelper implements Serializable{
	private static final long serialVersionUID = -1767281809514660171L;
	@In IUserAction userAction;
	
	@In(required=false, scope=ScopeType.CONVERSATION) @Out(required=false, scope=ScopeType.CONVERSATION) private File avatarData;

	static final SelectItem[] sexs = new SelectItem[] {
			new SelectItem(Sex.MALE, Constants.MALE),
			new SelectItem(Sex.FEMALE, Constants.FEMALE) };
	
	public SelectItem [] getSexs() {
		return sexs;
	}

	/**
	 * Convenience method invoked after user add avatar and outject avatar to conversation
	 *
	 * param event - upload event
	 */
	public void uploadAvatar(UploadEvent event) {
		UploadItem item = event.getUploadItem();
		avatarData = item.getFile();
	}
	
	public File getAvatarData() {
		return avatarData;
	}

	public void setAvatarData(File avatarData) {
		this.avatarData = avatarData;
	}
}