package nl.svenar.powerranks.bukkit.commands.player;

import java.util.ArrayList;

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

public class cmd_setownrank extends PowerCommand {

	private Users users;

	public cmd_setownrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 1) {
			String targetRank = users.getRankIgnoreCase(args[0]);

			boolean commandAllowed = false;
			if (sender instanceof Player) {
				commandAllowed = sender
						.hasPermission("powerranks.cmd." + commandName.toLowerCase() + "." + targetRank);
			} else {
				commandAllowed = true;
			}

			if (commandAllowed) {
				PRRank rank = CacheManager.getRank(users.getRankIgnoreCase(targetRank));
				PRPlayer targetPlayer = CacheManager.getPlayer(sender.getName());
				if (rank != null && targetPlayer != null) {
					PRPlayerRank playerRank = new PRPlayerRank(rank.getName());
					targetPlayer.setRank(playerRank);

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
			if (sender instanceof Player) {
				PRPlayer targetPlayer = CacheManager.getPlayer(sender.getName());
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
