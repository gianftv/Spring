package com.sopra.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadService {

	public Resource load(String filename) throws MalformedURLException ;
	
	public String  copy(MultipartFile file) throws IOException;
	
	public boolean delete(String filename);
	
	/*METODOS CREADOS PARA PODER ELIMINAR Y CREAR UPLOADS CADA VEZ QUE SE CIERRA O ABRE ECLIPSE O SE
	LEVANTA Y PARA EL SERVIDOR*/
	
	//public void deleteAll();
	
	//public void init() throws IOException;
	
}
