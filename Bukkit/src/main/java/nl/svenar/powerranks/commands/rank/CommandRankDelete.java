package nl.svenar.powerranks.commands.rank;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class CommandRankDelete extends PowerCommand {

    public CommandRankDelete(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "<rankname>";
    }

    @Override
    public String getDescription() {
        return "Delete an existing rank";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.rank.delete")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }
        
        if (args.length == 1) {
            String rankName = args[0];

            switch (PowerRanks.getAPI().deleteRank(rankName)) {
                case RANK_DELETE_SUCCESSFULLY:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN
                            + plugin.getLangConfig().getNode("plugin.commands.rank.delete.deleted-successfully"));
                    break;
                case RANK_FAILED_NAME_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.rank.general.rank-does-not-exists"));
                    break;
                case RANK_FAILED_CANNOT_DELETE_DEFAULT_RANK:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.rank.delete.cannot-delete-this-rank"));
                    break;
                default:
                    break;

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
                if (!rank.getName().equals("default")) {
                    list.add(rank.getName());
                }
            }
        }
        return list;
    }
}