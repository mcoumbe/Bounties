package uk.co.kicraft.wanted;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.kicraft.wanted.domain.DatabaseConnectionString;
import uk.co.kicraft.wanted.executors.WantedCommandExecutor;
import uk.co.kicraft.wanted.filters.DebugFilter;
import uk.co.kicraft.wanted.filters.ProductionFilter;
import uk.co.kicraft.wanted.listeners.BountiesListener;
import uk.co.kicraft.wanted.service.BountiesService;
import uk.co.kicraft.wanted.service.BountiesServiceImpl;

public class Wanted extends JavaPlugin {

	private Logger log = Logger.getLogger(LOGGER_NAME);

	public Wanted() {
		log.setLevel(Level.INFO);
		// economy = null;
	}

	public void onDisable() {
		// log.info(msg)
	}

	public void onEnable() {
		PluginManager pm = getPluginManager();

		if (!(new File(getDataFolder(), "config.yml")).exists()) {
			getConfig().options().copyDefaults(true);
			saveConfig();
		}

		if (!setupEconomy()) {
			log.severe(String.format(
					"[%s] - Disabled due to no Vault dependency found!",
					getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		setupPermissions();
		setupChat();
		setupServices();
		setupLogger(true);
		try {
			setupDatabase(getConfig());
		} catch (ClassNotFoundException e) {

		}

		wantedCommandExecutor = new WantedCommandExecutor(this);

		getCommand(COMMAND_ADD).setExecutor(wantedCommandExecutor);
		getCommand(COMMAND_DELETE).setExecutor(wantedCommandExecutor);
		getCommand(COMMAND_LIST).setExecutor(wantedCommandExecutor);
		
		getPluginManager().registerEvents(new BountiesListener(this), this);
	}

	private void setupLogger(boolean debug) {
		if(debug) {
			log.setFilter(new DebugFilter());
		} else {
			log.setFilter(new ProductionFilter());
		}
	}

	private void setupDatabase(FileConfiguration config)
			throws ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		databaseConfig = new DatabaseConnectionString(
				config.getConfigurationSection("database"));

	}

	private void setupServices() {
		bountiesService = new BountiesServiceImpl();
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer()
				.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		economy = rsp.getProvider();
		return economy != null;
	}

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager()
				.getRegistration(Chat.class);
		chat = rsp.getProvider();
		return chat != null;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer()
				.getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

	public Economy getEconomy() {
		return economy;
	}

	public PluginManager getPluginManager() {
		return getServer().getPluginManager();
	}

	public BountiesService getBountiesService() {
		return bountiesService;
	}

	private Economy economy = null;
	private Permission perms = null;
	private Chat chat = null;
	private BountiesService bountiesService = null;
	private WantedCommandExecutor wantedCommandExecutor = null;
	private static DatabaseConnectionString databaseConfig = null;

	public static final String COMMAND_ADD = "bountyadd";
	public static final String COMMAND_DELETE = "bountydel";
	public static final String COMMAND_LIST = "bountylist";
	
	public static final String LOGGER_NAME = "Wanted";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(databaseConfig.toString());
	}
}