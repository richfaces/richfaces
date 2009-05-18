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
 * @author Andrey Markavtsov
 *
 */
public class LoginTest extends SeleniumTestBase {
	
		
	@Test
	public void testAuthentication() {
		renderPage();
		
		RealWorldHelper.login(selenium);
		RealWorldHelper.logout(selenium);
	}
	
	@Test
	public void testRegistration() {
		renderPage();
		String name = "New user";
		String password = "pass";
		selenium.click(HtmlConstants.LogInOutArea.REGISTER_PATH);
		waitForAjaxCompletion();
		RealWorldHelper.testUserProfile(selenium);
		selenium.type(HtmlConstants.RegistrationArea.NAME_ID, name);
		selenium.type(HtmlConstants.RegistrationArea.PASSWORD_ID, password);
		selenium.type(HtmlConstants.RegistrationArea.CONFIRM_PASSWORD_ID, password);
		selenium.type(HtmlConstants.RegistrationArea.FIRST_NAME_ID, "first");
		selenium.type(HtmlConstants.RegistrationArea.SECOND_NAME_ID, "second");
		selenium.type(HtmlConstants.RegistrationArea.BIRTH_DATE_ID, "Jan 1, 1");
		selenium.type(HtmlConstants.RegistrationArea.EMAIL_ID, "user@exadel.com");
		selenium.click(HtmlConstants.RegistrationArea.REGISTER_PATH);
		waitForAjaxCompletion();
		Assert.assertTrue(RealWorldHelper.isLogined(selenium, name));
	}
}
