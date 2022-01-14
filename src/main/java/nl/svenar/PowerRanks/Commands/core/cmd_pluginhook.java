package nl.svenar.PowerRanks.Commands.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
// import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;

public class cmd_pluginhook extends PowerCommand {

	public cmd_pluginhook(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.pluginhook")) {
			if (args.length == 0) {
				Messages.messagePluginhookStats(sender);
			} else if (args.length == 2) {
				String state = args[0];
				String pluginname = args[1];

				if ((state.equalsIgnoreCase("enable") || state.equalsIgnoreCase("disable"))
						&& PowerRanks.getConfigManager().getMap("plugin_hook", new HashMap<String, String>()).keySet()
								.contains(pluginname.toLowerCase())) {
					PowerRanks.getConfigManager().setBool("plugin_hook." + pluginname.toLowerCase(),
							state.equalsIgnoreCase("enable"));
					Messages.pluginhookStateChanged(sender, pluginname.toLowerCase(),
							(state.equalsIgnoreCase("enable") ? ChatColor.DARK_GREEN + "Enabled"
									: ChatColor.DARK_RED + "Disabled"));
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
			for (Object plugin : PowerRanks.getConfigManager().getMap("plugin_hook", new HashMap<String, String>())
					.keySet()) {
				tabcomplete.add((String) plugin);
			}
		}

		return tabcomplete;
	}
}
