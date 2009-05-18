/**
 * 
 */
package org.richfaces.photoalbum;

import org.testng.Assert;

import com.thoughtworks.selenium.Selenium;

/**
 * @author Andrey Markavtsov
 *
 */
public class RealWorldHelper {
	
		
    private static final String WINDOW_JS_RESOLVER = "selenium.browserbot.getCurrentWindow().";
    private static final String STATUS_START_ID = "_viewRoot:status.start";
    private static final int TIMEOUT = 20000;
    
    public static interface UserInfoConstants {
		String LOGIN_NAME = "amarkhel";
		String LOGIN_PASSWORD = "12345";
    }
    
    public static interface HtmlConstants {
    	
		public static interface IndexPage {
			String MAIN_FORM_ID = "mainform";
			//String shelfCountPath = "//table[@id='menuform:menu']/tbody/tr/td[2]";
			//int SHELF_COUNT = 2;
		}
		
		public static interface LoginPanel {
			String FORM_ID = "loginPanelForm";
			String usernameId = FORM_ID + ":username";
			String passwordId = FORM_ID + ":password";
			String loginButtonPath= "//*[@class='login-table-col']//*[@type='image']";
			
		}
		
		public static interface ConfirmationPanel {
			String FORM_ID = "confirmForm";
			String OK_PATH = "//*[@id='" + FORM_ID + ":firstButton']//input";
			String CANCEL_PATH = "//*[@id='" + FORM_ID + ":secondButton']//input";			
		}
		
		public static interface AddShelfPanel {
			String FORM_ID = "form5";
			String NAME_ID = FORM_ID + ":name";
			String SAVE_PATH = "//*[@id='" + FORM_ID + ":firstButton']//input";
		}
		
		public static interface AddAlbumPanel {
			String FORM_ID = "form2";
			String NAME_ID = FORM_ID + ":name";
			String SAVE_PATH = "//*[@id='" + FORM_ID + ":firstButton']//input";
		}
		
		public static interface LogInOutArea {
			String PATH = "xpath=id('logInOutMenu')";
			String LOGIN_ID = "loginPanelShowlink";
			String REGISTER_PATH = PATH + "/descendant::a[2]";
			String LOGOUT_PATH = REGISTER_PATH;
			String USER_INFO_PATH = "//a[@class='logged-user']";
		}
		
		public static interface ToolBarArea {
			String PATH = "//*[@class='dr-toolbar-int rich-toolbar-item main-menu-toolbar-content']";
			String VIEW_SHELFS_PATH = PATH + "/div[2]";
			String VIEW_ALBUMS_PATH = PATH + "/div[3]";
			String VIEW_IMAGES_PATH = PATH + "/div[4]";
			String ADD_GROUP_PATH = PATH + "/*[@id='menuform:add_icons_div_id']";
			String ADD_SHELF_PATH = ADD_GROUP_PATH + "/a[1]";
			String ADD_ALBUM_PATH = ADD_GROUP_PATH + "/a[2]";
			String ADD_IMAGE_PATH = ADD_GROUP_PATH + "/a[3]";
		}
		
		public static interface ShelfArea {
			String HEADER_PATH = "//*[@class='content_box']/descendant::*[@class='shelf-header-table']";
			String HEADER_NAME_PATH_SUFFIX = "//h1//text()";
			String HEADER_NAME_PATH = HEADER_PATH + HEADER_NAME_PATH_SUFFIX;
			String DESCRIPTION_PATH = HEADER_PATH + "/following-sibling::p";
			String CONTROLS_PATH = "//*[@class='content_box']/descendant::*[@class='shelf-header-table-col2']";
			String EDIT_PATH = CONTROLS_PATH + "/a[1]";
			String DELETE_PATH = CONTROLS_PATH + "/a[2]";
			String VIEW_PATH_SUFFIX = "/a[3]";
		}
		
		public static interface EditShelfArea {
			String DESCRIPTION_ID = IndexPage.MAIN_FORM_ID + ":shelf_description";
			String SAVE_PATH = "//*[@id='" + IndexPage.MAIN_FORM_ID + "']//*[@class='photoalbumButton'][1]/input";
		}
		
