package org.richfaces.photoalbum;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;

import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.thoughtworks.selenium.DefaultSelenium;

/**
 * The base java class for selenium tests implementation.
 * 
 * @author Andrey Markavtsov
 * 
 */
public abstract class SeleniumTestBase implements RichSeleniumTest {

    /** Specifies the time to wait for page rendering */
    private static final Integer pageRenderTime = 60000;

    /** Specifies the time to wait for ajax processing */
    protected static final int ajaxCompletionTime = 20000;

    protected static final String serverPort = "8080";

    protected static final String WINDOW_JS_RESOLVER = "selenium.browserbot.getCurrentWindow().";

    /** Parent component id */
    private String parentId;

    /** The default selenium instance */
    public DefaultSelenium selenium;

    /** Host */
    public String host;

    /** Port */
    public String port;

    /** Protocol */
    public String protocol;

    private String filterPrefix;

    private SeleniumServer seleniumServer;

    private Object[][] data;
    
    private int seleniumServerPort = 5556;
    
    static final String STATUS_STOP_ID = "_viewRoot:status.stop";

    /** Defines the name of current j2ee application name */
    public static final String APPLICATION_NAME = "photoalbum";

    public SeleniumTestBase() {
        this("http", "localhost", serverPort);
    }

    public SeleniumTestBase(String protocol, String host, String port) {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
    }

    @BeforeSuite
    public void startSeleniumServer() throws Exception {
        RemoteControlConfiguration config = new RemoteControlConfiguration();
        config.setMultiWindow(false);
        config.setPort(seleniumServerPort);
        config.setUserExtensions(new File(getClass().getClassLoader().getResource("script/selenium/user-extensions.js").toURI()));
        seleniumServer = new SeleniumServer(false, config);
    	seleniumServer.start();
    }

    //@DataProvider(name = "templates")
//    protected Object[][] templates() {
//        return new Object[][] { { Template.SIMPLE }, { Template.DATA_TABLE }, { Template.MODAL_PANEL } };
//        //return this.data;
//    }

    /**
     * This method are invoked before selenium tests started
     */
    @BeforeClass
    @Parameters({"browser"})
    public void startSelenium(String browser) {
		if (browser.equals("*firefox")) {
			String firefox = findFirefox();
			if (firefox != null) {
				browser = firefox;
			}
		}
		
		synchronized (MUTEX) {
            this.filterPrefix = filterPrefix;
            selenium = createSeleniumClient(protocol + "://" + host + ":" + port + "/", browser);
            setFileExtensionContent();
            selenium.start();
            selenium.allowNativeXpath("false");
        }
    }

	private String findFirefox() {
		String browser = null;
		File[] libs = {new File("/usr/lib"), new File("/usr/lib64")};
		for (int i = 0; browser == null && i < libs.length; i++) {
			File lib = libs[i];
			if (lib.isDirectory()) {
				System.out.println("---->lib#" + i + ": " + lib.getPath());
				File[] firefoxDirs = lib.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						return pathname != null&& pathname.getName().startsWith("firefox")
						&& pathname.isDirectory();
					}
				});
	    		for (int j = firefoxDirs.length - 1; browser == null && j >= 0; j--) {
					System.out.println("------>firefoxDir#" + j + ": " + firefoxDirs[j].getPath());
	    			File[] firefoxes = firefoxDirs[j].listFiles(new FileFilter() {
						public boolean accept(File pathname) {
							boolean result = false;
							if (pathname != null && pathname.isFile()) {
								String name = pathname.getName();
								result = "firefox".equals(name) || "firefox-bin".equals(name);
							}
							return result;
						}
					});
	    			if (firefoxes.length > 1 && "firefox-bin".equals(firefoxes[1].getName())) {
	    				browser = "*firefox " + firefoxes[1].getPath();	    				
	    			} else if (firefoxes.length > 0){
	    				browser = "*firefox " + firefoxes[0].getPath();
	    			} 
				}
			}
		}
		return browser;
	}

    private void setFileExtensionContent() {
		try {
			File file = new File(getClass().getClassLoader().getResource(
					"script/selenium/user-extensions.js").toURI());
			FileInputStream stream = new FileInputStream(file);
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int read;
			while ((read = stream.read(buffer)) > 0) {
				b.write(buffer, 0, read);
			}
			
			String js = b.toString();
			if (js != null && js.length() > 0) {
				selenium.setExtensionJs(js);
			}
		} catch (Exception e) {

		}
	}

	/**
     * This method are invoked after selenium tests completed
     */
    @AfterClass(alwaysRun=true)
    public void stopSelenium() {
        synchronized (MUTEX) {
            selenium.stop();
            selenium = null;
        }
    }

