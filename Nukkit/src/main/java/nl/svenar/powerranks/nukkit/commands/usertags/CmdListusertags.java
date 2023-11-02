package nl.svenar.powerranks.nukkit.commands.usertags;

import java.util.ArrayList;
import java.util.Set;

import cn.nukkit.utils.TextFormat;

import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;
import nl.svenar.powerranks.common.utils.PowerColor;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;

public class CmdListusertags extends PowerCommand {

    public CmdListusertags(PowerRanks plugin, String command_name, COMMANDEXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        if (args.length == 0) {
            displayList(sender, commandLabel, 0);
        } else if (args.length == 1) {
            int page = Integer.parseInt(args[0].replaceAll("[a-zA-Z]", ""));
            displayList(sender, commandLabel, page);
        } else {
            sender.sendMessage(
                    plugin.getLanguageManager().getFormattedUsageMessage(commandLabel, commandName,
                            "commands." + commandName.toLowerCase() + ".arguments", sender instanceof Player));
        }
        return false;
    }

    private void displayList(CommandSender sender, String commandLabel, int page) {
        int targetPage = page;
        ArrayList<String> outputMessages = new ArrayList<String>();

        outputMessages.add(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "----------" + TextFormat.AQUA
                + plugin.getDescription().getName() + TextFormat.DARK_AQUA + "----------" + TextFormat.BLUE + "===");

        Set<String> items = plugin.getUsertagManager().getUsertags();

        int linesPerPage = sender instanceof Player ? 5 : 10;
        int lastPage = items.size() / linesPerPage;

        if (!(sender instanceof Player)) {
            targetPage -= 1;
        }

        targetPage = targetPage < 0 ? 0 : targetPage;
        targetPage = targetPage > lastPage ? lastPage : targetPage;

        if (sender instanceof Player) {
            String pageSelectorTellraw = "tellraw " + sender.getName()
                    + " [\"\",{\"text\":\"Page \",\"color\":\"aqua\"},{\"text\":\"" + "%next_page%"
                    + "\",\"color\":\"blue\"},{\"text\":\"/\",\"color\":\"aqua\"}"
                    + ",{\"text\":\"%lastPage%\",\"color\":\"blue\"},{\"text\":\": \",\"color\":\"aqua\"}"
                    + ",{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
                    + "%commandlabel%" + " listusertags " + "%previous_page%"
                    + "\"}},{\"text\":\"<\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
                    + "%commandlabel%" + " listusertags " + "%previous_page%"
                    + "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
                    + "%commandlabel%" + " listusertags " + "%previous_page%"
                    + "\"}},{\"text\":\" \",\"color\":\"aqua\"},{\"text\":\"[\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
                    + "%commandlabel%" + " listusertags" + "%next_page%"
                    + "\"}},{\"text\":\">\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
                    + "%commandlabel%" + " listusertags " + "%next_page%"
                    + "\"}},{\"text\":\"]\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/"
                    + "%commandlabel%" + " listusertags " + "%next_page%" + "\"}}]";

            pageSelectorTellraw = pageSelectorTellraw.replaceAll("%next_page%", String.valueOf(targetPage + 1));
            pageSelectorTellraw = pageSelectorTellraw.replaceAll("%previous_page%", String.valueOf(targetPage - 1));
            pageSelectorTellraw = pageSelectorTellraw.replaceAll("%lastPage%",
                    String.valueOf(lastPage + 1));
            pageSelectorTellraw = pageSelectorTellraw.replaceAll("%commandlabel%", commandLabel);

            outputMessages.add(pageSelectorTellraw);

            outputMessages.add(TextFormat.AQUA + "Usertags:");

            // sender.sendMessage("[A] " + lastPage + " " + linesPerPage);
        } else {
            outputMessages.add(TextFormat.AQUA + "Page " + TextFormat.BLUE + (targetPage + 1) + TextFormat.AQUA + "/"
                    + TextFormat.BLUE + (lastPage + 1));
            outputMessages.add(TextFormat.AQUA + "Next page " + TextFormat.BLUE + "/" + commandLabel + " listusertags "
                    + " " + TextFormat.BLUE + (targetPage + 2 > lastPage + 1 ? lastPage + 1 : targetPage + 2));
        }

        int lineIndex = 0;
        for (String item : items) {
            if (lineIndex >= targetPage * linesPerPage && lineIndex < targetPage * linesPerPage + linesPerPage) {
                String usertagValue = plugin.getUsertagManager().getUsertagValue(item);
                outputMessages
                        .add(TextFormat.DARK_GREEN + "#" + (lineIndex + 1) + ". " + TextFormat.GREEN + item + " "
                                + TextFormat.RESET + plugin.getPowerColor().format(PowerColor.UNFORMATTED_COLOR_CHAR, usertagValue, true, false, false));
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
        return tabcomplete;
    }
}
