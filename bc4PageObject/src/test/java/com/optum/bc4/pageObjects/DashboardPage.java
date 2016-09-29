package com.optum.bc4.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends PageBase {


	// Page elements
	@FindBy(id = "create-announcement-button")
	public WebElement createAnnouncementBtn;
	@FindBy(id = "announceContainer")
	public WebElement announceContainer;
	@FindBy(id = "search")
	public WebElement contactSearchBtn;

	public DashboardPage(WebDriver driverParam) {
		super(driverParam);
	}

	public void searchLoadContact(String string, String string2) {
		// TODO Auto-generated method stub
		
	}
}
