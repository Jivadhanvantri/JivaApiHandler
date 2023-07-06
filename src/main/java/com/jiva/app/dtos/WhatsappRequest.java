package com.jiva.app.dtos;

import java.io.Serializable;

public class WhatsappRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderCode;
	private String dhanId;
	private String whatsappId;
	private String mobileNo;
	private String status;
	private String sentFor;
	private String smsText;
	private String grpId;
	
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getDhanId() {
		return dhanId;
	}
	public void setDhanId(String dhanId) {
		this.dhanId = dhanId;
	}
	public String getWhatsappId() {
		return whatsappId;
	}
	public void setWhatsappId(String whatsappId) {
		this.whatsappId = whatsappId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSentFor() {
		return sentFor;
	}
	public void setSentFor(String sentFor) {
		this.sentFor = sentFor;
	}
	public String getSmsText() {
		return smsText;
	}
	public void setSmsText(String smsText) {
		this.smsText = smsText;
	}
	public String getGrpId() {
		return grpId;
	}
	public void setGrpId(String grpId) {
		this.grpId = grpId;
	}
	@Override
	public String toString() {
		return "WhatsappRequest [orderCode=" + orderCode + ", dhanId=" + dhanId + ", whatsappId=" + whatsappId
				+ ", mobileNo=" + mobileNo + ", status=" + status + ", sentFor=" + sentFor + ", smsText=" + smsText
				+ ", grpId=" + grpId + "]";
	}
	
	

}
