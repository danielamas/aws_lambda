package com.amazonaws.lambda.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class PoolFunctionHandler implements RequestHandler<String, String> {

	public DataSource dataSource = DAO.getDataSource();

	@Override
	public String handleRequest(String input, Context context) {
//		context.getLogger().log("Input: " + input);
		StringBuilder resp = new StringBuilder();
		resp.append("\n===== START PROCESS =====\n");

		if(dataSource != null) {
			resp.append("\n\n===== Get Data Source and Init DB Status =====\n");
			DAO.printDbStatus(resp);

			Statement statement = null;
			ResultSet rs = null;
			Connection connection = null;
			try {
				resp.append("\n\n===== Making A New Connection Object For Db Transaction =====\n\n");
				connection = dataSource.getConnection();
				DAO.printDbStatus(resp);
				resp.append("\n\n");

				String sql = "select * from log";
				statement = connection.createStatement();

				rs = statement.executeQuery(sql);
				while (rs.next()) {
					resp.append("ID : " + rs.getString("id")+ "\n");
					resp.append("Mensagem : " + rs.getString("mensagem")+ "\n");
					resp.append("Erro : " + rs.getString("erro")+ "\n");
				}

				resp.append("\n===== Releasing Connection Object To Pool =====\n");

			} catch (SQLException e) {
				context.getLogger().log(e.getMessage());
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						context.getLogger().log(e.getMessage());
					}
				}
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						context.getLogger().log(e.getMessage());
					}
				}

				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						context.getLogger().log(e.getMessage());
					}
				}
				DAO.printDbStatus(resp);
			}
		}

		resp.append("\n===== END PROCESS =====\n");

		if(resp.length() > 0) {
			return resp.toString();
		} else {
			return "empty";
		}
	}
}