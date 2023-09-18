package nl.svenar.powerranks.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import nl.svenar.common.structure.PRPermission;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRPlayerRank;
import nl.svenar.common.structure.PRRank;
import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.cache.CacheManager;
import nl.svenar.powerranks.data.Users;

public class PowerRanksAPI {

	private String API_VERSION = "1.4";

	public static PowerRanks plugin;
	private Users users;

	/**
	 * Initialize the API
	 * 
	 * @param plugin
	 */
	public PowerRanksAPI(PowerRanks plugin) {
		PowerRanksAPI.plugin = plugin;
		this.users = new Users(plugin);
	}

	/**
	 * Initialize the API
	 */
	public PowerRanksAPI() {
		this.users = new Users(plugin);
	}

	/**
	 * Get the plugin's name
	 * 
	 * @return PowerRanks
	 */
	public String getName() {
		return plugin.getName();
	}

	/**
	 * Get the PowerRanks version
	 * 
	 * @return string X.X.X
	 */
	public String getVersion() {
		return PowerRanks.getVersion();
	}

	/**
	 * Get the api version
	 * 
	 * @return String X.X
	 */
	public String getApiVersion() {
		return API_VERSION;
	}

	/**
	 * Get the rankname of a player
	 * 
	 * @param player
	 * @return player's rankname
	 */
	public String getPrimaryRank(Player player) {
		String rank = users.getPrimaryRank(player);
		return rank;
	}

	public List<String> getRanks(Player player) {
		List<String> ranks = new ArrayList<>();
		for (PRPlayerRank rank : CacheManager.getPlayer(player.getUniqueId().toString()).getRanks()) {
			ranks.add(rank.getName());
		}
		return ranks;
	}

	/**
	 * Overwrite all ranks of a player and set it to a single one
	 * 
	 * @param player
	 * @param rankname
	 * @return true if success, false otherwise
	 */
	public boolean setPlayerRank(Player player, String rankname) {
		return setPlayerRank(player, rankname, null);
	}

	/**
	 * Overwrite all ranks of a player and set it to a single one
	 * 
	 * @param player
	 * @param rankname
	 * @param tags
	 * @return true if success, false otherwise
	 */
	public boolean setPlayerRank(Player player, String rankname, Map<String, Object> tags) {
		PRRank rank = CacheManager.getRank(rankname);
		if (rank != null) {
			PRPlayerRank playerRank = new PRPlayerRank(rank.getName());
			if (tags != null) {
				for (String tag : tags.keySet()) {
					playerRank.addTag(tag, tags.get(tag));
				}
			}
			CacheManager.getPlayer(player.getUniqueId().toString()).setRank(playerRank);
		}
		return rank != null;
	}

	/**
	 * Add a rankname to the player's current ranks
	 * 
	 * @param player
	 * @param rankname
	 * @return true if success, false otherwise
	 */
	public boolean addPlayerRank(Player player, String rankname) {
		return addPlayerRank(player, rankname, null);
	}

	/**
	 * Add a rankname to the player's current ranks
	 * 
	 * @param player
	 * @param rankname
	 * @param tags
	 * @return true if success, false otherwise
	 */
	public boolean addPlayerRank(Player player, String rankname, Map<String, Object> tags) {
		PRRank rank = CacheManager.getRank(rankname);
		if (rank != null) {
			PRPlayerRank playerRank = new PRPlayerRank(rank.getName());
			if (tags != null) {
				for (String tag : tags.keySet()) {
					playerRank.addTag(tag, tags.get(tag));
				}
			}
			CacheManager.getPlayer(player.getUniqueId().toString()).addRank(playerRank);
		}
		return rank != null;
	}

