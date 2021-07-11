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

public class CommandPlayerRankList extends PowerCommand {

    public CommandPlayerRankList(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "<playerName>";
    }

    @Override
    public String getDescription() {
        return "List all ranks on a player";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.player.rank.list")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }
        
        if (args.length == 1) {
            PRPlayer prPlayer = BaseDataHandler.getPlayer(args[0]);
            if (prPlayer != null) {

                sender.sendMessage(plugin.getCommandHeader(prPlayer.getName() + "'s ranks"));
                for (PRRank rank : prPlayer.getRanks()) {
                    sender.sendMessage(rank.getName());
                    sender.sendMessage("- weight: " + rank.getWeight());
                    sender.sendMessage("- prefix: " + rank.getPrefix());
                    sender.sendMessage("- suffix: " + rank.getSuffix());
                    sender.sendMessage("- permission count: " + rank.getPermissions().size());
                }
                sender.sendMessage(plugin.getCommandFooter());
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
        return new ArrayList<String>();
    }
}