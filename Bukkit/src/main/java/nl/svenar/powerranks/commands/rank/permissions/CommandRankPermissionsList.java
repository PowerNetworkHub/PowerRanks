package nl.svenar.powerranks.commands.rank.permissions;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRPermission;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;
import nl.svenar.powerranks.utils.PaginationManager;

public class CommandRankPermissionsList extends PowerCommand {

    public CommandRankPermissionsList(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "<rankname>";
    }

    @Override
    public String getDescription() {
        return "List all available permissions in a rank";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.rank.permissions.list")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                    + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }

        if (args.length == 1 || args.length == 2) {
            String rankName = args[0];

            if (PowerRanks.getAPI().getRank(rankName) != null) {

                int page = Integer.MIN_VALUE;
                if (args.length > 1) {
                    try {
                        page = Integer.parseInt(args[1]) - 1;

                    } catch (NumberFormatException nfe) {
                        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                                + plugin.getLangConfig().getNode("plugin.commands.invalid-number-argument"));
                    }
                } else {
                    page = 0;
                }

                ArrayList<String> pageItems = new ArrayList<String>();

                for (PRPermission permission : PowerRanks.getAPI().getRankPermissions(rankName)) {
                    String pageItem = "";
                    pageItem += ChatColor.GREEN + permission.getName() + "\n";
                    pageItem += ChatColor.DARK_GREEN + "- allowed: "
                            + (permission.isAllowed(null) ? ChatColor.GREEN : ChatColor.RED)
                            + permission.isAllowed(null) + "\n";
                    // pageItem += ChatColor.DARK_GREEN + "- inherited ranks: " + ChatColor.GREEN +
                    // rank.getInheritedRanks().size() + "\n";
                    // pageItem += ChatColor.DARK_GREEN + "- permissions: " + ChatColor.GREEN +
                    // rank.getPermissions().size();
                    ArrayList<String> worldAllowances = new ArrayList<String>();
                    for (World world : Bukkit.getServer().getWorlds()) {
                        worldAllowances
                                .add((permission.isAllowed(world) ? ChatColor.GREEN : ChatColor.RED) + world.getName());
                    }
                    pageItem += ChatColor.DARK_GREEN + "- worlds: "
                            + String.join(ChatColor.RESET + ", ", worldAllowances);
                    pageItems.add(pageItem);
                }

                PaginationManager pm = new PaginationManager(pageItems, PowerRanks.getAPI().getRank(rankName).getName() + "'s permissions", "rank permissions list " + rankName, page, 3);
                pm.send(sender);

            } else {
                sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                        + plugin.getLangConfig().getNode("plugin.commands.rank.general.does-not-exists"));
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

        return list;
    }
}