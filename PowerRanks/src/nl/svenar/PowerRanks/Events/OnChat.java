package nl.svenar.PowerRanks.Events;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;

public class OnChat implements Listener {
	
	PowerRanks m;
	public OnChat(PowerRanks m){
		this.m = m;
	}
		
	@EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent e) {
        final File configFile = new File(String.valueOf(PowerRanks.configFileLoc) + "config" + ".yml");
        final File rankFile = new File(String.valueOf(PowerRanks.fileLoc) + "Ranks" + ".yml");
        final File playerFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
        final YamlConfiguration configYaml = new YamlConfiguration();
        final YamlConfiguration rankYaml = new YamlConfiguration();
        final YamlConfiguration playerYaml = new YamlConfiguration();
        final Player player = e.getPlayer();
        try {
            configYaml.load(configFile);
            rankYaml.load(rankFile);
            playerYaml.load(playerFile);
            final String rank = playerYaml.getString("players." + player.getUniqueId() + ".rank");
            String format = configYaml.getString("chat.format");
            final String prefix = (rankYaml.getString("Groups." + rank + ".chat.prefix") != null) ? rankYaml.getString("Groups." + rank + ".chat.prefix") : "";
            final String suffix = (rankYaml.getString("Groups." + rank + ".chat.suffix") != null) ? rankYaml.getString("Groups." + rank + ".chat.suffix") : "";
            final String chatColor = (rankYaml.getString("Groups." + rank + ".chat.chatColor") != null) ? rankYaml.getString("Groups." + rank + ".chat.chatColor") : "";
            final String nameColor = (rankYaml.getString("Groups." + rank + ".chat.nameColor") != null) ? rankYaml.getString("Groups." + rank + ".chat.nameColor") : "";
            format = Util.replaceAll(format, " ", "");
            format = Util.replaceAll(format, "[prefix]", PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), prefix, true) + ChatColor.RESET + (prefix.length() > 0 ? " " : ""));
            format = Util.replaceAll(format, "[suffix]", (suffix.length() > 0) ? (" " + PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), suffix, true)) : PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), suffix, true));
            format = Util.replaceAll(format, "[player]", String.valueOf(nameColor) + "%1$s");
            format = Util.replaceAll(format, "[msg]", String.valueOf(chatColor) + " %2$s");
            format = Util.replaceAll(format, "[format]", e.getFormat());
            format = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), format, false);
//            format = format.replaceAll("^[ ]+", ""); // Remove leading spaces
            if (configYaml.getBoolean("chat.enabled")) {
                e.setFormat(format);
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            e.setFormat("%1$s: %2$s");
        }
    }
}
