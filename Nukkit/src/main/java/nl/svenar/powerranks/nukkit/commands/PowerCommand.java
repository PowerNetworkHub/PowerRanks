package nl.svenar.powerranks.nukkit.commands;

import java.util.ArrayList;

import cn.nukkit.command.CommandSender;
import nl.svenar.powerranks.nukkit.PowerRanks;

public abstract class PowerCommand {
    
    protected PowerRanks plugin;
    
    private String commandPermission = "";
    
    public enum COMMANDEXECUTOR {
        NONE,
        PLAYER,
        CONSOLE,
        COMMANDBLOCK,
        ALL
    }
    
    private COMMANDEXECUTOR ce = COMMANDEXECUTOR.NONE;
    
    public PowerCommand(PowerRanks plugin, String command_name, COMMANDEXECUTOR ce) {
        PowerCommandHandler.addPowerCommand(command_name, this);
        this.plugin = plugin;
        this.ce = ce;
    }
    
    public abstract boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args);
    
    public COMMANDEXECUTOR getCommandExecutor() {
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