		public static interface AlbumArea {
			String HEADER_PATH = "//*[@class='album-header-table']";
			String HEADER_NAME_PATH_SUFFIX = "//h1//text()";
			String HEADER_NAME_PATH = HEADER_PATH + HEADER_NAME_PATH_SUFFIX;
			String DESCRIPTION_PATH = HEADER_PATH + "/following-sibling::p";
			String PREVIEW_PATH = "//*[@class='preview_box_album_120']";
			String PREVIEW_LINK_PATH_SUFFIX = "//a";
			String PREVIEW_DRAG_PATH_SUFFIX = PREVIEW_LINK_PATH_SUFFIX + "/img";
			String PREVIEW_NAME_PATH_SUFFIX = "/*[@class='album_name']";
			String CONTROLS_PATH = "//*[@class='album-header-table-col2']";
			String EDIT_PATH = CONTROLS_PATH + "/a[1]";
			String DELETE_PATH = CONTROLS_PATH + "/a[2]";
			//String IMAGES_CONTAINER = "//span[@id='mainform:userAlbumImages']";
		}
		
		public static interface EditAlbumArea {
			String DESCRIPTION_ID = IndexPage.MAIN_FORM_ID + ":album_description";
			String SAVE_PATH = "//*[@id='" + IndexPage.MAIN_FORM_ID + "']//*[@class='photoalbumButton'][1]/input";
		}
		
		public static interface ImageArea {
			String HEADER_PATH = "//*[@class='image-header-table']";
			String HEADER_NAME_PATH_SUFFIX = "//h1//text()";
			String HEADER_NAME_PATH = HEADER_PATH + HEADER_NAME_PATH_SUFFIX;
			String DESCRIPTION_PATH = HEADER_PATH + "/following-sibling::p";
			String PREVIEW_PATH = "//*[@class='preview_box_photo_120']";
			String PREVIEW_LINK_PATH_SUFFIX = "//a";
			String PREVIEW_DRAG_PATH_SUFFIX = PREVIEW_LINK_PATH_SUFFIX + "/img";
			String PREVIEW_NAME_PATH_SUFFIX = "/*[@class='photo_name']";
			String CONTROLS_PATH = "//*[@class='mainImage-table-col3']";
			String EDIT_PATH = CONTROLS_PATH + "/div[1]/input";
			String DELETE_PATH = CONTROLS_PATH + "/div[2]/input";
			String SCROLLER_PREVIEW_PATH = "//descendant::*[@class='preview_box_photo_80']";
			String SCROLLER_BUTTONS_PATH = "//*[contains(@class, 'rich-datascr-button')]";
			String SCROLLER_PREVIOUS_PATH = SCROLLER_BUTTONS_PATH + "[1]";
			String SCROLLER_NEXT_PATH = SCROLLER_BUTTONS_PATH  + "[2]";
		}
		
		public static interface EditImageArea {
			String PATH = "//*[@class='image-edit-div-table']";
			String DESCRIPTION_PATH = PATH + "//textarea";
			String SAVE_PATH = PATH + "//*[@class='photoalbumButton'][1]/input";
		}
		
		public static interface RegistrationArea {
			String NAME_ID = IndexPage.MAIN_FORM_ID + ":loginName";
			String PASSWORD_ID = IndexPage.MAIN_FORM_ID + ":password";
			String CONFIRM_PASSWORD_ID = IndexPage.MAIN_FORM_ID + ":confirmPassword";
			String FIRST_NAME_ID = IndexPage.MAIN_FORM_ID + ":firstname";
			String SECOND_NAME_ID = IndexPage.MAIN_FORM_ID + ":secondname";
			String BIRTH_DATE_ID = IndexPage.MAIN_FORM_ID + ":birthDateInputDate";
			String EMAIL_ID = IndexPage.MAIN_FORM_ID + ":email";
			String REGISTER_PATH = "//*[@class='content_box']/descendant::*[@class='photoalbumButton'][1]//input";
		}
		
		public static interface UserProfileArea {
			String NAME_PATH = "//*[@class='reg-table']/descendant::td[2]";
		}
		
		public static interface FilesUploadArea {
			String FILE_UPLOAD_ID = IndexPage.MAIN_FORM_ID + ":fileUpload";
		}

		public static interface TreeArea {
			String CHILDREN_PATH = "//*[@id='treeform:tree']/*[@class='dr-tree rich-tree']/*[contains(@id, 'childs')]";
			String SHELF_PATH_PART = CHILDREN_PATH + "/*[not(contains(@class, 'rich-tree-node-children'))]";
			String ALBUM_PATH_PART = CHILDREN_PATH + "/*[contains(@class, 'rich-tree-node-children')]";
			String TEXT_PATH_SUFFIX = "/descendant::*[contains(@id, 'text')]";
		}

