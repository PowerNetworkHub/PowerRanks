package nl.svenar.PowerRanks.commands;

import net.md_5.bungee.api.CommandSender;

public abstract class PowerRanksCommand {
	
	public PowerRanksCommand(String command_name) {
		Commands.add_powerranks_command(command_name, this);
	}
	
	public abstract void onCommand(CommandSender sender, String[] args);
}
