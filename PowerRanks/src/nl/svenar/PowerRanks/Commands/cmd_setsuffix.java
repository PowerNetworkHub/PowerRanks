package nl.svenar.PowerRanks.Commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class cmd_setsuffix extends PowerCommand {

	private Users users;

	public cmd_setsuffix(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.setsuffix")) {
			if (args.length == 1) {
				final String rankname = this.users.getRankIgnoreCase(args[0]);
				final String suffix = "";
				final boolean result = this.users.setSuffix(rankname, suffix);
				if (result) {
					Messages.messageCommandSetSuffix(sender, suffix, rankname);
				} else {
					Messages.messageGroupNotFound(sender, rankname);
				}
			} else if (args.length >= 2) {
				final String rankname = this.users.getRankIgnoreCase(args[0]);
				String suffix = "";
				for (int i = 1; i < args.length; i++) {
					suffix += args[i] + " ";
				}
				suffix = suffix.substring(0, suffix.length() - 1);
				final boolean result = this.users.setSuffix(rankname, suffix);
				if (result) {
					Messages.messageCommandSetSuffix(sender, suffix, rankname);
				} else {
					Messages.messageGroupNotFound(sender, rankname);
				}
			} else {
				Messages.messageCommandUsageSetSuffix(sender);
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
