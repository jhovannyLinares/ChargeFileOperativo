package mx.gob.bienestar.file.operativo.negocio.servicio.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import mx.gob.bienestar.file.operativo.persistencia.config.Pool;
import mx.gob.bienestar.file.operativo.persistencia.dao.OperativoDAO;
import mx.gob.bienestar.file.operativo.persistencia.dao.RegistroDAO;
import mx.gob.bienestar.file.operativo.persistencia.entity.ArchivoManual;

public class ArchivoService {

	static Logger logger = Logger.getLogger(ArchivoService.class.getName());

	private final Integer CARGA_CORRECTA = 2;
	private final Integer PENDIENTE = 3;

//	private final String DISCO = "E";
	private final String DISCO = "C";

	public List<Integer> getLista() {

		OperativoDAO operativo = new OperativoDAO();
		return operativo.getLista();

	}

	public void procesar(Integer index) {

		logger.debug("Procesando archivo: " + index);

		long startTime = System.currentTimeMillis();
		if (validarStatus(index)) {
			logger.debug("validarStatus: " + (System.currentTimeMillis() - startTime));

			startTime = System.currentTimeMillis();
			if (cambiarStatusArchivo(index)) {
				logger.debug("cambiarStatusArchivo: " + (System.currentTimeMillis() - startTime));
				startTime = System.currentTimeMillis();
				borrarDatosArchivoAnterior(index);
				logger.debug("borrarDatosArchivoAnterior: " + (System.currentTimeMillis() - startTime));
				readFile(index);
				startTime = System.currentTimeMillis();
				Pool.executeBach();
				logger.debug("executeBach: " + (System.currentTimeMillis() - startTime));
				Pool.commit();
				executePKG(index);
				cambiarStatusOperativo(index);
			}

		}

		Pool.commit();
		logger.debug("Archivo Procesado");

	}

	private void executePKG(Integer index) {
		OperativoDAO dao = new OperativoDAO();
		dao.validacionPKG(index);
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
			logger.debug(e);
			throw new Exception("El archivo no se puede leer");
		} catch (IOException e) {
			logger.debug(e);
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
			logger.debug(e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.debug(e);
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
			logger.debug(e);
			operativo.cambiarStatusErrorFile(index, "Error al procesar el archivo, valide el contenido del mismo");
			return false;
		}

	}

	public void borrarCancelados() {
		OperativoDAO dao = new OperativoDAO();
		logger.debug("Borrado de Operativos Cancelados");
		dao.borrarCancelados();
		Pool.commit();
	}

	public void Crear() {
		OperativoDAO dao = new OperativoDAO();
		logger.debug("Marcando Creados");
		dao.MarcarCreado();
		dao.Crear();
		Pool.commit();
	}

	public void cargaArchivosLocales() {

		logger.debug("Moviendo los archivos Locales");

		String from = DISCO + ":\\almacen\\cargaOperativoManual\\";
		String destino = DISCO + ":\\almacen\\cargaOperativo\\";

		File folder = new File(from);

		List<ArchivoManual> filesName = buscarArchivos(folder);

		try {
			moveFiles(from, destino, filesName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		OperativoDAO dao = new OperativoDAO();
		
		for (ArchivoManual archivoManual : filesName) {
			dao.guardarRegistro(archivoManual);
		}
		
		

	}

	private void moveFiles(String folder, String salida, List<ArchivoManual> filesName) throws IOException {

		for (ArchivoManual archivoManual : filesName) {

			Path from = Paths.get(folder + archivoManual.getFileName());
			Path dest = Paths.get(salida + archivoManual.getGuid() + archivoManual.getExtend());

			logger.debug("Moviendo " + from + " -> " + dest);

			Files.move(from, dest, StandardCopyOption.REPLACE_EXISTING);

		}

	}

	private static List<ArchivoManual> buscarArchivos(File folder) {

		logger.debug("Buscando en: -> " + folder.getAbsolutePath());

		List<ArchivoManual> filesName = new ArrayList<ArchivoManual>();

		ArchivoManual am = null;
		String fileName = "";

		for (File file : folder.listFiles()) {

			if (!file.isDirectory()) {

				am = new ArchivoManual();

				fileName = file.getName().replaceFirst("[.][^.]+$", "");

				if (nombreValido(fileName, am)) {

					am.setName(fileName);
					am.setFileName(file.getName());
					am.setExtend(file.getName().replace(fileName, ""));

					filesName.add(am);

				}

				logger.debug(file.getName());
			} else {
				buscarArchivos(file);
			}
		}

		return filesName;
	}

	private static boolean nombreValido(String fileName, ArchivoManual am) {

		String[] splitStr = fileName.split("-");

		try {

			if (splitStr.length > 3) {

				int idOperativo = Integer.parseInt(splitStr[0]);

				String operativo = splitStr[1];
				String fechaInicio = splitStr[2];
				String fechaFin = splitStr[3];

				convertDate(fechaInicio);
				convertDate(fechaFin);

				am.setIdOperativo(idOperativo);
				am.setOperativo(operativo);
				am.setFechaInicio(fechaInicio);
				am.setFechaFin(fechaFin);

				if (splitStr.length > 4) {
					String fechaCorte = splitStr[4];
					convertDate(fechaCorte);
					am.setFechaCorte(fechaCorte);
					logger.debug(fechaCorte);
				}

				return true;
			}

		} catch (Exception e) {
			return false;
		}

		return false;
	}

	private static void convertDate(String fechaString) throws ParseException {

		SimpleDateFormat sdfrmt = new SimpleDateFormat("ddMMyyyy");
		sdfrmt.setLenient(false);
		Date date = sdfrmt.parse(fechaString);
		logger.debug(date);

	}

}
