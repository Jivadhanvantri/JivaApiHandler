package com.jiva.app.dtos;

import java.io.Serializable;
import java.util.Date;

public class MvtDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MvtDto() {}
	
	
	
    private Long id;
    
    private String caseId;
    
	private String patientName;

	private String age;
 
	private String phoneNo;
  
	private String email;
 
	private String country;
   
	private String state;
   
	private String remarks;
   
	private String lookingFor;

	private String patientType;
   
	private String gender;
   
	
    private String appCreateId;
    
	private Date createDt;
	
	private String recommendation;

	private String docRemarks;
	
	private String appDocCreateId;
	
   
	private String doctorName;
	

	public String getAppCreateId() {
		return appCreateId;
	}

	public void setAppCreateId(String appCreateId) {
		this.appCreateId = appCreateId;
	}

	public String getAppDocCreateId() {
		return appDocCreateId;
	}

	public void setAppDocCreateId(String appDocCreateId) {
		this.appDocCreateId = appDocCreateId;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public String getDocRemarks() {
		return docRemarks;
	}

	public void setDocRemarks(String docRemarks) {
		this.docRemarks = docRemarks;
	}



	private void createDt() {
		createDt = new Date();
	} 
    
	public Date getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}


	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getLookingFor() {
		return lookingFor;
	}
	public void setLookingFor(String lookingFor) {
		this.lookingFor = lookingFor;
	}
	public String getPatientType() {
		return patientType;
	}
	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	
}
