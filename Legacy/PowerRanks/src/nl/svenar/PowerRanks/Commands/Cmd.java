package nl.svenar.PowerRanks.Commands;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.VaultHook;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.PowerRanksVerbose;
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
				if (PowerRanksVerbose.USE_VERBOSE) {
					String argstmp = "";
					for (String arg : args)
						argstmp += " " + arg;
					PowerRanksVerbose.log("onCommand", "Sender: " + sender.getName() + ", Command(" + cmd + "): /pr" + argstmp);
				}

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
					if (args.length == 3) {
						String target_rank = s.getRankIgnoreCase(args[2]);
						if (sender.hasPermission("powerranks.cmd.setrank") || sender.hasPermission("powerranks.cmd.setrank." + target_rank.toLowerCase())) {
							s.setGroup((Player) sender, args[1], target_rank, true);

						} else {
							Messages.noPermission(player);
						}
					} else {
						if (sender.hasPermission("powerranks.cmd.setrank") || sender.hasPermission("powerranks.cmd.setrank.*")) {
							Messages.messageCommandUsageSet(player);
						} else {
							Messages.noPermission(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("setownrank")) {
					if (args.length == 2) {
						String target_rank = s.getRankIgnoreCase(args[1]);
						if (sender.hasPermission("powerranks.cmd.setownrank") || sender.hasPermission("powerranks.cmd.setrank." + target_rank.toLowerCase())) {
							s.setGroup((Player) sender, sender.getName(), target_rank, true);

						} else {
							Messages.noPermission(player);
						}
					} else {
						if (sender.hasPermission("powerranks.cmd.setownrank") || sender.hasPermission("powerranks.cmd.setrank.*")) {
							Messages.messageCommandUsageSetown(player);
						} else {
							Messages.noPermission(player);
						}
					}
				} else if (args[0].equalsIgnoreCase("listranks")) {
					if (sender.hasPermission("powerranks.cmd.listranks")) {
						Set<String> ranks = s.getGroups();
						sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
						sender.sendMessage(ChatColor.DARK_GREEN + "Number of ranks: " + ChatColor.GREEN + ranks.size());
						int index = 0;
						for (String rank : ranks) {
							index++;
							sender.sendMessage(ChatColor.DARK_GREEN + "#" + index + ". " + ChatColor.GREEN + rank + ChatColor.RESET + " " + PowerRanks.chatColor(s.getPrefix(rank), true));
						}
						sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("listsubranks")) {
					if (sender.hasPermission("powerranks.cmd.listsubranks")) {
						if (args.length == 2) {
							if (Bukkit.getPlayer(args[1]) != null) {
								List<String> subranks = s.getSubranks(args[1]);
								sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
								sender.sendMessage(ChatColor.DARK_GREEN + "Subranks from player: " + ChatColor.GREEN + Bukkit.getPlayer(args[1]).getName());
								sender.sendMessage(ChatColor.DARK_GREEN + "Number of subranks: " + ChatColor.GREEN + subranks.size());
								int index = 0;
								for (String subrank : subranks) {
									index++;
									sender.sendMessage(ChatColor.DARK_GREEN + "#" + index + ". " + ChatColor.GREEN + subrank + ChatColor.RESET + " " + PowerRanks.chatColor(s.getPrefix(subrank), true));
								}
								sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
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
					String rank_name = "";

					if (sender.hasPermission("powerranks.cmd.listpermissions")) {
						if (args.length == 2) {
							rank_name = s.getRankIgnoreCase(args[1]);
							if (s.getGroups().contains(rank_name)) {
								Messages.listRankPermissions(sender, s, rank_name, 0);
							} else {
								Messages.messageGroupNotFound(player, args[1]);
							}
						} else if (args.length == 3) {
							rank_name = s.getRankIgnoreCase(args[1]);
							if (s.getGroups().contains(rank_name)) {
								int page = Integer.parseInt(args[2].replaceAll("[a-zA-Z]", ""));
								Messages.listRankPermissions(sender, s, rank_name, page);
							} else {
								Messages.messageGroupNotFound(player, args[1]);
							}
						} else {
							Messages.messageCommandUsageListPermissions(player);
						}

					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("listplayerpermissions")) {
					if (sender.hasPermission("powerranks.cmd.listplayerpermissions")) {
						if (args.length == 2) {
							String player_name = s.getRankIgnoreCase(args[1]);
							if (s.getPlayerNames().contains(player_name)) {
								Messages.listPlayerPermissions(sender, s, player_name, 0);
							} else {
								Messages.messagePlayerNotFound(player, args[1]);
							}
						} else if (args.length == 3) {
							String player_name = s.getRankIgnoreCase(args[1]);
							if (s.getPlayerNames().contains(player_name)) {
								int page = Integer.parseInt(args[2].replaceAll("[a-zA-Z]", ""));
								Messages.listPlayerPermissions(sender, s, player_name, page);
							} else {
								Messages.messagePlayerNotFound(player, args[1]);
							}
						} else {
							Messages.messageCommandUsageListPlayerPermissions(player);
						}

					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("checkrank")) {
					if (sender.hasPermission("powerranks.cmd.checkrank")) {
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
					if (sender.hasPermission("powerranks.cmd.addperm")) {
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
					if (sender.hasPermission("powerranks.cmd.delperm")) {
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
					if (sender.hasPermission("powerranks.cmd.addsubrank")) {
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
					if (sender.hasPermission("powerranks.cmd.delsubrank")) {
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
					if (sender.hasPermission("powerranks.cmd.enablesubrankprefix")) {
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
					if (sender.hasPermission("powerranks.cmd.disablesubrankprefix")) {
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
					if (sender.hasPermission("powerranks.cmd.enablesubranksuffix")) {
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
					if (sender.hasPermission("powerranks.cmd.disablesubranksuffix")) {
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
					if (sender.hasPermission("powerranks.cmd.enablesubrankpermissions")) {
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
					if (sender.hasPermission("powerranks.cmd.disablesubrankpermissions")) {
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
				} else if (args[0].equalsIgnoreCase("addsubrankworld")) {
					if (sender.hasPermission("powerranks.cmd.addsubrankworld")) {
						if (args.length == 4) {
							final String playername = args[1];
							final String subrank = s.getRankIgnoreCase(args[2]);
							final String worldname = args[3];
							final boolean result = s.addToSubrankList(playername, subrank, "worlds", worldname);
							if (result) {
								Messages.messageSuccessChangesubrank(player, subrank, playername);
							} else {
								Messages.messageErrorChangesubrank(player, subrank, playername);
							}
						} else {
							Messages.messageCommandUsageAddsubrankworld(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("delsubrankworld")) {
					if (sender.hasPermission("powerranks.cmd.delsubrankworld")) {
						if (args.length == 4) {
							final String playername = args[1];
							final String subrank = s.getRankIgnoreCase(args[2]);
							final String worldname = args[3];
							final boolean result = s.removeFromSubrankList(playername, subrank, "worlds", worldname);
							if (result) {
								Messages.messageSuccessChangesubrank(player, subrank, playername);
							} else {
								Messages.messageErrorChangesubrank(player, subrank, playername);
							}
						} else {
							Messages.messageCommandUsageDelsubrankworld(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("addinheritance")) {
					if (sender.hasPermission("powerranks.cmd.addinheritance")) {
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
					if (sender.hasPermission("powerranks.cmd.delinheritance")) {
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
					if (sender.hasPermission("powerranks.cmd.setprefix")) {
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
							String prefix = "";
							for (int i = 2; i < args.length; i++) {
								prefix += args[i] + " ";
							}
							prefix = prefix.substring(0, prefix.length() - 1);
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
					if (sender.hasPermission("powerranks.cmd.setsuffix")) {
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
					if (sender.hasPermission("powerranks.cmd.setchatcolor")) {
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
					if (sender.hasPermission("powerranks.cmd.setnamecolor")) {
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
					if (sender.hasPermission("powerranks.cmd.createrank")) {
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
					if (sender.hasPermission("powerranks.cmd.deleterank")) {
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
					if (sender.hasPermission("powerranks.cmd.enablebuild")) {
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
					if (sender.hasPermission("powerranks.cmd.disablebuild")) {
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
					if (sender.hasPermission("powerranks.cmd.promote")) {
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
					if (sender.hasPermission("powerranks.cmd.demote")) {
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
					if (sender.hasPermission("powerranks.cmd.renamerank")) {
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
					if (sender.hasPermission("powerranks.cmd.setdefaultrank")) {
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
					} else {
						Messages.noPermission(player);
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
				} else if (args[0].equalsIgnoreCase("buyrank")) {
					if (sender.hasPermission("powerranks.cmd.rankup")) {
						if (PowerRanks.vaultEconomyEnabled) {
							if (args.length == 1) {
								Messages.messageCommandBuyrank(sender, s, null);
							} else if (args.length == 2) {
								final String rankname = s.getRankIgnoreCase(args[1]);
								Messages.messageCommandBuyrank(sender, s, rankname);
							} else if (args.length == 3) {
								final String rankname = s.getRankIgnoreCase(args[1]);
								final boolean confirm = args[2].equalsIgnoreCase("confirm");

								if (confirm) {
									int cost = s.getRanksConfigFieldInt(rankname, "economy.cost");
									double player_balance = VaultHook.getVaultEconomy() != null ? VaultHook.getVaultEconomy().getBalance(player) : 0;
									if (cost >= 0 && player_balance >= cost) {
										VaultHook.getVaultEconomy().withdrawPlayer(player, cost);
										s.setGroup(player, rankname, true);
										Messages.messageBuyRankSuccess(player, rankname);
									} else {
										Messages.messageBuyRankError(player, rankname);
									}
								}
							} else {
								Messages.messageCommandUsageBuyrank(player);
							}
						} else {
							Messages.messageBuyRankNotAvailable(player);
						}
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("gui")) {
					if (sender.hasPermission("powerranks.cmd.gui")) {
						GUI.openGUI(player, GUI_PAGE_ID.MAIN);
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("rankup")) {
					if (sender.hasPermission("powerranks.cmd.rankup")) {
						if (PowerRanks.vaultEconomyEnabled)
							GUI.openGUI(player, GUI_PAGE_ID.RANKUP);
						else
							Messages.messageBuyRankNotAvailable(player);
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("stats")) {
					if (sender.hasPermission("powerranks.cmd.stats")) {
						Messages.messageStats(player);
					} else {
						Messages.noPermission(player);
					}
				} else if (args[0].equalsIgnoreCase("addbuyablerank")) {
					if (sender.hasPermission("powerranks.cmd.addbuyablerank")) {
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
					if (sender.hasPermission("powerranks.cmd.delbuyablerank")) {
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
				} else if (args[0].equalsIgnoreCase("setbuycost")) {
					if (sender.hasPermission("powerranks.cmd.setbuycost")) {
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
					if (sender.hasPermission("powerranks.cmd.addplayerperm")) {
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
					if (sender.hasPermission("powerranks.cmd.delplayerperm")) {
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
						if (sender.hasPermission("powerranks.cmd.createusertag")) {
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
						if (sender.hasPermission("powerranks.cmd.editusertag")) {
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
						if (sender.hasPermission("powerranks.cmd.removeusertag")) {
							if (args.length == 2) {
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
							if (sender.hasPermission("powerranks.cmd.setusertag.player")) {
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
							if (sender.hasPermission("powerranks.cmd.setusertag.admin")) {
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
							if (sender.hasPermission("powerranks.cmd.clearusertag.player")) {
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
							if (sender.hasPermission("powerranks.cmd.clearusertag.admin")) {
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
						if (sender.hasPermission("powerranks.cmd.listusertags")) {
							if (args.length == 1) {
								Set<String> tags = s.getUserTags();
								player.sendMessage("Usertags(" + tags.size() + "):");
								for (String tag : tags) {
									player.sendMessage(tag + " - " + PowerRanks.chatColor(s.getUserTagValue(tag), true));
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
					if (sender.hasPermission("powerranks.cmd.setpromoterank")) {
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
					if (sender.hasPermission("powerranks.cmd.setdemoterank")) {
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
					if (sender.hasPermission("powerranks.cmd.clearpromoterank")) {
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
					if (sender.hasPermission("powerranks.cmd.cleardemoterank")) {
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
					if (sender.hasPermission("powerranks.cmd.addoninfo")) {
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

				} else if (args[0].equalsIgnoreCase("setguiicon")) {
					if (player.hasPermission("powerranks.cmd.setguiicon")) {
						if (args.length == 2) {
							String rankName = s.getRankIgnoreCase(args[1]);
							Material material = player.getInventory().getItemInMainHand().getType();
							if (material != Material.AIR) {
								s.setRanksConfigFieldString(rankName, "gui.icon", material.name().toLowerCase());
								Messages.messageSuccessSetIcon(player, material.name().toLowerCase(), rankName);
							} else {
								Messages.messageErrorMustHoldItem(player);
							}

						} else {
							Messages.messageCommandUsageSeticon(player);
						}
					} else {
						Messages.noPermission(player);
					}

				} else if (args[0].equalsIgnoreCase("verbose")) {
					if (player.hasPermission("powerranks.cmd.verbose")) {
						if (args.length == 1) {
							Messages.checkVerbose(player);
						} else if (args.length == 2) {
							String verboseType = args[1].toLowerCase();
							if (verboseType.equals("start")) {
								if (!PowerRanksVerbose.USE_VERBOSE) {
									PowerRanksVerbose.start(false);
									Messages.messageCommandVerboseStarted(player);
								} else {
									Messages.messageCommandVerboseAlreadyRunning(player);
								}
							} else if (verboseType.equals("startlive")) {
								if (!PowerRanksVerbose.USE_VERBOSE) {
									PowerRanksVerbose.start(true);
									Messages.messageCommandVerboseStarted(player);
								} else {
									Messages.messageCommandVerboseAlreadyRunning(player);
								}
							} else if (verboseType.equals("stop")) {
								if (PowerRanksVerbose.USE_VERBOSE) {
									PowerRanksVerbose.stop();
									Messages.messageCommandVerboseStopped(player);
								} else {
									Messages.messageCommandVerboseNotRunning(player);
								}
							} else if (verboseType.equals("save")) {
								if (!PowerRanksVerbose.USE_VERBOSE) {
									if (PowerRanksVerbose.save()) {
										Messages.messageCommandVerboseSaved(player);
									} else {
										Messages.messageCommandErrorSavingVerbose(player);
									}
								} else {
									Messages.messageCommandVerboseMustStopBeforeSaving(player);
								}
							} else {
								Messages.messageCommandUsageVerbose(player);
							}
						} else {
							Messages.messageCommandUsageVerbose(player);
						}
					} else {
						Messages.noPermission(player);
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
					console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
					console.sendMessage(ChatColor.DARK_GREEN + "Number of ranks: " + ChatColor.GREEN + ranks.size());
					int index = 0;
					for (String rank : ranks) {
						index++;
						console.sendMessage(ChatColor.DARK_GREEN + "#" + index + ". " + ChatColor.GREEN + rank + ChatColor.RESET + " " + PowerRanks.chatColor(s.getPrefix(rank), true));
					}
					console.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
				} else if (args[0].equalsIgnoreCase("listsubranks")) {
					if (args.length == 2) {
						if (Bukkit.getPlayer(args[1]) != null) {
							List<String> subranks = s.getSubranks(args[1]);
							console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
							console.sendMessage(ChatColor.DARK_GREEN + "Subranks from player: " + ChatColor.GREEN + Bukkit.getPlayer(args[1]).getName());
							console.sendMessage(ChatColor.DARK_GREEN + "Number of subranks: " + ChatColor.GREEN + subranks.size());
							int index = 0;
							for (String subrank : subranks) {
								index++;
								console.sendMessage(ChatColor.DARK_GREEN + "#" + index + ". " + ChatColor.GREEN + subrank + ChatColor.RESET + " " + PowerRanks.chatColor(s.getPrefix(subrank), true));
							}
							console.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
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

							console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
							console.sendMessage(ChatColor.DARK_GREEN + "Permissions of rank: " + ChatColor.GREEN + s.getRankIgnoreCase(args[1]));
							console.sendMessage(ChatColor.DARK_GREEN + "Number of permissions: " + ChatColor.GREEN + permissions.size());
							int index = 0;
							for (String permission : permissions) {
								index++;
								console.sendMessage(ChatColor.DARK_GREEN + "#" + index + ". " + (permission.charAt(0) == '-' ? ChatColor.RED : ChatColor.GREEN) + permission);
							}
							console.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
						} else {
							Messages.messageGroupNotFound(console, args[1]);
						}
					} else {
						Messages.messageCommandUsageListPermissions(console);
					}
				} else if (args[0].equalsIgnoreCase("listplayerpermissions")) {
					if (args.length == 2) {
						if (s.getPlayerNames().contains(s.getRankIgnoreCase(args[1]))) {
							List<String> permissions = s.getPlayerPermissions(args[1]);
							console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
							console.sendMessage(ChatColor.DARK_GREEN + "Permissions of player: " + ChatColor.GREEN + args[1]);
							console.sendMessage(ChatColor.DARK_GREEN + "Number of permissions: " + ChatColor.GREEN + permissions.size());
							int index = 0;
							for (String permission : permissions) {
								index++;
								console.sendMessage(ChatColor.DARK_GREEN + "#" + index + ". " + (permission.charAt(0) == '-' ? ChatColor.RED : ChatColor.GREEN) + permission);
							}
							console.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
						} else {
							Messages.messagePlayerNotFound(console, args[1]);
						}
					} else {
						Messages.messageCommandUsageListPlayerPermissions(console);
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
				} else if (args[0].equalsIgnoreCase("addsubrankworld")) {
					if (args.length == 4) {
						final String playername = args[1];
						final String subrank = s.getRankIgnoreCase(args[2]);
						final String worldname = args[3];
						final boolean result = s.addToSubrankList(playername, subrank, "worlds", worldname);
						if (result) {
							Messages.messageSuccessChangesubrank(console, subrank, playername);
						} else {
							Messages.messageErrorChangesubrank(console, subrank, playername);
						}
					} else {
						Messages.messageCommandUsageAddsubrankworld(console);
					}
				} else if (args[0].equalsIgnoreCase("delsubrankworld")) {
					if (args.length == 4) {
						final String playername = args[1];
						final String subrank = s.getRankIgnoreCase(args[2]);
						final String worldname = args[3];
						final boolean result = s.removeFromSubrankList(playername, subrank, "worlds", worldname);
						if (result) {
							Messages.messageSuccessChangesubrank(console, subrank, playername);
						} else {
							Messages.messageErrorChangesubrank(console, subrank, playername);
						}
					} else {
						Messages.messageCommandUsageDelsubrankworld(console);
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
					if (args.length == 2) {
						final String rank2 = s.getRankIgnoreCase(args[1]);
						final String prefix = "";
						final boolean result = s.setPrefix(rank2, prefix);
						if (result) {
							Messages.messageCommandSetPrefix(console, prefix, rank2);
						} else {
							Messages.messageGroupNotFound(console, rank2);
						}
					} else if (args.length >= 3) {
						final String rank2 = s.getRankIgnoreCase(args[1]);
						String prefix = "";
						for (int i = 2; i < args.length; i++) {
							prefix += args[i] + " ";
						}
						prefix = prefix.substring(0, prefix.length() - 1);

						final boolean result = s.setPrefix(rank2, prefix);
						if (result) {
							Messages.messageCommandSetPrefix(console, prefix, rank2);
						} else {
							Messages.messageGroupNotFound(console, rank2);
						}
					} else {
						Messages.messageCommandUsageSetPrefix(console);
					}
				} else if (args[0].equalsIgnoreCase("setsuffix")) {
					if (args.length == 2) {
						final String rank2 = s.getRankIgnoreCase(args[1]);
						final String suffix = "";
						final boolean result = s.setSuffix(rank2, suffix);
						if (result) {
							Messages.messageCommandSetSuffix(console, suffix, rank2);
						} else {
							Messages.messageGroupNotFound(console, rank2);
						}
					} else if (args.length >= 3) {
						final String rank2 = s.getRankIgnoreCase(args[1]);
						String suffix = "";
						for (int i = 2; i < args.length; i++) {
							suffix += args[i] + " ";
						}
						suffix = suffix.substring(0, suffix.length() - 1);
						final boolean result = s.setSuffix(rank2, suffix);
						if (result) {
							Messages.messageCommandSetSuffix(console, suffix, rank2);
						} else {
							Messages.messageGroupNotFound(console, rank2);
						}
					} else {
						Messages.messageCommandUsageSetSuffix(console);
					}
				} else if (args[0].equalsIgnoreCase("setchatcolor")) {
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
				} else if (args[0].equalsIgnoreCase("setnamecolor")) {
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
				} else if (args[0].equalsIgnoreCase("createrank")) {
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
				} else if (args[0].equalsIgnoreCase("deleterank")) {
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
				} else if (args[0].equalsIgnoreCase("enablebuild")) {
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
				} else if (args[0].equalsIgnoreCase("setbuycost")) {
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
						if (args.length == 2) {
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
								console.sendMessage(tag + " - " + PowerRanks.chatColor(s.getUserTagValue(tag), true));
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
				} else if (args[0].equalsIgnoreCase("verbose")) {
					if (args.length == 1) {
						Messages.checkVerbose(console);
					} else if (args.length == 2) {
						String verboseType = args[1].toLowerCase();
						if (verboseType.equals("start")) {
							if (!PowerRanksVerbose.USE_VERBOSE) {
								PowerRanksVerbose.start(false);
								Messages.messageCommandVerboseStarted(console);
							} else {
								Messages.messageCommandVerboseAlreadyRunning(console);
							}
						} else if (verboseType.equals("startlive")) {
							if (!PowerRanksVerbose.USE_VERBOSE) {
								PowerRanksVerbose.start(true);
								Messages.messageCommandVerboseStarted(console);
							} else {
								Messages.messageCommandVerboseAlreadyRunning(console);
							}
						} else if (verboseType.equals("stop")) {
							if (PowerRanksVerbose.USE_VERBOSE) {
								PowerRanksVerbose.stop();
								Messages.messageCommandVerboseStopped(console);
							} else {
								Messages.messageCommandVerboseNotRunning(console);
							}
						} else if (verboseType.equals("save")) {
							if (!PowerRanksVerbose.USE_VERBOSE) {
								if (PowerRanksVerbose.save()) {
									Messages.messageCommandVerboseSaved(console);
								} else {
									Messages.messageCommandErrorSavingVerbose(console);
								}
							} else {
								Messages.messageCommandVerboseMustStopBeforeSaving(console);
							}
						} else {
							Messages.messageCommandUsageVerbose(console);
						}
					} else {
						Messages.messageCommandUsageVerbose(console);
					}
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
		} else if (sender instanceof BlockCommandSender) { // TODO nothing todo just easy navigation
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
						} else if (args.length >= 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							String prefix = "";
							for (int i = 2; i < args.length; i++) {
								prefix += args[i] + " ";
							}
							prefix = prefix.substring(0, prefix.length() - 1);
							s.setPrefix(rank2, prefix);
						}
					}
				} else if (args[0].equalsIgnoreCase("setsuffix")) {
					if (sender.hasPermission("powerranks.cmd.set")) {
						if (args.length == 2) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							final String suffix = "";
							s.setSuffix(rank2, suffix);
						} else if (args.length >= 3) {
							final String rank2 = s.getRankIgnoreCase(args[1]);
							String suffix = "";
							for (int i = 2; i < args.length; i++) {
								suffix += args[i] + " ";
							}
							suffix = suffix.substring(0, suffix.length() - 1);
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
				} else if (args[0].equalsIgnoreCase("setbuycost")) {
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