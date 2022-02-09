package nl.svenar.PowerRanks.Commands.buyable;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.Util.Util;
import nl.svenar.common.structure.PRRank;

public class cmd_setbuycost extends PowerCommand {

	private Users users;

	public cmd_setbuycost(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName, String[] args) {
			if (args.length == 2) {
				final String rankname = this.users.getRankIgnoreCase(args[0]);
				final String cost = this.users.getRankIgnoreCase(args[1]);
				final boolean success = this.users.setBuyCost(rankname, cost);
				if (success) {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".success-set"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("rank", rankname)
									.put("cost", cost)
									.build(),
							'[', ']'));
				} else {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".failed-set"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("rank", rankname)
									.put("cost", cost)
									.build(),
							'[', ']'));
				}
			} else {
				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
								"commands." + commandName.toLowerCase() + ".arguments"));
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
			tabcomplete.add("0");
			tabcomplete.add("10");
			tabcomplete.add("100");
			tabcomplete.add("1000");
			tabcomplete.add("10000");
		}

		return tabcomplete;
	}
}
