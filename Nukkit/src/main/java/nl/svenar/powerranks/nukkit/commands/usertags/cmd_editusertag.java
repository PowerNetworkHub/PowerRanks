package nl.svenar.powerranks.nukkit.commands.usertags;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.common.utils.PRUtil;


import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

public class cmd_editusertag extends PowerCommand {

    public cmd_editusertag(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        if (args.length == 2) {
            final String tag = args[0];
            final String text = args[1];
            final boolean result = plugin.getUsertagManager().editUsertag(tag, text);
            if (result) {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".success"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("usertag", tag)
                                .put("text", text)
                                .build(),
                        '[', ']'));
            } else {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".failed"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("usertag", tag)
                                .put("text", text)
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
            for (String tag : plugin.getUsertagManager().getUsertags()) {
                if (tag.toLowerCase().contains(args[0].toLowerCase()))
                    tabcomplete.add(tag);
            }
        }

        return tabcomplete;
    }
}
