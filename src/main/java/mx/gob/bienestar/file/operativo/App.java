package mx.gob.bienestar.file.operativo;

import org.apache.log4j.Logger;

import mx.gob.bienestar.file.operativo.negocio.servicio.ICargaService;
import mx.gob.bienestar.file.operativo.negocio.servicio.impl.CargaService;
import mx.gob.bienestar.file.operativo.persistencia.config.Pool;

public class App {

	static Logger logger = Logger.getLogger(App.class.getName());

	public static void main(String[] args) {

		ICargaService cargaService = null;
		logger.debug("Iniciando conexion");

		if (initConexion()) {
			cargaService = new CargaService();
			cargaService.ini();
			commit();

		} else {
			logger.debug("No se logro conectar a la BBDD");
		}

	}

	private static void commit() {
		Pool.commit();

	}

	private static boolean initConexion() {
		return Pool.initConexion();
	}

}