	/**
	 * Remove a rank by name from the player
	 * 
	 * @param player
	 * @param rankname
	 * @return true if success, false otherwise
	 */
	public boolean removePlayerRank(Player player, String rankname) {
		PRRank rank = CacheManager.getRank(rankname);
		if (rank != null) {
			PRPlayer targetPlayer = CacheManager.getPlayer(player.getUniqueId().toString());
			PRPlayerRank playerRank = null;
			for (PRPlayerRank targetPlayerRank : targetPlayer.getRanks()) {
				if (targetPlayerRank.getName().equals(rank.getName())) {
					playerRank = targetPlayerRank;
					break;
				}
			}
			if (playerRank != null) {
				targetPlayer.removeRank(playerRank);
			}
			return playerRank != null;
		}
		return rank != null;
	}

	/**
	 * Get all allowed permissions of a rank
	 * 
	 * @param rank
	 * @return list of allowed permissions that a rank has
	 */
	public List<String> getAllowedPermissions(String rank) {
		List<String> permissions = new ArrayList<String>();
		for (PRPermission permission : CacheManager.getRank(users.getRankIgnoreCase(rank)).getPermissions()) {
			if (permission.getValue()) {
				permissions.add(permission.getName());
			}
		}
		return permissions;
	}

	/**
	 * Get all allowed permissions of a player
	 * 
	 * @param player
	 * @return list of allowed permissions that a player has
	 */
	public List<String> getAllowedPlayerPermissions(Player player) {
		List<String> permissions = new ArrayList<String>();
		for (PRPermission permission : CacheManager.getPlayer(player).getPermissions()) {
			if (permission.getValue()) {
				permissions.add(permission.getName());
			}
		}
		return permissions;
	}

	/**
	 * Get all denied permissions of a rank
	 * 
	 * @param rank
	 * @return list of denied permissions that a rank has
	 */
	public List<String> getDeniedPermissions(String rank) {
		List<String> permissions = new ArrayList<String>();
		for (PRPermission permission : CacheManager.getRank(users.getRankIgnoreCase(rank)).getPermissions()) {
			if (!permission.getValue()) {
				permissions.add(permission.getName());
			}
		}
		return permissions;
	}

	/**
	 * Get all denied permissions of a player
	 * 
	 * @param player
	 * @return list of denied permissions that a player has
	 */
	public List<String> getDeniedPlayerPermissions(Player player) {
		List<String> permissions = new ArrayList<String>();
		for (PRPermission permission : CacheManager.getPlayer(player).getPermissions()) {
			if (!permission.getValue()) {
				permissions.add(permission.getName());
			}
		}
		return permissions;
	}

	/**
	 * Add a permission to a rank
	 * 
	 * @param rank
	 * @param permission
	 * @param allowed
	 * @return true if success, false otherwise
	 */
	public boolean addPermission(String rank, String permission, boolean allowed) {
		return this.users.addPermission(users.getRankIgnoreCase(rank), permission, allowed);
	}

	/**
	 * Remove a permission from a rank
	 * 
	 * @param rank
	 * @param permission
	 * @return true if success, false otherwise
	 */
	public boolean removePermission(String rank, String permission) {
		return this.users.removePermission(users.getRankIgnoreCase(rank), permission);
	}

	/**
	 * Get a list of inherited ranks on a rank
	 * 
	 * @param rank
	 * @return List of inherited rank names
	 */
	public List<String> getInheritances(String rank) {
		return this.users.getInheritances(users.getRankIgnoreCase(rank));
	}

	/**
	 * Add a rankname as inheritance to a rank
	 * 
	 * @param rank
	 * @param inheritance
	 * @return true if success, false otherwise
	 */
	public boolean addInheritance(String rank, String inheritance) {
		return this.users.addInheritance(users.getRankIgnoreCase(rank), inheritance);
	}

	/**
	 * Remove an inheritance from a rank
	 * 
	 * @param rank
	 * @param inheritance
	 * @return true if success, false otherwise
	 */
	public boolean removeInheritance(String rank, String inheritance) {
		return this.users.removeInheritance(users.getRankIgnoreCase(rank), inheritance);
	}

	/**
	 * Should the rank be given to the players when they join for the first time?
	 * 
	 * @param rank
	 * @param isDefault
	 * @return true if rank exists, false otherwise
	 */
	public boolean setDefault(String rank, boolean isDefault) {
		PRRank targetRank = CacheManager.getRank(rank);
		if (targetRank != null) {
			targetRank.setDefault(isDefault);
		}
		return targetRank != null;
	}

