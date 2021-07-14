package nl.svenar.powerranks.commands.rank.inheritance;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class CommandRankInheritanceList extends PowerCommand {

    public CommandRankInheritanceList(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);

    }

    @Override
    public String getArgumentSuggestions() {
        return "<rankname>";
    }

    @Override
    public String getDescription() {
        return "List all inheritances on a rank";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.rank.inheritance.list")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                    + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }

        if (args.length == 1) {
            String rankName = args[0];
            PRRank rank = BaseDataHandler.getRank(rankName);

            if (rank != null) {
                sender.sendMessage(plugin.getCommandHeader(rank.getName() + "'s inheritances"));
                for (String inheritanceName : rank.getInheritedRanks()) {
                    sender.sendMessage(inheritanceName);
                }
                sender.sendMessage(plugin.getCommandFooter());
            } else {
                sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                        + plugin.getLangConfig().getNode("plugin.commands.rank.general.does-not-exists"));
            }

        } else if (args.length > 1) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                    + plugin.getLangConfig().getNode("plugin.commands.too-many-arguments"));
        } else {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                    + plugin.getLangConfig().getNode("plugin.commands.insufficient-arguments"));
        }
        return false;
    }

    @Override
    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        ArrayList<String> list = new ArrayList<String>();

        if (args.length == 1) {
            for (PRRank rank : BaseDataHandler.getRanks()) {
                list.add(rank.getName());
            }
        }

        return list;
    }
}