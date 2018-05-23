package com.jeanwolff.cursomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jeanwolff.cursomc.domain.Categoria;
import com.jeanwolff.cursomc.domain.Cidade;
import com.jeanwolff.cursomc.domain.Estado;
import com.jeanwolff.cursomc.domain.Produto;
import com.jeanwolff.cursomc.repositories.CategoriaRepository;
import com.jeanwolff.cursomc.repositories.CidadeRepository;
import com.jeanwolff.cursomc.repositories.EstadoRepository;
import com.jeanwolff.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepositor;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EstadoRepository estadoRepository;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Categoria cat1 = new Categoria("Informática");

		Categoria cat2 = new Categoria("Escritório");

		Produto p1 = new Produto("Computador", 2000.00);

		Produto p2 = new Produto("Impressora", 800.00);

		Produto p3 = new Produto("Mouse", 80.00);

		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));

		cat2.getProdutos().addAll(Arrays.asList(p2));

		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		Estado est1 = new Estado(null, "Minas Gerais"); 
		Estado est2 = new Estado(null, "São Paulo"); 

		Cidade c1 = new Cidade(null, "Uberlândia", est1); 
		Cidade c2 = new Cidade(null, "São Paulo", est2); 
		Cidade c3 = new Cidade(null, "Campinas", est2); 
		
		est1.getCidades().addAll(Arrays.asList(c1)); 
		est2.getCidades().addAll(Arrays.asList(c2, c3)); 
		
		estadoRepository.saveAll(Arrays.asList(est1, est2)); 
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		
		categoriaRepositor.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
	}
}