	/**
	 * Get the list of default ranks that are given to new players
	 * 
	 * @return List of default ranks
	 */
	public List<String> getDefaultRanks() {
		List<String> defaultRanks = new ArrayList<String>();

		for (PRRank rank : CacheManager.getRanks()) {
			if (rank.isDefault()) {
				defaultRanks.add(rank.getName());
			}
		}

		return defaultRanks;
	}

	/**
	 * Get all available ranknames
	 * 
	 * @return list of ranks in PowerRanks
	 */
	public List<String> getRanks() {
		List<String> ranks = new ArrayList<String>();
		for (PRRank rank : CacheManager.getRanks()) {
			ranks.add(rank.getName());
		}
		return ranks;
	}

	/**
	 * Get the unformatted prefix of a rank
	 * 
	 * @param rank
	 * @return Unformatted prefix
	 */
	public String getPrefix(String rank) {
		if (CacheManager.getRank(rank) != null) {
			return CacheManager.getRank(rank).getPrefix();
		}
		return null;
	}

	/**
	 * Set the prefix of a rank
	 * 
	 * @param rank
	 * @param new_value
	 * @return true if success, false otherwise
	 */
	public boolean setPrefix(String rank, String new_value) {
		if (CacheManager.getRank(rank) != null) {
			CacheManager.getRank(rank).setPrefix(new_value);
			return true;
		}
		return false;
	}

	/**
	 * Get the unformatted suffix of a rank
	 * 
	 * @param rank
	 * @return Unformatted suffix
	 */
	public String getSuffix(String rank) {
		if (CacheManager.getRank(rank) != null) {
			return CacheManager.getRank(rank).getSuffix();
		}
		return null;
	}

	/**
	 * Set the suffix of a rank
	 * 
	 * @param rank
	 * @param new_value
	 * @return true if success, false otherwise
	 */
	public boolean setSuffix(String rank, String new_value) {
		if (CacheManager.getRank(rank) != null) {
			CacheManager.getRank(rank).setSuffix(new_value);
			return true;
		}
		return false;
	}

	/**
	 * Get the player's name color of a rank
	 * 
	 * @param rank
	 * @return Unformatted name color
	 */
	public String getNameColor(String rank) {
		if (CacheManager.getRank(rank) != null) {
			return CacheManager.getRank(rank).getNamecolor();
		}
		return null;
	}

	/**
	 * Set the player's name color of a rank
	 * 
	 * @param rank
	 * @param new_value
	 * @return true if success, false otherwise
	 */
	public boolean setNameColor(String rank, String new_value) {
		if (CacheManager.getRank(rank) != null) {
			CacheManager.getRank(rank).setNamecolor(new_value);
			return true;
		}
		return false;
	}

	/**
	 * Get the player's chat color of a rank
	 * 
	 * @param rank
	 * @return Unformatted chat color
	 */
	public String getChatColor(String rank) {
		if (CacheManager.getRank(rank) != null) {
			return CacheManager.getRank(rank).getChatcolor();
		}
		return null;
	}

	/**
	 * Set the player's chat color of a rank
	 * 
	 * @param rank
	 * @param new_value
	 * @return true if success, false otherwise
	 */
	public boolean setChatColor(String rank, String new_value) {
		if (CacheManager.getRank(rank) != null) {
			CacheManager.getRank(rank).setChatcolor(new_value);
			return true;
		}
		return false;
	}

	/**
	 * Create a new rank
	 * 
	 * @param rank
	 * @return true if success, false otherwise
	 */
	public boolean createRank(String rank) {
		boolean value = users.createRank(rank);
		return value;
	}

	/**
	 * Delete an existing rank
	 * 
	 * @param rank
	 * @return true if success, false otherwise
	 */
	public boolean deleteRank(String rank) {
		boolean value = users.deleteRank(rank);
		return value;
	}

