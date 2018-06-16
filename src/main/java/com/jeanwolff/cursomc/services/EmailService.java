package com.jeanwolff.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.jeanwolff.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);
	
}
