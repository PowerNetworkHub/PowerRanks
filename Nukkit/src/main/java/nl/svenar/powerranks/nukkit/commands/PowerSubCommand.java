package nl.svenar.powerranks.nukkit.commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand.COMMANDEXECUTOR;

public abstract class PowerSubCommand {
    
    protected PowerRanks plugin;
    
    private String commandName;
    
    private COMMANDEXECUTOR commandExecutor = COMMANDEXECUTOR.NONE;
    
    public PowerSubCommand(PowerRanks plugin, String commandName, COMMANDEXECUTOR commandExecutor) {
        this.plugin = plugin;
        this.commandExecutor = commandExecutor;
        this.commandName = commandName;
    }
    
    protected boolean verifyPermission(CommandSender sender, String permission_node) {
        return sender instanceof ConsoleCommandSender || sender.hasPermission(permission_node);
    }
    
    public abstract boolean onSubCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);
    
    public COMMANDEXECUTOR getCommandExecutor() {
        return this.commandExecutor;
    }

    public String commandName() {
        return commandName;
    }
}
