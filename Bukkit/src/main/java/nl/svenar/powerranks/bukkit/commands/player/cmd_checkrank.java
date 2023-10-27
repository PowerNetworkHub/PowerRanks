package nl.svenar.powerranks.bukkit.commands.player;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;

public class cmd_checkrank extends PowerCommand {

	public cmd_checkrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				// String rankname = this.users.getPrimaryRank();
				List<String> playerRanks = new ArrayList<>();
				for (PRPlayerRank rank : CacheManager.getPlayer(((Player) sender).getUniqueId().toString())
						.getRanks()) {
					playerRanks.add(rank.getName());
				}
				if (playerRanks.size() > 0) {
					sender.sendMessage(PRUtil.powerFormatter(
							PowerRanks.getLanguageManager()
									.getFormattedMessage("commands." + commandName.toLowerCase() + ".success-self"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("ranks", String.join(", ", playerRanks))
									.build(),
							'[', ']'));
				} else {
					sender.sendMessage(PRUtil.powerFormatter(
							PowerRanks.getLanguageManager()
									.getFormattedMessage(
											"commands." + commandName.toLowerCase() + ".success-self-none"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.build(),
							'[', ']'));
				}
			} else {
				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage("general.console-is-no-player"));
			}
		} else if (args.length == 1) {
			PRPlayer targetPlayer = CacheManager.getPlayer(args[0]);
			if (targetPlayer != null) {
				List<String> playerRanks = new ArrayList<>();
				for (PRPlayerRank rank : CacheManager.getPlayer((targetPlayer).getUUID().toString()).getRanks()) {
					playerRanks.add(rank.getName());
				}
				if (playerRanks.size() > 0) {
					sender.sendMessage(PRUtil.powerFormatter(
							PowerRanks.getLanguageManager()
									.getFormattedMessage("commands." + commandName.toLowerCase() + ".success-target"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("target", targetPlayer.getName())
									.put("ranks", String.join(", ", playerRanks))
									.build(),
							'[', ']'));
				} else {
					sender.sendMessage(PRUtil.powerFormatter(
							PowerRanks.getLanguageManager()
									.getFormattedMessage(
											"commands." + commandName.toLowerCase() + ".success-target-none"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("target", targetPlayer.getName())
									.build(),
							'[', ']'));
				}
			} else {
				sender.sendMessage(PRUtil.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage("general.player-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", args[0])
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
			for (PRPlayer player : CacheManager.getPlayers()) {
				tabcomplete.add(player.getName());
			}
		}

		return tabcomplete;
	}
}
