package nl.svenar.PowerRanks.Commands;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.VaultHook;
import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.PowerRanksVerbose;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.addons.DownloadableAddon;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;
import nl.svenar.PowerRanks.addons.PowerRanksPlayer;
import nl.svenar.PowerRanks.gui.GUI;
import nl.svenar.PowerRanks.gui.GUIPage.GUI_PAGE_ID;

public class Cmd implements CommandExecutor {
	PowerRanks plugin;
	Users users;

	public Cmd(PowerRanks plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		users = new Users(this.plugin);
		if (sender instanceof Player) { // TODO nothing TODO just easy navigation
			final Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("powerranks") || cmd.getName().equalsIgnoreCase("pr")) {
				if (PowerRanksVerbose.USE_VERBOSE) {
					String argstmp = "";
					for (String arg : args)
						argstmp += " " + arg;
					PowerRanksVerbose.log("onCommand", "Sender: " + sender.getName() + ", Command(" + cmd + "): /pr" + argstmp);
				}

				if (args.length == 0) {
					Messages.messageNoArgs(sender);

























					
































					







































					


					






					
				} else if (args[0].equalsIgnoreCase("addonmanager")) {
					if (player.hasPermission("powerranks.cmd.addonmanager")) {
						
					} else {
						Messages.noPermission(sender);
					}






					
				} else if (args[0].equalsIgnoreCase("playerinfo")) {
					if (player.hasPermission("powerranks.cmd.playerinfo")) {
						if (args.length == 1) {
							String target_player_name = args[0];
							Player target_player = Util.getPlayerByName(target_player_name);
							if (target_player != null) {
								Messages.messagePlayerInfo(sender, target_player);
							} else {
								Messages.messagePlayerNotFound(sender, target_player_name);
							}

						} else {
							Messages.messageCommandUsagePlayerinfo(sender);
						}
					} else {
						Messages.noPermission(sender);
					}




					
				} else if (args[0].equalsIgnoreCase("verbose")) {
					if (player.hasPermission("powerranks.cmd.verbose")) {
						if (args.length == 0) {
							Messages.checkVerbose(sender);
						} else if (args.length == 1) {
							String verboseType = args[0].toLowerCase();
							if (verboseType.equals("start")) {
								if (!PowerRanksVerbose.USE_VERBOSE) {
									PowerRanksVerbose.start(false);
									Messages.messageCommandVerboseStarted(sender);
								} else {
									Messages.messageCommandVerboseAlreadyRunning(sender);
								}
							} else if (verboseType.equals("startlive")) {
								if (!PowerRanksVerbose.USE_VERBOSE) {
									PowerRanksVerbose.start(true);
									Messages.messageCommandVerboseStarted(sender);
								} else {
									Messages.messageCommandVerboseAlreadyRunning(sender);
								}
							} else if (verboseType.equals("stop")) {
								if (PowerRanksVerbose.USE_VERBOSE) {
									PowerRanksVerbose.stop();
									Messages.messageCommandVerboseStopped(sender);
								} else {
									Messages.messageCommandVerboseNotRunning(sender);
								}
							} else if (verboseType.equals("clear")) {
								PowerRanksVerbose.clear();
								Messages.messageCommandVerboseCleared(sender);
							} else if (verboseType.equals("save")) {
								if (!PowerRanksVerbose.USE_VERBOSE) {
									if (PowerRanksVerbose.save()) {
										Messages.messageCommandVerboseSaved(sender);
									} else {
										Messages.messageCommandErrorSavingVerbose(sender);
									}
								} else {
									Messages.messageCommandVerboseMustStopBeforeSaving(sender);
								}
							} else {
								Messages.messageCommandUsageVerbose(sender);
							}
						} else {
							Messages.messageCommandUsageVerbose(sender);
						}
					} else {
						Messages.noPermission(sender);
					}




					
				} else if (args[0].equalsIgnoreCase("pluginhook")) {
					if (player.hasPermission("powerranks.cmd.pluginhook")) {
						if (args.length == 0) {
							Messages.messagePluginhookStats(sender);
						} else if (args.length == 2) {
							String state = args[0];
							String pluginname = args[1];
							if ((state.equalsIgnoreCase("enable") || state.equalsIgnoreCase("disable")) && CachedConfig.contains("plugin_hook." + pluginname.toLowerCase())) {
								CachedConfig.set("plugin_hook." + pluginname.toLowerCase(), state.equalsIgnoreCase("enable"));
								Messages.pluginhookStateChanged(sender, pluginname.toLowerCase(), (state.equalsIgnoreCase("enable") ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));
							} else {
								if (state.equalsIgnoreCase("enable") || state.equalsIgnoreCase("disable")) {
									Messages.pluginhookUnknownPlugin(sender);
								} else {
									Messages.pluginhookUnknownState(sender);
								}
							}
						} else {
							Messages.messageCommandUsagePluginhook(sender);
						}
					} else {
						Messages.noPermission(sender);
					}





				} else if (args[0].equalsIgnoreCase("config")) {
					if (player.hasPermission("powerranks.cmd.config")) {
						if (args.length == 1) {
							if (args[0].equalsIgnoreCase("removeworldtag")) {
								String world_tag_regex = "[ ]{0,1}([&][a-fA-F0-9k-oK-OrR]){0,1}[\\[]world[\\]]([&][a-fA-F0-9k-oK-OrR]){0,1}[ ]{0,1}";
								Pattern world_tag_pattern = Pattern.compile(world_tag_regex);
								Matcher world_tag_matcher_chat = world_tag_pattern.matcher(CachedConfig.getString("chat.format").toLowerCase());
								Matcher world_tag_matcher_tab = world_tag_pattern.matcher(CachedConfig.getString("tablist_modification.format").toLowerCase());

								while (world_tag_matcher_chat.find()) {
									int start = world_tag_matcher_chat.start();
									int end = world_tag_matcher_chat.end();
									CachedConfig.set("chat.format", CachedConfig.getString("chat.format").replace(CachedConfig.getString("chat.format").substring(start, end), ""));
								}

								while (world_tag_matcher_tab.find()) {
									int start = world_tag_matcher_tab.start();
									int end = world_tag_matcher_tab.end();
									CachedConfig.set("tablist_modification.format", CachedConfig.getString("tablist_modification.format").replace(CachedConfig.getString("tablist_modification.format").substring(start, end), ""));
								}

								plugin.updateAllPlayersTABlist();

								Messages.configWorldTagRemoved(sender);
							} else {
								Messages.messageCommandUsageConfig(sender);
							}
						} else if (args.length == 2) {
							if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable")) {
								boolean enable = args[0].equalsIgnoreCase("enable");
								if (args[1].equalsIgnoreCase("chat_formatting")) {
									CachedConfig.set("chat.enabled", enable);
									Messages.configStateChanged(sender, "Chat formatting", (enable ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));
								} else if (args[1].equalsIgnoreCase("tablist_formatting")) {
									CachedConfig.set("tablist_modification.enabled", enable);
									Messages.configStateChanged(sender, "Tablist formatting", (enable ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));
								} else {
									Messages.messageCommandUsageConfig(sender);
								}
							} else {
								Messages.messageCommandUsageConfig(sender);
							}
						} else {
							Messages.messageCommandUsageConfig(sender);
						}
					} else {
						Messages.noPermission(sender);
					}
				} else {
					boolean addonCommandFound = false;
					for (Entry<File, PowerRanksAddon> prAddon : this.plugin.addonsManager.addonClasses.entrySet()) {
						PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.plugin, player);
						if (prAddon.getValue().onPowerRanksCommand(prPlayer, true, args[0], args)) {
							addonCommandFound = true;
						}
					}
					if (!addonCommandFound)
						Messages.unknownCommand(sender);
				}
			}
























































