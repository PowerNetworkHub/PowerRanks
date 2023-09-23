package nl.svenar.powerranks.commands.player;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import nl.svenar.common.structure.PRPermission;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.cache.CacheManager;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.util.Util;

public class cmd_haspermission extends PowerCommand {

	public cmd_haspermission(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		PRPlayer prPlayer = null;
		String permissionNode = null;

		if (args.length == 2) {
			prPlayer = CacheManager.getPlayer(args[0]);
			permissionNode = args[1];

			if (prPlayer == null) {
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage("general.player-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("target", args[0])
								.build(),
						'[', ']'));
			}

			if (permissionNode == null || permissionNode.length() == 0) {
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage("general.permission-not-found"),
						ImmutableMap.<String, String>builder()
								.put("permission", args[1])
								.build(),
						'[', ']'));
			}
		} else {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		}

		if (prPlayer != null && permissionNode != null && permissionNode.length() > 0) {
			Player player = Bukkit.getPlayer(prPlayer.getUUID());
			if (player != null) {
				List<PRPermission> playerPermissions = PowerRanks.getInstance().getEffectivePlayerPermissions(player);
				PRPermission targetPermission = null;
				PRPermission targetWildcardPermission = null;

				for (PRPermission permission : playerPermissions) {
					if (permission.getName().equals(permissionNode)) {
						targetPermission = permission;
						break;
					}
				}

				ArrayList<String> wildcardPermissions = Util.generateWildcardList(permissionNode);
				for (PRPermission perm : playerPermissions) {

					if (wildcardPermissions.contains(perm.getName())) {
						targetWildcardPermission = perm;
						break;
					}
				}

				sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA
						+ PowerRanks.getInstance().getDescription().getName() + ChatColor.DARK_AQUA + "----------"
						+ ChatColor.BLUE + "===");
				sender.sendMessage(ChatColor.GREEN + "Player: " + ChatColor.DARK_GREEN + player.getName());
				sender.sendMessage(ChatColor.GREEN + "Permission: " + ChatColor.DARK_GREEN + permissionNode);
				sender.sendMessage(ChatColor.GREEN + "Player has permission: "
						+ (targetPermission == null ? ChatColor.DARK_RED + "no" : ChatColor.DARK_GREEN + "yes"));
				sender.sendMessage(ChatColor.GREEN + "Permission allowed value: "
						+ (targetPermission == null ? ChatColor.GOLD + "unknown"
								: (targetPermission.getValue() ? ChatColor.DARK_GREEN + "allowed"
										: ChatColor.DARK_RED + "denied")));
				sender.sendMessage(ChatColor.GREEN + "has Wildcard variant: "
						+ (targetWildcardPermission == null ? ChatColor.GOLD + "no"
								: ChatColor.DARK_GREEN + targetWildcardPermission.getName() + " (allowed: "
										+ (targetWildcardPermission.getValue() ? ChatColor.DARK_GREEN + "yes"
												: ChatColor.DARK_RED + "no")
										+ ChatColor.DARK_GREEN + ")"));
				sender.sendMessage(ChatColor.GREEN + "Is permission allowed: "
						+ (player.hasPermission(permissionNode) ? ChatColor.DARK_GREEN + "yes"
								: ChatColor.DARK_RED + "no"));
				sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------"
						+ ChatColor.BLUE + "===");
			} else {
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage("general.player-not-online"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.build(),
						'[', ']'));
			}
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
