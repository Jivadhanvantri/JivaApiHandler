package com.jiva.app.dtos;

import java.io.Serializable;

public class ResponseHandler implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private boolean statusCode;
	private int status;
	
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isStatusCode() {
		return statusCode;
	}
	public void setStatusCode(boolean statusCode) {
		this.statusCode = statusCode;
	}
	@Override
	public String toString() {
		return "ResponseHandler [message=" + message + ", statusCode=" + statusCode + "]";
	}
	
	
	
}
