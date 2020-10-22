package nl.svenar.PowerRanks.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;
import nl.svenar.PowerRanks.addons.PowerRanksAddon.RankChangeCause;
import nl.svenar.PowerRanks.addons.PowerRanksPlayer;

import org.bukkit.event.Listener;

@SuppressWarnings("deprecation")
public class Users implements Listener {
	PowerRanks m;

	public Users(PowerRanks m) {
		this.m = m;
	}

	public void setGroup(Player player, String t, String rank, boolean fireAddonEvent) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		if (player != null) {
			if (player.hasPermission("powerranks.cmd.set") || player.hasPermission("powerranks.cmd.set." + rank)) {
				Player target = Bukkit.getServer().getPlayer(t);

				if (target != null) {
					try {
						if (CachedRanks.get("Groups." + rank) != null) {
							this.m.removePermissions(player);
							String oldRank = CachedPlayers.getString("players." + target.getUniqueId() + ".rank");
							CachedPlayers.set("players." + target.getUniqueId() + ".rank", (Object) rank, false);
							if (CachedConfig.contains("announcements.rankup.enabled")) {
								if (CachedConfig.contains("announcements.rankup.enabled")) {
									if (fireAddonEvent && CachedConfig.getBoolean("announcements.rankup.enabled")) {
										Bukkit.broadcastMessage(PowerRanks.chatColor(CachedConfig.getString("announcements.rankup.format").replace("[player]", t).replace("[rank]", rank).replace("[powerranks_prefix]",
												langYaml.getString("general.prefix").replace("%plugin_name%", PowerRanks.pdf.getName())), true));
									}
								}
							}

							if (fireAddonEvent)
								for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
									PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, target);
									prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rank, RankChangeCause.SET, true);
								}

							Messages.messageSetRankSuccessSender(player, t, rank);
							Messages.messageSetRankSuccessTarget(target, player.getName(), rank);
							this.m.setupPermissions(target);
							this.m.updateTablistName(target);

						} else {
							Messages.messageGroupNotFound(player, rank);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
					if (CachedRanks.get("Groups." + rank) != null) {

						boolean offline_player_found = false;

						for (String key : CachedPlayers.getConfigurationSection("players").getKeys(false)) {
							if (CachedPlayers.getString("players." + key + ".name").equalsIgnoreCase(t)) {
								String oldRank = CachedPlayers.getString("players." + key + ".rank");
								CachedPlayers.set("players." + key + ".rank", (Object) rank, false);
								if (CachedConfig.contains("announcements.rankup.enabled")) {
									if (fireAddonEvent && CachedConfig.getBoolean("announcements.rankup.enabled")) {
										Bukkit.broadcastMessage(PowerRanks.chatColor(CachedConfig.getString("announcements.rankup.format").replace("[player]", t).replace("[rank]", rank).replace("[powerranks_prefix]",
												langYaml.getString("general.prefix").replace("%plugin_name%", PowerRanks.pdf.getName())), true));
									}
								}

								if (fireAddonEvent)
									for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
										PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, t);
										prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rank, RankChangeCause.SET, false);
									}

								Messages.messageSetRankSuccessSender(player, t, rank);

								offline_player_found = true;
							}
						}

