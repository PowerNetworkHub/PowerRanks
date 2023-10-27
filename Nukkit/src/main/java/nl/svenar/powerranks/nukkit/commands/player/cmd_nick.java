package nl.svenar.powerranks.nukkit.commands.player;

import java.util.ArrayList;


import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.nukkit.util.Util;

public class cmd_nick extends PowerCommand {

	public cmd_nick(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
	}

	@Override
	public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
		if (args.length == 1) { // Change own nickname
			if (sender instanceof Player) {
				Player targetPlayer = (Player) sender;
				String nickname = args[0];
				if (sender.hasPermission("powerranks.cmd." + commandName.toLowerCase() + ".setown")) {
					if (updateNickname(targetPlayer, nickname)) {

						sender.sendMessage(PRUtil.powerFormatter(
								plugin.getLanguageManager()
										.getFormattedMessage(
												"commands." + commandName.toLowerCase() + ".setown.success"),
								ImmutableMap.<String, String>builder()
										.put("target", targetPlayer.getName())
										.put("nickname", nickname)
										.build(),
								'[', ']'));
					} else {

						sender.sendMessage(PRUtil.powerFormatter(
								plugin.getLanguageManager()
										.getFormattedMessage(
												"commands." + commandName.toLowerCase() + ".setown.failed"),
								ImmutableMap.<String, String>builder()
										.put("target", targetPlayer.getName())
										.put("nickname", nickname)
										.build(),
								'[', ']'));
					}
				} else {
					sender.sendMessage(plugin.getLanguageManager().getFormattedMessage("general.no-permission"));
				}
			}

		} else if (args.length == 2) { // Change other's nickname
			String targetPlayername = args[0];
			Player targetPlayer = Util.getPlayerByName(targetPlayername);
			if (targetPlayer != null) {
				String nickname = args[1];
				if (sender.hasPermission("powerranks.cmd." + commandName.toLowerCase() + ".setother")) {
					if (updateNickname(targetPlayer, nickname)) {

						targetPlayer.sendMessage(PRUtil.powerFormatter(
								plugin.getLanguageManager()
										.getFormattedMessage(
												"commands." + commandName.toLowerCase() + ".setown.success"),
								ImmutableMap.<String, String>builder()
										.put("target", targetPlayer.getName())
										.put("nickname", nickname)
										.build(),
								'[', ']'));

						sender.sendMessage(PRUtil.powerFormatter(
								plugin.getLanguageManager()
										.getFormattedMessage(
												"commands." + commandName.toLowerCase() + ".setother.success"),
								ImmutableMap.<String, String>builder()
										.put("target", targetPlayer.getName())
										.put("nickname", nickname)
										.build(),
								'[', ']'));

					} else {
						sender.sendMessage(PRUtil.powerFormatter(
								plugin.getLanguageManager()
										.getFormattedMessage(
												"commands." + commandName.toLowerCase() + ".setother.failed"),
								ImmutableMap.<String, String>builder()
										.put("target", targetPlayer.getName())
										.put("nickname", nickname)
										.build(),
								'[', ']'));

					}
				} else {
					sender.sendMessage(plugin.getLanguageManager().getFormattedMessage("general.no-permission"));
				}
			}

		} else {
			if (sender.hasPermission("powerranks.cmd." + commandName.toLowerCase())
					|| sender.hasPermission("powerranks.cmd." + commandName.toLowerCase() + ".*")) {
				sender.sendMessage(
						plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
								"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
			} else {
				sender.sendMessage(plugin.getLanguageManager().getFormattedMessage("general.no-permission"));
			}
		}

		return false;
	}

	private boolean updateNickname(Player targetPlayer, String nickname) {
		PRPlayer prPlayer = PRCache.getPlayer(targetPlayer.getUniqueId().toString());
		if (prPlayer == null) {
			return false;
		}
		prPlayer.setNickname(nickname);
		return true;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			for (PRPlayer prPlayer : PRCache.getPlayers()) {
				tabcomplete.add(prPlayer.getName());
			}
		}

		if (args.length == 2) {
			PRPlayer targetPlayer = PRCache.getPlayer(args[0]);
			if (targetPlayer != null) {
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
