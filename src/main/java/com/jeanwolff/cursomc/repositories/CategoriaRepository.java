package com.jeanwolff.cursomc.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jeanwolff.cursomc.domain.Categoria;


@Repository
@Transactional(readOnly = true)
public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{

	
	
	
}
