package com.optum.bc4.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

	/**
	 * @author Chris Nye
	 * @description Helper method to wait on the UI to display a desired element
	 * @param element
	 *          Selector for element to be searched for
	 * @param seconds
	 *          Number of seconds to wait before timing out
	 * @return true: element was found and is being displayed<br>
	 *         false: element was not found or is not being displayed
	 * 
	 */
	public boolean waitForElement(WebElement element, int seconds) {
	
	  boolean elementDisplayedBool = false;
	
	  // Intialize wait object on current driver, with timeout set to 'seconds'
	  // parameter
	  WebDriverWait wait = new WebDriverWait(driver, seconds);
	
	  try {
	    // Wait condition is to be that element exists, it is displayed and spinner/loading overlay is invisible
	    wait.until(ExpectedConditions.and(
	        ExpectedConditions.visibilityOf(element),
	        ExpectedConditions.invisibilityOfElementLocated(By.id("modalSpinner"))
	        ));
	
	    // If exception is not thrown, element was found, set displayedElement to
	    // true
	    elementDisplayedBool = true;
	  } catch (TimeoutException e) {
	    //log.info("Did not find a visible element with [" + by.toString() + "]");
	    elementDisplayedBool = false;
	  } catch (Exception e) {
	    //log.error("FAIL - Generic exception e [" + e.getMessage() + "]");
	  	e.printStackTrace();
	    elementDisplayedBool = false;
	  }
	
	  /*
	  // Check page load status, reset Timeout to use default shortTimeout duration if input time is shorter
	  if ( seconds < shortTimeout) {
	  	wait.withTimeout(shortTimeout, TimeUnit.SECONDS);
	  }    
	  // Also only wait on this if displayedElement is true, 
	  // loadstatus exists with data-xhr other than init.
	  if ( displayedElement &&
	  		isElementPresent(By.cssSelector("div#loadstatus")) &&
	      driver.findElement(By.cssSelector("div#loadstatus")).getAttribute("data-xhr") != null &&
	      ! "init".equals(driver.findElement(By.cssSelector("div#loadstatus")).getAttribute("data-xhr")) ) {
	  	try {
	  		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div#loadstatus[data-xhr='completed']")));
	  	} catch (TimeoutException e) {
	  		Assert.fail("FAIL - page loadstatus did not indicate complete after waiting for [" + by.toString() + "], "
	  				+ "final page status of data-xhr: [" + driver.findElement(By.cssSelector("div#loadstatus")).getAttribute("data-xhr") + "]", 
	  				e);
	  	}
	  }
	  */
	  return elementDisplayedBool;
	}

}
