package com.sopra.springboot.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
//import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UploadServiceImpl implements IUploadService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final static String UPLOADS_FOLDER = "uploads";

	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathFoto = getPath(filename);
		log.info("pathFoto: " + pathFoto);
		Resource recurso = null;
		
			 recurso = new UrlResource(pathFoto.toUri());
			 if(!recurso.exists() || !recurso.isReadable()) {
				 throw new RuntimeException ("Error: no se puede cargar la imagen: " + pathFoto.toString());
			 }
		

		return recurso;
	}

	@Override
	public String copy(MultipartFile file)  throws IOException {
		//APUNTANDO A DIRECTORIO EXTERNO - OPCION MAS COMUN USANDO UN UNIQUE FILENAME
		String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();//nombre de la foto con numero random pasado a string
		//path relativo al proyecto
		Path rootPath = getPath(uniqueFilename);
		//path absoluto
		
		//usamos la clase LogFactory para ber por consola nuestras rutas
		log.info("rootPath: " + rootPath);
	
		
		
		
//			USANDO write Y PASANDOLO COMO bytes
//			byte[] bytes= foto.getBytes();
//			Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
//			Files.write(rutaCompleta, bytes);
			
//			MISMA OPERACION CON EL METODO COPY
			Files.copy(file.getInputStream(), rootPath);//foto del multiartfile, copia la foto a roorAbsolutePath
				
		return uniqueFilename;
	}

	@Override
	public boolean delete(String filename) {
		Path rootPath = getPath(filename);
		File archivo = rootPath.toFile();
		
		if(archivo.exists() && archivo.canRead()) {
			if(archivo.delete()){
				return true;
				
			}
		}
		return false;
	}
	
	public Path getPath(String filename) {
		
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
		
	}
	/*METODOS CREADOS PARA PODER ELIMINAR Y CREAR UPLOADS CADA VEZ QUE SE CIERRA O ABRE ECLIPSE O SE
	LEVANTA Y PARA EL SERVIDOR*/
//	@Override
//	public void deleteAll() {
//		
//		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
//		
//		
//	}
//
//	@Override
//	public void init() throws IOException {
//		
//		Files.createDirectory(Paths.get(UPLOADS_FOLDER));
//		
//	}
}
