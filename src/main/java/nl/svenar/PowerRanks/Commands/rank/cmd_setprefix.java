package nl.svenar.PowerRanks.Commands.rank;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.common.structure.PRRank;

public class cmd_setprefix extends PowerCommand {

	private Users users;

	public cmd_setprefix(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 1) {
			final String rankname = this.users.getRankIgnoreCase(args[0]);
			final String prefix = "";
			final boolean result = this.users.setPrefix(rankname, prefix);
			if (result) {
				Messages.messageCommandSetPrefix(sender, prefix, rankname);
			} else {
				Messages.messageGroupNotFound(sender, rankname);
			}
		} else if (args.length >= 2) {
			final String rankname = this.users.getRankIgnoreCase(args[0]);
			String prefix = "";
			for (int i = 1; i < args.length; i++) {
				prefix += args[i] + " ";
			}
			prefix = prefix.substring(0, prefix.length() - 1);
			final boolean result = this.users.setPrefix(rankname, prefix);
			if (result) {
				Messages.messageCommandSetPrefix(sender, prefix, rankname);
			} else {
				Messages.messageGroupNotFound(sender, rankname);
			}
		} else {
			sender.sendMessage(PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
					"commands." + commandName.toLowerCase() + ".arguments"));
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
