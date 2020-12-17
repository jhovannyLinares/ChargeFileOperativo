package mx.gob.bienestar.file.operativo;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import mx.gob.bienestar.file.operativo.negocio.servicio.ICargaService;
import mx.gob.bienestar.file.operativo.negocio.servicio.impl.CargaService;
import mx.gob.bienestar.file.operativo.persistencia.config.Pool;

public class App {

	//private static final Logger logger = LogManager.getLogger(App.class);

	public static void main(String[] args) {

		ICargaService cargaService = null;
		System.out.println("Iniciando conexion");
		
		if (initConexion()) {
			
			cargaService = new CargaService();
			cargaService.ini();
			commit();

		} else {
			System.out.println("No se logro conectar a la BBDD");
		}
		
	}

	private static void commit() {
		Pool.commit();

	}

	private static boolean initConexion() {
		return Pool.initConexion();
	}

}
