package nl.svenar.powerranks.bukkit.commands.rank;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.data.Users;
import nl.svenar.powerranks.bukkit.util.Util;

public class cmd_addperm extends PowerCommand {

	private Users users;

	public cmd_addperm(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 2) {
			final String rankname = args[0].equals("*") ? args[0] : this.users.getRankIgnoreCase(args[0]);
			String permission = args[1];
			boolean allowed = true;
			// this.setValue(!name.startsWith("-"));
			if (permission.startsWith("-")) {
				permission = permission.replaceFirst("-", "");
				allowed = false;
			}
			final boolean result = this.users.addPermission(rankname, permission, allowed);
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
			// for (Permission pai : Bukkit.getServer().getPermissions()) {
			for (PermissionAttachmentInfo pai : Bukkit.getServer().getConsoleSender().getEffectivePermissions()) {
				String perm = pai.getPermission();
				String userInput = args[1];
				String autocompletePermission = "";

				if (userInput.contains(".")) {
					String[] permSplit = perm.split("\\.");
					for (int i = 0; i < permSplit.length; i++) {
						String targetPerm = String.join(".", permSplit);
						while (targetPerm.endsWith(".")) {
							targetPerm = targetPerm.substring(0, targetPerm.length() - 1);
						}
						if (targetPerm.contains(userInput)) {
							autocompletePermission = targetPerm;
							permSplit[permSplit.length - 1 - i] = "";

						} else {
							break;
						}
					}
				} else {
					autocompletePermission = perm.split("\\.")[0];
				}

				while (autocompletePermission.endsWith(".")) {
					autocompletePermission = autocompletePermission.substring(0, autocompletePermission.length() - 1);
				}

				if (!tabcomplete.contains(autocompletePermission)) {
					tabcomplete.add(autocompletePermission);
				}
			}
		}

		return tabcomplete;
	}
}
