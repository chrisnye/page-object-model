package com.optum.bc4.tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.optum.bc4.pageObjects.DashboardPage;
import com.optum.bc4.pageObjects.LoginPage;

public class ContactTest extends BaseTest {
	
	@Test 
	public void basicTest() {
		
		WebDriver driver = getWebDriver();
		driver.get(BC4_URL);
		LoginPage login = new LoginPage(driver);
		DashboardPage dashboard = login.loginValidCredentials("", "");
		dashboard.searchLoadContact("Genedit", "Atest");
	}

}
