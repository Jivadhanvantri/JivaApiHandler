package com.jiva.app.dtos;

import java.io.Serializable;

public class JivaBotPaymentDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String caseId;
	private String phone;
	private String statuss;
	private String paycode;
	private String amount;
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getStatuss() {
		return statuss;
	}
	public void setStatuss(String statuss) {
		this.statuss = statuss;
	}
	public String getPaycode() {
		return paycode;
	}
	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "JivaBotPaymentDto [caseId=" + caseId + ", phone=" + phone + ", statuss=" + statuss + ", paycode="
				+ paycode + ", amount=" + amount + "]";
	}
	
}
