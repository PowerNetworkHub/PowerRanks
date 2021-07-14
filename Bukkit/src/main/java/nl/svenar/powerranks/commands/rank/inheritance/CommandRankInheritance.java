package nl.svenar.powerranks.commands.rank.inheritance;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;

public class CommandRankInheritance extends PowerCommand {

    public CommandRankInheritance(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);

        addSubPowerCommand("list", new CommandRankInheritanceList(plugin, ce, true));

        addSubPowerCommand("add", new CommandRankInheritanceAdd(plugin, ce, true));

        addSubPowerCommand("remove", new CommandRankInheritanceRemove(plugin, ce, true));
        addSubPowerCommand("delete", new CommandRankInheritanceRemove(plugin, ce, false));
        addSubPowerCommand("del", new CommandRankInheritanceRemove(plugin, ce, false));
    }

    @Override
    public String getArgumentSuggestions() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Base rank inheritance command";
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