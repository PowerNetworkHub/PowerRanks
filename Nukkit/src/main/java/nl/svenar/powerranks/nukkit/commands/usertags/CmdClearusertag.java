package nl.svenar.powerranks.nukkit.commands.usertags;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.common.utils.PRUtil;


import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

public class CmdClearusertag extends PowerCommand {

    public CmdClearusertag(PowerRanks plugin, String command_name, COMMANDEXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        if (args.length == 0) {

            final String playername = sender.getName();
            final boolean result = plugin.getUsertagManager().clearUsertag(playername);
            if (result) {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".success"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("target", playername)
                                .build(),
                        '[', ']'));
            } else {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".failed"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("target", playername)
                                .build(),
                        '[', ']'));
            }

        } else if (args.length == 1) {
            if (sender.hasPermission("powerranks.cmd." + commandName.toLowerCase() + ".other")) {

                final String playername = args[0];
                final boolean result = plugin.getUsertagManager().clearUsertag(playername);
                if (result) {
                    sender.sendMessage(PRUtil.powerFormatter(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "commands." + commandName.toLowerCase() + ".success"),
                            ImmutableMap.<String, String>builder()
                                    .put("player", sender.getName())
                                    .put("target", playername)
                                    .build(),
                            '[', ']'));
                } else {
                    sender.sendMessage(PRUtil.powerFormatter(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "commands." + commandName.toLowerCase() + ".failed"),
                            ImmutableMap.<String, String>builder()
                                    .put("player", sender.getName())
                                    .put("target", playername)
                                    .build(),
                            '[', ']'));
                }

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
            for (Player player : plugin.getServer().getOnlinePlayers().values()) {
                tabcomplete.add(player.getName());
            }
        }

        return tabcomplete;
    }
}
