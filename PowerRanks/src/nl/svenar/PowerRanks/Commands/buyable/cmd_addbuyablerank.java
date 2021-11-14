package nl.svenar.PowerRanks.Commands.buyable;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class cmd_addbuyablerank extends PowerCommand {

	private Users users;

	public cmd_addbuyablerank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.addbuyablerank")) {
			if (args.length == 2) {
				final String rankname = this.users.getRankIgnoreCase(args[0]);
				final String rankname2 = this.users.getRankIgnoreCase(args[1]);
				final boolean success = this.users.addBuyableRank(rankname, rankname2);
				if (CachedRanks.get("Groups." + rankname) != null && CachedRanks.get("Groups." + rankname2) != null) {
					if (success) {
						Messages.messageCommandAddbuyablerankSuccess(sender, rankname, rankname2);
					} else {
						Messages.messageCommandAddbuyablerankError(sender, rankname, rankname2);
					}
				} else {
					Messages.messageGroupNotFound(sender, CachedRanks.get("Groups." + rankname) == null ? rankname : rankname2);
				}
				
			} else {
				Messages.messageCommandUsageAddbuyablerank(sender);
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

		if (args.length == 2) {
			for (String rank : this.users.getGroups()) {
				if (!rank.toLowerCase().contains(args[0].toLowerCase())) {
					tabcomplete.add(rank);
				}
			}
		}

		return tabcomplete;
	}
}
