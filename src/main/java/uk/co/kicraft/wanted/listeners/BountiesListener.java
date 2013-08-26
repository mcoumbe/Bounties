// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 25/08/2013 22:51:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BountiesListener.java

package uk.co.kicraft.wanted.listeners;

import java.util.logging.Logger;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import uk.co.kicraft.wanted.Wanted;

// Referenced classes of package uk.co.kicraft.wanted.bounties:
//            Bounties

public class BountiesListener implements Listener {

	private Logger log = Logger.getLogger("Wanted");

	public BountiesListener(Wanted plugin) {
		log = Logger.getLogger("Minecraft");
		this.plugin = plugin;
	}

	//
	// public void onPlayerLogin(PlayerJoinEvent event) {
	// Player player = event.getPlayer();
	// playerDao.saveFirstLogin(player);
	// List active = plugin.getConfig().getConfigurationSection("bounties")
	// .getStringList("active");
	// for (Iterator iterator = active.iterator(); iterator.hasNext();) {
	// String bounty = (String) iterator.next();
	// String bountyInfo[] = bounty.split("-");
	// if (player.getName().equalsIgnoreCase(bountyInfo[0])) {
	// player.sendMessage((new StringBuilder(String
	// .valueOf(Bounties.LOG_NAME)))
	// .append("You currently have a bounty worth: ")
	// .append(ChatColor.RED)
	// .append(Bounties.getCurrency(plugin))
	// .append(bountyInfo[1]).toString());
	// break;
	// }
	// }
	//
	// }
	//
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player deadPlayer = event.getEntity();

		EntityDamageEvent lastDamageCause = event.getEntity()
				.getLastDamageCause();
		playerDao.saveDeath(deadPlayer, lastDamageCause.getCause());
		if (lastDamageCause instanceof EntityDamageByEntityEvent) {
			DamageCause cause = lastDamageCause.getCause();
			Entity damager = ((EntityDamageByEntityEvent) lastDamageCause)
					.getDamager();
			if (cause.equals(DamageCause.PROJECTILE))
				try {
					damager = ((Projectile) damager).getShooter();
				} catch (Exception ex) {
					damager = ((EntityDamageByEntityEvent) lastDamageCause)
							.getDamager();
					log.info(damager.toString());
				}
			if (!(damager instanceof Player))
				return;
			Player killer = (Player) damager;
			if (killer.equals(deadPlayer)) {
				killer.sendMessage((new StringBuilder(String
						.valueOf(Bounties.LOG_NAME))).append(
						"You can't claim your own bounty, silly!").toString());
				return;
			}
			if (hasBounty(deadPlayer)) {
				handleBounty(killer, deadPlayer);
				handleStats(killer, deadPlayer);
				return;
			}
			playerDao.saveKill(killer);
			if (playerDao.getKills(killer) % 10 == 0) {
				bountiesDao.addBounty(killer.getName(), 1000);
				plugin.getServer().broadcastMessage(
						Bounties.bountyAmount(killer.getName(), plugin));
			}
		}
	}

	//
	// private void handleStats(Player killer, Player deadPlayer) {
	// playerDao.saveDogTag(killer, deadPlayer);
	// }
	//
	// private void handleBounty(Player killer, Player deadPlayer) {
	// String bounty = bountiesDao.getBounty(deadPlayer.getName());
	// int reward = (int) ((double) bountiesDao.getAmmount(bounty) *
	// 0.90000000000000002D);
	// int hunterTax = (int) ((double) bountiesDao.getAmmount(bounty) *
	// 0.10000000000000001D);
	// List results = new ArrayList();
	// bountiesDao.removeBounty(deadPlayer.getName(), results);
	// plugin.getEconomy().depositPlayer(killer.getName(), reward);
	// bountiesDao.addBounty(killer.getName(), hunterTax);
	// plugin.getServer().broadcastMessage(
	// Bounties.announceKill(killer.getName(), deadPlayer.getName(),
	// reward, plugin));
	// killer.sendMessage((new StringBuilder(String.valueOf(Bounties.LOG_NAME)))
	// .append("Your bounty has increased!").toString());
	// }
	//
	// //
	// private boolean hasBounty(Player p) {
	// List active = plugin.getConfig().getConfigurationSection("bounties")
	// .getStringList("active");
	// for (Iterator iterator = active.iterator(); iterator.hasNext();) {
	// String bounty = (String) iterator.next();
	// String bountyInfo[] = bounty.split("-");
	// if (p.getName().equalsIgnoreCase(bountyInfo[0]))
	// return true;
	// }
	//
	// return false;
	// }

	//
	private Wanted plugin;

	// private PlayerDao playerDao;
	// private BountiesDao bountiesDao;
}