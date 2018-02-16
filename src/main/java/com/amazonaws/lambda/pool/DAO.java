package com.amazonaws.lambda.pool;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.amazonaws.services.lambda.runtime.Context;



public class DAO {

	//	public static Connection connection = null;
	//
		private static final String ENDPOINT = "mysql.cnqdm4ugj5z5.us-east-1.rds.amazonaws.com";
	//	
	//	public static Connection getConnection() {
	//		try {
	//			Class.forName("com.mysql.jdbc.Driver");
	//			if(connection == null) {
	//				connection = DriverManager.getConnection("jdbc:mysql://"+ENDPOINT+":3306/lambda","", "");
	//			}
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//		return connection;
	//	}

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String JDBC_DB_URL = "jdbc:mysql://"+ENDPOINT+":3306/lambda";
	static final String JDBC_USER = "";
	static final String JDBC_PASS = "";

	private static GenericObjectPool gPool = null;
	private static DataSource dataSource = null;

	private static void configPool() {
		if(gPool == null) {
			// Creates an Instance of GenericObjectPool That Holds Our Pool of Connections Object!
			gPool = new GenericObjectPool();
			gPool.setMaxActive(2);
			gPool.setMaxIdle(1);
		}
	}

	public static DataSource getDataSource() {
		try {
			if(dataSource == null) {
				Class.forName(JDBC_DRIVER);
				configPool();

				// Creates a ConnectionFactory Object Which Will Be Use by the Pool to Create the Connection Object!
				ConnectionFactory cf = new DriverManagerConnectionFactory(JDBC_DB_URL, JDBC_USER, JDBC_PASS);

				// Creates a PoolableConnectionFactory That Will Wraps the Connection Object Created by the ConnectionFactory to Add Object Pooling Functionality!
				//			PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, gPool, null, null, false, true);
				new PoolableConnectionFactory(cf, gPool, null, null, false, true);
				dataSource = new PoolingDataSource(gPool);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSource;
	}

	private static GenericObjectPool getConfigPool() {
		try {
			getDataSource();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gPool;
	}

	// This Method Is Used To Print The Connection Pool Status
	public static void printDbStatus(StringBuilder resp) {
		resp.append("\n Max Connection Active " + getConfigPool().getMaxActive() + " -"
				+ "Connection Active: " + getConfigPool().getNumActive() + " - "
						+ "Connection Idle: " + getConfigPool().getNumIdle());
	}

}
