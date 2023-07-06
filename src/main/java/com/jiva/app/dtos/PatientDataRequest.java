package com.jiva.app.dtos;

import java.io.Serializable;

public class PatientDataRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dhanid;
	private String phone;
	private String amount;
	private String fcurrency;
	private String group;
	private String type;
	private String pmode;
	private String userId;
	private String sno;
	private String feedtype;
	
	public String getDhanid() {
		return dhanid;
	}
	public void setDhanid(String dhanid) {
		this.dhanid = dhanid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getFcurrency() {
		return fcurrency;
	}
	public void setFcurrency(String fcurrency) {
		this.fcurrency = fcurrency;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPmode() {
		return pmode;
	}
	public void setPmode(String pmode) {
		this.pmode = pmode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getFeedtype() {
		return feedtype;
	}
	public void setFeedtype(String feedtype) {
		this.feedtype = feedtype;
	}
	@Override
	public String toString() {
		return "PatientDataRequest [dhanid=" + dhanid + ", phone=" + phone + ", amount=" + amount + ", fcurrency="
				+ fcurrency + ", group=" + group + ", type=" + type + ", pmode=" + pmode + ", userId=" + userId
				+ ", sno=" + sno + ", feedtype=" + feedtype + "]";
	}
	
}
