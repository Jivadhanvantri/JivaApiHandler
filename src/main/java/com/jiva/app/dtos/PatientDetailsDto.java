package com.jiva.app.dtos;

import java.io.Serializable;

public class PatientDetailsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String place;
	private String cityId;
	private String stateId;
	private String countryId;
	private String pincode;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getStateId() {
		return stateId;
	}
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	@Override
	public String toString() {
		return "PatientDetailsDto [name=" + name + ", place=" + place + ", cityId=" + cityId + ", stateId=" + stateId
				+ ", countryId=" + countryId + ", pincode=" + pincode + "]";
	}
	
	

}
