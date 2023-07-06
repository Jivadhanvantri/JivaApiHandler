package com.jiva.app.dtos;

import java.io.Serializable;

public class FinalOutcomeDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String finalOutcomeDt;
	private String finalOutcome;
	private String user;
	private String finalSubOutcome;
	public String getFinalOutcomeDt() {
		return finalOutcomeDt;
	}
	public void setFinalOutcomeDt(String finalOutcomeDt) {
		this.finalOutcomeDt = finalOutcomeDt;
	}
	public String getFinalOutcome() {
		return finalOutcome;
	}
	public void setFinalOutcome(String finalOutcome) {
		this.finalOutcome = finalOutcome;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getFinalSubOutcome() {
		return finalSubOutcome;
	}
	public void setFinalSubOutcome(String finalSubOutcome) {
		this.finalSubOutcome = finalSubOutcome;
	}
	@Override
	public String toString() {
		return "FinalOutcomeDto [finalOutcomeDt=" + finalOutcomeDt + ", finalOutcome=" + finalOutcome + ", user=" + user
				+ ", finalSubOutcome=" + finalSubOutcome + "]";
	}
	
	
}
