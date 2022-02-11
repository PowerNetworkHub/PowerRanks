package nl.svenar.PowerRanks.Commands.rank;

import java.util.ArrayList;
import java.util.Objects;

import com.google.common.collect.ImmutableMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.Util.Util;
import nl.svenar.common.structure.PRRank;

public class cmd_setweight extends PowerCommand {

	private Users users;

	public cmd_setweight(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 2) {

			final String rankname = this.users.getRankIgnoreCase(args[0]);
			final PRRank rank = CacheManager.getRank(rankname);
			if (Objects.isNull(rank)) {
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"general.rank-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("rank", rankname)
								.build(),
						'[', ']'));
			}

			int weight = 0;
			try {
				weight = Integer.parseInt(args[1]);
			} catch (Exception e) {
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".numbers-only"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("rank", rankname)
								.put("weight", args[1])
								.build(),
						'[', ']'));
			}

			rank.setWeight(weight);
			sender.sendMessage(Util.powerFormatter(
					PowerRanks.getLanguageManager().getFormattedMessage(
							"commands." + commandName.toLowerCase() + ".success"),
					ImmutableMap.<String, String>builder()
							.put("player", sender.getName())
							.put("rank", rankname)
							.put("weight", String.valueOf(weight))
							.build(),
					'[', ']'));
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
			for (PRRank rank : this.users.getGroups()) {
				tabcomplete.add(rank.getName());
			}
		}

		return tabcomplete;
	}
}
