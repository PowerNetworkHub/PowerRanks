package nl.svenar.powerranks.bukkit.commands.rank;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.data.Users;
import nl.svenar.powerranks.bukkit.util.Util;

public class cmd_delperm extends PowerCommand {

	private Users users;

	public cmd_delperm(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 2) {
			final String rankname = args[0].equals("*") ? args[0] : this.users.getRankIgnoreCase(args[0]);
			final String permission = args[1];
			final boolean result = this.users.removePermission(rankname, permission);
			if (result) {
				if (rankname.equals("*")) {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".success-all"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("rank", rankname)
									.put("permission", permission)
									.build(),
							'[', ']'));
				} else {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".success"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("rank", rankname)
									.put("permission", permission)
									.build(),
							'[', ']'));
				}
			} else { // Rank not found
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".failed"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("rank", rankname)
								.put("permission", permission)
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
			List<PRPermission> ranksPermissions = this.users.getPermissions(this.users.getRankIgnoreCase(args[0]));
			// for (Permission pai : Bukkit.getServer().getPermissions()) {
			for (PRPermission permission : ranksPermissions) {

				if (!tabcomplete.contains(permission.getName())) {
					tabcomplete.add(permission.getName());
				}
			}
		}

		return tabcomplete;
	}
}
