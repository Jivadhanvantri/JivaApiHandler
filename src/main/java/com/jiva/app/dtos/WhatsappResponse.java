package com.jiva.app.dtos;

import java.io.Serializable;

public class WhatsappResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String responseStatus;
	private String whatsappId;
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getWhatsappId() {
		return whatsappId;
	}
	public void setWhatsappId(String whatsappId) {
		this.whatsappId = whatsappId;
	}
	@Override
	public String toString() {
		return "WhatsappResponse [responseStatus=" + responseStatus + ", whatsappId=" + whatsappId + "]";
	}
	
	
}
