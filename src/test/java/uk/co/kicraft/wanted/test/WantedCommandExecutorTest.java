package uk.co.kicraft.wanted.test;

import org.bukkit.event.player.PlayerAnimationEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import uk.co.kicraft.wanted.Wanted;
import uk.co.kicraft.wanted.executors.WantedCommandExecutor;


public class WantedCommandExecutorTest {


	public void addBounty() {
		Wanted plugin = new Wanted();
		plugin.onEnable();
		
		WantedCommandExecutor cmd = new WantedCommandExecutor(plugin);
				
		
	}
	
}
