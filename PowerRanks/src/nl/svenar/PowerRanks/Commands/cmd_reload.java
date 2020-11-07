package nl.svenar.PowerRanks.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.Data.Messages;

public class cmd_reload extends PowerCommand {

	public cmd_reload(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("powerranks.cmd.reload")) {
			if (args.length != 1) {
				Messages.messageCommandUsageReload(sender);
			} else {
				if (args[0].equalsIgnoreCase("config")) {
					
					Messages.messageCommandReloadConfig(sender);
					CachedConfig.update();
					CachedRanks.update();
					CachedPlayers.update();
					this.plugin.updateAllPlayersTABlist();
					Messages.messageCommandReloadConfigDone(sender);
					
				} else if (args[0].equalsIgnoreCase("plugin")) {
					
					Messages.messageCommandReloadPlugin(sender);
					final PluginManager plg = Bukkit.getPluginManager();
					final Plugin plgname = plg.getPlugin(PowerRanks.pdf.getName());
					plg.disablePlugin(plgname);
					plg.enablePlugin(plgname);
					Messages.messageCommandReloadPluginDone(sender);
					
				} else if (args[0].equalsIgnoreCase("all")) {
					
					Messages.messageCommandReloadPlugin(sender);
					final PluginManager plg = Bukkit.getPluginManager();
					final Plugin plgname = plg.getPlugin(PowerRanks.pdf.getName());
					plg.disablePlugin(plgname);
					plg.enablePlugin(plgname);
					Messages.messageCommandReloadPluginDone(sender);
					Messages.messageCommandReloadConfig(sender);
					CachedConfig.update();
					CachedRanks.update();
					CachedPlayers.update();
					this.plugin.updateAllPlayersTABlist();
					Messages.messageCommandReloadConfigDone(sender);
					
				} else {
					
					Messages.messageCommandUsageReload(sender);
				}
			}
		} else {
			if (sender instanceof Player) {
				Messages.noPermission((Player) sender);
			}
		}

		return false;
	}

}
