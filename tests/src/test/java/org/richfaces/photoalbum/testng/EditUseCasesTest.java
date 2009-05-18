/**
 * 
 */
package org.richfaces.photoalbum.testng;

import org.richfaces.photoalbum.RealWorldHelper;
import org.richfaces.photoalbum.SeleniumTestBase;
import org.richfaces.photoalbum.RealWorldHelper.HtmlConstants;
import org.testng.annotations.Test;

/**
 * @author Konstantin Mishin
 *
 */
public class EditUseCasesTest extends SeleniumTestBase {
	
	@Test
	public void testEditShelf() {
		renderPage();
		RealWorldHelper.login(selenium);
		String shelfName = selenium.getText(HtmlConstants.ShelfArea.HEADER_NAME_PATH);
		String description = "New shelf description";
		selenium.click(HtmlConstants.ShelfArea.EDIT_PATH);
		waitForAjaxCompletion();
		selenium.type(HtmlConstants.EditShelfArea.DESCRIPTION_ID, description);
		selenium.click(HtmlConstants.EditShelfArea.SAVE_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.testShelfArea(selenium, shelfName, description);
	}
	
	@Test
	public void testEditAlbum() {
		renderPage();
		RealWorldHelper.login(selenium);
		String albumName = selenium.getText(HtmlConstants.AlbumArea.PREVIEW_PATH + HtmlConstants.AlbumArea.PREVIEW_NAME_PATH_SUFFIX);
		String description = "New album description";
		RealWorldHelper.openAlbumFromPreview(selenium);
		selenium.click(HtmlConstants.AlbumArea.EDIT_PATH);
		waitForAjaxCompletion();
		selenium.type(HtmlConstants.EditAlbumArea.DESCRIPTION_ID, description);
		selenium.click(HtmlConstants.EditAlbumArea.SAVE_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.testAlbumArea(selenium, albumName, description);
	}
	
	@Test
	public void testEditImage() {
		renderPage();
		RealWorldHelper.login(selenium);
		RealWorldHelper.openAlbumFromPreview(selenium);
		String imageName = selenium.getText(HtmlConstants.ImageArea.PREVIEW_PATH + HtmlConstants.ImageArea.PREVIEW_NAME_PATH_SUFFIX);
		String description = "New image description";
		RealWorldHelper.openImageFromPreview(selenium);
		selenium.click(HtmlConstants.ImageArea.EDIT_PATH);
		waitForAjaxCompletion();
		selenium.type(HtmlConstants.EditImageArea.DESCRIPTION_PATH, description);
		selenium.click(HtmlConstants.EditImageArea.SAVE_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.testImageArea(selenium, imageName, description);
	}
}
