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

public class cmd_delinheritance extends PowerCommand {

	private Users users;

	public cmd_delinheritance(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.delinheritance")) {
			if (args.length == 2) {
				final String rank2 = this.users.getRankIgnoreCase(args[0]);
				final String inheritance = args[1];
				final boolean result = this.users.removeInheritance(rank2, inheritance);
				if (result) {
					Messages.messageCommandInheritanceRemoved(sender, inheritance, rank2);
				} else {
					Messages.messageGroupNotFound(sender, rank2);
				}
			} else {
				Messages.messageCommandUsageRemoveInheritance(sender);
			}
		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (String rank : this.users.getGroups()) {
				// if (rank.toLowerCase().contains(args[0].toLowerCase())) {
				tabcomplete.add(rank);
				// }
			}
		}

		if (args.length == 2) {
			for (String inheritance : this.users.getInheritances(this.users.getRankIgnoreCase(args[0]))) {
				// if (inheritance.toLowerCase().contains(args[1].toLowerCase())) {
				tabcomplete.add(inheritance);
				// }
			}
		}

		return tabcomplete;
	}
}
