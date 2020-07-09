package nl.svenar.PowerRanks.api;

import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.Users;

public class PowerRanksAPI {
	
	public static PowerRanks main;
	private Users s;
	
	public PowerRanksAPI() {
		this.s = new Users(PowerRanksAPI.main);
	}
	
	public String getPlayerRank(Player player) {
		String rank = s.getGroup(player);
		return rank;
	}
	
	public boolean setPlayerRank(Player player, String rank) {
		boolean success = s.setGroup(player, s.getRankIgnoreCase(rank), true);
		return success;
	}
	
	public List<String> getPermissions(String rank) {
		return s.getPermissions(s.getRankIgnoreCase(rank));
	}
	
	public boolean addPermission(String rank, String permission) {
		return s.addPermission(s.getRankIgnoreCase(rank), permission);
	}
	
	public boolean removePermission(String rank, String permission) {
		return s.removePermission(s.getRankIgnoreCase(rank), permission);
	}
	
	public List<String> getInheritances(String rank) {
		return s.getInheritances(s.getRankIgnoreCase(rank));
	}
	
	public boolean addInheritance(String rank, String inheritance) {
		return s.addInheritance(s.getRankIgnoreCase(rank), inheritance);
	}
	
	public boolean removeInheritance(String rank, String inheritance) {
		return s.removeInheritance(s.getRankIgnoreCase(rank), inheritance);
	}
	
	public Set<String> getRanks() {
		return s.getGroups();
	}
	
	public String getPrefix(String rank) {
		String value = s.getRanksConfigFieldString(s.getRankIgnoreCase(rank), "chat.prefix");
		return value;
	}
	
	public boolean setPrefix(String rank, String new_value) {
		boolean value = s.setRanksConfigFieldString(s.getRankIgnoreCase(rank), "chat.prefix", new_value);
		return value;
	}
	
	public String getSuffix(String rank) {
		String value = s.getRanksConfigFieldString(s.getRankIgnoreCase(rank), "chat.suffix");
		return value;
	}
	
	public boolean setSuffix(String rank, String new_value) {
		boolean value = s.setRanksConfigFieldString(s.getRankIgnoreCase(rank), "chat.suffix", new_value);
		return value;
	}
	
	public String getNameColor(String rank) {
		String value = s.getRanksConfigFieldString(s.getRankIgnoreCase(rank), "chat.nameColor");
		return value;
	}
	
	public boolean setNameColor(String rank, String new_value) {
		boolean value = s.setRanksConfigFieldString(s.getRankIgnoreCase(rank), "chat.nameColor", new_value);
		return value;
	}
	
	public String getChatColor(String rank) {
		String value = s.getRanksConfigFieldString(s.getRankIgnoreCase(rank), "chat.chatcolor");
		return value;
	}
	
	public boolean setChatColor(String rank, String new_value) {
		boolean value = s.setRanksConfigFieldString(s.getRankIgnoreCase(rank), "chat.chatcolor", new_value);
		return value;
	}
	
	public boolean isBuildingEnabled(String rank) {
		boolean value = s.getRanksConfigFieldBoolean(s.getRankIgnoreCase(rank), "build");
		return value;
	}
	
	public boolean setBuildingEnabled(String rank, boolean new_value) {
		boolean value = s.setRanksConfigFieldBoolean(s.getRankIgnoreCase(rank), "build", new_value);
		return value;
	}
	
	public String getPromoteRank(String rank) {
		String value = s.getRanksConfigFieldString(s.getRankIgnoreCase(rank), "level.promote");
		return value;
	}
	
	public boolean setPromoteRank(String rank, String new_value) {
		boolean value = s.setRanksConfigFieldString(s.getRankIgnoreCase(rank), "level.promote", new_value);
		return value;
	}
	
	public String getDemoteRank(String rank) {
		String value = s.getRanksConfigFieldString(s.getRankIgnoreCase(rank), "level.demote");
		return value;
	}
	
	public boolean setDemoteRank(String rank, String new_value) {
		boolean value = s.setRanksConfigFieldString(s.getRankIgnoreCase(rank), "level.demote", new_value);
		return value;
	}
	
	public boolean createRank(String rank) {
		boolean value = s.createRank(rank);
		return value;
	}
	
	public boolean deleteRank(String rank) {
		boolean value = s.deleteRank(rank);
		return value;
	}
	
	public boolean addBuyableRank(String rank, String buyable_rank) {
		boolean value = s.addBuyableRank(rank, buyable_rank);
		return value;
	}
	
	public boolean delBuyableRank(String rank, String buyable_rank) {
		boolean value = s.delBuyableRank(rank, buyable_rank);
		return value;
	}
	
	public boolean setRankBuyCost(String rank, int cost) {
		return s.setBuyCost(rank, String.valueOf(cost));
	}
}