package nl.svenar.PowerRanks.Commands;

import java.util.ArrayList;
import java.util.Set;

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

public class cmd_pluginhook extends PowerCommand {

	private Users users;

	public cmd_pluginhook(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.pluginhook")) {
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

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			tabcomplete.add("enable");
			tabcomplete.add("disable");
		}

		if (args.length == 2) {
			for (String plugin : CachedConfig.getConfigurationSection("plugin_hook").getKeys(false)) {
				tabcomplete.add(plugin);
			}
		}

		return tabcomplete;
	}
}
