package com.jiva.app.dtos;

import java.io.Serializable;

public class DispositionResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dhanId;
	private String contactId;
	private String disposition;
	private String userId;
	private String sessionRouteDt;
	private String sessionId;
	
	public String getDhanId() {
		return dhanId;
	}
	public void setDhanId(String dhanId) {
		this.dhanId = dhanId;
	}
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getDisposition() {
		return disposition;
	}
	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSessionRouteDt() {
		return sessionRouteDt;
	}
	public void setSessionRouteDt(String sessionRouteDt) {
		this.sessionRouteDt = sessionRouteDt;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	@Override
	public String toString() {
		return "DispositionResponseDto [dhanId=" + dhanId + ", contactId=" + contactId + ", disposition=" + disposition
				+ ", userId=" + userId + ", sessionRouteDt=" + sessionRouteDt + ", sessionId=" + sessionId + "]";
	}
	
}
