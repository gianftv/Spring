package com.sopra.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sopra.springboot.app.models.dao.IClienteDao;
import com.sopra.springboot.app.models.entity.Cliente;

@Controller
public class ClienteController {

	@Autowired
	private IClienteDao clienteDao;

	@RequestMapping(value = "/", method = RequestMethod.GET) // en barra/ para que la aplicacion arranque en el listado
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de clientes");// atributos a la lista
		model.addAttribute("clientes", clienteDao.findAll());
		return "listar";
	}

	@RequestMapping(value = "/form")//entra por /form 
	public String crear(Map<String, Object> model) {// map es un array que le meto unn string y me devuelve un objeto

		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario del Cliente");
		return "form";

	}
	
	
	@RequestMapping(value = "/form", method=RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("titulo","Formulario del Cliente");//para que no vaya el titulo al forzar los errores
			return "form";
		}
		
		clienteDao.save(cliente);
		return "redirect:";
	}
}