// 		} else if (sender instanceof ConsoleCommandSender) { // TODO nothing TODO just easy navigation
// 			final ConsoleCommandSender console = (ConsoleCommandSender) sender;
// 			if (cmd.getName().equalsIgnoreCase("powerranks") || cmd.getName().equalsIgnoreCase("pr")) {
// 				if (args.length == 0) {
// 					Messages.messageNoArgs(console);
// 				} else if (args[0].equalsIgnoreCase("reload")) {
// 					if (args.length != 2) {
// 						Messages.messageCommandUsageReload(sender);
// 					} else {
// 						if (args[0].equalsIgnoreCase("config")) {
// 							Messages.messageCommandReloadConfig(sender);
// //							this.plugin.reloadConfig();
// 							CachedConfig.update();
// 							CachedRanks.update();
// 							CachedPlayers.update();
// 							this.plugin.updateAllPlayersTABlist();
// 							Messages.messageCommandReloadConfigDone(sender);
// 						} else if (args[0].equalsIgnoreCase("plugin")) {
// 							Messages.messageCommandReloadPlugin(sender);
// 							final PluginManager plg = Bukkit.getPluginManager();
// 							final Plugin plgname = plg.getPlugin(PowerRanks.pdf.getName());
// 							plg.disablePlugin(plgname);
// 							plg.enablePlugin(plgname);
// 							Messages.messageCommandReloadPluginDone(sender);
// 						} else if (args[0].equalsIgnoreCase("addons")) {
// 							Messages.messageCommandReloadAddons(sender);
// 							PowerRanks.getInstance().addonsManager.disable();
// 							PowerRanks.getInstance().addonsManager.setup();
// 							Messages.messageCommandReloadAddonsDone(sender);
// 						} else if (args[0].equalsIgnoreCase("all")) {
// 							Messages.messageCommandReloadPlugin(sender);
// 							final PluginManager plg = Bukkit.getPluginManager();
// 							final Plugin plgname = plg.getPlugin(PowerRanks.pdf.getName());
// 							plg.disablePlugin(plgname);
// 							plg.enablePlugin(plgname);
// 							Messages.messageCommandReloadPluginDone(sender);
// 							Messages.messageCommandReloadConfig(sender);
// 							CachedConfig.update();
// 							CachedRanks.update();
// 							CachedPlayers.update();
// 							this.plugin.updateAllPlayersTABlist();
// 							Messages.messageCommandReloadConfigDone(sender);
// 							Messages.messageCommandReloadAddons(sender);
// 							PowerRanks.getInstance().addonsManager.disable();
// 							PowerRanks.getInstance().addonsManager.setup();
// 							Messages.messageCommandReloadAddonsDone(sender);
// 						} else {
// 							Messages.messageCommandUsageReload(sender);
// 						}
// 					}
// 				} else if (args[0].equalsIgnoreCase("help")) {
// 					Messages.helpMenu(console);
// 				} else if (args[0].equalsIgnoreCase("setrank")) {
// 					if (args.length == 2) {
// 						this.users.setGroup(null, args[0], this.users.getRankIgnoreCase(args[1]), true);
// 					} else {
// 						Messages.messageCommandUsageSet(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("listranks")) {
// 					Set<String> ranks = this.users.getGroups();
// 					console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
// 					console.sendMessage(ChatColor.DARK_GREEN + "Number of ranks: " + ChatColor.GREEN + ranks.size());
// 					int index = 0;
// 					for (String rank : ranks) {
// 						index++;
// 						console.sendMessage(ChatColor.DARK_GREEN + "#" + index + ". " + ChatColor.GREEN + rank + ChatColor.RESET + " " + PowerRanks.chatColor(this.users.getPrefix(rank), true));
// 					}
// 					console.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
// 				} else if (args[0].equalsIgnoreCase("listsubranks")) {
// 					if (args.length == 1) {
// 						if (Bukkit.getPlayer(args[0]) != null) {
// 							List<String> subranks = this.users.getSubranks(args[0]);
// 							console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
// 							console.sendMessage(ChatColor.DARK_GREEN + "Subranks from player: " + ChatColor.GREEN + Bukkit.getPlayer(args[0]).getName());
// 							console.sendMessage(ChatColor.DARK_GREEN + "Number of subranks: " + ChatColor.GREEN + subranks.size());
// 							int index = 0;
// 							for (String subrank : subranks) {
// 								index++;
// 								console.sendMessage(ChatColor.DARK_GREEN + "#" + index + ". " + ChatColor.GREEN + subrank + ChatColor.RESET + " " + PowerRanks.chatColor(this.users.getPrefix(subrank), true));
// 							}
// 							console.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
// 						} else {
// 							Messages.messagePlayerNotFound(console, args[0]);
// 						}
// 					} else {
// 						Messages.messageCommandUsageListSubranks(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("listpermissions")) {
// 					if (args.length == 1) {
// 						if (this.users.getGroups().contains(this.users.getRankIgnoreCase(args[0]))) {
// 							List<String> permissions = this.users.getPermissions(this.users.getRankIgnoreCase(args[0]));

// 							console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
// 							console.sendMessage(ChatColor.DARK_GREEN + "Permissions of rank: " + ChatColor.GREEN + this.users.getRankIgnoreCase(args[0]));
// 							console.sendMessage(ChatColor.DARK_GREEN + "Number of permissions: " + ChatColor.GREEN + permissions.size());
// 							int index = 0;
// 							for (String permission : permissions) {
// 								index++;
// 								console.sendMessage(ChatColor.DARK_GREEN + "#" + index + ". " + (permission.charAt(0) == '-' ? ChatColor.RED : ChatColor.GREEN) + permission);
// 							}
// 							console.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
// 						} else {
// 							Messages.messageGroupNotFound(console, args[0]);
// 						}
// 					} else {
// 						Messages.messageCommandUsageListPermissions(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("listplayerpermissions")) {
					
