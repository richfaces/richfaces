package org.richfaces.photoalbum.testng;

import org.richfaces.photoalbum.RealWorldHelper;
import org.richfaces.photoalbum.SeleniumTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.richfaces.photoalbum.RealWorldHelper.HtmlConstants.Search.*;

/**
 * @user: akolonitsky
 * Date: Apr 29, 2009
 */
public class SearchTest extends SeleniumTestBase {
	private static final String NATURE = "nature";
	private static final String SPORT = "sport";

	private static final String OBJECTS_ALL = "Shelves,Albums,Images,Users,Tag";
	private static final String OBJECT_TAG = "Tag";

	private static final String SHELF_HEADER = "//*[@class='shelf-header-table']";
	private static final String KEYWORDS = "Keywords: ";


	@Test
	public void testSearchForSimpleUser() {
		renderPage();

		checkResult(NATURE, 1, 3, OBJECTS_ALL);
	}

	@Test
	public void testSearch() {
		renderPage();

		RealWorldHelper.login(selenium);

		selenium.click(SEARCH_OPTION);
		selenium.check(OBJECTS_MY);
		selenium.check(OBJECTS_SHARED);

		checkResult(NATURE, 1, 3, OBJECTS_ALL);
	}

	@Test
	public void testSearchOnMyOnly() {
		renderPage();

		RealWorldHelper.login(selenium);

		selenium.click(SEARCH_OPTION);
		selenium.check(OBJECTS_MY);
		selenium.uncheck(OBJECTS_SHARED);

		checkResult(SPORT, 1, 3, OBJECTS_ALL);
	}

	@Test
	public void testSearchOnSharedOnly() {
		renderPage();

		RealWorldHelper.login(selenium);

		selenium.click(SEARCH_OPTION);
		selenium.uncheck(OBJECTS_MY);
		selenium.check(OBJECTS_SHARED);

		checkResult(SPORT, 1, 3, OBJECTS_ALL);
	}

	@Test
	public void testSearchOnMyTags() {
		renderPage();

		RealWorldHelper.login(selenium);

		selenium.click(SEARCH_OPTION);
		selenium.check(OBJECTS_MY);
		selenium.uncheck(OBJECTS_SHARED);

		selenium.uncheck(OBJETCS_SHELVES);
		selenium.uncheck(OBJETCS_ALBUMS);
		selenium.uncheck(OBJETCS_IMAGES);
		selenium.uncheck(OBJETCS_USERS);
		selenium.check  (OBJETCS_TAGS);

		checkResult(SPORT, 0, 0, OBJECT_TAG);

		Assert.assertEquals(1, selenium.getXpathCount("//*[@class='dr-tbpnl-cntnt rich-tabpanel-content']//a"));
	}


	private void checkResult(final String searchValue, final int shelfCount, final int inactiveTabCount, final String searchObjects) {
		selenium.type(SEARCH_INPUT, searchValue);
		selenium.click(SEARCH_BUTTON);
		waitForAjaxCompletion();

		Assert.assertEquals(1, selenium.getXpathCount(ACTIVE_TAB));

		Assert.assertEquals(searchObjects, selenium.getText(SEARCH_CRITERIA_OBJECTS).trim());
		Assert.assertEquals(KEYWORDS + searchValue, selenium.getText(SEARCH_CRITERIA_KEYWORDS).trim());

		Assert.assertEquals(shelfCount, selenium.getXpathCount(SHELF_HEADER));
		Assert.assertEquals(inactiveTabCount, selenium.getXpathCount(INACTIVE_TAB));
	}


//	@Test
//	public void testClickOnFirstAlbum() {

//		renderPage();
//
//		RealWorldHelper.login(selenium);
//
////		final String natureShelf = "//table[//p[contains(. , 'Nature')] and //*[@class='shelf-header-table']]";
//		final String natuteAlbumsXPath = "//div[@class='preview_box_album_120' and ../" + getShelfHeaderCondition("Nature") + "]";
//		Assert.assertEquals(selenium.getXpathCount(natuteAlbumsXPath).intValue(), 2);
//
//		final String albumContainer = "//div[@class='preview_box_album_120']";
//		final String firstAlbumXPath = albumContainer + "[1]//a";
//		final String sportShelfOnTreeXPath = "//*[@id='treeform:tree']//span[span[contains(text(), 'Sport')]]";
//
////		ajaxClick(sportShelfOnTreeXPath);
//
//		String compId = "treeform:j_id146";
//
//		selenium.click("//*[@id='"+ compId + ":childs']/table[1]/tbody/tr/td[1]/div/a/img[2]");
//
//		String britneyAlbumXpath = "//*[@id='"+ compId + ":childs']/div[1]/table/tbody/tr/td[3]";
//		String britney1stSongXpath = "//*[@id='"+ compId + ":childs']/div[1]/div/table/tbody/tr/td[3]";
//
//		String natureShelfXpath = "//*[@id='"+ compId + ":childs']/table[1]/tbody/tr/td[3]";
//		String natureAlbumXpath = "//*[@id='"+ compId + ":childs']/div[1]/table[1]/tbody/tr/td[3]";
//		int natureAlbumsCount = selenium.getXpathCount("//*[@id='"+ compId + ":childs']/div[1]/table").intValue();
//
//		selenium.click("//*[@id='"+ compId + ":childs']/table[2]/tbody/tr/td[1]/div/a/img[2]");
//		String sportShelfXpath = "//*[@id='"+ compId + ":childs']/table[2]/tbody/tr/td[3]";
//		String sportAlbumXpath = "//*[@id='"+ compId + ":childs']/div[2]/table[1]/tbody/tr/td[3]";
//		int sportAlbumsCount = selenium.getXpathCount("//*[@id='"+ compId + ":childs']/div[2]/table").intValue();
//
//		selenium.dragAndDropToObject(natureAlbumXpath, sportShelfXpath);
//		waitForAjaxCompletion();
//		assertChildNodesCount(natureAlbumsCount - 1, "//*[@id='"+ compId + ":childs']/div[1]/table", "Song is not dragged");
//		assertChildNodesCount(sportAlbumsCount - 1, "//*[@id='"+ compId + ":childs']/div[2]/table", "Song is not dropped");
//
//		selenium.dragAndDropToObject(sportAlbumXpath, natureShelfXpath);
//		waitForAjaxCompletion();
//		assertChildNodesCount(natureAlbumsCount, "//*[@id='"+ compId + ":childs']/div[1]/table", "Song is not dragged back");
//		assertChildNodesCount(sportAlbumsCount, "//*[@id='"+ compId + ":childs']/div[2]/table", "Song is not dropped back");
//
//
////		selenium.getXpathCount("//*[@id='treeform:tree']//*[@class='dr-tree-full-width rich-tree-node rich-cm-attached'][last()]/tbody/tr/td[3]");
//		selenium.dragAndDropToObject("//*[@id='mainform:shelves:0:userAlbums:0:img']", "//*[@id='treeform:j_id146:childs']");
////		selenium.dragAndDrop(firstAlbumXPath, sportShelfOnTreeXPath);
//		waitForAjaxCompletion();
//
//		Assert.assertEquals(selenium.getXpathCount(natuteAlbumsXPath).intValue(), 1);
//	}
//
}
