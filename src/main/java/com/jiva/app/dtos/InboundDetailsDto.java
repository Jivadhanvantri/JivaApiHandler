package com.jiva.app.dtos;

import java.io.Serializable;


public class InboundDetailsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sessionId;
	private String agentName;
	private String workType;
	private String createDt;
	private String agentId;
	private String contactId;
	private String outcome;
	private String dialDate;
	private String answerDate;
	private String disconnectDate;
	private String diff;
	private String dhanId;
	private String subDispostion;
	private String incomingDid;
	private String source;
	private String jtcId;
	private String caseCreateDt;
	private String patientName;
	private String campaignName;
	private String leadCreateDt;
	private String disease;
	private String pincode;
	private String dob;
	private String otherDisease;
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	public String getCreateDt() {
		return createDt;
	}
	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getOutcome() {
		return outcome;
	}
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
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
	public String getDhanId() {
		return dhanId;
	}
	public void setDhanId(String dhanId) {
		this.dhanId = dhanId;
	}
	public String getSubDispostion() {
		return subDispostion;
	}
	public void setSubDispostion(String subDispostion) {
		this.subDispostion = subDispostion;
	}
	public String getIncomingDid() {
		return incomingDid;
	}
	public void setIncomingDid(String incomingDid) {
		this.incomingDid = incomingDid;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getJtcId() {
		return jtcId;
	}
	public void setJtcId(String jtcId) {
		this.jtcId = jtcId;
	}
	public String getCaseCreateDt() {
		return caseCreateDt;
	}
	public void setCaseCreateDt(String caseCreateDt) {
		this.caseCreateDt = caseCreateDt;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getCampaignName() {
		return campaignName;
	}
	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}
	public String getLeadCreateDt() {
		return leadCreateDt;
	}
	public void setLeadCreateDt(String leadCreateDt) {
		this.leadCreateDt = leadCreateDt;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getOtherDisease() {
		return otherDisease;
	}
	public void setOtherDisease(String otherDisease) {
		this.otherDisease = otherDisease;
	}
	@Override
	public String toString() {
		return "InboundDetailsDto [sessionId=" + sessionId + ", agentName=" + agentName + ", workType=" + workType
				+ ", createDt=" + createDt + ", agentId=" + agentId + ", contactId=" + contactId + ", outcome="
				+ outcome + ", dialDate=" + dialDate + ", answerDate=" + answerDate + ", disconnectDate="
				+ disconnectDate + ", diff=" + diff + ", dhanId=" + dhanId + ", subDispostion=" + subDispostion
				+ ", incomingDid=" + incomingDid + ", source=" + source + ", jtcId=" + jtcId + ", caseCreateDt="
				+ caseCreateDt + ", patientName=" + patientName + ", campaignName=" + campaignName + ", leadCreateDt="
				+ leadCreateDt + ", disease=" + disease + ", pincode=" + pincode + ", dob=" + dob + ", otherDisease="
				+ otherDisease + "]";
	}
	
}