// 				} else if (args[0].equalsIgnoreCase("listaddons")) {
// 					Collection<PowerRanksAddon> addons = this.plugin.addonsManager.getAddons();
// 					sender.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
// 					sender.sendMessage(ChatColor.DARK_GREEN + "Number of add-ons: " + ChatColor.GREEN + addons.size());
// 					int index = 0;
// 					for (Iterator<PowerRanksAddon> iterator = addons.iterator(); iterator.hasNext();) {
// 						PowerRanksAddon addon = iterator.next();
// 						index++;
// 						sender.sendMessage(ChatColor.DARK_GREEN + "#" + index + ". " + ChatColor.GREEN + addon.getIdentifier() + " v" + addon.getVersion());
// 					}
// 					sender.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
// 				} else if (args[0].equalsIgnoreCase("checkrank")) {
// 					if (args.length == 1) {
// 						this.users.getGroup(null, args[0]);
// 					} else {
// 						Messages.messageCommandUsageCheck(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("addperm")) {
// 					if (args.length == 2) {
// 						final String rankname = args[0].equals("*") ? args[0] : this.users.getRankIgnoreCase(args[0]);
// 						final String permission = args[1];
// 						final boolean result = this.users.addPermission(rankname, permission);
// 						if (result) {
// 							if (rankname.equals("*")) {
// 								Messages.messageCommandPermissionAddedToAllRanks(console, permission);
// 							} else {
// 								Messages.messageCommandPermissionAdded(console, permission, rankname);
// 							}
// 						} else {
// 							Messages.messageErrorAddingPermission(console, rankname, permission);
// 						}
// 					} else {
// 						Messages.messageCommandUsageAddperm(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("delperm")) {
// 					if (args.length == 2) {
// 						final String rankname = args[0].equals("*") ? args[0] : this.users.getRankIgnoreCase(args[0]);
// 						final String permission = args[1];
// 						final boolean result = this.users.removePermission(rankname, permission);
// 						if (result) {
// 							if (rankname == "*") {
// 								Messages.messageCommandPermissionRemovedFromAllRanks(console, permission);
// 							} else {
// 								Messages.messageCommandPermissionRemoved(console, permission, rankname);
// 							}
// 						} else {
// 							Messages.messageGroupNotFound(console, rankname);
// 						}
// 					} else {
// 						Messages.messageCommandUsageDelperm(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("addsubrank")) {
// 					if (args.length == 2) {
// 						final String playername = args[0];
// 						final String subrank = this.users.getRankIgnoreCase(args[1]);
// 						final boolean result = this.users.addSubrank(playername, subrank);
// 						if (result) {
// 							Messages.messageSuccessAddsubrank(console, subrank, playername);
// 						} else {
// 							Messages.messageErrorAddsubrank(console, subrank, playername);
// 						}
// 					} else {
// 						Messages.messageCommandUsageAddsubrank(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("delsubrank")) {
// 					if (args.length == 2) {
// 						final String playername = args[0];
// 						final String subrank = this.users.getRankIgnoreCase(args[1]);
// 						final boolean result = this.users.removeSubrank(playername, subrank);
// 						if (result) {
// 							Messages.messageSuccessDelsubrank(console, subrank, playername);
// 						} else {
// 							Messages.messageErrorDelsubrank(console, subrank, playername);
// 						}
// 					} else {
// 						Messages.messageCommandUsageDelsubrank(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("enablesubrankprefix")) {
// 					if (args.length == 2) {
// 						final String playername = args[0];
// 						final String subrank = this.users.getRankIgnoreCase(args[1]);
// 						final boolean result = this.users.changeSubrankField(playername, subrank, "use_prefix", true);
// 						if (result) {
// 							Messages.messageSuccessChangesubrank(console, subrank, playername);
// 						} else {
// 							Messages.messageErrorChangesubrank(console, subrank, playername);
// 						}
// 					} else {
// 						Messages.messageCommandUsageEnablesubrankprefix(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("disablesubrankprefix")) {
// 					if (args.length == 2) {
// 						final String playername = args[0];
// 						final String subrank = this.users.getRankIgnoreCase(args[1]);
// 						final boolean result = this.users.changeSubrankField(playername, subrank, "use_prefix", false);
// 						if (result) {
// 							Messages.messageSuccessChangesubrank(console, subrank, playername);
// 						} else {
// 							Messages.messageErrorChangesubrank(console, subrank, playername);
// 						}
// 					} else {
// 						Messages.messageCommandUsageDisablesubrankprefix(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("enablesubranksuffix")) {
// 					if (args.length == 2) {
// 						final String playername = args[0];
// 						final String subrank = this.users.getRankIgnoreCase(args[1]);
// 						final boolean result = this.users.changeSubrankField(playername, subrank, "use_suffix", true);
// 						if (result) {
// 							Messages.messageSuccessChangesubrank(console, subrank, playername);
// 						} else {
// 							Messages.messageErrorChangesubrank(console, subrank, playername);
// 						}
// 					} else {
// 						Messages.messageCommandUsageEnablesubranksuffix(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("disablesubranksuffix")) {
// 					if (args.length == 2) {
// 						final String playername = args[0];
// 						final String subrank = this.users.getRankIgnoreCase(args[1]);
// 						final boolean result = this.users.changeSubrankField(playername, subrank, "use_suffix", false);
// 						if (result) {
// 							Messages.messageSuccessChangesubrank(console, subrank, playername);
// 						} else {
// 							Messages.messageErrorChangesubrank(console, subrank, playername);
// 						}
// 					} else {
// 						Messages.messageCommandUsageDisablesubranksuffix(console);
// 					}

// 				} else if (args[0].equalsIgnoreCase("enablesubrankpermissions")) {
// 					if (args.length == 2) {
// 						final String playername = args[0];
// 						final String subrank = this.users.getRankIgnoreCase(args[1]);
// 						final boolean result = this.users.changeSubrankField(playername, subrank, "use_permissions", true);
// 						if (result) {
// 							Messages.messageSuccessChangesubrank(console, subrank, playername);
// 						} else {
// 							Messages.messageErrorChangesubrank(console, subrank, playername);
// 						}
// 					} else {
// 						Messages.messageCommandUsageEnablesubrankpermissions(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("disablesubrankpermissions")) {
// 					if (args.length == 2) {
// 						final String playername = args[0];
// 						final String subrank = this.users.getRankIgnoreCase(args[1]);
// 						final boolean result = this.users.changeSubrankField(playername, subrank, "use_permissions", false);
// 						if (result) {
// 							Messages.messageSuccessChangesubrank(console, subrank, playername);
// 						} else {
// 							Messages.messageErrorChangesubrank(console, subrank, playername);
// 						}
// 					} else {
// 						Messages.messageCommandUsageDisablesubrankpermissions(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("addsubrankworld")) {
// 					if (args.length == 3) {
// 						final String playername = args[0];
// 						final String subrank = this.users.getRankIgnoreCase(args[1]);
// 						final String worldname = args[2];
// 						final boolean result = this.users.addToSubrankList(playername, subrank, "worlds", worldname);
// 						if (result) {
// 							Messages.messageSuccessChangesubrank(console, subrank, playername);
// 						} else {
// 							Messages.messageErrorChangesubrank(console, subrank, playername);
// 						}
// 					} else {
// 						Messages.messageCommandUsageAddsubrankworld(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("delsubrankworld")) {
// 					if (args.length == 3) {
// 						final String playername = args[0];
// 						final String subrank = this.users.getRankIgnoreCase(args[1]);
// 						final String worldname = args[2];
// 						final boolean result = this.users.removeFromSubrankList(playername, subrank, "worlds", worldname);
// 						if (result) {
// 							Messages.messageSuccessChangesubrank(console, subrank, playername);
// 						} else {
// 							Messages.messageErrorChangesubrank(console, subrank, playername);
// 						}
// 					} else {
// 						Messages.messageCommandUsageDelsubrankworld(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("addinheritance")) {
// 					if (sender.hasPermission("powerranks.cmd.set")) {
// 						if (args.length == 2) {
// 							final String rankname = this.users.getRankIgnoreCase(args[0]);
// 							final String inheritance = args[1];
// 							final boolean result = this.users.addInheritance(rankname, inheritance);
// 							if (result) {
// 								Messages.messageCommandInheritanceAdded(console, inheritance, rankname);
// 							} else {
// 								Messages.messageGroupNotFound(console, rankname);
// 							}
// 						} else {
// 							Messages.messageCommandUsageAddInheritance(console);
// 						}
// 					}
// 				} else if (args[0].equalsIgnoreCase("delinheritance")) {
// 					if (sender.hasPermission("powerranks.cmd.set")) {
// 						if (args.length == 2) {
// 							final String rankname = this.users.getRankIgnoreCase(args[0]);
// 							final String inheritance = args[1];
// 							final boolean result = this.users.removeInheritance(rankname, inheritance);
// 							if (result) {
// 								Messages.messageCommandInheritanceRemoved(console, inheritance, rankname);
// 							} else {
// 								Messages.messageGroupNotFound(console, rankname);
// 							}
// 						} else {
// 							Messages.messageCommandUsageRemoveInheritance(console);
// 						}
// 					}
// 				} else if (args[0].equalsIgnoreCase("setprefix")) {
// 					if (args.length == 1) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						final String prefix = "";
// 						final boolean result = this.users.setPrefix(rankname, prefix);
// 						if (result) {
// 							Messages.messageCommandSetPrefix(console, prefix, rankname);
// 						} else {
// 							Messages.messageGroupNotFound(console, rankname);
// 						}
// 					} else if (args.length >= 3) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						String prefix = "";
// 						for (int i = 2; i < args.length; i++) {
// 							prefix += args[i] + " ";
// 						}
// 						prefix = prefix.substring(0, prefix.length() - 1);

