package com.optum.bc4.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.optum.bc4.pageObjects.DashboardPage;
import com.optum.bc4.pageObjects.HeaderSection;
import com.optum.bc4.pageObjects.LoginPage;
import com.optum.bc4.utils.TestUtils;

public class LoginTest extends BaseTest {
	
	@Test
	public void loginValidation() {
		WebDriver driver = getWebDriver();
		driver.get(BC4_URL);
		
		LoginPage login = new LoginPage(driver);
		
		DashboardPage dashboard = login.loginValidCredentials(USERNAME, PASSWORD);
		Assert.assertTrue(TestUtils.isElementDisplayed(dashboard.announceContainer));
		
		HeaderSection header = new HeaderSection(driver);
		header.logout();
		
	}
	
	@Test
	public void checkPageElements() {
		WebDriver driver = getWebDriver();
		driver.get(BC4_URL);
		
		LoginPage login = new LoginPage(driver);
		
		Assert.assertTrue(login.usernameInput.isDisplayed(), "FAIL: Missing username field");
		Assert.assertTrue(TestUtils.isElementEnabled(login.usernameInput));
		Assert.assertEquals(login.usernameInput.getAttribute("placeholder"), "User Name");
		Assert.assertTrue(login.passwordInput.isDisplayed(), "FAIL: Missing password field");
		Assert.assertTrue(TestUtils.isElementEnabled(login.passwordInput));
		Assert.assertEquals(login.passwordInput.getAttribute("placeholder"), "Password");
		Assert.assertTrue(login.loginButton.isDisplayed(), "FAIL: Missing Login button");
		Assert.assertTrue(TestUtils.isElementEnabled(login.loginButton));
	}

}
