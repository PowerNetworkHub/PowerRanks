package nl.svenar.powerranks.bukkit.commands.base;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.LanguageManager;
import nl.svenar.powerranks.bukkit.commands.PowerBaseCommand;
import nl.svenar.powerranks.common.utils.PowerColor;

@CommandAlias("%powerrankscommand")
@Description("Show available commands and their description")
public class CmdHelp extends PowerBaseCommand {

    private final String commandLabel = "pr";

    public CmdHelp(PowerRanks plugin) {
        super(plugin);
    }

    @Subcommand("help|?")
    @CommandPermission("powerranks.cmd.help")
    @Syntax("[page]")
    public void onHelp(CommandSender sender, @Default("0") int page) {
        int targetPage = page;
        ArrayList<String> helpMessages = new ArrayList<String>();

        LanguageManager languageManager = plugin.getLanguageManager();
        List<String> lines = languageManager.getKeys("commands");

        int linesPerPage = sender instanceof Player ? 3 : 8;
        int lastPage = lines.size() / linesPerPage;

        if (!(sender instanceof Player)) {
            targetPage -= 1;
        }

        targetPage = targetPage < 0 ? 0 : targetPage;
        targetPage = targetPage > lastPage ? lastPage : targetPage;

        if (lines != null) {
            helpMessages.add(PowerColor.ChatColor.BLUE + "===" + PowerColor.ChatColor.DARK_AQUA + "----------"
                    + PowerColor.ChatColor.AQUA + getPluginName() + PowerColor.ChatColor.DARK_AQUA + "----------"
                    + PowerColor.ChatColor.BLUE + "===");
            helpMessages.add(PowerColor.ChatColor.AQUA + "Page " + PowerColor.ChatColor.BLUE + (targetPage + 1)
                    + PowerColor.ChatColor.AQUA + "/"
                    + PowerColor.ChatColor.BLUE + (lastPage + 1) + PowerColor.ChatColor.AQUA + " | Next page " + PowerColor.ChatColor.BLUE + "/" + commandLabel + " help "
                            + PowerColor.ChatColor.BLUE + (targetPage + 2 > lastPage + 1 ? lastPage + 1 : targetPage + 2));

            int lineIndex = 0;
            for (String section : lines) {
                if (lineIndex >= targetPage * linesPerPage && lineIndex < targetPage * linesPerPage + linesPerPage) {
                    String help_command = section + " " + languageManager.getUnformattedMessage("commands." + section + ".arguments");
                    String help_description = languageManager .getUnformattedMessage("commands." + section + ".description");
                    String help_permission = "powerranks.cmd." + section.toLowerCase();
                    helpMessages.add(PowerColor.ChatColor.GREEN + "/" + commandLabel + " " + help_command);
                    helpMessages.add(PowerColor.ChatColor.GOLD + "| " + PowerColor.ChatColor.DARK_GREEN + help_permission);
                    helpMessages.add(PowerColor.ChatColor.GOLD + "| " + PowerColor.ChatColor.DARK_GREEN + help_description);
                }
                lineIndex += 1;
            }
        }

        helpMessages.add(
                PowerColor.ChatColor.BLUE + "===" + PowerColor.ChatColor.DARK_AQUA + "-----------------------------"
                        + PowerColor.ChatColor.BLUE + "===");

        for (String msg : helpMessages) {
            sender.sendMessage(msg);
        }
    }
}