// 						final boolean result = this.users.setPrefix(rankname, prefix);
// 						if (result) {
// 							Messages.messageCommandSetPrefix(console, prefix, rankname);
// 						} else {
// 							Messages.messageGroupNotFound(console, rankname);
// 						}
// 					} else {
// 						Messages.messageCommandUsageSetPrefix(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("setsuffix")) {
// 					if (args.length == 1) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						final String suffix = "";
// 						final boolean result = this.users.setSuffix(rankname, suffix);
// 						if (result) {
// 							Messages.messageCommandSetSuffix(console, suffix, rankname);
// 						} else {
// 							Messages.messageGroupNotFound(console, rankname);
// 						}
// 					} else if (args.length >= 3) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						String suffix = "";
// 						for (int i = 2; i < args.length; i++) {
// 							suffix += args[i] + " ";
// 						}
// 						suffix = suffix.substring(0, suffix.length() - 1);
// 						final boolean result = this.users.setSuffix(rankname, suffix);
// 						if (result) {
// 							Messages.messageCommandSetSuffix(console, suffix, rankname);
// 						} else {
// 							Messages.messageGroupNotFound(console, rankname);
// 						}
// 					} else {
// 						Messages.messageCommandUsageSetSuffix(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("setchatcolor")) {
// 					if (args.length == 2) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						final String color = args[1];
// 						final boolean result = this.users.setChatColor(rankname, color);
// 						if (result) {
// 							Messages.messageCommandSetChatColor(console, color, rankname);
// 						} else {
// 							Messages.messageGroupNotFound(console, rankname);
// 						}
// 					} else {
// 						Messages.messageCommandUsageSetChatColor(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("setnamecolor")) {
// 					if (args.length == 2) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						final String color = args[1];
// 						final boolean result = this.users.setNameColor(rankname, color);
// 						if (result) {
// 							Messages.messageCommandSetNameColor(console, color, rankname);
// 						} else {
// 							Messages.messageGroupNotFound(console, rankname);
// 						}
// 					} else {
// 						Messages.messageCommandUsageSetNameColor(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("createrank")) {
// 					if (args.length == 1) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						final boolean success = this.users.createRank(rankname);
// 						String[] forbiddenColorCharacters = {"&", "#"};
// 						String[] forbiddenCharacters = {"`", "~", "!", "@", "$", "%", "^", "*", "(", ")", "{", "}", "[", "]", ":", ";", "\"", "'", "|", "\\", "?", "/", ">", "<", ",", ".", "+", "="};
// 						if (success) {
// 							if (Util.stringContainsItemFromList(rankname, forbiddenColorCharacters)) {
// 								Messages.messageCommandCreateRankColorCharacterWarning(console, rankname);
// 							}

