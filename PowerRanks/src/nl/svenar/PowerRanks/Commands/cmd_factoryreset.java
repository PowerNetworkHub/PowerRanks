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

public class cmd_factoryreset extends PowerCommand {

	private Users users;

	public cmd_factoryreset(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.factoryreset")) {
			if (args.length == 0) {
				Messages.messageCommandFactoryReset(sender);
			} else if (args.length == 1) {
				if (PowerRanks.factoryresetid == null) {
					Messages.messageCommandFactoryReset(sender);
				} else {
					String resetid = args[0];
					if (resetid.equalsIgnoreCase(PowerRanks.factoryresetid))
						this.plugin.factoryReset(sender);
					else
						Messages.messageCommandFactoryReset(sender);
				}
			} else {
				Messages.messageCommandUsageFactoryReset(sender);
			}
		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}
}
