package nl.svenar.powerranks.commands;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class CommandStats extends PowerCommand {

    public CommandStats(PowerRanks plugin, COMMAND_EXECUTOR ce, boolean showInHelp) {
        super(plugin, ce, showInHelp);
    }

    @Override
    public String getArgumentSuggestions() {
        return "";
    }

    @Override
    public String getDescription() {
        return "PowerRanks stats";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("powerranks.command.stats")) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED
                    + plugin.getLangConfig().getNode("plugin.commands.no-permission"));
        }

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Instant current_time = Instant.now();

        sender.sendMessage(this.plugin.getCommandHeader("stats"));
        sender.sendMessage(ChatColor.GREEN + "Server version: " + ChatColor.DARK_GREEN + Bukkit.getVersion() + " | "
                + Bukkit.getServer().getBukkitVersion());
        sender.sendMessage(
                ChatColor.GREEN + "Java version: " + ChatColor.DARK_GREEN + System.getProperty("java.version"));
        sender.sendMessage(ChatColor.GREEN + "Uptime: " + ChatColor.DARK_GREEN
                + format.format(Duration.between(this.plugin.getStartupTime(), current_time).toMillis()));
        sender.sendMessage(ChatColor.GREEN + "PowerRanks Version: " + ChatColor.DARK_GREEN
                + this.plugin.getDescription().getVersion());
        sender.sendMessage(
                ChatColor.GREEN + "Registered ranks: " + ChatColor.DARK_GREEN + BaseDataHandler.getRanks().size());
        sender.sendMessage(
                ChatColor.GREEN + "Registered players: " + ChatColor.DARK_GREEN + BaseDataHandler.getPlayers().size());
        sender.sendMessage(ChatColor.GREEN + "Plugin hooks:");
        sender.sendMessage(ChatColor.GREEN + "- Vault: "
                + (this.plugin.getCoreConfig().pluginhookEnabled("vault") ? ChatColor.DARK_GREEN + "enabled"
                        : ChatColor.DARK_RED + "disabled"));
        sender.sendMessage(ChatColor.GREEN + "- PlaceholderAPI: "
                + (this.plugin.getCoreConfig().pluginhookEnabled("placeholderapi")
                        ? ChatColor.DARK_GREEN + "enabled"
                        : ChatColor.DARK_RED + "disabled"));
        sender.sendMessage(this.plugin.getCommandFooter());

        return false;
    }

    @Override
    public ArrayList<String> tabCompleteEvent(CommandSender sender, String[] args) {
        return new ArrayList<String>();
    }
}