package nl.svenar.powerranks.bukkit.commands.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.data.Users;
import nl.svenar.powerranks.bukkit.util.Util;

public class cmd_setrank extends PowerCommand {

	private Users users;

	public cmd_setrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length >= 2) {
			String target_rank = users.getRankIgnoreCase(args[1]);
			String[] tags = Arrays.copyOfRange(args, 2, args.length);

			boolean commandAllowed = sender.hasPermission("powerranks.cmd." + commandName.toLowerCase());
			if (sender instanceof Player) {
				for (PRPermission permission : PowerRanks.getInstance()
						.getEffectivePlayerPermissions((Player) sender)) {
					if (permission.getName()
							.equalsIgnoreCase("powerranks.cmd." + commandName.toLowerCase() + "." + target_rank)) {
						commandAllowed = permission.getValue();
						break;
					}
				}
			}

			if (commandAllowed) {
				PRRank rank = CacheManager.getRank(users.getRankIgnoreCase(target_rank));
				PRPlayer targetPlayer = CacheManager.getPlayer(args[0]);
				if (rank != null && targetPlayer != null) {
					PRPlayerRank playerRank = new PRPlayerRank(rank.getName());

					for (String tag : tags) {
						if (tag.split(":").length == 2) {
							String[] tagParts = tag.split(":");
							String tagName = tagParts[0];
							String tagValue = tagParts[1];

							if (tagName.length() > 0 && tagValue.length() > 0) {
								playerRank.addTag(tagName, tagValue);
								if (tagName.equalsIgnoreCase("expires")) {
									List<String> returnRanks = new ArrayList<String>();
									for (PRPlayerRank prRank : targetPlayer.getRanks()) {
										String prRankTags = "";
										for (Entry<String, Object> prRankTagEntry : prRank.getTags().entrySet()) {
											prRankTags += ";" + prRankTagEntry.getKey() + ":" + prRankTagEntry.getValue();
										}
										returnRanks.add(prRank.getName() + prRankTags);
									}
									playerRank.addTag("expiry-return-ranks", returnRanks);
								}
							}
						}
					}

					targetPlayer.setRank(playerRank);

					if (Bukkit.getPlayer(targetPlayer.getUUID()) != null) {
						PowerRanks.getInstance().updateTablistName(Bukkit.getPlayer(targetPlayer.getUUID()));
						PowerRanks.getInstance().getTablistManager()
								.updateSorting(Bukkit.getPlayer(targetPlayer.getUUID()));
					}

					boolean hasRank = false;
					for (PRPlayerRank playerrank : targetPlayer.getRanks()) {
						if (playerrank.getName().equals(rank.getName())) {
							hasRank = true;
							break;
						}
					}
					if (hasRank) {
						sender.sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager()
										.getFormattedMessage(
												"commands." + commandName.toLowerCase() + ".success-executor"),
								ImmutableMap.<String, String>builder()
										.put("player", targetPlayer.getName())
										.put("rank", rank.getName())
										.build(),
								'[', ']'));
					} else {
						sender.sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager()
										.getFormattedMessage(
												"commands." + commandName.toLowerCase() + ".success-executor"),
								ImmutableMap.<String, String>builder()
										.put("player", targetPlayer.getName())
										.put("rank", rank.getName())
										.build(),
								'[', ']'));
					}

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
											"commands." + commandName.toLowerCase() + ".failed-executor"),
							ImmutableMap.<String, String>builder()
									.put("player", targetPlayer == null ? args[0] : targetPlayer.getName())
									.put("rank", rank == null ? args[1] : rank.getName())
									.build(),
							'[', ']'));
				}

			} else {
				sender.sendMessage(PowerRanks.getLanguageManager().getFormattedMessage("general.no-permission"));
			}
		} else {
			if (sender.hasPermission("powerranks.cmd." + commandName.toLowerCase())
					|| sender.hasPermission("powerranks.cmd." + commandName.toLowerCase() + ".*")) {
				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
								"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
			} else {
				sender.sendMessage(PowerRanks.getLanguageManager().getFormattedMessage("general.no-permission"));
			}
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
