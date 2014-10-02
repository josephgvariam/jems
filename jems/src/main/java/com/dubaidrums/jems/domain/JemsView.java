package com.dubaidrums.jems.domain;

public class JemsView {

	private String eventDate;
	private String eventNumber;
	private String organization;
	private String eventType;
	private String eventStatus;
	private String eventTitle;
	private String eventDescription;
	private String numberFacilitators;
	private String numberPeople;
	private String companyName;
	private String companyContactPerson;
	private String companyContactNumber;
	private String companyEmail;
	private String currency;
	private String location;
	private String gps;
	private String country;
	private String region;
	private String numberDrums;
	private String numberSessions;
	private String chairRequired;
	private String staffAssigned;
	private String recieptVoucher;
	private String quotationDate;
	private String quotationNumber;
	private String invoiceDate;
	private String invoiceNumber;
	private String quotedAmount;
	private String invoicedAmount;
	private String paidStatus;
	private String paidAmount;
	private String paidDate;
	private String eventLink;
	private String quotationLink;
	private String invoiceLink;

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventNumber() {
		return eventNumber;
	}

	public void setEventNumber(String eventNumber) {
		this.eventNumber = eventNumber;
	}

	public String getOrganization() {
		return format(organization);
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getEventTitle() {
		return format(eventTitle);
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getEventDescription() {
		return format(eventDescription);
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getNumberFacilitators() {
		return numberFacilitators;
	}

	public void setNumberFacilitators(String numberFacilitators) {
		this.numberFacilitators = numberFacilitators;
	}

	public String getNumberPeople() {
		return numberPeople;
	}

	public void setNumberPeople(String numberPeople) {
		this.numberPeople = numberPeople;
	}

	public String getCompanyName() {
		return format(companyName);
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyContactPerson() {
		return format(companyContactPerson);
	}

	public void setCompanyContactPerson(String companyContactPerson) {
		this.companyContactPerson = companyContactPerson;
	}

	public String getCompanyContactNumber() {
		return format(companyContactNumber);
	}

	public void setCompanyContactNumber(String companyContactNumber) {
		this.companyContactNumber = companyContactNumber;
	}

	public String getCompanyEmail() {
		return format(companyEmail);
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getLocation() {
		return format(location);
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getNumberDrums() {
		return numberDrums;
	}

	public void setNumberDrums(String numberDrums) {
		this.numberDrums = numberDrums;
	}

	public String getNumberSessions() {
		return numberSessions;
	}

	public void setNumberSessions(String numberSessions) {
		this.numberSessions = numberSessions;
	}

	public String getChairRequired() {
		return chairRequired;
	}

	public void setChairRequired(String chairRequired) {
		this.chairRequired = chairRequired;
	}

	public String getStaffAssigned() {
		return staffAssigned;
	}

	public void setStaffAssigned(String staffAssigned) {
		this.staffAssigned = staffAssigned;
	}

	public String getRecieptVoucher() {
		return recieptVoucher;
	}

	public void setRecieptVoucher(String recieptVoucher) {
		this.recieptVoucher = recieptVoucher;
	}

	public String getQuotationDate() {
		return quotationDate;
	}

	public void setQuotationDate(String quotationDate) {
		this.quotationDate = quotationDate;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getQuotedAmount() {
		return quotedAmount;
	}

	public void setQuotedAmount(String quotedAmount) {
		this.quotedAmount = quotedAmount;
	}

	public String getInvoicedAmount() {
		return invoicedAmount;
	}

	public void setInvoicedAmount(String invoicedAmount) {
		this.invoicedAmount = invoicedAmount;
	}

	public String getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(String paidStatus) {
		this.paidStatus = paidStatus;
	}

	public String getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}

	public String getEventLink() {
		return eventLink;
	}

	public void setEventLink(String eventLink) {
		this.eventLink = eventLink;
	}

	public String getQuotationLink() {
		return quotationLink;
	}

	public void setQuotationLink(String quotationLink) {
		this.quotationLink = quotationLink;
	}

	public String getInvoiceLink() {
		return invoiceLink;
	}

	public void setInvoiceLink(String invoiceLink) {
		this.invoiceLink = invoiceLink;
	}

	public String getQuotationNumber() {
		return quotationNumber;
	}

	public void setQuotationNumber(String quotationNumber) {
		this.quotationNumber = quotationNumber;
	}

	private String format(String s) {
		return "\"" + s + "\"";
	}

}
