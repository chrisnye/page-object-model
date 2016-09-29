package com.optum.bc4.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;

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

}
