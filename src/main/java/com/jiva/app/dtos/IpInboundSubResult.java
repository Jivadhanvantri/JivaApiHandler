package com.jiva.app.dtos;

import java.io.Serializable;

public class IpInboundSubResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mobile;
	private String ProductType;
	private String callDetails;
	private String status;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProductType() {
		return ProductType;
	}
	public void setProductType(String productType) {
		ProductType = productType;
	}
	public String getCallDetails() {
		return callDetails;
	}
	public void setCallDetails(String callDetails) {
		this.callDetails = callDetails;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "IpInboundSubResult [mobile=" + mobile + ", ProductType=" + ProductType + ", callDetails=" + callDetails
				+ ", status=" + status + "]";
	}
	
	
}
