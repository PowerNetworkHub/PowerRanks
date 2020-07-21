package nl.svenar.PowerRanks.Commands;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_15_R1.command.CraftBlockCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;
import nl.svenar.PowerRanks.addons.PowerRanksPlayer;
import nl.svenar.PowerRanks.gui.GUI;
import nl.svenar.PowerRanks.gui.GUIPage.GUI_PAGE_ID;

public class Cmd implements CommandExecutor {
	PowerRanks m;

	public Cmd(PowerRanks m) {
		this.m = m;
	}

	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		final Users s = new Users(this.m);
		if (sender instanceof Player) { // TODO nothing todo just easy navigation
			final Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("powerranks") || cmd.getName().equalsIgnoreCase("pr")) {
				if (args.length == 0) {
					Messages.messageNoArgs(player);
				} else if (args[0].equalsIgnoreCase("help")) {
					if (sender.hasPermission("powerranks.cmd.help")) {
						if (args.length == 1) {
							Messages.helpMenu(player);
						} else if (args.length == 2) {
							int page = Integer.parseInt(args[1].replaceAll("[a-zA-Z]", ""));
							Messages.helpMenu(player, page);
						}
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
				} else if (args[0].equalsIgnoreCase("setrank")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							s.setGroup((Player) sender, args[1], s.getRankIgnoreCase(args[2]), true);
						} else {
							Messages.messageCommandUsageSet(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("setownrank")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							s.setGroup((Player) sender, sender.getName(), s.getRankIgnoreCase(args[1]), true);
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
				} else if (args[0].equalsIgnoreCase("listsubranks")) {
					if (sender.hasPermission("powerranks.cmd.list")) {
						if (args.length == 2) {
							if (Bukkit.getPlayer(args[1]) != null) {
								List<String> subranks = s.getSubranks(args[1]);
								sender.sendMessage("Subranks of " + Bukkit.getPlayer(args[1]).getName() + "(" + subranks.size() + "):");
								for (String subrank : subranks) {
									sender.sendMessage(subrank);
								}
							} else {
								Messages.messagePlayerNotFound(player, args[1]);
							}
						} else {
							Messages.messageCommandUsageListSubranks(player);
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
				} else if (args[0].equalsIgnoreCase("addsubrank")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 3) {
							final String playername = args[1];
							final String subrank = s.getRankIgnoreCase(args[2]);
							final boolean result = s.addSubrank(playername, subrank);
							if (result) {
								Messages.messageSuccessAddsubrank(player, subrank, playername);
							} else {
								Messages.messageErrorAddsubrank(player, subrank, playername);
							}
						} else {
							Messages.messageCommandUsageAddsubrank(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("delsubrank")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 3) {
							final String playername = args[1];
							final String subrank = s.getRankIgnoreCase(args[2]);
							final boolean result = s.removeSubrank(playername, subrank);
							if (result) {
								Messages.messageSuccessDelsubrank(player, subrank, playername);
							} else {
								Messages.messageErrorDelsubrank(player, subrank, playername);
							}
						} else {
							Messages.messageCommandUsageDelsubrank(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("enablesubrankprefix")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 3) {
							final String playername = args[1];
							final String subrank = s.getRankIgnoreCase(args[2]);
							final boolean result = s.changeSubrankField(playername, subrank, "use_prefix", true);
							if (result) {
								Messages.messageSuccessChangesubrank(player, subrank, playername);
							} else {
								Messages.messageErrorChangesubrank(player, subrank, playername);
							}
						} else {
							Messages.messageCommandUsageEnablesubrankprefix(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("disablesubrankprefix")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 3) {
							final String playername = args[1];
							final String subrank = s.getRankIgnoreCase(args[2]);
							final boolean result = s.changeSubrankField(playername, subrank, "use_prefix", false);
							if (result) {
								Messages.messageSuccessChangesubrank(player, subrank, playername);
							} else {
								Messages.messageErrorChangesubrank(player, subrank, playername);
							}
						} else {
							Messages.messageCommandUsageDisablesubrankprefix(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("enablesubranksuffix")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 3) {
							final String playername = args[1];
							final String subrank = s.getRankIgnoreCase(args[2]);
							final boolean result = s.changeSubrankField(playername, subrank, "use_suffix", true);
							if (result) {
								Messages.messageSuccessChangesubrank(player, subrank, playername);
							} else {
								Messages.messageErrorChangesubrank(player, subrank, playername);
							}
						} else {
							Messages.messageCommandUsageEnablesubranksuffix(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("disablesubranksuffix")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 3) {
							final String playername = args[1];
							final String subrank = s.getRankIgnoreCase(args[2]);
							final boolean result = s.changeSubrankField(playername, subrank, "use_suffix", false);
							if (result) {
								Messages.messageSuccessChangesubrank(player, subrank, playername);
							} else {
								Messages.messageErrorChangesubrank(player, subrank, playername);
							}
						} else {
							Messages.messageCommandUsageDisablesubranksuffix(player);
						}
					} else {
						Messages.noPermission(player);
					}

				} else if (args[0].equalsIgnoreCase("enablesubrankpermissions")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 3) {
							final String playername = args[1];
							final String subrank = s.getRankIgnoreCase(args[2]);
							final boolean result = s.changeSubrankField(playername, subrank, "use_permissions", true);
							if (result) {
								Messages.messageSuccessChangesubrank(player, subrank, playername);
							} else {
								Messages.messageErrorChangesubrank(player, subrank, playername);
							}
						} else {
							Messages.messageCommandUsageEnablesubrankpermissions(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("disablesubrankpermissions")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 3) {
							final String playername = args[1];
							final String subrank = s.getRankIgnoreCase(args[2]);
							final boolean result = s.changeSubrankField(playername, subrank, "use_permissions", false);
							if (result) {
								Messages.messageSuccessChangesubrank(player, subrank, playername);
							} else {
								Messages.messageErrorChangesubrank(player, subrank, playername);
							}
						} else {
							Messages.messageCommandUsageDisablesubrankpermissions(player);
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
				} else if (args[0].equalsIgnoreCase("factoryreset")) {
					if (sender.hasPermission("powerranks.cmd.factoryreset")) {
						if (args.length == 1) {
							Messages.messageCommandFactoryReset(player);
						} else if (args.length == 2) {
							if (PowerRanks.factoryresetid == null) {
								Messages.messageCommandFactoryReset(player);
							} else {
								String resetid = args[1];
								if (resetid.equalsIgnoreCase(PowerRanks.factoryresetid))
									this.m.factoryReset(sender);
								else
									Messages.messageCommandFactoryReset(player);
							}
						} else {
							Messages.messageCommandUsageFactoryReset(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("gui")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
//						GUI.openPowerRanksGUI(player, 0, "");
						GUI.openGUI(player, GUI_PAGE_ID.MAIN);
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("rankup")) {
					if (sender.hasPermission("powerranks.cmd.rankup")) {
						if (PowerRanks.getVaultEconomy() != null)
//							GUI.openPowerRanksRankupGUI(player, 0);
							GUI.openGUI(player, GUI_PAGE_ID.RANKUP);
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
								Messages.messageCommandDelbuyablerankSuccess(player, rankname, rankname2);
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
				} else if (args[0].equalsIgnoreCase("createusertag")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						if (sender.hasPermission("powerranks.cmd.admin")) {
							if (args.length == 3) {
								final String tag = args[1];
								final String text = args[2];
								final boolean result = s.createUserTag(tag, text);
								if (result) {
									Messages.messageCommandCreateusertagSuccess(player, tag, text);
								} else {
									Messages.messageCommandCreateusertagError(player, tag, text);
								}
							} else {
								Messages.messageCommandUsageCreateusertag(player);
							}
						} else {
							Messages.noPermission(player);
						}
					} else {
						Messages.messageUsertagsDisabled(player);
					}

				} else if (args[0].equalsIgnoreCase("editusertag")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						if (sender.hasPermission("powerranks.cmd.admin")) {
							if (args.length == 3) {
								final String tag = args[1];
								final String text = args[2];
								final boolean result = s.editUserTag(tag, text);
								if (result) {
									Messages.messageCommandEditusertagSuccess(player, tag, text);
								} else {
									Messages.messageCommandEditusertagError(player, tag, text);
								}
							} else {
								Messages.messageCommandUsageEditusertag(player);
							}
						} else {
							Messages.noPermission(player);
						}
					} else {
						Messages.messageUsertagsDisabled(player);
					}

				} else if (args[0].equalsIgnoreCase("removeusertag")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						if (sender.hasPermission("powerranks.cmd.admin")) {
							if (args.length == 3) {
								final String tag = args[1];
								final boolean result = s.removeUserTag(tag);
								if (result) {
									Messages.messageCommandRemoveusertagSuccess(player, tag);
								} else {
									Messages.messageCommandRemoveusertagError(player, tag);
								}
							} else {
								Messages.messageCommandUsageRemoveusertag(player);
							}
						} else {
							Messages.noPermission(player);
						}
					} else {
						Messages.messageUsertagsDisabled(player);
					}

				} else if (args[0].equalsIgnoreCase("setusertag")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						if (args.length == 2) {
							if (sender.hasPermission("powerranks.cmd.usertag")) {
								final String playername = player.getName();
								final String tag = args[1];
								final boolean result = s.setUserTag(playername, tag);
								if (result) {
									Messages.messageCommandSetusertagSuccess(player, playername, tag);
								} else {
									Messages.messageCommandSetusertagError(player, playername, tag);
								}
							} else {
								Messages.noPermission(player);
							}
						} else if (args.length == 3) {
							if (sender.hasPermission("powerranks.cmd.admin")) {
								final String playername = args[1];
								final String tag = args[2];
								final boolean result = s.setUserTag(playername, tag);
								if (result) {
									Messages.messageCommandSetusertagSuccess(player, playername, tag);
								} else {
									Messages.messageCommandSetusertagError(player, playername, tag);
								}
							} else {
								Messages.noPermission(player);
							}
						} else {
							Messages.messageCommandUsageSetusertag(player);
						}
					} else {
						Messages.messageUsertagsDisabled(player);
					}

				} else if (args[0].equalsIgnoreCase("clearusertag")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						if (args.length == 1) {
							if (sender.hasPermission("powerranks.cmd.usertag")) {
								final String playername = player.getName();
								final boolean result = s.clearUserTag(playername);
								if (result) {
									Messages.messageCommandClearusertagSuccess(player, playername);
								} else {
									Messages.messageCommandClearusertagError(player, playername);
								}
							} else {
								Messages.noPermission(player);
							}
						} else if (args.length == 2) {
							if (sender.hasPermission("powerranks.cmd.admin")) {
								final String playername = args[1];
								final boolean result = s.clearUserTag(playername);
								if (result) {
									Messages.messageCommandClearusertagSuccess(player, playername);
								} else {
									Messages.messageCommandClearusertagError(player, playername);
								}
							} else {
								Messages.noPermission(player);
							}
						} else {
							Messages.messageCommandUsageSetusertag(player);
						}
					} else {
						Messages.messageUsertagsDisabled(player);
					}

				} else if (args[0].equalsIgnoreCase("listusertags")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						if (sender.hasPermission("powerranks.cmd.usertag")) {
							if (args.length == 1) {
								Set<String> tags = s.getUserTags();
								player.sendMessage("Usertags(" + tags.size() + "):");
								for (String tag : tags) {
									player.sendMessage(tag + " - " + PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), s.getUserTagValue(tag), true));
								}
							} else {
								Messages.messageCommandUsageListusertags(player);
							}
						} else {
							Messages.noPermission(player);
						}
					} else {
						Messages.messageUsertagsDisabled(player);
					}
				} else if (args[0].equalsIgnoreCase("setpromoterank")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 3) {
							final String rankname = args[1];
							final String promote_rank = args[2];
							if (s.setPromoteRank(rankname, promote_rank)) {
								Messages.messageCommandSetpromoterankSuccess(player, rankname, promote_rank);
							} else {
								Messages.messageCommandSetpromoterankError(player, rankname, promote_rank);
							}
						} else {
							Messages.messageCommandUsageSetpromoterank(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("setdemoterank")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 3) {
							final String rankname = args[1];
							final String promote_rank = args[2];
							if (s.setDemoteRank(rankname, promote_rank)) {
								Messages.messageCommandSetdemoterankSuccess(player, rankname, promote_rank);
							} else {
								Messages.messageCommandSetdemoterankError(player, rankname, promote_rank);
							}
						} else {
							Messages.messageCommandUsageSetdemoterank(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("clearpromoterank")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 2) {
							final String rankname = args[1];
							if (s.clearPromoteRank(rankname)) {
								Messages.messageCommandClearpromoterankSuccess(player, rankname);
							} else {
								Messages.messageCommandClearpromoterankError(player, rankname);
							}
						} else {
							Messages.messageCommandUsageClearpromoterank(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("cleardemoterank")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 2) {
							final String rankname = args[1];
							if (s.clearDemoteRank(rankname)) {
								Messages.messageCommandCleardemoterankSuccess(player, rankname);
							} else {
								Messages.messageCommandCleardemoterankError(player, rankname);
							}
						} else {
							Messages.messageCommandUsageCleardemoterank(player);
						}
					} else {
						Messages.noPermission(player);
					}

				} else if (args[0].equalsIgnoreCase("addoninfo")) {
					if (sender.hasPermission("powerranks.cmd.admin")) {
						if (args.length == 2) {
							final String addon_name = args[1];
							PowerRanksAddon addon = null;
							for (Entry<File, PowerRanksAddon> a : this.m.addonsManager.addonClasses.entrySet()) {
								if (a.getValue().getIdentifier().equalsIgnoreCase(addon_name))
									addon = a.getValue();
							}
							if (addon != null) {
								player.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
								player.sendMessage(ChatColor.DARK_GREEN + "Add-on name: " + ChatColor.GREEN + addon.getIdentifier());
								player.sendMessage(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + addon.getAuthor());
								player.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + addon.getVersion());
								player.sendMessage(ChatColor.DARK_GREEN + "Registered Commands:");
								for (String command : addon.getRegisteredCommands()) {
									player.sendMessage(ChatColor.GREEN + "- /pr " + command);
								}
								player.sendMessage(ChatColor.DARK_GREEN + "Registered Permissions:");
								for (String permission : addon.getRegisteredPermissions()) {
									player.sendMessage(ChatColor.GREEN + "- " + permission);
								}
								player.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
							} else {
								Messages.messageCommandErrorAddonNotFound(player, addon_name);
							}
						} else {
							Messages.messageCommandUsageAddoninfo(player);
						}
					} else {
						Messages.noPermission(player);
					}

				} else if (args[0].equalsIgnoreCase("seticon")) {
					if (args.length == 2) {
						String rankName = s.getRankIgnoreCase(args[1]);
						Material material = player.getInventory().getItemInMainHand().getType();
						player.sendMessage(material.name());
						if (material != Material.AIR) {
							s.setRanksConfigFieldString(rankName, "gui.icon", material.name().toLowerCase());
							player.sendMessage("Set icon to " + material.name().toLowerCase() + " on rank :" + rankName);
						} else {
							player.sendMessage("You must held a item");
						}
						
					} else {
						player.sendMessage("/pr seticon <rank>");
					}
				} else {
					boolean addonCommandFound = false;
					for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
						PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
						if (prAddon.getValue().onPowerRanksCommand(prPlayer, true, args[0], args)) {
							addonCommandFound = true;
						}
					}
					if (!addonCommandFound)
						Messages.unknownCommand(player);
				}
			}
		} else if (sender instanceof ConsoleCommandSender) { // TODO nothing todo just easy navigation
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
				} else if (args[0].equalsIgnoreCase("setrank")) {
					if (args.length == 3) {
						s.setGroup(null, args[1], s.getRankIgnoreCase(args[2]), true);
					} else {
						Messages.messageCommandUsageSet(console);
					}
				} else if (args[0].equalsIgnoreCase("listranks")) {
					Set<String> ranks = s.getGroups();
					console.sendMessage("Ranks(" + ranks.size() + "):");
					for (String rank : ranks) {
						console.sendMessage(rank);
					}
				} else if (args[0].equalsIgnoreCase("listsubranks")) {
					if (args.length == 2) {
						if (Bukkit.getPlayer(args[1]) != null) {
							List<String> subranks = s.getSubranks(args[1]);
							sender.sendMessage("Subranks of " + Bukkit.getPlayer(args[1]).getName() + "(" + subranks.size() + "):");
							for (String subrank : subranks) {
								console.sendMessage(subrank);
							}
						} else {
							Messages.messagePlayerNotFound(console, args[1]);
						}
					} else {
						Messages.messageCommandUsageListSubranks(console);
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
				} else if (args[0].equalsIgnoreCase("addsubrank")) {
					if (args.length == 3) {
						final String playername = args[1];
						final String subrank = s.getRankIgnoreCase(args[2]);
						final boolean result = s.addSubrank(playername, subrank);
						if (result) {
							Messages.messageSuccessAddsubrank(console, subrank, playername);
						} else {
							Messages.messageErrorAddsubrank(console, subrank, playername);
						}
					} else {
						Messages.messageCommandUsageAddsubrank(console);
					}
				} else if (args[0].equalsIgnoreCase("delsubrank")) {
					if (args.length == 3) {
						final String playername = args[1];
						final String subrank = s.getRankIgnoreCase(args[2]);
						final boolean result = s.removeSubrank(playername, subrank);
						if (result) {
							Messages.messageSuccessDelsubrank(console, subrank, playername);
						} else {
							Messages.messageErrorDelsubrank(console, subrank, playername);
						}
					} else {
						Messages.messageCommandUsageDelsubrank(console);
					}
				} else if (args[0].equalsIgnoreCase("enablesubrankprefix")) {
					if (args.length == 3) {
						final String playername = args[1];
						final String subrank = s.getRankIgnoreCase(args[2]);
						final boolean result = s.changeSubrankField(playername, subrank, "use_prefix", true);
						if (result) {
							Messages.messageSuccessChangesubrank(console, subrank, playername);
						} else {
							Messages.messageErrorChangesubrank(console, subrank, playername);
						}
					} else {
						Messages.messageCommandUsageEnablesubrankprefix(console);
					}
				} else if (args[0].equalsIgnoreCase("disablesubrankprefix")) {
					if (args.length == 3) {
						final String playername = args[1];
						final String subrank = s.getRankIgnoreCase(args[2]);
						final boolean result = s.changeSubrankField(playername, subrank, "use_prefix", false);
						if (result) {
							Messages.messageSuccessChangesubrank(console, subrank, playername);
						} else {
							Messages.messageErrorChangesubrank(console, subrank, playername);
						}
					} else {
						Messages.messageCommandUsageDisablesubrankprefix(console);
					}
				} else if (args[0].equalsIgnoreCase("enablesubranksuffix")) {
					if (args.length == 3) {
						final String playername = args[1];
						final String subrank = s.getRankIgnoreCase(args[2]);
						final boolean result = s.changeSubrankField(playername, subrank, "use_suffix", true);
						if (result) {
							Messages.messageSuccessChangesubrank(console, subrank, playername);
						} else {
							Messages.messageErrorChangesubrank(console, subrank, playername);
						}
					} else {
						Messages.messageCommandUsageEnablesubranksuffix(console);
					}
				} else if (args[0].equalsIgnoreCase("disablesubranksuffix")) {
					if (args.length == 3) {
						final String playername = args[1];
						final String subrank = s.getRankIgnoreCase(args[2]);
						final boolean result = s.changeSubrankField(playername, subrank, "use_suffix", false);
						if (result) {
							Messages.messageSuccessChangesubrank(console, subrank, playername);
						} else {
							Messages.messageErrorChangesubrank(console, subrank, playername);
						}
					} else {
						Messages.messageCommandUsageDisablesubranksuffix(console);
					}

				} else if (args[0].equalsIgnoreCase("enablesubrankpermissions")) {
					if (args.length == 3) {
						final String playername = args[1];
						final String subrank = s.getRankIgnoreCase(args[2]);
						final boolean result = s.changeSubrankField(playername, subrank, "use_permissions", true);
						if (result) {
							Messages.messageSuccessChangesubrank(console, subrank, playername);
						} else {
							Messages.messageErrorChangesubrank(console, subrank, playername);
						}
					} else {
						Messages.messageCommandUsageEnablesubrankpermissions(console);
					}
				} else if (args[0].equalsIgnoreCase("disablesubrankpermissions")) {
					if (args.length == 3) {
						final String playername = args[1];
						final String subrank = s.getRankIgnoreCase(args[2]);
						final boolean result = s.changeSubrankField(playername, subrank, "use_permissions", false);
						if (result) {
							Messages.messageSuccessChangesubrank(console, subrank, playername);
						} else {
							Messages.messageErrorChangesubrank(console, subrank, playername);
						}
					} else {
						Messages.messageCommandUsageDisablesubrankpermissions(console);
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
				} else if (args[0].equalsIgnoreCase("factoryreset")) {
					if (args.length == 1) {
						Messages.messageCommandFactoryReset(console);
					} else if (args.length == 2) {
						if (PowerRanks.factoryresetid == null) {
							Messages.messageCommandFactoryReset(console);
						} else {
							String resetid = args[1];
							if (resetid.equalsIgnoreCase(PowerRanks.factoryresetid))
								this.m.factoryReset(sender);
							else
								Messages.messageCommandFactoryReset(console);
						}
					} else {
						Messages.messageCommandUsageFactoryReset(console);
					}
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
							Messages.messageCommandDelbuyablerankSuccess(console, rankname, rankname2);
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
				} else if (args[0].equalsIgnoreCase("createusertag")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						if (args.length == 3) {
							final String tag = args[1];
							final String text = args[2];
							final boolean result = s.createUserTag(tag, text);
							if (result) {
								Messages.messageCommandCreateusertagSuccess(console, tag, text);
							} else {
								Messages.messageCommandCreateusertagError(console, tag, text);
							}
						} else {
							Messages.messageCommandUsageCreateusertag(console);
						}
					} else {
						Messages.messageUsertagsDisabled(console);
					}
				} else if (args[0].equalsIgnoreCase("editusertag")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						if (args.length == 3) {
							final String tag = args[1];
							final String text = args[2];
							final boolean result = s.editUserTag(tag, text);
							if (result) {
								Messages.messageCommandEditusertagSuccess(console, tag, text);
							} else {
								Messages.messageCommandEditusertagError(console, tag, text);
							}
						} else {
							Messages.messageCommandUsageEditusertag(console);
						}
					} else {
						Messages.messageUsertagsDisabled(console);
					}
				} else if (args[0].equalsIgnoreCase("removeusertag")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						if (args.length == 3) {
							final String tag = args[1];
							final boolean result = s.removeUserTag(tag);
							if (result) {
								Messages.messageCommandRemoveusertagSuccess(console, tag);
							} else {
								Messages.messageCommandRemoveusertagError(console, tag);
							}
						} else {
							Messages.messageCommandUsageRemoveusertag(console);
						}
					} else {
						Messages.messageUsertagsDisabled(console);
					}
				} else if (args[0].equalsIgnoreCase("setusertag")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						if (args.length == 3) {
							final String player = args[1];
							final String tag = args[2];
							final boolean result = s.setUserTag(player, tag);
							if (result) {
								Messages.messageCommandSetusertagSuccess(console, player, tag);
							} else {
								Messages.messageCommandSetusertagError(console, player, tag);
							}
						} else {
							Messages.messageCommandUsageSetusertag(console);
						}
					} else {
						Messages.messageUsertagsDisabled(console);
					}
				} else if (args[0].equalsIgnoreCase("clearusertag")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						if (args.length == 2) {
							final String player = args[1];
							final boolean result = s.clearUserTag(player);
							if (result) {
								Messages.messageCommandClearusertagSuccess(console, player);
							} else {
								Messages.messageCommandClearusertagError(console, player);
							}
						} else {
							Messages.messageCommandUsageSetusertag(console);
						}
					} else {
						Messages.messageUsertagsDisabled(console);
					}
				} else if (args[0].equalsIgnoreCase("listusertags")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						if (args.length == 1) {
							Set<String> tags = s.getUserTags();
							console.sendMessage("Usertags(" + tags.size() + "):");
							for (String tag : tags) {
								console.sendMessage(tag + " - " + PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), s.getUserTagValue(tag), true));
							}
						} else {
							Messages.messageCommandUsageListusertags(console);
						}
					} else {
						Messages.messageUsertagsDisabled(console);
					}
				} else if (args[0].equalsIgnoreCase("setpromoterank")) {
					if (args.length == 3) {
						final String rankname = args[1];
						final String promote_rank = args[2];
						if (s.setPromoteRank(rankname, promote_rank)) {
							Messages.messageCommandSetpromoterankSuccess(console, rankname, promote_rank);
						} else {
							Messages.messageCommandSetpromoterankError(console, rankname, promote_rank);
						}
					} else {
						Messages.messageCommandUsageSetpromoterank(console);
					}
				} else if (args[0].equalsIgnoreCase("setdemoterank")) {
					if (args.length == 3) {
						final String rankname = args[1];
						final String promote_rank = args[2];
						if (s.setDemoteRank(rankname, promote_rank)) {
							Messages.messageCommandSetdemoterankSuccess(console, rankname, promote_rank);
						} else {
							Messages.messageCommandSetdemoterankError(console, rankname, promote_rank);
						}
					} else {
						Messages.messageCommandUsageSetdemoterank(console);
					}
				} else if (args[0].equalsIgnoreCase("clearpromoterank")) {
					if (args.length == 2) {
						final String rankname = args[1];
						if (s.clearPromoteRank(rankname)) {
							Messages.messageCommandClearpromoterankSuccess(console, rankname);
						} else {
							Messages.messageCommandClearpromoterankError(console, rankname);
						}
					} else {
						Messages.messageCommandUsageClearpromoterank(console);
					}
				} else if (args[0].equalsIgnoreCase("cleardemoterank")) {
					if (args.length == 2) {
						final String rankname = args[1];
						if (s.clearDemoteRank(rankname)) {
							Messages.messageCommandCleardemoterankSuccess(console, rankname);
						} else {
							Messages.messageCommandCleardemoterankError(console, rankname);
						}
					} else {
						Messages.messageCommandUsageCleardemoterank(console);
					}

				} else if (args[0].equalsIgnoreCase("addoninfo")) {
					if (args.length == 2) {
						final String addon_name = args[1];
						PowerRanksAddon addon = null;
						for (Entry<File, PowerRanksAddon> a : this.m.addonsManager.addonClasses.entrySet()) {
							if (a.getValue().getIdentifier().equalsIgnoreCase(addon_name))
								addon = a.getValue();
						}
						if (addon != null) {
							console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
							console.sendMessage(ChatColor.DARK_GREEN + "Add-on name: " + ChatColor.GREEN + addon.getIdentifier());
							console.sendMessage(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + addon.getAuthor());
							console.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + addon.getVersion());
							console.sendMessage(ChatColor.DARK_GREEN + "Registered Commands:");
							for (String command : addon.getRegisteredCommands()) {
								console.sendMessage(ChatColor.GREEN + "- /pr " + command);
							}
							console.sendMessage(ChatColor.DARK_GREEN + "Registered Permissions:");
							for (String permission : addon.getRegisteredPermissions()) {
								console.sendMessage(ChatColor.GREEN + "- " + permission);
							}
							console.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
						} else {
							Messages.messageCommandErrorAddonNotFound(console, addon_name);
						}
					} else {
						Messages.messageCommandUsageAddoninfo(console);
					}
