package uk.co.kicraft.wanted.domain;

import org.bukkit.configuration.ConfigurationSection;

public class DatabaseConnectionString {

	private String host;
	private String port;
	private String user;
	private String password;
	private String database;

	public DatabaseConnectionString(ConfigurationSection config) {
		this.host = config.getString("host");
		this.port = config.getString("port");
		this.user = config.getString("user");
		this.password = config.getString("pass");
		this.database = config.getString("name");
	}

	@Override
	public String toString() {
		return "jdbc:mysql://" + host + ":" + port + "/" + database + "?user="
				+ user + "&pass=" + password;
	}

}
