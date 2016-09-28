package com.optum.bc4.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ModalLogout extends ModalBase {
	
	public WebElement yesLogoutSessionBtn;
	public WebElement noLogoutSessionBtn;

	public ModalLogout(WebDriver driver) {
		super(driver);
	}

}
