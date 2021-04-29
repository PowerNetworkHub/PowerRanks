package nl.svenar.PowerRanks.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;

public class cmd_addoninfo extends PowerCommand {


	public cmd_addoninfo(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.addoninfo")) {
			if (args.length == 1) {
				final String addon_name = args[0];
				PowerRanksAddon addon = null;
				for (Entry<File, PowerRanksAddon> a : this.plugin.addonsManager.addonClasses.entrySet()) {
					if (a.getValue().getIdentifier().equalsIgnoreCase(addon_name))
						addon = a.getValue();
				}
				if (addon != null) {
					sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + this.plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
					sender.sendMessage(ChatColor.DARK_GREEN + "Add-on name: " + ChatColor.GREEN + addon.getIdentifier());
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
					sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------" + ChatColor.BLUE + "===");
				} else {
					Messages.messageCommandErrorAddonNotFound(sender, addon_name);
				}
			} else {
				Messages.messageCommandUsageAddoninfo(sender);
			}
		} else {
			Messages.noPermission(sender);
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
