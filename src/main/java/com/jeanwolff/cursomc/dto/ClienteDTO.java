package com.jeanwolff.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.jeanwolff.cursomc.domain.Cliente;
import com.jeanwolff.cursomc.services.validation.ClienteUpdate;

@ClienteUpdate
public class ClienteDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	public ClienteDTO() {
		
	}
	
	public ClienteDTO(Cliente cliente) {
		id = cliente.getId();
		nome = cliente.getNome();
		email = cliente.getEmail();
	}
	
	
	private Integer id;
	
	@NotEmpty(message="Preenchimento Obrigatório")
	@Length(min=5, max=120, message="O tamanho deve ser entre 5 e 120 caracteres")
	private String nome;
	
	@NotEmpty(message="Preenchimento Obrigatório")
	@Email(message="Email inválido")
	private String email;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
