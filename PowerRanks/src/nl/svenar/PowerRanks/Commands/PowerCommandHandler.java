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

	private PowerRanks plugin;

	public PowerCommandHandler(PowerRanks plugin) {
		this.plugin = plugin;

		new cmd_help(plugin, "help", COMMAND_EXECUTOR.ALL);
		new cmd_reload(plugin, "reload", COMMAND_EXECUTOR.ALL);
		new cmd_verbose(plugin, "verbose", COMMAND_EXECUTOR.ALL);
		new cmd_pluginhook(plugin, "pluginhook", COMMAND_EXECUTOR.ALL);
		new cmd_config(plugin, "config", COMMAND_EXECUTOR.ALL);
		new cmd_stats(plugin, "stats", COMMAND_EXECUTOR.ALL);
		new cmd_factoryreset(plugin, "factoryreset", COMMAND_EXECUTOR.ALL);
		new cmd_addoninfo(plugin, "addoninfo", COMMAND_EXECUTOR.ALL);
		new cmd_addonmanager(plugin, "addonmanager", COMMAND_EXECUTOR.ALL);
		new cmd_playerinfo(plugin, "playerinfo", COMMAND_EXECUTOR.ALL);

		new cmd_setrank(plugin, "setrank", COMMAND_EXECUTOR.ALL);
		new cmd_setownrank(plugin, "setownrank", COMMAND_EXECUTOR.PLAYER);

		new cmd_listranks(plugin, "listranks", COMMAND_EXECUTOR.ALL);
		new cmd_listsubranks(plugin, "listsubranks", COMMAND_EXECUTOR.ALL);
		new cmd_listpermissions(plugin, "listpermissions", COMMAND_EXECUTOR.ALL);
		new cmd_listplayerpermissions(plugin, "listplayerpermissions", COMMAND_EXECUTOR.ALL);
		new cmd_listaddons(plugin, "listaddons", COMMAND_EXECUTOR.ALL);
		new cmd_listusertags(plugin, "listusertags", COMMAND_EXECUTOR.ALL);

		new cmd_checkrank(plugin, "checkrank", COMMAND_EXECUTOR.ALL);

		new cmd_addsubrank(plugin, "addsubrank", COMMAND_EXECUTOR.ALL);
		new cmd_delsubrank(plugin, "delsubrank", COMMAND_EXECUTOR.ALL);
		new cmd_enablesubrankprefix(plugin, "enablesubrankprefix", COMMAND_EXECUTOR.ALL);
		new cmd_disablesubrankprefix(plugin, "disablesubrankprefix", COMMAND_EXECUTOR.ALL);
		new cmd_enablesubranksuffix(plugin, "enablesubranksuffix", COMMAND_EXECUTOR.ALL);
		new cmd_disablesubranksuffix(plugin, "disablesubranksuffix", COMMAND_EXECUTOR.ALL);
		new cmd_enablesubrankpermissions(plugin, "enablesubrankpermissions", COMMAND_EXECUTOR.ALL);
		new cmd_disablesubrankpermissions(plugin, "disablesubrankpermissions", COMMAND_EXECUTOR.ALL);
		new cmd_addsubrankworld(plugin, "addsubrankworld", COMMAND_EXECUTOR.ALL);
		new cmd_delsubrankworld(plugin, "delsubrankworld", COMMAND_EXECUTOR.ALL);

		new cmd_createrank(plugin, "createrank", COMMAND_EXECUTOR.ALL);
		new cmd_deleterank(plugin, "deleterank", COMMAND_EXECUTOR.ALL);
		new cmd_renamerank(plugin, "renamerank", COMMAND_EXECUTOR.ALL);
		new cmd_setdefaultrank(plugin, "setdefaultrank", COMMAND_EXECUTOR.ALL);
		new cmd_addperm(plugin, "addperm", COMMAND_EXECUTOR.ALL);
		new cmd_delperm(plugin, "delperm", COMMAND_EXECUTOR.ALL);
		new cmd_setprefix(plugin, "setprefix", COMMAND_EXECUTOR.ALL);
		new cmd_setsuffix(plugin, "setsuffix", COMMAND_EXECUTOR.ALL);
		new cmd_setchatcolor(plugin, "setchatcolor", COMMAND_EXECUTOR.ALL);
		new cmd_setnamecolor(plugin, "setnamecolor", COMMAND_EXECUTOR.ALL);
		new cmd_addinheritance(plugin, "addinheritance", COMMAND_EXECUTOR.ALL);
		new cmd_delinheritance(plugin, "delinheritance", COMMAND_EXECUTOR.ALL);

		new cmd_promote(plugin, "promote", COMMAND_EXECUTOR.ALL);
		new cmd_demote(plugin, "demote", COMMAND_EXECUTOR.ALL);
		new cmd_setpromoterank(plugin, "setpromoterank", COMMAND_EXECUTOR.ALL);
		new cmd_setdemoterank(plugin, "setdemoterank", COMMAND_EXECUTOR.ALL);
		new cmd_clearpromoterank(plugin, "clearpromoterank", COMMAND_EXECUTOR.ALL);
		new cmd_cleardemoterank(plugin, "cleardemoterank", COMMAND_EXECUTOR.ALL);

		new cmd_gui(plugin, "gui", COMMAND_EXECUTOR.PLAYER);
		new cmd_setguiicon(plugin, "setguiicon", COMMAND_EXECUTOR.PLAYER);
		new cmd_buyrank(plugin, "buyrank", COMMAND_EXECUTOR.PLAYER);
		new cmd_rankup(plugin, "rankup", COMMAND_EXECUTOR.PLAYER);
		new cmd_addbuyablerank(plugin, "addbuyablerank", COMMAND_EXECUTOR.ALL);
		new cmd_delbuyablerank(plugin, "delbuyablerank", COMMAND_EXECUTOR.ALL);
		new cmd_setbuycost(plugin, "setbuycost", COMMAND_EXECUTOR.ALL);
		new cmd_setbuydescription(plugin, "setbuydescription", COMMAND_EXECUTOR.ALL);
		new cmd_setbuycommand(plugin, "setbuycommand", COMMAND_EXECUTOR.ALL);

		new cmd_addplayerperm(plugin, "addplayerperm", COMMAND_EXECUTOR.ALL);
		new cmd_delplayerperm(plugin, "delplayerperm", COMMAND_EXECUTOR.ALL);
		new cmd_createusertag(plugin, "createusertag", COMMAND_EXECUTOR.ALL);
		new cmd_editusertag(plugin, "editusertag", COMMAND_EXECUTOR.ALL);
		new cmd_removeusertag(plugin, "removeusertag", COMMAND_EXECUTOR.ALL);
		new cmd_setusertag(plugin, "setusertag", COMMAND_EXECUTOR.ALL);
		new cmd_clearusertag(plugin, "clearusertag", COMMAND_EXECUTOR.ALL);

	}

	private static boolean canExecuteCommand(CommandSender sender, PowerCommand command_handler) {
		return (sender instanceof Player && (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.PLAYER || command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL))
			|| (sender instanceof ConsoleCommandSender && (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.CONSOLE || command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL))
			|| (sender instanceof BlockCommandSender && (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.COMMANDBLOCK || command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
			sender.sendMessage(ChatColor.GREEN + "/" + commandLabel + " help" + ChatColor.DARK_GREEN + " - For the command list.");
			sender.sendMessage("");
			sender.sendMessage(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + plugin.getDescription().getAuthors().get(0));
			sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + plugin.getDescription().getVersion());
			sender.sendMessage(ChatColor.DARK_GREEN + "Website: " + ChatColor.GREEN + plugin.getDescription().getWebsite());
			sender.sendMessage(ChatColor.DARK_GREEN + "Support me: " + ChatColor.YELLOW + "https://ko-fi.com/svenar");
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------" + ChatColor.BLUE + "===");
		} else {
			String command = args[0];
			PowerCommand command_handler = get_power_command(command);
			if (command_handler != null) {
				boolean is_allowed = canExecuteCommand(sender, command_handler);
				if (is_allowed) {
					return command_handler.onCommand(sender, cmd, commandLabel, Arrays.copyOfRange(args, 1, args.length));
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
						if (prAddon.getValue().onPowerRanksCommand(prPlayer, sender instanceof Player, args[0], args)) {
							addonCommandFound = true;
						}
					}
				}
				if (!addonCommandFound)
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
	
	public static ArrayList<String> handle_tab_complete(CommandSender sender, String cmd, String[] args) {
		ArrayList<String> output = new ArrayList<String>();
		if (args.length == 0) {
			for (Entry<String, PowerCommand> entry : power_commands.entrySet()) {
				if (cmd.length() == 0 || entry.getKey().toLowerCase().contains(cmd.toLowerCase())) {
					boolean is_allowed = canExecuteCommand(sender, entry.getValue());
					if (is_allowed) {
						output.add(entry.getKey());
					}
				}
			}
		} else {
			if (power_commands.containsKey(cmd.toLowerCase())) {
				output = power_commands.get(cmd.toLowerCase()).tabCompleteEvent(sender, args);
			}
			
		}
		return output;
	}
}