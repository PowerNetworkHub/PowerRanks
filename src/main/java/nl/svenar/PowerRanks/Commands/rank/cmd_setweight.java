package nl.svenar.PowerRanks.Commands.rank;

import java.util.ArrayList;
import java.util.Objects;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.common.structure.PRRank;

public class cmd_setweight extends PowerCommand {

	private Users users;

	public cmd_setweight(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName, String[] args) {
		if (sender.hasPermission("powerranks.cmd.setweight")) {
			if (args.length == 2) {

				final String rankname = this.users.getRankIgnoreCase(args[0]);
				final PRRank rank = CacheManager.getRank(rankname);
				if (Objects.isNull(rank)) {
					Messages.messageGroupNotFound(sender, args[0]);
				}

				int weight = 0;
				try {
					weight = Integer.parseInt(args[1]);
				} catch (Exception e) {
					Messages.messageErrorNotInt(sender, args[1]);
				}

				rank.setWeight(weight);
				Messages.messageCommandWeightSet(sender, weight, rankname);
			} else {
				Messages.messageCommandUsageSetWeight(sender);
			}
		} else {
			sender.sendMessage(PowerRanks.getLanguageManager().getFormattedMessage("general.no-permission"));
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
