package nl.svenar.powerranks.bukkit.commands.player;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.bukkit.commands.PowerCommand;
import nl.svenar.powerranks.bukkit.data.Users;

public class cmd_delplayerperm extends PowerCommand {

	private Users users;

	public cmd_delplayerperm(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 2) {
			final String target_player = args[0];
			final String permission = args[1];
			final boolean result = this.users.delPlayerPermission(target_player, permission);
			if (result) {
				sender.sendMessage(PRUtil.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".success"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", target_player)
								.put("permission", permission)
								.build(),
						'[', ']'));
			} else {
				sender.sendMessage(PRUtil.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".failed"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", target_player)
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
			for (PRPlayer player : CacheManager.getPlayers()) {
				tabcomplete.add(player.getName());
			}
		}

		if (args.length == 2) {
			PRPlayer targetPlayer = CacheManager.getPlayer(args[0]);
			if (targetPlayer != null) {
				// for (Permission pai : Bukkit.getServer().getPermissions()) {
				for (PRPermission perm : targetPlayer.getPermissions()) {
					// String perm = pai.getPermission();
					String userInput = args[1];
					String autocompletePermission = "";

					if (userInput.contains(".")) {
						String[] permSplit = perm.getName().split("\\.");
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
						autocompletePermission = perm.getName().split("\\.")[0];
					}

					while (autocompletePermission.endsWith(".")) {
						autocompletePermission = autocompletePermission.substring(0,
								autocompletePermission.length() - 1);
					}

					if (!tabcomplete.contains(autocompletePermission)) {
						tabcomplete.add(autocompletePermission);
					}
				}
			}
		}

		return tabcomplete;
	}
}