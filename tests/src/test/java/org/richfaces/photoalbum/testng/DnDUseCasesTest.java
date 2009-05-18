/**
 * 
 */
package org.richfaces.photoalbum.testng;

import org.richfaces.photoalbum.RealWorldHelper;
import org.richfaces.photoalbum.SeleniumTestBase;
import org.richfaces.photoalbum.RealWorldHelper.HtmlConstants;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Konstantin Mishin
 *
 */
public class DnDUseCasesTest extends SeleniumTestBase {
	
	@Test
	public void testDnDAlbumFromTree() {
		renderPage();
		RealWorldHelper.login(selenium);
		String toShelfName = selenium.getText(HtmlConstants.ShelfArea.HEADER_PATH + "[2]" + HtmlConstants.ShelfArea.HEADER_NAME_PATH_SUFFIX);
		String fromShelfName = selenium.getText(HtmlConstants.ShelfArea.HEADER_PATH + "[3]" + HtmlConstants.ShelfArea.HEADER_NAME_PATH_SUFFIX);
		RealWorldHelper.openShelf(selenium, fromShelfName);
		RealWorldHelper.openAlbumFromPreview(selenium);
		String albumName = selenium.getText(RealWorldHelper.HtmlConstants.AlbumArea.HEADER_NAME_PATH);
		String shelfTextPath = RealWorldHelper.getShelfTextPathInTree(selenium, toShelfName);
		String albumTextPath = RealWorldHelper.getAlbumTextPathInTree(selenium, albumName);
		selenium.dragAndDropToObject(albumTextPath, shelfTextPath);
		waitForAjaxCompletion();
		selenium.click(HtmlConstants.ToolBarArea.VIEW_SHELFS_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.openShelf(selenium, fromShelfName);
		Assert.assertFalse(RealWorldHelper.isAlbumPresentOnPage(selenium, albumName));
		selenium.click(HtmlConstants.ToolBarArea.VIEW_SHELFS_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.openShelf(selenium, toShelfName);
		Assert.assertTrue(RealWorldHelper.isAlbumPresentOnPage(selenium, albumName));
	}

	@Test
	public void testDnDAlbumFromPreview() {
		renderPage();
		RealWorldHelper.login(selenium);
		String fromShelfName = selenium.getText(HtmlConstants.ShelfArea.HEADER_PATH + "[2]" + HtmlConstants.ShelfArea.HEADER_NAME_PATH_SUFFIX);
		String toShelfName = selenium.getText(HtmlConstants.ShelfArea.HEADER_PATH + "[3]" + HtmlConstants.ShelfArea.HEADER_NAME_PATH_SUFFIX);
		RealWorldHelper.openShelf(selenium, fromShelfName);
		String albumName = selenium.getText(HtmlConstants.AlbumArea.PREVIEW_PATH + HtmlConstants.AlbumArea.PREVIEW_NAME_PATH_SUFFIX);
		String shelfTextPath = RealWorldHelper.getShelfTextPathInTree(selenium, toShelfName);
		selenium.dragAndDropToObject(HtmlConstants.AlbumArea.PREVIEW_PATH + HtmlConstants.AlbumArea.PREVIEW_DRAG_PATH_SUFFIX, shelfTextPath);
		waitForAjaxCompletion();
		selenium.click(HtmlConstants.ToolBarArea.VIEW_SHELFS_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.openShelf(selenium, fromShelfName);
		Assert.assertFalse(RealWorldHelper.isAlbumPresentOnPage(selenium, albumName));
		selenium.click(HtmlConstants.ToolBarArea.VIEW_SHELFS_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.openShelf(selenium, toShelfName);
		Assert.assertTrue(RealWorldHelper.isAlbumPresentOnPage(selenium, albumName));
	}
	
	@Test
	public void testDnDImage() {
		renderPage();
		RealWorldHelper.login(selenium);
		RealWorldHelper.openShelf(selenium);
		String fromAlbumName = selenium.getText(HtmlConstants.AlbumArea.PREVIEW_PATH + HtmlConstants.AlbumArea.PREVIEW_NAME_PATH_SUFFIX);
		String toAlbumName = selenium.getText(HtmlConstants.AlbumArea.PREVIEW_PATH + "[2]" + HtmlConstants.AlbumArea.PREVIEW_NAME_PATH_SUFFIX);
		RealWorldHelper.openAlbumFromPreview(selenium);
		String imageName = selenium.getText(HtmlConstants.ImageArea.PREVIEW_PATH + HtmlConstants.ImageArea.PREVIEW_NAME_PATH_SUFFIX);
		String albumTextPath = RealWorldHelper.getAlbumTextPathInTree(selenium, toAlbumName);
		selenium.dragAndDropToObject(HtmlConstants.ImageArea.PREVIEW_PATH + HtmlConstants.ImageArea.PREVIEW_DRAG_PATH_SUFFIX, albumTextPath);
		waitForAjaxCompletion();
		selenium.click(HtmlConstants.ToolBarArea.VIEW_ALBUMS_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.openAlbumFromPreview(selenium, fromAlbumName);
		Assert.assertFalse(RealWorldHelper.isImagePresentOnPage(selenium, imageName));
		selenium.click(HtmlConstants.ToolBarArea.VIEW_ALBUMS_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.openAlbumFromPreview(selenium, toAlbumName);
		Assert.assertTrue(RealWorldHelper.isImagePresentOnPage(selenium, imageName));
	}
}
