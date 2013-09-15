package uk.co.kicraft.wanted.tasks;


import org.bukkit.scheduler.BukkitRunnable;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

import uk.co.kicraft.wanted.ServiceRegister;
import uk.co.kicraft.wanted.Wanted;
import uk.co.kicraft.wanted.domain.Bounty;
import uk.co.kicraft.wanted.domain.WantedPoster;
import uk.co.kicraft.wanted.service.BountiesService;
import uk.co.kicraft.wanted.service.WantedService;

public class PosterUpdater extends BukkitRunnable {

	private WantedService wantedService;
	private BountiesService bountiesService;
	private Logger log = Logger.getLogger(Wanted.LOGGER_NAME);

	public PosterUpdater() {
		wantedService = (WantedService) ServiceRegister.getService("wanted");
		bountiesService = (BountiesService) ServiceRegister
				.getService("bounties");
	}

	@Override
	public void run() {
		List<Location> locations = wantedService.getSignLocations();

		log.info("Number of Signs: " + locations.size());

		if (!locations.isEmpty()) {
			for (Location location : locations) {
				Block block = location.getWorld().getBlockAt(location);

				if (block.getType().equals(Material.WALL_SIGN)) {

					WantedPoster wp = new WantedPoster((Sign) block.getState());

					if (wp.isPoster()) {
						Bounty bounty = bountiesService.getBounty(wp
								.getRanking());
						wp.updatePoster(bounty);
					} else {
						wantedService.deleteSignLocation(location);
					}

				} else {
					wantedService.deleteSignLocation(location);
				}

			}
		}

	}

}
