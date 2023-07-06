package com.jiva.app.mail.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;

public class Mail {
	private String mailFrom;
    private String mailTo;
    private String mailCc;
    private String mailBcc;
    private String mailSubject;
    private String mailContent;
    private String contentType;
    private List<Object> attachments;
   	private String templateName;    
    private Map<String, Object> placeHolderValues = new HashMap<String, Object>();
    private DataSource ds;
    private String fileName;
    
    public Mail() {
    	 contentType = "text/html";
    }

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailCc() {
		return mailCc;
	}

	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}

	public String getMailBcc() {
		return mailBcc;
	}

	public void setMailBcc(String mailBcc) {
		this.mailBcc = mailBcc;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public List<Object> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Object> attachments) {
		this.attachments = attachments;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Map<String, Object> getPlaceHolderValues() {
		return placeHolderValues;
	}

	public void setPlaceHolderValues(Map<String, Object> placeHolderValues) {
		this.placeHolderValues = placeHolderValues;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "Mail [mailFrom=" + mailFrom + ", mailTo=" + mailTo + ", mailCc=" + mailCc + ", mailBcc=" + mailBcc
				+ ", mailSubject=" + mailSubject + ", mailContent=" + mailContent + ", contentType=" + contentType
				+ ", attachments=" + attachments + ", templateName=" + templateName + ", placeHolderValues="
				+ placeHolderValues + ", ds=" + ds + ", fileName=" + fileName + "]";
	}
    
}
