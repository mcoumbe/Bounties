package uk.co.kicraft.wanted;

import java.io.File;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.kicraft.wanted.service.BountiesService;
import uk.co.kicraft.wanted.service.BountiesServiceImpl;
import uk.co.kicraft.wanted.service.StatsService;

public class Wanted extends JavaPlugin {

	private Logger log = Logger.getLogger("Wanted");

	public Wanted() {
		log = Logger.getLogger("Minecraft");
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

	private Economy economy = null;
	private Permission perms = null;
	private Chat chat = null;
	private BountiesService bountiesService = null;
	private StatsService statsService = null;

	public BountiesService getBountiesService() {
		return bountiesService;
	}

	public StatsService getStatsService() {
		return statsService;
	}

	public static final String COMMAND_ADD = "add";

}