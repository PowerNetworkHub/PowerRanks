package nl.svenar.powerranks.commands.rank;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;

public class CommandRankCreate extends PowerCommand {

    public CommandRankCreate(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "<rankname>";
    }

    @Override
    public String getDescription() {
        return "Create a new rank";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.rank.create")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }
        
        if (args.length == 1) {
            String rankName = args[0];

            switch (PowerRanks.getAPI().createRank(rankName)) {
                case RANK_CREATE_FAILED_INVALID_CHARACTERS:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig().getNode("plugin.commands.rank.create.invalid-characters"));
                    break;
                case RANK_CREATE_FAILED_NAME_ALREADY_EXISTS:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig().getNode("plugin.commands.rank.create.already-exists"));
                    break;
                case RANK_CREATE_SUCCESSFULLY:
                    sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + plugin.getLangConfig().getNode("plugin.commands.rank.create.created-successfully"));
                    break;
                default:
                    break;
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