package nl.svenar.PowerRanks.Commands.rank;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.common.structure.PRRank;

public class cmd_renamerank extends PowerCommand {

	private Users users;

	public cmd_renamerank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.renamerank")) {
			if (args.length == 2) {
				final String from = this.users.getRankIgnoreCase(args[0]);
				final String to = args[1];
				final boolean success = this.users.renameRank(from, to);
				if (success) {
					Messages.messageCommandRenameRankSuccess(sender, from);
				} else {
					Messages.messageCommandRenameRankError(sender, from);
				}
			} else {
				Messages.messageCommandUsageDemote(sender);
			}
		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (PRRank rank : this.users.getGroups()) {
				tabcomplete.add(rank.getName());
			}
		}
		
		return tabcomplete;
	}
}
