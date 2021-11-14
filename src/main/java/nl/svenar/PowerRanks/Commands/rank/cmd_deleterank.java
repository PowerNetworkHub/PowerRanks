package nl.svenar.PowerRanks.Commands.rank;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class cmd_deleterank extends PowerCommand {

	private Users users;

	public cmd_deleterank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.deleterank")) {
			if (args.length == 1) {
				final String rank2 = this.users.getRankIgnoreCase(args[0]);
				final boolean success = this.users.deleteRank(rank2);
				if (success) {
					Messages.messageCommandDeleteRankSuccess(sender, rank2);
				} else {
					Messages.messageCommandDeleteRankError(sender, rank2);
				}
			} else {
				Messages.messageCommandUsageDeleteRank(sender);
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
