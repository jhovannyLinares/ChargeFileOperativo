package mx.gob.bienestar.file.operativo.negocio.servicio.impl;

import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import mx.gob.bienestar.file.operativo.persistencia.config.Pool;

public class ThreadService extends Thread {

	Logger logger = Logger.getLogger(this.getClass());

	private Statement statementBach = null;

	private Integer SIZE_THREAD_COUNT = Integer.valueOf(0);

	public ThreadService(Statement statementBach, Integer SIZE_THREAD_COUNT) {
		this.statementBach = statementBach;
		this.SIZE_THREAD_COUNT = SIZE_THREAD_COUNT;
	}

	public void run() {
		try {
			this.statementBach.executeBatch();
			Pool.commit(this.SIZE_THREAD_COUNT);
			logger.debug("Fin Hilo: " + this.SIZE_THREAD_COUNT);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
