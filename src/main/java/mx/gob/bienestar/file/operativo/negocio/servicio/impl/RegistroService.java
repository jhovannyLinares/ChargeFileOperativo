package mx.gob.bienestar.file.operativo.negocio.servicio.impl;

import org.apache.log4j.Logger;

import mx.gob.bienestar.file.operativo.persistencia.dao.RegistroDAO;
import mx.gob.bienestar.file.operativo.persistencia.entity.Registro;

public class RegistroService {

	Logger logger = Logger.getLogger(this.getClass());

	private static final String tab = "\t";
	private static final String comma = ",";
	private static final String pipe = "|";
	private static final String LONGITUD_ERROR = "El registro no cumple la longitud del layout";

	RegistroDAO dao = null;

	public void save(String cadena, int registro, Integer index) {

		String[] registros = split(cadena);

		dao = new RegistroDAO();

		if (registros.length == 28) {

			try {

				registros = limpieza(registros);

				long startTime = System.currentTimeMillis();

				if (correcto(index, registros)) {

//					logger.debug("Validaciones: " + (System.currentTimeMillis() - startTime));
//					startTime = System.currentTimeMillis();
//					save(registros);
//					logger.debug("guardado: " + (System.currentTimeMillis() - startTime));n
					SaveTemporal(registros, registro);
				}

			} catch (Exception e) {
				dao.saveError(index, registro, registros, e.getMessage());
			}

		} else {

			dao.saveError(index, registro, registros, LONGITUD_ERROR);

		}

	}

	private void SaveTemporal(String[] registros, int numRegistro) {
		Registro registro = asingacion(registros);
		dao.saveTemporal(registro, numRegistro);
	}

	private String[] limpieza(String[] registros) {

		for (int i = 0; i < registros.length; i++) {
			String string = registros[i];
			string = string.trim().replace("NA", "").replace("\"", "").replace(",", "");
			if (string.equalsIgnoreCase("")) {
				string = null;
			}
			registros[i] = string;
		}
		return registros;
	}

	private boolean correcto(Integer index, String[] registros) throws Exception {
		boolean correcto = false;
		long startTime = System.currentTimeMillis();
		if (validaOperativo(index, registros[0])) {
			logger.debug("validaOperativo: " + (System.currentTimeMillis() - startTime));
			startTime = System.currentTimeMillis();
			if (validaEntidad(registros[25])) {
				logger.debug("ValidaciovalidaEntidadnes: " + (System.currentTimeMillis() - startTime));
				correcto = true;
			} else {
				throw new Exception("Entidad no localizada " + registros[25]);
			}
		} else {
			throw new Exception("El Operativo no corresponde al definido " + registros[0]);
		}
		return correcto;
	}

	private boolean validaOperativo(Integer index, String operativo) {

		boolean isValido = false;
		isValido = CatalogoService.getOperativo(index, operativo);

		return isValido;

	}

	private boolean validaPadron(String padron, String reposicion) {

		boolean isValido = false;
		isValido = CatalogoService.getPadron(padron, reposicion);

		return isValido;

	}

	private boolean validaTarjeta(String tarjeta) {

		boolean isValido = false;
		isValido = CatalogoService.getTarjetas(tarjeta);

		return isValido;
	}

	private boolean validaLocalidadInegi(String entidad, String municipio, String localidadInegi) {

		boolean isValido = false;

		if (CatalogoService.getLocalidadInegi(entidad, municipio, localidadInegi) == true) {
			return true;
		}

		return isValido;
	}

	private boolean validaRegion(String entidad, String region) {

		if (CatalogoService.getRegiones(entidad, region) == true) {
			return true;
		}

		return false;

	}

	private boolean validaMunicipio(String entidad, String municipio) {

		if (CatalogoService.getMunicipios(entidad, municipio) == true) {
			return true;
		}

		return false;

	}

	private boolean validaEntidad(String entidad) {

		if (CatalogoService.getEntidades(entidad) == true) {
			return true;
		}

		return false;
	}

