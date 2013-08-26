package uk.co.kicraft.wanted.service;

public interface BountiesService {

	public void addBounty(String player, String sponsor, int ammount);

	public void removeBounty(String player, int ammount);

	public void getBounty(String player);

	public void claimBounty(String player, String killer);

}
