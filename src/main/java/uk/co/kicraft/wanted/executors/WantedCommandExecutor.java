package uk.co.kicraft.wanted.executors;

import java.util.List;

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

		System.out.print(cmd.getName());

		Commands command = Commands.getCommand(cmd.getName());
		if (!sender.hasPermission(command.getPermission())) {
			this.sendMessage(
					sender,
					"You do not have permissions for command: "
							+ command.toString());
			return true;
		}

		String playerName = "";
		int amount = 0;

		try {
			switch (command) {
			case ADD:
				validateAddBounty(sender, args);

				amount = Integer.parseInt(args[1]);
				double fee = amount * 0.9;
				amount = (int) fee;
				playerName = args[0];

				String sponsorName = "";
				if (sender instanceof Player) {
					Player sponsor = (Player) sender;
					sponsorName = sender.getName();
					plugin.getEconomy().withdrawPlayer(sponsor.getName(),
							amount);
				} else {
					sponsorName = "Console";
				}

				bountiesService.addBounty(playerName, sponsorName, amount);
				sendMessage(sender, "Bounty Successfully Added!");
				break;

			case DELETE:
				validateDeleteBounty(sender, args);
				amount = Integer.parseInt(args[1]);
				playerName = args[0];
				bountiesService.removeBounty(playerName, amount);
				sendMessage(sender, "Bounty Successfully Removed!");
				break;

			case LIST:
				List<Bounty> bounties = bountiesService.getBounties();
				if (bounties.isEmpty()) {
					sendMessage(sender, "No active bounties!");
				} else {
					sendMessage(sender, "Bounties");

					for (Bounty bounty : bounties) {
						sendMessage(sender, bounty.toString());
					}
				}
				break;

			default:

				break;
			}
			return true;
		} catch (ValidationException ex) {
			this.sendMessage(sender, ex.getMessage());
			return true;
		}
	}

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

	public Wanted plugin;

}