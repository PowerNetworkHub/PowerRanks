package nl.svenar.powerranks.commands.rank;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class CommandRankSetDefault extends PowerCommand {

    public CommandRankSetDefault(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "<rankname> [default: boolean]";
    }

    @Override
    public String getDescription() {
        return "Change a ranks default value";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.rank.setdefault")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
            return false;
        }
        
        if (args.length == 2) {
            String rankName = args[0];
            try {
                boolean newDefault = Boolean.parseBoolean(args[1]);
                switch (PowerRanks.getAPI().setRankDefault(rankName, newDefault)) {
                    case RANK_SET_DEFAULT_SUCCESSFULLY:
                        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + plugin.getLangConfig()
                                .getNode("plugin.commands.rank.set-default.default-changed-successfully"));
                        break;
                    case RANK_FAILED_NAME_NOT_FOUND:
                        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                                + plugin.getLangConfig().getNode("plugin.commands.rank.general.rank-does-not-exists"));
                        break;
                    default:
                        break;
                }
            } catch (NumberFormatException nfe) {
                sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                        + plugin.getLangConfig().getNode("plugin.commands.invalid-number-argument"));
            }

            // switch (PowerRanks.getAPI().setRankWeight(rankName, newWeight)) {
            // case RANK_SET_PREFIX_SUCCESSFULLY:
            // sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN +
            // plugin.getLangConfig()
            // .getNode("plugin.commands.rank.set-prefix.prefix-changed-successfully"));
            // break;
            // case RANK_FAILED_NAME_NOT_FOUND:
            // sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
            // +
            // plugin.getLangConfig().getNode("plugin.commands.rank.general.rank-does-not-exists"));
            // break;
            // default:
            // break;

            // }
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
            list.add("true");
            list.add("false");
        }

        return list;
    }
}