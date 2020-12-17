package mx.gob.bienestar.file.operativo.persistencia.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import mx.gob.bienestar.file.operativo.persistencia.config.Pool;

public class OperativoDAO {
	
	//private static final Logger logger = LogManager.getLogger(OperativoDAO.class);

	public List<Integer> getLista() {
		
		String sql = "SELECT ID_OPERATIVO FROM OPERATIVO WHERE ID_ESTATUS_OPERATIVO = 1 AND ESTATUS_ARCHIVO = 0 ORDER BY ID_OPERATIVO ASC ";

		List<Integer> respuesta = new ArrayList<Integer>();

		try (Statement stmt = Pool.createStatement()) {

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				respuesta.add(rs.getInt(1));
			}

		} catch (SQLException e) {
			System.out.println("Error en la ejecucion del Query");
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
			System.out.println("Error en la ejecucion del Query");
		}

		return isActivo;
	}

	public boolean cambiarStatusArchivo(Integer index, int registros) {

		String sql = "UPDATE OPERATIVO SET ESTATUS_ARCHIVO = 1, registros = " + registros + ", FECHA_INICIO_PROCESAMIENTO = SYSDATE WHERE ID_OPERATIVO = "
				+ index;
		boolean isExecute = false;

		try (Statement stmt = Pool.createStatement()) {

			stmt.executeUpdate(sql);
			Pool.commit();
			isExecute = true;

		} catch (SQLException e) {
			System.out.println("Error en la ejecucion del Query");
		}

		return isExecute;

	}

	public boolean cambiarStatusOperativo(Integer index, Integer status) {

		String sql = "UPDATE OPERATIVO SET ID_ESTATUS_OPERATIVO = " + status + ", FECHA_FIN_PROCESAMIENTO = SYSDATE WHERE ID_OPERATIVO = " + index;
		boolean isExecute = false;

		try (Statement stmt = Pool.createStatement()) {

			stmt.executeUpdate(sql);
			Pool.commit();
			isExecute = true;

		} catch (SQLException e) {
			System.out.println("Error en la ejecucion del Query");
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
			System.out.println("Error en la ejecucion del Query");
		}

		return nombre;

	}

	public void borrarErrores(Integer index) {

		String sql = "DELETE OPERATIVO_ERROR WHERE ID_OPERATIVO = " + index;

		try (Statement stmt = Pool.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Error en la ejecucion del Query");
		}

	}

	public void borrarLote(Integer index) {

		String sql = "DELETE LOTEDERECHOHABIENTE WHERE OPERATIVO = (SELECT CLAVE_OPERATIVO FROM OPERATIVO WHERE ID_OPERATIVO = "
				+ index + ")";

		try (Statement stmt = Pool.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Error en la ejecucion del Query");
		}
	}

	public boolean cambiarStatusErrorFile(Integer index, String msg) {
		
		String sql = "UPDATE OPERATIVO SET ID_ESTATUS_OPERATIVO = 3 , FECHA_INICIO_PROCESAMIENTO = SYSDATE, FECHA_FIN_PROCESAMIENTO = SYSDATE, DESCRIPCION_ERROR = '"+msg+"' WHERE ID_OPERATIVO = " + index;
		
		boolean isExecute = false;

		try (Statement stmt = Pool.createStatement()) {

			stmt.executeUpdate(sql);
			Pool.commit();
			isExecute = true;

		} catch (SQLException e) {
			System.out.println("Error en la ejecucion del Query");
		}

		return isExecute;
		
	}

	public void borrarCancelados() {
		
		String sql = "DELETE LOTEDERECHOHABIENTE WHERE OPERATIVO IN (SELECT CLAVE_OPERATIVO FROM OPERATIVO WHERE ID_ESTATUS_OPERATIVO = 5)";

		try (Statement stmt = Pool.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Error en la ejecucion del Query");
		}
		
	}

	public void Crear() {
		
		String sql = "UPDATE LOTEDERECHOHABIENTE SET ACTIVO = 1 WHERE OPERATIVO IN(SELECT CLAVE_OPERATIVO FROM OPERATIVO WHERE ID_ESTATUS_OPERATIVO = 4 AND ESTATUS_ARCHIVO = 2)";

		try (Statement stmt = Pool.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Error en la ejecucion del Query");
		}
		
	}

	public void MarcarCreado() {
		
		String sql = "UPDATE OPERATIVO SET ESTATUS_ARCHIVO = 2  WHERE ID_ESTATUS_OPERATIVO = 4";

		try (Statement stmt = Pool.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Error en la ejecucion del Query");
		}
		
	}

}
