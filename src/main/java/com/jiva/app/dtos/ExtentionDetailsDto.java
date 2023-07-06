package com.jiva.app.dtos;

import java.io.Serializable;

public class ExtentionDetailsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String FB_CITY_NAME;
	private String COMPAIGN_NAME;
	private String UTM_CAMPAIGNID;
	private String CONTACT_ID;
	public String getFB_CITY_NAME() {
		return FB_CITY_NAME;
	}
	public void setFB_CITY_NAME(String fB_CITY_NAME) {
		FB_CITY_NAME = fB_CITY_NAME;
	}
	public String getCOMPAIGN_NAME() {
		return COMPAIGN_NAME;
	}
	public void setCOMPAIGN_NAME(String cOMPAIGN_NAME) {
		COMPAIGN_NAME = cOMPAIGN_NAME;
	}
	public String getUTM_CAMPAIGNID() {
		return UTM_CAMPAIGNID;
	}
	public void setUTM_CAMPAIGNID(String uTM_CAMPAIGNID) {
		UTM_CAMPAIGNID = uTM_CAMPAIGNID;
	}
	public String getCONTACT_ID() {
		return CONTACT_ID;
	}
	public void setCONTACT_ID(String cONTACT_ID) {
		CONTACT_ID = cONTACT_ID;
	}
	@Override
	public String toString() {
		return "ExtentionDetailsDto [FB_CITY_NAME=" + FB_CITY_NAME + ", COMPAIGN_NAME=" + COMPAIGN_NAME
				+ ", UTM_CAMPAIGNID=" + UTM_CAMPAIGNID + ", CONTACT_ID=" + CONTACT_ID + "]";
	}
	
	
}
