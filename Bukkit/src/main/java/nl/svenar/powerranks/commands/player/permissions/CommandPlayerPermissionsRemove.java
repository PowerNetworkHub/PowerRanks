package nl.svenar.powerranks.commands.player.permissions;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRPermission;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class CommandPlayerPermissionsRemove extends PowerCommand {

    public CommandPlayerPermissionsRemove(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "<playername> <permission.node>";
    }

    @Override
    public String getDescription() {
        return "Remove an permission from a player";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 2) {

            String playerName = args[0];
            String permissionNode = args[1];

            switch (PowerRanks.getAPI().removePlayerPermission(playerName, permissionNode)) {
                case PLAYER_FAILED_DOES_NOT_HAVE_PERMISSION:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.rank.general.permission-does-not-exist"));
                    break;
                case PLAYER_FAILED_NAME_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.player.general.player-not-found"));
                    break;
                case PLAYER_SUCCESSFULLY_REMOVED_PERMISSION:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + plugin.getLangConfig()
                            .getNode("plugin.commands.player.permissions.player-permission-removed-successfully"));
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
            for (PRPlayer player : BaseDataHandler.getPlayers()) {
                list.add(player.getName());
            }
        }

        if (args.length == 2) {
            PRPlayer player = BaseDataHandler.getPlayer(args[0]);
            if (player != null) {
                for (PRPermission permission : player.getPermissions()) {
                    list.add(permission.getName());
                }
            }

        }

        return list;
    }

}
