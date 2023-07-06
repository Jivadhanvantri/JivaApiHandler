package com.jiva.app.dtos;

import java.io.Serializable;

public class ClinicApptRequestDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String aptdate;
	private String dhandid;
	private String ptname;
	private String phone;
	private String groupname;
	private String createId;
	public String getAptdate() {
		return aptdate;
	}
	public void setAptdate(String aptdate) {
		this.aptdate = aptdate;
	}
	public String getDhandid() {
		return dhandid;
	}
	public void setDhandid(String dhandid) {
		this.dhandid = dhandid;
	}
	public String getPtname() {
		return ptname;
	}
	public void setPtname(String ptname) {
		this.ptname = ptname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	@Override
	public String toString() {
		return "ClinicApptRequestDto [aptdate=" + aptdate + ", dhandid=" + dhandid + ", ptname=" + ptname + ", phone="
				+ phone + ", groupname=" + groupname + ", createId=" + createId + "]";
	}
	
	
	
	
}
