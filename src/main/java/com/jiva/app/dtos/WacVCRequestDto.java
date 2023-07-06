package com.jiva.app.dtos;

import java.io.Serializable;

public class WacVCRequestDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String phone;
	private String patientName;
	private String botId;
	private String dhanId;
	private String consult_area;
	private String specialty;
	private String patient_age;
	private String gender;
	private String language;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getBotId() {
		return botId;
	}
	public void setBotId(String botId) {
		this.botId = botId;
	}
	public String getDhanId() {
		return dhanId;
	}
	public void setDhanId(String dhanId) {
		this.dhanId = dhanId;
	}
	public String getConsult_area() {
		return consult_area;
	}
	public void setConsult_area(String consult_area) {
		this.consult_area = consult_area;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getPatient_age() {
		return patient_age;
	}
	public void setPatient_age(String patient_age) {
		this.patient_age = patient_age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	@Override
	public String toString() {
		return "WacVCRequestDto [phone=" + phone + ", patientName=" + patientName + ", botId=" + botId + ", dhanId="
				+ dhanId + ", consult_area=" + consult_area + ", specialty=" + specialty + ", patient_age="
				+ patient_age + ", gender=" + gender + ", language=" + language + "]";
	}
	
	
	
	
	
}
