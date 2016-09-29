package com.optum.bc4.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestUtils {

	/**
	 * helper method to supplement Selenium IDE because WebDriver does not have it
	 * 
	 * Differs from isElementPresent by checking that element is present AND 
	 * visible on the page.
	 */
	public static boolean isElementDisplayed(By by, WebDriver driver) {
		return isElementDisplayed(driver.findElement(by));
	}
	
	/**
	 * helper method to supplement Selenium IDE because WebDriver does not have it
	 * 
	 * Differs from isElementPresent by checking that element is present AND 
	 * visible on the page.
	 */
	public static boolean isElementDisplayed(WebElement element) {
	  Boolean displayed = false;
	  try {
	    displayed = element.isDisplayed();
	  } catch (NoSuchElementException e) {
	    displayed = false;
	  } catch (StaleElementReferenceException e1) {
	    // try one more time...
	    try {
	      displayed = element.isDisplayed();
	    } catch (NoSuchElementException | StaleElementReferenceException e) {
	      displayed = false;
	    }
	  }
	  
	  return displayed;
	}

	/**
	 * Tests whether or not given element is enabled.
	 * 
	 * {element}.isEnabled only returns false on INPUT elements with disabled attribute.  
	 * Non-input elements return true from that method, even with disabled.
	 * 
	 * In addition to checking isEnabled, test whether:
	 * 	1. element contains 'disabled' class (returns false)
	 *  2. element contains 'disabled' attribute (returns false)
	 * 
	 * @author Chris Nye
	 * @param selector 
	 * @return enabled state of element
	 */
	public static boolean isElementEnabled(By selector, WebDriver driver) {
		return isElementEnabled(driver.findElement(selector));
	}

	/**
	 * Tests whether or not given element is enabled.
	 * 
	 * {element}.isEnabled only returns false on INPUT elements with disabled attribute.  
	 * Non-input elements return true from that method, even with disabled attribute.
	 * 
	 * In addition to checking isEnabled, test whether:
	 * 	1. element contains 'disabled' class (returns 'false')
	 *  2. element contains 'disabled' attribute (returns 'false')
	 * 
	 * @author Chris Nye
	 * @param element
	 * @return enabled state of element
	 */
	public static boolean isElementEnabled(WebElement element) {
		return (element.isEnabled() 
				&& ! element.getAttribute("class").contains("disabled")  
				&& element.getAttribute("disabled") == null);
	}

	/**
	 * helper method to supplement Selenium IDE because WebDriver does not have it
	 */
	public static boolean isElementPresent(By by, WebDriver driver) {
	  
	  try {
	    driver.findElement(by);
	    return true;
	  } catch (NoSuchElementException e) {
	    return false;
	  }
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
	public static boolean waitForElement(WebElement element, int seconds, WebDriver driver) {
	
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
