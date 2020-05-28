package nl.svenar.PowerRanks.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.Messages;
import nl.svenar.PowerRanks.PowerRanks;
import org.bukkit.event.Listener;

public class Users implements Listener {
	PowerRanks m;

	public Users(PowerRanks m) {
		this.m = m;
	}

	public void setGroup(Player player, String t, String rank) {
		if (player != null) {
			if (player.hasPermission("powerranks.cmd.set")) {
				Player target = Bukkit.getServer().getPlayer(t);

				if (target != null) {
					File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
					File playerFile = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
					YamlConfiguration rankYaml = new YamlConfiguration();
					YamlConfiguration playerYaml = new YamlConfiguration();
					try {
						rankYaml.load(rankFile);
						if (rankYaml.get("Groups." + rank) != null) {
							playerYaml.load(playerFile);
							playerYaml.set("players." + target.getUniqueId() + ".rank", (Object) rank);
							playerYaml.save(playerFile);
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
					File rankFile2 = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
					File playerFile2 = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
					YamlConfiguration rankYaml2 = new YamlConfiguration();
					YamlConfiguration playerYaml2 = new YamlConfiguration();
					try {
						rankYaml2.load(rankFile2);
						if (rankYaml2.get("Groups." + rank) != null) {
							playerYaml2.load(playerFile2);

							boolean offline_player_found = false;

							for (String key : playerYaml2.getConfigurationSection("players").getKeys(false)) {
								if (playerYaml2.getString("players." + key + ".name").equalsIgnoreCase(t)) {
									playerYaml2.set("players." + key + ".rank", (Object) rank);
									playerYaml2.save(playerFile2);
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
					} catch (IOException | InvalidConfigurationException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			ConsoleCommandSender console = Bukkit.getConsoleSender();
			Player target2 = Bukkit.getServer().getPlayer(t);

			if (target2 != null) {
				File rankFile2 = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
				File playerFile2 = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
				YamlConfiguration rankYaml2 = new YamlConfiguration();
				YamlConfiguration playerYaml2 = new YamlConfiguration();
				try {
					rankYaml2.load(rankFile2);
					if (rankYaml2.get("Groups." + rank) != null) {
						playerYaml2.load(playerFile2);
						playerYaml2.set("players." + target2.getUniqueId() + ".rank", (Object) rank);
						playerYaml2.save(playerFile2);
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
				File rankFile2 = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
				File playerFile2 = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
				YamlConfiguration rankYaml2 = new YamlConfiguration();
				YamlConfiguration playerYaml2 = new YamlConfiguration();
				try {
					rankYaml2.load(rankFile2);
					if (rankYaml2.get("Groups." + rank) != null) {
						playerYaml2.load(playerFile2);

						boolean offline_player_found = false;

						for (String key : playerYaml2.getConfigurationSection("players").getKeys(false)) {
							if (playerYaml2.getString("players." + key + ".name").equalsIgnoreCase(t)) {
								playerYaml2.set("players." + key + ".rank", (Object) rank);
								playerYaml2.save(playerFile2);
								Messages.messageSetRankSuccessSender(console, t, rank);

								offline_player_found = true;
							}
						}

						if (!offline_player_found) {
							Messages.messagePlayerNotFound(console, t);
						}
					} else {
						Messages.messageGroupNotFound(console, rank);
					}
				} catch (IOException | InvalidConfigurationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean setGroup(Player player, String rank) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		File playerFile = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		YamlConfiguration playerYaml = new YamlConfiguration();
		boolean success = false;
		try {
			rankYaml.load(rankFile);
			if (rankYaml.get("Groups." + rank) != null) {
				playerYaml.load(playerFile);
				playerYaml.set("players." + player.getUniqueId() + ".rank", (Object) rank);
				playerYaml.save(playerFile);
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
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			value = rankYaml.getString("Groups." + rank + "." + field);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public boolean setRanksConfigFieldString(String rank, String field, String new_value) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			rankYaml.set("Groups." + rank + "." + field, new_value);
			rankYaml.save(rankFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean getRanksConfigFieldBoolean(String rank, String field) {
		boolean value = false;
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			value = rankYaml.getBoolean("Groups." + rank + "." + field);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public boolean setRanksConfigFieldBoolean(String rank, String field, boolean new_value) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			rankYaml.set("Groups." + rank + "." + field, new_value);
			rankYaml.save(rankFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getGroup(String plr, String t) {
		Player sender = (plr == null || plr == "API") ? null : Bukkit.getServer().getPlayer(plr);
		Player target = Bukkit.getServer().getPlayer(t);
		File playerFile = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
		YamlConfiguration playerYaml = new YamlConfiguration();
		String group = "";
		if (target != null) {
			try {
				playerYaml.load(playerFile);
				group = playerYaml.getString("players." + target.getUniqueId() + ".rank");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				playerYaml.load(playerFile);
				for (String key : playerYaml.getConfigurationSection("players").getKeys(false)) {
					if (playerYaml.getString("players." + key + ".name").equalsIgnoreCase(t)) {
						group = playerYaml.getString("players." + key + ".rank");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (group.length() > 0) {
			if (sender != null) {
				Messages.messagePlayerCheckRank(sender, target.getName(), group);
			} else {
				Messages.messagePlayerCheckRank(Bukkit.getConsoleSender(), target.getName(), group);
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
		File playerFile = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
		YamlConfiguration playerYaml = new YamlConfiguration();
		String group = null;
		try {
			playerYaml.load(playerFile);
			group = playerYaml.getString("players." + player.getUniqueId() + ".rank");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return group;
	}

	public Set<String> getGroups() {
		ConfigurationSection ranks = null;
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			ranks = rankYaml.getConfigurationSection("Groups");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ranks.getKeys(false);
	}

	public boolean addPermission(String rank, String permission) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			if (rankYaml.get("Groups." + rank) != null) {
				List<String> list = (List<String>) rankYaml.getStringList("Groups." + rank + ".permissions");
				list.add(permission);
				rankYaml.set("Groups." + rank + ".permissions", (Object) list);
				rankYaml.save(rankFile);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removePermission(String rank, String permission) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			if (rankYaml.get("Groups." + rank) != null) {
				List<String> list = (List<String>) rankYaml.getStringList("Groups." + rank + ".permissions");
				list.remove(permission);
				rankYaml.set("Groups." + rank + ".permissions", (Object) list);
				rankYaml.save(rankFile);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean addInheritance(String rank, String inheritance) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			if (rankYaml.get("Groups." + rank) != null) {
				List<String> list = (List<String>) rankYaml.getStringList("Groups." + rank + ".inheritance");
				if (!list.contains(inheritance)) {
					list.add(inheritance);
				}
				rankYaml.set("Groups." + rank + ".inheritance", (Object) list);
				rankYaml.save(rankFile);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setPrefix(String rank, String prefix) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			if (rankYaml.get("Groups." + rank) != null) {
				rankYaml.set("Groups." + rank + ".chat.prefix", (Object) prefix);
				rankYaml.save(rankFile);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setSuffix(String rank, String suffix) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			if (rankYaml.get("Groups." + rank) != null) {
				rankYaml.set("Groups." + rank + ".chat.suffix", (Object) suffix);
				rankYaml.save(rankFile);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setChatColor(String rank, String color) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			if (rankYaml.get("Groups." + rank) != null) {
				rankYaml.set("Groups." + rank + ".chat.chatColor", (Object) color);
				rankYaml.save(rankFile);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setNameColor(String rank, String color) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			if (rankYaml.get("Groups." + rank) != null) {
				rankYaml.set("Groups." + rank + ".chat.nameColor", (Object) color);
				rankYaml.save(rankFile);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean removeInheritance(String rank, String inheritance) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			if (rankYaml.get("Groups." + rank) != null) {
				List<String> list = (List<String>) rankYaml.getStringList("Groups." + rank + ".inheritance");
				if (list.contains(inheritance)) {
					list.remove(inheritance);
				}
				rankYaml.set("Groups." + rank + ".inheritance", (Object) list);
				rankYaml.save(rankFile);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean createRank(String rank) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			if (rankYaml.get("Groups." + rank) == null) {
				rankYaml.set("Groups." + rank + ".permissions", (Object) "[]");
				rankYaml.set("Groups." + rank + ".inheritance", (Object) "[]");
				rankYaml.set("Groups." + rank + ".build", (Object) true);
				rankYaml.set("Groups." + rank + ".chat.prefix", (Object) ("[&7" + rank + "&r]"));
				rankYaml.set("Groups." + rank + ".chat.suffix", (Object) "");
				rankYaml.set("Groups." + rank + ".chat.chatColor", (Object) "&f");
				rankYaml.set("Groups." + rank + ".chat.nameColor", (Object) "&f");
				rankYaml.set("Groups." + rank + ".level.promote", (Object) "");
				rankYaml.set("Groups." + rank + ".level.demote", (Object) "");
				rankYaml.save(rankFile);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteRank(String rank) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			if (rankYaml.get("Groups." + rank) != null) {
				rankYaml.set("Groups." + rank, (Object) null);
				rankYaml.save(rankFile);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setBuild(String rank, boolean enabled) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			if (rankYaml.get("Groups." + rank) != null) {
				rankYaml.set("Groups." + rank + ".build", (Object) enabled);
				rankYaml.save(rankFile);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean promote(String playername) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		File playerFile = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		YamlConfiguration playerYaml = new YamlConfiguration();
		Player player = Bukkit.getServer().getPlayer(playername);
		if (player != null) {
			try {
				rankYaml.load(rankFile);
				playerYaml.load(playerFile);
				String rank = playerYaml.getString("players." + player.getUniqueId() + ".rank");
				if (rankYaml.get("Groups." + rank) != null) {
					String rankname = rankYaml.getString("Groups." + rank + ".level.promote");
					if (rankYaml.get("Groups." + rankname) != null && rankname.length() > 0) {
						this.setGroup(player, rankname);
//						playerYaml.set("players." + player.getUniqueId() + ".rank", (Object) rankname);
//						playerYaml.save(playerFile);
//						this.m.setupPermissions(player);
//						this.m.updateTablistName(player);
						return true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				rankYaml.load(rankFile);
				playerYaml.load(playerFile);

				boolean offline_player_found = false;

				for (String key : playerYaml.getConfigurationSection("players").getKeys(false)) {
					if (playerYaml.getString("players." + key + ".name").equalsIgnoreCase(playername)) {
						String rankname = rankYaml.getString("Groups." + playerYaml.getString("players." + key + ".rank") + ".level.promote");
						if (rankname.length() == 0)
							return false;

						this.setGroup(player, rankname);
//						playerYaml.set("players." + key + ".rank", (Object) rankname);
//						playerYaml.save(playerFile);
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
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		File playerFile = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		YamlConfiguration playerYaml = new YamlConfiguration();
		Player player = Bukkit.getServer().getPlayer(playername);
		if (player != null) {
			try {
				rankYaml.load(rankFile);
				playerYaml.load(playerFile);
				String rank = playerYaml.getString("players." + player.getUniqueId() + ".rank");
				if (rankYaml.get("Groups." + rank) != null) {
					String rankname = rankYaml.getString("Groups." + rank + ".level.demote");
					if (rankYaml.get("Groups." + rankname) != null && rankname.length() > 0) {
						this.setGroup(player, rankname);
//						playerYaml.set("players." + player.getUniqueId() + ".rank", (Object) rankname);
//						playerYaml.save(playerFile);
						return true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				rankYaml.load(rankFile);
				playerYaml.load(playerFile);

				boolean offline_player_found = false;

				for (String key : playerYaml.getConfigurationSection("players").getKeys(false)) {
					if (playerYaml.getString("players." + key + ".name").equalsIgnoreCase(playername)) {
						String rankname = rankYaml.getString("Groups." + playerYaml.getString("players." + key + ".rank") + ".level.demote");
						if (rankname.length() == 0)
							return false;

						this.setGroup(player, rankname);
//						playerYaml.set("players." + key + ".rank", (Object) rankname);
//						playerYaml.save(playerFile);

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
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		File playerFile = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		YamlConfiguration playerYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
			playerYaml.load(playerFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		if (rankYaml.get("Groups." + rank) != null) {
			List<String> listPermissions = (List<String>) rankYaml.getStringList("Groups." + to + ".permissions");
			for (String line : rankYaml.getStringList("Groups." + rank + ".permissions")) {
				listPermissions.add(line);
			}
			rankYaml.set("Groups." + to + ".permissions", (Object) listPermissions);

			List<String> listInheritance = (List<String>) rankYaml.getStringList("Groups." + to + ".inheritance");
			for (String line : rankYaml.getStringList("Groups." + rank + ".inheritance")) {
				listInheritance.add(line);
			}
			rankYaml.set("Groups." + to + ".inheritance", (Object) listInheritance);

			rankYaml.set("Groups." + to + ".build", rankYaml.get("Groups." + rank + ".build"));
			rankYaml.set("Groups." + to + ".chat.prefix", rankYaml.get("Groups." + rank + ".chat.prefix"));
			rankYaml.set("Groups." + to + ".chat.suffix", rankYaml.get("Groups." + rank + ".chat.suffix"));
			rankYaml.set("Groups." + to + ".chat.chatColor", rankYaml.get("Groups." + rank + ".chat.chatColor"));
			rankYaml.set("Groups." + to + ".chat.nameColor", rankYaml.get("Groups." + rank + ".chat.nameColor"));
			rankYaml.set("Groups." + to + ".level.promote", rankYaml.get("Groups." + rank + ".level.promote"));
			rankYaml.set("Groups." + to + ".level.demote", rankYaml.get("Groups." + rank + ".level.demote"));

			ConfigurationSection players = playerYaml.getConfigurationSection("players");
			for (String p : players.getKeys(false)) {
				if (playerYaml.getString("players." + p + ".rank") != null) {
					PowerRanks.log.info(playerYaml.getString("players." + p + ".rank") + (playerYaml.getString("players." + p + ".rank").equalsIgnoreCase(rank) ? " Match" : " No Match"));
					if (playerYaml.getString("players." + p + ".rank").equalsIgnoreCase(rank)) {
						playerYaml.set("players." + p + ".rank", to);
					}
				}
			}
			try {
				rankYaml.save(rankFile);
				playerYaml.save(playerFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			deleteRank(rank);
			return true;
		}
		return false;
	}

	public boolean setDefaultRank(String rankname) {
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		if (rankYaml.get("Groups." + rankname) != null) {
			rankYaml.set("Default", rankname);
			try {
				rankYaml.save(rankFile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return true;
		}

		return false;
	}

	public String getRankIgnoreCase(String rankname) {
		String rank = rankname;

		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		ConfigurationSection ranks = rankYaml.getConfigurationSection("Groups");
		for (String r : ranks.getKeys(false)) {
			if (r.equalsIgnoreCase(rankname)) {
				rank = r;
				break;
			}
		}

		return rank;
	}

	public List<String> getPermissions(String rank) {
		List<String> permissions = new ArrayList<String>();
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		permissions = rankYaml.getStringList("Groups." + rank + ".permissions");
		return permissions;
	}

	public List<String> getInheritances(String rank) {
		List<String> inheritances = new ArrayList<String>();
		File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		YamlConfiguration rankYaml = new YamlConfiguration();
		try {
			rankYaml.load(rankFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		inheritances = rankYaml.getStringList("Groups." + rank + ".inheritance");
		return inheritances;
	}
}