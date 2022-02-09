package nl.svenar.PowerRanks.Commands.core;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand;
import nl.svenar.PowerRanks.Data.Messages;

public class cmd_factoryreset extends PowerCommand {

	public cmd_factoryreset(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		super(plugin, command_name, ce);
		this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName,
			String[] args) {
		if (args.length == 0) {
			Messages.messageCommandFactoryReset(sender);
		} else if (args.length == 1) {
			if (PowerRanks.factoryresetid == null) {
				Messages.messageCommandFactoryReset(sender);
			} else {
				String resetid = args[0];
				if (resetid.equalsIgnoreCase(PowerRanks.factoryresetid))
					this.plugin.factoryReset(sender);
				else
					Messages.messageCommandFactoryReset(sender);
			}
		} else {
			sender.sendMessage(
					PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
							"commands." + commandName.toLowerCase() + ".arguments"));
		}

		return false;
	}

	public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
		ArrayList<String> tabcomplete = new ArrayList<String>();
		return tabcomplete;
	}
}
