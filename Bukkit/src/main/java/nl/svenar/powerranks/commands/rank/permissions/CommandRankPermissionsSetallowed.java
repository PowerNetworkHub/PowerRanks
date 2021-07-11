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

public class CommandRankPermissionsSetallowed extends PowerCommand {

    public CommandRankPermissionsSetallowed(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "<rankname> <permission.node> <allowed: true/false>";
    }

    @Override
    public String getDescription() {
        return "Change the allowance of a permission node on a rank";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.rank.permissions.setallowed")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                    + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }

        if (args.length == 3) {
            String rankName = args[0];
            String permissionName = args[1];
            boolean allowed = args[2].equalsIgnoreCase("true");

            if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
                switch (PowerRanks.getAPI().setRankPermissionAllowed(rankName, permissionName, allowed)) {
                    case RANK_FAILED_NAME_NOT_FOUND:
                        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                                + plugin.getLangConfig().getNode("plugin.commands.rank.general.does-not-exists"));
                        break;
                    case RANK_FAILED_PERMISSION_NOT_FOUND:
                        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig()
                                .getNode("plugin.commands.rank.general.permission-does-not-exist"));
                        break;
                    case RANK_PERMISSION_ALLOWED_CHANGED_SUCCESSFULLY:
                        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + plugin.getLangConfig()
                                .getNode("plugin.commands.rank.permissions.setallowed.allowed-changed-successfully"));
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
            for (PermissionAttachmentInfo permission : Bukkit.getServer().getConsoleSender()
                    .getEffectivePermissions()) {
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