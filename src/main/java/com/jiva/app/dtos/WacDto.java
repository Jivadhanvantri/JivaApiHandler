package com.jiva.app.dtos;

import java.io.Serializable;

public class WacDto implements Serializable{
	private String phone;
	private String patientName;
	private String dhanId;
	private String gender;
	private String age;
	private String disease;
	
	
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
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

	public String getDhanId() {
		return dhanId;
	}
	public void setDhanId(String dhanId) {
		this.dhanId = dhanId;
	}
	@Override
	public String toString() {
		return "WacDto [phone=" + phone + ", patientName=" + patientName + ", dhanId=" + dhanId
				+ "]";
	}
	
	
	
}
