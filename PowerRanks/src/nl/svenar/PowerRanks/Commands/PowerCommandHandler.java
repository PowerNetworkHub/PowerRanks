package nl.svenar.PowerRanks.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand.COMMAND_EXECUTOR;
import nl.svenar.PowerRanks.Data.Messages;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;
import nl.svenar.PowerRanks.addons.PowerRanksPlayer;

public class PowerCommandHandler implements CommandExecutor {

	private static HashMap<String, PowerCommand> power_commands = new HashMap<String, PowerCommand>();
	private static HashMap<PowerCommand, ArrayList<PowerSubCommand>> power_sub_commands = new HashMap<PowerCommand, ArrayList<PowerSubCommand>>();

	private PowerRanks plugin;

	public PowerCommandHandler(PowerRanks plugin) {
		this.plugin = plugin;

		new cmd_help(plugin, "help", COMMAND_EXECUTOR.ALL);
		new cmd_reload(plugin, "reload", COMMAND_EXECUTOR.ALL);
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
					if (args.length >= 2 && power_sub_commands.containsKey(command_handler)) {
						String sub_command = args[1];
						PowerSubCommand psc = get_power_sub_command(command_handler, sub_command);
						if (psc == null) {
							return command_handler.onCommand(sender, cmd, commandLabel, Arrays.copyOfRange(args, 1, args.length), true);
						} else {
							psc.onSubCommand(sender, cmd, commandLabel, Arrays.copyOfRange(args, 2, args.length));
						}
					} else {
						return command_handler.onCommand(sender, cmd, commandLabel, Arrays.copyOfRange(args, 1, args.length), false);
					}
				} else {
					sender.sendMessage(plugin.plp + ChatColor.DARK_RED + "Only players can use this command");
				}
			} else {
				boolean addonCommandFound = false;
				PowerRanksPlayer prPlayer = null;
				if (sender instanceof Player) {
					prPlayer = new PowerRanksPlayer(this.plugin, (Player) sender);
				} else {
					prPlayer = new PowerRanksPlayer(this.plugin, "CONSOLE");
				}

				if (prPlayer != null) {
					for (Entry<File, PowerRanksAddon> prAddon : this.plugin.addonsManager.addonClasses.entrySet()) {
						if (prAddon.getValue().onPowerRanksCommand(prPlayer, true, args[0], args)) {
							addonCommandFound = true;
						}
					}
				}
				if (!addonCommandFound)
					Messages.unknownCommand(sender);
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
	
	public static PowerSubCommand get_power_sub_command(PowerCommand command_handler, String sub_command_name) {
		for (PowerSubCommand psc : power_sub_commands.get(command_handler)) {
			if (psc.commandName().equalsIgnoreCase(sub_command_name)) {
				return psc;
			}
		}
		return null;
	}
}
