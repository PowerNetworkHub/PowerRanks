package nl.svenar.PowerRanks.Commands.addons;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
// import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.addons.DownloadableAddon;

public class cmd_addonmanager extends PowerCommand {


	public cmd_addonmanager(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.addonmanager")) {

			if (args.length == 0) {
				Messages.addonManagerListAddons(sender, 0);
			} else {
				String addonmanagerCommand = args[0].toLowerCase();
				if (addonmanagerCommand.equals("acceptterms")) {
					PowerRanks.getConfigManager().setBool("addon_manager.accepted_terms", true);
					Messages.addonManagerTermsAccepted(sender);
					Bukkit.getServer().dispatchCommand(sender, commandLabel + " addonmanager");
				}

				if (addonmanagerCommand.equals("declineterms")) {
					PowerRanks.getConfigManager().setBool("addon_manager.accepted_terms", false);
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
