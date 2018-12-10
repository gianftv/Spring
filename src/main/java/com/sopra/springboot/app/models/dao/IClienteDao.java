package com.sopra.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sopra.springboot.app.models.entity.Cliente;

public interface IClienteDao  extends PagingAndSortingRepository<Cliente, Long>{//crudrepository implementa todos los metodos de una interfaz(finAll,findOne,....)
	
	//PagingAndSortingRepository herencia inferior para ser mas especifico by raul
	
}
