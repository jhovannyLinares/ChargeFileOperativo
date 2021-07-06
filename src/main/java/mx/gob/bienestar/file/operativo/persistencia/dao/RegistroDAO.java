package mx.gob.bienestar.file.operativo.persistencia.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import mx.gob.bienestar.file.operativo.persistencia.config.Pool;
import mx.gob.bienestar.file.operativo.persistencia.entity.Registro;

public class RegistroDAO {

	Logger logger = Logger.getLogger(this.getClass());

	public boolean saveError(Integer index, int registro, String[] registros, String msg) {

		saveError(index, registro, registros[1], msg);

		if (registros.length > 24) {

			if (registros[24] != null && !registros[24].trim().contentEquals("")) {

				updateError(index, registro, registros[24]);
			}
		}

		return true;

	}

	private boolean updateError(Integer index, int registro, String tarjeta) {

		String sql = "UPDATE OPERATIVO_ERROR SET ID_TARJETA = '" + tarjeta + "' WHERE ID_OPERATIVO = " + index
				+ " AND LINEA = " + registro;

		boolean isExecute = false;
		logger.debug(sql);
		Pool.addBatch(sql);

		isExecute = true;

		return isExecute;

	}

	public boolean saveError(Integer index, int registro, String idPadron, String msg) {

		String sql = "INSERT INTO OPERATIVO_ERROR (ID_OPERATIVO, LINEA, ID_PADRON, DESCRIPCION) VALUES ( " + index
				+ ", " + registro + ", '" + idPadron + "' ,'" + msg + "')";

		boolean isExecute = false;
		logger.debug(sql);
		Pool.addBatch(sql);

		isExecute = true;

		return isExecute;

	}

	public boolean getError(Integer index) {

		String sql = "SELECT COUNT(*) FROM OPERATIVO_ERROR WHERE ID_OPERATIVO = " + index;

		boolean isError = false;

		try (Statement stmt = Pool.createStatement()) {

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if (rs.getInt(1) > 0) {
					isError = true;
				}
			}

		} catch (SQLException e) {
			logger.debug("Error en la ejecucion del Query");
		}

