package com.sopra.springboot.app.controllers;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sopra.springboot.app.model.service.IClienteService;
import com.sopra.springboot.app.models.dao.IClienteDao;
import com.sopra.springboot.app.models.entity.Cliente;
import com.sopra.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")//atributo de sesion
public class ClienteController {

	@Autowired
	private IClienteService clienteService;
	
	@GetMapping(value="/ver/{id}")//casi lo mismo que request
	public 	String ver(@PathVariable (value="id") Long id, Map<String, Object> model, RedirectAttributes flash){
		
		Cliente cliente = clienteService.findOne(id);
		if(cliente==null) {
			flash.addFlashAttribute("error", "El cliente no existe en la BBDD");
			return "redirect:/listar";
		}
		
		model.put("cliente",cliente);
		model.put("titulo", "Detalle cliente :" + cliente.getNombre());
		
		return "ver";
		
		
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET) // en barra/ para que la aplicacion arranque en el listado
	public String listar(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		
		Pageable pageRequest = new  PageRequest(page,5);//page, donde ha puesto el 5 antes habia puesto "size"
		//Pageable pageRequest = PageRequest.of(page) spring 5
		
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		model.addAttribute("titulo", "Listado de clientes");// atributos a la lista
		model.addAttribute("clientes", clientes);
		model.addAttribute("page",pageRender);
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
	public String guardar(@Valid Cliente cliente, BindingResult result,  Model model, @RequestParam("file")MultipartFile foto,RedirectAttributes flash,SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario del Cliente");// para que no vaya el titulo al forzar los errores
			return "form";
		}
		
		if(!foto.isEmpty()) {
			Path directorioRecursos = Paths.get("src/main/resources/static/uploads");
			String rootPath = directorioRecursos.toFile().getAbsolutePath();
			try {
				byte[] bytes= foto.getBytes();
				Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
				Files.write(rutaCompleta, bytes);
				flash.addFlashAttribute("info", "Ha subido correctamente '" + foto.getOriginalFilename()+ "'");
				
				cliente.setFoto(foto.getOriginalFilename());
			}catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
		String mensajeFlash = (cliente.getId() !=null)? "Cliente editado con éxito" : "Cliente creado con éxicto";

		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success",mensajeFlash);
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
