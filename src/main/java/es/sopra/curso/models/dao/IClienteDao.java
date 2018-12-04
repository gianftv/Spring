package es.sopra.curso.models.dao;

import java.util.List;

import es.sopra.cuso.models.entity.Cliente;

public interface IClienteDao {

	public List<Cliente> findAll();
	
	
}
