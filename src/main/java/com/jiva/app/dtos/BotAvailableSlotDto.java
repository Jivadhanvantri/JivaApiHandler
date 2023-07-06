package com.jiva.app.dtos;

import java.io.Serializable;

public class BotAvailableSlotDto implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String groupName;
	private String slotDate;
	private String docId;
	
	
	
	
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getSlotDate() {
		return slotDate;
	}
	public void setSlotDate(String slotDate) {
		this.slotDate = slotDate;
	}
	
	
	
}
