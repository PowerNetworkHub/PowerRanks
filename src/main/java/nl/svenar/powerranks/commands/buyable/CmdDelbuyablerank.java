package nl.svenar.powerranks.commands.buyable;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.common.structure.PRRank;
import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.Users;
import nl.svenar.powerranks.util.Util;

public class CmdDelbuyablerank extends PowerCommand {

	private Users users;

	public CmdDelbuyablerank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName, String[] args) {
			if (args.length == 2) {
				final String rankname = this.users.getRankIgnoreCase(args[0]);
				final String rankname2 = this.users.getRankIgnoreCase(args[1]);
				final boolean success = this.users.delBuyableRank(rankname, rankname2);
				if (success) {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".success-del"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("rank", rankname)
									.put("target_rank", rankname2)
									.build(),
							'[', ']'));
				} else {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".failed-del"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("rank", rankname)
									.put("target_rank", rankname2)
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
			for (String rank : this.users.getBuyableRanks(this.users.getRankIgnoreCase(args[0]))) {
				if (!rank.toLowerCase().contains(args[0].toLowerCase())) {
					tabcomplete.add(rank);
				}
			}
		}

		return tabcomplete;
	}
}
