package com.jiva.app.dtos;

import java.io.Serializable;

public class ShareChatRequestDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String date;
	private String webcard;
	private String name;
	private String phone_no; 
	private String city;
	private String src;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getWebcard() {
		return webcard;
	}
	public void setWebcard(String webcard) {
		this.webcard = webcard;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone_no() {
		return phone_no;
	}
	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	@Override
	public String toString() {
		return "ShareChatRequestDto [date=" + date + ", webcard=" + webcard + ", name=" + name + ", phone_no="
				+ phone_no + ", city=" + city + ", src=" + src + "]";
	}

	
}
