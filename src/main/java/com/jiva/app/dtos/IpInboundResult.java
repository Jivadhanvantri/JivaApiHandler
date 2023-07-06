package com.jiva.app.dtos;

import java.io.Serializable;
import java.util.List;

public class IpInboundResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status;
	private List<IpInboundSubResult> data;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<IpInboundSubResult> getData() {
		return data;
	}
	public void setData(List<IpInboundSubResult> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "IpInboundResult [status=" + status + ", data=" + data + "]";
	}
	
	
}
