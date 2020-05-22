package nl.svenar.PowerRanks.Commands;

import java.util.Set;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

import nl.svenar.PowerRanks.Main;
import nl.svenar.PowerRanks.Data.Users;

public class Cmd implements CommandExecutor {
	Main m;

	public Cmd(Main m) {
		this.m = m;
	}

	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		final Users s = new Users(this.m);
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("powerranks") || cmd.getName().equalsIgnoreCase("pr")) {
				if (args.length == 0) {
					this.m.messageNoArgs(player);
				} else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
					if (sender.hasPermission("powerranks.cmd.help")) {
						this.m.helpMenu(player);
					} else {
						this.m.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (args.length != 2) {
						this.m.messageCommandUsageReload(player);
					} else if (sender.hasPermission("powerranks.cmd.reload")) {
						if (args[1].equalsIgnoreCase("config")) {
							this.m.messageCommandReloadConfig(player);
							this.m.reloadConfig();
							this.m.messageCommandReloadConfigDone(player);
						} else if (args[1].equalsIgnoreCase("plugin")) {
							this.m.messageCommandReloadPlugin(player);
							final PluginManager plg = Bukkit.getPluginManager();
							final Plugin plgname = plg.getPlugin(this.m.pdf.getName());
							plg.disablePlugin(plgname);
							plg.enablePlugin(plgname);
							this.m.messageCommandReloadPluginDone(player);
						} else if (args[1].equalsIgnoreCase("all")) {
							this.m.messageCommandReloadPlugin(player);
							final PluginManager plg = Bukkit.getPluginManager();
							final Plugin plgname = plg.getPlugin(this.m.pdf.getName());
							plg.disablePlugin(plgname);
							plg.enablePlugin(plgname);
							this.m.messageCommandReloadPluginDone(player);
							this.m.messageCommandReloadConfig(player);
							this.m.reloadConfig();
							this.m.messageCommandReloadConfigDone(player);
						} else {
							this.m.messageCommandUsageReload(player);
						}
					} else {
						this.m.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("set")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							s.setGroup((Player) sender, args[1], args[2]);
						} else {
							this.m.messageCommandUsageSet(player);
						}
					} else {
						this.m.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("setown")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							s.setGroup((Player) sender, sender.getName(), args[1]);
						} else {
							this.m.messageCommandUsageSetown(player);
						}
					} else {
						this.m.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("list")) {
					if (sender.hasPermission("powerranks.cmd.list")) {
						Set<String> ranks = s.getGroups();
						sender.sendMessage("Ranks(" + ranks.size() + "):");
						for (String rank : ranks) {
							sender.sendMessage(rank);
						}
					} else {
						this.m.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("check")) {
					if (sender.hasPermission("powerranks.cmd.check")) {
						if (args.length == 2) {
							s.getGroup(((Player) sender).getName(), args[1]);
						} else if (args.length == 1) {
							s.getGroup(((Player) sender).getName(), ((Player) sender).getName());
						} else {
							player.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr check [username]");
						}
					} else {
						this.m.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("addperm")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String permission = args[2];
							final boolean result = s.addPermission(rank2, permission);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Permission '" + permission + "' added to rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to add permission. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr addperm <rank> <permission>");
						}
					} else {
						this.m.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("delperm")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String permission = args[2];
							final boolean result = s.removePermission(rank2, permission);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Permission '" + permission + "' removed from rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to remove permission. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr delperm <rank> <permission>");
						}
					} else {
						this.m.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("addinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String inheritance = args[2];
							final boolean result = s.addInheritance(rank2, inheritance);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Inheritance '" + inheritance + "' added to rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to add inheritance. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr addinheritance <rank> <inheritance>");
						}
					}
				} else if (args[0].equalsIgnoreCase("delinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String inheritance = args[2];
							final boolean result = s.removeInheritance(rank2, inheritance);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Inheritance '" + inheritance + "' removed from rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to remove inheritance. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr delinheritance <rank> <inheritance>");
						}
					}
				} else if (args[0].equalsIgnoreCase("setprefix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String prefix = args[2];
							final boolean result = s.setPrefix(rank2, prefix);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Changed prefix to '" + prefix + "' on rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to set prefix. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr setprefix <rank> <prefix>");
						}
					}
				} else if (args[0].equalsIgnoreCase("setsuffix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String suffix = args[2];
							final boolean result = s.setSuffix(rank2, suffix);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Changed suffix to '" + suffix + "' on rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to set suffix. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr setsuffix <rank> <suffix>");
						}
					}
				} else if (args[0].equalsIgnoreCase("setchatcolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String color = args[2];
							final boolean result = s.setChatColor(rank2, color);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Changed chat color to '" + color + "' on rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to set chat color. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr setchatcolor <rank> <color>");
						}
					}
				} else if (args[0].equalsIgnoreCase("setnamecolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String color = args[2];
							final boolean result = s.setNameColor(rank2, color);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Changed name color to '" + color + "' on rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to set name color. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr setnamecolor <rank> <color>");
						}
					}
				} else if (args[0].equalsIgnoreCase("createrank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.createRank(rank2);
							if (success) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Rank " + rank2 + " created");
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Could not create rank " + rank2);
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr createrank <rankName>");
						}
					}
				} else if (args[0].equalsIgnoreCase("deleterank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.deleteRank(rank2);
							if (success) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Rank " + rank2 + " deleted");
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Could not delete rank " + rank2);
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr deleterank <rankName>");
						}
					}
				} else if (args[0].equalsIgnoreCase("enablebuild")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.setBuild(rank2, true);
							if (success) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Building enabled on rank " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Could not enable building on rank " + rank2);
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr enablebuild <rankName>");
						}
					}
				} else if (args[0].equalsIgnoreCase("disablebuild")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.setBuild(rank2, false);
							if (success) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Building disabled on rank " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Could not disable building on rank " + rank2);
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr disablebuild <rankName>");
						}
					}
				} else if (args[0].equalsIgnoreCase("promote")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String playername = args[1];
							final boolean success = s.promote(playername);
							if (success) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Player promoted");
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Could not promote player");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr promote <playerName>");
						}
					}
				} else if (args[0].equalsIgnoreCase("demote") && sender.hasPermission("powerranks.cmd.set")) {
					if (args.length == 2) {
						final String playername = args[1];
						final boolean success = s.demote(playername);
						if (success) {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Player demoted");
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Could not demote player");
						}
					} else {
						sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr demote <playerName>");
					}
				} else if (args[0].equalsIgnoreCase("forceupdateconfigversion")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						this.m.forceUpdateConfigVersions();
						sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Configuration version updated!");
					} else {
						this.m.noPermission(player);
					}
				}
			}
		} else if (sender instanceof ConsoleCommandSender) {
			final ConsoleCommandSender console = (ConsoleCommandSender) sender;
			if (cmd.getName().equalsIgnoreCase("powerranks") || cmd.getName().equalsIgnoreCase("pr")) {
				if (args.length == 0) {
					this.m.messageNoArgs(console);
				} else if (args[0].equalsIgnoreCase("reload")) {
					final PluginManager plg = Bukkit.getPluginManager();
					final Plugin plgname = plg.getPlugin(this.m.pdf.getName());
					plg.disablePlugin(plgname);
					plg.enablePlugin(plgname);
				} else if (args[0].equalsIgnoreCase("help")) {
					this.m.helpMenu(console);
				} else if (args[0].equalsIgnoreCase("set")) {
					if (args.length == 3) {
						s.setGroup(null, args[1], args[2]);
					} else {
						console.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr set <username> <rank>");
					}
				} else if (args[0].equalsIgnoreCase("check")) {
					if (args.length == 2) {
						s.getGroup(null, args[1]);
					} else {
						console.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr check <username>");
					}
				} else if (args[0].equalsIgnoreCase("addperm")) {
					if (args.length == 3) {
						final String rank2 = args[1];
						final String permission = args[2];
						final boolean result = s.addPermission(rank2, permission);
						if (result) {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Permission '" + permission + "' added to rank: " + rank2);
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to add permission. (Rank must be Case Sensitive)");
						}
					} else {
						sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr addperm <rank> <permission>");
					}
				} else if (args[0].equalsIgnoreCase("delperm")) {
					if (args.length == 3) {
						final String rank2 = args[1];
						final String permission = args[2];
						final boolean result = s.removePermission(rank2, permission);
						if (result) {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Permission '" + permission + "' removed from rank: " + rank2);
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to remove permission. (Rank must be Case Sensitive)");
						}
					} else {
						sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr delperm <rank> <permission>");
					}
				} else if (args[0].equalsIgnoreCase("addinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String inheritance = args[2];
							final boolean result = s.addInheritance(rank2, inheritance);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Inheritance '" + inheritance + "' added to rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to add inheritance. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr addinheritance <rank> <inheritance>");
						}
					}
				} else if (args[0].equalsIgnoreCase("delinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String inheritance = args[2];
							final boolean result = s.removeInheritance(rank2, inheritance);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Inheritance '" + inheritance + "' removed from rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to remove inheritance. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr delinheritance <rank> <inheritance>");
						}
					}
				} else if (args[0].equalsIgnoreCase("setprefix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String prefix = args[2];
							final boolean result = s.setPrefix(rank2, prefix);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Changed prefix to '" + prefix + "' on rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to set prefix. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr setprefix <rank> <prefix>");
						}
					}
				} else if (args[0].equalsIgnoreCase("setsuffix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String suffix = args[2];
							final boolean result = s.setSuffix(rank2, suffix);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Changed suffix to '" + suffix + "' on rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to set suffix. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr setsuffix <rank> <suffix>");
						}
					}
				} else if (args[0].equalsIgnoreCase("setchatcolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String color = args[2];
							final boolean result = s.setChatColor(rank2, color);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Changed chat color to '" + color + "' on rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to set chat color. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr setchatcolor <rank> <color>");
						}
					}
				} else if (args[0].equalsIgnoreCase("setnamecolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String color = args[2];
							final boolean result = s.setNameColor(rank2, color);
							if (result) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Changed name color to '" + color + "' on rank: " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Failed to set name color. (Rank must be Case Sensitive)");
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr setnamecolor <rank> <color>");
						}
					}
				} else if (args[0].equalsIgnoreCase("createrank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.createRank(rank2);
							if (success) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Rank " + rank2 + " created");
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Could not create rank " + rank2);
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr createrank <rankName>");
						}
					}
				} else if (args[0].equalsIgnoreCase("deleterank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.deleteRank(rank2);
							if (success) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Rank " + rank2 + " deleted");
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Could not delete rank " + rank2);
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr deleterank <rankName>");
						}
					}
				} else if (args[0].equalsIgnoreCase("enablebuild")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.setBuild(rank2, true);
							if (success) {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Building enabled on rank " + rank2);
							} else {
								sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Could not enable building on rank " + rank2);
							}
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr enablebuild <rankName>");
						}
					}
				} else if (args[0].equalsIgnoreCase("disablebuild") && sender.hasPermission("powerranks.cmd.set")) {
					if (args.length == 2) {
						final String rank2 = args[1];
						final boolean success = s.setBuild(rank2, false);
						if (success) {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Building disabled on rank " + rank2);
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Could not disable building on rank " + rank2);
						}
					} else {
						sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr disablebuild <rankName>");
					}
				} else if (args[0].equalsIgnoreCase("promote")) {
					if (args.length == 2) {
						final String playername = args[1];
						final boolean success = s.promote(playername);
						if (success) {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Player promoted");
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Could not promote player");
						}
					} else {
						sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr promote <playerName>");
					}
				} else if (args[0].equalsIgnoreCase("demote")) {
					if (args.length == 2) {
						final String playername = args[1];
						final boolean success = s.demote(playername);
						if (success) {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Player demoted");
						} else {
							sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "Could not demote player");
						}
					} else {
						sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.RED + "/pr demote <playerName>");
					}
				} else if (args[0].equalsIgnoreCase("forceupdateconfigversion")) {
					this.m.forceUpdateConfigVersions();
					sender.sendMessage(String.valueOf(this.m.plp) + ChatColor.GREEN + "Configuration version updated!");
				}
			}
		}
		return false;
	}
}