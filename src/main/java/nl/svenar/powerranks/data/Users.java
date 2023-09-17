package nl.svenar.powerranks.data;

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

// import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.common.structure.PRPermission;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRPlayerRank;
import nl.svenar.common.structure.PRRank;
import nl.svenar.common.utils.PRUtil;
import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.cache.CacheManager;

public class Users implements Listener {
	PowerRanks m;

	public Users(PowerRanks m) {
		this.m = m;
	}

	public String getPrimaryRank(Player player) {
		if (player == null) {
			return "";
		}

		List<String> ranknames = new ArrayList<>();
		for (PRPlayerRank playerRank : CacheManager.getPlayer(player.getUniqueId().toString()).getRanks()) {
			ranknames.add(playerRank.getName());
		}
		List<PRRank> playerRanks = new ArrayList<PRRank>();
		for (String rankname : ranknames) {
			PRRank rank = CacheManager.getRank(rankname);
			if (rank != null) {
				playerRanks.add(rank);
			}
		}

		PRUtil.sortRanksByWeight(playerRanks);
		PRUtil.reverseRanks(playerRanks);

		return playerRanks.size() > 0 ? playerRanks.get(0).getName() : "";
	}

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

		for (PRPlayer prPlayer : CacheManager.getPlayers()) {
			List<String> ranknames = new ArrayList<>();
			for (PRPlayerRank playerRank : prPlayer.getRanks()) {
				ranknames.add(playerRank.getName());
			}

			List<PRRank> ranks = new ArrayList<PRRank>();
			for (String rankname : ranknames) {
				PRRank rank = CacheManager.getRank(rankname);
				if (rank != null) {
					ranks.add(rank);
				}
			}

			for (PRRank rank : ranks) {
				if (rank == CacheManager.getRank(rankToDelete)) {
					PRPlayerRank playerRank = null;
					for (PRPlayerRank targetPlayerRank : prPlayer.getRanks()) {
						if (targetPlayerRank.getName().equals(rank.getName())) {
							playerRank = targetPlayerRank;
							break;
						}
					}
					if (playerRank != null) {
						prPlayer.removeRank(playerRank);
					}
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

		List<String> ranknames = new ArrayList<>();
		for (PRPlayerRank playerRank : CacheManager.getPlayer(player.getUniqueId().toString()).getRanks()) {
			ranknames.add(playerRank.getName());
		}

		List<PRRank> ranks = new ArrayList<PRRank>();
		for (String rankname : ranknames) {
			PRRank rank = CacheManager.getRank(rankname);
			if (rank != null) {
				ranks.add(rank);
			}
		}

		PRUtil.sortRanksByWeight(ranks);
		PRUtil.reverseRanks(ranks);

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

		List<String> ranknames = new ArrayList<>();
		for (PRPlayerRank playerRank : CacheManager.getPlayer(player.getUniqueId().toString()).getRanks()) {
			ranknames.add(playerRank.getName());
		}

		List<PRRank> ranks = new ArrayList<PRRank>();
		for (String rankname : ranknames) {
			PRRank rank = CacheManager.getRank(rankname);
			if (rank != null) {
				ranks.add(rank);
			}
		}

		PRUtil.sortRanksByWeight(ranks);
		PRUtil.reverseRanks(ranks);

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

		List<String> ranknames = new ArrayList<>();
		for (PRPlayerRank playerRank : CacheManager.getPlayer(player.getUniqueId().toString()).getRanks()) {
			ranknames.add(playerRank.getName());
		}

		List<PRRank> ranks = new ArrayList<PRRank>();
		for (String rankname : ranknames) {
			PRRank rank = CacheManager.getRank(rankname);
			if (rank != null) {
				ranks.add(rank);
			}
		}

		PRUtil.sortRanksByWeight(ranks);
		PRUtil.reverseRanks(ranks);

		color = ranks.size() > 0 ? ranks.get(0).getChatcolor() : "&f";

		return color;
	}

	public String getNameColor(Player player) {
		String color = "";
		List<String> ranknames = new ArrayList<>();
		for (PRPlayerRank playerRank : CacheManager.getPlayer(player.getUniqueId().toString()).getRanks()) {
			ranknames.add(playerRank.getName());
		}

		List<PRRank> ranks = new ArrayList<PRRank>();
		for (String rankname : ranknames) {
			PRRank rank = CacheManager.getRank(rankname);
			if (rank != null) {
				ranks.add(rank);
			}
		}

		PRUtil.sortRanksByWeight(ranks);
		PRUtil.reverseRanks(ranks);

		color = ranks.size() > 0 ? ranks.get(0).getNamecolor() : "&f";

		return color;
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
		PRRank rank = CacheManager.getRank(rankname);
		if (rank != null) {
			ranks = rank.getBuyableRanks();
		}
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

	public boolean addPlayerPermission(String targetPlayerName, String permission, boolean allowed) {
		if (permission.contains("/") || permission.contains(":")) {
			return false;
		}

		PRPlayer targetPlayer = CacheManager.getPlayer(targetPlayerName);

		if (targetPlayer != null) {
			try {
				List<PRPermission> list = targetPlayer.getPermissions();
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
					targetPlayer.addPermission(newPermission);
				}
				return true;
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

		targetPlayer.setUsertag(key);

		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean addUserTag(Player player, String tag) {
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

	@SuppressWarnings("unchecked")
	public boolean delUserTag(Player player, String tag) {
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

		targetPlayer.removeUsertag(key);

		return true;
	}

	public boolean setUserTag(String playername, String tag) {
		Player player = Bukkit.getServer().getPlayer(playername);
		return setUserTag(player, tag);
	}

	public boolean addUserTag(String playername, String tag) {
		Player player = Bukkit.getServer().getPlayer(playername);
		return addUserTag(player, tag);
	}

	public boolean delUserTag(String playername, String tag) {
		Player player = Bukkit.getServer().getPlayer(playername);
		return delUserTag(player, tag);
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