		public static interface ResizeSlider {
			String TRACK_SLIDER = "//td[./div[@class='dr-insldr-track rich-inslider-track']]";
			String SLIDER_INPUT = "//*[@class='dr-insldr rich-slider']//input[@type='hidden']";
		}

		public static interface Search {
			String SEARCH_INPUT = "//*[@class='search-input']";
			String SEARCH_BUTTON = "//*[@class='search-find-button']";
			String SEARCH_OPTION = "//*[@class='search-option-link']";

			String INACTIVE_TAB = "//*[@class='dr-tbpnl-tb rich-tab-header dr-tbpnl-tb-inact rich-tab-inactive']";
			String ACTIVE_TAB = "//*[@class='dr-tbpnl-tbcell-act rich-tabhdr-cell-active']";

			String SEARCH_CRITERIA_KEYWORDS = "//*[@class='search-criteria'][2]";
			String SEARCH_CRITERIA_OBJECTS = "//*[@class='search-criteria'][1]/strong";
			String OBJECTS_MY = "//*[@class='search-options-div1']/input[1]";
			String OBJECTS_SHARED = "//*[@class='search-options-div1']/input[2]";

			String OBJETCS_SHELVES = "//*[@class='search-options-div2']//span[contains(. , 'Shelves')]/input";
			String OBJETCS_ALBUMS = "//*[@class='search-options-div2']//span[contains(. , 'Albums')]/input";
			String OBJETCS_IMAGES = "//*[@class='search-options-div2']//span[contains(. , 'Images')]/input";
			String OBJETCS_USERS = "//*[@class='search-options-div2']//span[contains(. , 'Users')]/input";
			String OBJETCS_TAGS = "//*[@class='search-options-div2']//span[contains(. , 'Tags')]/input";

		}
	}

	public static void login(Selenium selenium) {
		login(selenium, UserInfoConstants.LOGIN_NAME, UserInfoConstants.LOGIN_PASSWORD);
	}
	
	public static void login(Selenium selenium, String name, String password) {
		if (isLogined(selenium)) {
			logout(selenium);
		}
		selenium.click(HtmlConstants.LogInOutArea.LOGIN_ID);
		try {
			Thread.sleep(5000);
		}catch (Exception e) {
			Assert.fail("Test failed caused by: " + e);
		}
		
		Assert.assertTrue(selenium.isVisible(HtmlConstants.LoginPanel.usernameId), "Input for username in not visible");
		Assert.assertTrue(selenium.isVisible(HtmlConstants.LoginPanel.passwordId), "Input for password in not visible");
		
		String type = selenium.getAttribute("//*[@id='"+HtmlConstants.LoginPanel.passwordId+"']/@type");
		if (!"password".equals(type)) {
			Assert.fail("Password input should be of 'password' type");
		}
		
		selenium.type(HtmlConstants.LoginPanel.usernameId, name);
		selenium.type(HtmlConstants.LoginPanel.passwordId, password);
		
		selenium.click(HtmlConstants.LoginPanel.loginButtonPath);
		waitForAjaxCompletion(selenium);
		
		if (!isLogined(selenium, UserInfoConstants.LOGIN_NAME)) {
			Assert.fail("Authentication was not succesfull. Logged user text should contain typed login name");
		}
	}
	
	public static void logout(Selenium selenium) {
		selenium.click(HtmlConstants.LogInOutArea.LOGOUT_PATH);
		selenium.waitForPageToLoad(String.valueOf(TIMEOUT));
		Assert.assertFalse(isLogined(selenium), "Logout was not succesfull.");
	}
	
	public static void testUserProfile(Selenium selenium) {
		testUserProfile(selenium, null);
	}
	
	public static void testUserProfile(Selenium selenium, String name) {
		Assert.assertTrue(selenium.isVisible(HtmlConstants.UserProfileArea.NAME_PATH));
		if (name != null) {
			Assert.assertEquals(selenium.getText(HtmlConstants.UserProfileArea.NAME_PATH), name);
		}
	}
	
	public static void testFilesUpload(Selenium selenium) {
		Assert.assertTrue(selenium.isVisible(HtmlConstants.FilesUploadArea.FILE_UPLOAD_ID));
	}
	
