package nl.svenar.powerranks.bukkit.commands;

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

import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.addons.PowerRanksAddon;
import nl.svenar.powerranks.bukkit.addons.PowerRanksPlayer;
import nl.svenar.powerranks.bukkit.commands.PowerCommand.COMMAND_EXECUTOR;
import nl.svenar.powerranks.bukkit.commands.addons.cmd_addoninfo;
import nl.svenar.powerranks.bukkit.commands.addons.cmd_addonmanager;
import nl.svenar.powerranks.bukkit.commands.addons.cmd_listaddons;
import nl.svenar.powerranks.bukkit.commands.buyable.cmd_addbuyablerank;
import nl.svenar.powerranks.bukkit.commands.buyable.cmd_buyrank;
import nl.svenar.powerranks.bukkit.commands.buyable.cmd_delbuyablerank;
import nl.svenar.powerranks.bukkit.commands.buyable.cmd_rankup;
import nl.svenar.powerranks.bukkit.commands.buyable.cmd_setbuycommand;
import nl.svenar.powerranks.bukkit.commands.buyable.cmd_setbuycost;
import nl.svenar.powerranks.bukkit.commands.buyable.cmd_setbuydescription;
import nl.svenar.powerranks.bukkit.commands.core.cmd_config;
import nl.svenar.powerranks.bukkit.commands.core.cmd_dump;
import nl.svenar.powerranks.bukkit.commands.core.cmd_factoryreset;
import nl.svenar.powerranks.bukkit.commands.core.cmd_help;
import nl.svenar.powerranks.bukkit.commands.core.cmd_pluginhook;
import nl.svenar.powerranks.bukkit.commands.core.cmd_reload;
import nl.svenar.powerranks.bukkit.commands.core.cmd_stats;
import nl.svenar.powerranks.bukkit.commands.core.cmd_tablist;
import nl.svenar.powerranks.bukkit.commands.core.cmd_verbose;
import nl.svenar.powerranks.bukkit.commands.player.cmd_addownrank;
import nl.svenar.powerranks.bukkit.commands.player.cmd_addplayerperm;
import nl.svenar.powerranks.bukkit.commands.player.cmd_addrank;
import nl.svenar.powerranks.bukkit.commands.player.cmd_checkrank;
import nl.svenar.powerranks.bukkit.commands.player.cmd_delplayerperm;
import nl.svenar.powerranks.bukkit.commands.player.cmd_delrank;
import nl.svenar.powerranks.bukkit.commands.player.cmd_haspermission;
import nl.svenar.powerranks.bukkit.commands.player.cmd_listplayerpermissions;
import nl.svenar.powerranks.bukkit.commands.player.cmd_nick;
import nl.svenar.powerranks.bukkit.commands.player.cmd_playerinfo;
import nl.svenar.powerranks.bukkit.commands.player.cmd_setownrank;
import nl.svenar.powerranks.bukkit.commands.player.cmd_setrank;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_addinheritance;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_addperm;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_createrank;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_deleterank;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_delinheritance;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_delperm;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_listdefaultranks;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_listpermissions;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_listranks;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_rankinfo;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_renamerank;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_setchatcolor;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_setdefault;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_setnamecolor;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_setprefix;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_setsuffix;
import nl.svenar.powerranks.bukkit.commands.rank.cmd_setweight;
import nl.svenar.powerranks.bukkit.commands.test.cmd_test;
import nl.svenar.powerranks.bukkit.commands.usertags.cmd_addusertag;
import nl.svenar.powerranks.bukkit.commands.usertags.cmd_clearusertag;
import nl.svenar.powerranks.bukkit.commands.usertags.cmd_createusertag;
import nl.svenar.powerranks.bukkit.commands.usertags.cmd_delusertag;
import nl.svenar.powerranks.bukkit.commands.usertags.cmd_editusertag;
import nl.svenar.powerranks.bukkit.commands.usertags.cmd_listusertags;
import nl.svenar.powerranks.bukkit.commands.usertags.cmd_removeusertag;
import nl.svenar.powerranks.bukkit.commands.usertags.cmd_setusertag;
import nl.svenar.powerranks.bukkit.commands.webeditor.cmd_webeditor;

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
		new cmd_tablist(plugin, "tablist", COMMAND_EXECUTOR.ALL);
		new cmd_stats(plugin, "stats", COMMAND_EXECUTOR.ALL);
		new cmd_factoryreset(plugin, "factoryreset", COMMAND_EXECUTOR.ALL);
		new cmd_addoninfo(plugin, "addoninfo", COMMAND_EXECUTOR.ALL);
		new cmd_addonmanager(plugin, "addonmanager", COMMAND_EXECUTOR.ALL);
		new cmd_playerinfo(plugin, "playerinfo", COMMAND_EXECUTOR.ALL);
		new cmd_haspermission(plugin, "haspermission", COMMAND_EXECUTOR.ALL);
		new cmd_dump(plugin, "dump", COMMAND_EXECUTOR.ALL);

		new cmd_setrank(plugin, "setrank", COMMAND_EXECUTOR.ALL);
		new cmd_setownrank(plugin, "setownrank", COMMAND_EXECUTOR.PLAYER);
		new cmd_addrank(plugin, "addrank", COMMAND_EXECUTOR.ALL);
		new cmd_addownrank(plugin, "addownrank", COMMAND_EXECUTOR.PLAYER);
		new cmd_delrank(plugin, "delrank", COMMAND_EXECUTOR.ALL);

		new cmd_listranks(plugin, "listranks", COMMAND_EXECUTOR.ALL);
		new cmd_listpermissions(plugin, "listpermissions", COMMAND_EXECUTOR.ALL);
		new cmd_listplayerpermissions(plugin, "listplayerpermissions", COMMAND_EXECUTOR.ALL);
		new cmd_listaddons(plugin, "listaddons", COMMAND_EXECUTOR.ALL);
		new cmd_listusertags(plugin, "listusertags", COMMAND_EXECUTOR.ALL);

		new cmd_checkrank(plugin, "checkrank", COMMAND_EXECUTOR.ALL);
		new cmd_nick(plugin, "nick", COMMAND_EXECUTOR.ALL);

		new cmd_createrank(plugin, "createrank", COMMAND_EXECUTOR.ALL);
		new cmd_deleterank(plugin, "deleterank", COMMAND_EXECUTOR.ALL);
		new cmd_renamerank(plugin, "renamerank", COMMAND_EXECUTOR.ALL);
		new cmd_setdefault(plugin, "setdefault", COMMAND_EXECUTOR.ALL);
		new cmd_listdefaultranks(plugin, "listdefaultranks", COMMAND_EXECUTOR.ALL);
		new cmd_addperm(plugin, "addperm", COMMAND_EXECUTOR.ALL);
		new cmd_delperm(plugin, "delperm", COMMAND_EXECUTOR.ALL);
		new cmd_setprefix(plugin, "setprefix", COMMAND_EXECUTOR.ALL);
		new cmd_setsuffix(plugin, "setsuffix", COMMAND_EXECUTOR.ALL);
		new cmd_setchatcolor(plugin, "setchatcolor", COMMAND_EXECUTOR.ALL);
		new cmd_setnamecolor(plugin, "setnamecolor", COMMAND_EXECUTOR.ALL);
		new cmd_addinheritance(plugin, "addinheritance", COMMAND_EXECUTOR.ALL);
		new cmd_delinheritance(plugin, "delinheritance", COMMAND_EXECUTOR.ALL);
		new cmd_setweight(plugin, "setweight", COMMAND_EXECUTOR.ALL);
		new cmd_rankinfo(plugin, "rankinfo", COMMAND_EXECUTOR.ALL);

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
		new cmd_addusertag(plugin, "addusertag", COMMAND_EXECUTOR.ALL);
		new cmd_setusertag(plugin, "setusertag", COMMAND_EXECUTOR.ALL);
		new cmd_delusertag(plugin, "delusertag", COMMAND_EXECUTOR.ALL);
		new cmd_clearusertag(plugin, "clearusertag", COMMAND_EXECUTOR.ALL);

		new cmd_webeditor(plugin, "webeditor", COMMAND_EXECUTOR.ALL);
		new cmd_webeditor(plugin, "we", COMMAND_EXECUTOR.ALL);

		new cmd_test(plugin, "test", COMMAND_EXECUTOR.ALL);
	}

	private static boolean canExecuteCommand(CommandSender sender, PowerCommand command_handler) {
		return (sender instanceof Player && (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.PLAYER
				|| command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL))
				|| (sender instanceof ConsoleCommandSender
						&& (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.CONSOLE
								|| command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL))
				|| (sender instanceof BlockCommandSender
						&& (command_handler.getCommandExecutor() == COMMAND_EXECUTOR.COMMANDBLOCK
								|| command_handler.getCommandExecutor() == COMMAND_EXECUTOR.ALL));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA
					+ plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
			sender.sendMessage(ChatColor.GREEN + "/" + commandLabel + " help" + ChatColor.DARK_GREEN + " - For the command list.");
			sender.sendMessage("");
			sender.sendMessage(ChatColor.DARK_GREEN + "Author: " + ChatColor.GREEN + plugin.getDescription().getAuthors().get(0));
			sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + plugin.getDescription().getVersion());
            sender.sendMessage(ChatColor.DARK_GREEN + "Website: " + ChatColor.GREEN + plugin.getDescription().getWebsite());
            sender.sendMessage(ChatColor.DARK_GREEN + "Documentation: " + ChatColor.GREEN + "https://docs.powerranks.nl");
			sender.sendMessage(ChatColor.DARK_GREEN + "Support me: " + ChatColor.YELLOW + "https://ko-fi.com/svenar");
			sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "------------------------------"
					+ ChatColor.BLUE + "===");
		} else {
			String command = args[0];
			PowerCommand command_handler = get_power_command(command);
			if (command_handler != null) {
				boolean is_allowed = canExecuteCommand(sender, command_handler);
				boolean hasPermission = hasPermission(sender, command_handler);
				if (is_allowed) {
					if (hasPermission) {
						return command_handler.onCommand(sender, cmd, commandLabel, command,
								Arrays.copyOfRange(args, 1, args.length));
					} else {
						sender.sendMessage(
								PowerRanks.getLanguageManager().getFormattedMessage("general.no-permission"));
					}
				} else {
					sender.sendMessage(plugin.plp + ChatColor.DARK_RED + "You can't execute this command here");
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

	private boolean hasPermission(CommandSender sender, PowerCommand command_handler) {
		if (command_handler.getCommandPermission().length() == 0) {
			return true;
		}

		return sender.hasPermission(command_handler.getCommandPermission());
	}

	public static PowerCommand get_power_command(String command_name) {
		return power_commands.get(command_name.toLowerCase());
	}

	public static void add_power_command(String command_name, PowerCommand command_handler) {
		power_commands.put(command_name.toLowerCase(), command_handler);
	}

    private static boolean hasCommandPermission(CommandSender sender, String cmd) {
        boolean hasPermission = sender instanceof Player ? ((Player) sender).hasPermission("powerranks.cmd." + cmd.toLowerCase()) : true;
        return hasPermission;
    }

	public static ArrayList<String> handle_tab_complete(CommandSender sender, String cmd, String[] args) {
		ArrayList<String> output = new ArrayList<String>();
		if (args.length == 0) {
			for (Entry<String, PowerCommand> entry : power_commands.entrySet()) {
				if (cmd.length() == 0 || entry.getKey().toLowerCase().contains(cmd.toLowerCase())) {
					boolean is_allowed = canExecuteCommand(sender, entry.getValue()) && hasCommandPermission(sender, cmd);
					if (is_allowed) {
						output.add(entry.getKey());
					}
				}
			}
		} else {
			if (power_commands.containsKey(cmd.toLowerCase())) {
				if (hasCommandPermission(sender, cmd)) {
                    output = power_commands.get(cmd.toLowerCase()).tabCompleteEvent(sender, args);
                }
			}

		}
		return output;
	}
}