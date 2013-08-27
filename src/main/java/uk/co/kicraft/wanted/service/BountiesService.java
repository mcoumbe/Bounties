package uk.co.kicraft.wanted.service;

import uk.co.kicraft.wanted.domain.Bounty;

public interface BountiesService {

	public void addBounty(String player, String sponsor, int ammount);

	public void removeBounty(String player, int ammount);

	public Bounty getBounty(String player);

	public void claimBounty(String player, String killer, int deathId);

}
