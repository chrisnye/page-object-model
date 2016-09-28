package com.optum.bc4.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ContactFormSales extends ContactFormBase {

	// Sales specific Contact fields
	public WebElement doNotEmail;
	public WebElement householdIncome;
	public WebElement householdIncomeFrequency;
	public WebElement annualHouseholdIncome;
	@FindBy(css="div[name='oseSep'] label[data-value='oe_eligible']")
	public WebElement oeEligible;
	@FindBy(css="div[name='oseSep'] label[data-value='sep_eligible']")
	public WebElement sepEligible;
	public WebElement sepTriggerEvent;
	public WebElement sepTriggerEventDate;
	@FindBy(css="div[name='groupMember'] label[data-value='yes']")
	public WebElement groupMemberYes;
	@FindBy(css="div[name='groupMember'] label[data-value='no']")
	public WebElement groupMemberNo;
	@FindBy(css="div[name='insured'] label[data-value='yes']")
	public WebElement currentlyInsuredYes;
	@FindBy(css="div[name='insured'] label[data-value='no']")
	public WebElement currentlyInsuredNo;
	public WebElement otherInsurance;
	public WebElement smallBusiness;
	public WebElement nativeAmerican;
	public WebElement hipaaNotes;
	public WebElement referralFirstName;
	public WebElement referralLastName;
	public WebElement referralLastEmail;
	
	
	public ContactFormSales(WebDriver driverParam) {
		super(driverParam);
		
	}

}
