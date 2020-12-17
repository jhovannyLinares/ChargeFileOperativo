package mx.gob.bienestar.file.operativo.negocio.servicio.impl;

import java.util.List;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import mx.gob.bienestar.file.operativo.negocio.servicio.ICargaService;

public class CargaService implements ICargaService {
	
	//private static final Logger logger = LogManager.getLogger(CargaService.class);

	@Override
	public void ini() {
		
		ArchivoService archivoService = new ArchivoService();
		archivoService.borrarCancelados();
		archivoService.Crear();
		List<Integer> indexs =  archivoService.getLista();
		
		for (Integer index : indexs) {	
			archivoService.procesar(index);
		}
		
		System.out.println("Se procesaron todos los archivos");
		
	}

}
