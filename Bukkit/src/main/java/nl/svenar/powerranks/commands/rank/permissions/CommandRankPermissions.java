package nl.svenar.powerranks.commands.rank.permissions;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.commands.rank.permissions.world.CommandRankPermissionsWorld;

public class CommandRankPermissions extends PowerCommand {

    public CommandRankPermissions(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);

        addSubPowerCommand("list", new CommandRankPermissionsList(plugin, ce, true));

        addSubPowerCommand("add", new CommandRankPermissionsAdd(plugin, ce, true));

        addSubPowerCommand("setallowed", new CommandRankPermissionsSetallowed(plugin, ce, true));

        addSubPowerCommand("remove", new CommandRankPermissionsRemove(plugin, ce, true));
        addSubPowerCommand("delete", new CommandRankPermissionsRemove(plugin, ce, false));
        addSubPowerCommand("del", new CommandRankPermissionsRemove(plugin, ce, false));

        addSubPowerCommand("world", new CommandRankPermissionsWorld(plugin, ce, true));
        addSubPowerCommand("w", new CommandRankPermissionsWorld(plugin, ce, false));
    }

    @Override
    public String getArgumentSuggestions() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Base rank permissions command";
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