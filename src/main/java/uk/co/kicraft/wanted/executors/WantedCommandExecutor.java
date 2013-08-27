// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 25/08/2013 22:51:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BountiesCommandExecutor.java

package uk.co.kicraft.wanted.executors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import uk.co.kicraft.wanted.Wanted;

// Referenced classes of package uk.co.kicraft.wanted.bounties:
//            Bounties, BountiesPermissionsChecker

public class WantedCommandExecutor implements CommandExecutor {

	public WantedCommandExecutor(Wanted plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String args[]) {
		PluginManager pm = plugin.getPluginManager();
		ArrayList errors = new ArrayList();
		if (cmd.getName().equalsIgnoreCase("wanted")) {
			sender.sendMessage("Help");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("wadd")) {
			checkPermissions(sender, "bounty", errors);
			if (!errors.isEmpty()) {
				sendErrors(sender, errors);
				return true;
			}
			validateAddBounty(sender, args, errors);
			if (!errors.isEmpty()) {
				sendErrors(sender, errors);
				return true;
			}
			addBounty(sender, cmd, label, args, errors);
			if (!errors.isEmpty()) {
				sendErrors(sender, errors);
				return true;
			} else {
				sender.sendMessage("Bountey Added");
				sender.sendMessage("Bounty ammount");
				plugin.getServer().broadcastMessage("Broadcast bounty to everyone");
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("wdel")) {
			checkPermissions(sender, "bdel", errors);
			if (!errors.isEmpty()) {
				sendErrors(sender, errors);
				return true;
			}
			validateDelBounty(args, errors);
			if (!errors.isEmpty()) {
				sendErrors(sender, errors);
				return true;
			}
			deleteBounty(sender, cmd, label, args, errors);
			if (!errors.isEmpty()) {
				sendErrors(sender, errors);
				return true;
			} else {
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("wlist")) {
			checkPermissions(sender, "blist", errors);
			if (!errors.isEmpty()) {
				sendErrors(sender, errors);
				return true;
			}
			listBounty(sender, cmd, label, args, errors);
			if (!errors.isEmpty()) {
				sendErrors(sender, errors);
				return true;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private void checkPermissions(CommandSender sender, String cmd,
			ArrayList errors) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.equals("badd")) {
				if (!BountiesPermissionsChecker.hasAddPermissions(p))
					errors.add(Bounties.INVALID_PERMISSIONS);
				return;
			}
			if (cmd.equals("bdel")) {
				if (!BountiesPermissionsChecker.hasDelPermissions(p))
					errors.add(Bounties.INVALID_PERMISSIONS);
				return;
			}
			if (cmd.equals("blist")) {
				if (!BountiesPermissionsChecker.hasListPermissions(p))
					errors.add(Bounties.INVALID_PERMISSIONS);
				return;
			}
		}
	}

	public void validateAddBounty(CommandSender sender, String args[],
			List errors) {
		if (args.length != 2) {
			errors.add("Invalid arguments: /badd <name> <ammount>");
			return;
		}
		int ammount = 0;
		try {
			ammount = Integer.parseInt(args[1]);
		} catch (Exception ex) {
			errors.add("Invalid ammount specified. Must be greater than 100");
			return;
		}
		if (ammount < 100) {
			errors.add("Minimum Ammount is 100");
			return;
		}
		if (sender instanceof Player) {
			Double balance = Double.valueOf(plugin.getEconomy().getBalance(
					sender.getName()));
			if (balance.doubleValue() < (double) Integer.parseInt(args[1])) {
				errors.add("You don't have enough money");
				return;
			}
		}
		Player p = Bukkit.getServer().getPlayer(args[0]);
		if (p == null) {
			errors.add((new StringBuilder("Invalid player name: ")).append(
					args[0]).toString());
			return;
		} else {
			args[0] = p.getName();
			return;
		}
	}

	public synchronized void addBounty(CommandSender sender, Command cmd,
			String label, String args[], List errors) {
		boolean _tmp = sender instanceof Player;
		Player sponsor = (Player) sender;
		String playerName = args[0];
		int ammount = Integer.parseInt(args[1]);
		ammount = (int) ((double) ammount * 0.94999999999999996D);
		bountiesDao.addBounty(playerName, ammount);
		plugin.getEconomy().withdrawPlayer(sponsor.getName(), ammount);
	}

	private void listBounty(CommandSender sender, Command cmd, String label,
			String args[], List errors) {
		sender.sendMessage(bountiesDao.listBounties());
	}

	public void validateDelBounty(String args[], List errors) {
		if (args.length < 1 || args.length > 2)
			errors.add("Invalid arguments: /bdel <name> [<ammount>]");
		if (args.length == 2 && Integer.parseInt(args[1]) == 0)
			errors.add("Invalid ammount specified");
	}

	private synchronized void deleteBounty(CommandSender sender, Command cmd,
			String label, String args[], List errors) {
		String playerName = args[0];
		List results = new ArrayList();
		int ammount = args.length != 1 ? Integer.parseInt(args[1]) : 0;
		bountiesDao.removeBounty(playerName, ammount, results);
		sendResults(sender, results);
	}

	private void sendErrors(CommandSender sender, ArrayList errors) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			for (Iterator iterator1 = errors.iterator(); iterator1.hasNext();) {
				String error = (String) iterator1.next();
				if (player.isOnline())
					player.sendMessage((new StringBuilder(String
							.valueOf(Bounties.LOG_ERROR))).append(error)
							.toString());
			}

		} else {
			String error;
			for (Iterator iterator = errors.iterator(); iterator.hasNext(); sender
					.sendMessage((new StringBuilder("Bounties")).append(error)
							.toString()))
				error = (String) iterator.next();

		}
	}

	private void sendResults(CommandSender sender, List results) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			for (Iterator iterator1 = results.iterator(); iterator1.hasNext();) {
				String result = (String) iterator1.next();
				if (player.isOnline())
					player.sendMessage(result);
			}

		} else {
			String result;
			for (Iterator iterator = results.iterator(); iterator.hasNext(); sender
					.sendMessage(result))
				result = (String) iterator.next();

		}
	}

	//
	public Wanted plugin;
	public IBountiesDao bountiesDao;
}