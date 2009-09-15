/**
 * 
 */
package org.richfaces.photoalbum.testng;

import org.richfaces.photoalbum.RealWorldHelper;
import org.richfaces.photoalbum.SeleniumTestBase;
import org.richfaces.photoalbum.RealWorldHelper.HtmlConstants;
import org.richfaces.photoalbum.RealWorldHelper.UserInfoConstants;
import org.testng.annotations.Test;

/**
 * @author Konstantin Mishin
 *
 */
public class OpenAreasUseCasesTest extends SeleniumTestBase {
    private static final String LOGIN_VIKING = "Viking";

    @Test
	public void testOpenAlbumAndImageWithoutAuthentication() {
		renderPage();
		RealWorldHelper.openAlbumFromPreview(selenium);
		RealWorldHelper.openImageFromPreview(selenium);
	}
	
	@Test
	public void testOpenAlbumAndImageWithAuthentication() {
		renderPage();
		RealWorldHelper.login(this, LOGIN_VIKING);
		RealWorldHelper.openAlbumFromPreview(selenium);
		RealWorldHelper.openImageFromPreview(selenium);
	}

	@Test
	public void testOpenUserProfile() {
		renderPage();
		RealWorldHelper.login(this, LOGIN_VIKING);
		selenium.click(HtmlConstants.LogInOutArea.USER_INFO_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.testUserProfile(selenium, LOGIN_VIKING);
	}
	
	@Test
	public void testOpenFilesUpload() {
		renderPage();
		RealWorldHelper.login(this, LOGIN_VIKING);
		selenium.click(HtmlConstants.ToolBarArea.ADD_IMAGE_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.testFilesUpload(selenium);
	}
	
	
	@Test
	public void testToolbarViewButtons() {
		renderPage();
		RealWorldHelper.login(this, LOGIN_VIKING);
		
		selenium.click(HtmlConstants.ToolBarArea.VIEW_SHELFS_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.testShelfArea(selenium);

		selenium.click(HtmlConstants.ToolBarArea.VIEW_ALBUMS_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.testAlbumArea(selenium);
		
		selenium.click(HtmlConstants.ToolBarArea.VIEW_IMAGES_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.testImageArea(selenium);
	}
}
