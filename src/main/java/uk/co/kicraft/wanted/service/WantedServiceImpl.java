package uk.co.kicraft.wanted.service;

import java.util.List;

import org.bukkit.Location;

import uk.co.kicraft.wanted.service.dao.WantedDao;

public class WantedServiceImpl implements WantedService {

	@Override
	public void saveSignLocation(Location location) {
		wantedDao.saveSignLocation(location);
	}

	@Override
	public List<Location> getSignLocations() {
		return wantedDao.getSignLocations();
	}

	@Override
	public void deleteSignLocation(Location location) {
		wantedDao.deleteSignLocation(location);
	}

	public WantedServiceImpl() {
		wantedDao = new WantedDao();
	}

	private WantedDao wantedDao;

}
