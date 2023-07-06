package com.jiva.app.dtos;

import java.io.Serializable;

public class AdroidResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String adroidResponse;
	private String contactId;
	private String caseId;
	private String phone;
	public String getAdroidResponse() {
		return adroidResponse;
	}
	public void setAdroidResponse(String adroidResponse) {
		this.adroidResponse = adroidResponse;
	}
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString() {
		return "AdroidResponseDto [adroidResponse=" + adroidResponse + ", contactId=" + contactId + ", caseId=" + caseId
				+ ", phone=" + phone + "]";
	}

}
