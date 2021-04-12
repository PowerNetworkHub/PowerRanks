package nl.svenar.PowerRanks.Commands;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.Data.Users;
import nl.svenar.PowerRanks.addons.DownloadableAddon;

public class cmd_addonmanager extends PowerCommand {

	private Users users;

	public cmd_addonmanager(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.users = new Users(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.addonmanager")) {

			if (args.length == 0) {
				Messages.addonManagerListAddons(sender, 0);
			} else {
				String addonmanagerCommand = args[0].toLowerCase();
				if (addonmanagerCommand.equals("acceptterms")) {
					CachedConfig.set("addon_manager.accepted_terms", true);
					Messages.addonManagerTermsAccepted(sender);
					Bukkit.getServer().dispatchCommand(sender, commandLabel + " addonmanager");
				}

				if (addonmanagerCommand.equals("declineterms")) {
					CachedConfig.set("addon_manager.accepted_terms", false);
					Messages.addonManagerTermsDeclined(sender);
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
					for (DownloadableAddon dlAddon : this.plugin.addonsManager.getAddonDownloader().getDownloadableAddons()) {
						if (dlAddon.getName().equalsIgnoreCase(addonname)) {
							addon = dlAddon;
							break;
						}
					}

					if (addon.isDownloadable()) {
						if (addon.isCompatible()) {
							if (addon.download()) {
								Messages.addonManagerDownloadComplete(sender, addon.getName());
							} else {
								Messages.addonManagerDownloadFailed(sender, addon.getName());
							}
						} else {
							Messages.addonManagerDownloadNotAvailable(sender);
						}
					} else {
						Messages.addonManagerDownloadNotAvailable(sender);
					}
				}

				if (addonmanagerCommand.equals("uninstall")) {
					String addonname = args[1];

					DownloadableAddon addon = null;
					for (DownloadableAddon dlAddon : this.plugin.addonsManager.getAddonDownloader().getDownloadableAddons()) {
						if (dlAddon.getName().equalsIgnoreCase(addonname)) {
							addon = dlAddon;
							break;
						}
					}

					if (addon != null) {
						addon.uninstall();
						Messages.addonManagerUninstallComplete(sender, addon.getName());
					} else {
						Messages.messageCommandErrorAddonNotFound(sender, args[1]);
					}
				}
			}

		} else {
			Messages.noPermission(sender);
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}
}
