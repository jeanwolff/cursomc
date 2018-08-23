package com.jeanwolff.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jeanwolff.cursomc.domain.Cidade;
import com.jeanwolff.cursomc.domain.Cliente;
import com.jeanwolff.cursomc.domain.Endereco;
import com.jeanwolff.cursomc.domain.enums.Perfil;
import com.jeanwolff.cursomc.domain.enums.TipoCliente;
import com.jeanwolff.cursomc.dto.ClienteDTO;
import com.jeanwolff.cursomc.dto.ClienteNewDTO;
import com.jeanwolff.cursomc.repositories.ClienteRepository;
import com.jeanwolff.cursomc.repositories.EnderecoRepository;
import com.jeanwolff.cursomc.security.UserSS;
import com.jeanwolff.cursomc.services.exceptions.AuthorizationException;
import com.jeanwolff.cursomc.services.exceptions.DataIntegrityException;
import com.jeanwolff.cursomc.services.exceptions.ObjectNotFoundException;
import com.jeanwolff.cursomc.util.constants.ConstantsMessages;
import com.jeanwolff.cursomc.util.constants.UtilConstants;

@Service
public class ClienteService {

	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;

	@Value("${img.prefix.client.profile}")
	private String prefix;

	@Value("${img.profile.size}")
	private Integer size;

	public Cliente find(Integer id) {
		UserSS user = UserService.authenticated();

		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException(ConstantsMessages.ERRO_ACESSO_NEGADO);
		}

		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(ConstantsMessages.ERRO_OBJETO_NAO_ENCONTRADO+ " id:" + id + ", Tipo: " + Cliente.class.getSimpleName()));
	}

	public Cliente insert(Cliente cliente) {
		cliente.setId(null);
		cliente = repo.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
	}

	public Cliente update(Cliente cliente) {
		Cliente newCliente = find(cliente.getId());
		updateData(newCliente, cliente);
		return repo.save(newCliente);
	}

	public void deleteById(Integer id) {
		Cliente cliente = find(id);
		try {
			repo.deleteById(cliente.getId());
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não foi possível excluir " + cliente.getNome() + ", pois possui pedidos");
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
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDTO clienteNewDTO) {
		Cliente cliente = new Cliente(clienteNewDTO.getNome(), clienteNewDTO.getEmail(), clienteNewDTO.getCpfOuCnpj(), TipoCliente.toEnum(clienteNewDTO.getTipo()), pe.encode(clienteNewDTO.getSenha()));	
		Cidade cidade = new Cidade(clienteNewDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(clienteNewDTO.getLogradouro(), clienteNewDTO.getNumero(), clienteNewDTO.getComplemento(), clienteNewDTO.getBairro(), clienteNewDTO.getCep(), cliente, cidade);
		cliente.getEnderecos().add(end);
		cliente.getTelefones().add(clienteNewDTO.getTelefone1());

		if (clienteNewDTO.getTelefone2() != null) {
			cliente.getTelefones().add(clienteNewDTO.getTelefone2());
		}
		if (clienteNewDTO.getTelefone3() != null) {
			cliente.getTelefones().add(clienteNewDTO.getTelefone3());
		}

		return cliente;
	}

	private void updateData(Cliente newCliente, Cliente cliente) {
		newCliente.setNome(cliente.getNome());
		newCliente.setEmail(cliente.getEmail());
	}

	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();
		
		if(user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())){
			throw new AuthorizationException(ConstantsMessages.ERRO_ACESSO_NEGADO);
		}

		Cliente cliente = repo.findByEmail(email);
		
		if(cliente == null) {
			throw new ObjectNotFoundException(ConstantsMessages.ERRO_OBJETO_NAO_ENCONTRADO + " Id:" + user.getId() + ", Tipo: "+ Cliente.class.getName());
		}
		return cliente;
	}

	public URI uploadProfilePicture(MultipartFile multiPartFile) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException(ConstantsMessages.ERRO_ACESSO_NEGADO);
		}
		BufferedImage jpgImage = imageService.getImageFromFile(multiPartFile);
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		String fileName = prefix + user.getId() + UtilConstants.IMG_EXT_JPG;
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, UtilConstants.IMG_JPG), fileName,
				UtilConstants.IMG);
	}

}
