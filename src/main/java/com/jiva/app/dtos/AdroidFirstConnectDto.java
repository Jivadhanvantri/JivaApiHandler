package com.jiva.app.dtos;

import java.io.Serializable;

public class AdroidFirstConnectDto implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String caseId;
	private String contactId;
	private String assignedUserId;
	private String phone1;
	private String phone2;
	private String creatId;
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getAssignedUserId() {
		return assignedUserId;
	}
	public void setAssignedUserId(String assignedUserId) {
		this.assignedUserId = assignedUserId;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getCreatId() {
		return creatId;
	}
	public void setCreatId(String creatId) {
		this.creatId = creatId;
	}
	@Override
	public String toString() {
		return "AdroidFirstConnectDto [caseId=" + caseId + ", contactId=" + contactId + ", assignedUserId="
				+ assignedUserId + ", phone1=" + phone1 + ", phone2=" + phone2 + ", creatId=" + creatId + "]";
	}
	
	
	
}
