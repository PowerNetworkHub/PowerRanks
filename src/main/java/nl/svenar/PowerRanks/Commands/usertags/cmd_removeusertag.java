package nl.svenar.PowerRanks.Commands.usertags;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class cmd_removeusertag extends PowerCommand {

	private Users users;

	public cmd_removeusertag(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.removeusertag")) {
			if (!PowerRanks.plugin_hook_deluxetags) {
				if (args.length == 1) {
					final String tag = args[0];
					final boolean result = this.users.removeUserTag(tag);
					if (result) {
						Messages.messageCommandRemoveusertagSuccess(sender, tag);
					} else {
						Messages.messageCommandRemoveusertagError(sender, tag);
					}
				} else {
					Messages.messageCommandUsageRemoveusertag(sender);
				}
			}  else {
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
