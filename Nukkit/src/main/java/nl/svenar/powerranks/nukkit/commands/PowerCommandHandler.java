package nl.svenar.powerranks.nukkit.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand.COMMANDEXECUTOR;
import nl.svenar.powerranks.nukkit.commands.core.CmdConfig;
import nl.svenar.powerranks.nukkit.commands.core.CmdDump;
import nl.svenar.powerranks.nukkit.commands.core.CmdFactoryreset;
import nl.svenar.powerranks.nukkit.commands.core.CmdHelp;
import nl.svenar.powerranks.nukkit.commands.core.CmdPluginhook;
import nl.svenar.powerranks.nukkit.commands.core.CmdReload;
import nl.svenar.powerranks.nukkit.commands.core.CmdStats;
import nl.svenar.powerranks.nukkit.commands.player.CmdAddownrank;
import nl.svenar.powerranks.nukkit.commands.player.CmdAddplayerperm;
import nl.svenar.powerranks.nukkit.commands.player.CmdAddrank;
import nl.svenar.powerranks.nukkit.commands.player.CmdCheckrank;
import nl.svenar.powerranks.nukkit.commands.player.CmdDelplayerperm;
import nl.svenar.powerranks.nukkit.commands.player.CmdDelrank;
import nl.svenar.powerranks.nukkit.commands.player.CmdHaspermission;
import nl.svenar.powerranks.nukkit.commands.player.CmdListplayerpermissions;
import nl.svenar.powerranks.nukkit.commands.player.CmdNick;
import nl.svenar.powerranks.nukkit.commands.player.CmdPlayerinfo;
import nl.svenar.powerranks.nukkit.commands.player.CmdSetownrank;
import nl.svenar.powerranks.nukkit.commands.player.CmdSetrank;
import nl.svenar.powerranks.nukkit.commands.rank.CmdAddinheritance;
import nl.svenar.powerranks.nukkit.commands.rank.CmdAddperm;
import nl.svenar.powerranks.nukkit.commands.rank.CmdCreaterank;
import nl.svenar.powerranks.nukkit.commands.rank.CmdDeleterank;
import nl.svenar.powerranks.nukkit.commands.rank.CmdDelinheritance;
import nl.svenar.powerranks.nukkit.commands.rank.CmdDelperm;
import nl.svenar.powerranks.nukkit.commands.rank.CmdListdefaultranks;
import nl.svenar.powerranks.nukkit.commands.rank.CmdListpermissions;
import nl.svenar.powerranks.nukkit.commands.rank.CmdListranks;
import nl.svenar.powerranks.nukkit.commands.rank.CmdRankinfo;
import nl.svenar.powerranks.nukkit.commands.rank.CmdRenamerank;
import nl.svenar.powerranks.nukkit.commands.rank.CmdSetchatcolor;
import nl.svenar.powerranks.nukkit.commands.rank.CmdSetdefault;
import nl.svenar.powerranks.nukkit.commands.rank.CmdSetnamecolor;
import nl.svenar.powerranks.nukkit.commands.rank.CmdSetprefix;
import nl.svenar.powerranks.nukkit.commands.rank.CmdSetsuffix;
import nl.svenar.powerranks.nukkit.commands.rank.CmdSetweight;
import nl.svenar.powerranks.nukkit.commands.usertags.CmdAddusertag;
import nl.svenar.powerranks.nukkit.commands.usertags.CmdClearusertag;
import nl.svenar.powerranks.nukkit.commands.usertags.CmdCreateusertag;
import nl.svenar.powerranks.nukkit.commands.usertags.CmdDelusertag;
import nl.svenar.powerranks.nukkit.commands.usertags.CmdEditusertag;
import nl.svenar.powerranks.nukkit.commands.usertags.CmdListusertags;
import nl.svenar.powerranks.nukkit.commands.usertags.CmdRemoveusertag;
import nl.svenar.powerranks.nukkit.commands.usertags.CmdSetusertag;
import nl.svenar.powerranks.nukkit.commands.webeditor.CmdWebeditor;

public class PowerCommandHandler extends PluginCommand<PowerRanks> {

	private static HashMap<String, PowerCommand> power_commands = new HashMap<String, PowerCommand>();

	private PowerRanks plugin;

