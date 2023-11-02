package nl.svenar.powerranks.nukkit.commands.rank;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;

import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

public class cmd_createrank extends PowerCommand {


    public cmd_createrank(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        if (args.length == 1) {
            final String rankname = args[0];
            PRRank rank = PRCache.createRank(rankname);
            String[] forbiddenColorCharacters = { "&", "#" };
            String[] forbiddenCharacters = { "`", "~", "!", "@", "$", "%", "^", "*", "(", ")", "{", "}", "[", "]", ":",
                    ";", "\"", "'", "|", "\\", "?", "/", ">", "<", ",", ".", "+", "=" };
            if (rank != null) {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".success"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("rank", rankname)
                                .build(),
                        '[', ']'));
                if (PRUtil.stringContainsItemFromList(rankname, forbiddenColorCharacters)) {
                    sender.sendMessage(PRUtil.powerFormatter(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "commands." + commandName.toLowerCase() + ".warning-color"),
                            ImmutableMap.<String, String>builder()
                                    .put("player", sender.getName())
                                    .put("rank", rankname)
                                    .build(),
                            '[', ']'));
                }

                if (PRUtil.stringContainsItemFromList(rankname, forbiddenCharacters)) {
                    sender.sendMessage(PRUtil.powerFormatter(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "commands." + commandName.toLowerCase() + ".warning-character"),
                            ImmutableMap.<String, String>builder()
                                    .put("player", sender.getName())
                                    .put("rank", rankname)
                                    .build(),
                            '[', ']'));
                }
            } else {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".failed"),
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
        return tabcomplete;
    }
}
