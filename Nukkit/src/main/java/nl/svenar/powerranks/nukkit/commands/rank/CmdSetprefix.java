package nl.svenar.powerranks.nukkit.commands.rank;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;


import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;

public class CmdSetprefix extends PowerCommand {


    public CmdSetprefix(PowerRanks plugin, String command_name, COMMANDEXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        if (args.length == 1) {
            final String rankname = args[0];
            final String prefix = "";
            PRRank rank = PRCache.getRank(rankname);
            if (rank != null) {
                rank.setSuffix(prefix);
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".success-clear"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("rank", rankname)
                                .put("prefix", prefix)
                                .build(),
                        '[', ']'));
            } else {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage("general.rank-not-found"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("rank", rankname)
                                .build(),
                        '[', ']'));
            }
        } else if (args.length >= 2) {
            final String rankname = args[0];
            String prefix = "";
            for (int i = 1; i < args.length; i++) {
                prefix += args[i] + " ";
            }
            prefix = prefix.substring(0, prefix.length() - 1);
            PRRank rank = PRCache.getRank(rankname);
            if (rank != null) {
                rank.setSuffix(prefix);
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".success"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("rank", rankname)
                                .put("prefix", prefix)
                                .build(),
                        '[', ']'));
            } else {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "general.rank-not-found"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("rank", rankname)
                                .build(),
                        '[', ']'));
            }
        } else {
            sender.sendMessage(
                    plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
                            "commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
        }

        return false;
    }

    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        ArrayList<String> tabcomplete = new ArrayList<String>();

        if (args.length == 1) {
            for (PRRank rank : PRCache.getRanks()) {
                tabcomplete.add(rank.getName());
            }
        }

        return tabcomplete;
    }
}