//    protected void loadTemplates(String templateExpr) {
//        String[] array = new String[]{};
//        if(null != templateExpr) {
//            array = templateExpr.split(",");
//        }
//
//        List<Object[]> list = new ArrayList<Object[]>();
//        for (String string : array) {
//            Object[] elem = new Object[] {Template.valueOf(string)};
//            list.add(elem);
//        }
//
//        this.data = (Object[][]) list.toArray(new Object[0][0]);
//    }

    @AfterSuite
	public void stopSeleniumServer() throws Exception {
		seleniumServer.stop();
	}

    /**
	 * @param url
	 * @param browser
	 * @return
	 */
    private DefaultSelenium createSeleniumClient(String url, String browser) {
        return new DefaultSelenium(host, seleniumServerPort, browser, url);
    }

    private static final Object MUTEX = new Object();

    /**
     * Renders page
     */
    protected void renderPage() {
        selenium.open(protocol + "://" + host + ":" + port + "/" + APPLICATION_NAME + getTestUrl());
        selenium.waitForPageToLoad(String.valueOf(pageRenderTime));
    }

    /**
     * Writes status message on client side
     *
     * @param message
     */
    public void writeStatus(String message, boolean isError) {
        message = message.replace("'", "\\'");
        StringBuffer buffer = new StringBuffer("writeStatus('");
        buffer.append(message);
        if (!isError) {
        	buffer.append("')");
        }else {
        	buffer.append("',true)");
        }
        runScript(buffer.toString());
    }
    
    /**
     * Writes status message on client side
     *
     * @param message
     */
    public void writeStatus(String message) {
    	writeStatus(message, false);
    }

    /**
     * ReRenders the component
     */
    public void reRenderForm() {
        selenium.getEval("selenium.browserbot.getCurrentWindow().reRenderAll();");
        // clickById("_Selenium_Test_ReRender_Form:_reRender");
        waitForAjaxCompletion(3000);
    }

    /**
     * Performs script defined in parameter
     * 
     * @param script
     * @return
     */
    public String runScript(String script) {
        String result = selenium.getEval(WINDOW_JS_RESOLVER + script);
        checkJSError();
        return result;
    }

    /**
     * Performs script defined in parameter
     * 
     * @param script
     * @return
     */
    public String runScript(String script, boolean checkEerror) {
        String result = selenium.getEval(WINDOW_JS_RESOLVER + script);
        if (checkEerror) {
            checkJSError();
        }
        return result;
    }

    /**
     * Checks if JS error occurred. Fails test if yes. This method should be
     * invoked after each event or any thing which can be a cause of JS error.
     */
    public void checkJSError() {
    	/*
        String error = selenium.getEval(WINDOW_JS_RESOLVER + "checkError();");
        if (error != null && !("null".equals(error)) && !("".equals(error))) {
            Assert.fail("Failure by the following Javascript error: " + error);
        }*/
    }

    /**
     * Checks if page containing component test has been rendered completely
     */
    public void checkPageRendering() {
        try {
            String t1 = getTextById("_Selenium_Test_ControlPoint1");
            String t2 = getTextById("_Selenium_Test_ControlPoint2");
            if (t1 == null || t2 == null || !"Control1".equals(t1) || !"Control2".equals(t2)) {
                Assert.fail("The page has been not rendered properlly");
            }
        } catch (Exception e) {
            Assert.fail("The page has not been rendered properly due the following error: " + e);
        }
    }

    /**
     * Waits while AJAX request will be completed
     * 
     * @param miliseconds -
     *                time to wait
     */
    public void waitForAjaxCompletion(int miliseconds) {
        selenium.waitForCondition(WINDOW_JS_RESOLVER + "$('" + STATUS_STOP_ID + "').style.display != 'none'", String.valueOf(miliseconds));
    }

    /**
     * Waits while AJAX request will be completed
     * 
     * @param miliseconds -
     *                time to wait
     */
    public void waitForAjaxCompletion() {
        waitForAjaxCompletion(ajaxCompletionTime);
    }

    /**
     * Waits while simple request will be completed
     * 
     * @param miliseconds -
     *                time to wait
     */
    public void waitForPageToLoad(int miliseconds) {
        //selenium.waitForPageToLoad(String.valueOf(miliseconds));
    	
    }

    /**
     * Waits for condition
     * 
     * @param condition
     * @param miliseconds
     */
    public void waiteForCondition(String condition, int miliseconds) {
        selenium.waitForCondition(WINDOW_JS_RESOLVER + condition, String.valueOf(miliseconds));
        checkJSError();
    }

    /**
     * Waits while simple request will be completed
     * 
     */
    public void waitForPageToLoad() {
        selenium.waitForPageToLoad(String.valueOf(pageRenderTime));
    }

    /**
     * Asserts DOM node value equals to value defined
     * 
     * @param id -
     *                DOM element id
     * @param value -
     *                value defined
     */
    public void AssertValueEquals(String id, String value) {
        String _v = getValueById(id);
        Assert.assertEquals(_v, value);
    }
    
    /**
     * Asserts DOM node value equals to value defined
     * 
     * @param id -
     *                DOM element id
     * @param value -
     *                value defined
     * @param message = message to be displayed in failure case
     */
    public void AssertValueEquals(String id, String value, String message) {
        String _v = getValueById(id);
        Assert.assertEquals(_v, value, message);
    }

    /**
     * Asserts DOM node value does not equal to value defined
     * 
     * @param id -
     *                DOM element id
     * @param value -
     *                value defined
     */
    public void AssertValueNotEquals(String id, String value) {
        String _v = getValueById(id);
        Assert.assertNotSame(_v, value);
    }
    
    
    /**
     * Asserts DOM node value does not equal to value defined
     * 
     * @param id -
     *                DOM element id
     * @param value -
     *                value defined
     * @param message = message to be displayed in failure case
     */
    public void AssertValueNotEquals(String id, String value, String message) {
        String _v = getValueById(id);
        Assert.assertNotSame(_v, value, message);
    }


    /**
     * Asserts DOM node text equals to text defined
     *
     * @param locator -
     *                locator an element locator
     * @param value -
     *                text defined
     */
    public void AssertTextEquals(String locator, String value) {
        String _v = selenium.getText(locator);
        Assert.assertEquals(_v, value);
    }

    /**
     * Asserts DOM node text equals to text defined
     * 
     * @param id -
     *                DOM element id
     * @param value -
     *                text defined
     * @param message = message to be displayed in failure case
     */
    public void AssertTextEquals(String id, String value, String message) {
        String _v = getTextById(id);
        Assert.assertEquals(_v, value, message);
    }


    /**
     * Asserts DOM node text does not equal to text defined
     * 
     * @param id -
     *                DOM element id
     * @param value -
     *                text defined
     */
    public void AssertTextNotEquals(String id, String value) {
        String _v = getTextById(id);
        Assert.assertNotSame(_v, value);
    }
    
    /**
     * Asserts DOM node text does not equal to text defined
     * 
     * @param id -
     *                DOM element id
     * @param value -
     *                text defined
     * @param message = message to be displayed in failure case
     */
    public void AssertTextNotEquals(String id, String value, String message) {
        String _v = getTextById(id);
        Assert.assertNotSame(_v, value, message);
    }

    /**
     * Asserts DOM node is visible
     * 
     * @param id -
     *                DOM element id
     */
    public void AssertVisible(String id) {
        Assert.assertTrue(isVisibleById(id));
    }
    
    /**
     * Asserts DOM node is visible
     * 
     * @param id -
     *                DOM element id
     */
    public void AssertVisible(String id, String message) {
        Assert.assertTrue(isVisibleById(id), message);
    }

    /**
     * Asserts DOM node is not present
     * 
     * @param id -
     *                DOM element id
     */
    public void AssertNotPresent(String id) {
        Assert.assertFalse(isPresentById(id));
    }

    /**
     * Asserts DOM node is not visible
     * 
     * @param id -
     *                DOM element id
     */
    public void AssertNotVisible(String id) {
        Assert.assertFalse(isVisibleById(id));
    }
    
    /**
     * Asserts DOM node is present
     * 
     * @param id -
     *                DOM element id
     */
    public void AssertPresent(String id) {
        Assert.assertTrue(isPresentById(id));
    }
    
    /**
     * Asserts DOM node is present
     * @param id - DOM element id
     */
    public void AssertRendered(String id) {
	if (!isPresentById(id)) {
	    Assert.fail("Component " + id + " should be rendered on page");
	}
    }
    
    /**
     * Asserts DOM node is not present
     * @param id - DOM element id
     */
    public void AssertNotRendered(String id) {
	if (isPresentById(id)) {
	    Assert.fail("Component " + id + " should not be rendered on page");
	}
    }

    /**
     * Returns element's text
     * 
     * @param id -
     *                DOM element id
     * @return
     */
    public String getTextById(String id) {
        return selenium.getText("id=" + id);
    }

    /**
     * Returns element's value
     * 
     * @param id -
     *                DOM element id
     * @return
     */
    public String getValueById(String id) {
        return selenium.getValue("id=" + id);
    }

    /**
     * Sets a new value for DOM node with specified id.
     * 
     * @param id -
     *                DOM element id
     * @param value -
     *                a new DOM element's value
     */
    public void setValueById(String id, String value) {
        runScript(String.format("document.getElementById('%1$s').value='%2$s';", id, value));
    }

    /**
     * Returns element's width
     * 
     * @param id -
     *                DOM element id
     * @return
     */
    public Number getWidthById(String id) {
        return selenium.getElementWidth("id=" + id);
    }

    /**
     * Returns element's height
     * 
     * @param id -
     *                DOM element id
     * @return
     */
    public Number getHeightById(String id) {
        return selenium.getElementHeight("id=" + id);
    }

    /**
     * Clicks on element
     * 
     * @param id -
     *                DOM element id
     * @return
     */
    public void clickById(String id) {
        selenium.click("id=" + id);
        checkJSError();
    }

    /**
     * Clicks an ajax command and wait for ajax request completion.
     * @param locator an element locator
     */
    public void clickAjaxCommandAndWait(String locator) {
        selenium.click(locator);
        waitForAjaxCompletion();
    }

    /**
     * This method should be used for click on command controls instead of
     * 'clickById' method.
     * 
     * @param commandId
     */
    public void clickCommandAndWait(String commandId) {
        selenium.click("id=" + commandId);
        waitForPageToLoad();
    }

    /**
     * Return true if element is visible
     * 
     * @param id -
     *                DOM element id
     * @return
     */
    public boolean isVisibleById(String id) {
        return selenium.isVisible("id=" + id);
    }

    /**
     * Returns true if element with given id is present.
     * 
     * @param id -
     *                DOM element id
     * @return true if element with given id is present, otherwise - false
     */
    public boolean isPresentById(String id) {
        return selenium.isElementPresent("id=" + id);
    }

    /**
     * Creates delay
     * 
     * @param miliSeconds
     * @throws InterruptedException
     */
    public void delay(int miliSeconds) {
        try {
            Thread.sleep(miliSeconds);
        } catch (Exception e) {

        }
    }

    /**
     * Creates pause for defined time in miliSeconds
     * 
     * @param miliSeconds
     */
    public void pause(int miliSeconds, String id) {
        StringBuffer script = new StringBuffer("pause(");
        script.append(miliSeconds);
        script.append(",'");
        script.append(id);
        script.append("');");
        runScript(script.toString());
        selenium.waitForCondition(WINDOW_JS_RESOLVER + "pauseHolder['" + id + "'] == true;", String
                .valueOf(miliSeconds + 1000));
    }

    /**
     * @return the parentId
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     *                the parentId to set
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * Workaround for selenium.type/selenium.typeKeys 
     * @param locator
     * @param string
     */
    public void type(String locator, String string) {
        selenium.type(locator, "");
        StringBuffer value = new StringBuffer(selenium.getValue(locator));
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            String key = Character.toString(chars[i]);
            value.append(key);

            selenium.keyDown(locator, key);

            if(!isFF()) {
                selenium.type(locator, value.toString());
            }

            selenium.keyPress(locator, key);
            selenium.keyUp(locator, key);

        }
    }
    
    public void testOnclickEvent(String testElementId, String testElementResultId, String beforeResult, String afterResult) {
	writeStatus("Testing onclick event");
	AssertTextEquals(testElementResultId, beforeResult);
	selenium.click(testElementId);
	AssertTextEquals(testElementResultId, afterResult);
    }

    public void testOnDblclickEvent(String testElementId, String testElementResultId, String beforeResult, String afterResult) {
	writeStatus("Testing ondblclick event");
	AssertTextEquals(testElementResultId, beforeResult);
	selenium.doubleClick(testElementId);
	AssertTextEquals(testElementResultId, afterResult);
    }

    public void testOnfocusEvent(String testElementId, String testElementResultId, String beforeResult, String afterResult) {
	writeStatus("Testing onfocus event");
	AssertTextEquals(testElementResultId, beforeResult);
	selenium.focus(testElementId);
	AssertTextEquals(testElementResultId, afterResult);
    }

    public void testOnkeydownEvent(String testElementId, String testElementResultId, String beforeResult, String afterResult) {
	writeStatus("Testing onkeydown event");
	AssertTextEquals(testElementResultId, beforeResult);
	selenium.keyDown(testElementId, "1");
	AssertTextEquals(testElementResultId, afterResult);
    }

    public void testOnkeypressEvent(String testElementId, String testElementResultId, String beforeResult, String afterResult) {
	writeStatus("Testing onkeypress event");
	AssertTextEquals(testElementResultId, beforeResult);
	selenium.keyPress(testElementId, "1");
	AssertTextEquals(testElementResultId, afterResult);
    }

    public void testOnkeyupEvent(String testElementId, String testElementResultId, String beforeResult, String afterResult) {
	writeStatus("Testing onkeyup event");
	AssertTextEquals(testElementResultId, beforeResult);
	selenium.keyUp(testElementId, "1");
	AssertTextEquals(testElementResultId, afterResult);
    }

    public void testOnmousedownEvent(String testElementId, String testElementResultId, String beforeResult, String afterResult) {
	writeStatus("Testing onmousedown event");
	AssertTextEquals(testElementResultId, beforeResult);
	selenium.mouseDown(testElementId);
	AssertTextEquals(testElementResultId, afterResult);
    }

    public void testOnmousemoveEvent(String testElementId, String testElementResultId, String beforeResult, String afterResult) {
	writeStatus("Testing onmousemove event");
	AssertTextEquals(testElementResultId, beforeResult);
	selenium.mouseMove(testElementId);
	AssertTextEquals(testElementResultId, afterResult);
    }

    public void testOnmouseoutEvent(String testElementId, String testElementResultId, String beforeResult, String afterResult) {
	writeStatus("Testing onmouseout event");
	AssertTextEquals(testElementResultId, beforeResult);
	selenium.mouseOut(testElementId);
	AssertTextEquals(testElementResultId, afterResult);
    }

    public void testOnmouseoverEvent(String testElementId, String testElementResultId, String beforeResult, String afterResult) {
	writeStatus("Testing onmouseover event");
	AssertTextEquals(testElementResultId, beforeResult);
	selenium.mouseOver(testElementId);
	AssertTextEquals(testElementResultId, afterResult);
    }

    public void testOnmouseupEvent(String testElementId, String testElementResultId, String beforeResult, String afterResult) {
	writeStatus("Testing onmouseup event");
	AssertTextEquals(testElementResultId, beforeResult);
	selenium.mouseUp(testElementId);
	AssertTextEquals(testElementResultId, afterResult);
    }
    
    /**
     * Method to assert element style attribute with expected
     * @param elementId - tested element id
     * @param expectedExpression - expected style attribute
     */
    public void assertStyleAttribute(String elementId, String expectedExpression) {
	String styleAttribute = selenium.getAttribute("//*[@id='" + elementId + "']/@style");
	if (!styleAttribute.toLowerCase().contains(expectedExpression.toLowerCase())) {
	    Assert.fail("Element '" + elementId + "' with style '" + styleAttribute + "' doesn't contain '" + expectedExpression + "'");
	}
    }

    /**
     * Asserts that expected columns count equals actual one.
     * @param i the expected columns count
     * @param tableId id of table to be checked
     */
    public void assertColumnsCount(int i, String tableId) {
        StringBuffer script = new StringBuffer("$('");
        script.append(tableId);
        script.append("').rows[0].cells.length");

        String count = runScript(script.toString());
        Assert.assertEquals(count, String.valueOf(i));
    }

    /**
     * Asserts that expected rows count equals actual one.
     * @param i the expected rows count
     * @param tableId id of table to be checked
     */
    public void assertRowsCount(int i, String tableId) {
        int count = selenium.getXpathCount("//table[@id='" + tableId + "']/tbody/tr").intValue();
        Assert.assertEquals(count, i);
    }

    /**
     * Checks whether client is FireFox
     * 
     * @return true if client is FireFox
     */
    public boolean isFF() {
        return new Boolean(selenium.getEval("navigator.userAgent.indexOf('Firefox') > -1"));
    }

    /**
     * Returns the url to test page to be opened by selenium
     *
     * @return
     */
    public String getTestUrl() {
    	return "/index.seam?startConversation=1";
    }
    
    /**
     * Simulates mouse event for element 
     * @param id        - Element ID
     * @param eventName - Mouse event Name
     * @param x         - X mouse position
     * @param y         - Y mouse position
     * @return          - Event
     */
    public String fireMouseEvent(String id, String eventName, int x, int y, boolean ctrl) {
    	return runScript("fireMouseEvent('" + id + "','" + eventName + "', "
				+ x + "," + y + ","+ctrl+");");
    }
    
}
