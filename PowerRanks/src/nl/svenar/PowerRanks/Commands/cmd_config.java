package nl.svenar.PowerRanks.Commands;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class cmd_config extends PowerCommand {

	private Users users;

	public cmd_config(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.config")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("removeworldtag")) {
					String world_tag_regex = "[ ]{0,1}([&][a-fA-F0-9k-oK-OrR]){0,1}([#][a-fA-F0-9]{6}){0,1}[ ]{0,1}[\\[]world[\\]][ ]{0,1}([&][a-fA-F0-9k-oK-OrR]){0,1}([#][a-fA-F0-9]{6}){0,1}[ ]{0,1}";
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

					this.plugin.updateAllPlayersTABlist();

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

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			tabcomplete.add("removeworldtag");
			tabcomplete.add("enable");
			tabcomplete.add("disable");
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable")) {
				tabcomplete.add("chat_formatting");
				tabcomplete.add("tablist_formatting");
			}
		}

		return tabcomplete;
	}
}
