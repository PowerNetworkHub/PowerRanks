package nl.svenar.powerranks.commands.rank;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class CommandRankSetSuffix extends PowerCommand {

    public CommandRankSetSuffix(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "<rankname> [new suffix]";
    }

    @Override
    public String getDescription() {
        return "Change a ranks suffix";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.rank.setsuffix")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }
        
        if (args.length > 0) {
            String rankName = args[0];
            String newSuffix = args.length > 1 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "";

            switch (PowerRanks.getAPI().setRankSuffix(rankName, newSuffix)) {
                case RANK_SET_SUFFIX_SUCCESSFULLY:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN
                            + plugin.getLangConfig().getNode("plugin.commands.rank.set-suffix.suffix-changed-successfully"));
                    break;
                case RANK_FAILED_NAME_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.rank.general.rank-does-not-exists"));
                    break;
                default:
                    break;

            }
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
            PRRank rank = BaseDataHandler.getRank(args[0]);
            if (rank != null) {
                list.add(rank.getSuffix());
            }
        }

        return list;
    }
}