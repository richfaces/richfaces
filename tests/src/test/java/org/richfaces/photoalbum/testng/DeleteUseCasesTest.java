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
    public void testDelete() {
        testDeleteImage();
        testDeleteAlbum();
        testDeleteShelf();
    }

	public void testDeleteImage() {
		renderPage();
		RealWorldHelper.login(this, "user_for_del");
		selenium.click(HtmlConstants.ToolBarArea.VIEW_IMAGES_PATH);
		waitForAjaxCompletion();

        String imageName = selenium.getText(HtmlConstants.ImageArea.PREVIEW_PATH + HtmlConstants.ImageArea.PREVIEW_NAME_PATH_SUFFIX);
        String xpath = "//*[@class='preview_box_photo_120']/*[@class='photo_name' and . = '" + imageName + "']";
        int count = selenium.getXpathCount(xpath).intValue();
		RealWorldHelper.openImageFromPreview(selenium);
		RealWorldHelper.deleteCurrentImage(selenium);
		selenium.click(HtmlConstants.ToolBarArea.VIEW_IMAGES_PATH);
		waitForAjaxCompletion();
		Assert.assertTrue(count > selenium.getXpathCount(xpath).intValue());
	}

	public void testDeleteAlbum() {
		renderPage();
		RealWorldHelper.login(this, "user_for_del");
		selenium.click(HtmlConstants.ToolBarArea.VIEW_ALBUMS_PATH);
		waitForAjaxCompletion();

        String albumName = selenium.getText(HtmlConstants.AlbumArea.PREVIEW_PATH + HtmlConstants.AlbumArea.PREVIEW_NAME_PATH_SUFFIX);
		RealWorldHelper.openAlbumFromPreview(selenium);
		RealWorldHelper.deleteCurrentAlbum(selenium);
		selenium.click(HtmlConstants.ToolBarArea.VIEW_ALBUMS_PATH);
		waitForAjaxCompletion();
		Assert.assertFalse(RealWorldHelper.isAlbumPresentOnPage(selenium, albumName));
	}
	
	public void testDeleteShelf() {
		renderPage();
		RealWorldHelper.login(this, "user_for_del");

        String shelfName = selenium.getText(HtmlConstants.ShelfArea.HEADER_NAME_PATH);
		RealWorldHelper.deleteCurrentShelf(selenium);
		Assert.assertFalse(RealWorldHelper.isShelfPresentOnPage(selenium, shelfName));
	}
}
