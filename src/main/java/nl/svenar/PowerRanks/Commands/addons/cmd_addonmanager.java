package nl.svenar.PowerRanks.Commands.addons;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
// import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Util.Util;
import nl.svenar.PowerRanks.addons.DownloadableAddon;

public class cmd_addonmanager extends PowerCommand {

	public cmd_addonmanager(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {

		if (args.length == 0) {
			Messages.addonManagerListAddons(sender, 0);
		} else {
			String addonmanagerCommand = args[0].toLowerCase();
			if (addonmanagerCommand.equals("acceptterms")) {
				PowerRanks.getConfigManager().setBool("addon_manager.accepted_terms", true);
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".terms-accepted"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.build(),
						'[', ']'));
				Bukkit.getServer().dispatchCommand(sender, commandLabel + " addonmanager");
			}

			if (addonmanagerCommand.equals("declineterms")) {
				PowerRanks.getConfigManager().setBool("addon_manager.accepted_terms", false);
				sender.sendMessage(Util.powerFormatter(
						PowerRanks.getLanguageManager().getFormattedMessage(
								"commands." + commandName.toLowerCase() + ".terms-declined"),
						ImmutableMap.<String, String>builder()
								.put("player", sender.getName())
								.build(),
						'[', ']'));
			}

			if (addonmanagerCommand.equals("page")) {
				int page = Integer.parseInt(args[1].replaceAll("[a-zA-Z]", ""));
				Messages.addonManagerListAddons(sender, page);
			}

			if (addonmanagerCommand.equals("info")) {
				String addonname = args[1];
				Messages.addonManagerInfoAddon(sender, addonname);
			}

			if (addonmanagerCommand.equals("download")) {
				String addonname = args[1];
				DownloadableAddon addon = null;
				for (DownloadableAddon dlAddon : this.plugin.addonsManager.getAddonDownloader()
						.getDownloadableAddons()) {
					if (dlAddon.getName().equalsIgnoreCase(addonname)) {
						addon = dlAddon;
						break;
					}
				}

				if (addon.isDownloadable()) {
					if (addon.isCompatible()) {
						if (addon.download()) {

							sender.sendMessage(Util.powerFormatter(
									PowerRanks.getLanguageManager().getFormattedMessage(
											"commands." + commandName.toLowerCase() + ".download-complete"),
									ImmutableMap.<String, String>builder()
											.put("player", sender.getName())
											.put("addon", addon.getName())
											.build(),
									'[', ']'));
						} else {
							sender.sendMessage(Util.powerFormatter(
									PowerRanks.getLanguageManager().getFormattedMessage(
											"commands." + commandName.toLowerCase() + ".download-failed"),
									ImmutableMap.<String, String>builder()
											.put("player", sender.getName())
											.put("addon", addon.getName())
											.build(),
									'[', ']'));
						}
					} else {
						sender.sendMessage(Util.powerFormatter(
								PowerRanks.getLanguageManager().getFormattedMessage(
										"commands." + commandName.toLowerCase() + ".download-incompatible"),
								ImmutableMap.<String, String>builder()
										.put("player", sender.getName())
										.build(),
								'[', ']'));
					}
				} else {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".download-not-available"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.build(),
							'[', ']'));
				}
			}

			if (addonmanagerCommand.equals("uninstall")) {
				String addonname = args[1];

				DownloadableAddon addon = null;
				for (DownloadableAddon dlAddon : this.plugin.addonsManager.getAddonDownloader()
						.getDownloadableAddons()) {
					if (dlAddon.getName().equalsIgnoreCase(addonname)) {
						addon = dlAddon;
						break;
					}
				}

				if (addon != null) {
					addon.uninstall();
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".uninstall-complete"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("addon", addon.getName())
									.build(),
							'[', ']'));
				} else {
					sender.sendMessage(Util.powerFormatter(
							PowerRanks.getLanguageManager().getFormattedMessage(
									"commands." + commandName.toLowerCase() + ".failed-addon-not-found"),
							ImmutableMap.<String, String>builder()
									.put("player", sender.getName())
									.put("addon", args[1])
									.build(),
							'[', ']'));
				}
			}
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}
}
