package com.jeanwolff.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.jeanwolff.cursomc.domain.Cliente;
import com.jeanwolff.cursomc.dto.ClienteDTO;
import com.jeanwolff.cursomc.repositories.ClienteRepository;
import com.jeanwolff.cursomc.services.exceptions.DataIntegrityException;
import com.jeanwolff.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {


	@Autowired
	private ClienteRepository repo;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: "+ id +", Tipo: "+ Cliente.class.getSimpleName()));
	}

	public Cliente insert(Cliente cliente) {
		cliente.setId(null);
		return repo.save(cliente);
	}

	public Cliente update(Cliente cliente) {
		Cliente newCliente = find(cliente.getId());
		updateDate(newCliente, cliente);
		return repo.save(newCliente);
	}

	public void deleteById(Integer id) {
		Cliente cliente = find(id);
		try {
			repo.deleteById(cliente.getId());
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não foi possível excluir "+cliente.getNome()+", pois possui pedidos" );
		}
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String direction, String orderBy) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
	}
	
	private void updateDate(Cliente newCliente, Cliente cliente) {
		newCliente.setNome(cliente.getNome());
		newCliente.setEmail(cliente.getEmail());
	}
	
}
