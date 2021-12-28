package nl.svenar.PowerRanks.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CacheManager;
// import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.common.structure.PRPermission;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRRank;
import nl.svenar.common.utils.PRUtil;

public class Users implements Listener {
	PowerRanks m;

	public Users(PowerRanks m) {
		this.m = m;
	}

	public String getPrimaryRank(Player player) {
		if (player == null) {
			return "";
		}

		List<String> ranknames = CacheManager.getPlayer(player.getUniqueId().toString()).getRanks();
		List<PRRank> playerRanks = new ArrayList<PRRank>();
		for (String rankname : ranknames) {
			PRRank rank = CacheManager.getRank(rankname);
			if (rank != null) {
				playerRanks.add(rank);
			}
		}

		playerRanks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(playerRanks));

		return playerRanks.get(0).getName();
	}

	public void fireSetRankAddonEvent() {
	// 	PowerConfigManager languageManager = PowerRanks.getLanguageManager();

	// 	if (PowerRanks.getConfigManager().getBool("announcements.rankup.enabled", false)) {
	// 		Bukkit.broadcastMessage(PowerRanks.chatColor(
	// 				PowerRanks.getConfigManager().getString("announcements.rankup.format", "")
	// 						.replace("[player]", t).replace("[rank]", rank)
	// 						.replace("[powerranks_prefix]",
	// 								languageManager.getString("general.prefix", "")
	// 										.replace("%plugin_name%", PowerRanks.pdf.getName())),
	// 				true));
	// 	}
	}

	// public void setGroup(Player player, String t, String rank, boolean
	// fireAddonEvent) {
	// PowerConfigManager languageManager = PowerRanks.getLanguageManager();
	// if (player != null) {
	// if (player.hasPermission("powerranks.cmd.set") ||
	// player.hasPermission("powerranks.cmd.set." + rank)) {
	// PowerRanksVerbose.log("setGroup(Player, String, String, boolean)",
	// player.getName() + " Changed " + t + "'s rank to: " + rank);
	// Player target = Bukkit.getServer().getPlayer(t);

	// if (target != null) {
	// try {
	// if (CacheManager.getRank(rank) != null) {
	// // this.m.removePermissions(player);
	// List<String> oldRanks =
	// CacheManager.getPlayer(target.getUniqueId().toString()).getRanks();
	// CacheManager.getPlayer(target.getUniqueId().toString()).setRank(rank);
	// // CachedPlayers.set("players." + target.getUniqueId() + ".rank", (Object)
	// rank,
	// // false);
	// // if
	// (PowerRanks.getConfigManager().contains("announcements.rankup.enabled")) {
	// // if
	// (PowerRanks.getConfigManager().contains("announcements.rankup.enabled")) {
	// if (fireAddonEvent
	// && PowerRanks.getConfigManager().getBool("announcements.rankup.enabled",
	// false)) {
	// Bukkit.broadcastMessage(PowerRanks.chatColor(
	// PowerRanks.getConfigManager().getString("announcements.rankup.format", "")
	// .replace("[player]", t).replace("[rank]", rank)
	// .replace("[powerranks_prefix]",
	// languageManager.getString("general.prefix", "")
	// .replace("%plugin_name%", PowerRanks.pdf.getName())),
	// true));
	// }
	// // }
	// // }

	// if (fireAddonEvent)
	// for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses
	// .entrySet()) {
	// PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, target);
	// prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rank,
	// RankChangeCause.SET,
	// true);
	// }

	// Messages.messageSetRankSuccessSender(player, t, rank);
	// Messages.messageSetRankSuccessTarget(target, player.getName(), rank);
	// // this.m.setupPermissions(target);
	// this.m.updateTablistName(target);

	// } else {
	// Messages.messageGroupNotFound(player, rank);
	// }
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// }
	// } else {
	// if (CacheManager.getRank(rank) != null) {

	// boolean offline_player_found = false;

	// for (PRPlayer key : CacheManager.getPlayers()) {
	// if (key.getName().equalsIgnoreCase(t)) {
	// List<String> oldRanks = key.getRanks();
	// key.setRank(rank);
	// // CachedPlayers.set("players." + key + ".rank", (Object) rank, false);
	// // if
	// (PowerRanks.getConfigManager().contains("announcements.rankup.enabled")) {
	// if (fireAddonEvent && PowerRanks.getConfigManager()
	// .getBool("announcements.rankup.enabled", false)) {
	// Bukkit.broadcastMessage(PowerRanks.chatColor(PowerRanks.getConfigManager()
	// .getString("announcements.rankup.format", "").replace("[player]", t)
	// .replace("[rank]", rank).replace("[powerranks_prefix]",
	// languageManager.getString("general.prefix", "")
	// .replace("%plugin_name%", PowerRanks.pdf.getName())),
	// true));
	// }
	// // }

	// if (fireAddonEvent)
	// for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses
	// .entrySet()) {
	// PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, t);
	// prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rank,
	// RankChangeCause.SET, false);
	// }

	// Messages.messageSetRankSuccessSender(player, t, rank);

	// offline_player_found = true;
	// }
	// }

	// if (!offline_player_found) {
	// Messages.messagePlayerNotFound(player, t);
	// }
	// } else {
	// Messages.messageGroupNotFound(player, rank);
	// }
	// }
	// }
	// } else {
	// PowerRanksVerbose.log("setGroup(Player, String, String, boolean)",
	// "Unknown Changed " + t + "'s rank to: " + rank);
	// ConsoleCommandSender console = Bukkit.getConsoleSender();
	// Player target2 = Bukkit.getServer().getPlayer(t);

	// if (target2 != null) {
	// try {
	// if (CacheManager.getRank(rank) != null) {
	// // this.m.removePermissions(target2);
	// // String oldRank = CachedPlayers.getString("players." +
	// target2.getUniqueId() +
	// // ".rank");
	// // CachedPlayers.set("players." + target2.getUniqueId() + ".rank", (Object)
	// // rank, false);
	// List<String> oldRanks =
	// CacheManager.getPlayer(target2.getUniqueId().toString()).getRanks();
	// CacheManager.getPlayer(target2.getUniqueId().toString()).setRank(rank);
	// // if
	// (PowerRanks.getConfigManager().contains("announcements.rankup.enabled")) {
	// if (fireAddonEvent
	// && PowerRanks.getConfigManager().getBool("announcements.rankup.enabled",
	// false)) {
	// Bukkit.broadcastMessage(PowerRanks.chatColor(
	// PowerRanks.getConfigManager().getString("announcements.rankup.format", "")
	// .replace("[player]", t).replace("[rank]", rank)
	// .replace("[powerranks_prefix]",
	// languageManager.getString("general.prefix", "")
	// .replace("%plugin_name%", PowerRanks.pdf.getName())),
	// true));
	// }
	// // }

	// if (fireAddonEvent)
	// for (Entry<File, PowerRanksAddon> prAddon :
	// this.m.addonsManager.addonClasses.entrySet()) {
	// PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, target2);
	// prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rank,
	// RankChangeCause.SET,
	// true);
	// }

	// Messages.messageSetRankSuccessSender(console, t, rank);
	// Messages.messageSetRankSuccessTarget(target2, console.getName(), rank);
	// // this.m.setupPermissions(target2);
	// this.m.updateTablistName(target2);

	// } else {
	// Messages.messageGroupNotFound(console, rank);
	// }
	// } catch (Exception e2) {
	// e2.printStackTrace();
	// }
	// } else {
	// if (CacheManager.getRank(rank) != null) {

	// boolean offline_player_found = false;

	// // if (CachedPlayers.getConfigurationSection("players") != null) {
	// for (PRPlayer key : CacheManager.getPlayers()) {
	// if (key.getName().equalsIgnoreCase(t)) {
	// List<String> oldRanks = key.getRanks();
	// key.setRank(rank);
	// // CachedPlayers.set("players." + key + ".rank", (Object) rank, false);
	// // if
	// (PowerRanks.getConfigManager().contains("announcements.rankup.enabled")) {
	// if (fireAddonEvent
	// && PowerRanks.getConfigManager().getBool("announcements.rankup.enabled",
	// false)) {
	// Bukkit.broadcastMessage(PowerRanks.chatColor(
	// PowerRanks.getConfigManager().getString("announcements.rankup.format", "")
	// .replace("[player]", t).replace("[rank]", rank)
	// .replace("[powerranks_prefix]",
	// languageManager.getString("general.prefix", "")
	// .replace("%plugin_name%", PowerRanks.pdf.getName())),
	// true));
	// }
	// // }

	// if (fireAddonEvent)
	// for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses
	// .entrySet()) {
	// PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, t);
	// prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rank,
	// RankChangeCause.SET,
	// false);
	// }

	// Messages.messageSetRankSuccessSender(console, t, rank);

	// offline_player_found = true;
	// }
	// }
	// // }
	// if (!offline_player_found) {
	// Messages.messagePlayerNotFound(console, t);
	// }
	// } else {
	// Messages.messageGroupNotFound(console, rank);
	// }
	// }
	// }
	// }

	// public boolean setGroup(Player player, String rank, boolean fireAddonEvent) {
	// PowerRanksVerbose.log("setGroup(Player, String, boolean)",
	// " Changed " + player.getName() + "'s rank to: " + rank);
	// PowerConfigManager languageManager = PowerRanks.getLanguageManager();
	// boolean success = false;
	// try {
	// if (CacheManager.getRank(rank) != null) {
	// // this.m.removePermissions(player);
	// List<String> oldRanks =
	// CacheManager.getPlayer(player.getUniqueId().toString()).getRanks();
	// CacheManager.getPlayer(player.getUniqueId().toString()).setRank(rank);
	// // if
	// (PowerRanks.getConfigManager().contains("announcements.rankup.enabled")) {
	// if (fireAddonEvent &&
	// PowerRanks.getConfigManager().getBool("announcements.rankup.enabled", false))
	// {
	// Bukkit.broadcastMessage(
	// PowerRanks.chatColor(
	// PowerRanks.getConfigManager().getString("announcements.rankup.format", "")
	// .replace("[player]", player.getDisplayName()).replace("[rank]", rank)
	// .replace("[powerranks_prefix]",
	// languageManager.getString("general.prefix", "")
	// .replace("%plugin_name%", PowerRanks.pdf.getName())),
	// true));
	// }
	// // }

	// if (fireAddonEvent)
	// for (Entry<File, PowerRanksAddon> prAddon :
	// this.m.addonsManager.addonClasses.entrySet()) {
	// PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
	// prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rank,
	// RankChangeCause.SET, true);
	// }

	// // this.m.setupPermissions(player);
	// this.m.updateTablistName(player);

	// Messages.messageSetRankSuccessSender(player, player.getName(), rank);
	// success = true;
	// } else {
	// success = false;
	// }
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// }
	// return success;
	// }

	// public String getRanksConfigFieldString(String rank, String field) {
	// String value = "";
	// try {
	// value = CachedRanks.getString("Groups." + rank + "." + field);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return value;
	// }

	// public boolean setRanksConfigFieldString(String rank, String field, String
	// new_value) {
	// try {
	// CachedRanks.set("Groups." + rank + "." + field, new_value);
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

	// public int getRanksConfigFieldInt(String rank, String field) {
	// int value = -1;
	// try {
	// value = CachedRanks.getInt("Groups." + rank + "." + field);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return value;
	// }

	// public boolean setRanksConfigFieldInt(String rank, String field, int
	// new_value) {
	// try {
	// CachedRanks.set("Groups." + rank + "." + field, new_value);
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

	// public boolean getRanksConfigFieldBoolean(String rank, String field) {
	// boolean value = false;
	// try {
	// value = CachedRanks.getBoolean("Groups." + rank + "." + field);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return value;
	// }

	// public boolean setRanksConfigFieldBoolean(String rank, String field, boolean
	// new_value) {
	// try {
	// CachedRanks.set("Groups." + rank + "." + field, new_value);
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

	// public String getGroup(String plr, String t) {
	// Player sender = (plr == null || plr == "API") ? null :
	// Bukkit.getServer().getPlayer(plr);
	// Player target = Bukkit.getServer().getPlayer(t);
	// String target_name = "";
	// List<String> ranknames = new ArrayList<String>();
	// if (target != null) {
	// try {
	// ranknames =
	// CacheManager.getPlayer(target.getUniqueId().toString()).getRanks();//
	// CacheManager.getPlayer(target.getUniqueId().toString()).getRank();
	// target_name = target.getName();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// } else {
	// try {
	// for (PRPlayer key : CacheManager.getPlayers()) {
	// if (key.getName().equalsIgnoreCase(t)) {
	// ranknames = key.getRanks();
	// target_name = key.getName();
	// break;
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }

	// }
	// if (ranknames.size() > 0) {
	// if (sender != null) {
	// Messages.messagePlayerCheckRank(sender, target_name, group);
	// } else {
	// Messages.messagePlayerCheckRank(Bukkit.getConsoleSender(), target_name,
	// group);
	// }
	// } else {
	// if (sender != null) {
	// Messages.messagePlayerNotFound(sender, t);
	// } else {
	// Messages.messagePlayerNotFound(Bukkit.getConsoleSender(), t);
	// }
	// }
	// return (group.length() == 0) ? "error" : group;
	// }

	// public String getGroup(Player player) {
	// return CacheManager.getPlayer(player.getUniqueId().toString()).getRanks();
	// }

	// public String getGroup(String playername) {
	// String uuid = "";
	// String group = null;
	// if (Bukkit.getServer().getPlayer(playername) != null)
	// uuid = Bukkit.getServer().getPlayer(playername).getUniqueId().toString();

	// if (uuid.length() == 0) {
	// for (PRPlayer key : CacheManager.getPlayers()) {
	// if (key.getName().equalsIgnoreCase(playername)) {
	// uuid = key.getUUID().toString();
	// }
	// }
	// } else if (uuid.length() != 0) {
	// group = CacheManager.getPlayer(uuid).getRanks();
	// }
	// return group;
	// }

	public ArrayList<PRRank> getGroups() {
		return CacheManager.getRanks();
	}

	public boolean addPermission(String rank, String permission, boolean allowed) {
		if (permission.contains("/") || permission.contains(":")) {
			return false;
		}

		try {
			if (!rank.equals("*")) {
				List<PRPermission> list = CacheManager.getRank(rank).getPermissions();
				boolean contains = false;
				for (PRPermission prPermission : list) {
					if (prPermission.getName().equals(permission)) {
						contains = true;
						break;
					}
				}
				if (!contains) {
					PRPermission newPermission = new PRPermission();
					newPermission.setName(permission);
					newPermission.setValue(allowed);
					CacheManager.getRank(rank).addPermission(newPermission);
				}

				this.m.updatePlayersWithRank(this, rank);
				return true;
			} else {
				for (PRRank r : getGroups()) {
					// List<String> list = rank.getPermissions();
					List<PRPermission> list = r.getPermissions();
					boolean contains = false;
					for (PRPermission prPermission : list) {
						if (prPermission.getName().equals(permission)) {
							contains = true;
							break;
						}
					}
					if (!contains) {
						PRPermission newPermission = new PRPermission();
						newPermission.setName(permission);
						newPermission.setValue(allowed);
						r.addPermission(newPermission);
						// list.add(permission);
						// CachedRanks.set("Groups." + r + ".permissions", (Object) list);
						this.m.updatePlayersWithRank(this, r.getName());
					}
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removePermission(String rank, String permission) {
		try {
			if (!rank.equals("*")) {
				List<PRPermission> list = CacheManager.getRank(rank).getPermissions();
				PRPermission targetPermission = null;
				for (PRPermission prPermission : list) {
					if (prPermission.getName().equals(permission)) {
						targetPermission = prPermission;
						break;
					}
				}
				if (targetPermission != null) {
					CacheManager.getRank(rank).removePermission(targetPermission);
					this.m.updatePlayersWithRank(this, rank);
					return true;
				}

				// if (CacheManager.getRank(rank) != null) {
				// List<String> list = (List<String>) CachedRanks.getStringList("Groups." + rank
				// + ".permissions");
				// list.remove(permission);
				// CachedRanks.set("Groups." + rank + ".permissions", (Object) list);
				// this.m.updatePlayersWithRank(this, rank);
				// return true;
				// }

			} else {
				for (PRRank r : getGroups()) {
					List<PRPermission> list = r.getPermissions();
					PRPermission targetPermission = null;
					for (PRPermission prPermission : list) {
						if (prPermission.getName().equals(permission)) {
							targetPermission = prPermission;
							break;
						}
					}
					if (targetPermission != null) {
						r.removePermission(targetPermission);
						this.m.updatePlayersWithRank(this, r.getName());
					}
					// list.remove(permission);
					// CachedRanks.set("Groups." + r + ".permissions", (Object) list);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean addInheritance(String rank, String inheritance) {
		try {
			if (CacheManager.getRank(rank) != null) {
				CacheManager.getRank(rank).addInheritance(inheritance);
				// List<String> list = (List<String>) CachedRanks.getStringList("Groups." + rank
				// + ".inheritance");
				// if (!list.contains(inheritance)) {
				// list.add(inheritance);
				// }
				// CachedRanks.set("Groups." + rank + ".inheritance", (Object) list);
				this.m.updatePlayersWithRank(this, rank);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setPrefix(String rank, String prefix) {
		try {
			if (CacheManager.getRank(rank) != null) {
				CacheManager.getRank(rank).setPrefix(prefix);
				// CachedRanks.set("Groups." + rank + ".chat.prefix", (Object) prefix);
				this.m.updatePlayersWithRank(this, rank);
				this.m.updatePlayersTABlistWithRank(this, rank);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setSuffix(String rank, String suffix) {
		try {
			if (CacheManager.getRank(rank) != null) {
				CacheManager.getRank(rank).setSuffix(suffix);
				// CachedRanks.set("Groups." + rank + ".chat.suffix", (Object) suffix);
				this.m.updatePlayersWithRank(this, rank);
				this.m.updatePlayersTABlistWithRank(this, rank);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setChatColor(String rank, String color) {
		try {
			if (CacheManager.getRank(rank) != null) {
				CacheManager.getRank(rank).setChatcolor(color);
				// CachedRanks.set("Groups." + rank + ".chat.chatColor", (Object) color);
				this.m.updatePlayersWithRank(this, rank);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setNameColor(String rank, String color) {
		try {
			if (CacheManager.getRank(rank) != null) {
				CacheManager.getRank(rank).setNamecolor(color);
				// CachedRanks.set("Groups." + rank + ".chat.nameColor", (Object) color);
				this.m.updatePlayersWithRank(this, rank);
				this.m.updatePlayersTABlistWithRank(this, rank);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removeInheritance(String rank, String inheritance) {
		try {
			if (CacheManager.getRank(rank) != null) {
				CacheManager.getRank(rank).removeInheritance(inheritance);
				// List<String> list = CacheManager.getRank(rank).getInheritances();
				// if (list.contains(inheritance)) {
				// list.remove(inheritance);
				// }
				// CachedRanks.set("Groups." + rank + ".inheritance", (Object) list);
				this.m.updatePlayersWithRank(this, rank);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean createRank(String rankName) {
		try {
			if (CacheManager.getRank(rankName) == null) {
				PRRank rank = new PRRank();
				rank.setName(rankName);
				rank.setPrefix("[&7" + rankName + "&r]");
				CacheManager.addRank(rank);

				// CachedRanks.set("Groups." + rankName + ".permissions", new
				// ArrayList<String>());
				// CachedRanks.set("Groups." + rankName + ".inheritance", new
				// ArrayList<String>());
				// CachedRanks.set("Groups." + rankName + ".chat.prefix", "[&7" + rankName +
				// "&r]");
				// CachedRanks.set("Groups." + rankName + ".chat.suffix", "");
				// CachedRanks.set("Groups." + rankName + ".chat.chatColor", "&f");
				// CachedRanks.set("Groups." + rankName + ".chat.nameColor", "&f");
				// CachedRanks.set("Groups." + rankName + ".level.promote", "");
				// CachedRanks.set("Groups." + rankName + ".level.demote", "");
				// CachedRanks.set("Groups." + rankName + ".economy.buyable", new
				// ArrayList<String>());
				// CachedRanks.set("Groups." + rankName + ".economy.cost", 0);
				// CachedRanks.set("Groups." + rankName + ".gui.icon", "stone");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteRank(String rankToDelete) {
		if (CacheManager.getRank(rankToDelete) == null) {
			return false;
		}

		if (CacheManager.getRank(CacheManager.getDefaultRank()) == CacheManager.getRank(rankToDelete)) {
			return false;
		}
		for (PRPlayer prPlayer : CacheManager.getPlayers()) {
			List<String> ranknames = prPlayer.getRanks();

			List<PRRank> ranks = new ArrayList<PRRank>();
			for (String rankname : ranknames) {
				PRRank rank = CacheManager.getRank(rankname);
				if (rank != null) {
					ranks.add(rank);
				}
			}

			for (PRRank rank : ranks) {
				if (rank == CacheManager.getRank(rankToDelete)) {
					prPlayer.removeRank(rank.getName());
				}
			}
			// if (CacheManager.getRank(CacheManager.getDefaultRank()) ==
			// CacheManager.getRank(prPlayer.getRank())) {
			// prPlayer.setRank(CacheManager.getDefaultRank());
			// }
		}

		CacheManager.removeRank(CacheManager.getRank(rankToDelete));

		// for (String uuid :
		// CachedPlayers.getConfigurationSection("players").getKeys(false)) {
		// boolean setup_permissions = false;

		// String player_rank = CachedPlayers.getString("players." + uuid + ".rank");

		// if (player_rank.equalsIgnoreCase(rank)) {
		// CachedPlayers.set("players." + uuid + ".rank",
		// CachedRanks.getString("Default"), false);
		// setup_permissions = true;
		// }

		// if (CachedPlayers.getConfigurationSection("players." + uuid + ".subranks") !=
		// null) {
		// for (String subrank : CachedPlayers.getConfigurationSection("players." + uuid
		// + ".subranks")
		// .getKeys(false)) {
		// if (subrank.equalsIgnoreCase(rank)) {
		// CachedPlayers.set("players." + uuid + ".subranks." + subrank, null, false);
		// setup_permissions = true;
		// }
		// }
		// }

		// if (setup_permissions) {
		// Player target =
		// Bukkit.getServer().getPlayer(CachedPlayers.getString("players." + uuid +
		// ".name"));

		// if (target.isOnline()) {
		// // this.m.setupPermissions(target);
		// this.m.updateTablistName(target);
		// }
		// }

		// }
		// try {
		// if (CacheManager.getRank(rank) != null) {
		// CachedRanks.set("Groups." + rank, (Object) null);
		// return true;
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		return true;
	}

	public boolean renameRank(String rank, String to) {
		if (CacheManager.getRank(rank) != null) {
			// List<String> listPermissions = (List<String>)
			// CachedRanks.getStringList("Groups." + to + ".permissions");
			// for (String line : CachedRanks.getStringList("Groups." + rank +
			// ".permissions")) {
			// listPermissions.add(line);
			// }
			// CachedRanks.set("Groups." + to + ".permissions", (Object) listPermissions);

			// List<String> listInheritance = (List<String>)
			// CachedRanks.getStringList("Groups." + to + ".inheritance");
			// for (String line : CachedRanks.getStringList("Groups." + rank +
			// ".inheritance")) {
			// listInheritance.add(line);
			// }
			// CachedRanks.set("Groups." + to + ".inheritance", (Object) listInheritance);

			// CachedRanks.set("Groups." + to + ".chat.prefix", CachedRanks.get("Groups." +
			// rank + ".chat.prefix"));
			// CachedRanks.set("Groups." + to + ".chat.suffix", CachedRanks.get("Groups." +
			// rank + ".chat.suffix"));
			// CachedRanks.set("Groups." + to + ".chat.chatColor", CachedRanks.get("Groups."
			// + rank + ".chat.chatColor"));
			// CachedRanks.set("Groups." + to + ".chat.nameColor", CachedRanks.get("Groups."
			// + rank + ".chat.nameColor"));
			// CachedRanks.set("Groups." + to + ".level.promote", CachedRanks.get("Groups."
			// + rank + ".level.promote"));
			// CachedRanks.set("Groups." + to + ".level.demote", CachedRanks.get("Groups." +
			// rank + ".level.demote"));

			// List<String> listEconomyBuyable = (List<String>) CachedRanks
			// .getStringList("Groups." + to + ".economy.buyable");
			// for (String line : CachedRanks.getStringList("Groups." + rank +
			// ".economy.buyable")) {
			// listEconomyBuyable.add(line);
			// }
			// CachedRanks.set("Groups." + to + ".economy.buyable", (Object)
			// listEconomyBuyable);
			// CachedRanks.set("Groups." + to + ".economy.cost", CachedRanks.get("Groups." +
			// rank + ".economy.cost"));
			// CachedRanks.set("Groups." + to + ".gui.icon", CachedRanks.get("Groups." +
			// rank + ".gui.icon"));

			// ConfigurationSection players =
			// CachedPlayers.getConfigurationSection("players");
			// for (String p : players.getKeys(false)) {
			// if (CachedPlayers.getString("players." + p + ".rank") != null) {
			// if (CachedPlayers.getString("players." + p + ".rank").equalsIgnoreCase(rank))
			// {
			// CachedPlayers.set("players." + p + ".rank", to, false);
			// }
			// }
			// }
			// deleteRank(rank);
			if (CacheManager.getRank(getRankIgnoreCase(to)) == null) {
				CacheManager.getRank(rank).setName(to);
				return true;
			}
		}
		return false;
	}

	public boolean setDefaultRank(String rankname) {
		if (CacheManager.getRank(rankname) != null) {
			CacheManager.setDefaultRank(rankname);
			return true;
		}

		return false;
	}

	public String getRankIgnoreCase(String rankname) {
		String rank = rankname;

		try {
			// ConfigurationSection ranks = CachedRanks.getConfigurationSection("Groups");
			for (PRRank r : CacheManager.getRanks()) {
				if (r.getName().equalsIgnoreCase(rankname)) {
					rank = r.getName();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rank;
	}

	public ArrayList<PRPermission> getPermissions(String rankname) {
		return CacheManager.getRank(rankname).getPermissions();
	}

	public List<String> getInheritances(String rankname) {
		return CacheManager.getRank(rankname).getInheritances();
	}

	public ArrayList<PRPlayer> getCachedPlayers() {
		return CacheManager.getPlayers();
	}

	public String getPrefix(Player player) {
		String prefix = "";

		List<String> ranknames = CacheManager.getPlayer(player.getUniqueId().toString()).getRanks();

		List<PRRank> ranks = new ArrayList<PRRank>();
		for (String rankname : ranknames) {
			PRRank rank = CacheManager.getRank(rankname);
			if (rank != null) {
				ranks.add(rank);
			}
		}

		ranks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(ranks));

		for (PRRank rank : ranks) {
			prefix += rank.getPrefix() + " ";
		}

		if (prefix.endsWith(" ")) {
			prefix = prefix.substring(0, prefix.length() - 1);
		}

		return prefix;
		// return getPrefix(getGroup(player));
	}

	// public String getPrefix(String rankname) {
	// String prefix = "";
	// rankname = this.getRankIgnoreCase(rankname);

	// prefix = CacheManager.getRank(rankname).getPrefix();

	// return prefix;
	// }

	public String getSuffix(Player player) {
		String suffix = "";

		List<String> ranknames = CacheManager.getPlayer(player.getUniqueId().toString()).getRanks();

		List<PRRank> ranks = new ArrayList<PRRank>();
		for (String rankname : ranknames) {
			PRRank rank = CacheManager.getRank(rankname);
			if (rank != null) {
				ranks.add(rank);
			}
		}

		ranks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(ranks));

		for (PRRank rank : ranks) {
			suffix += rank.getSuffix() + " ";
		}

		if (suffix.endsWith(" ")) {
			suffix = suffix.substring(0, suffix.length() - 1);
		}

		return suffix;
		// return getSuffix(getGroup(player));
	}

	// public String getSuffix(String rankname) {
	// String suffix = "";
	// rankname = this.getRankIgnoreCase(rankname);

	// suffix = CacheManager.getRank(rankname).getSuffix();

	// return suffix;
	// }

	public String getChatColor(Player player) {
		String color = "";
		// String rankname = getGroup(player);

		List<String> ranknames = CacheManager.getPlayer(player.getUniqueId().toString()).getRanks();

		List<PRRank> ranks = new ArrayList<PRRank>();
		for (String rankname : ranknames) {
			PRRank rank = CacheManager.getRank(rankname);
			if (rank != null) {
				ranks.add(rank);
			}
		}

		ranks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(ranks));

		color = ranks.get(0).getChatcolor();

		return color;
	}

	public String getNameColor(Player player) {
		String color = "";
		List<String> ranknames = CacheManager.getPlayer(player.getUniqueId().toString()).getRanks();

		List<PRRank> ranks = new ArrayList<PRRank>();
		for (String rankname : ranknames) {
			PRRank rank = CacheManager.getRank(rankname);
			if (rank != null) {
				ranks.add(rank);
			}
		}

		ranks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(ranks));

		color = ranks.get(0).getNamecolor();

		return color;
	}

	public String getDefaultRanks() {
		return CacheManager.getDefaultRank();
		// return CachedRanks.getString("Default");
	}

	public boolean addBuyableRank(String rankname, String rankname2) {
		rankname = getRankIgnoreCase(rankname);
		rankname2 = getRankIgnoreCase(rankname2);
		try {
			if (CacheManager.getRank(rankname) != null) {
				CacheManager.getRank(rankname).addBuyableRank(rankname2);
				// List<String> list = (List<String>) CachedRanks.getStringList("Groups." +
				// rankname + ".economy.buyable");
				// if (!list.contains(rankname2)) {
				// list.add(rankname2);
				// }
				// CachedRanks.set("Groups." + rankname + ".economy.buyable", (Object) list);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean delBuyableRank(String rankname, String rankname2) {
		rankname = getRankIgnoreCase(rankname);
		rankname2 = getRankIgnoreCase(rankname2);
		try {
			if (CacheManager.getRank(rankname) != null) {
				CacheManager.getRank(rankname).removeBuyableRank(rankname2);
				// List<String> list = (List<String>) CachedRanks.getStringList("Groups." +
				// rankname + ".economy.buyable");
				// if (list.contains(rankname2)) {
				// list.remove(rankname2);
				// }
				// CachedRanks.set("Groups." + rankname + ".economy.buyable", (Object) list);
				this.m.updatePlayersWithRank(this, rankname);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<String> getBuyableRanks(String rankname) {
		List<String> ranks = new ArrayList<String>();
		ranks = CacheManager.getRank(rankname).getBuyableRanks();
		return ranks;
	}

	public boolean setBuyCost(String rankname, String cost) {
		rankname = getRankIgnoreCase(rankname);
		if (!cost.chars().anyMatch(Character::isLetter)) {
			try {
				if (CacheManager.getRank(rankname) != null) {
					CacheManager.getRank(rankname).setBuyCost(Float.parseFloat(cost));
					// CachedRanks.set("Groups." + rankname + ".economy.cost",
					// Integer.parseInt(cost));
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public float getRankCost(String rankname) {
		float cost = 0;
		try {
			if (CacheManager.getRank(rankname) != null) {
				cost = CacheManager.getRank(rankname).getBuyCost();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cost;
	}

	public boolean setBuyDescription(String rankname, final String description) {
		rankname = this.getRankIgnoreCase(rankname);
		try {
			if (CacheManager.getRank(rankname) != null) {
				CacheManager.getRank(rankname).setBuyDescription(description);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setBuyCommand(String rankname, final String command) {
		rankname = this.getRankIgnoreCase(rankname);
		try {
			if (CacheManager.getRank(rankname) != null) {
				CacheManager.getRank(rankname).setBuyCommand(command);
				// CachedRanks.set("Groups." + rankname + ".economy.command", command);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean addPlayerPermission(String target_player_name, String permission, boolean allowed) {
		if (permission.contains("/") || permission.contains(":")) {
			return false;
		}

		Player target_player = Bukkit.getServer().getPlayer(target_player_name);

		if (target_player != null) {
			try {
				if (CacheManager.getPlayer(target_player.getUniqueId().toString()) != null) {
					List<PRPermission> list = CacheManager.getPlayer(target_player.getUniqueId().toString())
							.getPermissions();
					// if (!list.contains(permission)) {
					// list.add(permission);
					// CachedPlayers.set("players." + target_player.getUniqueId() + ".permissions",
					// (Object) list,
					// false);
					// }
					// this.m.setupPermissions(target_player);
					PRPermission targetPermission = null;
					for (PRPermission prPermission : list) {
						if (prPermission.getName().equals(permission)) {
							targetPermission = prPermission;
							break;
						}
					}
					if (targetPermission == null) {
						PRPermission newPermission = new PRPermission();
						newPermission.setName(permission);
						newPermission.setValue(allowed);
						CacheManager.getPlayer(target_player.getUniqueId().toString()).addPermission(newPermission);
						// r.addPermission(newPermission);
						// list.add(permission);
						// CachedRanks.set("Groups." + r + ".permissions", (Object) list);
						// this.m.updatePlayersWithRank(this, r.getName());
					}
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			String uuid = "";
			try {
				for (PRPlayer key : CacheManager.getPlayers()) {
					if (key.getName().equalsIgnoreCase(target_player_name)) {
						uuid = key.getUUID().toString();
					}
				}

				if (uuid.length() > 0) {
					if (CacheManager.getPlayer(uuid) != null) {
						// List<String> list = (List<String>) CachedPlayers
						// .getStringList("players." + uuid + ".permissions");
						// if (!list.contains(permission)) {
						// list.add(permission);
						// CachedPlayers.set("players." + uuid + ".permissions", (Object) list, false);
						// }
						List<PRPermission> list = CacheManager.getPlayer(uuid).getPermissions();
						PRPermission targetPermission = null;
						for (PRPermission prPermission : list) {
							if (prPermission.getName().equals(permission)) {
								targetPermission = prPermission;
								break;
							}
						}
						if (targetPermission == null) {
							PRPermission newPermission = new PRPermission();
							newPermission.setName(permission);
							newPermission.setValue(allowed);
							CacheManager.getPlayer(uuid).addPermission(newPermission);
							// r.addPermission(newPermission);
							// list.add(permission);
							// CachedRanks.set("Groups." + r + ".permissions", (Object) list);
							// this.m.updatePlayersWithRank(this, r.getName());
						}
						return true;
					} else {
						return false;
					}
				} else
					return false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean delPlayerPermission(String target_player_name, String permission) {
		if (permission.contains("/") || permission.contains(":")) {
			return false;
		}

		Player target_player = Bukkit.getServer().getPlayer(target_player_name);

		if (target_player != null) {
			try {
				if (CacheManager.getPlayer(target_player.getUniqueId().toString()) != null) {
					List<PRPermission> list = CacheManager.getPlayer(target_player.getUniqueId().toString())
							.getPermissions();
					PRPermission targetPermission = null;
					for (PRPermission prPermission : list) {
						if (prPermission.getName().equals(permission)) {
							targetPermission = prPermission;
							break;
						}
					}
					if (targetPermission != null) {
						CacheManager.getPlayer(target_player.getUniqueId().toString())
								.removePermission(targetPermission);
					}
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			String uuid = "";
			try {
				for (PRPlayer key : CacheManager.getPlayers()) {
					if (key.getName().equalsIgnoreCase(target_player_name)) {
						uuid = key.getUUID().toString();
					}
				}

				if (uuid.length() > 0) {
					if (CacheManager.getPlayer(uuid) != null) {
						List<PRPermission> list = CacheManager.getPlayer(uuid).getPermissions();
						PRPermission targetPermission = null;
						for (PRPermission prPermission : list) {
							if (prPermission.getName().equals(permission)) {
								targetPermission = prPermission;
								break;
							}
						}
						if (targetPermission != null) {
							// PRPermission newPermission = new PRPermission();
							// newPermission.setName(permission);
							// newPermission.setValue(true);
							CacheManager.getPlayer(uuid).removePermission(targetPermission);
							// r.addPermission(newPermission);
							// list.add(permission);
							// CachedRanks.set("Groups." + r + ".permissions", (Object) list);
							// this.m.updatePlayersWithRank(this, r.getName());
						}
						// if (list.contains(permission)) {
						// list.remove(permission);
						// CachedPlayers.set("players." + uuid + ".permissions", (Object) list, false);
						// }
						return true;
					} else {
						return false;
					}
				} else
					return false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public List<PRPermission> getPlayerPermissions(String playername) {
		List<PRPermission> list = new ArrayList<PRPermission>();

		Player player = Bukkit.getServer().getPlayer(playername);
		if (player == null)
			return list;

		String uuid = player.getUniqueId().toString();

		try {

			if (CacheManager.getPlayer(uuid) != null) {
				list = CacheManager.getPlayer(uuid).getPermissions();
			} else {
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public boolean createUserTag(String tag, String format) {

		Map<String, String> availableUsertags = (Map<String, String>) PowerRanks.getUsertagManager().getMap("usertags",
				new HashMap<String, String>());

		boolean exists = false;
		for (Entry<?, ?> entry : availableUsertags.entrySet()) {
			if (entry.getKey().toString().equalsIgnoreCase(tag)) {
				exists = true;
			}
		}

		if (exists) {
			return false;
		}

		availableUsertags.put(tag, format);

		PowerRanks.getUsertagManager().setMap("usertags", availableUsertags);

		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean editUserTag(String tag, String format) {
		Map<String, String> availableUsertags = (Map<String, String>) PowerRanks.getUsertagManager().getMap("usertags",
				new HashMap<String, String>());

		String key = "";
		for (Entry<?, ?> entry : availableUsertags.entrySet()) {
			if (entry.getKey().toString().equalsIgnoreCase(tag)) {
				key = entry.getKey().toString();
			}
		}

		if (key.length() == 0) {
			return false;
		}

		availableUsertags.put(key, format);

		PowerRanks.getUsertagManager().setMap("usertags", availableUsertags);

		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean removeUserTag(String tag) {
		Map<String, String> availableUsertags = (Map<String, String>) PowerRanks.getUsertagManager().getMap("usertags",
				new HashMap<String, String>());

		String key = "";
		for (Entry<?, ?> entry : availableUsertags.entrySet()) {
			if (entry.getKey().toString().equalsIgnoreCase(tag)) {
				key = entry.getKey().toString();
			}
		}

		if (key.length() == 0) {
			return false;
		}

		availableUsertags.remove(key);

		PowerRanks.getUsertagManager().setMap("usertags", availableUsertags);

		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean setUserTag(Player player, String tag) {
		Map<String, String> availableUsertags = (Map<String, String>) PowerRanks.getUsertagManager().getMap("usertags",
				new HashMap<String, String>());

		PRPlayer targetPlayer = CacheManager.getPlayer(player.getUniqueId().toString());
		if (Objects.isNull(targetPlayer)) {
			return false;
		}

		String key = "";
		for (Entry<?, ?> entry : availableUsertags.entrySet()) {
			if (entry.getKey().toString().equalsIgnoreCase(tag)) {
				key = entry.getKey().toString();
			}
		}

		if (key.length() == 0) {
			return false;
		}

		targetPlayer.addUsertag(key);

		return true;
	}

	public boolean setUserTag(String playername, String tag) {
		Player player = Bukkit.getServer().getPlayer(playername);
		return setUserTag(player, tag);
	}

	@SuppressWarnings("unchecked")
	public Set<String> getUserTags() {
		Set<String> tags = new HashSet<String>();

		Map<String, String> availableUsertags = (Map<String, String>) PowerRanks.getUsertagManager().getMap("usertags",
				new HashMap<String, String>());

		for (Entry<String, String> entry : availableUsertags.entrySet()) {
			tags.add(entry.getKey());
		}

		return tags;
	}

	@SuppressWarnings("unchecked")
	public String getUserTagValue(String tag) {

		Map<String, String> availableUsertags = (Map<String, String>) PowerRanks.getUsertagManager().getMap("usertags",
				new HashMap<String, String>());

		String key = "";
		for (Entry<?, ?> entry : availableUsertags.entrySet()) {
			if (entry.getKey().toString().equalsIgnoreCase(tag)) {
				key = entry.getValue().toString();
			}
		}

		if (key.length() == 0) {
			return "";
		}

		return key;
	}

	public String getUserTagValue(Player player) {
		PRPlayer targetPlayer = CacheManager.getPlayer(player.getUniqueId().toString());
		if (Objects.isNull(targetPlayer)) {
			return "";
		}

		if (targetPlayer.getUsertags().size() == 0) {
			return "";
		}

		String usertag = targetPlayer.getUsertags().get(0);
		if (usertag.length() > 0) {
			return getUserTagValue(usertag);
		}

		return "";
	}

	public boolean clearUserTag(String playername) {
		PRPlayer targetPlayer = CacheManager.getPlayer(playername);
		if (Objects.isNull(targetPlayer)) {
			targetPlayer = CacheManager.getPlayer(Bukkit.getServer().getPlayer(playername).getUniqueId().toString());
		}
		if (Objects.isNull(targetPlayer)) {
			return false;
		}

		targetPlayer.setUsertags(new ArrayList<String>());

		return true;
	}

	// public boolean setPromoteRank(String rank, String promote_rank) {
	// try {
	// CacheManager.getRank(getRankIgnoreCase(rank)).setPromoteRank(promote_rank);
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// // return setRanksConfigFieldString(getRankIgnoreCase(rank), "level.promote",
	// // promote_rank);
	// }

	// public boolean setDemoteRank(String rank, String demote_rank) {
	// try {
	// CacheManager.getRank(getRankIgnoreCase(rank)).setDemoteRank(demote_rank);
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// // return setRanksConfigFieldString(getRankIgnoreCase(rank), "level.demote",
	// // demote_rank);
	// }

	// public boolean clearPromoteRank(String rank) {
	// try {
	// CacheManager.getRank(getRankIgnoreCase(rank)).setPromoteRank("");
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// // return setRanksConfigFieldString(getRankIgnoreCase(rank), "level.promote",
	// // "");
	// }

	// public boolean clearDemoteRank(String rank) {
	// try {
	// CacheManager.getRank(getRankIgnoreCase(rank)).setDemoteRank("");
	// return true;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// // return setRanksConfigFieldString(getRankIgnoreCase(rank), "level.demote",
	// // "");
	// }

	public ArrayList<String> getPlayerNames() {
		ArrayList<String> player_names = new ArrayList<String>();

		ArrayList<PRPlayer> players_section = CacheManager.getPlayers();
		for (PRPlayer key : players_section) {
			player_names.add(key.getName());
		}

		return player_names;
	}

	public boolean rankExists(String rankname) {
		return CacheManager.getRank(rankname) != null;
	}
}