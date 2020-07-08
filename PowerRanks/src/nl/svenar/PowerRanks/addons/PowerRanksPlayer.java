package nl.svenar.PowerRanks.addons;

import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.Users;

public class PowerRanksPlayer {

	private PowerRanks powerRanks;
	private Player player;
	private Users users;
	
	public PowerRanksPlayer(PowerRanks powerRanks, Player player) {
		this.powerRanks = powerRanks;
		this.player = player;
		this.users = new Users(this.powerRanks);
	}
	
	public PowerRanks getPowerRanks() {
		return powerRanks;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	/*
	 * Get the users current rank
	 * Arguments:
	 * Returns: String (player's rank)
	 */
	public String getRank() {
		return users.getGroup(getPlayer());
	}
	
	/*
	 * Set the users rank
	 * Arguments: rankname(as registered in powerRanks)
	 * Returns: boolean (true on success & false on fail)
	 */
	public boolean setRank(String rankName) {
		return users.setGroup(getPlayer(), users.getRankIgnoreCase(rankName));
	}
}
