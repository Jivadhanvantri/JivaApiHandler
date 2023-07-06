package com.jiva.app.dtos;

import java.io.Serializable;

public class PatientDataResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String place;
	private String stateName;
	private String cityName;
	private String orderId;
	private String dhanId;
	private String countryName;
	private String pincode;
	private String message;
	
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
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getDhanId() {
		return dhanId;
	}
	public void setDhanId(String dhanId) {
		this.dhanId = dhanId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "PatientDataResponse [name=" + name + ", place=" + place + ", stateName=" + stateName + ", cityName="
				+ cityName + ", orderId=" + orderId + ", dhanId=" + dhanId + ", countryName=" + countryName
				+ ", pincode=" + pincode + ", message=" + message + "]";
	}
	
	
	
	
}
