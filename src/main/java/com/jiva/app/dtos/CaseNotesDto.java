package com.jiva.app.dtos;

import java.io.Serializable;

public class CaseNotesDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String caseId;
	private String notes;
	private String sUserId;
	
	public CaseNotesDto() {}
	

	public CaseNotesDto(String caseId, String notes, String sUserId) {
		super();
		this.caseId = caseId;
		this.notes = notes;
		this.sUserId = sUserId;
	}
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getsUserId() {
		return sUserId;
	}
	public void setsUserId(String sUserId) {
		this.sUserId = sUserId;
	}
	@Override
	public String toString() {
		return "CaseNotesDto [caseId=" + caseId + ", notes=" + notes + ", sUserId=" + sUserId + "]";
	}
	
	
	
}
