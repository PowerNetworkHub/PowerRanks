package nl.svenar.powerranks.bukkit.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.common.utils.PowerColor;

@CommandAlias("%powerrankscommand")
@Description("Main PowerRanks command")
public class MainCommand extends PowerBaseCommand {

    private List<String> lines = Arrays.asList( //
            "██████  ██████  %name-%version", //
            "██   ██ ██   ██ %author", //
            "██████  ██████  %help", //
            "██      ██   ██ %docs", //
            "██      ██   ██ %donate" //
    );

    private List<String> linesThin = Arrays.asList( //
            "███  ███  %name-%version", //
            "█  █ █  █ %author", //
            "███  ███  %help", //
            "█    █  █ %docs", //
            "█    █  █ %donate" //
    );

    public MainCommand(PowerRanks plugin) {
        super(plugin);
    }

    @Default
    public void onDefault(CommandSender sender) {
        for (String line : sender instanceof Player ? linesThin : lines) {
            if (sender instanceof Player) {
                line = line.replaceAll("█", PowerColor.ChatColor.AQUA + "█");
                line = line.replaceAll(" ", PowerColor.ChatColor.BLACK + "█");
            }
            line = line.replace("%name", PowerColor.ChatColor.GREEN + getPluginName());

            line = line.replace("%version",
                    PowerColor.ChatColor.GREEN + "v" + getPluginVersion());

            line = line.replace("%author", PowerColor.ChatColor.GREEN + "Author"
                    + (this.plugin.getDescription().getAuthors().size() == 1 ? "" : "s") + ": "
                    + PowerColor.ChatColor.DARK_GREEN + String.join(" ", this.plugin.getDescription().getAuthors()));

            line = line.replace("%docs", PowerColor.ChatColor.GREEN + "Docs: " + PowerColor.ChatColor.DARK_GREEN
                    + "https://docs.powerranks.nl");

            line = line.replace("%donate",
                    PowerColor.ChatColor.YELLOW + "Donate: " + PowerColor.ChatColor.GOLD + "https://ko-fi.com/svenar");

            line = line.replace("%help",
                    PowerColor.ChatColor.GREEN + "Available commands: " + PowerColor.ChatColor.DARK_GREEN + "/pr help");

            sender.sendMessage(PowerColor.ChatColor.AQUA + line);
        }
    }
}
