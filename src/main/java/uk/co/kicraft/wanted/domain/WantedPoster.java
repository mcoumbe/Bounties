package uk.co.kicraft.wanted.domain;

import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class WantedPoster {

	private SignChangeEvent event;
	private Sign sign;

	public WantedPoster(SignChangeEvent sign) {
		this.event = sign;
	}

	public WantedPoster(Sign sign) {
		this.sign = sign;
	}

	public void updatePoster(Bounty bounty) {
		int amount = bounty.getAmount();
		String name = bounty.getPlayerName();

		if (event != null) {
			event.setLine(2, name);
			event.setLine(3, "£" + amount);
		} else {
			sign.setLine(2, name);
			sign.setLine(3, "£" + amount);
			sign.update();
		}

	}

	public boolean isPoster() {

		String lineone = "";
		String linetwo = "";

		if (event != null) {
			lineone = event.getLine(0);
			linetwo = event.getLine(1);
		} else if (sign != null) {
			lineone = sign.getLine(0);
			linetwo = sign.getLine(1);
		}

		if (lineone != null && linetwo != null) {
			if (lineone.equals("[KiWanted]")) {
				try {

					int ranking = Integer.parseInt(linetwo);

					if (ranking > 0) {
						return true;
					}

				} catch (Exception ex) {
					return false;
				}

			}
		}

		return false;

	}

	public int getRanking() {
		if (event != null) {
			return Integer.parseInt(event.getLine(1));
		} else {
			return Integer.parseInt(sign.getLine(1));
		}

	}
}