						if (!offline_player_found) {
							Messages.messagePlayerNotFound(player, t);
						}
					} else {
						Messages.messageGroupNotFound(player, rank);
					}
				}
			}
		} else {
			ConsoleCommandSender console = Bukkit.getConsoleSender();
			Player target2 = Bukkit.getServer().getPlayer(t);

			if (target2 != null) {
				try {
					if (CachedRanks.get("Groups." + rank) != null) {
						this.m.removePermissions(target2);
						String oldRank = CachedPlayers.getString("players." + target2.getUniqueId() + ".rank");
						CachedPlayers.set("players." + target2.getUniqueId() + ".rank", (Object) rank, false);
						if (CachedConfig.contains("announcements.rankup.enabled")) {
							if (fireAddonEvent && CachedConfig.getBoolean("announcements.rankup.enabled")) {
								Bukkit.broadcastMessage(PowerRanks.chatColor(CachedConfig.getString("announcements.rankup.format").replace("[player]", t).replace("[rank]", rank).replace("[powerranks_prefix]",
										langYaml.getString("general.prefix").replace("%plugin_name%", PowerRanks.pdf.getName())), true));
							}
						}

						if (fireAddonEvent)
							for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
								PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, target2);
								prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rank, RankChangeCause.SET, true);
							}

						Messages.messageSetRankSuccessSender(console, t, rank);
						Messages.messageSetRankSuccessTarget(target2, console.getName(), rank);
						this.m.setupPermissions(target2);
						this.m.updateTablistName(target2);

					} else {
						Messages.messageGroupNotFound(console, rank);
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} else {
				if (CachedRanks.get("Groups." + rank) != null) {

					boolean offline_player_found = false;

					if (CachedPlayers.getConfigurationSection("players") != null) {
						for (String key : CachedPlayers.getConfigurationSection("players").getKeys(false)) {
							if (CachedPlayers.getString("players." + key + ".name").equalsIgnoreCase(t)) {
								String oldRank = CachedPlayers.getString("players." + key + ".rank");
								CachedPlayers.set("players." + key + ".rank", (Object) rank, false);
								if (CachedConfig.contains("announcements.rankup.enabled")) {
									if (fireAddonEvent && CachedConfig.getBoolean("announcements.rankup.enabled")) {
										Bukkit.broadcastMessage(PowerRanks.chatColor(CachedConfig.getString("announcements.rankup.format").replace("[player]", t).replace("[rank]", rank).replace("[powerranks_prefix]",
												langYaml.getString("general.prefix").replace("%plugin_name%", PowerRanks.pdf.getName())), true));
									}
								}

								if (fireAddonEvent)
									for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
										PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, t);
										prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rank, RankChangeCause.SET, false);
									}

								Messages.messageSetRankSuccessSender(console, t, rank);

								offline_player_found = true;
							}
						}
					}
					if (!offline_player_found) {
						Messages.messagePlayerNotFound(console, t);
					}
				} else {
					Messages.messageGroupNotFound(console, rank);
				}
			}
		}
	}

	public boolean setGroup(Player player, String rank, boolean fireAddonEvent) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		boolean success = false;
		try {
			if (CachedRanks.get("Groups." + rank) != null) {
				this.m.removePermissions(player);
				String oldRank = CachedPlayers.getString("players." + player.getUniqueId() + ".rank");
				CachedPlayers.set("players." + player.getUniqueId() + ".rank", (Object) rank, false);
				CachedPlayers.update();
				if (CachedConfig.contains("announcements.rankup.enabled")) {
					if (fireAddonEvent && CachedConfig.getBoolean("announcements.rankup.enabled")) {
						Bukkit.broadcastMessage(PowerRanks.chatColor(CachedConfig.getString("announcements.rankup.format").replace("[player]", player.getDisplayName()).replace("[rank]", rank).replace("[powerranks_prefix]",
								langYaml.getString("general.prefix").replace("%plugin_name%", PowerRanks.pdf.getName())), true));
					}
				}

				if (fireAddonEvent)
					for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
						PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
						prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rank, RankChangeCause.SET, true);
					}

				this.m.setupPermissions(player);
				this.m.updateTablistName(player);

				Messages.messageSetRankSuccessSender(player, player.getName(), rank);
				success = true;
			} else {
				success = false;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return success;
	}

	public String getRanksConfigFieldString(String rank, String field) {
		String value = "";
		try {
			value = CachedRanks.getString("Groups." + rank + "." + field);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public boolean setRanksConfigFieldString(String rank, String field, String new_value) {
		try {
			CachedRanks.set("Groups." + rank + "." + field, new_value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public int getRanksConfigFieldInt(String rank, String field) {
		int value = -1;
		try {
			value = CachedRanks.getInt("Groups." + rank + "." + field);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public boolean setRanksConfigFieldInt(String rank, String field, int new_value) {
		try {
			CachedRanks.set("Groups." + rank + "." + field, new_value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean getRanksConfigFieldBoolean(String rank, String field) {
		boolean value = false;
		try {
			value = CachedRanks.getBoolean("Groups." + rank + "." + field);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public boolean setRanksConfigFieldBoolean(String rank, String field, boolean new_value) {
		try {
			CachedRanks.set("Groups." + rank + "." + field, new_value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getGroup(String plr, String t) {
		Player sender = (plr == null || plr == "API") ? null : Bukkit.getServer().getPlayer(plr);
		Player target = Bukkit.getServer().getPlayer(t);
		String target_name = "";
		String group = "";
		if (target != null) {
			try {
				group = CachedPlayers.getString("players." + target.getUniqueId() + ".rank");
				target_name = target.getName();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				for (String key : CachedPlayers.getConfigurationSection("players").getKeys(false)) {
					if (CachedPlayers.getString("players." + key + ".name").equalsIgnoreCase(t)) {
						group = CachedPlayers.getString("players." + key + ".rank");
						target_name = CachedPlayers.getString("players." + key + ".name");
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (group.length() > 0) {
			if (sender != null) {
				Messages.messagePlayerCheckRank(sender, target_name, group);
			} else {
				Messages.messagePlayerCheckRank(Bukkit.getConsoleSender(), target_name, group);
			}
		} else {
			if (sender != null) {
				Messages.messagePlayerNotFound(sender, t);
			} else {
				Messages.messagePlayerNotFound(Bukkit.getConsoleSender(), t);
			}
		}
		return (group.length() == 0) ? "error" : group;
	}

	public String getGroup(Player player) {
//		File playerFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
//		YamlConfiguration playerYaml = new YamlConfiguration();
//		String group = null;
//		try {
//			playerYaml.load(playerFile);
//			group = playerYaml.getString("players." + player.getUniqueId() + ".rank");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return group;
		return CachedPlayers.getString("players." + player.getUniqueId() + ".rank");
	}

	public String getGroup(String playername) {
//		File playerFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
//		YamlConfiguration playerYaml = new YamlConfiguration();
//		String group = null;
		String uuid = "";
		String group = null;
		if (Bukkit.getServer().getPlayer(playername) != null)
			uuid = Bukkit.getServer().getPlayer(playername).getUniqueId().toString();

		if (uuid.length() == 0) {
			for (String key : CachedPlayers.getConfigurationSection("players").getKeys(false)) {
				if (CachedPlayers.getString("players." + key + ".name").equalsIgnoreCase(playername)) {
					uuid = key;
				}
			}
		} else if (uuid.length() != 0) {
			group = CachedPlayers.getString("players." + uuid + ".rank");
		}
		return group;

//		try {
//			playerYaml.load(playerFile);
//
//			if (uuid.length() == 0) {
//				for (String key : playerYaml.getConfigurationSection("players").getKeys(false)) {
//					if (playerYaml.getString("players." + key + ".name").equalsIgnoreCase(playername)) {
//						uuid = key;
//					}
//				}
//			}
//
//			if (uuid.length() != 0) {
//				group = playerYaml.getString("players." + uuid + ".rank");
//			} else {
//				return "";
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return group;
	}

	public Set<String> getGroups() {
		return CachedRanks.getConfigurationSection("Groups").getKeys(false);
//		ConfigurationSection ranks = null;
//		File rankFile = new File(String.valueOf(PowerRanks.fileLoc) + "Ranks" + ".yml");
//		YamlConfiguration rankYaml = new YamlConfiguration();
//		try {
//			rankYaml.load(rankFile);
//			ranks = rankYaml.getConfigurationSection("Groups");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ranks.getKeys(false);
	}

	public boolean addPermission(String rank, String permission) {
		if (permission.contains("/") || permission.contains(":")) {
			return false;
		}

		try {
			if (!rank.equals("*")) {
				if (CachedRanks.get("Groups." + rank) != null) {
					List<String> list = (List<String>) CachedRanks.getStringList("Groups." + rank + ".permissions");
					if (list == null) {
						list = new ArrayList<String>();
					}
					if (!list.contains(permission)) {
						list.add(permission);
						CachedRanks.set("Groups." + rank + ".permissions", (Object) list);
					}

					this.m.updatePlayersWithRank(this, rank);
					return true;
				}
			} else {
				for (String r : getGroups()) {
					if (CachedRanks.get("Groups." + r) != null) {
						List<String> list = (List<String>) CachedRanks.getStringList("Groups." + r + ".permissions");
						if (!list.contains(permission)) {
							list.add(permission);
							CachedRanks.set("Groups." + r + ".permissions", (Object) list);
							this.m.updatePlayersWithRank(this, r);
						}
					}
				}
				CachedRanks.update();
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
				if (CachedRanks.get("Groups." + rank) != null) {
					List<String> list = (List<String>) CachedRanks.getStringList("Groups." + rank + ".permissions");
					list.remove(permission);
					CachedRanks.set("Groups." + rank + ".permissions", (Object) list);
					this.m.updatePlayersWithRank(this, rank);
					return true;
				}

			} else {
				for (String r : getGroups()) {
					if (CachedRanks.get("Groups." + r) != null) {
						List<String> list = (List<String>) CachedRanks.getStringList("Groups." + r + ".permissions");
						list.remove(permission);
						CachedRanks.set("Groups." + r + ".permissions", (Object) list);
						this.m.updatePlayersWithRank(this, r);
					}
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
			if (CachedRanks.get("Groups." + rank) != null) {
				List<String> list = (List<String>) CachedRanks.getStringList("Groups." + rank + ".inheritance");
				if (!list.contains(inheritance)) {
					list.add(inheritance);
				}
				CachedRanks.set("Groups." + rank + ".inheritance", (Object) list);
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
			if (CachedRanks.get("Groups." + rank) != null) {
				CachedRanks.set("Groups." + rank + ".chat.prefix", (Object) prefix);
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
			if (CachedRanks.get("Groups." + rank) != null) {
				CachedRanks.set("Groups." + rank + ".chat.suffix", (Object) suffix);
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
			if (CachedRanks.get("Groups." + rank) != null) {
				CachedRanks.set("Groups." + rank + ".chat.chatColor", (Object) color);
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
			if (CachedRanks.get("Groups." + rank) != null) {
				CachedRanks.set("Groups." + rank + ".chat.nameColor", (Object) color);
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
			if (CachedRanks.get("Groups." + rank) != null) {
				List<String> list = (List<String>) CachedRanks.getStringList("Groups." + rank + ".inheritance");
				if (list.contains(inheritance)) {
					list.remove(inheritance);
				}
				CachedRanks.set("Groups." + rank + ".inheritance", (Object) list);
				this.m.updatePlayersWithRank(this, rank);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean createRank(String rank) {
		try {
			if (CachedRanks.get("Groups." + rank) == null) {
				CachedRanks.set("Groups." + rank + ".permissions", new ArrayList<String>());
				CachedRanks.set("Groups." + rank + ".inheritance", new ArrayList<String>());
				CachedRanks.set("Groups." + rank + ".build", true);
				CachedRanks.set("Groups." + rank + ".chat.prefix", "[&7" + rank + "&r]");
				CachedRanks.set("Groups." + rank + ".chat.suffix", "");
				CachedRanks.set("Groups." + rank + ".chat.chatColor", "&f");
				CachedRanks.set("Groups." + rank + ".chat.nameColor", "&f");
				CachedRanks.set("Groups." + rank + ".level.promote", "");
				CachedRanks.set("Groups." + rank + ".level.demote", "");
				CachedRanks.set("Groups." + rank + ".economy.buyable", new ArrayList<String>());
				CachedRanks.set("Groups." + rank + ".economy.cost", 0);
				CachedRanks.set("Groups." + rank + ".gui.icon", "stone");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteRank(String rank) {
		if (CachedRanks.getString("Default").equalsIgnoreCase(rank)) {
			return false;
		} else {
			for (String uuid : CachedPlayers.getConfigurationSection("players").getKeys(false)) {
				boolean setup_permissions = false;

				String player_rank = CachedPlayers.getString("players." + uuid + ".rank");

				if (player_rank.equalsIgnoreCase(rank)) {
					CachedPlayers.set("players." + uuid + ".rank", CachedRanks.getString("Default"), false);
					setup_permissions = true;
				}

				if (CachedPlayers.getConfigurationSection("players." + uuid + ".subranks") != null) {
					for (String subrank : CachedPlayers.getConfigurationSection("players." + uuid + ".subranks").getKeys(false)) {
						if (subrank.equalsIgnoreCase(rank)) {
							CachedPlayers.set("players." + uuid + ".subranks." + subrank, null, false);
							setup_permissions = true;
						}
					}
				}

				if (setup_permissions) {
					Player target = Bukkit.getServer().getPlayer(CachedPlayers.getString("players." + uuid + ".name"));

					if (target.isOnline()) {
						this.m.setupPermissions(target);
						this.m.updateTablistName(target);
					}
				}

			}
			try {
				if (CachedRanks.get("Groups." + rank) != null) {
					CachedRanks.set("Groups." + rank, (Object) null);
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean setBuild(String rank, boolean enabled) {
		try {
			if (CachedRanks.get("Groups." + rank) != null) {
				CachedRanks.set("Groups." + rank + ".build", (Object) enabled);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean promote(String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		Player player = Bukkit.getServer().getPlayer(playername);
		if (player != null) {
			try {
				String oldRank = CachedPlayers.getString("players." + player.getUniqueId() + ".rank");
				String rank = CachedPlayers.getString("players." + player.getUniqueId() + ".rank");
				if (CachedRanks.get("Groups." + rank) != null) {
					String rankname = CachedRanks.getString("Groups." + rank + ".level.promote");
					if (CachedRanks.get("Groups." + rankname) != null && rankname.length() > 0) {
						this.setGroup(player, rankname, false);
						if (CachedConfig.getBoolean("announcements.promote.enabled")) {
							Bukkit.broadcastMessage(PowerRanks.chatColor(CachedConfig.getString("announcements.promote.format").replace("[player]", playername).replace("[rank]", rankname).replace("[powerranks_prefix]",
									langYaml.getString("general.prefix").replace("%plugin_name%", PowerRanks.pdf.getName())), true));
						}

						for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
							PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
							prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rank, RankChangeCause.PROMOTE, true);
						}
//						playerYaml.set("players." + player.getUniqueId() + ".rank", (Object) rankname);
//						playerYaml.save(playerFile);
//						CachedPlayers.update();
//						this.m.setupPermissions(player);
//						this.m.updateTablistName(player);
						this.m.updatePlayersWithRank(this, rank);

						return true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {

				boolean offline_player_found = false;

				for (String key : CachedPlayers.getConfigurationSection("players").getKeys(false)) {
					if (CachedPlayers.getString("players." + key + ".name").equalsIgnoreCase(playername)) {
						String oldRank = CachedPlayers.getString("players." + key + ".rank");
						String rankname = CachedRanks.getString("Groups." + CachedPlayers.getString("players." + key + ".rank") + ".level.promote");
						if (rankname.length() == 0)
							return false;

						this.setGroup(player, rankname, false);
						if (CachedConfig.getBoolean("announcements.promote.enabled")) {
							Bukkit.broadcastMessage(PowerRanks.chatColor(CachedConfig.getString("announcements.promote.format").replace("[player]", playername).replace("[rank]", rankname).replace("[powerranks_prefix]",
									langYaml.getString("general.prefix").replace("%plugin_name%", PowerRanks.pdf.getName())), true));
						}

						for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
							PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
							prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rankname, RankChangeCause.PROMOTE, true);
						}
//						playerYaml.set("players." + key + ".rank", (Object) rankname);
//						playerYaml.save(playerFile);
//						CachedPlayers.update();
//						this.m.setupPermissions(player);
//						this.m.updateTablistName(player);

						offline_player_found = true;
						return true;
					}
				}

				if (!offline_player_found) {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean demote(String playername) {
		YamlConfiguration langYaml = PowerRanks.loadLangFile();
		Player player = Bukkit.getServer().getPlayer(playername);
		if (player != null) {
			try {
				String oldRank = CachedPlayers.getString("players." + player.getUniqueId() + ".rank");
				String rank = CachedPlayers.getString("players." + player.getUniqueId() + ".rank");
				if (CachedRanks.get("Groups." + rank) != null) {
					String rankname = CachedRanks.getString("Groups." + rank + ".level.demote");
					if (CachedRanks.get("Groups." + rankname) != null && rankname.length() > 0) {
						this.setGroup(player, rankname, false);
						if (CachedConfig.getBoolean("announcements.demote.enabled")) {
							Bukkit.broadcastMessage(PowerRanks.chatColor(CachedConfig.getString("announcements.demote.format").replace("[player]", playername).replace("[rank]", rankname).replace("[powerranks_prefix]",
									langYaml.getString("general.prefix").replace("%plugin_name%", PowerRanks.pdf.getName())), true));
						}

						for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
							PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
							prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rankname, RankChangeCause.DEMOTE, true);
						}
//						playerYaml.set("players." + player.getUniqueId() + ".rank", (Object) rankname);
//						playerYaml.save(playerFile);
//						CachedPlayers.update();
						this.m.updatePlayersWithRank(this, rank);
						return true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				boolean offline_player_found = false;

				for (String key : CachedPlayers.getConfigurationSection("players").getKeys(false)) {
					if (CachedPlayers.getString("players." + key + ".name").equalsIgnoreCase(playername)) {
						String oldRank = CachedPlayers.getString("players." + key + ".rank");
						String rankname = CachedRanks.getString("Groups." + CachedPlayers.getString("players." + key + ".rank") + ".level.demote");
						if (rankname.length() == 0)
							return false;

						this.setGroup(player, rankname, false);
						if (CachedConfig.getBoolean("announcements.demote.enabled")) {
							Bukkit.broadcastMessage(PowerRanks.chatColor(CachedConfig.getString("announcements.demote.format").replace("[player]", playername).replace("[rank]", rankname).replace("[powerranks_prefix]",
									langYaml.getString("general.prefix").replace("%plugin_name%", PowerRanks.pdf.getName())), true));
						}

						for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
							PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
							prAddon.getValue().onPlayerRankChange(prPlayer, oldRank, rankname, RankChangeCause.PROMOTE, true);
						}
//						playerYaml.set("players." + key + ".rank", (Object) rankname);
//						playerYaml.save(playerFile);
//						CachedPlayers.update();
						offline_player_found = true;
						return true;
					}
				}

				if (!offline_player_found) {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean renameRank(String rank, String to) {
		if (CachedRanks.get("Groups." + rank) != null) {
			List<String> listPermissions = (List<String>) CachedRanks.getStringList("Groups." + to + ".permissions");
			for (String line : CachedRanks.getStringList("Groups." + rank + ".permissions")) {
				listPermissions.add(line);
			}
			CachedRanks.set("Groups." + to + ".permissions", (Object) listPermissions);

			List<String> listInheritance = (List<String>) CachedRanks.getStringList("Groups." + to + ".inheritance");
			for (String line : CachedRanks.getStringList("Groups." + rank + ".inheritance")) {
				listInheritance.add(line);
			}
			CachedRanks.set("Groups." + to + ".inheritance", (Object) listInheritance);

			CachedRanks.set("Groups." + to + ".build", CachedRanks.get("Groups." + rank + ".build"));
			CachedRanks.set("Groups." + to + ".chat.prefix", CachedRanks.get("Groups." + rank + ".chat.prefix"));
			CachedRanks.set("Groups." + to + ".chat.suffix", CachedRanks.get("Groups." + rank + ".chat.suffix"));
			CachedRanks.set("Groups." + to + ".chat.chatColor", CachedRanks.get("Groups." + rank + ".chat.chatColor"));
			CachedRanks.set("Groups." + to + ".chat.nameColor", CachedRanks.get("Groups." + rank + ".chat.nameColor"));
			CachedRanks.set("Groups." + to + ".level.promote", CachedRanks.get("Groups." + rank + ".level.promote"));
			CachedRanks.set("Groups." + to + ".level.demote", CachedRanks.get("Groups." + rank + ".level.demote"));

			List<String> listEconomyBuyable = (List<String>) CachedRanks.getStringList("Groups." + to + ".economy.buyable");
			for (String line : CachedRanks.getStringList("Groups." + rank + ".economy.buyable")) {
				listEconomyBuyable.add(line);
			}
			CachedRanks.set("Groups." + to + ".economy.buyable", (Object) listEconomyBuyable);
			CachedRanks.set("Groups." + to + ".economy.cost", CachedRanks.get("Groups." + rank + ".economy.cost"));
			CachedRanks.set("Groups." + to + ".gui.icon", CachedRanks.get("Groups." + rank + ".gui.icon"));

			ConfigurationSection players = CachedPlayers.getConfigurationSection("players");
			for (String p : players.getKeys(false)) {
				if (CachedPlayers.getString("players." + p + ".rank") != null) {
//					PowerRanks.log.info(playerYaml.getString("players." + p + ".rank") + (playerYaml.getString("players." + p + ".rank").equalsIgnoreCase(rank) ? " Match" : " No Match"));
					if (CachedPlayers.getString("players." + p + ".rank").equalsIgnoreCase(rank)) {
						CachedPlayers.set("players." + p + ".rank", to, false);
					}
				}
			}
			deleteRank(rank);
			return true;
		}
		return false;
	}

	public boolean setDefaultRank(String rankname) {
		if (CachedRanks.get("Groups." + rankname) != null) {
			CachedRanks.set("Default", rankname);
			return true;
		}

		return false;
	}

	public String getRankIgnoreCase(String rankname) {
		String rank = rankname;

		try {
			ConfigurationSection ranks = CachedRanks.getConfigurationSection("Groups");
			for (String r : ranks.getKeys(false)) {
				if (r.equalsIgnoreCase(rankname)) {
					rank = r;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rank;
	}

	public List<String> getPermissions(String rank) {
		List<String> permissions = new ArrayList<String>();
		permissions = CachedRanks.getStringList("Groups." + rank + ".permissions");
		return permissions;
	}

	public List<String> getInheritances(String rank) {
		List<String> inheritances = new ArrayList<String>();
		inheritances = CachedRanks.getStringList("Groups." + rank + ".inheritance");
		return inheritances;
	}

	public Set<String> getCachedPlayers() {
		ConfigurationSection players = null;
		try {
			players = CachedPlayers.getConfigurationSection("players");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return players.getKeys(false);
	}

	public String getPrefix(Player player) {
		return getPrefix(getGroup(player));
	}

	public String getPrefix(String rank) {
		String prefix = "";
		rank = this.getRankIgnoreCase(rank);

		prefix = (CachedRanks.getString("Groups." + rank + ".chat.prefix") != null) ? CachedRanks.getString("Groups." + rank + ".chat.prefix") : "";

		return prefix;
	}

	public String getSuffix(Player player) {
		return getSuffix(getGroup(player));
	}

	public String getSuffix(String rank) {
		String suffix = "";
		rank = this.getRankIgnoreCase(rank);

		suffix = (CachedRanks.getString("Groups." + rank + ".chat.suffix") != null) ? CachedRanks.getString("Groups." + rank + ".chat.suffix") : "";

		return suffix;
	}

	public String getChatColor(Player player) {
		String color = "";
		String rank = getGroup(player);

		color = (CachedRanks.getString("Groups." + rank + ".chat.chatColor") != null) ? CachedRanks.getString("Groups." + rank + ".chat.chatColor") : "";

		return color;
	}

	public String getNameColor(Player player) {
		String color = "";
		String rank = getGroup(player);

		color = (CachedRanks.getString("Groups." + rank + ".chat.nameColor") != null) ? CachedRanks.getString("Groups." + rank + ".chat.nameColor") : "";

		return color;
	}

	public String getDefaultRanks() {
		return CachedRanks.getString("Default");
	}

	public boolean addBuyableRank(String rankname, String rankname2) {
		rankname = getRankIgnoreCase(rankname);
		rankname2 = getRankIgnoreCase(rankname2);
		try {
			if (CachedRanks.get("Groups." + rankname) != null) {
				List<String> list = (List<String>) CachedRanks.getStringList("Groups." + rankname + ".economy.buyable");
				if (!list.contains(rankname2)) {
					list.add(rankname2);
				}
				CachedRanks.set("Groups." + rankname + ".economy.buyable", (Object) list);
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
			if (CachedRanks.get("Groups." + rankname) != null) {
				List<String> list = (List<String>) CachedRanks.getStringList("Groups." + rankname + ".economy.buyable");
				if (list.contains(rankname2)) {
					list.remove(rankname2);
				}
				CachedRanks.set("Groups." + rankname + ".economy.buyable", (Object) list);
				this.m.updatePlayersWithRank(this, rankname);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<String> getBuyableRanks(String rank) {
		List<String> ranks = new ArrayList<String>();
		ranks = CachedRanks.getStringList("Groups." + rank + ".economy.buyable");
		return ranks;
	}

	public boolean setBuyCost(String rankname, String cost) {
		rankname = getRankIgnoreCase(rankname);
		if (!cost.chars().anyMatch(Character::isLetter)) {
			try {
				if (CachedRanks.get("Groups." + rankname) != null) {
					CachedRanks.set("Groups." + rankname + ".economy.cost", Integer.parseInt(cost));
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public int getRankCost(String rankname) {
		int cost = 0;
		try {
			if (CachedRanks.get("Groups." + rankname) != null) {
				cost = CachedRanks.getInt("Groups." + rankname + ".economy.cost");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cost;
	}

	public boolean addPlayerPermission(String target_player_name, String permission) {
		if (permission.contains("/") || permission.contains(":")) {
			return false;
		}

		Player target_player = Bukkit.getServer().getPlayer(target_player_name);

		if (target_player != null) {
			try {
				if (CachedPlayers.get("players." + target_player.getUniqueId()) != null) {
					List<String> list = (List<String>) CachedPlayers.getStringList("players." + target_player.getUniqueId() + ".permissions");
					if (!list.contains(permission)) {
						list.add(permission);
						CachedPlayers.set("players." + target_player.getUniqueId() + ".permissions", (Object) list, false);
					}
					this.m.setupPermissions(target_player);

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
				for (String key : CachedPlayers.getConfigurationSection("players").getKeys(false)) {
					if (CachedPlayers.getString("players." + key + ".name").equalsIgnoreCase(target_player_name)) {
						uuid = key;
					}
				}

				if (uuid.length() > 0) {
					if (CachedPlayers.get("players." + uuid) != null) {
						List<String> list = (List<String>) CachedPlayers.getStringList("players." + uuid + ".permissions");
						if (!list.contains(permission)) {
							list.add(permission);
							CachedPlayers.set("players." + uuid + ".permissions", (Object) list, false);
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
				if (CachedPlayers.get("players." + target_player.getUniqueId()) != null) {
					List<String> list = (List<String>) CachedPlayers.getStringList("players." + target_player.getUniqueId() + ".permissions");
					if (list.contains(permission)) {
						list.remove(permission);
						CachedPlayers.set("players." + target_player.getUniqueId() + ".permissions", (Object) list, false);
					}
					this.m.setupPermissions(target_player);

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
				for (String key : CachedPlayers.getConfigurationSection("players").getKeys(false)) {
					if (CachedPlayers.getString("players." + key + ".name").equalsIgnoreCase(target_player_name)) {
						uuid = key;
					}
				}

				if (uuid.length() > 0) {
					if (CachedPlayers.get("players." + uuid) != null) {
						List<String> list = (List<String>) CachedPlayers.getStringList("players." + uuid + ".permissions");
						if (list.contains(permission)) {
							list.remove(permission);
							CachedPlayers.set("players." + uuid + ".permissions", (Object) list, false);
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

	public boolean addSubrank(String playername, String subrank) {
		Player player = Bukkit.getServer().getPlayer(playername);
		if (player == null)
			return false;

		String uuid = player.getUniqueId().toString();

		try {
			ArrayList<String> default_worlds = new ArrayList<String>();
			default_worlds.add("All");

			if (CachedPlayers.get("players." + uuid + ".subranks." + getRankIgnoreCase(subrank)) == null) {
				CachedPlayers.set("players." + uuid + ".subranks." + getRankIgnoreCase(subrank) + ".use_prefix", true, false);
				CachedPlayers.set("players." + uuid + ".subranks." + getRankIgnoreCase(subrank) + ".use_suffix", true, false);
				CachedPlayers.set("players." + uuid + ".subranks." + getRankIgnoreCase(subrank) + ".use_permissions", true, false);
				CachedPlayers.set("players." + uuid + ".subranks." + getRankIgnoreCase(subrank) + ".worlds", default_worlds, false);
			} else {
				return false;
			}

			this.m.setupPermissions(player);
			this.m.updateTablistName(player);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean removeSubrank(String playername, String subrank) {
		Player player = Bukkit.getServer().getPlayer(playername);
		if (player == null)
			return false;

		String uuid = player.getUniqueId().toString();

		try {

			if (CachedPlayers.get("players." + uuid + ".subranks." + getRankIgnoreCase(subrank)) != null) {
				CachedPlayers.set("players." + uuid + ".subranks." + getRankIgnoreCase(subrank), null, false);
			} else {
				return false;
			}

			this.m.setupPermissions(player);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public List<String> getSubranks(String playername) {
		List<String> ranks = new ArrayList<String>();
		Player player = Bukkit.getServer().getPlayer(playername);
		if (player == null)
			return ranks;

		String uuid = player.getUniqueId().toString();

		try {
			if (CachedPlayers.getConfigurationSection("players." + uuid + ".subranks") != null) {
				ConfigurationSection subranks = CachedPlayers.getConfigurationSection("players." + uuid + ".subranks");
				for (String r : subranks.getKeys(false)) {
					ranks.add(getRankIgnoreCase(r));
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return ranks;
	}

	public boolean changeSubrankField(String playername, String subrank, String field, boolean value) {
		Player player = Bukkit.getServer().getPlayer(playername);
		if (player == null)
			return false;

		String uuid = player.getUniqueId().toString();

		try {

			if (CachedPlayers.get("players." + uuid + ".subranks." + getRankIgnoreCase(subrank)) != null) {
				CachedPlayers.set("players." + uuid + ".subranks." + getRankIgnoreCase(subrank) + "." + field, value, false);
			} else {
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public List<String> getPlayerPermissions(String playername) {
		List<String> list = new ArrayList<String>();

		Player player = Bukkit.getServer().getPlayer(playername);
		if (player == null)
			return list;

		String uuid = player.getUniqueId().toString();

		try {

			if (CachedPlayers.get("players." + uuid + ".permissions") != null) {
				list = (List<String>) CachedPlayers.getStringList("players." + uuid + ".permissions");
			} else {
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public String getSubrankprefixes(Player player) {
		String values = "";
		String uuid = player.getUniqueId().toString();
		try {

			if (CachedPlayers.get("players." + uuid + ".subranks") != null) {
				ConfigurationSection subranks = CachedPlayers.getConfigurationSection("players." + uuid + ".subranks");

				for (String r : subranks.getKeys(false)) {
					if (CachedPlayers.getBoolean("players." + uuid + ".subranks." + r + ".use_prefix")) {
						values += ChatColor.RESET
								+ (CachedRanks.getString("Groups." + r + ".chat.prefix") != null && CachedRanks.getString("Groups." + r + ".chat.prefix").length() > 0 ? CachedRanks.getString("Groups." + r + ".chat.prefix") + " " : "");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return values;
	}

	public String getSubranksuffixes(Player player) {
		String values = "";
		String uuid = player.getUniqueId().toString();
		try {

			if (CachedPlayers.get("players." + uuid + ".subranks") != null) {
				ConfigurationSection subranks = CachedPlayers.getConfigurationSection("players." + uuid + ".subranks");

				for (String r : subranks.getKeys(false)) {
					if (CachedPlayers.getBoolean("players." + uuid + ".subranks." + r + ".use_suffix")) {
						values += ChatColor.RESET
								+ (CachedRanks.getString("Groups." + r + ".chat.suffix") != null && CachedRanks.getString("Groups." + r + ".chat.suffix").length() > 0 ? CachedRanks.getString("Groups." + r + ".chat.suffix") + " " : "");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return values;
	}

	public boolean createUserTag(String tag, String format) {

		try {

			if (CachedRanks.contains("Usertags")) {
				boolean tagExists = false;
				if (CachedRanks.getConfigurationSection("Usertags") != null) {
					try {
						ConfigurationSection tags = CachedRanks.getConfigurationSection("Usertags");
						for (String key : tags.getKeys(false)) {
							if (key.equalsIgnoreCase(tag)) {
								tagExists = true;
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					CachedRanks.set("Usertags", null);
					CachedRanks.set("Usertags." + tag, format);
				}

				if (!tagExists) {
					CachedRanks.set("Usertags." + tag, format);
					return true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean editUserTag(String tag, String format) {
		try {

			if (CachedRanks.contains("Usertags")) {
				try {
					ConfigurationSection tags = CachedRanks.getConfigurationSection("Usertags");
					boolean tagExists = false;
					for (String key : tags.getKeys(false)) {
						if (key.equalsIgnoreCase(tag)) {
							tagExists = true;
							break;
						}
					}

					if (tagExists) {
						CachedRanks.set("Usertags." + tag, format);
						return true;
					}
				} catch (Exception e) {
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removeUserTag(String tag) {
		try {

			if (CachedRanks.contains("Usertags")) {
				try {
					ConfigurationSection tags = CachedRanks.getConfigurationSection("Usertags");
					boolean tagExists = false;
					for (String key : tags.getKeys(false)) {
						if (key.equalsIgnoreCase(tag)) {
							tagExists = true;
							break;
						}
					}

					if (tagExists) {
						CachedRanks.set("Usertags." + tag, null);
						return true;
					}
				} catch (Exception e) {
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setUserTag(String playername, String tag) {
		Player player = Bukkit.getServer().getPlayer(playername);
		if (player == null)
			return false;

		String uuid = player.getUniqueId().toString();

		try {
			if (CachedRanks.contains("Usertags")) {
				try {
					ConfigurationSection tags = CachedRanks.getConfigurationSection("Usertags");
					boolean tagExists = false;
					for (String key : tags.getKeys(false)) {
						if (key.equalsIgnoreCase(tag)) {
							tagExists = true;
							break;
						}
					}

					if (tagExists) {
						CachedPlayers.set("players." + uuid + ".usertag", tag, false);
						this.m.updateTablistName(player);
						return true;
					}
				} catch (Exception e) {
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Set<String> getUserTags() {
		Set<String> tags = new HashSet<String>();

		try {

			if (CachedRanks.getConfigurationSection("Usertags") != null) {
				try {
					ConfigurationSection tmp_tags = CachedRanks.getConfigurationSection("Usertags");
					tags = tmp_tags.getKeys(false);
				} catch (Exception e) {
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tags;
	}

	public String getUserTagValue(String usertag) {
		String value = "";

		try {
			if (CachedRanks.getConfigurationSection("Usertags") != null) {
				if (CachedRanks.contains("Usertags." + usertag)) {
					value = CachedRanks.getString("Usertags." + usertag);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	public String getUserTagValue(Player player) {
		String usertag = CachedPlayers.getString("players." + player.getUniqueId() + ".usertag");
		if (usertag.length() > 0) {
			return getUserTagValue(usertag);
		} else {
			return "";
		}
	}

	public boolean clearUserTag(String playername) {
		Player player = Bukkit.getServer().getPlayer(playername);
		if (player == null)
			return false;

		String uuid = player.getUniqueId().toString();

		try {

			CachedPlayers.set("players." + uuid + ".usertag", "", false);
			this.m.updateTablistName(player);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setPromoteRank(String rank, String promote_rank) {
		return setRanksConfigFieldString(getRankIgnoreCase(rank), "level.promote", promote_rank);
	}

	public boolean setDemoteRank(String rank, String promote_rank) {
		return setRanksConfigFieldString(getRankIgnoreCase(rank), "level.demote", promote_rank);
	}

	public boolean clearPromoteRank(String rank) {
		return setRanksConfigFieldString(getRankIgnoreCase(rank), "level.promote", "");
	}

	public boolean clearDemoteRank(String rank) {
		return setRanksConfigFieldString(getRankIgnoreCase(rank), "level.demote", "");
	}

	public ArrayList<String> getPlayerNames() {
		ArrayList<String> player_names = new ArrayList<String>();

//		File playersFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
//		YamlConfiguration playersYaml = new YamlConfiguration();
//		try {
//			playersYaml.load(playersFile);
//			ConfigurationSection players_section = playersYaml.getConfigurationSection("players");
		ConfigurationSection players_section = CachedPlayers.getConfigurationSection("players");
		for (String key : players_section.getKeys(false)) {
//				player_names.add(playersYaml.getString("players." + key + ".name"));
			player_names.add(CachedPlayers.getString("players." + key + ".name"));
		}

//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		return player_names;
	}

	public boolean addToSubrankList(String playername, String subrank, String string, String worldname) {
		Player player = Bukkit.getServer().getPlayer(playername);
		if (player == null)
			return false;

		String uuid = player.getUniqueId().toString();

		try {

			List<String> list = (List<String>) CachedPlayers.getStringList("players." + uuid + ".subranks." + getRankIgnoreCase(subrank) + ".worlds");
			if (!list.contains(worldname)) {
				list.add(worldname);
				if (list.contains("All")) {
					list.remove("All");
				}
				CachedPlayers.set("players." + uuid + ".subranks." + getRankIgnoreCase(subrank) + ".worlds", list, false);
			} else {
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean removeFromSubrankList(String playername, String subrank, String string, String worldname) {
		Player player = Bukkit.getServer().getPlayer(playername);
		if (player == null)
			return false;

		String uuid = player.getUniqueId().toString();

		try {
			List<String> list = (List<String>) CachedPlayers.getStringList("players." + uuid + ".subranks." + getRankIgnoreCase(subrank) + ".worlds");
			if (list.contains(worldname)) {
				list.remove(worldname);
				if (list.size() == 0) {
					list.add("All");
				}
				CachedPlayers.set("players." + uuid + ".subranks." + getRankIgnoreCase(subrank) + ".worlds", list, false);
			} else {
				return false;
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}