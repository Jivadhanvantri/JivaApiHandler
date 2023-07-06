package com.jiva.app.dtos;

import java.io.Serializable;

public class WacDocDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dhanId;
	private String docId;
	private String vcLink;
	private String sendFor;
	private String vcStatus;
	
	public String getDhanId() {
		return dhanId;
	}
	public void setDhanId(String dhanId) {
		this.dhanId = dhanId;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getVcLink() {
		return vcLink;
	}
	public void setVcLink(String vcLink) {
		this.vcLink = vcLink;
	}
	public String getSendFor() {
		return sendFor;
	}
	public void setSendFor(String sendFor) {
		this.sendFor = sendFor;
	}
	public String getVcStatus() {
		return vcStatus;
	}
	public void setVcStatus(String vcStatus) {
		this.vcStatus = vcStatus;
	}
	@Override
	public String toString() {
		return "WacDocDto [dhanId=" + dhanId + ", docId=" + docId + ", vcLink=" + vcLink + ", sendFor=" + sendFor
				+ ", vcStatus=" + vcStatus + "]";
	}
	
	
}
