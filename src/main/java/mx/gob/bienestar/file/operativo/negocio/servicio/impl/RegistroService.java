package mx.gob.bienestar.file.operativo.negocio.servicio.impl;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import mx.gob.bienestar.file.operativo.persistencia.dao.RegistroDAO;
import mx.gob.bienestar.file.operativo.persistencia.entity.Registro;

public class RegistroService {

	//private static final Logger logger = LogManager.getLogger(RegistroService.class);

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
					System.out.println("Validaciones: " + (System.currentTimeMillis() - startTime));
					startTime = System.currentTimeMillis();
					save(registros);
					System.out.println("guardado: " + (System.currentTimeMillis() - startTime));
				}

			} catch (Exception e) {
				dao.saveError(index, registro, registros, e.getMessage());
			}

		} else {

			dao.saveError(index, registro, registros, LONGITUD_ERROR);

		}

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
			System.out.println("validaOperativo: " + (System.currentTimeMillis() - startTime));
			startTime = System.currentTimeMillis();
			if (validaEntidad(registros[25])) {
				System.out.println("ValidaciovalidaEntidadnes: " + (System.currentTimeMillis() - startTime));
				startTime = System.currentTimeMillis();
				if (validaMunicipio(registros[25], registros[26])) {
					System.out.println("validaMunicipio: " + (System.currentTimeMillis() - startTime));
					startTime = System.currentTimeMillis();
					if (validaRegion(registros[25], registros[13])) {
						System.out.println("validaRegion: " + (System.currentTimeMillis() - startTime));
						startTime = System.currentTimeMillis();
						if (validaLocalidadInegi(registros[25], registros[26], registros[12])) {
							System.out.println("validaLocalidadInegi: " + (System.currentTimeMillis() - startTime));
							startTime = System.currentTimeMillis();
							if (validaTarjeta(registros[24])) {
								System.out.println("validaTarjeta: " + (System.currentTimeMillis() - startTime));
								startTime = System.currentTimeMillis();
								if (validaPadron(registros[1], registros[27])) {
									System.out.println("validaPadron: " + (System.currentTimeMillis() - startTime));
									correcto = true;
								} else {
									throw new Exception("El Beneficiario ya cuenta con tarjeta " + registros[1]);
								}
							} else {
								throw new Exception("La tarjeta ya se encuentra registrada " + registros[24]);
							}
						} else {
							throw new Exception("LocalidadInegi no localizada " + registros[12]);
						}
					} else {
						throw new Exception("Region no localizada " + registros[13]);
					}
				} else {
					throw new Exception("Municipio no localizado " + registros[26]);
				}
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

}
