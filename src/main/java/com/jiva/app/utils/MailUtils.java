package com.jiva.app.utils;

import java.util.HashMap;
import java.util.Map;

import com.jiva.app.comman.utils.JivaConstants;
import com.jiva.app.mail.domain.Mail;


public class MailUtils {
	
	
	public MailUtils() {
		super();
	}

	public static Mail alertMail(Map<String,Object> mailParam) throws Exception {
		Mail mail = new Mail();
		mail.setContentType("text/html");
		mail.setMailTo("prabir.roy@jiva.com");
		mail.setMailCc("sujeet@jiva.com,pawan.kumar@jiva.com");
		mail.setMailFrom("alert@jiva.com");
		mail.setTemplateName(JivaConstants.NORMAL_TEMPLATE);
		mail.setMailSubject("Meeting scheduled within appointment time.");
		Map<String, Object> placeHolders = new HashMap<String, Object>();
		placeHolders.putAll(mailParam);
		placeHolders.put("fullNameText", "Team");		
		placeHolders.put("FooterText", "Team Jiva");
		mail.setPlaceHolderValues(placeHolders);
		return mail;
  }

}
