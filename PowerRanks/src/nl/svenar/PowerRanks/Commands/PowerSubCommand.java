package nl.svenar.PowerRanks.Commands;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Commands.PowerCommand.COMMAND_EXECUTOR;

public abstract class PowerSubCommand {
	
	protected PowerRanks plugin;
	
	private COMMAND_EXECUTOR ce = COMMAND_EXECUTOR.NONE;
	private String command_name;
	
	public PowerSubCommand(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
		this.plugin = plugin;
		this.ce = ce;
		this.command_name = command_name;
	}
	
	protected boolean verifyPermission(CommandSender sender, String permission_node) {
		return sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender || sender.hasPermission(permission_node);
	}
	
	public abstract boolean onSubCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);
	
	public COMMAND_EXECUTOR getCommandExecutor() {
		return this.ce;
	}

	public String commandName() {
		return command_name;
	}
}