	public static void openShelf(Selenium selenium) {
		openShelf(selenium, selenium.getText(HtmlConstants.ShelfArea.HEADER_PATH + "[2]" + HtmlConstants.ShelfArea.HEADER_NAME_PATH_SUFFIX));
	}
	public static void openShelf(Selenium selenium, String shelfName) {
		boolean presented = false;
		int xpathCount = selenium.getXpathCount(HtmlConstants.ShelfArea.HEADER_PATH).intValue();
		for (int i = 2; i <= xpathCount && !presented; i++) {
			presented = selenium.getText("" + HtmlConstants.ShelfArea.HEADER_PATH
					+ "[" + i + "]" + HtmlConstants.ShelfArea.HEADER_NAME_PATH_SUFFIX)
					.equals(shelfName);
			
			if (presented) {
				selenium.click(HtmlConstants.ShelfArea.CONTROLS_PATH + "[" + (i - 1) + "]" +  HtmlConstants.ShelfArea.VIEW_PATH_SUFFIX);
				waitForAjaxCompletion(selenium);
				testShelfArea(selenium, shelfName);
			}
		}
		if (!presented) {
			Assert.fail("Shelf with name '" + shelfName + "' doesn't exist on this page.");
		}
		
	}
	
	public static void testShelfArea(Selenium selenium) {
		testShelfArea(selenium, null, null);
	}

	public static void testShelfArea(Selenium selenium, String shelfName) {
		testShelfArea(selenium, shelfName, null);
	}

	public static void testShelfArea(Selenium selenium, String shelfName, String description) {
		Assert.assertTrue(selenium.isVisible(HtmlConstants.ShelfArea.HEADER_PATH));
		if (shelfName != null) {
			String text = selenium.getText(RealWorldHelper.HtmlConstants.ShelfArea.HEADER_NAME_PATH);
			Assert.assertTrue(text.equals(shelfName), "Incorrect data was opened on shelf area.");
		}
		if (description != null) {
			String text = selenium.getText(RealWorldHelper.HtmlConstants.ShelfArea.DESCRIPTION_PATH);
			Assert.assertTrue(text.equals(description), "Incorrect data was opened on album area.");
		}
	}
	
	public static boolean isShelfPresentOnPage(Selenium selenium, String shelfName) {
		boolean presented = false;
		if (shelfName != null) {
			int xpathCount = selenium.getXpathCount(HtmlConstants.ShelfArea.HEADER_PATH).intValue();
			for (int i = 2; i <= xpathCount && !presented; i++) {
				presented = selenium.getText("" + HtmlConstants.ShelfArea.HEADER_PATH
						+ "[" + i + "]" + HtmlConstants.ShelfArea.HEADER_NAME_PATH_SUFFIX)
						.equals(shelfName);
				
			}
		} else {
			Assert.fail("ShelfName shouldn't be null.");
		}
		return presented;
	}

	public static void deleteCurrentShelf(Selenium selenium) {
		selenium.click(HtmlConstants.ShelfArea.DELETE_PATH);
		waitForAjaxCompletion(selenium);
		confirm(selenium);
		testShelfArea(selenium);
	}

	public static void addShelf(Selenium selenium, String shelfName) {
		selenium.click(HtmlConstants.ToolBarArea.ADD_SHELF_PATH);
		waitForAjaxCompletion(selenium);
		selenium.type(HtmlConstants.AddShelfPanel.NAME_ID, shelfName);
		selenium.click(HtmlConstants.AddShelfPanel.SAVE_PATH);
		waitForAjaxCompletion(selenium);
		testShelfArea(selenium, shelfName);
	}

	public static String getShelfTextPathInTree(Selenium selenium, String shelfName) {
		String xPath = null;
		int xpathCount = selenium.getXpathCount(HtmlConstants.TreeArea.SHELF_PATH_PART).intValue();
		for (int i = 1; i <= xpathCount && xPath == null; i++) {
			String path = HtmlConstants.TreeArea.SHELF_PATH_PART + "[" + i + "]" + HtmlConstants.TreeArea.TEXT_PATH_SUFFIX;
			if (selenium.getText(path).contains(shelfName)) {
				xPath = path;
			}
		}
		if (xPath == null) {
			Assert.fail("Shelf with name '" + shelfName + "' doesn't exist in tree.");
		}
		return xPath;
	}

