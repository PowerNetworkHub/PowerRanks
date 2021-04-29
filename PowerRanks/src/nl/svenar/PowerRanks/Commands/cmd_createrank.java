package nl.svenar.PowerRanks.Commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;

public class cmd_createrank extends PowerCommand {

	private Users users;

	public cmd_createrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.createrank")) {
			if (args.length == 1) {
				final String rankname = this.users.getRankIgnoreCase(args[0]);
				final boolean success = this.users.createRank(rankname);
				String[] forbiddenColorCharacters = {"&", "#"};
				String[] forbiddenCharacters = {"`", "~", "!", "@", "$", "%", "^", "*", "(", ")", "{", "}", "[", "]", ":", ";", "\"", "'", "|", "\\", "?", "/", ">", "<", ",", ".", "+", "="};
				if (success) {
					Messages.messageCommandCreateRankSuccess(sender, rankname);
					if (Util.stringContainsItemFromList(rankname, forbiddenColorCharacters)) {
						Messages.messageCommandCreateRankColorCharacterWarning(sender, rankname);
					}

					if (Util.stringContainsItemFromList(rankname, forbiddenCharacters)) {
						Messages.messageCommandCreateRankCharacterWarning(sender, rankname);
					}
				} else {
					Messages.messageCommandCreateRankError(sender, rankname);
				}
			} else {
				Messages.messageCommandUsageCreateRank(sender);
			}
		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}
}