//				} else if (args[0].equalsIgnoreCase("webeditor")) {
//					Editor editor = new Editor();
//					editor.setup();
				} else {
					boolean addonCommandFound = false;
					for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
						PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, "CONSOLE");
						if (prAddon.getValue().onPowerRanksCommand(prPlayer, false, args[0], args)) {
							addonCommandFound = true;
						}
					}
					if (!addonCommandFound)
						Messages.unknownCommand(console);
				}
			}
		} else if (sender instanceof CraftBlockCommandSender) { // TODO nothing todo just easy navigation
			if (cmd.getName().equalsIgnoreCase("powerranks") || cmd.getName().equalsIgnoreCase("pr")) {
				if (args.length == 0) {
				} else if (args[0].equalsIgnoreCase("reload")) {
					final PluginManager plg = Bukkit.getPluginManager();
					final Plugin plgname = plg.getPlugin(PowerRanks.pdf.getName());
					plg.disablePlugin(plgname);
					plg.enablePlugin(plgname);
				} else if (args[0].equalsIgnoreCase("setrank")) {
					if (args.length == 3) {
						s.setGroup(null, args[1], s.getRankIgnoreCase(args[2]), true);
					} else {
					}
				} else if (args[0].equalsIgnoreCase("addperm")) {
					if (args.length == 3) {
						final String rank2 = args[1].equals("*") ? args[1] : s.getRankIgnoreCase(args[1]);
						final String permission = args[2];
						s.addPermission(rank2, permission);
					}
				} else if (args[0].equalsIgnoreCase("delperm")) {
					if (args.length == 3) {
						final String rank2 = args[1].equals("*") ? args[1] : s.getRankIgnoreCase(args[1]);
						final String permission = args[2];
						s.removePermission(rank2, permission);
					}
				} else if (args[0].equalsIgnoreCase("addinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String inheritance = args[2];
							s.addInheritance(rank2, inheritance);
						}
					}
				} else if (args[0].equalsIgnoreCase("delinheritance")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String inheritance = args[2];
							s.removeInheritance(rank2, inheritance);
						}
					}
				} else if (args[0].equalsIgnoreCase("setprefix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String prefix = "";
							s.setPrefix(rank2, prefix);
						} else if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String prefix = args[2];
							s.setPrefix(rank2, prefix);
						}
					}
				} else if (args[0].equalsIgnoreCase("setsuffix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String suffix = "";
							s.setSuffix(rank2, suffix);
						} else if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String suffix = args[2];
							s.setSuffix(rank2, suffix);
						}
					}
				} else if (args[0].equalsIgnoreCase("setchatcolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String color = args[2];
							s.setChatColor(rank2, color);
						}
					}
				} else if (args[0].equalsIgnoreCase("setnamecolor")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String color = args[2];
							s.setNameColor(rank2, color);
						}
					}
				} else if (args[0].equalsIgnoreCase("createrank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							s.createRank(rank2);
						}
					}
				} else if (args[0].equalsIgnoreCase("deleterank")) {
					if (sender.hasPermission("powerranks.cmd.create")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							s.deleteRank(rank2);
						}
					}
				} else if (args[0].equalsIgnoreCase("enablebuild")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							s.setBuild(rank2, true);
						}
					}
				} else if (args[0].equalsIgnoreCase("disablebuild")) {
					if (args.length == 2) {
						final String rank2 = s.getRankIgnoreCase(args[1]);
						s.setBuild(rank2, false);
					}
				} else if (args[0].equalsIgnoreCase("promote")) {
					if (args.length == 2) {
						final String playername = args[1];
						s.promote(playername);
					}
				} else if (args[0].equalsIgnoreCase("demote")) {
					if (args.length == 2) {
						final String playername = args[1];
						s.demote(playername);
					}
				} else if (args[0].equalsIgnoreCase("renamerank")) {
					if (args.length == 3) {
						final String from = s.getRankIgnoreCase(args[1]);
						final String to = args[2];
						s.renameRank(from, to);
					}
				} else if (args[0].equalsIgnoreCase("setdefaultrank")) {
					if (args.length == 2) {
						final String rankname = s.getRankIgnoreCase(args[1]);
						s.setDefaultRank(rankname);
					}
				} else if (args[0].equalsIgnoreCase("addbuyablerank")) {
					if (args.length == 3) {
						final String rankname = s.getRankIgnoreCase(args[1]);
						final String rankname2 = s.getRankIgnoreCase(args[2]);
						s.addBuyableRank(rankname, rankname2);
					}
				} else if (args[0].equalsIgnoreCase("delbuyablerank")) {
					if (args.length == 3) {
						final String rankname = s.getRankIgnoreCase(args[1]);
						final String rankname2 = s.getRankIgnoreCase(args[2]);
						s.delBuyableRank(rankname, rankname2);
					}
				} else if (args[0].equalsIgnoreCase("setcost")) {
					if (args.length == 3) {
						final String rankname = s.getRankIgnoreCase(args[1]);
						final String cost = s.getRankIgnoreCase(args[2]);
						s.setBuyCost(rankname, cost);
					}
				} else if (args[0].equalsIgnoreCase("addplayerperm")) {
					if (args.length == 3) {
						final String target_player = args[1];
						final String permission = args[2];
						s.addPlayerPermission(target_player, permission);
					}
				} else if (args[0].equalsIgnoreCase("delplayerperm")) {
					if (args.length == 3) {
						final String target_player = args[1];
						final String permission = args[2];
						s.delPlayerPermission(target_player, permission);
					}

				} else if (args[0].equalsIgnoreCase("createusertag")) {
					if (args.length == 3) {
						final String tag = args[1];
						final String text = args[2];
						s.createUserTag(tag, text);
					}

				} else if (args[0].equalsIgnoreCase("editusertag")) {
					if (args.length == 3) {
						final String tag = args[1];
						final String text = args[2];
						s.editUserTag(tag, text);
					}

				} else if (args[0].equalsIgnoreCase("removeusertag")) {
					if (args.length == 3) {
						final String tag = args[1];
						s.removeUserTag(tag);
					}

				} else if (args[0].equalsIgnoreCase("setusertag")) {
					if (args.length == 3) {
						final String player = args[1];
						final String tag = args[2];
						s.setUserTag(player, tag);
					}
				}

			} else if (args[0].equalsIgnoreCase("setpromoterank")) {
				if (args.length == 3) {
					final String rankname = args[1];
					final String promote_rank = args[2];
					s.setPromoteRank(rankname, promote_rank);
				}
			} else if (args[0].equalsIgnoreCase("setdemoterank")) {
				if (args.length == 3) {
					final String rankname = args[1];
					final String promote_rank = args[2];
					s.setDemoteRank(rankname, promote_rank);
				}
			} else if (args[0].equalsIgnoreCase("clearpromoterank")) {
				if (args.length == 2) {
					final String rankname = args[1];
					s.clearPromoteRank(rankname);
				}
			} else if (args[0].equalsIgnoreCase("cleardemoterank")) {
				if (args.length == 2) {
					final String rankname = args[1];
					s.clearDemoteRank(rankname);
				}
			}
		}
		return false;
	}
}