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

public class cmd_clearusertag extends PowerCommand {

	private Users users;

	public cmd_clearusertag(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.setusertag") || sender.hasPermission("powerranks.cmd.admin")) {
			if (args.length == 0) {
				if (sender.hasPermission("powerranks.cmd.setusertag")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						
						final String playername = sender.getName();
						final boolean result = this.users.clearUserTag(playername);
						if (result) {
							Messages.messageCommandClearusertagSuccess(sender, playername);
						} else {
							Messages.messageCommandClearusertagError(sender, playername);
						}

					} else {
						Messages.messageUsertagsDisabled(sender);
					}
				} else {
					Messages.noPermission(sender);
				}
			} else if (args.length == 1) {
				if (sender.hasPermission("powerranks.cmd.admin")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						
						final String playername = args[0];
						final boolean result = this.users.clearUserTag(playername);
						if (result) {
							Messages.messageCommandClearusertagSuccess(sender, playername);
						} else {
							Messages.messageCommandClearusertagError(sender, playername);
						}

					} else {
						Messages.messageUsertagsDisabled(sender);
					}
				} else {
					Messages.noPermission(sender);
				}
			} else {
				Messages.messageCommandUsageClearusertag(sender);
			}
		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				tabcomplete.add(player.getName());
			}
		}

		return tabcomplete;
	}
}
