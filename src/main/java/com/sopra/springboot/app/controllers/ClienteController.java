package com.sopra.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
import com.sopra.springboot.app.models.entity.Cliente;
import com.sopra.springboot.app.models.service.IClienteService;
import com.sopra.springboot.app.models.service.IUploadService;
import com.sopra.springboot.app.util.paginator.PageRender;


@Controller
@SessionAttributes("cliente")
public class ClienteController {
	
	/* INYECTAMOS NUESTRO SERVICIO HECHO EN BASE AL DAO DE CLIENTE EL CUAL HEMOS SACADO
	CON CRUD REPOSITORY Y DESPUES HEMOS SUSTITUIDO CON PAGING AND SORTING, RECORDAR QUE TAMBIEN
	TENEMOS OTRAS COMO JPA REPOSITORY, AL FINAL TODAS EXTIENDEN */
	@Autowired
	private IClienteService clienteService;
	
	/* INYECTAMOS EL COMPONENT DE TIPO SERVICE QUE HEMOS CREADO PARA LA SUBIDA DE IMAGENES */
	@Autowired
	private IUploadService uploadService;
	
	
	/* METODO PARA LA SUBIDA DE IMAGENES SIN TENER QUE USAR RESOURCE HANDLER EN MVC CONFIG Y EL QUE
	NOS LLEVAREMOS A UNA INTERFACE E IMPLEMENTACION PARA PODER INYECTAR EN CUALQUIER CONTROLLER
	USA RESPONSE ENTITY Y TRANSPORTE HTTP - ES EL MAS RECOMENDADO, LO HEMOS APUNTADO DENTRO DEL PROYECTO
	PERO PODRIAMOS SACARLO FUERA PERFECTAMENTE COMO YA HEMOS VISTO */
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename){
		
		Resource recurso = null;
		try {
			recurso = uploadService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() +"\"")
			.body(recurso);		
	}
	
	/* METODO PARA MOSTRAR EL DETALLE DEL CLIENTE EN LA VISTA VER.HTML 
	CUANDO PINCHAMOS ENCIMA DEL ID EN EL LISTADO DE CLIENTES */
	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = clienteService.findOne(id);
		if(cliente==null) {
			flash.addFlashAttribute("error", "El cliente no existe en la BBDD");
			return "redirect:/listar";
		}
			model.put("cliente", cliente);
			model.put("titulo", "Detalle cliente: " + cliente.getNombre());
		
		return "ver";
	}
	
	/* METODO PARA LISTAR TODOS NUESTROS CLIENTES EN LA VISTA LISTAR.HTML */
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		
		Pageable pageRequest = new PageRequest(page, 5);
		
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
			model.addAttribute("titulo", "Listado de clientes");
			model.addAttribute("clientes", clientes);
			model.addAttribute("page", pageRender);
		
			return "listar";
	}
	
	/* METODO QUE AL EJECUTARSE NOS POSICIONA EN LA 
	VISTA FORM.HTML LISTOS PARA CREAR UN CLIENTE */
	@RequestMapping(value="/form")
	public String crear(Map<String, Object> model) {
		
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
		return "form";
	}
	
	/* METODO QUE SE EJECUTA AL DAR A EDITAR CLIENTE Y QUE
    AL EJECUTARSE NOS POSICIONA EN LA VISTA FORM.HTML LISTOS 
    PARA EDITAR UN CLIENTE EN BASE AL ID QUE SE LE PASE POR 
    PARAMETRO A LA WILDCARD DEL ENDPOINT */
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = null;
		
		if(id > 0) {
			cliente = clienteService.findOne(id);
			if(cliente == null) {
				flash.addFlashAttribute("error", "El ID cliente no existe en BBDD!");
				
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del Cliente no puede ser 0!");
			
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		
		return "form";
	}
	
	/* METODO PARA CREAR O EDITAR EL CLIENTE, SE EJECUTA CUANDO 
	ESTAMOS DENTRO DEL FORM Y HEMOS RELLENADO EL FORMULARIO Y 
	DADO A CREAR CLIENTE(SUBMIT) YA SEA ENTRANDO AL FORM.HTML
	DESDE EDICION O CREACION DEL CLIENTE*/
	@RequestMapping(value="/form", method=RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		
		if(!foto.isEmpty()) {
			
			if(cliente.getId() !=null 
					&& cliente.getId() > 0
					&& cliente.getFoto()!=null
					&& cliente.getFoto().length() > 0) {
				
					uploadService.delete(cliente.getFoto());
				}
				String uniqueFilename = null;
				try {
					uniqueFilename = uploadService.copy(foto);
				} catch (IOException e) {
					e.printStackTrace();
				}
				flash.addFlashAttribute("info", "Ha subido correctamente '" + uniqueFilename + "'");
				
				cliente.setFoto(uniqueFilename);
			}
		
//			1 APUNTANDO AL WORKSPACE DE LA MANERA MAS SENCILLA		
//			Path directorioRecursos = Paths.get("src//main//resources//static//uploads");
//			String rootPath = directorioRecursos.toFile().getAbsolutePath();
//			try {
//
//				byte[] bytes = foto.getBytes();
//				Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
//				Files.write(rutaCompleta, bytes);
//				flash.addFlashAttribute("info", "Has subido correctamente '" + foto.getOriginalFilename() + "'");
//
//				cliente.setFoto(foto.getOriginalFilename());
//
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
			
//			2 APUNTANDO A DIRECTORIO EXTERNO DE LA MANERA MAS SENCILLA, PARA ELLO 
//		    SOBREESCRIBIREMOS EL METODO RESOURCE HANDLER EN LA CLASE MVC CONFIG 
//		    QUE DEBEREMOS CREAR IMPLEMENTANDOLE LA INTERFACE WEBMVCCONFIGURERADAPTER, 
//		    ES EL MISMO MODO DE SUBIR IMAGENES QUE EL ANTERIOR PERO APUNTANDO FUERA DEL 
//		    WORKSPACE, Y CREAMOS LA  CLASE MVCCONFIG E IMPORTAMOS LA INTERFACE CITADA PARA 
//		    DECIRLE AL MANEJADOR DE RECURSOS DE SPRING (RESOURCE HANDLER, METODO QUE SOBREESCRIBIREMOS) 
//		    QUE NO ACUDA A BUSCARLO AL CLASSPATH DEL WORKSPACE SINO FUERA, A LA RUTA QUE LE HEMOS DADO
//			String rootPath = "C://temp//uploads";
//			log.info("rootPath: " + rootPath);
//			try {
//
//				byte[] bytes = foto.getBytes();
//				Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
//				Files.write(rutaCompleta, bytes);
//				flash.addFlashAttribute("info", "Has subido correctamente '" + foto.getOriginalFilename() + "'");
//
//				cliente.setFoto(foto.getOriginalFilename());
//
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
			
//          3 METODO COPY EN SUSTITUCION DEL WRITE CON BYTES USADO EN LOS 2 ANTERIORES, ADEMAS ESTE NO NECESITA USAR 
//		    LA CLASE MVC CONFIG NI SOBREESCRIBIR EL METODO RESOURCE HANDLER, ESTE NOS LO HEMOS LLEVADO DE AQUI A LA 
//   		INTERFACE E IMPLEMENTACION IUploadService y UploadServiceImpl PARA DESACOPLAR MAS ESE CODIGO DE METODOS DE 
//			STORAGE Y PODER REUTILIZARLO NO SOLO EN ESTE CONTROLADOR SINO EN OTROS Y DEJAR LOS CONTROLADORES SOLO CON 
//			SU LOGICA DE NEGOCIO (CRUD) HACIA BASE DE DATOS

		
//      COMUN A LOS 3 METODOS DE SUBIDA DE IMAGENES, ESTE TROZO SE ENCARGA DEL MENSAJE FLASH PARA 
//      LA CREACION O EDICION Y DE GUARDAR EL CLIENTE EN BASE DE DATOS UNA VEZ CREADO O EDITADO
		String mensajeFlash = (cliente.getId() !=null)? "Cliente editado con éxito!" : "Cliente creado con éxito!";
		
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/listar";
	}
	
	/* METODO PARA ELIMINAR EL CLIENTE EN BASE AL ID QUE SE
	LE PASE, SE EJECUTA DESDE BOTON ELIMNAR EN LISTAR Y 
	RETORNA A LA MISMA VISTA DESPUES DE BORRAR */
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		if(id>0) {
			Cliente cliente = clienteService.findOne(id);
			
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito!");
			
				if(uploadService.delete(cliente.getFoto())) {
					flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con éxito!");
				}
		}
		
		return "redirect:/listar";
	}
}
