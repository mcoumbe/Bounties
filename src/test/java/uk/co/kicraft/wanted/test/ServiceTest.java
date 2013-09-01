package uk.co.kicraft.wanted.test;

import uk.co.kicraft.wanted.Wanted;
import uk.co.kicraft.wanted.dao.BountiesDao;

public class ServiceTest {

	Wanted plugin = new Wanted();
	
	public ServiceTest() {
	
	}
	
	private BountiesDao bd = new BountiesDao();
	
	public void saveDeath() {
		bd.saveDeath("kimike", "shit");
	}
	
}
