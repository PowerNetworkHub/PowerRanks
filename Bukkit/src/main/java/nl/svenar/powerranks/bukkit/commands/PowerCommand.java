package nl.svenar.powerranks.bukkit.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PRUtil;

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

	protected String prepareMessage(String langArg, boolean addPluginPrefix) {
        return prepareMessage(langArg, ImmutableMap.of(), addPluginPrefix);
    }

    protected String prepareMessage(String langArg, ImmutableMap<String, @NotNull String> data, boolean addPluginPrefix) {
        String langLine = PowerRanks.getLanguageManager().getFormattedMessage(langArg, false);
        if (addPluginPrefix) {
            String langPrefix = PowerRanks.getLanguageManager().getFormattedMessage("prefix", false);
            return PRUtil.powerFormatter(langPrefix + " " + langLine, data, '[', ']');
        } else {
            return PRUtil.powerFormatter(langLine, data, '[', ']');
        }
    }
}
