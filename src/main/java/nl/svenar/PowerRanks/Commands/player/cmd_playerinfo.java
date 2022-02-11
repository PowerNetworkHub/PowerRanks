package nl.svenar.PowerRanks.Commands.player;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Util.Util;

public class cmd_playerinfo extends PowerCommand {

	public cmd_playerinfo(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 1) {
			String target_player_name = args[0];
			Player target_player = Util.getPlayerByName(target_player_name);
			if (target_player != null) {
				Messages.messagePlayerInfo(sender, target_player, 0);
			} else {
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage("general.player-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", target_player_name)
								.build(),
						'[', ']'));
			}
		} else if (args.length == 2) {
			String target_player_name = args[0];
			int page = Integer.parseInt(args[1].replaceAll("[a-zA-Z]", ""));
			Player target_player = Util.getPlayerByName(target_player_name);
			if (target_player != null) {
				Messages.messagePlayerInfo(sender, target_player, page);
			} else {
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage("general.player-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", target_player_name)
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

		if (args.length == 1) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				tabcomplete.add(player.getName());
			}
		}

		return tabcomplete;
	}
}
