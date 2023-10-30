package nl.svenar.powerranks.bukkit.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;

import co.aikar.commands.BaseCommand;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.utils.PRUtil;

public class PowerBaseCommand extends BaseCommand {
    
    protected PowerRanks plugin;

    public PowerBaseCommand(PowerRanks plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the name of the plugin
     * @return String
     */
    protected String getPluginName() {
        return plugin.getDescription().getName();
    }

    /**
     * Get the version of the plugin
     * @return String
     */
    protected String getPluginVersion() {
        return plugin.getDescription().getVersion();
    }

    /**
     * Get a PRPlayer object from the cache
     * @param identifier
     * @return PRPlayer
     */
    protected PRPlayer getPRPlayer(String identifier) {
        return PRCache.getPlayer(identifier);
    }

    /**
     * Get a PRPlayer object from the cache
     * @param player
     * @return PRPlayer
     */
    protected PRPlayer getPRPlayer(Player player) {
        return getPRPlayer(player.getUniqueId().toString());
    }

    protected PRRank getPRRank(String identifier) {
        return PRCache.getRank(identifier);
    }

    protected void sendMessage(CommandSender sender, String langArg, ImmutableMap<String, @NotNull String> data) {
        String langLine = plugin.getLanguageManager().getFormattedMessage(langArg);
        String output = PRUtil.powerFormatter(langLine, data, '[', ']');
        sender.sendMessage(output);
    }
}
