package nl.svenar.PowerRanks.Commands.core;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Util.PluginReloader;

public class cmd_reload extends PowerCommand {

	public cmd_reload(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		sender.sendMessage(
				PowerRanks.getLanguageManager().getFormattedMessage(
						"commands." + commandName.toLowerCase() + ".warning"));

		if (args.length != 1) {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		} else {
			if (args[0].equalsIgnoreCase("config")) {

				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".config-start"));

				PowerRanks.getConfigManager().reload();
				CacheManager.load(PowerRanks.fileLoc);
				this.plugin.updateAllPlayersTABlist();

				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".config-done"));

			} else if (args[0].equalsIgnoreCase("plugin")) {

				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".plugin-start"));

				PluginReloader pluginReloader = new PluginReloader();
				pluginReloader.reload(PowerRanks.pdf.getName());

				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".plugin-done"));

			} else if (args[0].equalsIgnoreCase("addons")) {

				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".addons-start"));

				PowerRanks.getInstance().addonsManager.disable();
				PowerRanks.getInstance().addonsManager.setup();

				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".addons-done"));

			} else if (args[0].equalsIgnoreCase("all")) {

				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".config-start"));

				PowerRanks.getConfigManager().reload();

				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".config-done"));

				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".plugin-start"));

				PluginReloader pluginReloader = new PluginReloader();
				pluginReloader.reload(PowerRanks.pdf.getName());

				CacheManager.load(PowerRanks.fileLoc);
				this.plugin.updateAllPlayersTABlist();

				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".plugin-done"));

			} else {
				sender.sendMessage(
						PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
								"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
			}
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();

		if (args.length == 1) {
			tabcomplete.add("plugin");
			tabcomplete.add("config");
			tabcomplete.add("addons");
			tabcomplete.add("all");
		}

		return tabcomplete;
	}
}
