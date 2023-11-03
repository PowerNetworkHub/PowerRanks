package nl.svenar.powerranks.bukkit.addons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;

public class PowerRanksPlayer {

    private PowerRanks powerRanks;
    
    private Player player;

    private String name;

    public PowerRanksPlayer(PowerRanks powerRanks, Player player) {
        this.powerRanks = powerRanks;
        this.player = player;
        if (player != null)
            name = player.getName();
    }

    public PowerRanksPlayer(PowerRanks powerRanks, String name) {
        this.powerRanks = powerRanks;
        this.name = name;
    }

    public PowerRanks getPowerRanks() {
        return powerRanks;
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    // Get the users current rank
    // Arguments: Returns:
    // String (player's rank)
    public String getRank() {
        List<PRRank> playerRanks = new ArrayList<PRRank>();

		for (PRPlayerRank playerRank : PRCache.getPlayer(getPlayer().getUniqueId().toString()).getRanks()) {
			PRRank rank = CacheManager.getRank(playerRank.getName());
			if (rank != null) {
				playerRanks.add(rank);
			}
		}

		PRUtil.sortRanksByWeight(playerRanks);
		PRUtil.reverseRanks(playerRanks);
        
        return playerRanks.size() > 0 ? playerRanks.get(0).getName() : null;
    }
    
//    // Set the users rank
//    // Arguments: rankname(as registered in powerRanks)
//    // Returns: boolean (true on success & false on fail)
//    public boolean setRank(String rankName) {
//        return users.setGroup(getPlayer(), users.getRankIgnoreCase(rankName));
//    }
//
//    // Get the prefix of the players rank
//    // Arguments:
//    // Returns: String (the current prefix that the player has)
//    public String getPrefix() {
//        return users.getPrefix(getPlayer());
//    }
//
//    // Get the prefix of the players rank
//    // Arguments: rankname(as registered in powerRanks)
//    // Returns: String (the current prefix that the player has)
//    public String getPrefix(String rankName) {
//        return users.getPrefix(users.getRankIgnoreCase(rankName));
//    }
//
//    // Set the prefix of the players rank
//    // Arguments: newPreifx(the new prefix for the player's current rank)
//    // Returns: boolean (true on success & false on fail)
//    public boolean setPrefix(String newPrefix) {
//        return users.setPrefix(getRank(), newPrefix);
//    }
//
//    // Set the prefix of the players rank
//    // Arguments: rankname(as registered in powerRanks), newPreifx(the new prefix for the player's current rank)
//    // Returns: boolean (true on success & false on fail)
//    public boolean setPrefix(String rankName, String newPrefix) {
//        return users.setPrefix(users.getRankIgnoreCase(rankName), newPrefix);
//    }
}