		return isError;

	}

	public boolean save(Registro reg) {

		if (reg.getREPOSICION().equalsIgnoreCase("1")) {

			deleteReg(reg.getID_PADRON());

		}

		StringBuffer sql = new StringBuffer("INSERT INTO LOTEDERECHOHABIENTE ( ");

		if (reg.getOPERATIVO() != null) {
			sql.append(" OPERATIVO, ");
		}
		if (reg.getID_PADRON() != null) {
			sql.append(" ID_PADRON, ");
		}
		if (reg.getID_PROGRAMA_SOCIAL() != null) {
			sql.append(" ID_PROGRAMA_SOCIAL, ");
		}
		if (reg.getTITULAR_A_PATERNO() != null) {
			sql.append(" TITULAR_A_PATERNO, ");
		}
		if (reg.getTITULAR_A_MATERNO() != null) {
			sql.append(" TITULAR_A_MATERNO, ");
		}
		if (reg.getTITULAR_NOMBRE() != null) {
			sql.append(" TITULAR_NOMBRE, ");
		}
		if (reg.getTITULAR_CURP() != null) {
			sql.append(" TITULAR_CURP, ");
		}
		if (reg.getREGISTRO_AUXILIAR() != null) {
			sql.append(" REGISTRO_AUXILIAR, ");
		}
		if (reg.getAUX_A_PATERNO() != null) {
			sql.append(" AUX_A_PATERNO, ");
		}
		if (reg.getAUX_A_MATERNO() != null) {
			sql.append(" AUX_A_MATERNO, ");
		}
		if (reg.getAUX_NOMBRE() != null) {
			sql.append("AUX_NOMBRE, ");
		}
		if (reg.getAUX_CURP() != null) {
			sql.append(" AUX_CURP, ");
		}
		if (reg.getID_LOCALIDAD_INEGI() != null) {
			sql.append(" ID_LOCALIDAD_INEGI, ");
		}
		if (reg.getID_REGION() != null) {
			sql.append("ID_REGION, ");
		}
		if (reg.getLOCALIDADID() != null) {
			sql.append(" LOCALIDADID, ");
		}
		if (reg.getCOLONIA() != null) {
			sql.append(" COLONIA, ");
		}
		if (reg.getCALLE() != null) {
			sql.append(" CALLE, ");
		}
		if (reg.getAREA() != null) {
			sql.append(" AREA, ");
		}
		if (reg.getNUMERO_EXTERNO() != null) {
			sql.append(" NUMERO_EXTERNO, ");
		}
		if (reg.getNUMERO_INTERIOR() != null) {
			sql.append(" NUMERO_INTERIOR, ");
		}
		if (reg.getMANZANA() != null) {
			sql.append(" MANZANA, ");
		}
		if (reg.getLOTE() != null) {
			sql.append(" LOTE, ");
		}
		if (reg.getCODIGO_POSTAL() != null) {
			sql.append(" CODIGO_POSTAL, ");
		}

		if (reg.getID_ACUSE() != null) {
			sql.append("ID_ACUSE, ");
		}

		if (reg.getID_ENTIDAD_FEDERATIVA() != null) {
			sql.append("ID_ENTIDAD_FEDERATIVA, ");
		}
		if (reg.getID_MUNICIPIO() != null) {
			sql.append(" ID_MUNICIPIO, ");
		}

		if (reg.getREPOSICION() != null) {
			sql.append(" REPOSICION, ");
		}

		if (reg.getTARJETAID() != null) {
			sql.append(" TARJETAID ");
		}

		sql.append(" ) VALUES ( ");

		if (reg.getOPERATIVO() != null) {
			sql.append("'");
			sql.append(reg.getOPERATIVO());
			sql.append("',");
		}
		if (reg.getID_PADRON() != null) {
			sql.append(reg.getID_PADRON());
			sql.append(",");
		}
		if (reg.getID_PROGRAMA_SOCIAL() != null) {
			sql.append(reg.getID_PROGRAMA_SOCIAL());
			sql.append(",");
		}
		if (reg.getTITULAR_A_PATERNO() != null) {
			sql.append("'");
			sql.append(reg.getTITULAR_A_PATERNO());
			sql.append("',");
		}
		if (reg.getTITULAR_A_MATERNO() != null) {
			sql.append("'");
			sql.append(reg.getTITULAR_A_MATERNO());
			sql.append("',");
		}
		if (reg.getTITULAR_NOMBRE() != null) {
			sql.append("'");
			sql.append(reg.getTITULAR_NOMBRE());
			sql.append("',");
		}
		if (reg.getTITULAR_CURP() != null) {
			sql.append("'");
			sql.append(reg.getTITULAR_CURP());
			sql.append("',");
		}
		if (reg.getREGISTRO_AUXILIAR() != null) {
			sql.append(reg.getREGISTRO_AUXILIAR());
			sql.append(",");
		}
		if (reg.getAUX_A_PATERNO() != null) {
			sql.append("'");
			sql.append(reg.getAUX_A_PATERNO());
			sql.append("',");
		}
		if (reg.getAUX_A_MATERNO() != null) {
			sql.append("'");
			sql.append(reg.getAUX_A_MATERNO());
			sql.append("',");
		}
		if (reg.getAUX_NOMBRE() != null) {
			sql.append("'");
			sql.append(reg.getAUX_NOMBRE());
			sql.append("',");
		}
		if (reg.getAUX_CURP() != null) {
			sql.append("'");
			sql.append(reg.getAUX_CURP());
			sql.append("',");
		}
		if (reg.getID_LOCALIDAD_INEGI() != null) {
			sql.append(reg.getID_LOCALIDAD_INEGI());
			sql.append(",");
		}
		if (reg.getID_REGION() != null) {
			sql.append(reg.getID_REGION());
			sql.append(",");
		}
		if (reg.getLOCALIDADID() != null) {
			sql.append(reg.getLOCALIDADID());
			sql.append(",");
		}
		if (reg.getCOLONIA() != null) {
			sql.append("'");
			sql.append(reg.getCOLONIA());
			sql.append("',");
		}
		if (reg.getCALLE() != null) {
			sql.append("'");
			sql.append(reg.getCALLE());
			sql.append("',");
		}
		if (reg.getAREA() != null) {
			sql.append(reg.getAREA());
			sql.append(",");
		}
		if (reg.getNUMERO_EXTERNO() != null) {
			sql.append("'");
			sql.append(reg.getNUMERO_EXTERNO());
			sql.append("',");
		}
		if (reg.getNUMERO_INTERIOR() != null) {
			sql.append("'");
			sql.append(reg.getNUMERO_INTERIOR());
			sql.append("',");
		}
		if (reg.getMANZANA() != null) {
			sql.append("'");
			sql.append(reg.getMANZANA());
			sql.append("',");
		}
		if (reg.getLOTE() != null) {
			sql.append("'");
			sql.append(reg.getLOTE());
			sql.append("',");
		}
		if (reg.getCODIGO_POSTAL() != null) {
			sql.append("'");
			sql.append(reg.getCODIGO_POSTAL());
			sql.append("',");
		}

		if (reg.getID_ACUSE() != null) {
			sql.append("'");
			sql.append(reg.getID_ACUSE());
			sql.append("',");
		}
		if (reg.getID_ENTIDAD_FEDERATIVA() != null) {
			sql.append("'");
			sql.append(reg.getID_ENTIDAD_FEDERATIVA());
			sql.append("',");
		}
		if (reg.getID_MUNICIPIO() != null) {
			sql.append("'");
			sql.append(reg.getID_MUNICIPIO());
			sql.append("',");
		}
		if (reg.getREPOSICION() != null) {
			sql.append(reg.getREPOSICION());
			sql.append(",");
		}

		if (reg.getTARJETAID() != null) {
			sql.append("'");
			sql.append(reg.getTARJETAID());
			sql.append("'");
		}

		sql.append(" )");

		boolean isExecute = false;

		logger.debug(sql.toString());

		Pool.addBatch(sql.toString());

		isExecute = true;

		return isExecute;

	}

	private void deleteReg(String padron) {

		StringBuffer sqlDelete = new StringBuffer("DELETE LOTEDERECHOHABIENTE WHERE ID_PADRON = " + padron);
		logger.debug(sqlDelete.toString());

		Pool.addBatch(sqlDelete.toString());

	}

	private String getInsert(Registro reg, String tabla, int numRegistro) {
		StringBuffer sql = new StringBuffer(tabla);
		if (reg.getOPERATIVO() != null)
			sql.append(" OPERATIVO, ");
		if (reg.getID_PADRON() != null)
			sql.append(" ID_PADRON, ");
		if (reg.getID_PROGRAMA_SOCIAL() != null)
			sql.append(" ID_PROGRAMA_SOCIAL, ");
		if (reg.getTITULAR_A_PATERNO() != null)
			sql.append(" TITULAR_A_PATERNO, ");
		if (reg.getTITULAR_A_MATERNO() != null)
			sql.append(" TITULAR_A_MATERNO, ");
		if (reg.getTITULAR_NOMBRE() != null)
			sql.append(" TITULAR_NOMBRE, ");
		if (reg.getTITULAR_CURP() != null)
			sql.append(" TITULAR_CURP, ");
		if (reg.getREGISTRO_AUXILIAR() != null)
			sql.append(" REGISTRO_AUXILIAR, ");
		if (reg.getAUX_A_PATERNO() != null)
			sql.append(" AUX_A_PATERNO, ");
		if (reg.getAUX_A_MATERNO() != null)
			sql.append(" AUX_A_MATERNO, ");
		if (reg.getAUX_NOMBRE() != null)
			sql.append("AUX_NOMBRE, ");
		if (reg.getAUX_CURP() != null)
			sql.append(" AUX_CURP, ");
		if (reg.getID_LOCALIDAD_INEGI() != null)
			sql.append(" ID_LOCALIDAD_INEGI, ");
		if (reg.getID_REGION() != null)
			sql.append("ID_REGION, ");
		if (reg.getLOCALIDADID() != null)
			sql.append(" LOCALIDADID, ");
		if (reg.getCOLONIA() != null)
			sql.append(" COLONIA, ");
		if (reg.getCALLE() != null)
			sql.append(" CALLE, ");
		if (reg.getAREA() != null)
			sql.append(" AREA, ");
		if (reg.getNUMERO_EXTERNO() != null)
			sql.append(" NUMERO_EXTERNO, ");
		if (reg.getNUMERO_INTERIOR() != null)
			sql.append(" NUMERO_INTERIOR, ");
		if (reg.getMANZANA() != null)
			sql.append(" MANZANA, ");
		if (reg.getLOTE() != null)
			sql.append(" LOTE, ");
		if (reg.getCODIGO_POSTAL() != null)
			sql.append(" CODIGO_POSTAL, ");
		if (reg.getID_ACUSE() != null)
			sql.append("ID_ACUSE, ");
		if (reg.getID_ENTIDAD_FEDERATIVA() != null)
			sql.append("ID_ENTIDAD_FEDERATIVA, ");
		if (reg.getID_MUNICIPIO() != null)
			sql.append(" ID_MUNICIPIO, ");
		if (reg.getREPOSICION() != null)
			sql.append(" REPOSICION, ");
		sql.append(" ID_REGISTRO, ");
		if (reg.getTARJETAID() != null)
			sql.append(" TARJETAID ");
		sql.append(" ) VALUES ( ");
		if (reg.getOPERATIVO() != null) {
			sql.append("'");
			sql.append(reg.getOPERATIVO());
			sql.append("',");
		}
		if (reg.getID_PADRON() != null) {
			sql.append(reg.getID_PADRON());
			sql.append(",");
		}
		if (reg.getID_PROGRAMA_SOCIAL() != null) {
			sql.append(reg.getID_PROGRAMA_SOCIAL());
			sql.append(",");
		}
		if (reg.getTITULAR_A_PATERNO() != null) {
			sql.append("'");
			sql.append(reg.getTITULAR_A_PATERNO());
			sql.append("',");
		}
		if (reg.getTITULAR_A_MATERNO() != null) {
			sql.append("'");
			sql.append(reg.getTITULAR_A_MATERNO());
			sql.append("',");
		}
		if (reg.getTITULAR_NOMBRE() != null) {
			sql.append("'");
			sql.append(reg.getTITULAR_NOMBRE());
			sql.append("',");
		}
		if (reg.getTITULAR_CURP() != null) {
			sql.append("'");
			sql.append(reg.getTITULAR_CURP());
			sql.append("',");
		}
		if (reg.getREGISTRO_AUXILIAR() != null) {
			sql.append(reg.getREGISTRO_AUXILIAR());
			sql.append(",");
		}
		if (reg.getAUX_A_PATERNO() != null) {
			sql.append("'");
			sql.append(reg.getAUX_A_PATERNO());
			sql.append("',");
		}
		if (reg.getAUX_A_MATERNO() != null) {
			sql.append("'");
			sql.append(reg.getAUX_A_MATERNO());
			sql.append("',");
		}
		if (reg.getAUX_NOMBRE() != null) {
			sql.append("'");
			sql.append(reg.getAUX_NOMBRE());
			sql.append("',");
		}
		if (reg.getAUX_CURP() != null) {
			sql.append("'");
			sql.append(reg.getAUX_CURP());
			sql.append("',");
		}
		if (reg.getID_LOCALIDAD_INEGI() != null) {
			sql.append(reg.getID_LOCALIDAD_INEGI());
			sql.append(",");
		}
		if (reg.getID_REGION() != null) {
			sql.append(reg.getID_REGION());
			sql.append(",");
		}
		if (reg.getLOCALIDADID() != null) {
			sql.append(reg.getLOCALIDADID());
			sql.append(",");
		}
		if (reg.getCOLONIA() != null) {
			sql.append("'");
			sql.append(reg.getCOLONIA());
			sql.append("',");
		}
		if (reg.getCALLE() != null) {
			sql.append("'");
			sql.append(reg.getCALLE());
			sql.append("',");
		}
		if (reg.getAREA() != null) {
			sql.append(reg.getAREA());
			sql.append(",");
		}
		if (reg.getNUMERO_EXTERNO() != null) {
			sql.append("'");
			sql.append(reg.getNUMERO_EXTERNO());
			sql.append("',");
		}
		if (reg.getNUMERO_INTERIOR() != null) {
			sql.append("'");
			sql.append(reg.getNUMERO_INTERIOR());
			sql.append("',");
		}
		if (reg.getMANZANA() != null) {
			sql.append("'");
			sql.append(reg.getMANZANA());
			sql.append("',");
		}
		if (reg.getLOTE() != null) {
			sql.append("'");
			sql.append(reg.getLOTE());
			sql.append("',");
		}
		if (reg.getCODIGO_POSTAL() != null) {
			sql.append("'");
			sql.append(reg.getCODIGO_POSTAL());
			sql.append("',");
		}
		if (reg.getID_ACUSE() != null) {
			sql.append("'");
			sql.append(reg.getID_ACUSE());
			sql.append("',");
		}
		if (reg.getID_ENTIDAD_FEDERATIVA() != null) {
			sql.append("'");
			sql.append(reg.getID_ENTIDAD_FEDERATIVA());
			sql.append("',");
		}
		if (reg.getID_MUNICIPIO() != null) {
			sql.append("'");
			sql.append(reg.getID_MUNICIPIO());
			sql.append("',");
		}
		if (reg.getREPOSICION() != null) {
			sql.append(reg.getREPOSICION());
			sql.append(",");
		}
		sql.append(numRegistro);
		sql.append(",");
		if (reg.getTARJETAID() != null) {
			sql.append("'");
			sql.append(reg.getTARJETAID());
			sql.append("'");
		}
		sql.append(" )");
		return sql.toString();
	}

	public boolean saveTemporal(Registro registro, int numRegistro) {
		String tabla = "INSERT INTO LOTE_DERECHOHABIENTE_TEMP ( ";
		String sql = getInsert(registro, tabla, numRegistro);
		boolean isExecute = false;
		logger.debug(sql);
		Pool.addBatch(sql);
		isExecute = true;
		return isExecute;
	}

}
