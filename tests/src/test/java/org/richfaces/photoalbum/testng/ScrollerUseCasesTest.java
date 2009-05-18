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
public class ScrollerUseCasesTest extends SeleniumTestBase {
	
	@Test
	public void testScroller() {
		renderPage();
		RealWorldHelper.login(selenium);
		selenium.click(HtmlConstants.ToolBarArea.VIEW_IMAGES_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.openImageFromPreview(selenium);
		String firstImageName = selenium.getText(HtmlConstants.ImageArea.SCROLLER_PREVIEW_PATH + "[1]" + HtmlConstants.ImageArea.PREVIEW_NAME_PATH_SUFFIX);
		String secondImageName = selenium.getText(HtmlConstants.ImageArea.SCROLLER_PREVIEW_PATH + "[2]" + HtmlConstants.ImageArea.PREVIEW_NAME_PATH_SUFFIX);
		selenium.click(HtmlConstants.ImageArea.SCROLLER_NEXT_PATH);
		waitForAjaxCompletion();
		Assert.assertEquals(selenium.getText(HtmlConstants.ImageArea.SCROLLER_PREVIEW_PATH + "[1]" + HtmlConstants.ImageArea.PREVIEW_NAME_PATH_SUFFIX), secondImageName);
		selenium.click(HtmlConstants.ImageArea.SCROLLER_PREVIOUS_PATH);
		waitForAjaxCompletion();
		Assert.assertEquals(selenium.getText(HtmlConstants.ImageArea.SCROLLER_PREVIEW_PATH + "[1]" + HtmlConstants.ImageArea.PREVIEW_NAME_PATH_SUFFIX), firstImageName);
		Assert.assertEquals(selenium.getText(HtmlConstants.ImageArea.SCROLLER_PREVIEW_PATH + "[2]" + HtmlConstants.ImageArea.PREVIEW_NAME_PATH_SUFFIX), secondImageName);
		selenium.click(HtmlConstants.ImageArea.SCROLLER_PREVIEW_PATH + "[2]" + HtmlConstants.ImageArea.PREVIEW_NAME_PATH_SUFFIX);
		waitForAjaxCompletion();
		RealWorldHelper.testImageArea(selenium, secondImageName);
	}
}
