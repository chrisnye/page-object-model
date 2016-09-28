package com.optum.bc4.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ContactFormBase extends PageBase {
	
	// Form header button elements
	@FindBy(id="savecontact") 
	public WebElement saveButton;
	@FindBy(id="cancel") 
	public WebElement cancelButton;
	@FindBy(id="updateLeadSource") 
	public WebElement updateLeadButton;
	
	// Form messaging elements
	@FindBy(id="mesholder") 
	public WebElement successMessage;
	@FindBy(id="errormessage") 
	public WebElement errorMessage; 

	// Form fields
	//NOTE: these are the fields shared across all orgs, others to be added in sub-classes
	//TODO: Trying out not using FindBy to see if PageFactory maps 
	//		field name to element name selector
	public WebElement customerID;
	public WebElement deceased;
		public WebElement notInterested;
	@FindBy(name="title")
	public WebElement salutation;
	public WebElement firstName;
	public WebElement middleInitial;
	public WebElement lastName;
	public WebElement suffix;
	@FindBy(css="contact input[name='dob']")
	public WebElement dob;
	public WebElement dobAge;
	@FindBy(css="div[name='gender'] label[data-value='male']")
	public WebElement maleSelect;
	@FindBy(css="div[name='gender'] label[data-value='female']")
	public WebElement femaleSelect;
	public WebElement address1;
	public WebElement address2;
	public WebElement city;
	public WebElement state;
	public WebElement county;
	public WebElement zip;
	public WebElement zipPlusFour;
	public WebElement doNotMail;
	public WebElement same;
	public WebElement mailingAddress1;
	public WebElement mailingAddress2;
	public WebElement mailingCity;
	public WebElement mailingState;
	public WebElement mailingZip;
	public WebElement mailingZipPlusFour;
	public WebElement mailingCountry;
	public WebElement doNotCall;
	@FindBy(name="phone")
	public WebElement phone_home;
	public WebElement phone_cell;
	public WebElement phone_other;
	public WebElement fax;
	public WebElement bestTime;
	public WebElement doNotEmail;
	public WebElement email;
	public WebElement email2;
	public WebElement languagePreference;
	public WebElement spokenLanguagePreference;
	@FindBy(css="div[name='married'] label[data-value='yes']")
	public WebElement marriedYes;
	@FindBy(css="div[name='married'] label[data-value='no']")
	public WebElement marriedNo;
	public WebElement children;
	

	public ContactFormBase(WebDriver driverParam) {
		super(driverParam);
		
	}
	
	public Boolean saveContactForm() {
		Boolean result = false;
		saveButton.click();
		
		return result;
	}
}