// 							if (Util.stringContainsItemFromList(rankname, forbiddenCharacters)) {
// 								Messages.messageCommandCreateRankCharacterWarning(console, rankname);
// 							}
// 							Messages.messageCommandCreateRankSuccess(console, rankname);
// 						} else {
// 							Messages.messageCommandCreateRankError(console, rankname);
// 						}
// 					} else {
// 						Messages.messageCommandUsageCreateRank(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("deleterank")) {
// 					if (args.length == 1) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						final boolean success = this.users.deleteRank(rankname);
// 						if (success) {
// 							Messages.messageCommandDeleteRankSuccess(console, rankname);
// 						} else {
// 							Messages.messageCommandDeleteRankError(console, rankname);
// 						}
// 					} else {
// 						Messages.messageCommandUsageDeleteRank(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("promote")) {
// 					if (args.length == 1) {
// 						final String playername = args[0];
// 						final boolean success = this.users.promote(playername);
// 						if (success) {
// 							Messages.messageCommandPromoteSuccess(console, playername);
// 						} else {
// 							Messages.messageCommandPromoteError(console, playername);
// 						}
// 					} else {
// 						Messages.messageCommandUsagePromote(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("demote")) {
// 					if (args.length == 1) {
// 						final String playername = args[0];
// 						final boolean success = this.users.demote(playername);
// 						if (success) {
// 							Messages.messageCommandDemoteSuccess(console, playername);
// 						} else {
// 							Messages.messageCommandDemoteError(console, playername);
// 						}
// 					} else {
// 						Messages.messageCommandUsageDemote(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("renamerank")) {
// 					if (args.length == 2) {
// 						final String from = this.users.getRankIgnoreCase(args[0]);
// 						final String to = args[1];
// 						final boolean success = this.users.renameRank(from, to);
// 						if (success) {
// 							Messages.messageCommandRenameRankSuccess(console, from);
// 						} else {
// 							Messages.messageCommandRenameRankError(console, from);
// 						}
// 					} else {
// 						Messages.messageCommandUsageDemote(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("setdefaultrank")) {
// 					if (args.length == 1) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						final boolean success = this.users.setDefaultRank(rankname);
// 						if (success) {
// 							Messages.messageCommandSetDefaultRankSuccess(console, rankname);
// 						} else {
// 							Messages.messageCommandSetDefaultRankError(console, rankname);
// 						}
// 					} else {
// 						Messages.messageCommandUsageDemote(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("factoryreset")) {
// 					if (args.length == 0) {
// 						Messages.messageCommandFactoryReset(console);
// 					} else if (args.length == 1) {
// 						if (PowerRanks.factoryresetid == null) {
// 							Messages.messageCommandFactoryReset(console);
// 						} else {
// 							String resetid = args[0];
// 							if (resetid.equalsIgnoreCase(PowerRanks.factoryresetid))
// 								this.plugin.factoryReset(sender);
// 							else
// 								Messages.messageCommandFactoryReset(console);
// 						}
// 					} else {
// 						Messages.messageCommandUsageFactoryReset(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("stats")) {
// 					Messages.messageStats(console);
// 				} else if (args[0].equalsIgnoreCase("addbuyablerank")) {
// 					if (args.length == 2) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						final String rankname2 = this.users.getRankIgnoreCase(args[1]);
// 						final boolean success = this.users.addBuyableRank(rankname, rankname2);
// 						if (success) {
// 							Messages.messageCommandAddbuyablerankSuccess(console, rankname, rankname2);
// 						} else {
// 							Messages.messageCommandAddbuyablerankError(console, rankname, rankname2);
// 						}
// 					} else {
// 						Messages.messageCommandUsageAddbuyablerank(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("delbuyablerank")) {
// 					if (args.length == 2) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						final String rankname2 = this.users.getRankIgnoreCase(args[1]);
// 						final boolean success = this.users.delBuyableRank(rankname, rankname2);
// 						if (success) {
// 							Messages.messageCommandDelbuyablerankSuccess(console, rankname, rankname2);
// 						} else {
// 							Messages.messageCommandDelbuyablerankError(console, rankname, rankname2);
// 						}
// 					} else {
// 						Messages.messageCommandUsageDelbuyablerank(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("setbuycost")) {
// 					if (args.length == 2) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						final String cost = this.users.getRankIgnoreCase(args[1]);
// 						final boolean success = this.users.setBuyCost(rankname, cost);
// 						if (success) {
// 							Messages.messageCommandSetcostSuccess(console, rankname, cost);
// 						} else {
// 							Messages.messageCommandSetcostError(console, rankname, cost);
// 						}
// 					} else {
// 						Messages.messageCommandUsageSetcost(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("setbuydescription")) {
//                     if (args.length >= 3) {
//                         final String rankname = this.users.getRankIgnoreCase(args[0]);
//                         String description = "";
//                         for (int i = 2; i < args.length; i++) {
//                             description = String.valueOf(description) + args[i] + " ";
//                         }
//                         description = description.substring(0, description.length() - 1);
//                         final boolean success2 = this.users.setBuyDescription(rankname, description);
//                         if (success2) {
//                             Messages.messageCommandSetbuydescriptionSuccess((CommandSender)console, rankname, description);
//                         } else {
//                             Messages.messageCommandSetbuydescriptionError((CommandSender)console, rankname, description);
//                         }
//                     } else {
//                         Messages.messageCommandUsageSetbuydescription((CommandSender)console);
//                     }
//                 } else if (args[0].equalsIgnoreCase("setbuycommand")) {
//                     if (args.length >= 3) {
//                         final String rankname = this.users.getRankIgnoreCase(args[0]);
//                         String command2 = "";
//                         for (int i = 2; i < args.length; i++) {
//                             command2 = String.valueOf(command2) + args[i] + " ";
//                         }
//                         command2 = command2.substring(0, command2.length() - 1);
//                         final boolean success2 = this.users.setBuyCommand(rankname, command2);
//                         if (success2) {
//                             Messages.messageCommandSetbuycommandSuccess((CommandSender)console, rankname, command2);
//                         } else {
//                             Messages.messageCommandSetbuycommandError((CommandSender)console, rankname, command2);
//                         }
//                     } else {
//                         Messages.messageCommandUsageSetbuycommand((CommandSender)console);
//                     }
//                 } else if (args[0].equalsIgnoreCase("addplayerperm")) {
// 					if (args.length == 2) {
// 						final String target_player = args[0];
// 						final String permission = args[1];
// 						final boolean result = this.users.addPlayerPermission(target_player, permission);
// 						if (result) {
// 							Messages.messageCommandPlayerPermissionAdded(console, permission, target_player);
// 						} else {
// 							Messages.messageErrorAddingPlayerPermission(console, target_player, permission);
// 						}
// 					} else {
// 						Messages.messageCommandUsageAddplayerperm(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("delplayerperm")) {
// 					if (args.length == 2) {
// 						final String target_player = args[0];
// 						final String permission = args[1];
// 						final boolean result = this.users.delPlayerPermission(target_player, permission);
// 						if (result) {
// 							Messages.messageCommandPlayerPermissionRemoved(console, permission, target_player);
// 						} else {
// 							Messages.messageErrorRemovingPlayerPermission(console, target_player, permission);
// 						}
// 					} else {
// 						Messages.messageCommandUsageDelplayerperm(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("createusertag")) {
// 					if (!PowerRanks.plugin_hook_deluxetags) {
// 						if (args.length == 2) {
// 							final String tag = args[0];
// 							final String text = args[1];
// 							final boolean result = this.users.createUserTag(tag, text);
// 							if (result) {
// 								Messages.messageCommandCreateusertagSuccess(console, tag, text);
// 							} else {
// 								Messages.messageCommandCreateusertagError(console, tag, text);
// 							}
// 						} else {
// 							Messages.messageCommandUsageCreateusertag(console);
// 						}
// 					} else {
// 						Messages.messageUsertagsDisabled(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("editusertag")) {
// 					if (!PowerRanks.plugin_hook_deluxetags) {
// 						if (args.length == 2) {
// 							final String tag = args[0];
// 							final String text = args[1];
// 							final boolean result = this.users.editUserTag(tag, text);
// 							if (result) {
// 								Messages.messageCommandEditusertagSuccess(console, tag, text);
// 							} else {
// 								Messages.messageCommandEditusertagError(console, tag, text);
// 							}
// 						} else {
// 							Messages.messageCommandUsageEditusertag(console);
// 						}
// 					} else {
// 						Messages.messageUsertagsDisabled(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("removeusertag")) {
// 					if (!PowerRanks.plugin_hook_deluxetags) {
// 						if (args.length == 1) {
// 							final String tag = args[0];
// 							final boolean result = this.users.removeUserTag(tag);
// 							if (result) {
// 								Messages.messageCommandRemoveusertagSuccess(console, tag);
// 							} else {
// 								Messages.messageCommandRemoveusertagError(console, tag);
// 							}
// 						} else {
// 							Messages.messageCommandUsageRemoveusertag(console);
// 						}
// 					} else {
// 						Messages.messageUsertagsDisabled(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("setusertag")) {
// 					if (!PowerRanks.plugin_hook_deluxetags) {
// 						if (args.length == 2) {
// 							final String player = args[0];
// 							final String tag = args[1];
// 							final boolean result = this.users.setUserTag(sender, tag);
// 							if (result) {
// 								Messages.messageCommandSetusertagSuccess(console, player, tag);
// 							} else {
// 								Messages.messageCommandSetusertagError(console, player, tag);
// 							}
// 						} else {
// 							Messages.messageCommandUsageSetusertag(console);
// 						}
// 					} else {
// 						Messages.messageUsertagsDisabled(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("clearusertag")) {
// 					if (!PowerRanks.plugin_hook_deluxetags) {
// 						if (args.length == 1) {
// 							final String player = args[0];
// 							final boolean result = this.users.clearUserTag(sender);
// 							if (result) {
// 								Messages.messageCommandClearusertagSuccess(console, player);
// 							} else {
// 								Messages.messageCommandClearusertagError(console, player);
// 							}
// 						} else {
// 							Messages.messageCommandUsageSetusertag(console);
// 						}
// 					} else {
// 						Messages.messageUsertagsDisabled(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("listusertags")) {
// 					if (!PowerRanks.plugin_hook_deluxetags) {
// 						if (args.length == 0) {
// 							Set<String> tags = this.users.getUserTags();
// 							console.sendMessage("Usertags(" + tags.size() + "):");
// 							for (String tag : tags) {
// 								console.sendMessage(tag + " - " + PowerRanks.chatColor(this.users.getUserTagValue(tag), true));
// 							}
// 						} else {
// 							Messages.messageCommandUsageListusertags(console);
// 						}
// 					} else {
// 						Messages.messageUsertagsDisabled(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("setpromoterank")) {
// 					if (args.length == 2) {
// 						final String rankname = args[0];
// 						final String promote_rank = args[1];
// 						if (this.users.setPromoteRank(rankname, promote_rank)) {
// 							Messages.messageCommandSetpromoterankSuccess(console, rankname, promote_rank);
// 						} else {
// 							Messages.messageCommandSetpromoterankError(console, rankname, promote_rank);
// 						}
// 					} else {
// 						Messages.messageCommandUsageSetpromoterank(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("setdemoterank")) {
// 					if (args.length == 2) {
// 						final String rankname = args[0];
// 						final String promote_rank = args[1];
// 						if (this.users.setDemoteRank(rankname, promote_rank)) {
// 							Messages.messageCommandSetdemoterankSuccess(console, rankname, promote_rank);
// 						} else {
// 							Messages.messageCommandSetdemoterankError(console, rankname, promote_rank);
// 						}
// 					} else {
// 						Messages.messageCommandUsageSetdemoterank(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("clearpromoterank")) {
// 					if (args.length == 1) {
// 						final String rankname = args[0];
// 						if (this.users.clearPromoteRank(rankname)) {
// 							Messages.messageCommandClearpromoterankSuccess(console, rankname);
// 						} else {
// 							Messages.messageCommandClearpromoterankError(console, rankname);
// 						}
// 					} else {
// 						Messages.messageCommandUsageClearpromoterank(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("cleardemoterank")) {
// 					if (args.length == 1) {
// 						final String rankname = args[0];
// 						if (this.users.clearDemoteRank(rankname)) {
// 							Messages.messageCommandCleardemoterankSuccess(console, rankname);
// 						} else {
// 							Messages.messageCommandCleardemoterankError(console, rankname);
// 						}
// 					} else {
// 						Messages.messageCommandUsageCleardemoterank(console);
// 					}

