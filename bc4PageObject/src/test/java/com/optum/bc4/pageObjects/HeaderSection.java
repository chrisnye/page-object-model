package com.optum.bc4.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page object class to interact with Application header
 * 
 * @author Chris Nye
 */
public class HeaderSection extends PageBase {

	// Page elements
	public WebElement logoutbtn;

	public HeaderSection(WebDriver driverParam) {
		super(driverParam);
	}
	
	public LoginPage logout() {
		logoutbtn.click();
		ModalLogout modal = new ModalLogout(driver);
		waitForElement(modal.yesLogoutSessionBtn, 5);
		modal.closeModal(modal.yesLogoutSessionBtn);
		return new LoginPage(driver);
	}
}
