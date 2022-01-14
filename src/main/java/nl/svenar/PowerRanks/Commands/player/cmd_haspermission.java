package nl.svenar.PowerRanks.Commands.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Util.Util;
import nl.svenar.common.structure.PRPermission;

public class cmd_haspermission extends PowerCommand {

	public cmd_haspermission(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.haspermission")) {
			Player player = null;
			String permissionNode = null;

			if (args.length == 1) {
				permissionNode = args[0];

				if (sender instanceof ConsoleCommandSender) {
					Messages.messagePlayerNotFound(sender, sender.getName());
				} else {
					player = (Player) sender;
				}

				if (permissionNode == null || permissionNode.length() == 0) {
					Messages.messageGroupNotFound(sender, args[0]);
				}

			} else if (args.length == 2) {
				player = Util.getPlayerByName(args[0]);
				permissionNode = args[1];

				if (player == null) {
					Messages.messagePlayerNotFound(sender, args[0]);
				}

				if (permissionNode == null || permissionNode.length() == 0) {
					Messages.messageGroupNotFound(sender, args[1]);
				}
			} else {
				Messages.messageCommandUsageHasPermission(sender);
			}

			if (player != null && permissionNode != null && permissionNode.length() > 0) {
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
			}

		} else {
			Messages.noPermission(sender);
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
