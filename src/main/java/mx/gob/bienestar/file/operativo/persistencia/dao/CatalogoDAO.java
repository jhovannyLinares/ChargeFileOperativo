package mx.gob.bienestar.file.operativo.persistencia.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import mx.gob.bienestar.file.operativo.persistencia.config.Pool;

public class CatalogoDAO {

	//private static final Logger logger = LogManager.getLogger(CatalogoDAO.class);

	public HashMap<String, HashMap<String, HashMap<Integer, Boolean>>> getEntidadesMunicipio(String entidad,
			HashMap<String, HashMap<String, HashMap<Integer, Boolean>>> entidadesMunicipio) {

		String sql = "SELECT ID_ENTIDAD_FEDERATIVA FROM CAT_ENTIDAD_FEDERATIVA WHERE ID_ENTIDAD_FEDERATIVA = "
				+ entidad;
		System.out.println( sql);
		try (Statement stmt = Pool.createStatement()) {

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				entidadesMunicipio.put(rs.getString(1), new HashMap<String, HashMap<Integer, Boolean>>());
			}

		} catch (SQLException e) {
			System.out.println( "Error en la ejecucion del Query");
		}

		return entidadesMunicipio;

	}

	public HashMap<String, HashMap<String, Boolean>> getRegiones(String entidad,
			HashMap<String, HashMap<String, Boolean>> regiones) {

		String sql = "select ID_ENTIDAD_FEDERATIVA, ID_REGION from cat_region where ID_ENTIDAD_FEDERATIVA = '" + entidad
				+ "' ";

		System.out.println( sql);
		try (Statement stmt = Pool.createStatement()) {

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				entidad = rs.getString(1);
				String region = rs.getString(2);

				if (regiones.get(entidad) == null) {
					regiones.put(entidad, new HashMap<String, Boolean>());
				}

				regiones.get(entidad).put(region, true);

			}

		} catch (SQLException e) {
			System.out.println( "Error en la ejecucion del Query");
		}

		return regiones;

	}

	public HashMap<String, HashMap<String, HashMap<Integer, Boolean>>> getLocalidades(String entidad, String municipio,
			HashMap<String, HashMap<String, HashMap<Integer, Boolean>>> municipios) {

		String sql = "SELECT ID_INEGI FROM CAT_LOCALIDAD WHERE ID_ENTIDAD_FEDERATIVA = '" + entidad
				+ "' AND ID_MUNICIPIO = '" + municipio + "'";

		System.out.println( sql);
		try (Statement stmt = Pool.createStatement()) {

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				municipios.get(entidad).get(municipio).put(rs.getInt(1), true);
			}

		} catch (SQLException e) {
			System.out.println( "Error en la ejecucion del Query");
		}

		return municipios;

	}

	public HashMap<String, Boolean> getTarjetas() {

		String sql = "SELECT TARJETAID FROM LOTEDERECHOHABIENTE ";

		HashMap<String, Boolean> tarjetas = new HashMap<String, Boolean>();

		System.out.println( sql);
		try (Statement stmt = Pool.createStatement()) {

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				tarjetas.put(rs.getString(1), true);
			}

		} catch (SQLException e) {
			System.out.println( "Error en la ejecucion del Query");
		}

		return tarjetas;
	}

	public HashMap<String, Boolean> getEntidades(HashMap<String, Boolean> entidades) {

		String sql = "SELECT ID_ENTIDAD_FEDERATIVA FROM CAT_ENTIDAD_FEDERATIVA";

		System.out.println( sql);
		try (Statement stmt = Pool.createStatement()) {

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				entidades.put(rs.getString(1), true);

			}

		} catch (SQLException e) {
			System.out.println( "Error en la ejecucion del Query");
		}

		return entidades;
	}

	public HashMap<String, HashMap<String, HashMap<Integer, Boolean>>> getMunicipios(String entidad,
			HashMap<String, HashMap<String, HashMap<Integer, Boolean>>> entidadesMunicipios) {

		String sql = "SELECT ID_ENTIDAD_FEDERATIVA,ID_MUNICIPIO FROM CAT_MUNICIPIO WHERE ID_ENTIDAD_FEDERATIVA = '"
				+ entidad + "'";

		System.out.println( sql);
		try (Statement stmt = Pool.createStatement()) {

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {

				entidad = rs.getString(1);
				String municipio = rs.getString(2);

				if (entidadesMunicipios.get(entidad) == null) {
					entidadesMunicipios.put(entidad, new HashMap<String, HashMap<Integer, Boolean>>());
				}
				entidadesMunicipios.get(entidad).put(municipio, new HashMap<Integer, Boolean>());

			}

		} catch (SQLException e) {
			System.out.println( "Error en la ejecucion del Query");
		}

		return entidadesMunicipios;

	}

	public HashMap<String, Boolean> getPadron() {
		String sql = "SELECT id_padron FROM LOTEDERECHOHABIENTE ";

		HashMap<String, Boolean> padrones = new HashMap<String, Boolean>();

		System.out.println( sql);
		try (Statement stmt = Pool.createStatement()) {

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				padrones.put(rs.getString(1), true);
			}

		} catch (SQLException e) {
			System.out.println( "Error en la ejecucion del Query");
		}

		return padrones;
	}

	public HashMap<Integer, String> getOperativos(HashMap<Integer, String> operativos) {
		
		String sql = "SELECT ID_OPERATIVO, CLAVE_OPERATIVO FROM OPERATIVO";

		System.out.println( sql);
		try (Statement stmt = Pool.createStatement()) {

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				operativos.put(rs.getInt(1), rs.getString(2));

			}

		} catch (SQLException e) {
			System.out.println( "Error en la ejecucion del Query");
		}

		return operativos;
	}

}
