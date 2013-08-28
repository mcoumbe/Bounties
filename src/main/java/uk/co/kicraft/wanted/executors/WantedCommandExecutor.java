package uk.co.kicraft.wanted.executors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.kicraft.wanted.Wanted;
import uk.co.kicraft.wanted.domain.Bounty;
import uk.co.kicraft.wanted.domain.Commands;
import uk.co.kicraft.wanted.exceptions.ValidationException;
import uk.co.kicraft.wanted.service.BountiesService;

public class WantedCommandExecutor implements CommandExecutor {

	private BountiesService bountiesService;
	
	public WantedCommandExecutor(Wanted plugin) {
		this.plugin = plugin;
		this.bountiesService = plugin.getBountiesService();
	}

	public void sendMessage(CommandSender sender, String msg) {
		if (sender instanceof Player) {
			if (((Player) sender).isOnline()) {
				sender.sendMessage(msg);
			}
		} else {
			sender.sendMessage(msg);
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		Commands command = Commands.valueOf(cmd.getName());
		if (!sender.hasPermission(command.getPermission())) {
			this.sendMessage(
					sender,
					"You do not have permissions for command: "
							+ command.toString());
			return true;
		}

		try {
			switch (command) {
			case ADD:
				validateAddBounty(sender, args);
				//bountiesService.addBounty(player, sponsor, amount);
				break;
			case DELETE:

				//bountiesService.removeBounty(player, ammount);
				break;
			case LIST:

				bountiesService.getBounties();
				break;

			default:

				break;
			}
		} catch (ValidationException ex) {
			this.sendMessage(sender, ex.getMessage());
		}

		return false;
	}

	// public boolean onCommand(CommandSender sender, Command cmd, String label,
	// String args[]) {
	// PluginManager pm = plugin.getPluginManager();
	// ArrayList errors = new ArrayList();
	// if (cmd.getName().equalsIgnoreCase("wanted")) {
	// sender.sendMessage("Help");
	// return true;
	// }
	// if (cmd.getName().equalsIgnoreCase("wadd")) {
	// checkPermissions(sender, "bounty", errors);
	// if (!errors.isEmpty()) {
	// sendErrors(sender, errors);
	// return true;
	// }
	// validateAddBounty(sender, args, errors);
	// if (!errors.isEmpty()) {
	// sendErrors(sender, errors);
	// return true;
	// }
	// addBounty(sender, cmd, label, args, errors);
	// if (!errors.isEmpty()) {
	// sendErrors(sender, errors);
	// return true;
	// } else {
	// sender.sendMessage("Bountey Added");
	// sender.sendMessage("Bounty ammount");
	// plugin.getServer().broadcastMessage("Broadcast bounty to everyone");
	// return true;
	// }
	// }
	// if (cmd.getName().equalsIgnoreCase("wdel")) {
	// checkPermissions(sender, "bdel", errors);
	// if (!errors.isEmpty()) {
	// sendErrors(sender, errors);
	// return true;
	// }
	// validateDelBounty(args, errors);
	// if (!errors.isEmpty()) {
	// sendErrors(sender, errors);
	// return true;
	// }
	// deleteBounty(sender, cmd, label, args, errors);
	// if (!errors.isEmpty()) {
	// sendErrors(sender, errors);
	// return true;
	// } else {
	// return true;
	// }
	// }
	// if (cmd.getName().equalsIgnoreCase("wlist")) {
	// checkPermissions(sender, "blist", errors);
	// if (!errors.isEmpty()) {
	// sendErrors(sender, errors);
	// return true;
	// }
	// listBounty(sender, cmd, label, args, errors);
	// if (!errors.isEmpty()) {
	// sendErrors(sender, errors);
	// return true;
	// } else {
	// return true;
	// }
	// } else {
	// return false;
	// }
	// }
	//
	//
	//
	// private void checkPermissions(CommandSender sender, String cmd,
	// ArrayList errors) {
	// if (sender instanceof Player) {
	// Player p = (Player) sender;
	// if (cmd.equals("badd")) {
	// if (!BountiesPermissionsChecker.hasAddPermissions(p))
	// errors.add(Bounties.INVALID_PERMISSIONS);
	// return;
	// }
	// if (cmd.equals("bdel")) {
	// if (!BountiesPermissionsChecker.hasDelPermissions(p))
	// errors.add(Bounties.INVALID_PERMISSIONS);
	// return;
	// }
	// if (cmd.equals("blist")) {
	// if (!BountiesPermissionsChecker.hasListPermissions(p))
	// errors.add(Bounties.INVALID_PERMISSIONS);
	// return;
	// }
	// }
	// }
	//
	public void validateAddBounty(CommandSender sender, String args[])
			throws ValidationException {
		if (args.length != 2) {
			throw new ValidationException(
					"Invalid arguments: /badd <name> <ammount>");
		}

		int ammount = 0;

		try {
			ammount = Integer.parseInt(args[1]);
		} catch (Exception ex) {
			throw new ValidationException(
					"Invalid ammount specified. Must be greater than 100.");
		}

		if (ammount < 100) {
			throw new ValidationException("Minimum Ammount is 100.");
		}
		if (sender instanceof Player) {
			Double balance = Double.valueOf(plugin.getEconomy().getBalance(
					sender.getName()));
			if (balance.doubleValue() < (double) Integer.parseInt(args[1])) {
				throw new ValidationException("You don't have enough money.");
			}
		}
		Player p = Bukkit.getServer().getPlayer(args[0]);
		if (p == null) {
			throw new ValidationException("Invalid player name: " + args[0]);
		}
	}

	public void validateDeleteBounty(CommandSender sender, String args[])
			throws ValidationException {
		if (args.length < 1 || args.length > 2)
			throw new ValidationException(
					"Invalid arguments: /bdel <name> [<ammount>]");
		if (args.length == 2 && Integer.parseInt(args[1]) == 0)
			throw new ValidationException("Invalid ammount specified");
	}

	//
	// public synchronized void addBounty(CommandSender sender, Command cmd,
	// String label, String args[], List errors) {
	// boolean _tmp = sender instanceof Player;
	// Player sponsor = (Player) sender;
	// String playerName = args[0];
	// int ammount = Integer.parseInt(args[1]);
	// ammount = (int) ((double) ammount * 0.94999999999999996D);
	// bountiesDao.addBounty(playerName, ammount);
	// plugin.getEconomy().withdrawPlayer(sponsor.getName(), ammount);
	// }
	//
	// private void listBounty(CommandSender sender, Command cmd, String label,
	// String args[], List errors) {
	// sender.sendMessage(bountiesDao.listBounties());
	// }
	//
	// public void validateDelBounty(String args[], List errors) {
	// if (args.length < 1 || args.length > 2)
	// errors.add("Invalid arguments: /bdel <name> [<ammount>]");
	// if (args.length == 2 && Integer.parseInt(args[1]) == 0)
	// errors.add("Invalid ammount specified");
	// }
	//
	// private synchronized void deleteBounty(CommandSender sender, Command cmd,
	// String label, String args[], List errors) {
	// String playerName = args[0];
	// List results = new ArrayList();
	// int ammount = args.length != 1 ? Integer.parseInt(args[1]) : 0;
	// bountiesDao.removeBounty(playerName, ammount, results);
	// sendResults(sender, results);
	// }
	//
	//
	// } else {
	// String error;
	// for (Iterator iterator = errors.iterator(); iterator.hasNext(); sender
	// .sendMessage((new StringBuilder("Bounties")).append(error)
	// .toString()))
	// error = (String) iterator.next();
	//
	// }
	// }
	//
	// private void sendResults(CommandSender sender, List results) {
	// if (sender instanceof Player) {
	// Player player = (Player) sender;
	// for (Iterator iterator1 = results.iterator(); iterator1.hasNext();) {
	// String result = (String) iterator1.next();
	// if (player.isOnline())
	// player.sendMessage(result);
	// }
	//
	// } else {
	// String result;
	// for (Iterator iterator = results.iterator(); iterator.hasNext(); sender
	// .sendMessage(result))
	// result = (String) iterator.next();
	//
	// }
	// }

	public Wanted plugin;

}