package org.richfaces.photoalbum.testng;

import org.richfaces.photoalbum.SeleniumTestBase;
import org.testng.annotations.Test;
import org.testng.Assert;

import static org.richfaces.photoalbum.RealWorldHelper.HtmlConstants.ResizeSlider.*;

/**
 * @user: akolonitsky
 * Date: May 4, 2009
 */
public class ImageResizerTest extends SeleniumTestBase {

	private static final int LENGTH = 100;
	
	private static final int VALUE_START = 80;
	private static final int VALUE_STEP = 40;
	private static final int VALUE_END = 200;

	private static final int VALUE_DEFAULT = 120;

	@Test
	public void testResize() {
		renderPage();

		selenium.click("//*[@class='album-cover-image'][1]");
		waitForAjaxCompletion();

		final int count = getImageCount(VALUE_DEFAULT);

		for (int i = 0; i < (VALUE_END-VALUE_START)/VALUE_STEP; i++) {
			testPosition(count, i);
		}
	}

	private void testPosition(final int count, final int position) {
		final int value = VALUE_START + position * VALUE_STEP;

		clickSlider(position);
		waitForAjaxCompletion();
		Assert.assertEquals(value, getSliderValue());
		Assert.assertEquals(count, getImageCount(value));
	}

	private int getImageCount(final int value) {
		return selenium.getXpathCount("//*[@class='preview_box_photo_"+value+"']").intValue();
	}

	private void clickSlider(int position) {
		final String track = TRACK_SLIDER;
		final String coords = (LENGTH * VALUE_STEP / (VALUE_END - VALUE_START) * position + 1) + ",1";
		selenium.mouseDownAt(track, coords);
		selenium.mouseUpAt(track, coords);
	}

	private int getSliderValue() {
		return Integer.parseInt(selenium.getValue(SLIDER_INPUT));
	}
}
