package nl.svenar.powerranks.commands.player.permissions.world;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;

public class CommandPlayerPermissionsWorld extends PowerCommand {

    public CommandPlayerPermissionsWorld(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);

        // addSubPowerCommand("list", new CommandRankPermissionsWorldList(plugin, ce, true));

        // addSubPowerCommand("add", new CommandRankPermissionsWorldAdd(plugin, ce, true));

        // addSubPowerCommand("remove", new CommandRankPermissionsWorldRemove(plugin, ce, true));
        // addSubPowerCommand("delete", new CommandRankPermissionsWorldRemove(plugin, ce, false));
        // addSubPowerCommand("del", new CommandRankPermissionsWorldRemove(plugin, ce, false));
    }

    @Override
    public String getArgumentSuggestions() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Base player permission worlds command";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig().getNode("plugin.commands.insufficient-arguments"));
        return false;
    }

    @Override
    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        for (Entry<String, PowerCommand> subCommand : getSubPowerCommands().entrySet()) {
            list.add(subCommand.getKey());
        }
        return list;
    }
}