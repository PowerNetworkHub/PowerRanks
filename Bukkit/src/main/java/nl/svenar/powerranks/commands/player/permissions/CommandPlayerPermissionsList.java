package nl.svenar.powerranks.commands.player.permissions;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRPermission;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.handlers.BaseDataHandler;
import nl.svenar.powerranks.utils.PaginationManager;

public class CommandPlayerPermissionsList extends PowerCommand {

    public CommandPlayerPermissionsList(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);

    }

    @Override
    public String getArgumentSuggestions() {
        return "<playername>";
    }

    @Override
    public String getDescription() {
        return "List all available permissions of a player";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1 || args.length == 2) {

            String playerName = args[0];
            PRPlayer player = PowerRanks.getAPI().getPlayer(playerName);

            if (player != null) {
                Collection<PRPermission> playerPermissions = PowerRanks.getAPI().getPlayerPermissions(player);
                ArrayList<String> resultLines = new ArrayList<String>();
                for (PRPermission permission : playerPermissions) {
                    resultLines
                            .add((permission.isAllowed(null) ? ChatColor.GREEN : ChatColor.RED) + permission.getName());
                }

                int page = Integer.MIN_VALUE;
                if (args.length == 2) {
                    try {
                        page = Integer.parseInt(args[1]) - 1;

                    } catch (NumberFormatException nfe) {
                        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                                + plugin.getLangConfig().getNode("plugin.commands.invalid-number-argument"));
                    }
                } else {
                    page = 0;
                }

                if (page > Integer.MIN_VALUE) {
                    PaginationManager paginationManager = new PaginationManager(resultLines, args[0] + "'s permissions",
                            commandLabel + " player permissions list " + args[0], page, 5);
                    paginationManager.send(sender);
                }
            } else {
                sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                        + plugin.getLangConfig().getNode("plugin.commands.player.general.player-not-found"));
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
            for (PRPlayer player : BaseDataHandler.getPlayers()) {
                list.add(player.getName());
            }
        }

        return list;
    }
}