// 				} else if (args[0].equalsIgnoreCase("addoninfo")) {
// 					if (args.length == 1) {
// 						final String addon_name = args[0];
// 						PowerRanksAddon addon = null;
// 						for (Entry<File, PowerRanksAddon> a : this.plugin.addonsManager.addonClasses.entrySet()) {
// 							if (a.getValue().getIdentifier().equalsIgnoreCase(addon_name))
// 								addon = a.getValue();
// 						}
// 						if (addon != null) {
// 							console.sendMessage(ChatColor.DARK_AQUA + "--------" + ChatColor.DARK_BLUE + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "--------");
// 							console.sendMessage(ChatColor.DARK_GREEN + "Add-on name: " + ChatColor.GREEN + addon.getIdentifier());
// 							console.sendMessage(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + addon.getAuthor());
// 							console.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + addon.getVersion());
// 							console.sendMessage(ChatColor.DARK_GREEN + "Registered Commands:");
// 							for (String command : addon.getRegisteredCommands()) {
// 								console.sendMessage(ChatColor.GREEN + "- /pr " + command);
// 							}
// 							console.sendMessage(ChatColor.DARK_GREEN + "Registered Permissions:");
// 							for (String permission : addon.getRegisteredPermissions()) {
// 								console.sendMessage(ChatColor.GREEN + "- " + permission);
// 							}
// 							console.sendMessage(ChatColor.DARK_AQUA + "--------------------------");
// 						} else {
// 							Messages.messageCommandErrorAddonNotFound(console, addon_name);
// 						}
// 					} else {
// 						Messages.messageCommandUsageAddoninfo(console);
// 					}

// 				} else if (args[0].equalsIgnoreCase("addonmanager")) {
// 					boolean hasAcceptedTerms = CachedConfig.getBoolean("addon_manager.accepted_terms");
// 					if (args.length == 0) {
// 						Messages.addonManagerListAddons(sender, 0);
// 					} else {
// 						String addonmanagerCommand = args[0].toLowerCase();
// 						if (addonmanagerCommand.equals("acceptterms")) {
// 							CachedConfig.set("addon_manager.accepted_terms", true);
// 							Messages.addonManagerTermsAccepted(sender);
// 							this.plugin.addonsManager.setupAddonDownloader();
// 						}

// 						if (addonmanagerCommand.equals("declineterms")) {
// 							CachedConfig.set("addon_manager.accepted_terms", false);
// 							Messages.addonManagerTermsDeclined(sender);
// 						}

// 						if (addonmanagerCommand.equals("page")) {
// 							int page = Integer.parseInt(args[1].replaceAll("[a-zA-Z]", ""));
// 							Messages.addonManagerListAddons(sender, page);
// 						}

// 						if (addonmanagerCommand.equals("info")) {
// 							if (hasAcceptedTerms) {
// 								String addonname = args[1];
// 								Messages.addonManagerInfoAddon(sender, addonname);
// 							} else {
// 								Messages.addonManagerListAddons(sender, 0);
// 							}
// 						}

// 						if (addonmanagerCommand.equals("download")) {
// 							if (hasAcceptedTerms) {
// 								String addonname = args[1];
// 								DownloadableAddon addon = null;
// 								for (DownloadableAddon dlAddon : PowerRanks.getInstance().addonsManager.getAddonDownloader().getDownloadableAddons()) {
// 									if (dlAddon.getName().equalsIgnoreCase(addonname)) {
// 										addon = dlAddon;
// 										break;
// 									}
// 								}

// 								if (addon.isDownloadable()) {
// 									if (addon.isCompatible()) {
// 										if (addon.download()) {
// 											Messages.addonManagerDownloadComplete(sender, addon.getName());
// 										} else {
// 											Messages.addonManagerDownloadFailed(sender, addon.getName());
// 										}
// 									} else {
// 										Messages.addonManagerDownloadNotAvailable(sender);
// 									}
// 								} else {
// 									Messages.addonManagerDownloadNotAvailable(sender);
// 								}
// 							} else {
// 								Messages.addonManagerListAddons(sender, 0);
// 							}
// 						}

// 						if (addonmanagerCommand.equals("uninstall")) {
// 							if (hasAcceptedTerms) {
// 								String addonname = args[1];

// 								DownloadableAddon addon = null;
// 								if (PowerRanks.getInstance().addonsManager.getAddonDownloader() != null) {
// 									for (DownloadableAddon dlAddon : PowerRanks.getInstance().addonsManager.getAddonDownloader().getDownloadableAddons()) {
// 										if (dlAddon.getName().equalsIgnoreCase(addonname)) {
// 											addon = dlAddon;
// 											break;
// 										}
// 									}
// 								}

// 								if (addon != null) {
// 									addon.uninstall();
// 									Messages.addonManagerUninstallComplete(sender, addon.getName());
// 								} else {
// 									Messages.messageCommandErrorAddonNotFound(sender, args[1]);
// 								}
// 							} else {
// 								Messages.addonManagerListAddons(sender, 0);
// 							}
// 						}
// 					}

// 				} else if (args[0].equalsIgnoreCase("playerinfo")) {
// 					if (args.length == 1) {
// 						String target_player_name = args[0];
// 						Player target_player = Util.getPlayerByName(target_player_name);
// 						if (target_player != null) {
// 							Messages.messagePlayerInfo(console, target_player);
// 						} else {
// 							Messages.messagePlayerNotFound(console, target_player_name);
// 						}

// 					} else {
// 						Messages.messageCommandUsagePlayerinfo(console);
// 					}

