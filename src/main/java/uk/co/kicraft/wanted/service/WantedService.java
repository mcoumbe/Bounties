package uk.co.kicraft.wanted.service;

import java.util.List;

import org.bukkit.Location;

public interface WantedService {

	public void saveSignLocation(Location location);
	
	public List<Location> getSignLocations();
}
