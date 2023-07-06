package com.jiva.app.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

public class ClinicFreshCallDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message = "Case Id can not be blank.")
	private String caseId;
	@NotEmpty(message = "Phone can not be blank.")
	private String phone;
	@NotEmpty(message = "clinic name can not be blank.")
	private String clinicName;
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
	public String getClinicName() {
		return clinicName;
	}
	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}
	@Override
	public String toString() {
		return "ClinicFreshCallDto [caseId=" + caseId + ", phone=" + phone + ", clinicName=" + clinicName + "]";
	}
	
	
	
}
