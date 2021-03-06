package com.dubaidrums.jems.util.dashboard;

import java.math.BigDecimal;

public class OrgData {
	private String orgName;
	private int numQuotations;
	private int numInvoices;
	private BigDecimal quotationAmount;
	private BigDecimal invoiceAmount;
	private BigDecimal paidAmount;

	// public double getOutstandingAmount(){
	// return getInvoiceAmount()-getPaidAmount();
	// }
	// public double getRatio(){
	// return getNumInvoices()/getNumQuotations();
	// }
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public int getNumQuotations() {
		return numQuotations;
	}

	public void setNumQuotations(int numQuotations) {
		this.numQuotations = numQuotations;
	}

	public int getNumInvoices() {
		return numInvoices;
	}

	public void setNumInvoices(int numInvoices) {
		this.numInvoices = numInvoices;
	}

	public BigDecimal getQuotationAmount() {
		return quotationAmount;
	}

	public void setQuotationAmount(BigDecimal quotationAmount) {
		this.quotationAmount = quotationAmount;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orgName == null) ? 0 : orgName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrgData other = (OrgData) obj;
		if (orgName == null) {
			if (other.orgName != null)
				return false;
		} else if (!orgName.equals(other.orgName))
			return false;
		return true;
	}

}