	private String[] split(String cadena) {

		String[] comillas = cadena.split("\"");

		if (comillas.length > 0) {

			StringBuffer cad = new StringBuffer();

			boolean isPar = false;

			for (int i = 0; i < comillas.length; i++) {
				String string = comillas[i];
				if (isPar) {
					isPar = false;
					comillas[i] = string.replace(",", "");
				} else {
					isPar = true;
				}
			}

			for (String string : comillas) {

				cad.append(string);
			}

			cadena = cad.toString();
		}

		cadena = cadena.trim().replace("NA", "").replace("\"", "");

		String[] registros = cadena.split(tab);

		if (registros.length == 1) {
			registros = cadena.split(comma);
		}

		if (registros.length == 1) {
			registros = cadena.split(pipe);
		}

		return registros;

	}

	private void save(String[] registros) {

		Registro reg = new Registro();

		reg.setOPERATIVO(registros[0]);
		reg.setID_PADRON(registros[1]);
		reg.setID_PROGRAMA_SOCIAL(registros[2]);
		reg.setTITULAR_A_PATERNO(registros[3]);
		reg.setTITULAR_A_MATERNO(registros[4]);
		reg.setTITULAR_NOMBRE(registros[5]);
		reg.setTITULAR_CURP(registros[6]);
		reg.setREGISTRO_AUXILIAR(registros[7]);
		reg.setAUX_A_PATERNO(registros[8]);
		reg.setAUX_A_MATERNO(registros[9]);
		reg.setAUX_NOMBRE(registros[10]);
		reg.setAUX_CURP(registros[11]);
		reg.setID_LOCALIDAD_INEGI(registros[12]);
		reg.setID_REGION(registros[13]);
		reg.setLOCALIDADID(registros[14]);
		reg.setCOLONIA(registros[15]);
		reg.setCALLE(registros[16]);
		reg.setAREA(registros[17]);
		reg.setNUMERO_EXTERNO(registros[18]);
		reg.setNUMERO_INTERIOR(registros[19]);
		reg.setMANZANA(registros[20]);
		reg.setLOTE(registros[21]);
		reg.setCODIGO_POSTAL(registros[22]);
		reg.setID_ACUSE(registros[23]);
		reg.setTARJETAID(registros[24]);
		reg.setID_ENTIDAD_FEDERATIVA(registros[25]);
		reg.setID_MUNICIPIO(registros[26]);
		reg.setREPOSICION(registros[27]);

		dao.save(reg);

	}

	private Registro asingacion(String[] registros) {
		Registro registro = new Registro();
		registro.setOPERATIVO(registros[0]);
		registro.setID_PADRON(registros[1]);
		registro.setID_PROGRAMA_SOCIAL(registros[2]);
		registro.setTITULAR_A_PATERNO(registros[3]);
		registro.setTITULAR_A_MATERNO(registros[4]);
		registro.setTITULAR_NOMBRE(registros[5]);
		registro.setTITULAR_CURP(registros[6]);
		registro.setREGISTRO_AUXILIAR(registros[7]);
		registro.setAUX_A_PATERNO(registros[8]);
		registro.setAUX_A_MATERNO(registros[9]);
		registro.setAUX_NOMBRE(registros[10]);
		registro.setAUX_CURP(registros[11]);
		registro.setID_LOCALIDAD_INEGI(registros[12]);
		registro.setID_REGION(registros[13]);
		registro.setLOCALIDADID(registros[14]);
		registro.setCOLONIA(registros[15]);
		registro.setCALLE(registros[16]);
		registro.setAREA(registros[17]);
		registro.setNUMERO_EXTERNO(registros[18]);
		registro.setNUMERO_INTERIOR(registros[19]);
		registro.setMANZANA(registros[20]);
		registro.setLOTE(registros[21]);
		registro.setCODIGO_POSTAL(registros[22]);
		registro.setID_ACUSE(registros[23]);
		registro.setTARJETAID(registros[24]);
		registro.setID_ENTIDAD_FEDERATIVA(registros[25]);
		registro.setID_MUNICIPIO(registros[26]);
		registro.setREPOSICION(registros[27]);
		return registro;
	}

}
