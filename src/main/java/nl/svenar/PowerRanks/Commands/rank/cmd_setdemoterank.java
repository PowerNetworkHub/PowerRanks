package nl.svenar.PowerRanks.Commands.rank;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.common.structure.PRRank;

public class cmd_setdemoterank extends PowerCommand {

	private Users users;

	public cmd_setdemoterank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.setdemoterank")) {
			if (args.length == 2) {
				final String rankname = args[0];
				final String promote_rank = args[1];
				if (this.users.setDemoteRank(rankname, promote_rank)) {
					Messages.messageCommandSetdemoterankSuccess(sender, rankname, promote_rank);
				} else {
					Messages.messageCommandSetdemoterankError(sender, rankname, promote_rank);
				}
			} else {
				Messages.messageCommandUsageSetdemoterank(sender);
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

		if (args.length == 2) {
			for (PRRank rank : this.users.getGroups()) {
				if (!rank.getName().toLowerCase().contains(args[0].toLowerCase())) {
					tabcomplete.add(rank.getName());
				}
			}
		}

		return tabcomplete;
	}
}