	public PowerCommandHandler(PowerRanks plugin, String commandName) {
		super("powerranks", plugin);
		this.setAliases(new String[] { "pr" });
		this.getCommandParameters().clear();

		this.plugin = plugin;

		this.getCommandParameters().put("help", new CommandParameter[] {
				CommandParameter.newType("page", false, CommandParamType.INT)
		});

		this.getCommandParameters().put("stats", new CommandParameter[] {
		});

		new CmdHelp(plugin, "help", COMMANDEXECUTOR.ALL);
		new CmdReload(plugin, "reload", COMMANDEXECUTOR.ALL);
		new CmdPluginhook(plugin, "pluginhook", COMMANDEXECUTOR.ALL);
		new CmdConfig(plugin, "config", COMMANDEXECUTOR.ALL);
		new CmdStats(plugin, "stats", COMMANDEXECUTOR.ALL);
		new CmdFactoryreset(plugin, "factoryreset", COMMANDEXECUTOR.ALL);
		new CmdPlayerinfo(plugin, "playerinfo", COMMANDEXECUTOR.ALL);
		new CmdHaspermission(plugin, "haspermission", COMMANDEXECUTOR.ALL);
		new CmdDump(plugin, "dump", COMMANDEXECUTOR.ALL);

		new CmdSetrank(plugin, "setrank", COMMANDEXECUTOR.ALL);
		new CmdSetownrank(plugin, "setownrank", COMMANDEXECUTOR.PLAYER);
		new CmdAddrank(plugin, "addrank", COMMANDEXECUTOR.ALL);
		new CmdAddownrank(plugin, "addownrank", COMMANDEXECUTOR.PLAYER);
		new CmdDelrank(plugin, "delrank", COMMANDEXECUTOR.ALL);

		new CmdListranks(plugin, "listranks", COMMANDEXECUTOR.ALL);
		new CmdListpermissions(plugin, "listpermissions", COMMANDEXECUTOR.ALL);
		new CmdListplayerpermissions(plugin, "listplayerpermissions", COMMANDEXECUTOR.ALL);
		new CmdListusertags(plugin, "listusertags", COMMANDEXECUTOR.ALL);

		new CmdCheckrank(plugin, "checkrank", COMMANDEXECUTOR.ALL);
		new CmdNick(plugin, "nick", COMMANDEXECUTOR.ALL);

		new CmdCreaterank(plugin, "createrank", COMMANDEXECUTOR.ALL);
		new CmdDeleterank(plugin, "deleterank", COMMANDEXECUTOR.ALL);
		new CmdRenamerank(plugin, "renamerank", COMMANDEXECUTOR.ALL);
		new CmdSetdefault(plugin, "setdefault", COMMANDEXECUTOR.ALL);
		new CmdListdefaultranks(plugin, "listdefaultranks", COMMANDEXECUTOR.ALL);
		new CmdAddperm(plugin, "addperm", COMMANDEXECUTOR.ALL);
		new CmdDelperm(plugin, "delperm", COMMANDEXECUTOR.ALL);
		new CmdSetprefix(plugin, "setprefix", COMMANDEXECUTOR.ALL);
		new CmdSetsuffix(plugin, "setsuffix", COMMANDEXECUTOR.ALL);
		new CmdSetchatcolor(plugin, "setchatcolor", COMMANDEXECUTOR.ALL);
		new CmdSetnamecolor(plugin, "setnamecolor", COMMANDEXECUTOR.ALL);
		new CmdAddinheritance(plugin, "addinheritance", COMMANDEXECUTOR.ALL);
		new CmdDelinheritance(plugin, "delinheritance", COMMANDEXECUTOR.ALL);
		new CmdSetweight(plugin, "setweight", COMMANDEXECUTOR.ALL);
		new CmdRankinfo(plugin, "rankinfo", COMMANDEXECUTOR.ALL);

		new CmdAddplayerperm(plugin, "addplayerperm", COMMANDEXECUTOR.ALL);
		new CmdDelplayerperm(plugin, "delplayerperm", COMMANDEXECUTOR.ALL);
		new CmdCreateusertag(plugin, "createusertag", COMMANDEXECUTOR.ALL);
		new CmdEditusertag(plugin, "editusertag", COMMANDEXECUTOR.ALL);
		new CmdRemoveusertag(plugin, "removeusertag", COMMANDEXECUTOR.ALL);
		new CmdAddusertag(plugin, "addusertag", COMMANDEXECUTOR.ALL);
		new CmdSetusertag(plugin, "setusertag", COMMANDEXECUTOR.ALL);
		new CmdDelusertag(plugin, "delusertag", COMMANDEXECUTOR.ALL);
		new CmdClearusertag(plugin, "clearusertag", COMMANDEXECUTOR.ALL);

		new CmdWebeditor(plugin, "webeditor", COMMANDEXECUTOR.ALL);
		new CmdWebeditor(plugin, "we", COMMANDEXECUTOR.ALL);
	}

