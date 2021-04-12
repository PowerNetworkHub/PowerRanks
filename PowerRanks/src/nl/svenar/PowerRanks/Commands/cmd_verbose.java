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
import nl.svenar.PowerRanks.Data.PowerRanksVerbose;

public class cmd_verbose extends PowerCommand {

	private Users users;

	public cmd_verbose(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.verbose")) {
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

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		tabcomplete.add("start");
		tabcomplete.add("startlive");
		tabcomplete.add("stop");
		tabcomplete.add("save");

		return tabcomplete;
	}
}
