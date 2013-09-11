package uk.co.kicraft.wanted.listeners;

import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import uk.co.kicraft.wanted.Wanted;
import uk.co.kicraft.wanted.domain.Bounty;
import uk.co.kicraft.wanted.service.BountiesService;
import uk.co.kicraft.wanted.service.WantedService;

public class WantedListener implements Listener {

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
	
}
