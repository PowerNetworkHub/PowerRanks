package nl.svenar.powerranks.commands.rank;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.utils.PaginationManager;
import nl.svenar.powerranks.utils.PowerChatColor;

public class CommandRankList extends PowerCommand {

    public CommandRankList(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "";
    }

    @Override
    public String getDescription() {
        return "List all available ranks";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.rank.list")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                    + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }

        if (args.length == 0 || args.length == 1) {

            int page = Integer.MIN_VALUE;
            if (args.length > 0) {
                try {
                    page = Integer.parseInt(args[0]) - 1;

                } catch (NumberFormatException nfe) {
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.invalid-number-argument"));
                }
            } else {
                page = 0;
            }

            ArrayList<String> pageItems = new ArrayList<String>();

            for (PRRank rank : PowerRanks.getAPI().getRanks()) {
                String pageItem = "";
                pageItem += ChatColor.GREEN + rank.getName() + "\n";
                pageItem += ChatColor.DARK_GREEN + "- weight: " + ChatColor.GREEN + rank.getWeight() + "\n";
                pageItem += ChatColor.DARK_GREEN + "- prefix: " + ChatColor.GREEN + PowerChatColor.colorize(rank.getPrefix(), true) + "\n";
                pageItem += ChatColor.DARK_GREEN + "- suffix: " + ChatColor.GREEN + PowerChatColor.colorize(rank.getSuffix(), true) + "\n";
                pageItem += ChatColor.DARK_GREEN + "- inherited ranks: " + ChatColor.GREEN + rank.getInheritedRanks().size() + "\n";
                pageItem += ChatColor.DARK_GREEN + "- permissions: " + ChatColor.GREEN + rank.getPermissions().size();
                pageItems.add(pageItem);
            }

            PaginationManager pm = new PaginationManager(pageItems, "rank list", "pr rank list", page, 2);
            pm.send(sender);

            // sender.sendMessage(plugin.getCommandHeader("rank list"));
            // for (PRRank rank : PowerRanks.getAPI().getRanks()) {
            // sender.sendMessage(rank.getName());
            // sender.sendMessage("- weight: " + rank.getWeight());
            // sender.sendMessage("- prefix: " + rank.getPrefix());
            // sender.sendMessage("- suffix: " + rank.getSuffix());
            // sender.sendMessage("- permission count: " + rank.getPermissions().size());
            // }
            // sender.sendMessage(plugin.getCommandFooter());

        } else {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                    + plugin.getLangConfig().getNode("plugin.commands.too-many-arguments"));
        }
        return false;
    }

    @Override
    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        return new ArrayList<String>();
    }
}