package uk.co.kicraft.wanted;

import java.io.File;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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

	}

	// private void registerBountyCommands() {
	// getCommand("bounty").setExecutor(bountiesCmdExe);
	// getCommand("badd").setExecutor(bountiesCmdExe);
	// getCommand("bdel").setExecutor(bountiesCmdExe);
	// getCommand("blist").setExecutor(bountiesCmdExe);
	// }
	//
	// private void registerDogTagCommands() {
	// getCommand("dt").setExecutor(dogTagCmdExe);
	// }
	//
	// private boolean setupEconomy() {
	// RegisteredServiceProvider economyProvider = getServer()
	// .getServicesManager().getRegistration(
	// net / milkbowl / vault / economy / Economy);
	// if (economyProvider != null)
	// economy = (Economy) economyProvider.getProvider();
	// return economy != null;
	// }
	//
	public Economy getEconomy() {
		return economy;
	}

	public PluginManager getPluginManager() {
		return getServer().getPluginManager();
	}

	private Economy economy;

	public static final String COMMAND_ADD = "add";

}