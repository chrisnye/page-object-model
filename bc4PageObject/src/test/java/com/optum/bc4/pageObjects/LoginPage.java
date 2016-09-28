package com.optum.bc4.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.optum.bc4.utils.TestUtils;

public class LoginPage extends PageBase {

	// Page elements
	@FindBy(id = "usernameInput")
	public WebElement usernameInput;
	@FindBy(id = "passwordInput")
	public WebElement passwordInput;
	@FindBy(id = "loginBtn")
	public WebElement loginButton;

	public LoginPage(WebDriver driverParam) {
		super(driverParam);		
		// Check that we're on the right page.
		if ( ! waitDefault.until(ExpectedConditions.visibilityOf(usernameInput)).isDisplayed() ) {
			// Alternatively, we could navigate to the login page, perhaps logging out
			// first
			throw new IllegalStateException(driver.getCurrentUrl() + " is not the login page or page is not displayed");
		}

	}

	public DashboardPage loginValidCredentials(String username, String password) {
		usernameInput.sendKeys(username);
		passwordInput.sendKeys(password);
		loginButton.click();

		DashboardPage nextPage = new DashboardPage(driver);
		if ( !TestUtils.waitForElement(nextPage.announceContainer, 15, driver) ) {
			throw new IllegalStateException(driver.getCurrentUrl() + " did not display dashboard");
		}
		return nextPage;

	}

}
