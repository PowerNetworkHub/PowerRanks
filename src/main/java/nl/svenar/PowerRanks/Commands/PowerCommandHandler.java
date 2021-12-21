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
import nl.svenar.PowerRanks.Commands.addons.cmd_addoninfo;
import nl.svenar.PowerRanks.Commands.addons.cmd_addonmanager;
import nl.svenar.PowerRanks.Commands.addons.cmd_listaddons;
import nl.svenar.PowerRanks.Commands.buyable.cmd_addbuyablerank;
import nl.svenar.PowerRanks.Commands.buyable.cmd_buyrank;
import nl.svenar.PowerRanks.Commands.buyable.cmd_delbuyablerank;
import nl.svenar.PowerRanks.Commands.buyable.cmd_rankup;
import nl.svenar.PowerRanks.Commands.buyable.cmd_setbuycommand;
import nl.svenar.PowerRanks.Commands.buyable.cmd_setbuycost;
import nl.svenar.PowerRanks.Commands.buyable.cmd_setbuydescription;
import nl.svenar.PowerRanks.Commands.core.cmd_config;
import nl.svenar.PowerRanks.Commands.core.cmd_factoryreset;
import nl.svenar.PowerRanks.Commands.core.cmd_help;
import nl.svenar.PowerRanks.Commands.core.cmd_pluginhook;
import nl.svenar.PowerRanks.Commands.core.cmd_reload;
import nl.svenar.PowerRanks.Commands.core.cmd_stats;
import nl.svenar.PowerRanks.Commands.core.cmd_verbose;
import nl.svenar.PowerRanks.Commands.player.cmd_addplayerperm;
import nl.svenar.PowerRanks.Commands.player.cmd_checkrank;
import nl.svenar.PowerRanks.Commands.player.cmd_delplayerperm;
import nl.svenar.PowerRanks.Commands.player.cmd_demote;
import nl.svenar.PowerRanks.Commands.player.cmd_listplayerpermissions;
import nl.svenar.PowerRanks.Commands.player.cmd_playerinfo;
import nl.svenar.PowerRanks.Commands.player.cmd_promote;
import nl.svenar.PowerRanks.Commands.player.cmd_setownrank;
import nl.svenar.PowerRanks.Commands.player.cmd_setrank;
import nl.svenar.PowerRanks.Commands.rank.cmd_addinheritance;
import nl.svenar.PowerRanks.Commands.rank.cmd_addperm;
import nl.svenar.PowerRanks.Commands.rank.cmd_cleardemoterank;
import nl.svenar.PowerRanks.Commands.rank.cmd_clearpromoterank;
import nl.svenar.PowerRanks.Commands.rank.cmd_createrank;
import nl.svenar.PowerRanks.Commands.rank.cmd_deleterank;
import nl.svenar.PowerRanks.Commands.rank.cmd_delinheritance;
import nl.svenar.PowerRanks.Commands.rank.cmd_delperm;
import nl.svenar.PowerRanks.Commands.rank.cmd_listpermissions;
import nl.svenar.PowerRanks.Commands.rank.cmd_listranks;
import nl.svenar.PowerRanks.Commands.rank.cmd_renamerank;
import nl.svenar.PowerRanks.Commands.rank.cmd_setchatcolor;
import nl.svenar.PowerRanks.Commands.rank.cmd_setdefaultrank;
import nl.svenar.PowerRanks.Commands.rank.cmd_setdemoterank;
import nl.svenar.PowerRanks.Commands.rank.cmd_setnamecolor;
import nl.svenar.PowerRanks.Commands.rank.cmd_setprefix;
import nl.svenar.PowerRanks.Commands.rank.cmd_setpromoterank;
import nl.svenar.PowerRanks.Commands.rank.cmd_setsuffix;
import nl.svenar.PowerRanks.Commands.rank.cmd_setweight;
import nl.svenar.PowerRanks.Commands.subranks.cmd_addsubrank;
import nl.svenar.PowerRanks.Commands.subranks.cmd_addsubrankworld;
import nl.svenar.PowerRanks.Commands.subranks.cmd_delsubrank;
import nl.svenar.PowerRanks.Commands.subranks.cmd_delsubrankworld;
import nl.svenar.PowerRanks.Commands.subranks.cmd_disablesubrankpermissions;
import nl.svenar.PowerRanks.Commands.subranks.cmd_disablesubrankprefix;
import nl.svenar.PowerRanks.Commands.subranks.cmd_disablesubranksuffix;
import nl.svenar.PowerRanks.Commands.subranks.cmd_enablesubrankpermissions;
import nl.svenar.PowerRanks.Commands.subranks.cmd_enablesubrankprefix;
import nl.svenar.PowerRanks.Commands.subranks.cmd_enablesubranksuffix;
import nl.svenar.PowerRanks.Commands.subranks.cmd_listsubranks;
import nl.svenar.PowerRanks.Commands.usertags.cmd_clearusertag;
import nl.svenar.PowerRanks.Commands.usertags.cmd_createusertag;
import nl.svenar.PowerRanks.Commands.usertags.cmd_editusertag;
import nl.svenar.PowerRanks.Commands.usertags.cmd_listusertags;
import nl.svenar.PowerRanks.Commands.usertags.cmd_removeusertag;
import nl.svenar.PowerRanks.Commands.usertags.cmd_setusertag;
import nl.svenar.PowerRanks.Commands.webeditor.cmd_webeditor;
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
		new cmd_setweight(plugin, "setweight", COMMAND_EXECUTOR.ALL);

		new cmd_promote(plugin, "promote", COMMAND_EXECUTOR.ALL);
		new cmd_demote(plugin, "demote", COMMAND_EXECUTOR.ALL);
		new cmd_setpromoterank(plugin, "setpromoterank", COMMAND_EXECUTOR.ALL);
		new cmd_setdemoterank(plugin, "setdemoterank", COMMAND_EXECUTOR.ALL);
		new cmd_clearpromoterank(plugin, "clearpromoterank", COMMAND_EXECUTOR.ALL);
		new cmd_cleardemoterank(plugin, "cleardemoterank", COMMAND_EXECUTOR.ALL);

		// new cmd_gui(plugin, "gui", COMMAND_EXECUTOR.PLAYER);
		// new cmd_setguiicon(plugin, "setguiicon", COMMAND_EXECUTOR.PLAYER);
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

		new cmd_webeditor(plugin, "webeditor", COMMAND_EXECUTOR.ALL);
		new cmd_webeditor(plugin, "we", COMMAND_EXECUTOR.ALL);

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