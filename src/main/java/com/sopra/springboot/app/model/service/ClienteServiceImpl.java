package com.sopra.springboot.app.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sopra.springboot.app.models.dao.IClienteDao;
import com.sopra.springboot.app.models.entity.Cliente;


@Service
public class ClienteServiceImpl implements  IClienteService{

	
	@Autowired
	private IClienteDao clienteDao;
	
	
	@Override
	public List<Cliente> findAll() {
		
		return clienteDao.findAll();
	}

	@Override
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
		
	}

	@Override
	public Cliente findOne(Long id) {
		
		return clienteDao.findOne(id);
	}

	@Override
	public void delete(Long id) {
		clienteDao.delete(id);
		
	}

}
