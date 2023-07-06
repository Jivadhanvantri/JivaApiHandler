package com.jiva.app.dtos;

import java.io.Serializable;

public class WacVCResponseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String consultation_link;
	private String dhanId;
	public String getConsultation_link() {
		return consultation_link;
	}
	public void setConsultation_link(String consultation_link) {
		this.consultation_link = consultation_link;
	}
	public String getDhanId() {
		return dhanId;
	}
	public void setDhanId(String dhanId) {
		this.dhanId = dhanId;
	}
	@Override
	public String toString() {
		return "WacVCResponseDto [consultation_link=" + consultation_link + ", dhanId=" + dhanId + "]";
	}
	
	
}
