package com.optum.bc4.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.optum.bc4.pageObjects.HeaderSection;
import com.optum.bc4.utils.ResourcePool;
import com.optum.bc4.utils.TestUtils;

public class BaseTest {

	Properties prop = new Properties();
	final String BC4_URL;
	final String USERNAME;
	final String PASSWORD;

	protected ThreadLocal<WebDriver> threadDriver = new ThreadLocal<WebDriver>();
	private static ResourcePool<WebDriver> driverPool = new ResourcePool<WebDriver>();

	BaseTest() {
		try {
			prop.load(new FileInputStream("bc4.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BC4_URL = prop.getProperty("bc4Url");
		USERNAME = prop.getProperty("username");
		PASSWORD = prop.getProperty("password");
	}

	@BeforeMethod
	@Parameters({ "browser" })
	public void beforeMethod(@Optional("firefox") String browser) {

		threadDriver.set(createWebDriverLocal(browser));

	}

	protected WebDriver createWebDriverLocal(String browser) {
		WebDriver thisDriver = driverPool.getResource();
		if (thisDriver != null) {
			return thisDriver;
		}

		DesiredCapabilities capabilities = new DesiredCapabilities();

		switch (browser) {
		case "firefox":
			ProfilesIni myProfile = new ProfilesIni();
			FirefoxProfile ffprofile = myProfile.getProfile("default");
			return new FirefoxDriver(ffprofile);

		case "chrome":
			String os = System.getProperty("os.name").toLowerCase();
			if (os.contains("win")) {
				// Assumes web driver executable is in 'Drivers' directory under
				// project.
				System.setProperty("webdriver.chrome.driver", "Drivers" + File.separator + "chromedriver.exe");
				ChromeDriverService service = ChromeDriverService.createDefaultService();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("test-type");
				options.addArguments("--start-maximized");
				options.addArguments("--disable-extensions");
				return new ChromeDriver(service, options);
			} else {
				// Assumes web driver executable is in 'Drivers' directory under
				// project.
				System.setProperty("webdriver.chrome.driver", "Drivers" + File.separator + "chromedriver");
				return new ChromeDriver();
			}

		case "ie":
			// Assumes web driver executable is in 'Drivers' directory under
			// project.
			capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(CapabilityType.BROWSER_NAME, "internet explorer");
			capabilities.setCapability(CapabilityType.VERSION, "11");
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			System.setProperty("webdriver.ie.driver", "Drivers" + File.separator + "IEDriverServer.exe");
			return new InternetExplorerDriver(capabilities);

		case "safari":
			return new SafariDriver();

		default:
			Assert.fail("FAIL - Invalid browser parameter passed");

		}

		return null;
	}

	@AfterMethod
	public void afterMethod() {
		WebDriver driver = getWebDriver();

		HeaderSection header = new HeaderSection(driver);

		if (TestUtils.isElementDisplayed(header.logoutbtn)) {
			header.logout();
		}
		if (driver != null) {
			// When driver is local and has not been invalidated, add back to
			// pool

			// Indicate unused browser instance by navigating to "about:blank"
			driver.get("about:blank");
			// Ensure that if alert exists click accept
			try {
				driver.switchTo().alert().accept();
			} catch (NoAlertPresentException e) { // no alert found, continue
			}

			driverPool.returnResource(driver);
		}
	}

	@AfterSuite
	public void afterSuite() {
		// Close down opened drivers stored in driverPool
		WebDriver currDriver = driverPool.getResource();
		while (currDriver != null) {

			try {
				currDriver.close();
				currDriver.quit();
			} catch (Exception e) {
				// consume exceptions with nothing else.
				e.printStackTrace();
			}
			currDriver = driverPool.getResource();
		}
	}

	/**
	 * @return the {@link WebDriver} for the current thread
	 */
	public WebDriver getWebDriver() {
		// System.out.println("WebDriver " + threadDriver.get());
		return threadDriver.get();
	}
}
