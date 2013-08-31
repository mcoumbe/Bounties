package uk.co.kicraft.wanted.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uk.co.kicraft.wanted.Wanted;
import uk.co.kicraft.wanted.domain.Bounty;

public class BountiesDao {

	public List<Bounty> getBountys() {

		List<Bounty> results = new ArrayList<Bounty>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT id, player, sum(amount) AS amount FROM Bounty b ")
		.append(" INNER JOIN Sponsor s ON b.id = s.bounty_id")
		.append(" WHERE claimed_date IS NULL")
		.append(" GROUP BY id, player");
		String sql = sb.toString();
		
		try {
			conn = Wanted.getConnection();
			ps = conn.prepareStatement(sql);
			
			rs = ps.executeQuery();		
			
			if(rs.next()) {
				Bounty b = new Bounty();
				b.setId(rs.getInt("id"));
				b.setAmount(rs.getInt("amount"));
				b.setPlayerName(rs.getString("player"));
				results.add(b);
			}			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}
		
		return results;
		
	}

	private void close(Connection conn, PreparedStatement ps, ResultSet rs) {
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

/**
 * SELECT id, player, sum(amount) AS amount FROM Bounty b INNER JOIN Sponsor s
 * ON b.id = s.bounty_id WHERE claimed_date IS NULL GROUP BY id, player
 * */
