package nl.svenar.powerranks.bukkit.external;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.data.PowerRanksVerbose;

import com.google.common.collect.Iterables;

import net.milkbowl.vault.permission.Permission;

@SuppressWarnings("deprecation")
public class PowerRanksVaultPermission extends Permission {

	final PowerRanks plugin;
	
	private boolean playerHasErrorQueueOffline = false;

	PowerRanksVaultPermission(PowerRanks plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return this.plugin.getName();
	}

	@Override
	public boolean isEnabled() {
		return this.plugin.isEnabled();
	}

	@Override
	public boolean hasSuperPermsCompat() {
		return true;
	}

	@Override
	public String[] getGroups() {
		List<String> ranks = new ArrayList<String>();
		for (PRRank rank : CacheManager.getRanks()) {
			ranks.add(rank.getName());
		}
		return Iterables.toArray(ranks, String.class);
	}

	@Override
	public boolean hasGroupSupport() {
		return true;
	}

	// String[] getGroup(String name) {
	// return null;
	// }
	//
	// String[] getSubject(OfflinePlayer player) {
	// return null;
	// }
	//
	// String[] getSubject(String player) {
	// return null;
	// }

	/**
	 * Get an active world from an offline player if possible
	 *
	 * @param player The offline player object
	 * @return Maybe a world?
	 */
	String getActiveWorld(OfflinePlayer player) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.getActiveWorld(...)", "Called, player: " + player.getName());
		Player p = player.getPlayer();
		return p != null ? p.getWorld().getName() : null;
	}

	@Override
	public boolean groupHas(String world, String name, String permission) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.groupHas(...)", "Called");
		return CacheManager.getRank(name).getPermission(permission) != null;
		// return CachedRanks.getStringList("Groups." + name +
		// ".permissions").contains(permission);
	}

	@Override
	public boolean groupAdd(final String world, String name, final String permission) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.groupAdd(...)", "Called");
		return false; // return prapi.addPermission(name, permission); // TODO: add support
	}

	@Override
	public boolean groupRemove(final String world, String name, final String permission) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.groupRemove(...)", "Called");
		return false; // return prapi.removePermission(name, permission); // TODO: add support

	}

	@Override
	public boolean playerHas(String world, OfflinePlayer player, String permission) {
		if (player.isOnline()) {
			// boolean hasPermission =
			// this.plugin.playerAllowedPermissions.get(player.getUniqueId()).contains(permission);
			boolean hasPermission = ((Player) player).hasPermission(permission);

			PowerRanksVerbose.log("PowerRanksVaultPermission.playerHas(...)", "Checking player '" + player.getName()
					+ "', permission: " + permission + ", hasPermission: " + hasPermission);
			return hasPermission;
		} else {
			if (!playerHasErrorQueueOffline) {
				playerHasErrorQueueOffline = true;
				PowerRanksVerbose.log("PowerRanksVaultPermission.playerHas(...)", "===----------WARNING----------===");
				PowerRanksVerbose.log("PowerRanksVaultPermission.playerHas(...)",
						"PowerRanksVaultPermission.playerHas(...)");
				PowerRanksVerbose.log("PowerRanksVaultPermission.playerHas(...)",
						"The UUID of player '" + player.getName() + "' is offline and permissions can't be checked!");
				PowerRanksVerbose.log("PowerRanksVaultPermission.playerHas(...)", "===---------------------------===");

				BukkitScheduler scheduler = plugin.getServer().getScheduler();
				scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {

					@Override
					public void run() {
						playerHasErrorQueueOffline = false;
					}
				}, 20L);

			}

			return false;
		}
	}

	@Override
	public boolean playerAdd(final String world, OfflinePlayer player, final String permission) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.playerAdd(...)", "Called, player: " + player.getName());
		return false; // return prapi.addPermission(player.getPlayer(), permission); // TODO: add support
	}

	@Override
	public boolean playerAddTransient(OfflinePlayer player, String permission) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.playerAddTransient(...)",
				"Called, player: " + player.getName());
		return playerAddTransient(getActiveWorld(player), player, permission);
	}

	@Override
	public boolean playerAddTransient(Player player, String permission) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.playerAddTransient(...)",
				"Called, player: " + player.getName());
		return playerAddTransient(player.getWorld().getName(), player, permission);
	}

	@Override
	public boolean playerAddTransient(final String worldName, OfflinePlayer player, final String permission) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.playerAddTransient(...)",
				"Called, player: " + player.getName());
				return false; // return prapi.addPermission(player.getPlayer(), permission); // TODO: add support
	}

	@Override
	public boolean playerRemoveTransient(final String worldName, OfflinePlayer player, final String permission) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.playerRemoveTransient(...)",
				"Called, player: " + player.getName());
				return false; // return prapi.removePermission(player.getPlayer(), permission); // TODO: add support
	}

	@Override
	public boolean playerRemove(final String world, OfflinePlayer player, final String permission) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.playerRemove(...)", "Called, player: " + player.getName());
		return false; // return prapi.removePermission(player.getPlayer(), permission); // TODO: add support
	}

	@Override
	public boolean playerRemoveTransient(Player player, String permission) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.playerRemoveTransient(...)",
				"Called, player: " + player.getName());
		return playerRemoveTransient(player.getWorld().getName(), player, permission);
	}

	@Override
	public boolean playerRemoveTransient(OfflinePlayer player, String permission) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.playerRemoveTransient(...)",
				"Called, player: " + player.getName());
		return playerRemoveTransient(getActiveWorld(player), player, permission);
	}

	@Override
	public boolean playerInGroup(String world, OfflinePlayer player, String group) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.playerInGroup(...)", "Called, player: " + player.getName());
		return false; // return prapi.getSubranks(player.getPlayer()).contains(group); // TODO: add support
	}

	@Override
	public boolean playerAddGroup(final String world, OfflinePlayer player, final String group) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.playerAddGroup(...)", "Called, player: " + player.getName());
		return false; // return prapi.addSubrank(player.getPlayer(), group); // TODO: add support
	}

	@Override
	public boolean playerRemoveGroup(final String world, OfflinePlayer player, final String group) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.playerRemoveGroup(...)",
				"Called, player: " + player.getName());
				return false; // return prapi.removeSubrank(player.getPlayer(), group); // TODO: add support
	}

	@Override
	public String[] getPlayerGroups(String world, OfflinePlayer player) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.getPlayerGroups(...)", "Called, player: " + player.getName());
		ArrayList<String> groups = new ArrayList<String>();
		// groups.add(CachedPlayers.getString("players." + player.getUniqueId() +
		// ".rank"));
		List<String> ranks = new ArrayList<>();
		for (PRPlayerRank rank : CacheManager.getPlayer(player.getUniqueId().toString()).getRanks()) {
			ranks.add(rank.getName());
		}
		if (ranks != null) {
			for (String rank : ranks) {
				groups.add(rank);
			}
		}
		// return (String[]) groups.toArray();
		// return groups/*.stream()*/.toArray(new String[0]);
		String[] output = new String[groups.size()];

		for (int i = 0; i < groups.size(); i++) {
			output[i] = groups.get(i);
		}
		return output;
	}

	@Override
	public String getPrimaryGroup(String world, OfflinePlayer player) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.getPrimaryGroup(...)", "Called, player: " + player.getName());
		List<PRRank> ranks = new ArrayList<PRRank>();
		for (PRPlayerRank playerRank : CacheManager.getPlayer(player.getUniqueId().toString()).getRanks()) {
			PRRank rank = CacheManager.getRank(playerRank.getName());
			if (rank != null) {
				ranks.add(rank);
			}
		}

		PRUtil.sortRanksByWeight(ranks);
		PRUtil.reverseRanks(ranks);

		return ranks.size() > 0 ? ranks.get(0).getName() : "";
	}

	// -- Deprecated methods

	private OfflinePlayer playerFromName(String name) {
		PowerRanksVerbose.log("PowerRanksVaultPermission.playerFromName(...)", "Called, player: " + name);
		return this.plugin.getServer().getOfflinePlayer(name);
	}

	@Override
	public boolean playerHas(String world, String name, String permission) {
		return playerHas(world, playerFromName(name), permission);
	}

	@Override
	public boolean playerAdd(String world, String name, String permission) {
		return playerAdd(world, playerFromName(name), permission);
	}

	@Override
	public boolean playerRemove(String world, String name, String permission) {
		return playerRemove(world, playerFromName(name), permission);
	}

	@Override
	public boolean playerInGroup(String world, String player, String group) {
		return playerInGroup(world, playerFromName(player), group);
	}

	@Override
	public boolean playerAddGroup(String world, String player, String group) {
		return playerAddGroup(world, playerFromName(player), group);
	}

	@Override
	public boolean playerRemoveGroup(String world, String player, String group) {
		return playerRemoveGroup(world, playerFromName(player), group);
	}

	@Override
	public String[] getPlayerGroups(String world, String player) {
		return getPlayerGroups(world, playerFromName(player));
	}

	@Override
	public String getPrimaryGroup(String world, String player) {
		return getPrimaryGroup(world, playerFromName(player));
	}
}