package nl.svenar.powerranks.nukkit.commands.player;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.collect.ImmutableMap;


import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.nukkit.util.Util;

public class CmdAddrank extends PowerCommand {

    public CmdAddrank(PowerRanks plugin, String command_name, COMMANDEXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        if (args.length >= 2) {
            String target_rank = args[1];
            String[] tags = Arrays.copyOfRange(args, 2, args.length);

            boolean commandAllowed = false;
            if (sender instanceof Player) {
                commandAllowed = sender
                        .hasPermission("powerranks.cmd." + commandName.toLowerCase() + "." + target_rank);
            } else {
                commandAllowed = true;
            }

            if (commandAllowed) {
                PRRank rank = PRCache.getRankIgnoreCase(target_rank);
                PRPlayer targetPlayer = PRCache.getPlayer(args[0]);
                if (rank != null && targetPlayer != null) {
                    PRPlayerRank playerRank = new PRPlayerRank(rank.getName());
                    boolean alreadyHasRank = targetPlayer.hasRank(rank.getName());
                    if (!alreadyHasRank) {

                        for (String tag : tags) {
                            if (tag.split(":").length == 2) {
                                String[] tagParts = tag.split(":");
                                String tagName = tagParts[0];
                                String tagValue = tagParts[1];

                                if (tagName.length() > 0 && tagValue.length() > 0) {
                                    playerRank.addTag(tagName, tagValue);
                                }
                            }
                        }

                        targetPlayer.addRank(playerRank);

                        sender.sendMessage(PRUtil.powerFormatter(
                                plugin.getLanguageManager()
                                        .getFormattedMessage(
                                                "commands." + commandName.toLowerCase() + ".success-executor"),
                                ImmutableMap.<String, String>builder()
                                        .put("player", targetPlayer.getName())
                                        .put("rank", rank.getName())
                                        .build(),
                                '[', ']'));
                        if (Util.getPlayerByName(targetPlayer.getName()) != null) {
                            Util.getPlayerByName(targetPlayer.getName()).sendMessage(PRUtil.powerFormatter(
                                    plugin.getLanguageManager().getFormattedMessage(
                                            "commands." + commandName.toLowerCase() + ".success-receiver"),
                                    ImmutableMap.<String, String>builder()
                                            .put("player", sender.getName())
                                            .put("rank", rank.getName())
                                            .build(),
                                    '[', ']'));
                        }
                    } else {
                        sender.sendMessage(PRUtil.powerFormatter(
                                plugin.getLanguageManager()
                                        .getFormattedMessage(
                                                "commands." + commandName.toLowerCase() + ".failed-already-has-rank"),
                                ImmutableMap.<String, String>builder()
                                        .put("player", targetPlayer.getName())
                                        .put("rank", rank.getName())
                                        .build(),
                                '[', ']'));
                    }
                } else {
                    if (targetPlayer != null && rank != null) {
                        sender.sendMessage(PRUtil.powerFormatter(
                                plugin.getLanguageManager()
                                        .getFormattedMessage(
                                                "commands." + commandName.toLowerCase() + ".failed-executor"),
                                ImmutableMap.<String, String>builder()
                                        .put("player", targetPlayer.getName())
                                        .put("rank", rank.getName())
                                        .build(),
                                '[', ']'));
                    }
                }
                // users.setGroup(sender instanceof Player ? (Player) sender : null, args[0],
                // target_rank, true);

            } else {
                sender.sendMessage(plugin.getLanguageManager().getFormattedMessage("general.no-permission"));
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
            for (PRPlayer player : PRCache.getPlayers()) {
                tabcomplete.add(player.getName());
            }
        }

        if (args.length == 2) {
            PRPlayer targetPlayer = PRCache.getPlayer(args[0]);
            if (targetPlayer != null) {
                for (PRRank rank : PRCache.getRanks()) {
                    if (!targetPlayer.hasRank(rank.getName())) {
                        tabcomplete.add(rank.getName());
                    }
                }
            }
        }

        return tabcomplete;
    }
}
