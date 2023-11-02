package nl.svenar.powerranks.nukkit.commands.player;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import cn.nukkit.utils.TextFormat;

import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.nukkit.util.Util;

public class cmd_haspermission extends PowerCommand {

    public cmd_haspermission(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        PRPlayer prPlayer = null;
        String permissionNode = null;

        if (args.length == 2) {
            prPlayer = PRCache.getPlayer(args[0]);
            permissionNode = args[1];

            if (prPlayer == null) {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage("general.player-not-found"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("target", args[0])
                                .build(),
                        '[', ']'));
            }

            if (permissionNode == null || permissionNode.length() == 0) {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage("general.permission-not-found"),
                        ImmutableMap.<String, String>builder()
                                .put("permission", args[1])
                                .build(),
                        '[', ']'));
            }
        } else {
            sender.sendMessage(
                    plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
                            "commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
        }

        if (prPlayer != null && permissionNode != null && permissionNode.length() > 0) {
            Player player = Util.getPlayerByName(prPlayer.getName());
            if (player != null) {
                List<PRPermission> playerPermissions = prPlayer.getEffectivePermissions();
                PRPermission targetPermission = null;
                PRPermission targetWildcardPermission = null;

                for (PRPermission permission : playerPermissions) {
                    if (permission.getName().equals(permissionNode)) {
                        targetPermission = permission;
                        break;
                    }
                }

                ArrayList<String> wildcardPermissions = PRUtil.generateWildcardList(permissionNode);
                for (PRPermission perm : playerPermissions) {

                    if (wildcardPermissions.contains(perm.getName())) {
                        targetWildcardPermission = perm;
                        break;
                    }
                }

                sender.sendMessage(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "----------" + TextFormat.AQUA
                        + PowerRanks.getInstance().getDescription().getName() + TextFormat.DARK_AQUA + "----------"
                        + TextFormat.BLUE + "===");
                sender.sendMessage(TextFormat.GREEN + "Player: " + TextFormat.DARK_GREEN + player.getName());
                sender.sendMessage(TextFormat.GREEN + "Permission: " + TextFormat.DARK_GREEN + permissionNode);
                sender.sendMessage(TextFormat.GREEN + "Player has permission: "
                        + (targetPermission == null ? TextFormat.DARK_RED + "no" : TextFormat.DARK_GREEN + "yes"));
                sender.sendMessage(TextFormat.GREEN + "Permission allowed value: "
                        + (targetPermission == null ? TextFormat.GOLD + "unknown"
                                : (targetPermission.getValue() ? TextFormat.DARK_GREEN + "allowed"
                                        : TextFormat.DARK_RED + "denied")));
                sender.sendMessage(TextFormat.GREEN + "has Wildcard variant: "
                        + (targetWildcardPermission == null ? TextFormat.GOLD + "no"
                                : TextFormat.DARK_GREEN + targetWildcardPermission.getName() + " (allowed: "
                                        + (targetWildcardPermission.getValue() ? TextFormat.DARK_GREEN + "yes"
                                                : TextFormat.DARK_RED + "no")
                                        + TextFormat.DARK_GREEN + ")"));
                sender.sendMessage(TextFormat.GREEN + "Is permission allowed: "
                        + (player.hasPermission(permissionNode) ? TextFormat.DARK_GREEN + "yes"
                                : TextFormat.DARK_RED + "no"));
                sender.sendMessage(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "------------------------------"
                        + TextFormat.BLUE + "===");
            } else {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage("general.player-not-online"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .build(),
                        '[', ']'));
            }
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
            // for (Permission pai : Nukkit.getServer().getPermissions()) {
            for (String perm : plugin.getPermissionRegistry().getPermissions()) {
                String userInput = args[1];
                String autocompletePermission = "";

                if (userInput.contains(".")) {
                    String[] permSplit = perm.split("\\.");
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
                    autocompletePermission = perm.split("\\.")[0];
                }

                while (autocompletePermission.endsWith(".")) {
                    autocompletePermission = autocompletePermission.substring(0, autocompletePermission.length() - 1);
                }

                if (!tabcomplete.contains(autocompletePermission)) {
                    tabcomplete.add(autocompletePermission);
                }
            }
        }

        return tabcomplete;
    }
}
