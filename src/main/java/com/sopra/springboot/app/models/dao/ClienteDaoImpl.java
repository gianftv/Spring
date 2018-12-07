package com.sopra.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sopra.springboot.app.models.entity.Cliente;

@Repository
public class ClienteDaoImpl implements IClienteDao {

	@PersistenceContext
	private EntityManager em;

	/** metodos de consulta */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Cliente> findAll() {

		return em.createQuery("from Cliente").getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {// lectura en base de los tansactrional
		return em.find(Cliente.class, id);
	}

	/** metodos de actualizacion */
	@Override
	@Transactional // es abierto completo
	public void save(Cliente cliente) {// vamos a hacer un insercción
		if (cliente.getId() != null && cliente.getId() > 0) {
			em.merge(cliente);
		} else {
			em.persist(cliente);
		}
	}

	@Override
	@Transactional
	public void delete(Long id) {
		em.remove(findOne(id));

	}

}
