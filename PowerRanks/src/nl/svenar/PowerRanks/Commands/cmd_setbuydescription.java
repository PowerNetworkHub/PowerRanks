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

public class cmd_setbuydescription extends PowerCommand {

	private Users users;

	public cmd_setbuydescription(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.setbuydescription")) {
			if (args.length >= 2) {
				final String rankname = this.users.getRankIgnoreCase(args[0]);
				String description = "";
				for (int i = 1; i < args.length; i++) {
					description = String.valueOf(description) + args[i] + " ";
				}
				description = description.substring(0, description.length() - 1);
				final boolean success2 = this.users.setBuyDescription(rankname, description);
				if (success2) {
					Messages.messageCommandSetbuydescriptionSuccess(sender, rankname, description);
				} else {
					Messages.messageCommandSetbuydescriptionError(sender, rankname, description);
				}
			} else {
				Messages.messageCommandUsageSetbuydescription(sender);
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
				tabcomplete.add(rank);
			}
		}
		
		return tabcomplete;
	}
}
