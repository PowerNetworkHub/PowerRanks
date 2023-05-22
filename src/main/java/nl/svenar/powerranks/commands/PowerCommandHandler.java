package nl.svenar.powerranks.commands;

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

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.addons.PowerRanksAddon;
import nl.svenar.powerranks.addons.PowerRanksPlayer;
import nl.svenar.powerranks.commands.PowerCommand.COMMAND_EXECUTOR;
import nl.svenar.powerranks.commands.addons.CmdAddoninfo;
import nl.svenar.powerranks.commands.addons.CmdAddonmanager;
import nl.svenar.powerranks.commands.addons.CmdListaddons;
import nl.svenar.powerranks.commands.buyable.CmdAddbuyablerank;
import nl.svenar.powerranks.commands.buyable.CmdBuyrank;
import nl.svenar.powerranks.commands.buyable.CmdDelbuyablerank;
import nl.svenar.powerranks.commands.buyable.CmdRankup;
import nl.svenar.powerranks.commands.buyable.CmdSetbuycommand;
import nl.svenar.powerranks.commands.buyable.CmdSetbuycost;
import nl.svenar.powerranks.commands.buyable.CmdSetbuydescription;
import nl.svenar.powerranks.commands.core.CmdConfig;
import nl.svenar.powerranks.commands.core.CmdDump;
import nl.svenar.powerranks.commands.core.CmdFactoryreset;
import nl.svenar.powerranks.commands.core.CmdHelp;
import nl.svenar.powerranks.commands.core.CmdPluginhook;
import nl.svenar.powerranks.commands.core.CmdReload;
import nl.svenar.powerranks.commands.core.CmdStats;
import nl.svenar.powerranks.commands.core.CmdTablist;
import nl.svenar.powerranks.commands.core.CmdVerbose;
import nl.svenar.powerranks.commands.player.CmdAddownrank;
import nl.svenar.powerranks.commands.player.CmdAddplayerperm;
import nl.svenar.powerranks.commands.player.CmdAddrank;
import nl.svenar.powerranks.commands.player.CmdCheckrank;
import nl.svenar.powerranks.commands.player.CmdDelplayerperm;
import nl.svenar.powerranks.commands.player.CmdDelrank;
import nl.svenar.powerranks.commands.player.CmdHaspermission;
import nl.svenar.powerranks.commands.player.CmdListplayerpermissions;
import nl.svenar.powerranks.commands.player.CmdPlayerinfo;
import nl.svenar.powerranks.commands.player.CmdSetownrank;
import nl.svenar.powerranks.commands.player.CmdSetrank;
import nl.svenar.powerranks.commands.rank.CmdAddinheritance;
import nl.svenar.powerranks.commands.rank.CmdAddperm;
import nl.svenar.powerranks.commands.rank.CmdCreaterank;
import nl.svenar.powerranks.commands.rank.CmdDeleterank;
import nl.svenar.powerranks.commands.rank.CmdDelinheritance;
import nl.svenar.powerranks.commands.rank.CmdDelperm;
import nl.svenar.powerranks.commands.rank.CmdListdefaultranks;
import nl.svenar.powerranks.commands.rank.CmdListpermissions;
import nl.svenar.powerranks.commands.rank.CmdListranks;
import nl.svenar.powerranks.commands.rank.CmdRankinfo;
import nl.svenar.powerranks.commands.rank.CmdRenamerank;
import nl.svenar.powerranks.commands.rank.CmdSetchatcolor;
import nl.svenar.powerranks.commands.rank.CmdSetdefault;
import nl.svenar.powerranks.commands.rank.CmdSetnamecolor;
import nl.svenar.powerranks.commands.rank.CmdSetprefix;
import nl.svenar.powerranks.commands.rank.CmdSetsuffix;
import nl.svenar.powerranks.commands.rank.CmdSetweight;
import nl.svenar.powerranks.commands.usertags.CmdAddusertag;
import nl.svenar.powerranks.commands.usertags.CmdClearusertag;
import nl.svenar.powerranks.commands.usertags.CmdCreateusertag;
import nl.svenar.powerranks.commands.usertags.CmdDelusertag;
import nl.svenar.powerranks.commands.usertags.CmdEditusertag;
import nl.svenar.powerranks.commands.usertags.CmdListusertags;
import nl.svenar.powerranks.commands.usertags.CmdRemoveusertag;
import nl.svenar.powerranks.commands.usertags.CmdSetusertag;
import nl.svenar.powerranks.commands.webeditor.CmdWebeditor;

public class PowerCommandHandler implements CommandExecutor {

	private static HashMap<String, PowerCommand> power_commands = new HashMap<String, PowerCommand>();

	private PowerRanks plugin;

