package com.jeanwolff.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailService extends AbstractEmailService {

	private static final Logger log = LoggerFactory.getLogger(SmtpEmailService.class);
	
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		log.info("Enviando Email ...");
		log.info(msg.toString());
		mailSender.send(msg);
		log.info("Email Enviado!!!");
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		log.info("Enviando Email HTML...");
		log.info(msg.toString());
		javaMailSender.send(msg);
		log.info("Email Enviado!!!");
		
	}
	
	

}
