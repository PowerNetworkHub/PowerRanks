package nl.svenar.powerranks.bukkit.commands.player;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.data.Messages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmd_playerinfo extends PowerCommand {

	public cmd_playerinfo(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 1) {
			String targetPlayerName = args[0];
			PRPlayer targetPlayer = CacheManager.getPlayer(targetPlayerName);
			if (targetPlayer != null) {
				Messages.messagePlayerInfo(sender, targetPlayer, 0);
			} else {
				sender.sendMessage(PRUtil.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage("general.player-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", targetPlayerName)
								.build(),
						'[', ']'));
			}
		} else if (args.length == 2) {
			String targetPlayerName = args[0];
			int page = Integer.parseInt(args[1].replaceAll("[a-zA-Z]", ""));
			PRPlayer targetPlayer = CacheManager.getPlayer(targetPlayerName);
			if (targetPlayer != null) {
				Messages.messagePlayerInfo(sender, targetPlayer, page);
			} else {
				sender.sendMessage(PRUtil.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage("general.player-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", targetPlayerName)
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
			for (PRPlayer prPlayer : CacheManager.getPlayers()) {
				tabcomplete.add(prPlayer.getName());
			}
		}

		return tabcomplete;
	}
}
