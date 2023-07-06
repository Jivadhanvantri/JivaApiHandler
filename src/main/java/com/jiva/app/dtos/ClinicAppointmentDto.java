package com.jiva.app.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class ClinicAppointmentDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotBlank(message = "Clinic group should not be empty")
	private String clinicGroup;
	@NotBlank(message = "Appointment date should not be empty")
	private String appointmentDt;
	
	public String getClinicGroup() {
		return clinicGroup;
	}
	public void setClinicGroup(String clinicGroup) {
		this.clinicGroup = clinicGroup;
	}
	public String getAppointmentDt() {
		return appointmentDt;
	}
	public void setAppointmentDt(String appointmentDt) {
		this.appointmentDt = appointmentDt;
	}
	@Override
	public String toString() {
		return "ClinicAppointmentDto [clinicGroup=" + clinicGroup + ", appointmentDt=" + appointmentDt + "]";
	}
}
