package nl.svenar.powerranks.commands.player;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRPlayerRank;
import nl.svenar.common.structure.PRRank;
import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.cache.CacheManager;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.Users;
import nl.svenar.powerranks.util.Util;

public class cmd_delrank extends PowerCommand {

	private Users users;

	public cmd_delrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 2) {
			String target_rank = users.getRankIgnoreCase(args[1]);

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
			Player targetPlayer = Util.getPlayerByName(args[0]);
			if (targetPlayer != null) {
				List<String> ranks = new ArrayList<>();
				for (PRPlayerRank rank : CacheManager.getPlayer(targetPlayer.getUniqueId().toString()).getRanks()) {
					ranks.add(rank.getName());
				}
				for (String rank : ranks) {
					tabcomplete.add(rank);
				}
			}
		}

		return tabcomplete;
	}
}
