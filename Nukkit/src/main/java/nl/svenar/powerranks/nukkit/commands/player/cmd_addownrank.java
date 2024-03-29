package nl.svenar.powerranks.nukkit.commands.player;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;


import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;

public class cmd_addownrank extends PowerCommand {

	public cmd_addownrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
		if (args.length == 1) {
			String targetRankName = args[0];

			boolean commandAllowed = false;
			if (sender instanceof Player) {
				commandAllowed = sender
						.hasPermission("powerranks.cmd." + commandName.toLowerCase() + "." + targetRankName);
			} else {
				commandAllowed = true;
			}

			if (commandAllowed) {
				PRRank rank = PRCache.getRankIgnoreCase(targetRankName);
				PRPlayer targetPlayer = PRCache.getPlayer(sender.getName());
				if (rank != null && targetPlayer != null) {
					PRPlayerRank playerRank = new PRPlayerRank(rank.getName());
					targetPlayer.addRank(playerRank);

					sender.sendMessage(PRUtil.powerFormatter(
							plugin.getLanguageManager()
									.getFormattedMessage(
											"commands." + commandName.toLowerCase() + ".success-executor"),
							ImmutableMap.<String, String>builder()
									.put("player", targetPlayer.getName())
									.put("rank", rank.getName())
									.build(),
							'[', ']'));

					sender.sendMessage(PRUtil.powerFormatter(
							plugin.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".success-receiver"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("rank", rank.getName())
									.build(),
							'[', ']'));
				} else {
					if (targetPlayer != null && rank != null) {
						sender.sendMessage(PRUtil.powerFormatter(
							plugin.getLanguageManager()
									.getFormattedMessage(
											"commands." + commandName.toLowerCase() + ".failed-executor"),
							ImmutableMap.<String, String>builder()
									.put("player", targetPlayer.getName())
									.put("rank", rank.getName())
									.build(),
							'[', ']'));
					}
				}
			} else {
				sender.sendMessage(plugin.getLanguageManager().getFormattedMessage("general.no-permission"));
			}
		} else {
			sender.sendMessage(
					plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			if (sender instanceof Player) {
				PRPlayer targetPlayer = PRCache.getPlayer(sender.getName());
				for (PRRank rank : PRCache.getRanks()) {
					if (!targetPlayer.hasRank(rank.getName())) {
						tabcomplete.add(rank.getName());
					}
				}
			}
		}

		return tabcomplete;
	}
}
