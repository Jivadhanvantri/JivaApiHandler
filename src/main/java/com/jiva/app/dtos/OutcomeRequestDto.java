package com.jiva.app.dtos;

import java.io.Serializable;

public class OutcomeRequestDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String caseId;
	private String outcome;
	private String userId;
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getOutcome() {
		return outcome;
	}
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "OutcomeRequestDto [caseId=" + caseId + ", outcome=" + outcome + ", userId=" + userId + "]";
	}
}