	public PowerCommandHandler(PowerRanks plugin) {
		this.plugin = plugin;

		new CmdHelp(plugin, "help", COMMAND_EXECUTOR.ALL);
		new CmdReload(plugin, "reload", COMMAND_EXECUTOR.ALL);
		new CmdVerbose(plugin, "verbose", COMMAND_EXECUTOR.ALL);
		new CmdPluginhook(plugin, "pluginhook", COMMAND_EXECUTOR.ALL);
		new CmdConfig(plugin, "config", COMMAND_EXECUTOR.ALL);
		new CmdTablist(plugin, "tablist", COMMAND_EXECUTOR.ALL);
		new CmdStats(plugin, "stats", COMMAND_EXECUTOR.ALL);
		new CmdFactoryreset(plugin, "factoryreset", COMMAND_EXECUTOR.ALL);
		new CmdAddoninfo(plugin, "addoninfo", COMMAND_EXECUTOR.ALL);
		new CmdAddonmanager(plugin, "addonmanager", COMMAND_EXECUTOR.ALL);
		new CmdPlayerinfo(plugin, "playerinfo", COMMAND_EXECUTOR.ALL);
		new CmdHaspermission(plugin, "haspermission", COMMAND_EXECUTOR.ALL);
		new CmdDump(plugin, "dump", COMMAND_EXECUTOR.ALL);

		new CmdSetrank(plugin, "setrank", COMMAND_EXECUTOR.ALL);
		new CmdSetownrank(plugin, "setownrank", COMMAND_EXECUTOR.PLAYER);
		new CmdAddrank(plugin, "addrank", COMMAND_EXECUTOR.ALL);
		new CmdAddownrank(plugin, "addownrank", COMMAND_EXECUTOR.PLAYER);
		new CmdDelrank(plugin, "delrank", COMMAND_EXECUTOR.ALL);

		new CmdListranks(plugin, "listranks", COMMAND_EXECUTOR.ALL);
		new CmdListpermissions(plugin, "listpermissions", COMMAND_EXECUTOR.ALL);
		new CmdListplayerpermissions(plugin, "listplayerpermissions", COMMAND_EXECUTOR.ALL);
		new CmdListaddons(plugin, "listaddons", COMMAND_EXECUTOR.ALL);
		new CmdListusertags(plugin, "listusertags", COMMAND_EXECUTOR.ALL);

		new CmdCheckrank(plugin, "checkrank", COMMAND_EXECUTOR.ALL);

		new CmdCreaterank(plugin, "createrank", COMMAND_EXECUTOR.ALL);
		new CmdDeleterank(plugin, "deleterank", COMMAND_EXECUTOR.ALL);
		new CmdRenamerank(plugin, "renamerank", COMMAND_EXECUTOR.ALL);
		new CmdSetdefault(plugin, "setdefault", COMMAND_EXECUTOR.ALL);
		new CmdListdefaultranks(plugin, "listdefaultranks", COMMAND_EXECUTOR.ALL);
		new CmdAddperm(plugin, "addperm", COMMAND_EXECUTOR.ALL);
		new CmdDelperm(plugin, "delperm", COMMAND_EXECUTOR.ALL);
		new CmdSetprefix(plugin, "setprefix", COMMAND_EXECUTOR.ALL);
		new CmdSetsuffix(plugin, "setsuffix", COMMAND_EXECUTOR.ALL);
		new CmdSetchatcolor(plugin, "setchatcolor", COMMAND_EXECUTOR.ALL);
		new CmdSetnamecolor(plugin, "setnamecolor", COMMAND_EXECUTOR.ALL);
		new CmdAddinheritance(plugin, "addinheritance", COMMAND_EXECUTOR.ALL);
		new CmdDelinheritance(plugin, "delinheritance", COMMAND_EXECUTOR.ALL);
		new CmdSetweight(plugin, "setweight", COMMAND_EXECUTOR.ALL);
		new CmdRankinfo(plugin, "rankinfo", COMMAND_EXECUTOR.ALL);

		new CmdBuyrank(plugin, "buyrank", COMMAND_EXECUTOR.PLAYER);
		new CmdRankup(plugin, "rankup", COMMAND_EXECUTOR.PLAYER);
		new CmdAddbuyablerank(plugin, "addbuyablerank", COMMAND_EXECUTOR.ALL);
		new CmdDelbuyablerank(plugin, "delbuyablerank", COMMAND_EXECUTOR.ALL);
		new CmdSetbuycost(plugin, "setbuycost", COMMAND_EXECUTOR.ALL);
		new CmdSetbuydescription(plugin, "setbuydescription", COMMAND_EXECUTOR.ALL);
		new CmdSetbuycommand(plugin, "setbuycommand", COMMAND_EXECUTOR.ALL);

		new CmdAddplayerperm(plugin, "addplayerperm", COMMAND_EXECUTOR.ALL);
		new CmdDelplayerperm(plugin, "delplayerperm", COMMAND_EXECUTOR.ALL);
		new CmdCreateusertag(plugin, "createusertag", COMMAND_EXECUTOR.ALL);
		new CmdEditusertag(plugin, "editusertag", COMMAND_EXECUTOR.ALL);
		new CmdRemoveusertag(plugin, "removeusertag", COMMAND_EXECUTOR.ALL);
		new CmdAddusertag(plugin, "addusertag", COMMAND_EXECUTOR.ALL);
		new CmdSetusertag(plugin, "setusertag", COMMAND_EXECUTOR.ALL);
		new CmdDelusertag(plugin, "delusertag", COMMAND_EXECUTOR.ALL);
		new CmdClearusertag(plugin, "clearusertag", COMMAND_EXECUTOR.ALL);

		new CmdWebeditor(plugin, "webeditor", COMMAND_EXECUTOR.ALL);
		new CmdWebeditor(plugin, "we", COMMAND_EXECUTOR.ALL);

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