// 				} else if (args[0].equalsIgnoreCase("verbose")) {
// 					if (args.length == 0) {
// 						Messages.checkVerbose(console);
// 					} else if (args.length == 1) {
// 						String verboseType = args[0].toLowerCase();
// 						if (verboseType.equals("start")) {
// 							if (!PowerRanksVerbose.USE_VERBOSE) {
// 								PowerRanksVerbose.start(false);
// 								Messages.messageCommandVerboseStarted(console);
// 							} else {
// 								Messages.messageCommandVerboseAlreadyRunning(console);
// 							}
// 						} else if (verboseType.equals("startlive")) {
// 							if (!PowerRanksVerbose.USE_VERBOSE) {
// 								PowerRanksVerbose.start(true);
// 								Messages.messageCommandVerboseStarted(console);
// 							} else {
// 								Messages.messageCommandVerboseAlreadyRunning(console);
// 							}
// 						} else if (verboseType.equals("stop")) {
// 							if (PowerRanksVerbose.USE_VERBOSE) {
// 								PowerRanksVerbose.stop();
// 								Messages.messageCommandVerboseStopped(console);
// 							} else {
// 								Messages.messageCommandVerboseNotRunning(console);
// 							}
// 						} else if (verboseType.equals("clear")) {
// 							PowerRanksVerbose.clear();
// 							Messages.messageCommandVerboseCleared(console);
// 						} else if (verboseType.equals("save")) {
// 							if (!PowerRanksVerbose.USE_VERBOSE) {
// 								if (PowerRanksVerbose.save()) {
// 									Messages.messageCommandVerboseSaved(console);
// 								} else {
// 									Messages.messageCommandErrorSavingVerbose(console);
// 								}
// 							} else {
// 								Messages.messageCommandVerboseMustStopBeforeSaving(console);
// 							}
// 						} else {
// 							Messages.messageCommandUsageVerbose(console);
// 						}
// 					} else {
// 						Messages.messageCommandUsageVerbose(console);
// 					}
// 				} else if (args[0].equalsIgnoreCase("pluginhook")) {
// 					if (args.length == 0) {
// 						Messages.messagePluginhookStats(sender);
// 					} else if (args.length == 2) {
// 						String state = args[0];
// 						String pluginname = args[1];
// 						if ((state.equalsIgnoreCase("enable") || state.equalsIgnoreCase("disable")) && CachedConfig.contains("plugin_hook." + pluginname.toLowerCase())) {
// 							CachedConfig.set("plugin_hook." + pluginname.toLowerCase(), state.equalsIgnoreCase("enable"));
// 							Messages.pluginhookStateChanged(sender, pluginname.toLowerCase(), (state.equalsIgnoreCase("enable") ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));
// 						} else {
// 							if (state.equalsIgnoreCase("enable") || state.equalsIgnoreCase("disable")) {
// 								Messages.pluginhookUnknownPlugin(sender);
// 							} else {
// 								Messages.pluginhookUnknownState(sender);
// 							}
// 						}
// 					} else {
// 						Messages.messageCommandUsagePluginhook(sender);
// 					}
// 				} else if (args[0].equalsIgnoreCase("config")) {
// 					if (args.length == 1) {
// 						if (args[0].equalsIgnoreCase("removeworldtag")) {
// 							String world_tag_regex = "[ ]{0,1}([&][a-fA-F0-9k-oK-OrR]){0,1}[\\[]world[\\]]([&][a-fA-F0-9k-oK-OrR]){0,1}[ ]{0,1}";
// 							Pattern world_tag_pattern = Pattern.compile(world_tag_regex);
// 							Matcher world_tag_matcher_chat = world_tag_pattern.matcher(CachedConfig.getString("chat.format").toLowerCase());
// 							Matcher world_tag_matcher_tab = world_tag_pattern.matcher(CachedConfig.getString("tablist_modification.format").toLowerCase());

// 							while (world_tag_matcher_chat.find()) {
// 								int start = world_tag_matcher_chat.start();
// 								int end = world_tag_matcher_chat.end();
// 								CachedConfig.set("chat.format", CachedConfig.getString("chat.format").replace(CachedConfig.getString("chat.format").substring(start, end), ""));
// 							}

// 							while (world_tag_matcher_tab.find()) {
// 								int start = world_tag_matcher_tab.start();
// 								int end = world_tag_matcher_tab.end();
// 								CachedConfig.set("tablist_modification.format", CachedConfig.getString("tablist_modification.format").replace(CachedConfig.getString("tablist_modification.format").substring(start, end), ""));
// 							}

// 							plugin.updateAllPlayersTABlist();

