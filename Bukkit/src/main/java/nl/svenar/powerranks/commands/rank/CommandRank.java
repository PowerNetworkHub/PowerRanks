package nl.svenar.powerranks.commands.rank;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.commands.rank.permissions.CommandRankPermissions;

public class CommandRank extends PowerCommand {

    public CommandRank(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);

        addSubPowerCommand("create", new CommandRankCreate(plugin, ce, true));
        addSubPowerCommand("add", new CommandRankCreate(plugin, ce, false));
        addSubPowerCommand("new", new CommandRankCreate(plugin, ce, false));

        addSubPowerCommand("delete", new CommandRankDelete(plugin, ce, true));
        addSubPowerCommand("remove", new CommandRankDelete(plugin, ce, false));
        addSubPowerCommand("del", new CommandRankDelete(plugin, ce, false));

        addSubPowerCommand("setweight", new CommandRankSetWeight(plugin, ce, true));
        addSubPowerCommand("setprefix", new CommandRankSetPrefix(plugin, ce, true));
        addSubPowerCommand("setsuffix", new CommandRankSetSuffix(plugin, ce, true));

        addSubPowerCommand("permissions", new CommandRankPermissions(plugin, ce, true));
        addSubPowerCommand("p", new CommandRankPermissions(plugin, ce, false));

        addSubPowerCommand("list", new CommandRankList(plugin, ce, true));
    }

    @Override
    public String getArgumentSuggestions() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Base ranks command";
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