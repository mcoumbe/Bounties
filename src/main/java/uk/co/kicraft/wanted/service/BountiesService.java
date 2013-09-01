package uk.co.kicraft.wanted.service;

import java.util.List;

import uk.co.kicraft.wanted.domain.Bounty;

public interface BountiesService {

	public void addBounty(String player, String sponsor, int amount);

	public void removeBounty(String player, int amount);

	public Bounty getBounty(String player);
	
	public List<Bounty> getBounties();

	public void claimBounty(int deathId, int bountyId);
	
	public int saveDeath(String player, String cause);
	
	public void updateDeath(String killer, int deathId);

}
