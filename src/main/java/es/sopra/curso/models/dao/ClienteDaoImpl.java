package es.sopra.curso.models.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.sopra.cuso.models.entity.Cliente;

@Repository
//@Repository("clienteDaoJPA")
public class ClienteDaoImpl implements IClienteDao{

	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	@Override
	public List<Cliente> findAll() {
		
		// TODO Auto-generated method stub
		return em.createQuery("from Cliente").getResultList();
	}
	
	

}
