package nl.svenar.PowerRanks.Commands.core;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.PowerRanksVerbose;

public class cmd_verbose extends PowerCommand {


	public cmd_verbose(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName, String[] args) {
		if (sender.hasPermission("powerranks.cmd.verbose")) {
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
						Messages.messageCommandVerboseStarted(sender);
					} else {
						Messages.messageCommandVerboseAlreadyRunning(sender);
					}
				} else if (verboseType.equals("startlive")) {
					if (!PowerRanksVerbose.USE_VERBOSE) {
						PowerRanksVerbose.start(true);
						if (args.length == 2) {
							PowerRanksVerbose.setFilter(args[1]);
						}
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
			sender.sendMessage(PowerRanks.getLanguageManager().getFormattedMessage("general.no-permission"));
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
