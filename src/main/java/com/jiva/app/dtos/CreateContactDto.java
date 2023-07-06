package com.jiva.app.dtos;

import java.io.Serializable;

public class CreateContactDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mobile;
	private String name;
	
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "CreateContactDto [mobile=" + mobile + ", name=" + name + "]";
	}
	
}
