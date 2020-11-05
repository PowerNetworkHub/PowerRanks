package nl.svenar.PowerRanks.Commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand.COMMAND_EXECUTOR;

public class PowerCommandHandler implements CommandExecutor {

	private static HashMap<String, PowerCommand> power_commands = new HashMap<String, PowerCommand>();

	private PowerRanks plugin;

	public PowerCommandHandler(PowerRanks plugin) {
		this.plugin = plugin;

		new cmd_help(plugin, "help", COMMAND_EXECUTOR.ALL);
//		new cmd_create(plugin, "create");
//		new cmd_remove(plugin, "remove");
//		new cmd_addpoint(plugin, "addpoint");
//		new cmd_delpoint(plugin, "delpoint");
//		new cmd_select(plugin, "select");
//		new cmd_preview(plugin, "preview");
//		new cmd_info(plugin, "info");
//		new cmd_setduration(plugin, "setduration");
//		new cmd_start(plugin, "start");
//		new cmd_stats(plugin, "stats");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + PowerRanks.pdf.getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
			sender.sendMessage(ChatColor.GREEN + "/" + commandLabel + " help" + ChatColor.DARK_GREEN + " - For the command list.");
			sender.sendMessage("");
			sender.sendMessage(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + PowerRanks.pdf.getAuthors().get(0));
			sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + PowerRanks.pdf.getVersion());
			sender.sendMessage(ChatColor.DARK_GREEN + "Website: " + ChatColor.GREEN + PowerRanks.pdf.getWebsite());
			sender.sendMessage(ChatColor.DARK_GREEN + "Support me: " + ChatColor.YELLOW + "https://ko-fi.com/svenar");
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "-------------------------------" + ChatColor.BLUE + "===");
		} else {
			String command = args[0];
			PowerCommand command_handler = get_power_command(command);
			if (command_handler != null) {
				boolean is_allowed = (sender instanceof Player && (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.PLAYER || command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL))
						|| (sender instanceof ConsoleCommandSender && (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.CONSOLE || command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL))
						|| (sender instanceof BlockCommandSender && (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.COMMANDBLOCK || command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL));
				if (is_allowed) {
					return command_handler.onCommand(sender, cmd, commandLabel, Arrays.copyOfRange(args, 1, args.length));
				} else {
					sender.sendMessage(plugin.plp + ChatColor.DARK_RED + "Only players can use this command");
				}
			} else {
				sender.sendMessage(plugin.plp + ChatColor.DARK_RED + "Unknown Command");
			}

		}
		return false;
	}

	public static PowerCommand get_power_command(String command_name) {
		return power_commands.get(command_name.toLowerCase());
	}

	public static void add_power_command(String command_name, PowerCommand command_handler) {
		power_commands.put(command_name.toLowerCase(), command_handler);
	}
}
