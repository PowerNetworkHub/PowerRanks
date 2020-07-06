package nl.svenar.PowerRanks.Events;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.collect.ImmutableMap;

import me.clip.deluxetags.DeluxeTag;
import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;

public class OnChat implements Listener {

	PowerRanks m;

	public OnChat(PowerRanks m) {
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
		final String uuid = player.getUniqueId().toString();

		try {
			configYaml.load(configFile);
			rankYaml.load(rankFile);
			playerYaml.load(playerFile);
			final String rank = playerYaml.getString("players." + player.getUniqueId() + ".rank");
			String format = configYaml.getString("chat.format");
			String prefix = (rankYaml.getString("Groups." + rank + ".chat.prefix") != null) ? rankYaml.getString("Groups." + rank + ".chat.prefix") : "";
			String suffix = (rankYaml.getString("Groups." + rank + ".chat.suffix") != null) ? rankYaml.getString("Groups." + rank + ".chat.suffix") : "";
			final String chatColor = (rankYaml.getString("Groups." + rank + ".chat.chatColor") != null) ? rankYaml.getString("Groups." + rank + ".chat.chatColor") : "";
			final String nameColor = (rankYaml.getString("Groups." + rank + ".chat.nameColor") != null) ? rankYaml.getString("Groups." + rank + ".chat.nameColor") : "";
			String subprefix = "";
			String subsuffix = "";
			String usertag = "";

			try {
				if (playerYaml.getConfigurationSection("players." + uuid + ".subranks") != null) {
					ConfigurationSection subranks = playerYaml.getConfigurationSection("players." + uuid + ".subranks");
					for (String r : subranks.getKeys(false)) {
						if (playerYaml.getBoolean("players." + uuid + ".subranks." + r + ".use_prefix")) {
							subprefix += (rankYaml.getString("Groups." + r + ".chat.prefix") != null && rankYaml.getString("Groups." + r + ".chat.prefix").length() > 0
									? ChatColor.RESET + rankYaml.getString("Groups." + r + ".chat.prefix")
									: "");
						}

						if (playerYaml.getBoolean("players." + uuid + ".subranks." + r + ".use_suffix")) {
							subsuffix += (rankYaml.getString("Groups." + r + ".chat.suffix") != null && rankYaml.getString("Groups." + r + ".chat.suffix").length() > 0
									? ChatColor.RESET + rankYaml.getString("Groups." + r + ".chat.suffix")
									: "");

						}
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			subprefix = subprefix.trim();
			subsuffix = subsuffix.trim();

			if (subsuffix.endsWith(" ")) {
				subsuffix = subsuffix.substring(0, subsuffix.length() - 1);
			}

			if (subsuffix.replaceAll(" ", "").length() == 0) {
				subsuffix = "";
			}
			
			if (playerYaml.isSet("players." + uuid + ".usertag") && playerYaml.getString("players." + uuid + ".usertag").length() > 0) {
				String tmp_usertag = playerYaml.getString("players." + uuid + ".usertag");

				if (rankYaml.getConfigurationSection("Usertags") != null) {
					ConfigurationSection tags = rankYaml.getConfigurationSection("Usertags");
					for (String key : tags.getKeys(false)) {
						if (key.equalsIgnoreCase(tmp_usertag)) {
							usertag = rankYaml.getString("Usertags." + key) + ChatColor.RESET;
							break;
						}
					}
				}
			}
			
			format = Util.powerFormatter(format, 
					ImmutableMap.<String, String>builder()
				    .put("prefix", prefix)
				    .put("suffix", suffix)
				    .put("subprefix", subprefix)
				    .put("subsuffix", subsuffix)
				    .put("usertag", !PowerRanks.plugin_hook_deluxetags ? usertag : DeluxeTag.getPlayerDisplayTag(player))
				    .put("player", nameColor + "%1$s")
				    .put("msg", chatColor + "%2$s")
				    .put("format", e.getFormat())
				    .build()
					, '[', ']');

			format = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), format, true);
			this.m.updateTablistName(player, prefix, suffix, subprefix, subsuffix, !PowerRanks.plugin_hook_deluxetags ? usertag : DeluxeTag.getPlayerDisplayTag(player), nameColor); // TODO: Remove (DeluxeTags workaround)
			
			if (configYaml.getBoolean("chat.enabled")) {
				e.setFormat(format);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			e.setFormat("%1$s: %2$s");
		}
	}
}