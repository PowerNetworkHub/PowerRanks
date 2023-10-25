package nl.svenar.powerranks.bukkit.commands.player;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.data.Users;
import nl.svenar.powerranks.bukkit.util.Util;

public class cmd_addrank extends PowerCommand {

	private Users users;

	public cmd_addrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length >= 2) {
			String target_rank = users.getRankIgnoreCase(args[1]);
			String[] tags = Arrays.copyOfRange(args, 2, args.length);

			boolean commandAllowed = false;
			if (sender instanceof Player) {
				commandAllowed = sender
						.hasPermission("powerranks.cmd." + commandName.toLowerCase() + "." + target_rank);
			} else {
				commandAllowed = true;
			}

			if (commandAllowed) {
				PRRank rank = CacheManager.getRank(users.getRankIgnoreCase(target_rank));
				PRPlayer targetPlayer = CacheManager.getPlayer(args[0]);
				if (rank != null && targetPlayer != null) {
					PRPlayerRank playerRank = new PRPlayerRank(rank.getName());
					boolean alreadyHasRank = targetPlayer.hasRank(rank.getName());
					if (!alreadyHasRank) {

						for (String tag : tags) {
							if (tag.split(":").length == 2) {
								String[] tagParts = tag.split(":");
								String tagName = tagParts[0];
								String tagValue = tagParts[1];

								if (tagName.length() > 0 && tagValue.length() > 0) {
									playerRank.addTag(tagName, tagValue);
								}
							}
						}

						targetPlayer.addRank(playerRank);

						if (Bukkit.getPlayer(targetPlayer.getUUID()) != null) {
							PowerRanks.getInstance().updateTablistName(Bukkit.getPlayer(targetPlayer.getUUID()));
							PowerRanks.getInstance().getTablistManager()
									.updateSorting(Bukkit.getPlayer(targetPlayer.getUUID()));
						}

						sender.sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager()
										.getFormattedMessage(
												"commands." + commandName.toLowerCase() + ".success-executor"),
								ImmutableMap.<String, String>builder()
										.put("player", targetPlayer.getName())
										.put("rank", rank.getName())
										.build(),
								'[', ']'));
						if (Bukkit.getPlayer(targetPlayer.getUUID()) != null) {
							Bukkit.getPlayer(targetPlayer.getUUID()).sendMessage(Util.powerFormatter(
									PowerRanks.getLanguageManager().getFormattedMessage(
											"commands." + commandName.toLowerCase() + ".success-receiver"),
									ImmutableMap.<String, String>builder()
											.put("player", sender.getName())
											.put("rank", rank.getName())
											.build(),
									'[', ']'));
						}
					} else {
						sender.sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager()
										.getFormattedMessage(
												"commands." + commandName.toLowerCase() + ".failed-already-has-rank"),
								ImmutableMap.<String, String>builder()
										.put("player", targetPlayer.getName())
										.put("rank", rank.getName())
										.build(),
								'[', ']'));
					}
				} else {
					if (targetPlayer != null && rank != null) {
						sender.sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager()
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
			for (PRPlayer player : CacheManager.getPlayers()) {
				tabcomplete.add(player.getName());
			}
		}

		if (args.length == 2) {
			PRPlayer targetPlayer = CacheManager.getPlayer(args[0]);
			if (targetPlayer != null) {
				for (PRRank rank : CacheManager.getRanks()) {
					if (!targetPlayer.hasRank(rank.getName())) {
						tabcomplete.add(rank.getName());
					}
				}
			}
		}

		return tabcomplete;
	}
}
