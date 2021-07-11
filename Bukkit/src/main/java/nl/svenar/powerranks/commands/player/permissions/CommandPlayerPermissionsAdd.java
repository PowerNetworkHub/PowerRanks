package nl.svenar.powerranks.commands.player.permissions;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionAttachmentInfo;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class CommandPlayerPermissionsAdd extends PowerCommand {

    public CommandPlayerPermissionsAdd(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);

    }

    @Override
    public String getArgumentSuggestions() {
        return "<playername> <permission.node> [allowed: true/false]";
    }

    @Override
    public String getDescription() {
        return "Add a permission to a player";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 2 || args.length == 3) {

            String playerName = args[0];
            String permissionNode = args[1];
            boolean permissionAllowed = args.length == 3 ? (args[2].equalsIgnoreCase("true") ? true : false) : true;

            switch (PowerRanks.getAPI().addPlayerPermission(playerName, permissionNode, permissionAllowed)) {
                case PLAYER_FAILED_ALREADY_HAS_PERMISSION:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.player.permissions.player-permission-already-exists"));
                    break;
                case PLAYER_FAILED_NAME_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.player.general.player-not-found"));
                    break;
                case PLAYER_SUCCESSFULLY_ADDED_PERMISSION:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN
                            + plugin.getLangConfig().getNode("plugin.commands.player.permissions.player-permission-added-successfully"));
                    break;
                default:
                    break;
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
            for (PRPlayer player : BaseDataHandler.getPlayers()) {
                list.add(player.getName());
            }
        }

        if (args.length == 2) {
            for (PermissionAttachmentInfo pai : Bukkit.getServer().getConsoleSender().getEffectivePermissions()) {
                list.add(pai.getPermission());
            }
        }

        if (args.length == 3) {
            list.add("true");
            list.add("false");
        }

        return list;
    }
}