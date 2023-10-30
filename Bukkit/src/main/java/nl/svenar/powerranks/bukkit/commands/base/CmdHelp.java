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
        ArrayList<String> help_messages = new ArrayList<String>();

        LanguageManager languageManager = plugin.getLanguageManager();
        List<String> lines = languageManager.getKeys("commands");

        int lines_per_page = sender instanceof Player ? 3 : 8;
        int last_page = lines.size() / lines_per_page;

        if (!(sender instanceof Player)) {
            page -= 1;
        }

        page = page < 0 ? 0 : page;
        page = page > last_page ? last_page : page;

        if (lines != null) {
            help_messages.add(PowerColor.ChatColor.BLUE + "===" + PowerColor.ChatColor.DARK_AQUA + "----------"
                    + PowerColor.ChatColor.AQUA + getPluginName() + PowerColor.ChatColor.DARK_AQUA + "----------"
                    + PowerColor.ChatColor.BLUE + "===");
            help_messages.add(PowerColor.ChatColor.AQUA + "Page " + PowerColor.ChatColor.BLUE + (page + 1)
                    + PowerColor.ChatColor.AQUA + "/"
                    + PowerColor.ChatColor.BLUE + (last_page + 1) + PowerColor.ChatColor.AQUA + " | Next page " + PowerColor.ChatColor.BLUE + "/" + commandLabel + " help "
                            + PowerColor.ChatColor.BLUE + (page + 2 > last_page + 1 ? last_page + 1 : page + 2));

            int line_index = 0;
            for (String section : lines) {
                if (line_index >= page * lines_per_page && line_index < page * lines_per_page + lines_per_page) {
                    String help_command = section + " " + languageManager.getUnformattedMessage("commands." + section + ".arguments");
                    String help_description = languageManager .getUnformattedMessage("commands." + section + ".description");
                    String help_permission = "powerranks.cmd." + section.toLowerCase();
                    help_messages.add(PowerColor.ChatColor.GREEN + "/" + commandLabel + " " + help_command);
                    help_messages.add(PowerColor.ChatColor.GOLD + "| " + PowerColor.ChatColor.DARK_GREEN + help_permission);
                    help_messages.add(PowerColor.ChatColor.GOLD + "| " + PowerColor.ChatColor.DARK_GREEN + help_description);
                }
                line_index += 1;
            }
        }

        help_messages.add(
                PowerColor.ChatColor.BLUE + "===" + PowerColor.ChatColor.DARK_AQUA + "-----------------------------"
                        + PowerColor.ChatColor.BLUE + "===");

        for (String msg : help_messages) {
            sender.sendMessage(msg);
        }
    }
}