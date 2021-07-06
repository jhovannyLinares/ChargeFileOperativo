package mx.gob.bienestar.file.operativo.negocio.servicio.impl;

import java.util.List;

import org.apache.log4j.Logger;

import mx.gob.bienestar.file.operativo.negocio.servicio.ICargaService;

public class CargaService implements ICargaService {

	static Logger logger = Logger.getLogger(CargaService.class.getName());

	@Override
	public void ini() {

		ArchivoService archivoService = new ArchivoService();
		
		archivoService.borrarCancelados();
		archivoService.cargaArchivosLocales();
		archivoService.Crear();
		
		List<Integer> indexs = archivoService.getLista();

		for (Integer index : indexs) {
			archivoService.procesar(index);
		}

		logger.debug("Se procesaron todos los archivos");

	}

}
