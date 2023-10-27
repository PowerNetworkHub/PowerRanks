package nl.svenar.powerranks.nukkit.commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand.COMMAND_EXECUTOR;

public abstract class PowerSubCommand {
	
	protected PowerRanks plugin;
	
	private String commandName;
	private COMMAND_EXECUTOR commandExecutor = COMMAND_EXECUTOR.NONE;
	
	public PowerSubCommand(PowerRanks plugin, String commandName, COMMAND_EXECUTOR commandExecutor) {
		this.plugin = plugin;
		this.commandExecutor = commandExecutor;
		this.commandName = commandName;
	}
	
	protected boolean verifyPermission(CommandSender sender, String permission_node) {
		return sender instanceof ConsoleCommandSender || sender.hasPermission(permission_node);
	}
	
	public abstract boolean onSubCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);
	
	public COMMAND_EXECUTOR getCommandExecutor() {
		return this.commandExecutor;
	}

	public String commandName() {
		return commandName;
	}
}
