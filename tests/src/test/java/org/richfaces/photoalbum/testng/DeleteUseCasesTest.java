/**
 * 
 */
package org.richfaces.photoalbum.testng;

import junit.framework.Assert;

import org.richfaces.photoalbum.RealWorldHelper;
import org.richfaces.photoalbum.SeleniumTestBase;
import org.richfaces.photoalbum.RealWorldHelper.HtmlConstants;
import org.testng.annotations.Test;

/**
 * @author Konstantin Mishin
 *
 */
public class DeleteUseCasesTest extends SeleniumTestBase {
	
	@Test
	public void testDeleteImage() {
		renderPage();
		RealWorldHelper.login(selenium);
		selenium.click(HtmlConstants.ToolBarArea.VIEW_IMAGES_PATH);
		waitForAjaxCompletion();
		String imageName = selenium.getText(HtmlConstants.ImageArea.PREVIEW_PATH + HtmlConstants.ImageArea.PREVIEW_NAME_PATH_SUFFIX);
		RealWorldHelper.openImageFromPreview(selenium);
		RealWorldHelper.deleteCurrentImage(selenium);
		selenium.click(HtmlConstants.ToolBarArea.VIEW_IMAGES_PATH);
		waitForAjaxCompletion();
		Assert.assertFalse(RealWorldHelper.isImagePresentOnPage(selenium, imageName));
	}

	@Test
	public void testDeleteAlbum() {
		renderPage();
		RealWorldHelper.login(selenium);
		selenium.click(HtmlConstants.ToolBarArea.VIEW_ALBUMS_PATH);
		waitForAjaxCompletion();
		String albumName = selenium.getText(HtmlConstants.AlbumArea.PREVIEW_PATH + HtmlConstants.AlbumArea.PREVIEW_NAME_PATH_SUFFIX);
		RealWorldHelper.openAlbumFromPreview(selenium);
		RealWorldHelper.deleteCurrentAlbum(selenium);
		selenium.click(HtmlConstants.ToolBarArea.VIEW_ALBUMS_PATH);
		waitForAjaxCompletion();
		Assert.assertFalse(RealWorldHelper.isAlbumPresentOnPage(selenium, albumName));
	}
	
	@Test
	public void testDeleteShelf() {
		renderPage();
		RealWorldHelper.login(selenium);
		String shelfName = selenium.getText(HtmlConstants.ShelfArea.HEADER_NAME_PATH);
		RealWorldHelper.deleteCurrentShelf(selenium);
		Assert.assertFalse(RealWorldHelper.isShelfPresentOnPage(selenium, shelfName));
	}
}
