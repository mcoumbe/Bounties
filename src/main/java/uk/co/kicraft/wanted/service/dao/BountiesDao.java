package uk.co.kicraft.wanted.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import uk.co.kicraft.wanted.Wanted;
import uk.co.kicraft.wanted.domain.Bounty;

public class BountiesDao {

	private Logger log = Logger.getLogger(Wanted.LOGGER_NAME);
	
	public List<Bounty> getActiveBounties() {

		List<Bounty> results = new ArrayList<Bounty>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT b.id, b.player, sum(s.amount) AS amount FROM Wanted_Bounty b ")
		.append(" INNER JOIN Wanted_Sponsor s ON b.id = s.bounties_id")
		.append(" WHERE b.claimed_date IS NULL")
		.append(" GROUP BY b.id, b.player");
		String sql = sb.toString();
		
		log.fine("Executing sql: " + sql);
		
		try {
			conn = Wanted.getConnection();
			ps = conn.prepareStatement(sql);
			
			rs = ps.executeQuery();		
			
			while(rs.next()) {
				Bounty b = new Bounty();
				b.setId(rs.getInt("id"));
				b.setAmount(rs.getInt("amount"));
				b.setPlayerName(rs.getString("player"));
				results.add(b);
			}			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}
		
		return results;
		
	}
	
	/**
	 * 
	 * @return death_id
	 */
	public int saveDeath(String playerName, String cause) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		int deathId = 0;
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO Wanted_Death (player, cause)")
		.append(" VALUES (?, ?)");
		String sql = sb.toString();
		
		log.fine("Executing sql: " + sql);
		
		try {
			conn = getConnection();
			
			ps = conn.prepareStatement(sql, 1);
			ps.setString(1, playerName);
			ps.setString(2, cause);
			ps.execute();
			
			rs = ps.getGeneratedKeys();
			
			if(rs.next()) {
				deathId = rs.getInt(1);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}
		
		return deathId;
		
	}
	
	public void updateDeath(String killer, int deathId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE Wanted_Death")
		.append(" SET killer = ?")
		.append(" WHERE id = ?");
		String sql = sb.toString();
		
		log.fine("Executing sql: " + sql);
		
		try {
			conn = getConnection();
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, killer);
			ps.setInt(2, deathId);
			ps.execute();
						
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}
		
	}
	
	public void endBounty(int bountyId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE Wanted_Bounty")
		.append(" SET claimed_date = Now()")
		.append(" WHERE id = ?");
		String sql = sb.toString();
		
		log.fine("Executing sql: " + sql);
		
		try {
			conn = getConnection();
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, bountyId);
			ps.execute();
						
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}
		
	}
	
	public void addKill(int bountyId, int deathId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO Wanted_Kill (death_id, bounty_id)")
		.append(" VALUES (?, ?)");
		String sql = sb.toString();
		
		log.fine("Executing sql: " + sql);
		
		try {
			conn = getConnection();
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, deathId);
			ps.setInt(2, bountyId);
			ps.execute();
						
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}
	}
	
	public Bounty getBounty(String playerName) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT b.id, b.player, sum(s.amount) AS amount FROM Wanted_Bounty b ")
		.append(" INNER JOIN Wanted_Sponsor s ON b.id = s.bounties_id")
		.append(" WHERE b.claimed_date IS NULL")
		.append(" AND b.player = ?")
		.append(" GROUP BY b.id, b.player");
		String sql = sb.toString();
		
		log.fine("Executing sql: " + sql);
		
		Bounty b = new Bounty();
		b.setPlayerName(playerName);
		
		try {
			conn = getConnection();
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, playerName);
			rs = ps.executeQuery();
				
			if(rs.next()) {
				b.setAmount(rs.getInt("amount"));
				b.setId(rs.getInt("id"));
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}
		
		return b;
	}
	
	public int createBounty(String playerName) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		int bountyId = 0;
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO Wanted_Bounty (player)")
		.append(" VALUES (?)");
		String sql = sb.toString();
		
		log.fine("Executing sql: " + sql);		
		
		try {
			conn = getConnection();
			
			ps = conn.prepareStatement(sql, 1);
			ps.setString(1, playerName);
			ps.execute();
			
			rs = ps.getGeneratedKeys();
			
			if(rs.next()) {
				bountyId = rs.getInt(1);
			}
							
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}

		return bountyId;
		
	}
	
	public void updateBounty(String sponsor, int amount, int bountyId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO Wanted_Sponsor (sponsor, amount, bounties_id)")
		.append(" VALUES (?, ?, ?)");
		String sql = sb.toString();
		
		log.fine("Executing sql: " + sql);		
		
		try {
			conn = getConnection();
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, sponsor);
			ps.setInt(2, amount);
			ps.setInt(3, bountyId);
			ps.execute();
							
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}

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
	
	private Connection getConnection() throws SQLException {
		return Wanted.getConnection();
	}

}