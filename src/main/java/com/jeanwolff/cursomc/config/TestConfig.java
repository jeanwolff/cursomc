package com.jeanwolff.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.jeanwolff.cursomc.services.DBService;
import com.jeanwolff.cursomc.services.EmailService;
import com.jeanwolff.cursomc.services.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private DBService dBService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		dBService.instantiateTestDatabase();
		return true;	
	}
	
	@Bean 
	public EmailService getEmailService() {
		//return new SmtpEmailService();
		return new MockEmailService();
	}
	
}
