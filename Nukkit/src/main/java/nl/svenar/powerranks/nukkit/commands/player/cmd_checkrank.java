package nl.svenar.powerranks.nukkit.commands.player;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;


import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;

public class cmd_checkrank extends PowerCommand {

	public cmd_checkrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				// String rankname = this.users.getPrimaryRank();
				List<String> playerRanks = new ArrayList<>();
				for (PRPlayerRank rank : PRCache.getPlayer(((Player) sender).getUniqueId().toString())
						.getRanks()) {
					playerRanks.add(rank.getName());
				}
				if (playerRanks.size() > 0) {
					sender.sendMessage(PRUtil.powerFormatter(
							plugin.getLanguageManager()
									.getFormattedMessage("commands." + commandName.toLowerCase() + ".success-self"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("ranks", String.join(", ", playerRanks))
									.build(),
							'[', ']'));
				} else {
					sender.sendMessage(PRUtil.powerFormatter(
							plugin.getLanguageManager()
									.getFormattedMessage(
											"commands." + commandName.toLowerCase() + ".success-self-none"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.build(),
							'[', ']'));
				}
			} else {
				sender.sendMessage(
						plugin.getLanguageManager().getFormattedMessage("general.console-is-no-player"));
			}
		} else if (args.length == 1) {
			PRPlayer targetPlayer = PRCache.getPlayer(args[0]);
			if (targetPlayer != null) {
				List<String> playerRanks = new ArrayList<>();
				for (PRPlayerRank rank : PRCache.getPlayer((targetPlayer).getUUID().toString()).getRanks()) {
					playerRanks.add(rank.getName());
				}
				if (playerRanks.size() > 0) {
					sender.sendMessage(PRUtil.powerFormatter(
							plugin.getLanguageManager()
									.getFormattedMessage("commands." + commandName.toLowerCase() + ".success-target"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("target", targetPlayer.getName())
									.put("ranks", String.join(", ", playerRanks))
									.build(),
							'[', ']'));
				} else {
					sender.sendMessage(PRUtil.powerFormatter(
							plugin.getLanguageManager()
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
						plugin.getLanguageManager().getFormattedMessage("general.player-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", args[0])
								.build(),
						'[', ']'));
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

		return tabcomplete;
	}
}
