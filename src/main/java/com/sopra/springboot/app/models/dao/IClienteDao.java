package com.sopra.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sopra.springboot.app.models.entity.Cliente;

public interface IClienteDao  extends CrudRepository<Cliente, Long>{//crudrepository implementa todos los metodos de una interfaz(finAll,findOne,....)
	
	
	
}
