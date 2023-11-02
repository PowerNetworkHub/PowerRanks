package nl.svenar.powerranks.nukkit.commands.core;

import java.util.ArrayList;


import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;

public class cmd_reload extends PowerCommand {

    public cmd_reload(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        sender.sendMessage(
                plugin.getLanguageManager().getFormattedMessage(
                        "commands." + commandName.toLowerCase() + ".warning"));

        if (args.length != 1) {
            sender.sendMessage(
                    plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
                            "commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
        } else {
            if (args[0].equalsIgnoreCase("config")) {

                sender.sendMessage(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".config-start"));

                                plugin.getConfigManager().reload();
                plugin.getLanguageManager().reload();
                plugin.getUsertagStorage().reload();

                plugin.getStorageLoader().loadData(plugin.getStorageManager());

                sender.sendMessage(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".config-done"));

            } else if (args[0].equalsIgnoreCase("plugin")) {

                sender.sendMessage(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".plugin-start"));

                sender.sendMessage("Not implemented yet"); // TODO

                sender.sendMessage(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".plugin-done"));

            } else if (args[0].equalsIgnoreCase("all")) {

                sender.sendMessage(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".config-start"));

                                plugin.getConfigManager().reload();
                plugin.getLanguageManager().reload();
                plugin.getUsertagStorage().reload();

                sender.sendMessage(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".config-done"));

                sender.sendMessage(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".plugin-start"));

                                sender.sendMessage("Not implemented yet"); // TODO

                                plugin.getStorageLoader().loadData(plugin.getStorageManager());
                sender.sendMessage(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".plugin-done"));

            } else {
                sender.sendMessage(
                        plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
                                "commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
            }
        }

        return false;
    }

    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        ArrayList<String> tabcomplete = new ArrayList<String>();

        if (args.length == 1) {
            tabcomplete.add("plugin");
            tabcomplete.add("config");
            tabcomplete.add("all");
        }

        return tabcomplete;
    }
}
