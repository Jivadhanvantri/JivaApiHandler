package com.jiva.app.service;

import com.jiva.app.mail.domain.Mail;

public interface MailService {
	 public String sendEmail(Mail mail);
	 public String sendEmailWithAttachment(Mail mail);
}
