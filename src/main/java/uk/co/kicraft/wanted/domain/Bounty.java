package uk.co.kicraft.wanted.domain;

public class Bounty {

	private String playerName;
	private int amount;
	
	public boolean isActive() {
		return amount>0;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public String toString() {
		return playerName + " :  �" + amount; 
	}
	
	
	
}