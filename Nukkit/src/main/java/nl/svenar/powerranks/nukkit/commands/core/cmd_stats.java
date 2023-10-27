package nl.svenar.powerranks.nukkit.commands.core;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.TimeZone;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;

public class cmd_stats extends PowerCommand {

	public cmd_stats(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));

		Instant current_time = Instant.now();

		sender.sendMessage(TextFormat.DARK_AQUA + "--------" + TextFormat.DARK_BLUE + plugin.getDescription().getName()
				+ TextFormat.DARK_AQUA + "--------");
		sender.sendMessage(TextFormat.GREEN + "Server version: " + TextFormat.DARK_GREEN + plugin.getServer().getName()
				+ " " + plugin.getServer().getVersion() + " | "
				+ plugin.getServer().getNukkitVersion());
		sender.sendMessage(
				TextFormat.GREEN + "Java version: " + TextFormat.DARK_GREEN + System.getProperty("java.version"));
		sender.sendMessage(TextFormat.GREEN + "Storage method: " + TextFormat.DARK_GREEN
				+ plugin.getConfigManager().getString("storage.type", "yaml").toUpperCase());
		sender.sendMessage(TextFormat.GREEN + "Uptime: " + TextFormat.DARK_GREEN
				+ format.format(Duration.between(plugin.pluginStartTime, current_time).toMillis()));
		sender.sendMessage(
				TextFormat.GREEN + "PowerRanks Version: " + TextFormat.DARK_GREEN
						+ plugin.getDescription().getVersion());
		sender.sendMessage(TextFormat.GREEN + "Registered ranks: " + TextFormat.DARK_GREEN + PRCache.getRanks().size());
		sender.sendMessage(
				TextFormat.GREEN + "Registered players: " + TextFormat.DARK_GREEN + PRCache.getPlayers().size());

		boolean hex_color_supported = isHEXColorsSupported();

		sender.sendMessage(TextFormat.GREEN + "RGB colors: "
				+ (hex_color_supported ? TextFormat.DARK_GREEN + "" : TextFormat.DARK_RED + "un") + "supported");

		sender.sendMessage(TextFormat.GREEN + "Plugin hooks:");
		sender.sendMessage(TextFormat.GREEN + "- None // TODO");

		// Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
		// String pluginNames = "";
		// for (Plugin plugin : plugins) {
		// pluginNames += plugin.getName() + "(" + plugin.getDescription().getVersion()
		// + "), ";
		// }
		// pluginNames = pluginNames.substring(0, pluginNames.length() - 2);

		// sender.sendMessage(TextFormat.GREEN + "Plugins (" + plugins.length + "): " +
		// TextFormat.DARK_GREEN + pluginNames);
		sender.sendMessage(TextFormat.DARK_AQUA + "--------------------------");

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}

	private boolean isHEXColorsSupported() {
		try {
			Class.forName("net.md_5.bungee.api.TextFormat");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
