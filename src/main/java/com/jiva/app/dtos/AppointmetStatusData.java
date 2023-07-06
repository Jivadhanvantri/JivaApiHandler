package com.jiva.app.dtos;

import java.io.Serializable;

public class AppointmetStatusData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dhanId;
	private String patientName;
	private String appointmentStatus;
	private String clinicName;
	private String patientType;
	private String appointmentDate;
	private String createDt;
	

	public String getDhanId() {
		return dhanId;
	}
	public void setDhanId(String dhanId) {
		this.dhanId = dhanId;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getAppointmentStatus() {
		return appointmentStatus;
	}
	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}
	public String getClinicName() {
		return clinicName;
	}
	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}
	public String getPatientType() {
		return patientType;
	}
	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}
	public String getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public String getCreateDt() {
		return createDt;
	}
	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}
	@Override
	public String toString() {
		return "AppointmetStatusData [dhanId=" + dhanId + ", patientName=" + patientName + ", appointmentStatus="
				+ appointmentStatus + ", clinicName=" + clinicName + ", patientType=" + patientType
				+ ", appointmentDate=" + appointmentDate + ", createDt=" + createDt + "]";
	}
	
	

}
