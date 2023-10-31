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
    
    protected String prepareMessage(String langArg, boolean addPluginPrefix) {
        return prepareMessage(langArg, ImmutableMap.of(), addPluginPrefix);
    }

    protected String prepareMessage(String langArg, ImmutableMap<String, @NotNull String> data, boolean addPluginPrefix) {
        String langLine = plugin.getLanguageManager().getFormattedMessage(langArg);
        if (addPluginPrefix) {
            String langPrefix = plugin.getLanguageManager().getFormattedMessage("prefix");
            return PRUtil.powerFormatter(langPrefix + " " + langLine, data, '[', ']');
        } else {
            return PRUtil.powerFormatter(langLine, data, '[', ']');
        }
    }

    protected void sendMessage(CommandSender sender, String langArg, boolean addPluginPrefix) {
        sendMessage(sender, langArg, ImmutableMap.of(), addPluginPrefix);
    }

    protected void sendMessage(CommandSender sender, String langArg, ImmutableMap<String, @NotNull String> data, boolean addPluginPrefix) {
        sender.sendMessage(prepareMessage(langArg, data, addPluginPrefix));
    }

    protected int numTagsStartsWith(String[] tags, String search) {
        int count = 0;
        for (String tag : tags) {
            if (tag.toLowerCase().startsWith(search)) {
                count++;
            }
        }
        return count;
    }

    protected int numTagsEndsWith(String[] tags, String search) {
        int count = 0;
        for (String tag : tags) {
            if (tag.toLowerCase().endsWith(search)) {
                count++;
            }
        }
        return count;
    }
}
