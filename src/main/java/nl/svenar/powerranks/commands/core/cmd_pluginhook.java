package nl.svenar.powerranks.commands.core;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.Messages;
import nl.svenar.powerranks.util.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmd_pluginhook extends PowerCommand {

	public cmd_pluginhook(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 0) {
			Messages.messagePluginhookStats(sender);
		} else if (args.length == 2) {
			String state = args[0];
			String pluginname = args[1];

			if ((state.equalsIgnoreCase("enable") || state.equalsIgnoreCase("disable"))
					&& PowerRanks.getConfigManager().getMap("plugin_hook", new HashMap<String, String>()).keySet()
							.contains(pluginname.toLowerCase())) {

				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".state-changed"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("config_target", pluginname.toLowerCase())
								.put("old_state",
										String.valueOf(PowerRanks.getConfigManager()
												.getBool("plugin_hook." + pluginname.toLowerCase(), true)))
								.put("new_state", String.valueOf(state.equalsIgnoreCase("enable")))
								.build(),
						'[', ']'));

				PowerRanks.getConfigManager().setBool("plugin_hook." + pluginname.toLowerCase(),
						state.equalsIgnoreCase("enable"));

			} else {
				if (state.equalsIgnoreCase("enable") || state.equalsIgnoreCase("disable")) {
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".unknown-plugin"));
				} else {
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".unknown-state"));
				}
			}
		} else {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
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
