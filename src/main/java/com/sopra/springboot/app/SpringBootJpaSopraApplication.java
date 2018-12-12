package com.sopra.springboot.app;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import com.sopra.springboot.app.models.service.IUploadService;

@SpringBootApplication/*IMPLEMENTAMOS COMMAND LINE RUNNER PARA PEDIRLE QUE USE LOS DOS METODOS
QUE HEMOS CREADO EN IUPLOADSERVICE E IMPLEMENTADO EN UPLOADSERVICEIMP
Y PODER DE ESE MODO DESTRUIR LA CARPETA UPLOADS Y SU CONTENIDO Y VOLVER A CREAR AL LEVANTAR EL SERVIDOR,
EN ESTE CASO NO ES UTIL, PERO PUEDE RESULTAR UTIL EN PROCESOS BATCH...., O EN OPERACIONES INTERMEDIAS EN LAS QUE TRANSFORMAMOS 
O TRATAMOS ARCHIVOS QUE LUEGO NO SON EL DEFINITIVO Y DEBEN BORRARSE  */
public class SpringBootJpaSopraApplication // implements CommandLineRunner
{
//	
//	@Autowired
//	IUploadService  uploadService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJpaSopraApplication.class, args);
	}

//	@Override
//	public void run(String... arg0) throws Exception {// ya podemos utilizar uploadservice para inyectar
//	uploadService.deleteAll();
//		uploadService.init();
//		
//	}
}
