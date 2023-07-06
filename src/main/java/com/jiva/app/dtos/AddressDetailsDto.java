package com.jiva.app.dtos;

import java.io.Serializable;

public class AddressDetailsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cityName;
	private String stateName;
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	@Override
	public String toString() {
		return "AddressDetailsDto [cityName=" + cityName + ", stateName=" + stateName + "]";
	}
	
	
	
}
