package nl.svenar.PowerRanks.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.PowerRanks.PowerRanks;

public abstract class PowerCommand {
	
	protected PowerRanks plugin;
	
	enum COMMAND_EXECUTOR {
		NONE,
		PLAYER,
		CONSOLE,
		COMMANDBLOCK,
		ALL
	}
	
	private COMMAND_EXECUTOR ce = COMMAND_EXECUTOR.NONE;
	
	public PowerCommand(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		PowerCommandHandler.add_power_command(command_name, this);
		this.plugin = plugin;
		this.ce = ce;
	}
	
	public abstract boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);
	
	public COMMAND_EXECUTOR getCommandExecutor() {
		return this.ce;
	}
}
