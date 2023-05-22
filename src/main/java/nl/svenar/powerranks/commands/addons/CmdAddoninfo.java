package nl.svenar.powerranks.commands.addons;

import java.io.File;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.addons.PowerRanksAddon;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.util.Util;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAddoninfo extends PowerCommand {

	public CmdAddoninfo(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 1) {
			final String addon_name = args[0];
			PowerRanksAddon addon = null;
			for (Entry<File, PowerRanksAddon> a : this.plugin.addonsManager.addonClasses.entrySet()) {
				if (a.getValue().getIdentifier().equalsIgnoreCase(addon_name))
					addon = a.getValue();
			}
			if (addon != null) {
				sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA
						+ this.plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------"
						+ ChatColor.BLUE + "===");
				sender.sendMessage(
						ChatColor.DARK_GREEN + "Add-on name: " + ChatColor.GREEN + addon.getIdentifier());
				sender.sendMessage(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + addon.getAuthor());
				sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + addon.getVersion());
				sender.sendMessage(ChatColor.DARK_GREEN + "Registered Commands:");
				for (String command : addon.getRegisteredCommands()) {
					sender.sendMessage(ChatColor.GREEN + "- /" + commandLabel + " " + command);
				}
				sender.sendMessage(ChatColor.DARK_GREEN + "Registered Permissions:");
				for (String permission : addon.getRegisteredPermissions()) {
					sender.sendMessage(ChatColor.GREEN + "- " + permission);
				}
				sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------"
						+ ChatColor.BLUE + "===");
			} else {
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".failed-addon-not-found"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.put("addon", addon_name)
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

		for (Entry<File, PowerRanksAddon> addon : this.plugin.addonsManager.addonClasses.entrySet()) {
			tabcomplete.add(addon.getValue().getIdentifier());
		}

		return tabcomplete;
	}
}