// 							Messages.configWorldTagRemoved(sender);
// 						} else {
// 							Messages.messageCommandUsageConfig(sender);
// 						}
// 					} else {
// 						Messages.messageCommandUsageConfig(sender);
// 					}
// 				} else {
// 					boolean addonCommandFound = false;
// 					for (Entry<File, PowerRanksAddon> prAddon : this.plugin.addonsManager.addonClasses.entrySet()) {
// 						PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.plugin, "CONSOLE");
// 						if (prAddon.getValue().onPowerRanksCommand(prPlayer, false, args[0], args)) {
// 							addonCommandFound = true;
// 						}
// 					}
// 					if (!addonCommandFound)
// 						Messages.unknownCommand(console);
// 				}
// 			}
// 		} else if (sender instanceof BlockCommandSender) { // TODO nothing TODO just easy navigation
// 			if (cmd.getName().equalsIgnoreCase("powerranks") || cmd.getName().equalsIgnoreCase("pr")) {
// 				if (args.length == 0) {
// 				} else if (args[0].equalsIgnoreCase("reload")) {
// 					final PluginManager plg = Bukkit.getPluginManager();
// 					final Plugin plgname = plg.getPlugin(PowerRanks.pdf.getName());
// 					plg.disablePlugin(plgname);
// 					plg.enablePlugin(plgname);
// 				} else if (args[0].equalsIgnoreCase("setrank")) {
// 					if (args.length == 2) {
// 						this.users.setGroup(null, args[0], this.users.getRankIgnoreCase(args[1]), true);
// 					} else {
// 					}
// 				} else if (args[0].equalsIgnoreCase("addperm")) {
// 					if (args.length == 2) {
// 						final String rankname = args[0].equals("*") ? args[0] : this.users.getRankIgnoreCase(args[0]);
// 						final String permission = args[1];
// 						this.users.addPermission(rankname, permission);
// 					}
// 				} else if (args[0].equalsIgnoreCase("delperm")) {
// 					if (args.length == 2) {
// 						final String rankname = args[0].equals("*") ? args[0] : this.users.getRankIgnoreCase(args[0]);
// 						final String permission = args[1];
// 						this.users.removePermission(rankname, permission);
// 					}
// 				} else if (args[0].equalsIgnoreCase("addinheritance")) {
// 					if (sender.hasPermission("powerranks.cmd.set")) {
// 						if (args.length == 2) {
// 							final String rankname = this.users.getRankIgnoreCase(args[0]);
// 							final String inheritance = args[1];
// 							this.users.addInheritance(rankname, inheritance);
// 						}
// 					}
// 				} else if (args[0].equalsIgnoreCase("delinheritance")) {
// 					if (sender.hasPermission("powerranks.cmd.set")) {
// 						if (args.length == 2) {
// 							final String rankname = this.users.getRankIgnoreCase(args[0]);
// 							final String inheritance = args[1];
// 							this.users.removeInheritance(rankname, inheritance);
// 						}
// 					}
// 				} else if (args[0].equalsIgnoreCase("setprefix")) {
// 					if (sender.hasPermission("powerranks.cmd.set")) {
// 						if (args.length == 1) {
// 							final String rankname = this.users.getRankIgnoreCase(args[0]);
// 							final String prefix = "";
// 							this.users.setPrefix(rankname, prefix);
// 						} else if (args.length >= 3) {
// 							final String rankname = this.users.getRankIgnoreCase(args[0]);
// 							String prefix = "";
// 							for (int i = 2; i < args.length; i++) {
// 								prefix += args[i] + " ";
// 							}
// 							prefix = prefix.substring(0, prefix.length() - 1);
// 							this.users.setPrefix(rankname, prefix);
// 						}
// 					}
// 				} else if (args[0].equalsIgnoreCase("setsuffix")) {
// 					if (sender.hasPermission("powerranks.cmd.set")) {
// 						if (args.length == 1) {
// 							final String rankname = this.users.getRankIgnoreCase(args[0]);
// 							final String suffix = "";
// 							this.users.setSuffix(rankname, suffix);
// 						} else if (args.length >= 3) {
// 							final String rankname = this.users.getRankIgnoreCase(args[0]);
// 							String suffix = "";
// 							for (int i = 2; i < args.length; i++) {
// 								suffix += args[i] + " ";
// 							}
// 							suffix = suffix.substring(0, suffix.length() - 1);
// 							this.users.setSuffix(rankname, suffix);
// 						}
// 					}
// 				} else if (args[0].equalsIgnoreCase("setchatcolor")) {
// 					if (sender.hasPermission("powerranks.cmd.set")) {
// 						if (args.length == 2) {
// 							final String rankname = this.users.getRankIgnoreCase(args[0]);
// 							final String color = args[1];
// 							this.users.setChatColor(rankname, color);
// 						}
// 					}
// 				} else if (args[0].equalsIgnoreCase("setnamecolor")) {
// 					if (sender.hasPermission("powerranks.cmd.set")) {
// 						if (args.length == 2) {
// 							final String rankname = this.users.getRankIgnoreCase(args[0]);
// 							final String color = args[1];
// 							this.users.setNameColor(rankname, color);
// 						}
// 					}
// 				} else if (args[0].equalsIgnoreCase("createrank")) {
// 					if (sender.hasPermission("powerranks.cmd.create")) {
// 						if (args.length == 1) {
// 							final String rankname = this.users.getRankIgnoreCase(args[0]);
// 							this.users.createRank(rankname);
// 						}
// 					}
// 				} else if (args[0].equalsIgnoreCase("deleterank")) {
// 					if (sender.hasPermission("powerranks.cmd.create")) {
// 						if (args.length == 1) {
// 							final String rankname = this.users.getRankIgnoreCase(args[0]);
// 							this.users.deleteRank(rankname);
// 						}
// 					}
// 				} else if (args[0].equalsIgnoreCase("promote")) {
// 					if (args.length == 1) {
// 						final String playername = args[0];
// 						this.users.promote(playername);
// 					}
// 				} else if (args[0].equalsIgnoreCase("demote")) {
// 					if (args.length == 1) {
// 						final String playername = args[0];
// 						this.users.demote(playername);
// 					}
// 				} else if (args[0].equalsIgnoreCase("renamerank")) {
// 					if (args.length == 2) {
// 						final String from = this.users.getRankIgnoreCase(args[0]);
// 						final String to = args[1];
// 						this.users.renameRank(from, to);
// 					}
// 				} else if (args[0].equalsIgnoreCase("setdefaultrank")) {
// 					if (args.length == 1) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						this.users.setDefaultRank(rankname);
// 					}
// 				} else if (args[0].equalsIgnoreCase("addbuyablerank")) {
// 					if (args.length == 2) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						final String rankname2 = this.users.getRankIgnoreCase(args[1]);
// 						this.users.addBuyableRank(rankname, rankname2);
// 					}
// 				} else if (args[0].equalsIgnoreCase("delbuyablerank")) {
// 					if (args.length == 2) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						final String rankname2 = this.users.getRankIgnoreCase(args[1]);
// 						this.users.delBuyableRank(rankname, rankname2);
// 					}
// 				} else if (args[0].equalsIgnoreCase("setbuycost")) {
// 					if (args.length == 2) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						final String cost = this.users.getRankIgnoreCase(args[1]);
// 						this.users.setBuyCost(rankname, cost);
// 					}
// 				} else if (args[0].equalsIgnoreCase("setbuydescription")) {
// 					if (args.length >= 3) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						String description = "";
// 						for (int j = 2; j < args.length; j++) {
// 							description = String.valueOf(description) + args[j] + " ";
// 						}
// 						description = description.substring(0, description.length() - 1);
// 						this.users.setBuyDescription(rankname, description);
// 					}
// 				} else if (args[0].equalsIgnoreCase("setbuycommand")) {
// 					if (args.length >= 3) {
// 						final String rankname = this.users.getRankIgnoreCase(args[0]);
// 						String command = "";
// 						for (int j = 2; j < args.length; j++) {
// 							command = String.valueOf(command) + args[j] + " ";
// 						}
// 						command = command.substring(0, command.length() - 1);
// 						this.users.setBuyCommand(rankname, command);
// 					}
// 				} else if (args[0].equalsIgnoreCase("addplayerperm")) {
// 					if (args.length == 2) {
// 						final String target_player = args[0];
// 						final String permission = args[1];
// 						this.users.addPlayerPermission(target_player, permission);
// 					}
// 				} else if (args[0].equalsIgnoreCase("delplayerperm")) {
// 					if (args.length == 2) {
// 						final String target_player = args[0];
// 						final String permission = args[1];
// 						this.users.delPlayerPermission(target_player, permission);
// 					}

// 				} else if (args[0].equalsIgnoreCase("createusertag")) {
// 					if (args.length == 2) {
// 						final String tag = args[0];
// 						final String text = args[1];
// 						this.users.createUserTag(tag, text);
// 					}

// 				} else if (args[0].equalsIgnoreCase("editusertag")) {
// 					if (args.length == 2) {
// 						final String tag = args[0];
// 						final String text = args[1];
// 						this.users.editUserTag(tag, text);
// 					}

// 				} else if (args[0].equalsIgnoreCase("removeusertag")) {
// 					if (args.length == 2) {
// 						final String tag = args[0];
// 						this.users.removeUserTag(tag);
// 					}

// 				} else if (args[0].equalsIgnoreCase("setusertag")) {
// 					if (args.length == 2) {
// 						final String player = args[0];
// 						final String tag = args[1];
// 						this.users.setUserTag(sender, tag);
// 					}
// 				}

// 			} else if (args[0].equalsIgnoreCase("setpromoterank")) {
// 				if (args.length == 2) {
// 					final String rankname = args[0];
// 					final String promote_rank = args[1];
// 					this.users.setPromoteRank(rankname, promote_rank);
// 				}
// 			} else if (args[0].equalsIgnoreCase("setdemoterank")) {
// 				if (args.length == 2) {
// 					final String rankname = args[0];
// 					final String promote_rank = args[1];
// 					this.users.setDemoteRank(rankname, promote_rank);
// 				}
// 			} else if (args[0].equalsIgnoreCase("clearpromoterank")) {
// 				if (args.length == 1) {
// 					final String rankname = args[0];
// 					this.users.clearPromoteRank(rankname);
// 				}
// 			} else if (args[0].equalsIgnoreCase("cleardemoterank")) {
// 				if (args.length == 1) {
// 					final String rankname = args[0];
// 					this.users.clearDemoteRank(rankname);
// 				}
// 			}
		}
		return false;
	}
}