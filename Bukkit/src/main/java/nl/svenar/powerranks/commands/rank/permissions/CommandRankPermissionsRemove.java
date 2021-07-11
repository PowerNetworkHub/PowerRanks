package nl.svenar.powerranks.commands.rank.permissions;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRPermission;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class CommandRankPermissionsRemove extends PowerCommand {

    public CommandRankPermissionsRemove(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "<rankname> <permission.node>";
    }

    @Override
    public String getDescription() {
        return "Remove a permission node from a rank";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.rank.permissions.remove")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }
        
        if (args.length == 2) {
            String rankName = args[0];
            String permissionName = args[1];

            switch (PowerRanks.getAPI().removeRankPermission(rankName, permissionName)) {
                case RANK_FAILED_NAME_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.rank.general.rank-does-not-exists"));
                    break;
                case RANK_FAILED_PERMISSION_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig()
                            .getNode("plugin.commands.rank.general.permission-does-not-exist"));
                    break;
                case RANK_PERMISSION_REMOVED_SUCCESSFULLY:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + plugin.getLangConfig()
                            .getNode("plugin.commands.rank.permissions.remove.permission-removed-successfully"));
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