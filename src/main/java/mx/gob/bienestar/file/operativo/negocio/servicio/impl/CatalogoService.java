package mx.gob.bienestar.file.operativo.negocio.servicio.impl;

import java.util.HashMap;

import mx.gob.bienestar.file.operativo.persistencia.dao.CatalogoDAO;

public class CatalogoService {

	public static HashMap<String, Boolean> tarjetas = null;
	public static HashMap<String, Boolean> padrones = null;
	public static HashMap<String, Boolean> entidades = new HashMap<String, Boolean>();
	public static HashMap<Integer, String> operativos = new HashMap<Integer, String>();
	public static HashMap<String, HashMap<String, Boolean>> entidadesRegiones = new HashMap<String, HashMap<String, Boolean>>();
	public static HashMap<String, HashMap<String, HashMap<Integer, Boolean>>> entidadesMunicipios = new HashMap<String, HashMap<String, HashMap<Integer, Boolean>>>();

	private CatalogoService() {
	}

	public static boolean getRegiones(String entidad, String region) {

		CatalogoDAO dao = new CatalogoDAO();

		if (entidadesRegiones.get(entidad) == null) {
			entidadesRegiones = dao.getRegiones(entidad, entidadesRegiones);
		}

		if (entidadesRegiones.get(entidad).get(region) == null) {
			entidadesRegiones = dao.getRegiones(entidad, entidadesRegiones);
		}

		if (entidadesRegiones.get(entidad).get(region) != null) {
			return true;
		}

		return false;

	}

	public static boolean getLocalidadInegi(String entidad, String municipio, String localidadInegi) {

		CatalogoDAO dao = new CatalogoDAO();

		Integer localidad = Integer.parseInt(localidadInegi);

		if (entidadesMunicipios.get(entidad).get(municipio) == null) {
			entidadesMunicipios = dao.getLocalidades(entidad, municipio, entidadesMunicipios);
		}

		if (entidadesMunicipios.get(entidad).get(municipio).get(localidad) == null) {
			entidadesMunicipios = dao.getLocalidades(entidad, municipio, entidadesMunicipios);
		}

		if (entidadesMunicipios.get(entidad).get(municipio).get(localidad) != null) {
			return true;
		}

		return false;

	}

	public static boolean getEntidades(String entidad) {

		if (entidades.get(entidad) == null) {
			entidades.put(entidad, false);
			CatalogoDAO dao = new CatalogoDAO();
			entidades = dao.getEntidades(entidades);
		}

		if (entidades.get(entidad) == true) {
			return true;
		}else {
			return false;
		}

	}

	public static boolean getMunicipios(String entidad, String municipio) {

		CatalogoDAO dao = new CatalogoDAO();

		if (entidadesMunicipios.get(entidad) == null) {
			entidadesMunicipios = dao.getMunicipios(entidad, entidadesMunicipios);
		}

		if (entidadesMunicipios.get(entidad).get(municipio) == null) {
			entidadesMunicipios = dao.getMunicipios(entidad, entidadesMunicipios);
		}

		if (entidadesMunicipios.get(entidad).get(municipio) != null) {
			return true;
		}

		return false;
	}

	public static boolean getTarjetas(String tarjeta) {

		CatalogoDAO dao = new CatalogoDAO();

		if (tarjetas == null) {
			tarjetas = dao.getTarjetas();
		}

		if (tarjetas.get(tarjeta) == null) {
			tarjetas.put(tarjeta, true);
			return true;
		}

		return false;
	}

	public static boolean getPadron(String padron, String reposicion) {
		CatalogoDAO dao = new CatalogoDAO();

		if (padrones == null) {
			padrones = dao.getPadron();
		}

		if (reposicion.equalsIgnoreCase("1")) {
			return true;
		} else {
			if (padrones.get(padron) == null) {
				padrones.put(padron, true);
				return true;
			}
		}

		return false;
	}

	public static boolean getOperativo(Integer index, String operativo) {

		CatalogoDAO dao = null;

		if (operativos.get(index) == null) {
			dao = new CatalogoDAO();
			operativos = dao.getOperativos(operativos);
		}

		if (operativos.get(index).equalsIgnoreCase(operativo)) {
			return true;
		}

		return false;
	}

}
