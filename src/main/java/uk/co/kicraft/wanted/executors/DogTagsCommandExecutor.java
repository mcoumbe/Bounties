package uk.co.kicraft.wanted.executors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.kicraft.wanted.Wanted;

//Referenced classes of package uk.co.kicraft.wanted.dogtags:
//         DogTags, DogTagsPermissionsChecker

public class DogTagsCommandExecutor implements CommandExecutor {

	public DogTagsCommandExecutor(Wanted plugin) {
		this.plugin = plugin;

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String args[]) {
		if (cmd.getName().equalsIgnoreCase("dt")) {
			if (args.length == 0) {
				// sender.sendMessage(DogTags.getHelp());
				return true;
			}
			List errors = new ArrayList();
			if (args[0].equalsIgnoreCase("help")) {
				// sender.sendMessage(DogTags.getHelp());
				return true;
			}
			if (args[0].equalsIgnoreCase("view")) {
				checkPermissions(sender, "view", errors);
				if (!errors.isEmpty()) {
					sendErrors(sender, errors);
					return true;
				}
				validateView(args, errors);
				if (!errors.isEmpty()) {
					sendErrors(sender, errors);
					return true;
				} else {
					processView(sender, cmd, label, args, errors);
					return true;
				}
			}
		}
		return false;
	}

	private void processView(CommandSender sender, Command cmd, String label,
			String args[], List errors) {
		// sender.sendMessage((new
		// StringBuilder(String.valueOf(DogTags.LOG_NAME)))
		// .append("You have collect the follow tags:").toString());
		// sender.sendMessage(playerDao.viewDogTags((Player) sender));
	}

	private void validateView(String args[], List errors) {
		if (args.length == 1)
			return;
		if (args.length != 2) {
			errors.add("Invalid Arguments /dt view [<name>]");
			return;
		} else {
			return;
		}
	}

	private void checkPermissions(CommandSender sender, String cmd, List errors) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.equals("badd")) {
				// if (!DogTagsPermissionsChecker.hasViewPermissions(p))
				errors.add("view");
				return;
			}
		}
	}

	public void sendErrors(CommandSender sender, List errors) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			for (Iterator iterator1 = errors.iterator(); iterator1.hasNext();) {
				String error = (String) iterator1.next();
				// if (player.isOnline())
				// player.sendMessage((new StringBuilder(String
				// .valueOf(DogTags.LOG_ERROR))).append(error)
				// .toString());
			}

		} else {
			String error;
			for (Iterator iterator = errors.iterator(); iterator.hasNext(); sender
					.sendMessage((new StringBuilder("DogTags")).append(error)
							.toString()))
				error = (String) iterator.next();

		}
	}

	public void sendResults(CommandSender sender, List results) {
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

	private Wanted plugin;

}