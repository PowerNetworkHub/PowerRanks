package nl.svenar.PowerRanks.Commands.rank;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.Util.Util;

public class cmd_createrank extends PowerCommand {

	private Users users;

	public cmd_createrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 1) {
			final String rankname = this.users.getRankIgnoreCase(args[0]);
			final boolean success = this.users.createRank(rankname);
			String[] forbiddenColorCharacters = { "&", "#" };
			String[] forbiddenCharacters = { "`", "~", "!", "@", "$", "%", "^", "*", "(", ")", "{", "}", "[", "]", ":",
					";", "\"", "'", "|", "\\", "?", "/", ">", "<", ",", ".", "+", "=" };
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
			sender.sendMessage(PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
					"commands." + commandName.toLowerCase() + ".arguments"));
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}
}
