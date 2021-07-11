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

public class CommandPlayerPermissionsSetallowed extends PowerCommand {

    public CommandPlayerPermissionsSetallowed(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
        //TODO Auto-generated constructor stub
    }

    @Override
    public String getArgumentSuggestions() {
        return "<playername> <permission.node> <allowed: true/false>";
    }

    @Override
    public String getDescription() {
        return "Change the allowance of a permission on a player";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 2 || args.length == 3) {

            String playerName = args[0];
            String permissionNode = args[1];
            boolean permissionAllowed = args.length == 3 ? (args[2].equalsIgnoreCase("true") ? true : false) : true;

            switch (PowerRanks.getAPI().setPlayerPermissionAllowed(playerName, permissionNode, permissionAllowed)) {
                case PLAYER_FAILED_DOES_NOT_HAVE_PERMISSION:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.rank.general.permission-does-not-exist"));
                    break;
                case PLAYER_FAILED_NAME_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.player.general.player-not-found"));
                    break;
                case PLAYER_PERMISSION_ALLOWED_CHANGED_SUCCESSFULLY:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + plugin.getLangConfig()
                            .getNode("plugin.commands.player.permissions.player-permission-allowance-changed-successfully"));
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

        if (args.length == 3) {
            list.add("true");
            list.add("false");
        }

        return list;
    }

}
