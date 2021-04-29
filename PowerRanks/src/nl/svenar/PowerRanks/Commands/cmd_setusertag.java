package nl.svenar.PowerRanks.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class cmd_setusertag extends PowerCommand {

	private Users users;

	public cmd_setusertag(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.setusertag") || sender.hasPermission("powerranks.cmd.admin")) {
			if (args.length == 1) {
				if (sender.hasPermission("powerranks.cmd.setusertag")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						final String playername = sender.getName();
						final String tag = args[0];
						final boolean result = this.users.setUserTag(playername, tag);
						if (result) {
							Messages.messageCommandSetusertagSuccess(sender, playername, tag);
						} else {
							Messages.messageCommandSetusertagError(sender, playername, tag);
						}
					} else {
						Messages.messageUsertagsDisabled(sender);
					}
				} else {
					Messages.noPermission(sender);
				}
			} else if (args.length == 2) {
				if (sender.hasPermission("powerranks.cmd.admin")) {
					if (!PowerRanks.plugin_hook_deluxetags) {
						final String playername = args[0];
						final String tag = args[1];
						final boolean result = this.users.setUserTag(playername, tag);
						if (result) {
							Messages.messageCommandSetusertagSuccess(sender, playername, tag);
						} else {
							Messages.messageCommandSetusertagError(sender, playername, tag);
						}
					} else {
						Messages.messageUsertagsDisabled(sender);
					}
				} else {
					Messages.noPermission(sender);
				}
			} else {
				Messages.messageCommandUsageSetusertag(sender);
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

		if (args.length == 2) {
			for (String tag : this.users.getUserTags()) {
				if (tag.toLowerCase().contains(args[0].toLowerCase()))
				tabcomplete.add(tag);
			}
		}

		return tabcomplete;
	}
}
