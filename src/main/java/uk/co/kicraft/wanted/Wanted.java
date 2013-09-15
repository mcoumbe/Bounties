package uk.co.kicraft.wanted;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.kicraft.wanted.domain.DatabaseConnectionString;
import uk.co.kicraft.wanted.executors.BountiesCommandExecutor;
import uk.co.kicraft.wanted.filters.DebugFilter;
import uk.co.kicraft.wanted.filters.ProductionFilter;
import uk.co.kicraft.wanted.listeners.BountiesListener;
import uk.co.kicraft.wanted.listeners.WantedListener;
import uk.co.kicraft.wanted.service.BountiesService;
import uk.co.kicraft.wanted.service.BountiesServiceImpl;
import uk.co.kicraft.wanted.service.WantedService;
<<<<<<< HEAD
=======
import uk.co.kicraft.wanted.service.WantedServiceImpl;
>>>>>>> Removed Spring
import uk.co.kicraft.wanted.tasks.PosterUpdater;

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

		try {
			setupDatabase(getConfig());
		} catch (Exception ex) {

		}

		setupPermissions();
		setupChat();
		setupServices();
		setupLogger(true);
<<<<<<< HEAD
		setupWantedSigns();
		try {
			setupDatabase(getConfig());
		} catch (ClassNotFoundException e) {
=======
>>>>>>> Removed Spring

		bountiesCommandExecutor = new BountiesCommandExecutor(this);

		getCommand(COMMAND_ADD).setExecutor(bountiesCommandExecutor);
		getCommand(COMMAND_DELETE).setExecutor(bountiesCommandExecutor);
		getCommand(COMMAND_LIST).setExecutor(bountiesCommandExecutor);

		PosterUpdater updater = new PosterUpdater();
		updater.runTaskTimer(this, 1200, 1200);

		getPluginManager().registerEvents(new BountiesListener(this), this);
		getPluginManager().registerEvents(new WantedListener(), this);

	}

	private void setupWantedSigns() {
		
			
				
	}

	private void setupLogger(boolean debug) {
		if (debug) {
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
		ServiceRegister.registerService("wanted", new WantedServiceImpl());
		ServiceRegister.registerService("bounties", new BountiesServiceImpl());
		bountiesService = (BountiesService) ServiceRegister
				.getService("bounties");
		wantedService = (WantedService) ServiceRegister.getService("wanted");
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
<<<<<<< HEAD
	
	// Service Objects
	private BountiesService bountiesService;
	private WantedService wantedService;	
	
	// Scheduled Tasks
	private PosterUpdater posterUpdater;
			
	private WantedCommandExecutor wantedCommandExecutor = null;
=======
	private BountiesService bountiesService = null;
	private WantedService wantedService = null;
	private BountiesCommandExecutor bountiesCommandExecutor = null;
>>>>>>> Removed Spring
	private static DatabaseConnectionString databaseConfig = null;

	public static final String COMMAND_ADD = "bountyadd";
	public static final String COMMAND_DELETE = "bountydel";
	public static final String COMMAND_LIST = "bountylist";

	public static final String LOGGER_NAME = "[" + ChatColor.GREEN + "Wanted"
			+ ChatColor.WHITE + "] ";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(databaseConfig.toString());
	}
}