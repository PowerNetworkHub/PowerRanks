package nl.svenar.powerranks.commands.player;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRPlayerRank;
import nl.svenar.common.structure.PRRank;
import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.cache.CacheManager;
import nl.svenar.powerranks.commands.PowerCommand;
import nl.svenar.powerranks.data.Users;
import nl.svenar.powerranks.util.Util;

public class CmdAddownrank extends PowerCommand {

    private Users users;

    public CmdAddownrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
        super(plugin, command_name, ce);
        this.users = new Users(plugin);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String commandName, String[] args) {
        if (args.length == 1) {
            String target_rank = users.getRankIgnoreCase(args[0]);

            boolean commandAllowed = false;
            if (sender instanceof Player) {
                commandAllowed = sender.hasPermission("powerranks.cmd." + commandName.toLowerCase() + "." + target_rank);
            } else {
                commandAllowed = true;
            }

            if (commandAllowed) {
                PRRank rank = CacheManager.getRank(users.getRankIgnoreCase(target_rank));
                PRPlayer targetPlayer = CacheManager.getPlayer(sender.getName());
                if (rank != null && targetPlayer != null) {
                    PRPlayerRank playerRank = new PRPlayerRank(rank.getName());
                    targetPlayer.addRank(playerRank);

                    if (Bukkit.getPlayer(targetPlayer.getUUID()) != null) {
                        PowerRanks.getInstance().updateTablistName(Bukkit.getPlayer(targetPlayer.getUUID()));
                        PowerRanks.getInstance().getTablistManager().updateSorting(Bukkit.getPlayer(targetPlayer.getUUID()));
                    }

                    sender.sendMessage(Util.powerFormatter(
                            PowerRanks.getLanguageManager()
                                    .getFormattedMessage("commands." + commandName.toLowerCase() + ".success-executor"),
                            ImmutableMap.<String, String> builder().put("player", targetPlayer.getName()).put("rank", rank.getName())
                                    .build(),
                            '[', ']'));

                    sender.sendMessage(Util.powerFormatter(
                            PowerRanks.getLanguageManager()
                                    .getFormattedMessage("commands." + commandName.toLowerCase() + ".success-receiver"),
                            ImmutableMap.<String, String> builder().put("player", sender.getName()).put("rank", rank.getName()).build(),
                            '[', ']'));
                } else {
                    if (targetPlayer != null) {
                        sender.sendMessage(Util.powerFormatter(
                                PowerRanks.getLanguageManager()
                                        .getFormattedMessage("commands." + commandName.toLowerCase() + ".failed-executor"),
                                ImmutableMap.<String, String> builder().put("player", args[0]).put("rank", rank.getName()).build(), '[',
                                ']'));
                    } else {
                        sender.sendMessage(
                                Util.powerFormatter(
                                        PowerRanks.getLanguageManager().getFormattedMessage("general.player-not-found"), ImmutableMap
                                                .<String, String> builder().put("player", sender.getName()).put("target", args[0]).build(),
                                        '[', ']'));
                    }
                }
            } else {
                sender.sendMessage(PowerRanks.getLanguageManager().getFormattedMessage("general.no-permission"));
            }
        } else {
            sender.sendMessage(PowerRanks.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
                    "commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
        }

        return false;
    }

    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        ArrayList<String> tabcomplete = new ArrayList<String>();

        if (args.length == 1) {
            for (PRRank rank : this.users.getGroups()) {
                tabcomplete.add(rank.getName());
            }
        }

        return tabcomplete;
    }
}
