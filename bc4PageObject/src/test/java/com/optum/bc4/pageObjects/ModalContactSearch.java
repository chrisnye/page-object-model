package com.optum.bc4.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ModalContactSearch extends ModalBase {
	
	public WebElement yesLogoutSessionBtn;
	public WebElement noLogoutSessionBtn;

	public ModalContactSearch(WebDriver driver) {
		super(driver);
	}

}
