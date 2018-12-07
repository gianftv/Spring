package com.sopra.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sopra.springboot.app.model.service.IClienteService;
import com.sopra.springboot.app.models.dao.IClienteDao;
import com.sopra.springboot.app.models.entity.Cliente;

@Controller
@SessionAttributes("cliente")//atributo de sesion
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@RequestMapping(value = "/listar", method = RequestMethod.GET) // en barra/ para que la aplicacion arranque en el listado
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de clientes");// atributos a la lista
		model.addAttribute("clientes", clienteService.findAll());
		return "listar";
	}

	@RequestMapping(value = "/form") // entra por /form
	public String crear(Map<String, Object> model) {// map es un array que le meto unn string y me devuelve un objeto

		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario del Cliente");
		return "form";

	}

	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, RedirectAttributes flash,  Map<String, Object> model) {

		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if(cliente==null) {
				flash.addFlashAttribute("error","El ID deñ Cliente no esta en la BBDD");
				return "redirect/listar";
			}
		} else {
			flash.addFlashAttribute("error","el ID del Cliente no pueden ser 0!");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo","Editar Cliente");
		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash,SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario del Cliente");// para que no vaya el titulo al forzar los errores
			return "form";
		}

		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success","Cliente creado con éxito");
		return "redirect:/listar";
	}

	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		if(id>0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success","Cliente eliminado con éxito");
		}
		return "redirect:/listar";
		
	}
}
