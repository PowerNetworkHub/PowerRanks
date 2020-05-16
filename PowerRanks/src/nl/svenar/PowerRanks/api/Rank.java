package nl.svenar.PowerRanks.api;

import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.Main;
import nl.svenar.PowerRanks.Data.Users;

public class Rank {
	
	public static Main main;
	private Users s;
	
	public Rank() {
		this.s = new Users(Rank.main);
	}
	
	public String getPlayerRank(Player player) {
		String rank = s.getGroup(player);
		return rank;
	}
	
	public boolean setPlayerRank(Player player, String rank) {
		boolean success = s.setGroup(player, rank);
		return success;
	}
	
	public String getPrefix(Player player) {
		String prefix = s.getPrefix(player);
		return prefix;
	}
}