package uk.co.kicraft.wanted.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import uk.co.kicraft.wanted.Wanted;

public class WantedDao extends Dao {

	private Logger log = Logger.getLogger(Wanted.LOGGER_NAME);

	public void deleteSignLocation(Location location) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM wanted_sign_location WHERE world = ? AND x = ? AND y = ? AND z = ?");
		String sql = sb.toString();

		log.fine("Executing sql: " + sql);

		try {
			conn = getConnection();

			ps = conn.prepareStatement(sql);
			ps.setString(1, location.getWorld().getName());
			ps.setInt(2, location.getBlockX());
			ps.setInt(3, location.getBlockY());
			ps.setInt(4, location.getBlockZ());
			ps.execute();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}
	}

	public void saveSignLocation(Location location) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO wanted_sign_location(world, x, y, z)").append(
				" VALUES (?, ?, ?, ?)");
		String sql = sb.toString();

		log.fine("Executing sql: " + sql);

		try {
			conn = getConnection();

			ps = conn.prepareStatement(sql);
			ps.setString(1, location.getWorld().getName());
			ps.setInt(2, location.getBlockX());
			ps.setInt(3, location.getBlockY());
			ps.setInt(4, location.getBlockZ());
			ps.execute();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}
	}

	public List<Location> getSignLocations() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<Location> locations = new ArrayList<Location>();

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT world, x, y, z FROM wanted_sign_location");
		String sql = sb.toString();

		log.fine("Executing sql: " + sql);

		try {
			conn = getConnection();

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				String world = rs.getString("world");
				int x = rs.getInt("x");
				int y = rs.getInt("y");
				int z = rs.getInt("z");
				locations.add(new Location(Bukkit.getWorld(world), x, y, z));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}

		return locations;
	}
}
