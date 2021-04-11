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

public class cmd_editusertag extends PowerCommand {

	private Users users;

	public cmd_editusertag(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.editusertag")) {
			if (!PowerRanks.plugin_hook_deluxetags) {
				if (args.length == 2) {
					final String tag = args[0];
					final String text = args[1];
					final boolean result = this.users.editUserTag(tag, text);
					if (result) {
						Messages.messageCommandEditusertagSuccess(sender, tag, text);
					} else {
						Messages.messageCommandEditusertagError(sender, tag, text);
					}
				} else {
					Messages.messageCommandUsageEditusertag(sender);
				}
			} else {
				Messages.messageUsertagsDisabled(sender);
			}
		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (String tag : this.users.getUserTags()) {
				if (tag.toLowerCase().contains(args[0].toLowerCase()))
				tabcomplete.add(tag);
			}
		}

		return tabcomplete;
	}
}
