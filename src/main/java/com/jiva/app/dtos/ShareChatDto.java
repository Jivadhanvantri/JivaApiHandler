package com.jiva.app.dtos;

import java.io.Serializable;

public class ShareChatDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String SUBMIT_DT;
	private String WEB_CARD;
	private String NAME;
	private String MOBILE;
	private String CITY;
	private String CASE_ID;
	private String SRC;
	private String CONTACT_ID;
	
	public String getSUBMIT_DT() {
		return SUBMIT_DT;
	}
	public void setSUBMIT_DT(String sUBMIT_DT) {
		SUBMIT_DT = sUBMIT_DT;
	}
	public String getWEB_CARD() {
		return WEB_CARD;
	}
	public void setWEB_CARD(String wEB_CARD) {
		WEB_CARD = wEB_CARD;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getMOBILE() {
		return MOBILE;
	}
	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}
	public String getCITY() {
		return CITY;
	}
	public void setCITY(String cITY) {
		CITY = cITY;
	}
	public String getCASE_ID() {
		return CASE_ID;
	}
	public void setCASE_ID(String cASE_ID) {
		CASE_ID = cASE_ID;
	}
	public String getSRC() {
		return SRC;
	}
	public void setSRC(String sRC) {
		SRC = sRC;
	}
	public String getCONTACT_ID() {
		return CONTACT_ID;
	}
	public void setCONTACT_ID(String cONTACT_ID) {
		CONTACT_ID = cONTACT_ID;
	}
	@Override
	public String toString() {
		return "ShareChatDto [SUBMIT_DT=" + SUBMIT_DT + ", WEB_CARD=" + WEB_CARD + ", NAME=" + NAME + ", MOBILE="
				+ MOBILE + ", CITY=" + CITY + ", CASE_ID=" + CASE_ID + ", SRC=" + SRC + ", CONTACT_ID=" + CONTACT_ID
				+ "]";
	}
	
	
	
}
