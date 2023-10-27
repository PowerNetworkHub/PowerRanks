package nl.svenar.powerranks.bukkit.commands.rank;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.data.Users;

public class cmd_addinheritance extends PowerCommand {

	private Users users;

	public cmd_addinheritance(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 2) {
			final String rankname = this.users.getRankIgnoreCase(args[0]);
			final String inheritance = args[1];
			final boolean result = this.users.addInheritance(rankname, inheritance);
			if (result) {
				sender.sendMessage(PRUtil.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".success"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("rank", rankname)
								.put("inheritance", inheritance)
								.build(),
						'[', ']'));
			} else {
				sender.sendMessage(PRUtil.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"general.rank-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("rank", CacheManager.getRank(rankname) == null ? rankname : inheritance)
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
			for (PRRank rank : this.users.getGroups()) {
				tabcomplete.add(rank.getName());
			}
		}

		if (args.length == 2) {
			for (PRRank rank : this.users.getGroups()) {
				if (!rank.getName().toLowerCase().contains(args[0].toLowerCase())) {
					tabcomplete.add(rank.getName());
				}
			}
		}

		return tabcomplete;
	}
}
