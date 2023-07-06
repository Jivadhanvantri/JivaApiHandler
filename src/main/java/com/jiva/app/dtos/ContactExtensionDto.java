package com.jiva.app.dtos;

import java.io.Serializable;

public class ContactExtensionDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String compaignName;
	private String leadCreateDate;
	private String disease;
	private String otherDisease;
	private String pincode;
	private String dob;
	private String caseCreateDate;
	public String getCompaignName() {
		return compaignName;
	}
	public void setCompaignName(String compaignName) {
		this.compaignName = compaignName;
	}
	public String getLeadCreateDate() {
		return leadCreateDate;
	}
	public void setLeadCreateDate(String leadCreateDate) {
		this.leadCreateDate = leadCreateDate;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	public String getOtherDisease() {
		return otherDisease;
	}
	public void setOtherDisease(String otherDisease) {
		this.otherDisease = otherDisease;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getCaseCreateDate() {
		return caseCreateDate;
	}
	public void setCaseCreateDate(String caseCreateDate) {
		this.caseCreateDate = caseCreateDate;
	}
	@Override
	public String toString() {
		return "ContactExtensionDto [compaignName=" + compaignName + ", leadCreateDate=" + leadCreateDate + ", disease="
				+ disease + ", otherDisease=" + otherDisease + ", pincode=" + pincode + ", dob=" + dob
				+ ", caseCreateDate=" + caseCreateDate + "]";
	}
	
	
	
}
