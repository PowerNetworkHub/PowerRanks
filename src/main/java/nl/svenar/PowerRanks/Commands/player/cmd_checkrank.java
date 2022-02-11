package nl.svenar.PowerRanks.Commands.player;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.Util.Util;

public class cmd_checkrank extends PowerCommand {

	private Users users;

	public cmd_checkrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				String rankname = this.users.getPrimaryRank((Player) sender);
				Messages.messageCommandCheckrankResponse(sender, sender.getName(), rankname);
			} else {
				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage("general.console-is-no-player"));
			}
		} else if (args.length == 1) {
			Player targetPlayer = Util.getPlayerByName(args[0]);
			if (targetPlayer != null) {
				String rankname = this.users.getPrimaryRank(targetPlayer);
				if (rankname.length() > 0) {
					Messages.messageCommandCheckrankResponse(sender, targetPlayer.getName(), rankname);
				} else {
					Messages.messageCommandCheckrankResponse(sender, targetPlayer.getName());
				}
			} else {
				sender.sendMessage(Util.powerFormatter(
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
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				tabcomplete.add(player.getName());
			}
		}

		return tabcomplete;
	}
}
