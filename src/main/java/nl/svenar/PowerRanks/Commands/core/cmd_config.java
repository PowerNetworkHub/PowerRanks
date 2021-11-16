package nl.svenar.PowerRanks.Commands.core;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
// import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;

import org.bukkit.ChatColor;

public class cmd_config extends PowerCommand {


	public cmd_config(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.config")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("removeworldtag")) {
					String world_tag_regex = "[ ]{0,1}([&][a-fA-F0-9k-oK-OrR]){0,1}((&){0,1}[#][a-fA-F0-9]{6}){0,1}[ ]{0,1}[\\[]world[\\]][ ]{0,1}([&][a-fA-F0-9k-oK-OrR]){0,1}((&){0,1}[#][a-fA-F0-9]{6}){0,1}[ ]{0,1}";
					Pattern world_tag_pattern = Pattern.compile(world_tag_regex);
					Matcher world_tag_matcher_chat = world_tag_pattern.matcher(PowerRanks.getConfigManager().getString("chat.format", "").toLowerCase());
					Matcher world_tag_matcher_tab = world_tag_pattern.matcher(PowerRanks.getConfigManager().getString("tablist_modification.format", "").toLowerCase());

					while (world_tag_matcher_chat.find()) {
						int start = world_tag_matcher_chat.start();
						int end = world_tag_matcher_chat.end();
						PowerRanks.getConfigManager().setString("chat.format", PowerRanks.getConfigManager().getString("chat.format", "").replace(PowerRanks.getConfigManager().getString("chat.format", "").substring(start, end), ""));
					}

					while (world_tag_matcher_tab.find()) {
						int start = world_tag_matcher_tab.start();
						int end = world_tag_matcher_tab.end();
						PowerRanks.getConfigManager().setString("tablist_modification.format", PowerRanks.getConfigManager().getString("tablist_modification.format", "").replace(PowerRanks.getConfigManager().getString("tablist_modification.format", "").substring(start, end), ""));
					}

					this.plugin.updateAllPlayersTABlist();

					Messages.configWorldTagRemoved(sender);
				} else {
					Messages.messageCommandUsageConfig(sender);
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable")) {
					boolean enable = args[0].equalsIgnoreCase("enable");
					if (args[1].equalsIgnoreCase("chat_formatting")) {
						PowerRanks.getConfigManager().setBool("chat.enabled", enable);
						Messages.configStateChanged(sender, "Chat formatting", (enable ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));

					} else if (args[1].equalsIgnoreCase("tablist_formatting")) {
						PowerRanks.getConfigManager().setBool("tablist_modification.enabled", enable);
						Messages.configStateChanged(sender, "Tablist formatting", (enable ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));

					} else if (args[1].equalsIgnoreCase("op")) {
						PowerRanks.getConfigManager().setBool("general.disable-op", !enable);
						Messages.configStateChanged(sender, "Op command", (enable ? ChatColor.DARK_GREEN + "Enabled" : ChatColor.DARK_RED + "Disabled"));

					} else {
						Messages.messageCommandUsageConfig(sender);
					}
				} else {
					Messages.messageCommandUsageConfig(sender);
				}
			} else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("set")) {
					if (args[1].equalsIgnoreCase("playtime_update_interval")) {
						try {
							int time = Integer.parseInt(args[2]);
							PowerRanks.getConfigManager().setInt("general.playtime-update-interval", time);
							Messages.configStateChanged(sender, "Player playtime interval", "" + ChatColor.DARK_GREEN + time);
						} catch (Exception e) {
							Messages.numbersOnly(sender);
						}

					}
				}
			} else {
				Messages.messageCommandUsageConfig(sender);
			}
		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			tabcomplete.add("removeworldtag");
			tabcomplete.add("enable");
			tabcomplete.add("set");
			tabcomplete.add("disable");
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable")) {
				tabcomplete.add("chat_formatting");
				tabcomplete.add("tablist_formatting");
				tabcomplete.add("op");
			}
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("set")) {
				tabcomplete.add("playtime_update_interval");
			}
		}

		if (args.length == 3) {
			if (args[1].equalsIgnoreCase("playtime_update_interval")) {
				tabcomplete.add("1");
				tabcomplete.add("5");
				tabcomplete.add("10");
				tabcomplete.add("60");
			}
		}

		return tabcomplete;
	}
}
