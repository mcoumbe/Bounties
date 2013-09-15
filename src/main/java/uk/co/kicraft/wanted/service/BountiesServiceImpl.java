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
	public void addBounty(String player, String sponsor, int amount) {
		Bounty b = bountiesDao.getBounty(player);
		if (b.isActive()) {
			bountiesDao.updateBounty(sponsor, amount, b.getId());
		} else {
			int bountyId = bountiesDao.createBounty(player);
			bountiesDao.updateBounty(sponsor, amount, bountyId);
		}
	}

	@Override
	public void removeBounty(String player, int amount) {

	}

	@Override
	public Bounty getBounty(String player) {
		return bountiesDao.getBounty(player);
	}

	@Override
	public List<Bounty> getBounties() {
		return bountiesDao.getActiveBounties();
	}

	@Override
	public void claimBounty(int deathId, int bountyId) {
		bountiesDao.endBounty(bountyId);
		bountiesDao.addKill(bountyId, deathId);
	}

	@Override
	public int saveDeath(String player, String cause) {
		return bountiesDao.saveDeath(player, cause);
	}

	@Override
	public void updateDeath(String killer, int deathId) {
		bountiesDao.updateDeath(killer, deathId);

	}

	@Override
	public Bounty getBounty(int ranking) {
		return bountiesDao.getBounty(ranking);
	}

}