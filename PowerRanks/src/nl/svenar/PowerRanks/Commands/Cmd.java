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
							this.m.messageCommandUsageCheck(player);
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
								this.m.messageCommandPermissionAdded(player, permission, rank2);
							} else {
								this.m.messageGroupNotFound(player, rank2);
							}
						} else {
							this.m.messageCommandUsageAddperm(player);
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
								this.m.messageCommandPermissionRemoved(player, permission, rank2);
							} else {
								this.m.messageGroupNotFound(player, rank2);
							}
						} else {
							this.m.messageCommandUsageDelperm(player);
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
								this.m.messageCommandInheritanceAdded(player, inheritance, rank2);
							} else {
								this.m.messageGroupNotFound(player, rank2);
							}
						} else {
							this.m.messageCommandUsageAddInheritance(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("delinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String inheritance = args[2];
							final boolean result = s.removeInheritance(rank2, inheritance);
							if (result) {
								this.m.messageCommandInheritanceRemoved(player, inheritance, rank2);
							} else {
								this.m.messageGroupNotFound(player, rank2);
							}
						} else {
							this.m.messageCommandUsageRemoveInheritance(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("setprefix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String prefix = args[2];
							final boolean result = s.setPrefix(rank2, prefix);
							if (result) {
								this.m.messageCommandSetPrefix(player, prefix, rank2);
							} else {
								this.m.messageGroupNotFound(player, rank2);
							}
						} else {
							this.m.messageCommandUsageSetPrefix(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("setsuffix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String suffix = args[2];
							final boolean result = s.setSuffix(rank2, suffix);
							if (result) {
								this.m.messageCommandSetSuffix(player, suffix, rank2);
							} else {
								this.m.messageGroupNotFound(player, rank2);
							}
						} else {
							this.m.messageCommandUsageSetSuffix(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("setchatcolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String color = args[2];
							final boolean result = s.setChatColor(rank2, color);
							if (result) {
								this.m.messageCommandSetChatColor(player, color, rank2);
							} else {
								this.m.messageGroupNotFound(player, rank2);
							}
						} else {
							this.m.messageCommandUsageSetChatColor(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("setnamecolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String color = args[2];
							final boolean result = s.setNameColor(rank2, color);
							if (result) {
								this.m.messageCommandSetNameColor(player, color, rank2);
							} else {
								this.m.messageGroupNotFound(player, rank2);
							}
						} else {
							this.m.messageCommandUsageSetNameColor(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("createrank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.createRank(rank2);
							if (success) {
								this.m.messageCommandCreateRankSuccess(player, rank2);
							} else {
								this.m.messageCommandCreateRankError(player, rank2);
							}
						} else {
							this.m.messageCommandUsageCreateRank(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("deleterank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.deleteRank(rank2);
							if (success) {
								this.m.messageCommandDeleteRankSuccess(player, rank2);
							} else {
								this.m.messageCommandDeleteRankError(player, rank2);
							}
						} else {
							this.m.messageCommandUsageDeleteRank(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("enablebuild")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.setBuild(rank2, true);
							if (success) {
								this.m.messageCommandBuildEnabled(player, rank2);
							} else {
								this.m.messageGroupNotFound(player, rank2);
							}
						} else {
							this.m.messageCommandUsageEnableBuild(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("disablebuild")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.setBuild(rank2, false);
							if (success) {
								this.m.messageCommandBuildDisabled(player, rank2);
							} else {
								this.m.messageGroupNotFound(player, rank2);
							}
						} else {
							this.m.messageCommandUsageDisableBuild(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("promote")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String playername = args[1];
							final boolean success = s.promote(playername);
							if (success) {
								this.m.messageCommandPromoteSuccess(player, playername);
							} else {
								this.m.messageCommandPromoteError(player, playername);
							}
						} else {
							this.m.messageCommandUsagePromote(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("demote") && sender.hasPermission("powerranks.cmd.set")) {
					if (args.length == 2) {
						final String playername = args[1];
						final boolean success = s.demote(playername);
						if (success) {
							this.m.messageCommandDemoteSuccess(player, playername);
						} else {
							this.m.messageCommandDemoteError(player, playername);
						}
					} else {
						this.m.messageCommandUsageDemote(player);
					}
				} else if (args[0].equalsIgnoreCase("forceupdateconfigversion")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						this.m.forceUpdateConfigVersions();
						this.m.messageConfigVersionUpdated(player);
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
						this.m.messageCommandUsageSet(console);
					}
				} else if (args[0].equalsIgnoreCase("check")) {
					if (args.length == 2) {
						s.getGroup(null, args[1]);
					} else {
						this.m.messageCommandUsageCheck(console);
					}
				} else if (args[0].equalsIgnoreCase("addperm")) {
					if (args.length == 3) {
						final String rank2 = args[1];
						final String permission = args[2];
						final boolean result = s.addPermission(rank2, permission);
						if (result) {
							this.m.messageCommandPermissionAdded(console, permission, rank2);
						} else {
							this.m.messageGroupNotFound(console, rank2);
						}
					} else {
						this.m.messageCommandUsageAddperm(console);
					}
				} else if (args[0].equalsIgnoreCase("delperm")) {
					if (args.length == 3) {
						final String rank2 = args[1];
						final String permission = args[2];
						final boolean result = s.removePermission(rank2, permission);
						if (result) {
							this.m.messageCommandPermissionRemoved(console, permission, rank2);
						} else {
							this.m.messageGroupNotFound(console, rank2);
						}
					} else {
						this.m.messageCommandUsageDelperm(console);
					}
				} else if (args[0].equalsIgnoreCase("addinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String inheritance = args[2];
							final boolean result = s.addInheritance(rank2, inheritance);
							if (result) {
								this.m.messageCommandInheritanceAdded(console, inheritance, rank2);
							} else {
								this.m.messageGroupNotFound(console, rank2);
							}
						} else {
							this.m.messageCommandUsageAddInheritance(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("delinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String inheritance = args[2];
							final boolean result = s.removeInheritance(rank2, inheritance);
							if (result) {
								this.m.messageCommandInheritanceRemoved(console, inheritance, rank2);
							} else {
								this.m.messageGroupNotFound(console, rank2);
							}
						} else {
							this.m.messageCommandUsageRemoveInheritance(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("setprefix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String prefix = args[2];
							final boolean result = s.setPrefix(rank2, prefix);
							if (result) {
								this.m.messageCommandSetPrefix(console, prefix, rank2);
							} else {
								this.m.messageGroupNotFound(console, rank2);
							}
						} else {
							this.m.messageCommandUsageSetPrefix(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("setsuffix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String suffix = args[2];
							final boolean result = s.setSuffix(rank2, suffix);
							if (result) {
								this.m.messageCommandSetSuffix(console, suffix, rank2);
							} else {
								this.m.messageGroupNotFound(console, rank2);
							}
						} else {
							this.m.messageCommandUsageSetSuffix(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("setchatcolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String color = args[2];
							final boolean result = s.setChatColor(rank2, color);
							if (result) {
								this.m.messageCommandSetChatColor(console, color, rank2);
							} else {
								this.m.messageGroupNotFound(console, rank2);
							}
						} else {
							this.m.messageCommandUsageSetChatColor(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("setnamecolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1];
							final String color = args[2];
							final boolean result = s.setNameColor(rank2, color);
							if (result) {
								this.m.messageCommandSetNameColor(console, color, rank2);
							} else {
								this.m.messageGroupNotFound(console, rank2);
							}
						} else {
							this.m.messageCommandUsageSetNameColor(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("createrank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.createRank(rank2);
							if (success) {
								this.m.messageCommandCreateRankSuccess(console, rank2);
							} else {
								this.m.messageCommandCreateRankError(console, rank2);
							}
						} else {
							this.m.messageCommandUsageCreateRank(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("deleterank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.deleteRank(rank2);
							if (success) {
								this.m.messageCommandDeleteRankSuccess(console, rank2);
							} else {
								this.m.messageCommandDeleteRankError(console, rank2);
							}
						} else {
							this.m.messageCommandUsageDeleteRank(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("enablebuild")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = args[1];
							final boolean success = s.setBuild(rank2, true);
							if (success) {
								this.m.messageCommandBuildEnabled(console, rank2);
							} else {
								this.m.messageGroupNotFound(console, rank2);
							}
						} else {
							this.m.messageCommandUsageEnableBuild(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("disablebuild") && sender.hasPermission("powerranks.cmd.set")) {
					if (args.length == 2) {
						final String rank2 = args[1];
						final boolean success = s.setBuild(rank2, false);
						if (success) {
							this.m.messageCommandBuildDisabled(console, rank2);
						} else {
							this.m.messageGroupNotFound(console, rank2);
						}
					} else {
						this.m.messageCommandUsageDisableBuild(console);
					}
				} else if (args[0].equalsIgnoreCase("promote")) {
					if (args.length == 2) {
						final String playername = args[1];
						final boolean success = s.promote(playername);
						if (success) {
							this.m.messageCommandPromoteSuccess(console, playername);
						} else {
							this.m.messageCommandPromoteError(console, playername);
						}
					} else {
						this.m.messageCommandUsagePromote(console);
					}
				} else if (args[0].equalsIgnoreCase("demote")) {
					if (args.length == 2) {
						final String playername = args[1];
						final boolean success = s.demote(playername);
						if (success) {
							this.m.messageCommandDemoteSuccess(console, playername);
						} else {
							this.m.messageCommandDemoteError(console, playername);
						}
					} else {
						this.m.messageCommandUsageDemote(console);
					}
				} else if (args[0].equalsIgnoreCase("forceupdateconfigversion")) {
					this.m.forceUpdateConfigVersions();
					this.m.messageConfigVersionUpdated(console);
				}
			}
		}
		return false;
	}
}