package uk.co.kicraft.wanted.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uk.co.kicraft.wanted.Wanted;

public class Dao {

	protected Connection getConnection() throws SQLException {
		return Wanted.getConnection();
	}

	protected void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (conn != null) {
				conn.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (Exception ex) {

		}
	}

}
