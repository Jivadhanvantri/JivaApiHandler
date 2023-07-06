package com.jiva.app.dtos;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class IperformanceObDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Size(min = 1)
	@Size(max = 30,message = "Name can not be greater than 30 chars")
	
	private String pName;
	@Size(min = 10)
	@Size(max = 10,message = "Phone should be 10 digit")

	private String pPhone;
	
	@Size(min = 1)
	@Size(max = 40,message = "Categoty can not be greater than 40 chars")
	private String pCategory;
	
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getpPhone() {
		return pPhone;
	}
	public void setpPhone(String pPhone) {
		this.pPhone = pPhone;
	}
	public String getpCategory() {
		return pCategory;
	}
	public void setpCategory(String pCategory) {
		this.pCategory = pCategory;
	}
	@Override
	public String toString() {
		return "IperformanceObDto [pName=" + pName + ", pPhone=" + pPhone + ", pCategory=" + pCategory + "]";
	}
	
	
}
