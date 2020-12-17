package mx.gob.bienestar.file.operativo.negocio.servicio.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import mx.gob.bienestar.file.operativo.persistencia.config.Pool;
import mx.gob.bienestar.file.operativo.persistencia.dao.OperativoDAO;
import mx.gob.bienestar.file.operativo.persistencia.dao.RegistroDAO;

public class ArchivoService {

	//private static final Logger logger = LogManager.getLogger(ArchivoService.class);

	private final Integer CARGA_CORRECTA = 2;
	private final Integer PENDIENTE = 3;

//	private final String DISCO = "E";
	private final String DISCO = "C";

	public List<Integer> getLista() {

		OperativoDAO operativo = new OperativoDAO();
		return operativo.getLista();

	}

	public void procesar(Integer index) {

		System.out.println("Procesando archivo: " + index);

		long startTime = System.currentTimeMillis();
		if (validarStatus(index)) {
			System.out.println("validarStatus: " + (System.currentTimeMillis() - startTime));

			startTime = System.currentTimeMillis();
			if (cambiarStatusArchivo(index)) {
				System.out.println("cambiarStatusArchivo: " + (System.currentTimeMillis() - startTime));
				startTime = System.currentTimeMillis();
				borrarDatosArchivoAnterior(index);
				System.out.println("borrarDatosArchivoAnterior: " + (System.currentTimeMillis() - startTime));
				readFile(index);
				startTime = System.currentTimeMillis();
				Pool.executeBach();
				System.out.println("executeBach: " + (System.currentTimeMillis() - startTime));
				cambiarStatusOperativo(index);
			}

		}

		Pool.commit();
		System.out.println("Archivo Procesado");

	}

	private void borrarDatosArchivoAnterior(Integer index) {

		OperativoDAO dao = new OperativoDAO();
		dao.borrarErrores(index);
		dao.borrarLote(index);
		Pool.commit();

	}

	private void cambiarStatusOperativo(Integer index) {

		RegistroDAO dao = new RegistroDAO();
		OperativoDAO operativo = new OperativoDAO();

		if (dao.getError(index)) {
			operativo.cambiarStatusOperativo(index, PENDIENTE);
		} else {
			operativo.cambiarStatusOperativo(index, CARGA_CORRECTA);
		}
	}

	private int getRegistros(Integer index) throws Exception {

		String nameFile = getName(index);

		int registros = 0;

		try (FileReader file = new FileReader(DISCO + ":\\almacen\\cargaOperativo\\" + nameFile)) {
			BufferedReader b = new BufferedReader(file);
			while ((b.readLine()) != null) {
				registros += 1;
			}
			b.close();

		} catch (FileNotFoundException e) {
			throw new Exception("El archivo no se puede leer");
		} catch (IOException e) {
			throw new Exception("El archivo no se puede leer");
		}

		return registros;

	}

	private void readFile(Integer index) {

		String nameFile = getName(index);

		RegistroService registroService = new RegistroService();

		try (FileReader file = new FileReader(DISCO + ":\\almacen\\cargaOperativo\\" + nameFile)) {
			BufferedReader b = new BufferedReader(file);
			String cadena = "";
			int registro = 0;

			while ((cadena = b.readLine()) != null) {
				registro += 1;
				if (registro != 1) {
					registroService.save(cadena, registro, index);
				}
			}

			Pool.executeBach();
			Pool.commit();
			b.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String getName(Integer index) {
		OperativoDAO operativo = new OperativoDAO();
		return operativo.getName(index);

	}

	private boolean validarStatus(Integer index) {
		OperativoDAO operativo = new OperativoDAO();
		return operativo.getStatus(index);
	}

	private boolean cambiarStatusArchivo(Integer index) {

		OperativoDAO operativo = new OperativoDAO();

		try {

			int registros = getRegistros(index);
			operativo.cambiarStatusArchivo(index, registros);
			return true;
		} catch (Exception e) {

			operativo.cambiarStatusErrorFile(index, "Error al procesar el archivo, valide el contenido del mismo");
			return false;
		}

	}

	public void borrarCancelados() {
		OperativoDAO dao = new OperativoDAO();
		dao.borrarCancelados();
		Pool.commit();
	}

	public void Crear() {
		OperativoDAO dao = new OperativoDAO();
		dao.MarcarCreado();
		dao.Crear();
		Pool.commit();
	}

}
