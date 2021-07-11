package nl.svenar.powerranks.commands.rank.permissions.world;

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

public class CommandRankPermissionsWorldList extends PowerCommand {

    public CommandRankPermissionsWorldList(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "<rankname> <permission.node>";
    }

    @Override
    public String getDescription() {
        return "List all worlds in a permissions in a rank";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.rank.permissions.world.list")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                    + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }

        if (args.length == 2 || args.length == 3) {
            String rankName = args[0];
            String permissionNode = args[1];
            PRPermission prPermission = null;

            if (PowerRanks.getAPI().getRank(rankName) != null) {
                for (PRPermission permission : PowerRanks.getAPI().getRankPermissions(rankName)) {
                    if (permission.getName().equals(permissionNode)) {
                        prPermission = permission;
                        break;
                    }
                }
                if (prPermission == null) {
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.rank.general.permission-does-not-exist"));
                } else {
                    int page = Integer.MIN_VALUE;
                    if (args.length > 2) {
                        try {
                            page = Integer.parseInt(args[2]) - 1;

                        } catch (NumberFormatException nfe) {
                            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                                    + plugin.getLangConfig().getNode("plugin.commands.invalid-number-argument"));
                        }
                    } else {
                        page = 0;
                    }

                    ArrayList<String> pageItems = new ArrayList<String>();

                    for (World world : Bukkit.getServer().getWorlds()) {
                        pageItems.add(ChatColor.DARK_GREEN + world.getName() + ": "
                                + (prPermission.isAllowed(world) ? ChatColor.GREEN : ChatColor.RED)
                                + String.valueOf(prPermission.isAllowed(world)));

                    }

                    PaginationManager pm = new PaginationManager(pageItems,
                            PowerRanks.getAPI().getRank(rankName).getName() + "> " + prPermission.getName() + "> "
                                    + "worlds",
                            "rank permissions world list " + rankName + " " + permissionNode, page, 3);
                    pm.send(sender);
                }
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

        if (args.length == 2) {
            PRRank rank = BaseDataHandler.getRank(args[0]);
            if (rank != null) {
                for (PRPermission permission : rank.getPermissions()) {
                    list.add(permission.getName());
                }
            }
        }

        return list;
    }
}