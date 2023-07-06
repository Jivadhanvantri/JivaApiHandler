package com.jiva.app.serviceImp;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.jiva.app.mail.domain.Mail;
import com.jiva.app.service.MailService;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class MailServiceImpl  implements MailService{
	
	@Autowired
    JavaMailSender mailSender;
	
	@Autowired
	Configuration configuration;

	@Override
	public String sendEmail(Mail mail) {
		 String mailMessage = null;
		 MimeMessage mimeMessage = mailSender.createMimeMessage();
		 try {
	            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
	            mimeMessageHelper.setSubject(mail.getMailSubject());
	            mimeMessageHelper.setFrom(mail.getMailFrom());
	            mimeMessageHelper.setTo(InternetAddress.parse(mail.getMailTo()));
	            if(mail.getMailCc() !=null && mail.getMailCc().length()>0) {
	            	mimeMessageHelper.setCc(InternetAddress.parse(mail.getMailCc()));
	            }
	            if(mail.getMailBcc() !=null && mail.getMailBcc().length() >0) {
	            	 mimeMessageHelper.setBcc(InternetAddress.parse(mail.getMailBcc()));
	            }
	            Template t = configuration.getTemplate(mail.getTemplateName());
	            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getPlaceHolderValues());
	            mimeMessageHelper.setText(html,true);
	            mailSender.send(mimeMessageHelper.getMimeMessage());
	            mailMessage="Mail sent successfully";
	        } catch (MessagingException e) {
	            mailMessage=e.getMessage();
	            e.printStackTrace();
	        } catch (Exception e) {
	        	mailMessage=e.getMessage();
	            e.printStackTrace();
	        }
	       return mailMessage;
	}

	@Override
	public String sendEmailWithAttachment(Mail mail) {
		// TODO Auto-generated method stub
		return null;
	}

}
