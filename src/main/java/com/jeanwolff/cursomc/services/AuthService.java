package com.jeanwolff.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jeanwolff.cursomc.domain.Cliente;
import com.jeanwolff.cursomc.repositories.ClienteRepository;
import com.jeanwolff.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository repository;


	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private EmailService emailService;
	
	private Random ran = new Random();

	public void sendNewPassword(String email) {
		Cliente cliente = repository.findByEmail(email);

		if (cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado!");
		}

		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));

		repository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);

	}

	private String newPassword() {
		char[] vet = new char[10];
		for (int i = 0; i < 10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = ran.nextInt(3);
		if(opt ==0) {
			return (char) (ran.nextInt(10)+ 48);
		} else if(opt == 1) {
			return (char) (ran.nextInt(26)+ 65);
		} else {
			return (char) (ran.nextInt(26)+ 97);
		}
	}

}