	/**
	 * Set a rank's weight
	 * Higher weight has more priority
	 * 
	 * @param weight
	 * @return true if success, false otherwise
	 */
	public boolean setRankWeight(String rankname, int weight) {
		PRRank rank = CacheManager.getRank(rankname);
		if (rank != null) {
			rank.setWeight(weight);
		}
		return rank != null;
	}

	/**
	 * Add a rank as buyable to a rank
	 * 
	 * @param rank
	 * @param buyable_rank
	 * @return true if success, false otherwise
	 */
	public boolean addBuyableRank(String rank, String buyable_rank) {
		boolean value = users.addBuyableRank(rank, buyable_rank);
		return value;
	}

	/**
	 * Remove a rank as buyable from a rank
	 * 
	 * @param rank
	 * @param buyable_rank
	 * @return true if success, false otherwise
	 */
	public boolean delBuyableRank(String rank, String buyable_rank) {
		boolean value = users.delBuyableRank(rank, buyable_rank);
		return value;
	}

	/**
	 * Set the buy cost of a rank
	 * 
	 * @param rank
	 * @param buyable_rank
	 * @return true if success, false otherwise
	 */
	public boolean setRankBuyCost(String rank, int cost) {
		return this.users.setBuyCost(rank, String.valueOf(cost));
	}

	/**
	 * Add a permission to a rank
	 * 
	 * @param rank
	 * @param permission
	 * @return true if success, false otherwise
	 */
	public boolean addPermission(Player player, String permission, boolean allowed) {
		return this.users.addPlayerPermission(player.getName(), permission, allowed);
	}

	/**
	 * Remove a permission from a player
	 * 
	 * @param player
	 * @param permission
	 * @return true if success, false otherwise
	 */
	public boolean removePermission(Player player, String permission) {
		return this.users.delPlayerPermission(player.getName(), permission);
	}

	/**
	 * Create a usertag on the server
	 * 
	 * @param usertag
	 * @param value
	 * @return true if successful, false otherwise
	 */
	public boolean createUsertag(String usertag, String value) {
		return this.users.createUserTag(usertag, value);
	}

	/**
	 * Change a usertag on the server
	 * 
	 * @param usertag
	 * @param value
	 * @return true if successful, false otherwise
	 */
	public boolean editUsertag(String usertag, String value) {
		return this.users.editUserTag(usertag, value);
	}

	/**
	 * Delete a usertag from the server
	 * 
	 * @param usertag
	 * @return true if successful, false otherwise
	 */
	public boolean removeUsertag(String usertag) {
		return this.users.removeUserTag(usertag);
	}

	/**
	 * Assign a usertag to an player
	 * 
	 * @param player
	 * @param usertag
	 * @return true if successful, false otherwise
	 */
	public boolean setUsertag(Player player, String usertag) {
		return this.users.setUserTag(player.getName(), usertag);
	}

	/**
	 * Add a usertag to an player
	 * 
	 * @param player
	 * @param usertag
	 * @return true if successful, false otherwise
	 */
	public boolean addUsertag(Player player, String usertag) {
		return this.users.addUserTag(player.getName(), usertag);
	}

	/**
	 * Remove an usertag from a player
	 * 
	 * @param player
	 * @param usertag
	 * @return true if successful, false otherwise
	 */
	public boolean delUsertag(Player player, String usertag) {
		return this.users.delUserTag(player.getName(), usertag);
	}

	/**
	 * Deprecated | replaced by {@link #getPrimaryRank(Player)}
	 * 
	 * Get the rankname of a player
	 * 
	 * @param player
	 * @return player's rankname
	 */
	@Deprecated
	public String getPlayerRank(Player player) {
		String rank = users.getPrimaryRank(player);
		return rank;
	}

	/**
	 * Deprecated | replaced by {@link #getAllowedPermissions(String)} &
	 * {@link #getDeniedPermissions(String)}
	 * 
	 * Get a list of permissions of a rank
	 * 
	 * @param rank
	 * @return list of permissions that a rank has
	 */
	@Deprecated
	public List<String> getPermissions(String rank) {
		List<String> permissions = new ArrayList<String>();
		for (PRPermission permission : CacheManager.getRank(users.getRankIgnoreCase(rank)).getPermissions()) {
			permissions.add(permission.getName());
		}
		return permissions;
	}

