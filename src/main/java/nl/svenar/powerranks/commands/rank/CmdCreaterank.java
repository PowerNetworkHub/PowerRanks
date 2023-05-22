package nl.svenar.powerranks.commands.rank;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.Users;
import nl.svenar.powerranks.util.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdCreaterank extends PowerCommand {

	private Users users;

	public CmdCreaterank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
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
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".success"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("rank", rankname)
								.build(),
						'[', ']'));
				if (Util.stringContainsItemFromList(rankname, forbiddenColorCharacters)) {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".warning-color"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("rank", rankname)
									.build(),
							'[', ']'));
				}

				if (Util.stringContainsItemFromList(rankname, forbiddenCharacters)) {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".warning-character"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("rank", rankname)
									.build(),
							'[', ']'));
				}
			} else {
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".failed"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("rank", rankname)
								.build(),
						'[', ']'));
			}
		} else {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}
}
