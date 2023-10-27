package nl.svenar.powerranks.nukkit.commands.player;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;


import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.nukkit.util.Util;

public class cmd_delrank extends PowerCommand {


	public cmd_delrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
		if (args.length == 2) {
			String target_rank = args[1];

			boolean commandAllowed = false;
			if (sender instanceof Player) {
				commandAllowed = sender
						.hasPermission("powerranks.cmd." + commandName.toLowerCase() + "." + target_rank);
			} else {
				commandAllowed = true;
			}

			if (commandAllowed) {
				PRRank rank = PRCache.getRankIgnoreCase(target_rank);
				PRPlayer targetPlayer = PRCache.getPlayer(args[0]);
				if (rank != null && targetPlayer != null) {
					PRPlayerRank playerRank = null;
					for (PRPlayerRank targetPlayerRank : targetPlayer.getRanks()) {
						if (targetPlayerRank.getName().equals(rank.getName())) {
							playerRank = targetPlayerRank;
							break;
						}
					}
					if (playerRank != null) {
						targetPlayer.removeRank(playerRank);
					}

					sender.sendMessage(PRUtil.powerFormatter(
							plugin.getLanguageManager()
									.getFormattedMessage(
											"commands." + commandName.toLowerCase() + ".success-executor"),
							ImmutableMap.<String, String>builder()
									.put("player", targetPlayer.getName())
									.put("rank", rank.getName())
									.build(),
							'[', ']'));

					if (Util.getPlayerByName(targetPlayer.getName()) != null) {
						Util.getPlayerByName(targetPlayer.getName()).sendMessage(PRUtil.powerFormatter(
								plugin.getLanguageManager().getFormattedMessage(
										"commands." + commandName.toLowerCase() + ".success-receiver"),
								ImmutableMap.<String, String>builder()
										.put("player", sender.getName())
										.put("rank", rank.getName())
										.build(),
								'[', ']'));
					}
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
				// users.setGroup(sender instanceof Player ? (Player) sender : null, args[0],
				// target_rank, true);

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
			for (PRPlayer player : PRCache.getPlayers()) {
				tabcomplete.add(player.getName());
			}
		}

		if (args.length == 2) {
			PRPlayer targetPlayer = PRCache.getPlayer(args[0]);
			if (targetPlayer != null) {
				for (PRPlayerRank rank : targetPlayer.getRanks()) {
					tabcomplete.add(rank.getName());
				}
			}
		}

		return tabcomplete;
	}
}
