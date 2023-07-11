package nl.svenar.powerranks.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;

public abstract class PowerCommand {
	
	protected PowerRanks plugin;
	private String commandPermission = "";
	
	public enum COMMAND_EXECUTOR {
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
	
	public abstract boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName, String[] args);
	
	public COMMAND_EXECUTOR getCommandExecutor() {
		return this.ce;
	}
	
	public abstract ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args);

	protected void setCommandPermission(String permission) {
		this.commandPermission = permission;
	}

	public String getCommandPermission() {
		return this.commandPermission;
	}
}
