package com.jiva.app.dtos;

import java.io.Serializable;

public class SMSDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dhanId;
	private String phoneNo;
	private String msgText;
	private String createId;
	private String sessionId;
	private String sendStatus;
	private String transactionId;
	public String getDhanId() {
		return dhanId;
	}
	public void setDhanId(String dhanId) {
		this.dhanId = dhanId;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getMsgText() {
		return msgText;
	}
	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	@Override
	public String toString() {
		return "SMSDetails [dhanId=" + dhanId + ", phoneNo=" + phoneNo + ", msgText=" + msgText + ", createId="
				+ createId + ", sessionId=" + sessionId + ", sendStatus=" + sendStatus + ", transactionId="
				+ transactionId + "]";
	}	
}
