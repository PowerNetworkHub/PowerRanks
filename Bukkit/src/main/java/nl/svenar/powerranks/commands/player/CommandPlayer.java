package nl.svenar.powerranks.commands.player;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.commands.player.permissions.CommandPlayerPermissions;
import nl.svenar.powerranks.commands.player.rank.CommandPlayerRank;

public class CommandPlayer extends PowerCommand {

    public CommandPlayer(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);

        addSubPowerCommand("rank", new CommandPlayerRank(plugin, ce, true));
        addSubPowerCommand("r", new CommandPlayerRank(plugin, ce, false));

        addSubPowerCommand("permissions", new CommandPlayerPermissions(plugin, ce, true));
        addSubPowerCommand("p", new CommandPlayerPermissions(plugin, ce, false));
    }

    @Override
    public String getArgumentSuggestions() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Base player command";
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