package nl.svenar.powerranks.commands.player.rank;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class CommandPlayerRankRemove extends PowerCommand {

    public CommandPlayerRankRemove(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);

    }

    @Override
    public String getArgumentSuggestions() {
        return "<playerName> <rankName>";
    }

    @Override
    public String getDescription() {
        return "Remove a rank from a player";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.player.rank.remove")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }

        if (args.length == 2) {
            String playerName = args[0];
            String rankName = args[1];

            switch (PowerRanks.getAPI().removePlayerRank(playerName, rankName)) {
                case PLAYER_FAILED_NAME_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.player.general.player-not-found"));
                    break;

                case RANK_FAILED_NAME_NOT_FOUND:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.rank.general.rank-does-not-exists"));
                    break;

                case PLAYER_FAILED_DOES_NOT_HAVE_RANK:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig()
                            .getNode("plugin.commands.player.general.player-does-not-have-that-rank"));
                    break;

                case RANK_FAILED_CANNOT_DELETE_DEFAULT_RANK:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                            + plugin.getLangConfig().getNode("plugin.commands.rank.delete.cannot-delete-this-rank"));
                    break;

                case PLAYER_SUCCESSFULLY_REMOVED_RANK:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + plugin.getLangConfig()
                            .getNode("plugin.commands.player.general.player-rank-removed-successfully"));
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
                for (PRRank rank : player.getRanks()) {
                    list.add(rank.getName());
                }
            }
        }

        return list;
    }
}