	public static void openAlbumFromPreview(Selenium selenium) {
		openAlbumFromPreview(selenium, selenium.getText(HtmlConstants.AlbumArea.PREVIEW_PATH + HtmlConstants.AlbumArea.PREVIEW_NAME_PATH_SUFFIX));
	}
	
	public static void openAlbumFromPreview(Selenium selenium, String albumName) {
		boolean presented = false;
		int xpathCount = selenium.getXpathCount(HtmlConstants.AlbumArea.PREVIEW_PATH).intValue();
		for (int i = 1; i <= xpathCount && !presented; i++) {
			presented = selenium.getText(HtmlConstants.AlbumArea.PREVIEW_PATH
					+ "[" + i + "]" + HtmlConstants.AlbumArea.PREVIEW_NAME_PATH_SUFFIX)
					.equals(albumName);
			if (presented) {
				selenium.click(HtmlConstants.AlbumArea.PREVIEW_PATH + "[" + i + "]" +  HtmlConstants.AlbumArea.PREVIEW_LINK_PATH_SUFFIX);
				waitForAjaxCompletion(selenium);
				testAlbumArea(selenium, albumName);
			}
			
		}
		if (!presented) {
			Assert.fail("Album with name '" + albumName + "' doesn't exist on this page.");
		}
		
	}
	
	public static void testAlbumArea(Selenium selenium) {
		testAlbumArea(selenium, null, null);
	}

	public static void testAlbumArea(Selenium selenium, String albumName) {
		testAlbumArea(selenium, albumName, null);
	}

	public static void testAlbumArea(Selenium selenium, String albumName, String description) {
		Assert.assertTrue(selenium.isVisible(HtmlConstants.AlbumArea.HEADER_PATH));
		if (albumName != null) {
			String text = selenium.getText(RealWorldHelper.HtmlConstants.AlbumArea.HEADER_NAME_PATH);
			Assert.assertTrue(text.equals(albumName), "Incorrect data was opened on album area.");
		}
		if (description != null) {
			String text = selenium.getText(RealWorldHelper.HtmlConstants.AlbumArea.DESCRIPTION_PATH);
			Assert.assertTrue(text.equals(description), "Incorrect data was opened on album area.");
		}
	}
	
	public static boolean isAlbumPresentOnPage(Selenium selenium, String albumName) {
		boolean presented = false;
		if (albumName != null) {
			int xpathCount = selenium.getXpathCount(HtmlConstants.AlbumArea.PREVIEW_PATH).intValue();
			for (int i = 1; i <= xpathCount && !presented; i++) {
				presented = selenium.getText(HtmlConstants.AlbumArea.PREVIEW_PATH
						+ "[" + i + "]" + HtmlConstants.AlbumArea.PREVIEW_NAME_PATH_SUFFIX)
						.equals(albumName);
				
			}
		} else {
			Assert.fail("AlbumName shouldn't be null.");
		}
		return presented;
	}

	public static void deleteCurrentAlbum(Selenium selenium) {
		selenium.click(HtmlConstants.AlbumArea.DELETE_PATH);
		waitForAjaxCompletion(selenium);
		confirm(selenium);
		testAlbumArea(selenium);
	}

	public static void addAlbum(Selenium selenium, String albumName) {
		selenium.click(HtmlConstants.ToolBarArea.ADD_ALBUM_PATH);
		waitForAjaxCompletion(selenium);
		selenium.type(HtmlConstants.AddAlbumPanel.NAME_ID, albumName);
		selenium.click(HtmlConstants.AddAlbumPanel.SAVE_PATH);
		waitForAjaxCompletion(selenium);
		testAlbumArea(selenium, albumName);
	}

	public static String getAlbumTextPathInTree(Selenium selenium, String albumName) {
		String xPath = null;
		int xpathCount = selenium.getXpathCount(HtmlConstants.TreeArea.ALBUM_PATH_PART).intValue();
		for (int i = 1; i <= xpathCount && xPath == null; i++) {
			String pathPart = HtmlConstants.TreeArea.ALBUM_PATH_PART + "[" + i + "]" + HtmlConstants.TreeArea.TEXT_PATH_SUFFIX;
			int xpathCount2 = selenium.getXpathCount(pathPart).intValue();
			for (int j = 1; j <= xpathCount2 && xPath == null; j++) {
				String path = pathPart + "[" + j + "]";
				if (selenium.getText(path).contains(albumName)) {
					xPath = path;
				}
			}
		}
		if (xPath == null) {
			Assert.fail("Album with name '" + albumName + "' doesn't exist in tree.");
		}
		return xPath;
	}

