package com.jiva.app.dtos;

import java.io.Serializable;


public class NiceDetailsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dialDate;
	private String answerDate;
	private String disconnectDate;
	private String diff;
	private String incomingDid;
	private String sessionId;
	public String getDialDate() {
		return dialDate;
	}
	public void setDialDate(String dialDate) {
		this.dialDate = dialDate;
	}
	public String getAnswerDate() {
		return answerDate;
	}
	public void setAnswerDate(String answerDate) {
		this.answerDate = answerDate;
	}
	public String getDisconnectDate() {
		return disconnectDate;
	}
	public void setDisconnectDate(String disconnectDate) {
		this.disconnectDate = disconnectDate;
	}
	public String getDiff() {
		return diff;
	}
	public void setDiff(String diff) {
		this.diff = diff;
	}
	public String getIncomingDid() {
		return incomingDid;
	}
	public void setIncomingDid(String incomingDid) {
		this.incomingDid = incomingDid;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	@Override
	public String toString() {
		return "NiceDetailsDto [dialDate=" + dialDate + ", answerDate=" + answerDate + ", disconnectDate="
				+ disconnectDate + ", diff=" + diff + ", incomingDid=" + incomingDid + ", sessionId=" + sessionId + "]";
	}
	
}
