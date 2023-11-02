package nl.svenar.powerranks.nukkit.commands.rank;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;


import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;

public class CmdDelperm extends PowerCommand {


    public CmdDelperm(PowerRanks plugin, String command_name, COMMANDEXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        if (args.length == 2) {
            final String rankname = args[0].equals("*") ? args[0] : args[0];
            final String permissionNode = args[1];
            PRRank rank = PRCache.getRank(rankname);
            if (rank != null) {
                boolean found = false;
                for (PRPermission permission : rank.getPermissions()) {
                    if (permission.getName().equalsIgnoreCase(permissionNode)) {
                        rank.removePermission(permission);
                        found = true;
                        break;
                    }
                }
                if (found) {
                    if (rankname.equals("*")) {
                        sender.sendMessage(PRUtil.powerFormatter(
                                plugin.getLanguageManager().getFormattedMessage(
                                        "commands." + commandName.toLowerCase() + ".success-all"),
                                ImmutableMap.<String, String>builder()
                                        .put("player", sender.getName())
                                        .put("rank", rankname)
                                        .put("permission", permissionNode)
                                        .build(),
                                '[', ']'));
                    } else {
                        sender.sendMessage(PRUtil.powerFormatter(
                                plugin.getLanguageManager().getFormattedMessage(
                                        "commands." + commandName.toLowerCase() + ".success"),
                                ImmutableMap.<String, String>builder()
                                        .put("player", sender.getName())
                                        .put("rank", rankname)
                                        .put("permission", permissionNode)
                                        .build(),
                                '[', ']'));
                    }
                } else { // Permission not found
                    sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".failed"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("rank", rankname)
                                .put("permission", permissionNode)
                                .build(),
                        '[', ']'));
                }
            } else { // Rank not found
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".failed"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("rank", rankname)
                                .put("permission", permissionNode)
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

        if (args.length == 2) {
            PRRank rank = PRCache.getRankIgnoreCase(args[0]);
            if (rank != null) {
                for (PRPermission permission : rank.getPermissions()) {

                    if (!tabcomplete.contains(permission.getName())) {
                        tabcomplete.add(permission.getName());
                    }
                }
            }
        }

        return tabcomplete;
    }
}
