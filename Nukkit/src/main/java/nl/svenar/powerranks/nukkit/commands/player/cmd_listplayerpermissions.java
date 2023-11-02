package nl.svenar.powerranks.nukkit.commands.player;

import java.util.ArrayList;
import java.util.Set;

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

public class cmd_listplayerpermissions extends PowerCommand {

    public cmd_listplayerpermissions(PowerRanks plugin, String command_name, COMMAND_EXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        if (args.length == 1) {
            PRPlayer targetPlayer = PRCache.getPlayer(args[0]);
            if (targetPlayer != null) {
                displayList(sender, targetPlayer, commandLabel, 0);
            } else {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage("general.player-not-found"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("target", args[0])
                                .build(),
                        '[', ']'));
            }
        } else if (args.length == 2) {
            int page = Integer.parseInt(args[1].replaceAll("[a-zA-Z]", ""));
            PRPlayer targetPlayer = PRCache.getPlayer(args[0]);
            if (targetPlayer != null) {
                displayList(sender, targetPlayer, commandLabel, page);
            } else {
                sender.sendMessage(PRUtil.powerFormatter(
                        plugin.getLanguageManager().getFormattedMessage("general.player-not-found"),
                        ImmutableMap.<String, String>builder()
                                .put("player", sender.getName())
                                .put("target", args[0])
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

    private void displayList(CommandSender sender, PRPlayer prPlayer, String commandLabel, int page) {
        int targetPage = page;
        ArrayList<String> outputMessages = new ArrayList<String>();

        outputMessages.add(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "----------" + TextFormat.AQUA
                + plugin.getDescription().getName() + TextFormat.DARK_AQUA + "----------" + TextFormat.BLUE + "===");

        Set<PRPermission> playerPermissions = prPlayer.getPermissions();

        int linesPerPage = sender instanceof Player ? 5 : 10;
        int last_page = playerPermissions.size() / linesPerPage;

        if (!(sender instanceof Player)) {
            targetPage -= 1;
        }

        targetPage = targetPage < 0 ? 0 : targetPage;
        targetPage = targetPage > last_page ? last_page : targetPage;

        if (sender instanceof Player) {
            String pageSelectorTellraw = "tellraw " + sender.getName()
                    + " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + "%next_page%"
                    + "\",\"color\":\"blue\"},{\"text\":\"/\",\"color\":\"aqua\"}"
                    + ",{\"text\":\"%last_page%\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"}"
                    + ",{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
                    + "%commandlabel%" + " listplayerpermissions %playername% " + "%previous_page%"
                    + "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
                    + "%commandlabel%" + " listplayerpermissions %playername% " + "%previous_page%"
                    + "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
                    + "%commandlabel%" + " listplayerpermissions %playername% " + "%previous_page%"
                    + "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
                    + "%commandlabel%" + " listplayerpermissions %playername% " + "%next_page%"
                    + "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
                    + "%commandlabel%" + " listplayerpermissions %playername% " + "%next_page%"
                    + "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
                    + "%commandlabel%" + " listplayerpermissions %playername% " + "%next_page%" + "\"}}]";

            pageSelectorTellraw = pageSelectorTellraw.replaceAll("%next_page%", String.valueOf(targetPage + 1));
            pageSelectorTellraw = pageSelectorTellraw.replaceAll("%previous_page%", String.valueOf(targetPage - 1));
            pageSelectorTellraw = pageSelectorTellraw.replaceAll("%last_page%",
                    String.valueOf(last_page + 1));
            pageSelectorTellraw = pageSelectorTellraw.replaceAll("%playername%", prPlayer.getName());
            pageSelectorTellraw = pageSelectorTellraw.replaceAll("%commandlabel%", commandLabel);

            outputMessages.add(pageSelectorTellraw);

            outputMessages.add(TextFormat.AQUA + prPlayer.getName() + "'s permissions:");

            // sender.sendMessage("[A] " + last_page + " " + linesPerPage);
        } else {
            outputMessages.add(TextFormat.AQUA + "Page " + TextFormat.BLUE + (targetPage + 1) + TextFormat.AQUA + "/"
                    + TextFormat.BLUE + (last_page + 1));
            outputMessages.add(TextFormat.AQUA + "Next page " + TextFormat.BLUE + "/" + commandLabel
                    + " listplayerpermissions " + prPlayer.getName() + " " + TextFormat.BLUE
                    + (targetPage + 2 > last_page + 1 ? last_page + 1 : targetPage + 2));
        }

        int lineIndex = 0;
        for (PRPermission permission : playerPermissions) {
            if (lineIndex >= targetPage * linesPerPage && lineIndex < targetPage * linesPerPage + linesPerPage) {
                outputMessages.add(TextFormat.DARK_GREEN + "#" + (lineIndex + 1) + ". "
                        + (permission.getValue() ? TextFormat.GREEN : TextFormat.RED) + permission.getName());
            }
            lineIndex += 1;
        }

        outputMessages.add(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "------------------------------"
                + TextFormat.BLUE + "===");

        if (plugin != null) {
            for (String msg : outputMessages) {
                if (msg.startsWith("tellraw")) {
                    plugin.getServer().dispatchCommand((CommandSender) plugin.getServer().getConsoleSender(), msg);
                } else {
                    sender.sendMessage(msg);
                }
            }
        }
    }

    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        ArrayList<String> tabcomplete = new ArrayList<String>();

        if (args.length == 1) {
            for (PRPlayer prPlayer : PRCache.getPlayers()) {
                tabcomplete.add(prPlayer.getName());
            }
        }

        return tabcomplete;
    }
}
