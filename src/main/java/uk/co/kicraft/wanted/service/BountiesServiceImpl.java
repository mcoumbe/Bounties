package uk.co.kicraft.wanted.service;

import java.util.List;

import uk.co.kicraft.wanted.domain.Bounty;
import uk.co.kicraft.wanted.service.dao.BountiesDao;

public class BountiesServiceImpl implements BountiesService {

	BountiesDao bountiesDao = null;
	
	public BountiesServiceImpl() {
		this.bountiesDao = new BountiesDao();
	}
	
	
	@Override
	public void addBounty(String player, String sponsor, int ammount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeBounty(String player, int ammount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Bounty getBounty(String player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void claimBounty(String player, String killer, int deathId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Bounty> getBounties() {
		// TODO Auto-generated method stub
		return null;
	}

}