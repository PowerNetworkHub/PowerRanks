package nl.svenar.PowerRanks.Commands.player;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.Util.Util;
import nl.svenar.common.structure.PRPermission;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRPlayerRank;
import nl.svenar.common.structure.PRRank;

public class cmd_setrank extends PowerCommand {

	private Users users;

	public cmd_setrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 2) {
			String target_rank = users.getRankIgnoreCase(args[1]);

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
					targetPlayer.setRank(playerRank);

                    if (Bukkit.getPlayer(targetPlayer.getUUID()) != null) {
                        PowerRanks.getInstance().updateTablistName(Bukkit.getPlayer(targetPlayer.getUUID()));
                        PowerRanks.getInstance().getTablistManager().updateSorting(Bukkit.getPlayer(targetPlayer.getUUID()));
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
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				tabcomplete.add(player.getName());
			}
		}

		if (args.length == 2) {
			for (PRRank rank : this.users.getGroups()) {
				tabcomplete.add(rank.getName());
			}
		}

		return tabcomplete;
	}
}
