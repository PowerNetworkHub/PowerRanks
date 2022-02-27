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
import nl.svenar.common.structure.PRRank;

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
				commandAllowed = sender.hasPermission("powerranks.cmd." + commandName.toLowerCase() + "." + target_rank);
			} else {
				commandAllowed = true;
			}

			if (commandAllowed) {
				PRRank rank = CacheManager.getRank(users.getRankIgnoreCase(target_rank));
				PRPlayer targetPlayer = CacheManager.getPlayer(args[0]);
				if (rank != null && targetPlayer != null) {
					targetPlayer.removeRank(rank.getName());

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
						sender.sendMessage(Util.powerFormatter(
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
									.put("player", targetPlayer.getName())
									.put("rank", rank.getName())
									.build(),
							'[', ']'));
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
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				tabcomplete.add(player.getName());
			}
		}

		if (args.length == 2) {
			Player target_player = Util.getPlayerByName(args[0]);
			if (target_player != null) {
				for (String rank : CacheManager.getPlayer(target_player.getUniqueId().toString()).getRanks()) {
					tabcomplete.add(rank);
				}
			}
		}

		return tabcomplete;
	}
}
