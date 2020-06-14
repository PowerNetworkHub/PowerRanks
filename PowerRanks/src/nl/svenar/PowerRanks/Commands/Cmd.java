package nl.svenar.PowerRanks.Commands;

import java.util.List;
import java.util.Set;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_15_R1.command.CraftBlockCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.PowerRanksGUI;
import nl.svenar.PowerRanks.Data.Users;

public class Cmd implements CommandExecutor {
	PowerRanks m;

	public Cmd(PowerRanks m) {
		this.m = m;
	}

	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		final Users s = new Users(this.m);
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("powerranks") || cmd.getName().equalsIgnoreCase("pr")) {
				if (args.length == 0) {
					Messages.messageNoArgs(player);
				} else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
					if (sender.hasPermission("powerranks.cmd.help")) {
						Messages.helpMenu(player);
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (args.length != 2) {
						Messages.messageCommandUsageReload(player);
					} else if (sender.hasPermission("powerranks.cmd.reload")) {
						if (args[1].equalsIgnoreCase("config")) {
							Messages.messageCommandReloadConfig(player);
							this.m.reloadConfig();
							Messages.messageCommandReloadConfigDone(player);
						} else if (args[1].equalsIgnoreCase("plugin")) {
							Messages.messageCommandReloadPlugin(player);
							final PluginManager plg = Bukkit.getPluginManager();
							final Plugin plgname = plg.getPlugin(PowerRanks.pdf.getName());
							plg.disablePlugin(plgname);
							plg.enablePlugin(plgname);
							Messages.messageCommandReloadPluginDone(player);
						} else if (args[1].equalsIgnoreCase("all")) {
							Messages.messageCommandReloadPlugin(player);
							final PluginManager plg = Bukkit.getPluginManager();
							final Plugin plgname = plg.getPlugin(PowerRanks.pdf.getName());
							plg.disablePlugin(plgname);
							plg.enablePlugin(plgname);
							Messages.messageCommandReloadPluginDone(player);
							Messages.messageCommandReloadConfig(player);
							this.m.reloadConfig();
							Messages.messageCommandReloadConfigDone(player);
						} else {
							Messages.messageCommandUsageReload(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("set")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							s.setGroup((Player) sender, args[1], s.getRankIgnoreCase(args[2]));
						} else {
							Messages.messageCommandUsageSet(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("setown")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							s.setGroup((Player) sender, sender.getName(), s.getRankIgnoreCase(args[1]));
						} else {
							Messages.messageCommandUsageSetown(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("listranks")) {
					if (sender.hasPermission("powerranks.cmd.list")) {
						Set<String> ranks = s.getGroups();
						sender.sendMessage("Ranks(" + ranks.size() + "):");
						for (String rank : ranks) {
							sender.sendMessage(rank);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("listpermissions")) {
					if (sender.hasPermission("powerranks.cmd.list")) {
						if (args.length == 2) {
							if (s.getGroups().contains(s.getRankIgnoreCase(args[1]))) {
								List<String> permissions = s.getPermissions(s.getRankIgnoreCase(args[1]));
								sender.sendMessage("Permissions of " + s.getRankIgnoreCase(args[1]) + "(" + permissions.size() + "):");
								for (String permission : permissions) {
									sender.sendMessage(permission);
								}
							} else {
								Messages.messageGroupNotFound(player, args[1]);
							}
						} else {
							Messages.messageCommandUsageListPermissions(player);
						}

					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("check")) {
					if (sender.hasPermission("powerranks.cmd.check")) {
						if (args.length == 2) {
							s.getGroup(((Player) sender).getName(), args[1]);
						} else if (args.length == 1) {
							s.getGroup(((Player) sender).getName(), ((Player) sender).getName());
						} else {
							Messages.messageCommandUsageCheck(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("addperm")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1].equals("*") ? args[1] : s.getRankIgnoreCase(args[1]);
							final String permission = args[2];
							final boolean result = s.addPermission(rank2, permission);
							if (result) {
								if (rank2.equals("*")) {
									Messages.messageCommandPermissionAddedToAllRanks(player, permission);
								} else {
									Messages.messageCommandPermissionAdded(player, permission, rank2);
								}
							} else {
								Messages.messageErrorAddingPermission(player, rank2, permission);
							}
						} else {
							Messages.messageCommandUsageAddperm(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("delperm")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = args[1].equals("*") ? args[1] : s.getRankIgnoreCase(args[1]);
							final String permission = args[2];
							final boolean result = s.removePermission(rank2, permission);
							if (result) {
								if (rank2 == "*") {
									Messages.messageCommandPermissionRemovedFromAllRanks(player, permission);
								} else {
									Messages.messageCommandPermissionRemoved(player, permission, rank2);
								}
							} else {
								Messages.messageGroupNotFound(player, rank2);
							}
						} else {
							Messages.messageCommandUsageDelperm(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("addinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String inheritance = args[2];
							final boolean result = s.addInheritance(rank2, inheritance);
							if (result) {
								Messages.messageCommandInheritanceAdded(player, inheritance, rank2);
							} else {
								Messages.messageGroupNotFound(player, rank2);
							}
						} else {
							Messages.messageCommandUsageAddInheritance(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("delinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String inheritance = args[2];
							final boolean result = s.removeInheritance(rank2, inheritance);
							if (result) {
								Messages.messageCommandInheritanceRemoved(player, inheritance, rank2);
							} else {
								Messages.messageGroupNotFound(player, rank2);
							}
						} else {
							Messages.messageCommandUsageRemoveInheritance(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("setprefix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String prefix = "";
							final boolean result = s.setPrefix(rank2, prefix);
							if (result) {
								Messages.messageCommandSetPrefix(player, prefix, rank2);
							} else {
								Messages.messageGroupNotFound(player, rank2);
							}
						} else if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String prefix = args[2];
							final boolean result = s.setPrefix(rank2, prefix);
							if (result) {
								Messages.messageCommandSetPrefix(player, prefix, rank2);
							} else {
								Messages.messageGroupNotFound(player, rank2);
							}
						} else {
							Messages.messageCommandUsageSetPrefix(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("setsuffix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String suffix = "";
							final boolean result = s.setSuffix(rank2, suffix);
							if (result) {
								Messages.messageCommandSetSuffix(player, suffix, rank2);
							} else {
								Messages.messageGroupNotFound(player, rank2);
							}
						} else if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String suffix = args[2];
							final boolean result = s.setSuffix(rank2, suffix);
							if (result) {
								Messages.messageCommandSetSuffix(player, suffix, rank2);
							} else {
								Messages.messageGroupNotFound(player, rank2);
							}
						} else {
							Messages.messageCommandUsageSetSuffix(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("setchatcolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String color = args[2];
							final boolean result = s.setChatColor(rank2, color);
							if (result) {
								Messages.messageCommandSetChatColor(player, color, rank2);
							} else {
								Messages.messageGroupNotFound(player, rank2);
							}
						} else {
							Messages.messageCommandUsageSetChatColor(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("setnamecolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String color = args[2];
							final boolean result = s.setNameColor(rank2, color);
							if (result) {
								Messages.messageCommandSetNameColor(player, color, rank2);
							} else {
								Messages.messageGroupNotFound(player, rank2);
							}
						} else {
							Messages.messageCommandUsageSetNameColor(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("createrank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final boolean success = s.createRank(rank2);
							if (success) {
								Messages.messageCommandCreateRankSuccess(player, rank2);
							} else {
								Messages.messageCommandCreateRankError(player, rank2);
							}
						} else {
							Messages.messageCommandUsageCreateRank(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("deleterank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final boolean success = s.deleteRank(rank2);
							if (success) {
								Messages.messageCommandDeleteRankSuccess(player, rank2);
							} else {
								Messages.messageCommandDeleteRankError(player, rank2);
							}
						} else {
							Messages.messageCommandUsageDeleteRank(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("enablebuild")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final boolean success = s.setBuild(rank2, true);
							if (success) {
								Messages.messageCommandBuildEnabled(player, rank2);
							} else {
								Messages.messageGroupNotFound(player, rank2);
							}
						} else {
							Messages.messageCommandUsageEnableBuild(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("disablebuild")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final boolean success = s.setBuild(rank2, false);
							if (success) {
								Messages.messageCommandBuildDisabled(player, rank2);
							} else {
								Messages.messageGroupNotFound(player, rank2);
							}
						} else {
							Messages.messageCommandUsageDisableBuild(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("promote")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String playername = args[1];
							final boolean success = s.promote(playername);
							if (success) {
								Messages.messageCommandPromoteSuccess(player, playername);
							} else {
								Messages.messageCommandPromoteError(player, playername);
							}
						} else {
							Messages.messageCommandUsagePromote(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("demote")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String playername = args[1];
							final boolean success = s.demote(playername);
							if (success) {
								Messages.messageCommandDemoteSuccess(player, playername);
							} else {
								Messages.messageCommandDemoteError(player, playername);
							}
						} else {
							Messages.messageCommandUsageDemote(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("renamerank")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String from = s.getRankIgnoreCase(args[1]);
							final String to = args[2];
							final boolean success = s.renameRank(from, to);
							if (success) {
								Messages.messageCommandRenameRankSuccess(player, from);
							} else {
								Messages.messageCommandRenameRankError(player, from);
							}
						} else {
							Messages.messageCommandUsageDemote(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("setdefaultrank")) {
					if (args.length == 2) {
						final String rankname = s.getRankIgnoreCase(args[1]);
						final boolean success = s.setDefaultRank(rankname);
						if (success) {
							Messages.messageCommandSetDefaultRankSuccess(player, rankname);
						} else {
							Messages.messageCommandSetDefaultRankError(player, rankname);
						}
					} else {
						Messages.messageCommandUsageDemote(player);
					}
				} else if (args[0].equalsIgnoreCase("forceupdateconfigversion")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						this.m.forceUpdateConfigVersions();
						Messages.messageConfigVersionUpdated(player);
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("gui")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						PowerRanksGUI.openPowerRanksGUI(player, PowerRanksGUI.MAIN_GUI_PAGE.MAIN, 0, "");
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("rankup")) {
					if (sender.hasPermission("powerranks.cmd.rankup")) {
						if (PowerRanks.getVaultEconomy() != null)
							PowerRanksGUI.openPowerRanksRankupGUI(player, 0);
						else
							Messages.messageBuyRankNotAvailable(player);
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("stats")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						Messages.messageStats(player);
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("addbuyablerank")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 3) {
							final String rankname = s.getRankIgnoreCase(args[1]);
							final String rankname2 = s.getRankIgnoreCase(args[2]);
							final boolean success = s.addBuyableRank(rankname, rankname2);
							if (success) {
								Messages.messageCommandAddbuyablerankSuccess(player, rankname, rankname2);
							} else {
								Messages.messageCommandAddbuyablerankError(player, rankname, rankname2);
							}
						} else {
							Messages.messageCommandUsageAddbuyablerank(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("delbuyablerank")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 3) {
							final String rankname = s.getRankIgnoreCase(args[1]);
							final String rankname2 = s.getRankIgnoreCase(args[2]);
							final boolean success = s.delBuyableRank(rankname, rankname2);
							if (success) {
								Messages.messageCommanddelbuyablerankkSuccess(player, rankname, rankname2);
							} else {
								Messages.messageCommandDelbuyablerankError(player, rankname, rankname2);
							}
						} else {
							Messages.messageCommandUsageDelbuyablerank(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("setcost")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 3) {
							final String rankname = s.getRankIgnoreCase(args[1]);
							final String cost = s.getRankIgnoreCase(args[2]);
							final boolean success = s.setBuyCost(rankname, cost);
							if (success) {
								Messages.messageCommandSetcostSuccess(player, rankname, cost);
							} else {
								Messages.messageCommandSetcostError(player, rankname, cost);
							}
						} else {
							Messages.messageCommandUsageSetcost(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("addplayerperm")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String target_player = args[1];
							final String permission = args[2];
							final boolean result = s.addPlayerPermission(target_player, permission);
							if (result) {
								Messages.messageCommandPlayerPermissionAdded(player, permission, target_player);
							} else {
								Messages.messageErrorAddingPlayerPermission(player, target_player, permission);
							}
						} else {
							Messages.messageCommandUsageAddplayerperm(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("delplayerperm")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String target_player = args[1];
							final String permission = args[2];
							final boolean result = s.delPlayerPermission(target_player, permission);
							if (result) {
								Messages.messageCommandPlayerPermissionRemoved(player, permission, target_player);
							} else {
								Messages.messageErrorRemovingPlayerPermission(player, target_player, permission);
							}
						} else {
							Messages.messageCommandUsageDelplayerperm(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else {
					Messages.unknownCommand(player);
				}
			}
		} else if (sender instanceof ConsoleCommandSender) {
			final ConsoleCommandSender console = (ConsoleCommandSender) sender;
			if (cmd.getName().equalsIgnoreCase("powerranks") || cmd.getName().equalsIgnoreCase("pr")) {
				if (args.length == 0) {
					Messages.messageNoArgs(console);
				} else if (args[0].equalsIgnoreCase("reload")) {
					final PluginManager plg = Bukkit.getPluginManager();
					final Plugin plgname = plg.getPlugin(PowerRanks.pdf.getName());
					plg.disablePlugin(plgname);
					plg.enablePlugin(plgname);
				} else if (args[0].equalsIgnoreCase("help")) {
					Messages.helpMenu(console);
				} else if (args[0].equalsIgnoreCase("set")) {
					if (args.length == 3) {
						s.setGroup(null, args[1], s.getRankIgnoreCase(args[2]));
					} else {
						Messages.messageCommandUsageSet(console);
					}
				} else if (args[0].equalsIgnoreCase("listranks")) {
					Set<String> ranks = s.getGroups();
					console.sendMessage("Ranks(" + ranks.size() + "):");
					for (String rank : ranks) {
						console.sendMessage(rank);
					}
				} else if (args[0].equalsIgnoreCase("listpermissions")) {
					if (args.length == 2) {
						if (s.getGroups().contains(s.getRankIgnoreCase(args[1]))) {
							List<String> permissions = s.getPermissions(s.getRankIgnoreCase(args[1]));
							sender.sendMessage("Permissions of " + s.getRankIgnoreCase(args[1]) + "(" + permissions.size() + "):");
							for (String permission : permissions) {
								sender.sendMessage(permission);
							}
						} else {
							Messages.messageGroupNotFound(console, args[1]);
						}
					} else {
						Messages.messageCommandUsageListPermissions(console);
					}

				} else if (args[0].equalsIgnoreCase("check")) {
					if (args.length == 2) {
						s.getGroup(null, args[1]);
					} else {
						Messages.messageCommandUsageCheck(console);
					}
				} else if (args[0].equalsIgnoreCase("addperm")) {
					if (args.length == 3) {
						final String rank2 = args[1].equals("*") ? args[1] : s.getRankIgnoreCase(args[1]);
						final String permission = args[2];
						final boolean result = s.addPermission(rank2, permission);
						if (result) {
							if (rank2.equals("*")) {
								Messages.messageCommandPermissionAddedToAllRanks(console, permission);
							} else {
								Messages.messageCommandPermissionAdded(console, permission, rank2);
							}
						} else {
							Messages.messageErrorAddingPermission(console, rank2, permission);
						}
					} else {
						Messages.messageCommandUsageAddperm(console);
					}
				} else if (args[0].equalsIgnoreCase("delperm")) {
					if (args.length == 3) {
						final String rank2 = args[1].equals("*") ? args[1] : s.getRankIgnoreCase(args[1]);
						final String permission = args[2];
						final boolean result = s.removePermission(rank2, permission);
						if (result) {
							if (rank2 == "*") {
								Messages.messageCommandPermissionRemovedFromAllRanks(console, permission);
							} else {
								Messages.messageCommandPermissionRemoved(console, permission, rank2);
							}
						} else {
							Messages.messageGroupNotFound(console, rank2);
						}
					} else {
						Messages.messageCommandUsageDelperm(console);
					}
				} else if (args[0].equalsIgnoreCase("addinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String inheritance = args[2];
							final boolean result = s.addInheritance(rank2, inheritance);
							if (result) {
								Messages.messageCommandInheritanceAdded(console, inheritance, rank2);
							} else {
								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
							Messages.messageCommandUsageAddInheritance(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("delinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String inheritance = args[2];
							final boolean result = s.removeInheritance(rank2, inheritance);
							if (result) {
								Messages.messageCommandInheritanceRemoved(console, inheritance, rank2);
							} else {
								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
							Messages.messageCommandUsageRemoveInheritance(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("setprefix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String prefix = "";
							final boolean result = s.setPrefix(rank2, prefix);
							if (result) {
								Messages.messageCommandSetPrefix(console, prefix, rank2);
							} else {
								Messages.messageGroupNotFound(console, rank2);
							}
						} else if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String prefix = args[2];
							final boolean result = s.setPrefix(rank2, prefix);
							if (result) {
								Messages.messageCommandSetPrefix(console, prefix, rank2);
							} else {
								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
							Messages.messageCommandUsageSetPrefix(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("setsuffix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String suffix = "";
							final boolean result = s.setSuffix(rank2, suffix);
							if (result) {
								Messages.messageCommandSetSuffix(console, suffix, rank2);
							} else {
								Messages.messageGroupNotFound(console, rank2);
							}
						} else if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String suffix = args[2];
							final boolean result = s.setSuffix(rank2, suffix);
							if (result) {
								Messages.messageCommandSetSuffix(console, suffix, rank2);
							} else {
								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
							Messages.messageCommandUsageSetSuffix(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("setchatcolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String color = args[2];
							final boolean result = s.setChatColor(rank2, color);
							if (result) {
								Messages.messageCommandSetChatColor(console, color, rank2);
							} else {
								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
							Messages.messageCommandUsageSetChatColor(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("setnamecolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String color = args[2];
							final boolean result = s.setNameColor(rank2, color);
							if (result) {
								Messages.messageCommandSetNameColor(console, color, rank2);
							} else {
								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
							Messages.messageCommandUsageSetNameColor(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("createrank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final boolean success = s.createRank(rank2);
							if (success) {
								Messages.messageCommandCreateRankSuccess(console, rank2);
							} else {
								Messages.messageCommandCreateRankError(console, rank2);
							}
						} else {
							Messages.messageCommandUsageCreateRank(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("deleterank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final boolean success = s.deleteRank(rank2);
							if (success) {
								Messages.messageCommandDeleteRankSuccess(console, rank2);
							} else {
								Messages.messageCommandDeleteRankError(console, rank2);
							}
						} else {
							Messages.messageCommandUsageDeleteRank(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("enablebuild")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final boolean success = s.setBuild(rank2, true);
							if (success) {
								Messages.messageCommandBuildEnabled(console, rank2);
							} else {
								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
							Messages.messageCommandUsageEnableBuild(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("disablebuild") && sender.hasPermission("powerranks.cmd.set")) {
					if (args.length == 2) {
						final String rank2 = s.getRankIgnoreCase(args[1]);
						final boolean success = s.setBuild(rank2, false);
						if (success) {
							Messages.messageCommandBuildDisabled(console, rank2);
						} else {
							Messages.messageGroupNotFound(console, rank2);
						}
					} else {
						Messages.messageCommandUsageDisableBuild(console);
					}
				} else if (args[0].equalsIgnoreCase("promote")) {
					if (args.length == 2) {
						final String playername = args[1];
						final boolean success = s.promote(playername);
						if (success) {
							Messages.messageCommandPromoteSuccess(console, playername);
						} else {
							Messages.messageCommandPromoteError(console, playername);
						}
					} else {
						Messages.messageCommandUsagePromote(console);
					}
				} else if (args[0].equalsIgnoreCase("demote")) {
					if (args.length == 2) {
						final String playername = args[1];
						final boolean success = s.demote(playername);
						if (success) {
							Messages.messageCommandDemoteSuccess(console, playername);
						} else {
							Messages.messageCommandDemoteError(console, playername);
						}
					} else {
						Messages.messageCommandUsageDemote(console);
					}
				} else if (args[0].equalsIgnoreCase("renamerank")) {
					if (args.length == 3) {
						final String from = s.getRankIgnoreCase(args[1]);
						final String to = args[2];
						final boolean success = s.renameRank(from, to);
						if (success) {
							Messages.messageCommandRenameRankSuccess(console, from);
						} else {
							Messages.messageCommandRenameRankError(console, from);
						}
					} else {
						Messages.messageCommandUsageDemote(console);
					}
				} else if (args[0].equalsIgnoreCase("setdefaultrank")) {
					if (args.length == 2) {
						final String rankname = s.getRankIgnoreCase(args[1]);
						final boolean success = s.setDefaultRank(rankname);
						if (success) {
							Messages.messageCommandSetDefaultRankSuccess(console, rankname);
						} else {
							Messages.messageCommandSetDefaultRankError(console, rankname);
						}
					} else {
						Messages.messageCommandUsageDemote(console);
					}
				} else if (args[0].equalsIgnoreCase("forceupdateconfigversion")) {
					this.m.forceUpdateConfigVersions();
					Messages.messageConfigVersionUpdated(console);
				} else if (args[0].equalsIgnoreCase("stats")) {
					Messages.messageStats(console);
				} else if (args[0].equalsIgnoreCase("addbuyablerank")) {
					if (args.length == 3) {
						final String rankname = s.getRankIgnoreCase(args[1]);
						final String rankname2 = s.getRankIgnoreCase(args[2]);
						final boolean success = s.addBuyableRank(rankname, rankname2);
						if (success) {
							Messages.messageCommandAddbuyablerankSuccess(console, rankname, rankname2);
						} else {
							Messages.messageCommandAddbuyablerankError(console, rankname, rankname2);
						}
					} else {
						Messages.messageCommandUsageAddbuyablerank(console);
					}
				} else if (args[0].equalsIgnoreCase("delbuyablerank")) {
					if (args.length == 3) {
						final String rankname = s.getRankIgnoreCase(args[1]);
						final String rankname2 = s.getRankIgnoreCase(args[2]);
						final boolean success = s.delBuyableRank(rankname, rankname2);
						if (success) {
							Messages.messageCommanddelbuyablerankkSuccess(console, rankname, rankname2);
						} else {
							Messages.messageCommandDelbuyablerankError(console, rankname, rankname2);
						}
					} else {
						Messages.messageCommandUsageDelbuyablerank(console);
					}
				} else if (args[0].equalsIgnoreCase("setcost")) {
					if (args.length == 3) {
						final String rankname = s.getRankIgnoreCase(args[1]);
						final String cost = s.getRankIgnoreCase(args[2]);
						final boolean success = s.setBuyCost(rankname, cost);
						if (success) {
							Messages.messageCommandSetcostSuccess(console, rankname, cost);
						} else {
							Messages.messageCommandSetcostError(console, rankname, cost);
						}
					} else {
						Messages.messageCommandUsageSetcost(console);
					}
				} else if (args[0].equalsIgnoreCase("addplayerperm")) {
					if (args.length == 3) {
						final String target_player = args[1];
						final String permission = args[2];
						final boolean result = s.addPlayerPermission(target_player, permission);
						if (result) {
							Messages.messageCommandPlayerPermissionAdded(console, permission, target_player);
						} else {
							Messages.messageErrorAddingPlayerPermission(console, target_player, permission);
						}
					} else {
						Messages.messageCommandUsageAddplayerperm(console);
					}
				} else if (args[0].equalsIgnoreCase("delplayerperm")) {
					if (args.length == 3) {
						final String target_player = args[1];
						final String permission = args[2];
						final boolean result = s.delPlayerPermission(target_player, permission);
						if (result) {
							Messages.messageCommandPlayerPermissionRemoved(console, permission, target_player);
						} else {
							Messages.messageErrorRemovingPlayerPermission(console, target_player, permission);
						}
					} else {
						Messages.messageCommandUsageDelplayerperm(console);
					}
				} else {
					Messages.unknownCommand(console);
				}
			}
		} else if (sender instanceof CraftBlockCommandSender) {
//			final CraftBlockCommandSender console = (CraftBlockCommandSender) sender;
			if (cmd.getName().equalsIgnoreCase("powerranks") || cmd.getName().equalsIgnoreCase("pr")) {
				if (args.length == 0) {
//					Messages.messageNoArgs(console);
				} else if (args[0].equalsIgnoreCase("reload")) {
					final PluginManager plg = Bukkit.getPluginManager();
					final Plugin plgname = plg.getPlugin(PowerRanks.pdf.getName());
					plg.disablePlugin(plgname);
					plg.enablePlugin(plgname);
				} else if (args[0].equalsIgnoreCase("set")) {
					if (args.length == 3) {
						s.setGroup(null, args[1], s.getRankIgnoreCase(args[2]));
					} else {
//						Messages.messageCommandUsageSet(console);
					}
				} else if (args[0].equalsIgnoreCase("addperm")) {
					if (args.length == 3) {
						final String rank2 = args[1].equals("*") ? args[1] : s.getRankIgnoreCase(args[1]);
						final String permission = args[2];
						final boolean result = s.addPermission(rank2, permission);
						if (result) {
//							Messages.messageCommandPermissionAdded(console, permission, rank2);
						} else {
//							Messages.messageGroupNotFound(console, rank2);
						}
					} else {
//						Messages.messageCommandUsageAddperm(console);
					}
				} else if (args[0].equalsIgnoreCase("delperm")) {
					if (args.length == 3) {
						final String rank2 = args[1].equals("*") ? args[1] : s.getRankIgnoreCase(args[1]);
						final String permission = args[2];
						final boolean result = s.removePermission(rank2, permission);
						if (result) {
//							Messages.messageCommandPermissionRemoved(console, permission, rank2);
						} else {
//							Messages.messageGroupNotFound(console, rank2);
						}
					} else {
//						Messages.messageCommandUsageDelperm(console);
					}
				} else if (args[0].equalsIgnoreCase("addinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String inheritance = args[2];
							final boolean result = s.addInheritance(rank2, inheritance);
							if (result) {
//								Messages.messageCommandInheritanceAdded(console, inheritance, rank2);
							} else {
//								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
//							Messages.messageCommandUsageAddInheritance(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("delinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String inheritance = args[2];
							final boolean result = s.removeInheritance(rank2, inheritance);
							if (result) {
//								Messages.messageCommandInheritanceRemoved(console, inheritance, rank2);
							} else {
//								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
//							Messages.messageCommandUsageRemoveInheritance(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("setprefix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String prefix = "";
							final boolean result = s.setPrefix(rank2, prefix);
							if (result) {
//								Messages.messageCommandSetPrefix(console, prefix, rank2);
							} else {
//								Messages.messageGroupNotFound(console, rank2);
							}
						} else if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String prefix = args[2];
							final boolean result = s.setPrefix(rank2, prefix);
							if (result) {
//								Messages.messageCommandSetPrefix(console, prefix, rank2);
							} else {
//								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
//							Messages.messageCommandUsageSetPrefix(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("setsuffix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String suffix = "";
							final boolean result = s.setSuffix(rank2, suffix);
							if (result) {
//								Messages.messageCommandSetSuffix(console, suffix, rank2);
							} else {
//								Messages.messageGroupNotFound(console, rank2);
							}
						} else if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String suffix = args[2];
							final boolean result = s.setSuffix(rank2, suffix);
							if (result) {
//								Messages.messageCommandSetSuffix(console, suffix, rank2);
							} else {
//								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
//							Messages.messageCommandUsageSetSuffix(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("setchatcolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String color = args[2];
							final boolean result = s.setChatColor(rank2, color);
							if (result) {
//								Messages.messageCommandSetChatColor(console, color, rank2);
							} else {
//								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
//							Messages.messageCommandUsageSetChatColor(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("setnamecolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String color = args[2];
							final boolean result = s.setNameColor(rank2, color);
							if (result) {
//								Messages.messageCommandSetNameColor(console, color, rank2);
							} else {
//								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
//							Messages.messageCommandUsageSetNameColor(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("createrank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final boolean success = s.createRank(rank2);
							if (success) {
//								Messages.messageCommandCreateRankSuccess(console, rank2);
							} else {
//								Messages.messageCommandCreateRankError(console, rank2);
							}
						} else {
//							Messages.messageCommandUsageCreateRank(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("deleterank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final boolean success = s.deleteRank(rank2);
							if (success) {
//								Messages.messageCommandDeleteRankSuccess(console, rank2);
							} else {
//								Messages.messageCommandDeleteRankError(console, rank2);
							}
						} else {
//							Messages.messageCommandUsageDeleteRank(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("enablebuild")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final boolean success = s.setBuild(rank2, true);
							if (success) {
//								Messages.messageCommandBuildEnabled(console, rank2);
							} else {
//								Messages.messageGroupNotFound(console, rank2);
							}
						} else {
//							Messages.messageCommandUsageEnableBuild(console);
						}
					}
				} else if (args[0].equalsIgnoreCase("disablebuild") && sender.hasPermission("powerranks.cmd.set")) {
					if (args.length == 2) {
						final String rank2 = s.getRankIgnoreCase(args[1]);
						final boolean success = s.setBuild(rank2, false);
						if (success) {
//							Messages.messageCommandBuildDisabled(console, rank2);
						} else {
//							Messages.messageGroupNotFound(console, rank2);
						}
					} else {
//						Messages.messageCommandUsageDisableBuild(console);
					}
				} else if (args[0].equalsIgnoreCase("promote")) {
					if (args.length == 2) {
						final String playername = args[1];
						final boolean success = s.promote(playername);
						if (success) {
//							Messages.messageCommandPromoteSuccess(console, playername);
						} else {
//							Messages.messageCommandPromoteError(console, playername);
						}
					} else {
//						Messages.messageCommandUsagePromote(console);
					}
				} else if (args[0].equalsIgnoreCase("demote")) {
					if (args.length == 2) {
						final String playername = args[1];
						final boolean success = s.demote(playername);
						if (success) {
//							Messages.messageCommandDemoteSuccess(console, playername);
						} else {
//							Messages.messageCommandDemoteError(console, playername);
						}
					} else {
//						Messages.messageCommandUsageDemote(console);
					}
				} else if (args[0].equalsIgnoreCase("renamerank")) {
					if (args.length == 3) {
						final String from = s.getRankIgnoreCase(args[1]);
						final String to = args[2];
						final boolean success = s.renameRank(from, to);
						if (success) {
//							Messages.messageCommandRenameRankSuccess(console, from);
						} else {
//							Messages.messageCommandRenameRankError(console, from);
						}
					} else {
//						Messages.messageCommandUsageDemote(console);
					}
				} else if (args[0].equalsIgnoreCase("setdefaultrank")) {
					if (args.length == 2) {
						final String rankname = s.getRankIgnoreCase(args[1]);
						final boolean success = s.setDefaultRank(rankname);
						if (success) {
//							Messages.messageCommandSetDefaultRankSuccess(console, rankname);
						} else {
//							Messages.messageCommandSetDefaultRankError(console, rankname);
						}
					} else {
//						Messages.messageCommandUsageDemote(console);
					}
				} else if (args[0].equalsIgnoreCase("addbuyablerank")) {
					if (args.length == 3) {
						final String rankname = s.getRankIgnoreCase(args[1]);
						final String rankname2 = s.getRankIgnoreCase(args[2]);
						final boolean success = s.addBuyableRank(rankname, rankname2);
						if (success) {
//									Messages.messageCommandSetDefaultRankSuccess(console, rankname);
						} else {
//									Messages.messageCommandSetDefaultRankError(console, rankname);
						}
					} else {
//								Messages.messageCommandUsageDemote(console);
					}
				} else if (args[0].equalsIgnoreCase("delbuyablerank")) {
					if (args.length == 3) {
						final String rankname = s.getRankIgnoreCase(args[1]);
						final String rankname2 = s.getRankIgnoreCase(args[2]);
						final boolean success = s.delBuyableRank(rankname, rankname2);
						if (success) {
//											Messages.messageCommandSetDefaultRankSuccess(console, rankname);
						} else {
//											Messages.messageCommandSetDefaultRankError(console, rankname);
						}
					} else {
//										Messages.messageCommandUsageDemote(console);
					}
				} else if (args[0].equalsIgnoreCase("setcost")) {
					if (args.length == 3) {
						final String rankname = s.getRankIgnoreCase(args[1]);
						final String cost = s.getRankIgnoreCase(args[2]);
						final boolean success = s.setBuyCost(rankname, cost);
						if (success) {
//											Messages.messageCommandSetDefaultRankSuccess(console, rankname);
						} else {
//											Messages.messageCommandSetDefaultRankError(console, rankname);
						}
					} else {
//										Messages.messageCommandUsageDemote(console);
					}
				} else if (args[0].equalsIgnoreCase("forceupdateconfigversion")) {
					this.m.forceUpdateConfigVersions();
//					Messages.messageConfigVersionUpdated(console);
				}
			}
		}
		return false;
	}
}