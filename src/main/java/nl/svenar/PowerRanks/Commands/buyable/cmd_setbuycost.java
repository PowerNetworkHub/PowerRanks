package nl.svenar.PowerRanks.Commands.buyable;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.common.structure.PRRank;

public class cmd_setbuycost extends PowerCommand {

	private Users users;

	public cmd_setbuycost(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.setbuycost")) {
			if (args.length == 2) {
				final String rankname = this.users.getRankIgnoreCase(args[0]);
				final String cost = this.users.getRankIgnoreCase(args[1]);
				final boolean success = this.users.setBuyCost(rankname, cost);
				if (success) {
					Messages.messageCommandSetcostSuccess(sender, rankname, cost);
				} else {
					Messages.messageCommandSetcostError(sender, rankname, cost);
				}
			} else {
				Messages.messageCommandUsageSetcost(sender);
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
			tabcomplete.add("0");
			tabcomplete.add("10");
			tabcomplete.add("100");
			tabcomplete.add("1000");
			tabcomplete.add("10000");
		}

		return tabcomplete;
	}
}
