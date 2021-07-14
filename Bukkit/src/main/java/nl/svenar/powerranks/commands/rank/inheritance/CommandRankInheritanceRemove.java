package nl.svenar.powerranks.commands.rank.inheritance;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class CommandRankInheritanceRemove extends PowerCommand {

    public CommandRankInheritanceRemove(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);

    }

    @Override
    public String getArgumentSuggestions() {
        return "<rankname> <inheritancerankname>";
    }

    @Override
    public String getDescription() {
        return "Remove a inheritance from an rank";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.rank.inheritance.remove")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                    + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }

        if (args.length == 2) {
            String rankName = args[0];
            String inheritanceName = args[1];

            switch (PowerRanks.getAPI().removeRankInheritance(rankName, inheritanceName)) {
                case RANK_REMOVE_INHERITANCE_SUCCESSFULLY:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN
                            + plugin.getLangConfig().getNode("plugin.commands.rank.inheritance.inheritance-removed-successfully"));
                    break;
                case RANK_REMOVE_INHERITANCE_FAILED_TARGET_RANK_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.rank.inheritance.target-rank-not-found"));
                    break;
                case RANK_REMOVE_INHERITANCE_FAIL_RANK_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.rank.inheritance.inheritance-rank-not-found"));
                    break;
                default:
                    break;
            }

        } else if (args.length > 2) {
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

        if (args.length == 2) {
            PRRank targetRank = BaseDataHandler.getRank(args[0]);
            for (String rankname : targetRank.getInheritedRanks()) {
                list.add(rankname);
            }
        }

        return list;
    }
}