package nl.svenar.powerranks.nukkit.commands.player;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;


import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;

public class cmd_delplayerperm extends PowerCommand {

    public cmd_delplayerperm(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        if (args.length == 2) {
            final String targetPlayerName = args[0];
            final String permission = args[1];

            boolean result = false;
            PRPlayer targetPlayer = PRCache.getPlayer(targetPlayerName);
            if (targetPlayer != null) {
                for (PRPermission prPermission : targetPlayer.getPermissions()) {
                    targetPlayer.removePermission(prPermission);
                    result = true;
                    break;
                }
            }
            if (result) {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".success"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("target", targetPlayerName)
                                .put("permission", permission)
                                .build(),
                        '[', ']'));
            } else {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage(
                                "commands." + commandName.toLowerCase() + ".failed"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("target", targetPlayerName)
                                .put("permission", permission)
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
            for (PRPlayer player : PRCache.getPlayers()) {
                tabcomplete.add(player.getName());
            }
        }

        if (args.length == 2) {
            PRPlayer targetPlayer = PRCache.getPlayer(args[0]);
            if (targetPlayer != null) {
                // for (Permission pai : Nukkit.getServer().getPermissions()) {
                for (PRPermission perm : targetPlayer.getPermissions()) {
                    // String perm = pai.getPermission();
                    String userInput = args[1];
                    String autocompletePermission = "";

                    if (userInput.contains(".")) {
                        String[] permSplit = perm.getName().split("\\.");
                        for (int i = 0; i < permSplit.length; i++) {
                            String targetPerm = String.join(".", permSplit);
                            while (targetPerm.endsWith(".")) {
                                targetPerm = targetPerm.substring(0, targetPerm.length() - 1);
                            }
                            if (targetPerm.contains(userInput)) {
                                autocompletePermission = targetPerm;
                                permSplit[permSplit.length - 1 - i] = "";

                            } else {
                                break;
                            }
                        }
                    } else {
                        autocompletePermission = perm.getName().split("\\.")[0];
                    }

                    while (autocompletePermission.endsWith(".")) {
                        autocompletePermission = autocompletePermission.substring(0,
                                autocompletePermission.length() - 1);
                    }

                    if (!tabcomplete.contains(autocompletePermission)) {
                        tabcomplete.add(autocompletePermission);
                    }
                }
            }
        }

        return tabcomplete;
    }
}