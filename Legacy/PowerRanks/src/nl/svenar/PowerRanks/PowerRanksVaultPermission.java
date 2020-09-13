package nl.svenar.PowerRanks;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.google.common.collect.Iterables;

import net.milkbowl.vault.permission.Permission;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.api.PowerRanksAPI;

@SuppressWarnings("deprecation")
public class PowerRanksVaultPermission extends Permission {
	final PowerRanks plugin;
	final Users users;
	final PowerRanksAPI prapi;

	PowerRanksVaultPermission(PowerRanks plugin) {
		this.plugin = plugin;
		this.users = new Users(plugin);
		this.prapi = new PowerRanksAPI(plugin);
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
		return Iterables.toArray(users.getGroups(), String.class);
	}

	@Override
	public boolean hasGroupSupport() {
		return true;
	}

//	String[] getGroup(String name) {
//		return null;
//	}
//
//	String[] getSubject(OfflinePlayer player) {
//		return null;
//	}
//
//	String[] getSubject(String player) {
//		return null;
//	}

	/**
	 * Get an active world from an offline player if possible
	 *
	 * @param player The offline player object
	 * @return Maybe a world?
	 */
	String getActiveWorld(OfflinePlayer player) {
		Player p = player.getPlayer();
		return p != null ? p.getWorld().getName() : null;
	}

	@Override
	public boolean groupHas(String world, String name, String permission) {
		return CachedRanks.getStringList("Groups." + name + ".permissions").contains(permission);
	}

	@Override
	public boolean groupAdd(final String world, String name, final String permission) {
		return prapi.addPermission(name, permission);
	}

	@Override
	public boolean groupRemove(final String world, String name, final String permission) {
		return prapi.removePermission(name, permission);

	}

	@Override
	public boolean playerHas(String world, OfflinePlayer player, String permission) {
		return this.plugin.playerAllowedPermissions.get(player).contains(permission);
	}

	@Override
	public boolean playerAdd(final String world, OfflinePlayer player, final String permission) {
		return prapi.addPermission(player.getPlayer(), permission);
	}

	@Override
	public boolean playerAddTransient(OfflinePlayer player, String permission) {
		return playerAddTransient(getActiveWorld(player), player, permission);
	}

	@Override
	public boolean playerAddTransient(Player player, String permission) {
		return playerAddTransient(player.getWorld().getName(), player, permission);
	}

	@Override
	public boolean playerAddTransient(final String worldName, OfflinePlayer player, final String permission) {
		return prapi.addPermission(player.getPlayer(), permission);
	}

	@Override
	public boolean playerRemoveTransient(final String worldName, OfflinePlayer player, final String permission) {
		return prapi.removePermission(player.getPlayer(), permission);
	}

	@Override
	public boolean playerRemove(final String world, OfflinePlayer player, final String permission) {
		return prapi.removePermission(player.getPlayer(), permission);
	}

	@Override
	public boolean playerRemoveTransient(Player player, String permission) {
		return playerRemoveTransient(player.getWorld().getName(), player, permission);
	}

	@Override
	public boolean playerRemoveTransient(OfflinePlayer player, String permission) {
		return playerRemoveTransient(getActiveWorld(player), player, permission);
	}

	@Override
	public boolean playerInGroup(String world, OfflinePlayer player, String group) {
		return prapi.getSubranks(player.getPlayer()).contains(group);
	}

	@Override
	public boolean playerAddGroup(final String world, OfflinePlayer player, final String group) {
		return prapi.addSubrank(player.getPlayer(), group);
	}

	@Override
	public boolean playerRemoveGroup(final String world, OfflinePlayer player, final String group) {
		return prapi.removeSubrank(player.getPlayer(), group);
	}

	@Override
	public String[] getPlayerGroups(String world, OfflinePlayer player) {
		ArrayList<String> groups = new ArrayList<String>();
		groups.add(CachedPlayers.getString("players." + player.getUniqueId() + ".rank"));
		ConfigurationSection subranks = CachedPlayers.getConfigurationSection("players." + player.getUniqueId() + ".subranks");
		if (subranks != null) {
			for (String subrank : subranks.getKeys(false)) {
				groups.add(subrank);
			}
		}
		return (String[]) groups.toArray();
	}

	@Override
	public String getPrimaryGroup(String world, OfflinePlayer player) {
		return CachedPlayers.getString("players." + player.getUniqueId() + ".rank");
	}

	// -- Deprecated methods

	private OfflinePlayer playerFromName(String name) {
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
