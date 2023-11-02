package nl.svenar.powerranks.nukkit.commands.core;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.ImmutableMap;

import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.common.utils.PRUtil;


import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.Player;

public class cmd_pluginhook extends PowerCommand {

    public cmd_pluginhook(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(
                    TextFormat.DARK_AQUA + "--------" + TextFormat.DARK_BLUE + plugin.getDescription().getName()
                            + TextFormat.DARK_AQUA + "--------");
            sender.sendMessage(TextFormat.GREEN + "Plugin hooks:");
            sender.sendMessage(TextFormat.GREEN + "- None");
            sender.sendMessage(TextFormat.DARK_AQUA + "--------------------------");
        } else if (args.length == 2) {
            String state = args[0];
            String pluginname = args[1];

            if ((state.equalsIgnoreCase("enable") || state.equalsIgnoreCase("disable"))
                    && plugin.getConfigManager().getMap("plugin_hook", new HashMap<String, String>()).keySet()
                            .contains(pluginname.toLowerCase())) {

                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".state-changed"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("config_target", pluginname.toLowerCase())
                                .put("old_state",
                                        String.valueOf(plugin.getConfigManager()
                                                .getBool("plugin_hook." + pluginname.toLowerCase(), true)))
                                .put("new_state", String.valueOf(state.equalsIgnoreCase("enable")))
                                .build(),
                        '[', ']'));

                plugin.getConfigManager().setBool("plugin_hook." + pluginname.toLowerCase(),
                        state.equalsIgnoreCase("enable"));

            } else {
                if (state.equalsIgnoreCase("enable") || state.equalsIgnoreCase("disable")) {
                    sender.sendMessage(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "commands." + commandName.toLowerCase() + ".unknown-plugin"));
                } else {
                    sender.sendMessage(
                            plugin.getLanguageManager().getFormattedMessage(
                                    "commands." + commandName.toLowerCase() + ".unknown-state"));
                }
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
            tabcomplete.add("enable");
            tabcomplete.add("disable");
        }

        if (args.length == 2) {
            for (Object plugin : plugin.getConfigManager().getMap("plugin_hook", new HashMap<String, String>())
                    .keySet()) {
                tabcomplete.add((String) plugin);
            }
        }

        return tabcomplete;
    }
}
