package com.optum.bc4.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageBase {
	
	final WebDriver driver;
	final WebDriverWait waitDefault;
	protected final long shortTimeout = 15;
	
	public PageBase(WebDriver driverParam) {
		driver = driverParam;
		waitDefault = new WebDriverWait(driver, shortTimeout );
		PageFactory.initElements(driver, this);
		
	}

}
