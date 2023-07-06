package com.jiva.app.dtos;

import java.io.Serializable;

public class ResponseMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private int statusCode;
	private Object obj;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	@Override
	public String toString() {
		return "ResponseMessage [message=" + message + ", statusCode=" + statusCode + ", obj=" + obj + "]";
	}
	

}
