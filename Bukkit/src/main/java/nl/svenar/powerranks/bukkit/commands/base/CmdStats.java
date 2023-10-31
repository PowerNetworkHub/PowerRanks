package nl.svenar.powerranks.bukkit.commands.base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.addons.AddonsManager;
import nl.svenar.powerranks.bukkit.commands.PowerBaseCommand;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PowerColor;

@CommandAlias("%powerrankscommand")
@Description("Show stats about PowerRanks")
public class CmdStats extends PowerBaseCommand {

    public CmdStats(PowerRanks plugin) {
        super(plugin);
    }

    @Subcommand("stats")
    @CommandPermission("powerranks.cmd.stats")
    public void onStats(CommandSender sender) {
        messageStats(sender);
    }

    private void messageStats(CommandSender sender) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        int addonCount = 0;
        for (Entry<File, Boolean> prAddon : AddonsManager.loadedAddons.entrySet()) {
            if (prAddon.getValue() == true)
                addonCount++;
        }
        Instant current_time = Instant.now();

        sendMessage(sender, "list-header", false);
        sender.sendMessage(PowerColor.ChatColor.GREEN + "Server version: " + PowerColor.ChatColor.DARK_GREEN
                + Bukkit.getVersion() + " | "
                + Bukkit.getServer().getBukkitVersion());
        sender.sendMessage(
                PowerColor.ChatColor.GREEN + "Java version: " + PowerColor.ChatColor.DARK_GREEN
                        + System.getProperty("java.version"));
        sender.sendMessage(PowerColor.ChatColor.GREEN + "Storage method: " + PowerColor.ChatColor.DARK_GREEN
                + PowerRanks.getConfigManager().getString("storage.type", "yaml").toUpperCase());
        sender.sendMessage(PowerColor.ChatColor.GREEN + "Uptime: " + PowerColor.ChatColor.DARK_GREEN
                + format.format(Duration.between(PowerRanks.powerranks_start_time, current_time).toMillis()));
        sender.sendMessage(
                PowerColor.ChatColor.GREEN + "PowerRanks Version: " + PowerColor.ChatColor.DARK_GREEN
                        + PowerRanks.pdf.getVersion());
        sender.sendMessage(PowerColor.ChatColor.GREEN + "Registered ranks: " + PowerColor.ChatColor.DARK_GREEN
                + PRCache.getRanks().size());
        sender.sendMessage(
                PowerColor.ChatColor.GREEN + "Registered players: " + PowerColor.ChatColor.DARK_GREEN
                        + PRCache.getPlayers().size());
        sender.sendMessage(
                PowerColor.ChatColor.GREEN + "Registered addons: " + PowerColor.ChatColor.DARK_GREEN + addonCount);

        boolean hex_color_supported = false;
        try {
            "#FF0000a".replace("#FF0000", net.md_5.bungee.api.ChatColor.of("#FF0000") + "");
            hex_color_supported = true;
        } catch (Exception | NoSuchMethodError e) {
            hex_color_supported = false;
        }
        sender.sendMessage(PowerColor.ChatColor.GREEN + "RGB colors: "
                + (hex_color_supported ? PowerColor.ChatColor.DARK_GREEN + "" : PowerColor.ChatColor.DARK_RED + "un")
                + "supported");
        sender.sendMessage(PowerColor.ChatColor.GREEN + "Bungeecord: "
                + (PowerRanks.getInstance().getBungeecordManager().isReady()
                        ? PowerColor.ChatColor.DARK_GREEN + "enabled"
                        : PowerColor.ChatColor.DARK_RED + "disabled"));
        sender.sendMessage(PowerColor.ChatColor.GREEN + "- Connected servers: "
                + PowerColor.ChatColor.DARK_GREEN + PowerRanks.getInstance().getBungeecordManager().getServerCount());

        sender.sendMessage(PowerColor.ChatColor.GREEN + "Plugin hooks:");
        sender.sendMessage(PowerColor.ChatColor.GREEN + "- Vault Economy: "
                + (PowerRanks.vaultEconomyEnabled ? PowerColor.ChatColor.DARK_GREEN + "enabled"
                        : PowerColor.ChatColor.DARK_RED + "disabled"));
        sender.sendMessage(PowerColor.ChatColor.GREEN + "- Vault Permissions: "
                + (PowerRanks.vaultPermissionsEnabled ? PowerColor.ChatColor.DARK_GREEN + "enabled"
                        : PowerColor.ChatColor.DARK_RED + "disabled"));
        sender.sendMessage(PowerColor.ChatColor.GREEN + "- PlaceholderAPI: "
                + (PowerRanks.getPlaceholderapiExpansion() != null ? PowerColor.ChatColor.DARK_GREEN + "enabled"
                        : PowerColor.ChatColor.DARK_RED + "disabled"));
        sender.sendMessage(PowerColor.ChatColor.GREEN + "- DeluxeTags: "
                + (PowerRanks.plugin_hook_deluxetags ? PowerColor.ChatColor.DARK_GREEN + "enabled"
                        : PowerColor.ChatColor.DARK_RED + "disabled"));
        sender.sendMessage(PowerColor.ChatColor.GREEN + "- NametagEdit: "
                + (PowerRanks.plugin_hook_nametagedit ? PowerColor.ChatColor.DARK_GREEN + "enabled"
                        : PowerColor.ChatColor.DARK_RED + "disabled"));

        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        String pluginNames = "";
        for (Plugin plugin : plugins) {
            pluginNames += plugin.getName() + "(" + plugin.getDescription().getVersion() + "), ";
        }
        pluginNames = pluginNames.substring(0, pluginNames.length() - 2);

        sender.sendMessage(PowerColor.ChatColor.GREEN + "Plugins (" + plugins.length + "): "
                + PowerColor.ChatColor.DARK_GREEN + pluginNames);

        sendMessage(sender, "list-footer", false);
    }
}