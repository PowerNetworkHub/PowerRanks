package nl.svenar.PowerRanks.Events;

import java.io.File;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import nl.svenar.PowerRanks.Main;

public class OnChat implements Listener {
	
	Main m;
	public OnChat(Main m){
		this.m = m;
	}
		
	@EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent e) {
        final File configFile = new File(String.valueOf(this.m.configFileLoc) + "config" + ".yml");
        final File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
        final File playerFile = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
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
            format = replaceAll(format, "[prefix]", prefix);
            format = replaceAll(format, " [suffix]", (suffix.length() > 0) ? (" " + suffix) : suffix);
            format = replaceAll(format, "[player]", String.valueOf(nameColor) + "%1$s");
            format = replaceAll(format, "[msg]", String.valueOf(chatColor) + "%2$s");
            format = replaceAll(format, "[format]", e.getFormat());
            format = this.m.chatColor(this.m.colorChar.charAt(0), format);
            if (configYaml.getBoolean("chat.enabled")) {
                e.setFormat(format);
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            e.setFormat("%1$s: %2$s");
        }
    }
    
    public static String replaceAll(String source, final String key, final String value) {
        final String[] split = source.split(Pattern.quote(key));
        final StringBuilder builder = new StringBuilder();
        builder.append(split[0]);
        for (int i = 1; i < split.length; ++i) {
            builder.append(value);
            builder.append(split[i]);
        }
        while (source.endsWith(key)) {
            builder.append(value);
            source = source.substring(0, source.length() - key.length());
        }
        return builder.toString();
    }
}
