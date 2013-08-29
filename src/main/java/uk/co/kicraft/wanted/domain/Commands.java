package uk.co.kicraft.wanted.domain;

import org.bukkit.entity.Player;

public enum Commands {
	ADD("bountyadd", "kicraft.wanted.add"), DELETE("bountydel",
			"kicraft.wanted.delete"), LIST("bountylist", "kicraft.wanted.list");

	private String command;
	private String permission;

	Commands(String command, String permission) {
		this.command = command;
		this.permission = permission;
	}

	@Override
	public String toString() {
		return this.command;
	}

	public String getPermission() {
		return this.permission;
	}

	public static Commands getCommand(String cmd) {

		if (cmd.equals("bountyadd")) {
			return ADD;
		}
		if (cmd.equals("bountydel")) {
			return DELETE;
		}
		if (cmd.equals("bountylist")) {
			return LIST;
		}

		return null;

	}

}
