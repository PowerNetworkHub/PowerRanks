package nl.svenar.powerranks.nukkit.commands.core;

import java.util.ArrayList;


import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.Player;

import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;

public class cmd_factoryreset extends PowerCommand {

	private String factoryresetid;

	public cmd_factoryreset(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
		if (args.length == 0 || (args.length == 1 && factoryresetid == null)
				|| (args.length == 1 && !args[0].equalsIgnoreCase(factoryresetid))) {
			factoryresetid = (100 + Math.round(Math.random() * 900)) + "-"
					+ (100 + Math.round(Math.random() * 900)) + "-" + (100 + Math.round(Math.random() * 900));

			sender.sendMessage(
					TextFormat.DARK_AQUA + "--------" + TextFormat.DARK_BLUE + plugin.getDescription().getName()
							+ TextFormat.DARK_AQUA + "--------");
			sender.sendMessage(TextFormat.DARK_RED + "WARNING!");
			sender.sendMessage(TextFormat.RED + "This action is irreversible if you continue");
			sender.sendMessage(TextFormat.RED + "Factory reset ID: " + TextFormat.GOLD + factoryresetid);
			sender.sendMessage(
					TextFormat.RED + "To continue do: " + TextFormat.GOLD + "/pr factoryreset "
							+ factoryresetid);
			sender.sendMessage(TextFormat.DARK_AQUA + "--------------------------");
		} else if (args.length == 1) {
			this.plugin.factoryReset(sender);

			sender.sendMessage(TextFormat.DARK_AQUA + "--------" + TextFormat.DARK_BLUE + plugin.getDescription().getName()
					+ TextFormat.DARK_AQUA + "--------");
			sender.sendMessage(TextFormat.GREEN + "Factory reset complete!");
			sender.sendMessage(TextFormat.GREEN + "It is recommended to restart your server.");
			sender.sendMessage(TextFormat.DARK_AQUA + "--------------------------");
		} else {
			sender.sendMessage(
					plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}
}