	/**
	 * Deprecated | replaced by {@link #addPermission(String, String, boolean)}
	 * 
	 * Add a permission to a rank
	 * If the permissions starts with '-' it will be denied
	 * 
	 * @param rank
	 * @param permission
	 * @return true if success, false otherwise
	 */
	@Deprecated
	public boolean addPermission(String rank, String permission) {
		return this.users.addPermission(users.getRankIgnoreCase(rank),
				permission.startsWith("-") ? permission.replaceFirst("-", "") : permission,
				!permission.startsWith("-"));
	}

	/**
	 * Deprecated | replaced by {@link #addPermission(Player, String, boolean)}
	 * 
	 * Add a permission to a rank
	 * If the permissions starts with '-' it will be denied
	 * 
	 * @param rank
	 * @param permission
	 * @return true if success, false otherwise
	 */
	@Deprecated
	public boolean addPermission(Player player, String permission) {
		return this.users.addPlayerPermission(player.getName(),
				permission.startsWith("-") ? permission.replaceFirst("-", "") : permission,
				!permission.startsWith("-"));
	}

	/**
	 * Deprecated
	 * 
	 * Is building enabled on a rank?
	 * return bool
	 */
	@Deprecated
	public boolean isBuildingEnabled(String rank) {
		return false;
	}

	/**
	 * Deprecated
	 * 
	 * set building enabled on a rank
	 * 
	 * @param rank
	 * @param new_value
	 * @return bool
	 */
	@Deprecated
	public boolean setBuildingEnabled(String rank, boolean new_value) {
		return false;
	}

	/**
	 * Deprecated
	 * 
	 * Get the rankname to promote to
	 * 
	 * @param rank
	 * @return String
	 */
	@Deprecated
	public String getPromoteRank(String rank) {
		return "";
	}

	/**
	 * Deprecated
	 * 
	 * Set the rankname to promote to
	 * 
	 * @param rank
	 * @param new_value
	 * @return bool
	 */
	@Deprecated
	public boolean setPromoteRank(String rank, String new_value) {
		return false;
	}

	/**
	 * Deprecated
	 * 
	 * Get the rankname to demote to
	 * 
	 * @param rank
	 * @return String
	 */
	@Deprecated
	public String getDemoteRank(String rank) {
		return "";
	}

	/**
	 * Deprecated
	 * 
	 * Set the rankname to demote to
	 * 
	 * @param rank
	 * @param new_value
	 * @return bool
	 */
	@Deprecated
	public boolean setDemoteRank(String rank, String new_value) {
		return false;
	}

	/**
	 * Deprecated
	 * 
	 * Get all subranks of a player
	 * 
	 * @return List<String>
	 */
	@Deprecated
	public List<String> getSubranks(Player player) {
		return new ArrayList<String>();
	}

	/**
	 * Deprecated
	 * 
	 * Add a subrank to a player
	 * 
	 * @param player
	 * @param rank
	 * @return bool
	 */
	@Deprecated
	public boolean addSubrank(Player player, String rank) {
		return false;
	}

	/**
	 * Deprecated
	 * 
	 * Remove a subrank from a player
	 * 
	 * @param player
	 * @param rank
	 * @return bool
	 */
	@Deprecated
	public boolean removeSubrank(Player player, String rank) {
		return false;
	}

	/**
	 * Deprecated
	 * 
	 * Get formatted subrank prefixes
	 * 
	 * @param player
	 * @return String
	 */
	@Deprecated
	public String getPlayerSubrankPrefixes(Player player) {
		return "";
	}

	/**
	 * Deprecated
	 * 
	 * Get formatted subrank suffixes
	 * 
	 * @param player
	 * @return String
	 */
	@Deprecated
	public String getPlayerSubrankSuffixes(Player player) {
		return "";
	}
}