package nl.svenar.powerranks.commands.usertags;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.Users;
import nl.svenar.powerranks.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdClearusertag extends PowerCommand {

	private Users users;

	public CmdClearusertag(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 0) {
			if (!PowerRanks.plugin_hook_deluxetags) {

				final String playername = sender.getName();
				final boolean result = this.users.clearUserTag(playername);
				if (result) {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".success"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("target", playername)
									.build(),
							'[', ']'));
				} else {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".failed"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("target", playername)
									.build(),
							'[', ']'));
				}

			} else {
				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".disabled"));
			}
		} else if (args.length == 1) {
			if (sender.hasPermission("powerranks.cmd." + commandName.toLowerCase() + ".other")) {
				if (!PowerRanks.plugin_hook_deluxetags) {

					final String playername = args[0];
					final boolean result = this.users.clearUserTag(playername);
					if (result) {
						sender.sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"commands." + commandName.toLowerCase() + ".success"),
								ImmutableMap.<String, String>builder()
										.put("player", sender.getName())
										.put("target", playername)
										.build(),
								'[', ']'));
					} else {
						sender.sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"commands." + commandName.toLowerCase() + ".failed"),
								ImmutableMap.<String, String>builder()
										.put("player", sender.getName())
										.put("target", playername)
										.build(),
								'[', ']'));
					}

				} else {
					sender.sendMessage(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".disabled"));
				}
			} else {
				sender.sendMessage(PowerRanks.getLanguageManager().getFormattedMessage("general.no-permission"));
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

		if (args.length == 1) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				tabcomplete.add(player.getName());
			}
		}

		return tabcomplete;
	}
}