	public static void openImageFromPreview(Selenium selenium) {
		openImageFromPreview(selenium, selenium.getText(HtmlConstants.ImageArea.PREVIEW_PATH + HtmlConstants.ImageArea.PREVIEW_NAME_PATH_SUFFIX));
	}

	public static void openImageFromPreview(Selenium selenium, String imageName) {		
		boolean presented = false;
		int xpathCount = selenium.getXpathCount(HtmlConstants.ImageArea.PREVIEW_PATH).intValue();
		for (int i = 1; i <= xpathCount && !presented; i++) {
			presented = selenium.getText(HtmlConstants.ImageArea.PREVIEW_PATH
					+ "[" + i + "]" + HtmlConstants.ImageArea.PREVIEW_NAME_PATH_SUFFIX)
					.equals(imageName);
			if (presented) {
				selenium.click(HtmlConstants.ImageArea.PREVIEW_PATH + "[" + i + "]" +  HtmlConstants.ImageArea.PREVIEW_LINK_PATH_SUFFIX);
				waitForAjaxCompletion(selenium);
				testImageArea(selenium, imageName);
			}
			
		}
		if (!presented) {
			Assert.fail("Image with name '" + imageName + "' doesn't exist on this page.");
		}
	}
		
	public static void testImageArea(Selenium selenium) {
		testImageArea(selenium, null, null);
	}
	
	public static void testImageArea(Selenium selenium, String imageName) {
		testImageArea(selenium, imageName, null);
	}
	
	public static void testImageArea(Selenium selenium, String imageName, String description) {
		Assert.assertTrue(selenium.isVisible(HtmlConstants.ImageArea.HEADER_PATH));
		if (imageName != null) {
			String text = selenium.getText(RealWorldHelper.HtmlConstants.ImageArea.HEADER_NAME_PATH);
			Assert.assertTrue(text.equals(imageName), "Incorrect data was opened on image area.");
		}
		if (description != null) {
			String text = selenium.getText(RealWorldHelper.HtmlConstants.ImageArea.DESCRIPTION_PATH);
			Assert.assertTrue(text.equals(description), "Incorrect data was opened on album area.");
		}
	}

	public static boolean isImagePresentOnPage(Selenium selenium, String imageName) {
		boolean presented = false;
		if (imageName != null) {
			int xpathCount = selenium.getXpathCount(HtmlConstants.ImageArea.PREVIEW_PATH).intValue();
			for (int i = 1; i <= xpathCount && !presented; i++) {
				presented = selenium.getText(HtmlConstants.ImageArea.PREVIEW_PATH
						+ "[" + i + "]" + HtmlConstants.ImageArea.PREVIEW_NAME_PATH_SUFFIX)
						.equals(imageName);
				
			}
		} else {
			Assert.fail("ImageName shouldn't be null.");
		}
		return presented;
	}

	public static void deleteCurrentImage(Selenium selenium) {
		selenium.click(HtmlConstants.ImageArea.DELETE_PATH);
		waitForAjaxCompletion(selenium);
		confirm(selenium);
		testAlbumArea(selenium);
	}

	public static void waitForAjaxCompletion(Selenium selenium) {
		waitForAjaxCompletion(selenium, TIMEOUT);
    }
    public static void waitForAjaxCompletion(Selenium selenium, int miliseconds) {
        selenium.waitForCondition(WINDOW_JS_RESOLVER + "document.getElementById('" + STATUS_START_ID + "').style.display == 'none'", String.valueOf(miliseconds));
    }
    
    public static void confirm(Selenium selenium) {
		selenium.click(HtmlConstants.ConfirmationPanel.OK_PATH);
		waitForAjaxCompletion(selenium);
	}
    
    public static boolean isLogined(Selenium selenium) {
    	return isLogined(selenium, null);
    }

    public static boolean isLogined(Selenium selenium, String name) {
    	boolean logined = !selenium.getXpathCount(HtmlConstants.LogInOutArea.USER_INFO_PATH).equals(0);
    	if (logined && name != null) {
    		logined = selenium.getText(HtmlConstants.LogInOutArea.USER_INFO_PATH).equals(name);
    	}
    	return logined;    	
    }
}
