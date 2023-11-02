package nl.svenar.powerranks.nukkit.commands.core;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import nl.svenar.powerranks.nukkit.PowerRanks;
import nl.svenar.powerranks.nukkit.commands.PowerCommand;
import nl.svenar.powerranks.nukkit.manager.LanguageManager;

public class CmdHelp extends PowerCommand {

    public CmdHelp(PowerRanks plugin, String command_name, COMMANDEXECUTOR ce) {
        super(plugin, command_name, ce);
        this.setCommandPermission("powerranks.cmd." + command_name.toLowerCase());
    }

    @Override
    public boolean onCommand(CommandSender sender, String commandLabel, String commandName, String[] args) {
        int targetPage = 0;
        ArrayList<String> helpMessages = new ArrayList<String>();

        LanguageManager languageManager = plugin.getLanguageManager();
        List<String> lines = languageManager.getKeys("commands");

        int linesPerPage = sender instanceof Player ? 5 : 10;
        int lastPage = lines.size() / linesPerPage;

        if (args.length > 0) {
            targetPage = args[0].replaceAll("[a-zA-Z]", "").length() > 0
                    ? Integer.parseInt(args[0].replaceAll("[a-zA-Z]", ""))
                    : 0;

            if (!(sender instanceof Player)) {
                targetPage -= 1;
            }
        }

        targetPage = targetPage < 0 ? 0 : targetPage;
        targetPage = targetPage > lastPage ? lastPage : targetPage;

        if (lines != null) {
            helpMessages.add(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "----------" + TextFormat.AQUA
                    + plugin.getDescription().getName() + TextFormat.DARK_AQUA + "----------" + TextFormat.BLUE
                    + "===");
            helpMessages.add(TextFormat.AQUA + "Page " + TextFormat.BLUE + (targetPage + 1) + TextFormat.AQUA + "/"
                    + TextFormat.BLUE + (lastPage + 1));
            helpMessages.add(TextFormat.AQUA + "Next page " + TextFormat.BLUE + "/" + commandLabel + " help "
                    + TextFormat.BLUE + (targetPage + 2 > lastPage + 1 ? lastPage + 1 : targetPage + 2));

            int lineIndex = 0;
            for (String section : lines) {
                if (lineIndex >= targetPage * linesPerPage && lineIndex < targetPage * linesPerPage + linesPerPage) {
                    String help_command = section + " "
                            + languageManager.getUnformattedMessage("commands." + section + ".arguments");
                    String help_description = languageManager
                            .getUnformattedMessage("commands." + section + ".description");
                    helpMessages.add(TextFormat.BLACK + "[" + TextFormat.GREEN + "/" + commandLabel + " "
                            + help_command + TextFormat.BLACK + "] " + TextFormat.DARK_GREEN + help_description);
                }
                lineIndex += 1;
            }
        }

        helpMessages.add(TextFormat.BLUE + "===" + TextFormat.DARK_AQUA + "-----------------------------"
                + TextFormat.BLUE + "===");

        if (plugin != null)
            for (String msg : helpMessages)
                sender.sendMessage(msg);

        return false;
    }

    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        ArrayList<String> tabcomplete = new ArrayList<String>();
        return tabcomplete;
    }
}
