package nl.svenar.powerranks.commands.rank.permissions;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionAttachmentInfo;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class CommandRankPermissionsAdd extends PowerCommand {

    public CommandRankPermissionsAdd(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "<rankname> <permission.node> [allowed: true/false]";
    }

    @Override
    public String getDescription() {
        return "Add a permission node to a rank";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.rank.permissions.add")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }
        
        if (args.length == 2 || args.length == 3) {
            String rankName = args[0];
            String permissionName = args[1];
            boolean allowed = args.length == 3 ? args[2].equalsIgnoreCase("true") : true;

            if (args.length == 2 || (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false"))) {
                switch (PowerRanks.getAPI().addRankPermission(rankName, permissionName, allowed)) {
                    case RANK_FAILED_NAME_NOT_FOUND:
                        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                                + plugin.getLangConfig().getNode("plugin.commands.rank.general.does-not-exists"));
                        break;
                    case RANK_PERMISSION_ADD_FAILED_PERMISSION_ALREADY_EXISTS:
                        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig()
                                .getNode("plugin.commands.rank.permissions.add.permission-already-exists"));
                        break;
                    case RANK_PERMISSION_ADD_SUCCESSFULLY:
                        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + plugin.getLangConfig()
                                .getNode("plugin.commands.rank.permissions.add.permission-added-successfully"));
                        break;
                    default:
                        break;
                }
            } else {
                sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                        + plugin.getLangConfig().getNode("plugin.commands.invalid-boolean-argument"));
            }

        } else if (args.length > 3) {
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
            for (PermissionAttachmentInfo permission : Bukkit.getServer().getConsoleSender().getEffectivePermissions()) {
                list.add(permission.getPermission());
            }
        }

        if (args.length == 3) {
            list.add("true");
            list.add("false");
        }

        return list;
    }
}