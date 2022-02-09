package nl.svenar.PowerRanks.Commands.core;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.PowerRanksVerbose;
import nl.svenar.PowerRanks.Util.Util;

public class cmd_verbose extends PowerCommand {

	public cmd_verbose(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 0) {
			Messages.checkVerbose(sender);
		} else if (args.length == 1 || args.length == 2) {
			String verboseType = args[0].toLowerCase();
			if (verboseType.equals("start")) {
				if (!PowerRanksVerbose.USE_VERBOSE) {
					PowerRanksVerbose.start(false);
					if (args.length == 2) {
						PowerRanksVerbose.setFilter(args[1]);
					}
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".started"));
				} else {
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".already-running"));
				}
			} else if (verboseType.equals("startlive")) {
				if (!PowerRanksVerbose.USE_VERBOSE) {
					PowerRanksVerbose.start(true);
					if (args.length == 2) {
						PowerRanksVerbose.setFilter(args[1]);
					}
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".started"));
				} else {
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".already-running"));
				}
			} else if (verboseType.equals("stop")) {
				if (PowerRanksVerbose.USE_VERBOSE) {
					PowerRanksVerbose.stop();
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".stopped"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("command", "/pr " + commandName.toLowerCase())
									.build(),
							'[', ']'));
				} else {
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".not-running"));
				}
			} else if (verboseType.equals("clear")) {
				PowerRanksVerbose.clear();
				sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedMessage(
							"commands." + commandName.toLowerCase() + ".cleared"));
			} else if (verboseType.equals("save")) {
				if (!PowerRanksVerbose.USE_VERBOSE) {
					if (PowerRanksVerbose.save()) {
						sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".saved"));
				} else {
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".failed-saving"));
				}
				} else {
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".must-stop-before-saving"));
				}
			} else {
				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
								"commands." + commandName.toLowerCase() + ".arguments"));
			}
		} else {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments"));
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			tabcomplete.add("start");
			tabcomplete.add("startlive");
			tabcomplete.add("stop");
			tabcomplete.add("save");
		}

		if (args.length == 2) {
			tabcomplete.add("---Add a permission filter ---");
			tabcomplete.add("example:");
			tabcomplete.add("powerranks");
			tabcomplete.add("powerranks.cmd");
			tabcomplete.add("------------------------------");
		}

		return tabcomplete;
	}
}
