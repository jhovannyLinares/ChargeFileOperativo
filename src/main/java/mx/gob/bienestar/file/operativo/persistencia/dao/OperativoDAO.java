package mx.gob.bienestar.file.operativo.persistencia.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import mx.gob.bienestar.file.operativo.persistencia.config.Pool;
import mx.gob.bienestar.file.operativo.persistencia.entity.ArchivoManual;
import oracle.jdbc.OracleTypes;

public class OperativoDAO {

	Logger logger = Logger.getLogger(this.getClass());

	public List<Integer> getLista() {

		String sql = "SELECT ID_OPERATIVO FROM OPERATIVO WHERE ID_ESTATUS_OPERATIVO = 1 AND ESTATUS_ARCHIVO = 0 ORDER BY ID_OPERATIVO ASC ";

		List<Integer> respuesta = new ArrayList<Integer>();

		try (Statement stmt = Pool.createStatement()) {

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				respuesta.add(rs.getInt(1));
			}

		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query");
		}

		return respuesta;

	}

	public boolean getStatus(Integer index) {

		String sql = "SELECT ESTATUS_ARCHIVO FROM OPERATIVO WHERE ID_OPERATIVO = " + index;
		boolean isActivo = false;

		try (Statement stmt = Pool.createStatement()) {

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				isActivo = !rs.getBoolean(1);
			}

		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query");
		}

		return isActivo;
	}

	public boolean cambiarStatusArchivo(Integer index, int registros) {

		String sql = "UPDATE OPERATIVO SET ESTATUS_ARCHIVO = 1, registros = " + registros
				+ ", FECHA_INICIO_PROCESAMIENTO = SYSDATE WHERE ID_OPERATIVO = " + index;
		boolean isExecute = false;

		try (Statement stmt = Pool.createStatement()) {

			stmt.executeUpdate(sql);
			Pool.commit();
			isExecute = true;

		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query");
		}

		return isExecute;

	}

	public boolean cambiarStatusOperativo(Integer index, Integer status) {

		String sql = "UPDATE OPERATIVO SET ID_ESTATUS_OPERATIVO = " + status
				+ ", FECHA_FIN_PROCESAMIENTO = SYSDATE WHERE ID_OPERATIVO = " + index;
		boolean isExecute = false;

		try (Statement stmt = Pool.createStatement()) {

			stmt.executeUpdate(sql);
			Pool.commit();
			isExecute = true;

		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query");
		}

		return isExecute;

	}

	public String getName(Integer index) {

		String sql = "SELECT NOMBRE_GUID FROM OPERATIVO WHERE ID_OPERATIVO = " + index;
		String nombre = "";

		try (Statement stmt = Pool.createStatement()) {

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				nombre = rs.getString(1);
			}

		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query");
		}

		return nombre;

	}

	public void borrarErrores(Integer index) {

		String sql = "DELETE OPERATIVO_ERROR WHERE ID_OPERATIVO = " + index;

		try (Statement stmt = Pool.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query");
		}

	}

	public void borrarLote(Integer index) {

		String sql = "DELETE LOTEDERECHOHABIENTE WHERE OPERATIVO = (SELECT CLAVE_OPERATIVO FROM OPERATIVO WHERE ID_OPERATIVO = "
				+ index + ")";

		try (Statement stmt = Pool.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query");
		}
	}

	public boolean cambiarStatusErrorFile(Integer index, String msg) {

		String sql = "UPDATE OPERATIVO SET ID_ESTATUS_OPERATIVO = 3 , FECHA_INICIO_PROCESAMIENTO = SYSDATE, FECHA_FIN_PROCESAMIENTO = SYSDATE, DESCRIPCION_ERROR = '"
				+ msg + "' WHERE ID_OPERATIVO = " + index;

		boolean isExecute = false;

		try (Statement stmt = Pool.createStatement()) {

			stmt.executeUpdate(sql);
			Pool.commit();
			isExecute = true;

		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query");
		}

		return isExecute;

	}

	public void borrarCancelados() {

		String sql = "DELETE LOTEDERECHOHABIENTE WHERE OPERATIVO IN (SELECT CLAVE_OPERATIVO FROM OPERATIVO WHERE ID_ESTATUS_OPERATIVO = 5)";

		try (Statement stmt = Pool.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query");
		}

	}

	public void Crear() {

		String sql = "UPDATE LOTEDERECHOHABIENTE SET ACTIVO = 1 WHERE OPERATIVO IN(SELECT CLAVE_OPERATIVO FROM OPERATIVO WHERE ID_ESTATUS_OPERATIVO = 4 AND ESTATUS_ARCHIVO = 2)";

		try (Statement stmt = Pool.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query");
		}

	}

	public void MarcarCreado() {

		String sql = "UPDATE OPERATIVO SET ESTATUS_ARCHIVO = 2  WHERE ID_ESTATUS_OPERATIVO = 4";

		try (Statement stmt = Pool.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query");
		}

	}

	public void validacionPKG(Integer index) {
		String sql = "{call PKG_OPERATIVO_VALIDACIONES.SP_VALIDA_REGISTRO (?,?)}";
		try (CallableStatement stmt = Pool.prepareCall(sql)) {
			stmt.setInt(1, index);
			stmt.registerOutParameter(2, OracleTypes.NUMERIC);
			stmt.execute();
		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query " + sql);
			e.printStackTrace();
		}
	}

	public void guardarRegistro(ArchivoManual archivoManual) {

		String sql = "INSERT INTO APP_MOVIL.OPERATIVO (ID_ESTATUS_OPERATIVO, ID_PROGRAMA, CLAVE_OPERATIVO,  FECHA_INICIO, FECHA_FIN, FECHA_CORTE, NOMBRE_GUID, ESTATUS_ARCHIVO,NOMBRE_ARCHIVO) "
				+ " VALUES( 1, ?, ?, ?, ?, ?, ?, 0,?) ";

		try (PreparedStatement stmt = Pool.createPreparedStatement(sql)) {

			stmt.setInt(1, archivoManual.getIdOperativo());
			stmt.setString(2, archivoManual.getOperativo());
			stmt.setString(3, archivoManual.getFechaInicio());
			stmt.setString(4, archivoManual.getFechaFin());
			stmt.setString(5, archivoManual.getFechaCorte());
			stmt.setString(6, archivoManual.getGuid()+archivoManual.getExtend());
			stmt.setString(7, archivoManual.getFileName());

			stmt.execute();

		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query");
			logger.debug(e);
			e.printStackTrace();
		}

	}

}
