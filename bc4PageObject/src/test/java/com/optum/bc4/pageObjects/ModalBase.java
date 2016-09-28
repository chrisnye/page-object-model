package com.optum.bc4.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ModalBase extends PageBase {

	By backgroundBy = By.cssSelector("div.modal-background.fade.in");
	
	public ModalBase(WebDriver driverParam) {
		super(driverParam);
	}

	public void closeModal(WebElement element) {
		element.click();
		waitDefault.until(ExpectedConditions.invisibilityOfElementLocated(backgroundBy));
		
	}

}
