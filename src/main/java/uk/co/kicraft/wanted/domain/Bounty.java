package uk.co.kicraft.wanted.domain;

public class Bounty {

	private int id;
	private String playerName;
	private int amount;

	public boolean isActive() {
		return amount > 0;
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
		return playerName + " :  £" + amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
