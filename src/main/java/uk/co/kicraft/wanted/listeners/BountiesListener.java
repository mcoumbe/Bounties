// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 25/08/2013 22:51:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BountiesListener.java

package uk.co.kicraft.wanted.listeners;

import java.util.logging.Logger;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import uk.co.kicraft.wanted.ServiceRegister;
import uk.co.kicraft.wanted.Wanted;
import uk.co.kicraft.wanted.domain.Bounty;
import uk.co.kicraft.wanted.service.BountiesService;

// Referenced classes of package uk.co.kicraft.wanted.bounties:
//            Bounties

public class BountiesListener implements Listener {

	private Logger log = Logger.getLogger("Wanted");
	private BountiesService bountiesService;

	public BountiesListener(Wanted plugin) {
		log = Logger.getLogger("Minecraft");
		this.plugin = plugin;
		bountiesService = (BountiesService) ServiceRegister
				.getService("bounties");
	}

	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		Bounty bounty = bountiesService.getBounty(player.getName());

		if (bounty.isActive()) {
			player.sendMessage("You have an Active Bounty of "
					+ bounty.getAmount());
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {

		Player player = event.getEntity();
		EntityDamageEvent lastDamageCause = event.getEntity()
				.getLastDamageCause();

		if (lastDamageCause == null) {
			return;
		}

		String damCause = lastDamageCause.getCause().toString();

		int deathId = bountiesService.saveDeath(player.getName(), damCause);

		if (lastDamageCause instanceof EntityDamageByEntityEvent) {
			DamageCause cause = lastDamageCause.getCause();
			Entity damager = ((EntityDamageByEntityEvent) lastDamageCause)
					.getDamager();

			if (cause.equals(DamageCause.PROJECTILE)) {
				try {
					damager = ((Projectile) damager).getShooter();
				} catch (Exception ex) {
					damager = ((EntityDamageByEntityEvent) lastDamageCause)
							.getDamager();
					log.fine(damager.toString());
				}
			}

			Player killer = null;
			if (damager instanceof Player) {
				killer = ((Player) damager);
			} else {
				return;
			}

			if (((Player) damager).equals(player)) {
				player.sendMessage("You can't claim your own bounty, silly!");
				return;
			}

			Bounty bounty = bountiesService.getBounty(player.getName());

			if (killer != null) {
				bountiesService.updateDeath(killer.getName(), deathId);

				if (bounty.isActive()) {
					bountiesService.claimBounty(deathId, bounty.getId());
					plugin.getEconomy().depositPlayer(killer.getName(),
							bounty.getAmount());
					plugin.getServer().broadcastMessage(
							Wanted.LOGGER_NAME + killer.getDisplayName()
									+ " has claimed " + player.getDisplayName()
									+ "'s Bounty!");
					killer.sendMessage("You claimed a bounty!");
				}

			}

		}

	}

	private Wanted plugin;

}