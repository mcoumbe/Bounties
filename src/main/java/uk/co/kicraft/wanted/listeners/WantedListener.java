package uk.co.kicraft.wanted.listeners;

<<<<<<< HEAD
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import uk.co.kicraft.wanted.Wanted;
import uk.co.kicraft.wanted.domain.Bounty;
=======
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import uk.co.kicraft.wanted.ServiceRegister;
import uk.co.kicraft.wanted.domain.Bounty;
import uk.co.kicraft.wanted.domain.WantedPoster;
>>>>>>> Removed Spring
import uk.co.kicraft.wanted.service.BountiesService;
import uk.co.kicraft.wanted.service.WantedService;

public class WantedListener implements Listener {

<<<<<<< HEAD
	private Logger log = Logger.getLogger("Wanted");
	private BountiesService bountiesService;
	private WantedService wantedService;
	private Wanted plugin;

	public WantedListener(Wanted plugin) {
		log = Logger.getLogger("Minecraft");
		this.plugin = plugin;
	}	
	
	@EventHandler
	public void onevent(SignChangeEvent event) {
		
		String lineone = event.getLine(0);
		String linetwo = event.getLine(1);
		if(lineone!=null && linetwo!=null) {
			if(lineone.equals("[KiWanted]")) {
				
				try {
					
					int ranking = Integer.parseInt(linetwo);
					
					if(ranking>0) {
						Bounty bounty = bountiesService.getBounty(ranking);
						
						if(bounty.isActive()) {
							event.setLine(2, bounty.getPlayerName());
							event.setLine(3, "£"+bounty.getAmount());
						} else {
							event.setLine(2, "No Bounty");
						}
						wantedService.saveSignLocation(event.getBlock().getLocation());
					}
					
				} catch (Exception ex) {
					
				}
			}
		}
		
	}
	
=======
	@EventHandler
	public void onSignChange(SignChangeEvent event) {

		WantedPoster poster = new WantedPoster(event);

		if (poster.isPoster()) {
			Bounty bounty = bountiesService.getBounty(poster.getRanking());
			poster.updatePoster(bounty);
			wantedService.saveSignLocation(event.getBlock().getLocation());
		} else {
			wantedService.deleteSignLocation(event.getBlock().getLocation());
		}

	}

	@EventHandler
	public void onSignBreak(BlockBreakEvent event) {
		if (event.getBlock().getType().equals(Material.WALL_SIGN)) {
			wantedService.deleteSignLocation(event.getBlock().getLocation());
		}
	}

	public WantedListener() {
		wantedService = (WantedService) ServiceRegister.getService("wanted");
		bountiesService = (BountiesService) ServiceRegister
				.getService("bounties");
	}

	private WantedService wantedService;
	private BountiesService bountiesService;

>>>>>>> Removed Spring
}
