package nl.svenar.powerranks.commands.usertags;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.Users;
import nl.svenar.powerranks.util.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmd_createusertag extends PowerCommand {

	private Users users;

	public cmd_createusertag(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (!PowerRanks.plugin_hook_deluxetags) {
			if (args.length == 2) {
				final String tag = args[0];
				final String text = args[1];
				final boolean result = this.users.createUserTag(tag, text);
				if (result) {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".success"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("usertag", tag)
									.put("text", text)
									.build(),
							'[', ']'));
				} else {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".failed"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("usertag", tag)
									.put("text", text)
									.build(),
							'[', ']'));
				}
			} else {
				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
								"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
			}
		} else {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedMessage(
							"commands." + commandName.toLowerCase() + ".disabled"));
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}
}