	private static boolean canExecuteCommand(CommandSender sender, PowerCommand command_handler) {
		return (sender instanceof Player && (command_handler.getCommandExecutor() == COMMANDEXECUTOR.PLAYER
				|| command_handler.getCommandExecutor() == COMMANDEXECUTOR.ALL))
				|| (sender instanceof ConsoleCommandSender
						&& (command_handler.getCommandExecutor() == COMMANDEXECUTOR.CONSOLE
								|| command_handler.getCommandExecutor() == COMMANDEXECUTOR.ALL));
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "----------" + TextFormat.AQUA
					+ plugin.getDescription().getName() + TextFormat.DARK_AQUA + "----------" + TextFormat.BLUE
					+ "===");
			sender.sendMessage(TextFormat.GREEN + "/" + commandLabel + " help" + TextFormat.DARK_GREEN
					+ " - For the command list.");
			sender.sendMessage("");
			sender.sendMessage(TextFormat.DARK_GREEN + "Author: " + TextFormat.GREEN
					+ plugin.getDescription().getAuthors().get(0));
			sender.sendMessage(
					TextFormat.DARK_GREEN + "Version: " + TextFormat.GREEN + plugin.getDescription().getVersion());
			sender.sendMessage(
					TextFormat.DARK_GREEN + "Website: " + TextFormat.GREEN + plugin.getDescription().getWebsite());
			sender.sendMessage(
					TextFormat.DARK_GREEN + "Documentation: " + TextFormat.GREEN + "https://docs.powerranks.nl");
			sender.sendMessage(TextFormat.DARK_GREEN + "Support me: " + TextFormat.YELLOW + "https://ko-fi.com/svenar");
			sender.sendMessage(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "------------------------------"
					+ TextFormat.BLUE + "===");
		} else {
			String command = args[0];
			PowerCommand command_handler = getPowerCommand(command);
			if (command_handler != null) {
				boolean is_allowed = canExecuteCommand(sender, command_handler);
				boolean hasPermission = hasPermission(sender, command_handler);
				if (is_allowed) {
					if (hasPermission) {
						return command_handler.onCommand(sender, commandLabel, command,
								Arrays.copyOfRange(args, 1, args.length));
					} else {
						sender.sendMessage(
								plugin.getLanguageManager().getFormattedMessage("general.no-permission"));
					}
				} else {
					sender.sendMessage(
							plugin.getChatPluginPrefix() + TextFormat.DARK_RED + "You can't execute this command here");
				}
				// } else {
				// boolean addonCommandFound = false;
				// PowerRanksPlayer prPlayer = null;
				// if (sender instanceof Player) {
				// prPlayer = new PowerRanksPlayer(this.plugin, (Player) sender);
				// } else {
				// prPlayer = new PowerRanksPlayer(this.plugin, "CONSOLE");
				// }

				// if (prPlayer != null) {
				// for (Entry<File, PowerRanksAddon> prAddon :
				// this.plugin.addonsManager.addonClasses.entrySet()) {
				// if (prAddon.getValue().onPowerRanksCommand(prPlayer, sender instanceof
				// Player, args[0], args)) {
				// addonCommandFound = true;
				// }
				// }
				// }
				// if (!addonCommandFound)
				// sender.sendMessage(plugin.plp + TextFormat.DARK_RED + "Unknown Command");
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

	public static PowerCommand getPowerCommand(String command_name) {
		return power_commands.get(command_name.toLowerCase());
	}

	public static void addPowerCommand(String command_name, PowerCommand command_handler) {
		power_commands.put(command_name.toLowerCase(), command_handler);
	}

	private static boolean hasCommandPermission(CommandSender sender, String cmd) {
		boolean hasPermission = !(sender instanceof Player)
				|| ((Player) sender).hasPermission("powerranks.cmd." + cmd.toLowerCase());
		return hasPermission;
	}

	public static ArrayList<String> handleTabComplete(CommandSender sender, String cmd, String[] args) {
		ArrayList<String> output = new ArrayList<String>();
		if (args.length == 0) {
			for (Entry<String, PowerCommand> entry : power_commands.entrySet()) {
				if (cmd.length() == 0 || entry.getKey().toLowerCase().contains(cmd.toLowerCase())) {
					boolean is_allowed = canExecuteCommand(sender, entry.getValue())
							&& hasCommandPermission(sender, cmd);
					if (is_allowed) {
						output.add(entry.getKey());
					}
				}
			}
		} else {
			if (power_commands.containsKey(cmd.toLowerCase()) && hasCommandPermission(sender, cmd)) {
				output = power_commands.get(cmd.toLowerCase()).tabCompleteEvent(sender, args);
			}

		}
		return output;
	}
}