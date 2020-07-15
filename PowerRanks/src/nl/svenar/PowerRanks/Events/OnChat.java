package nl.svenar.PowerRanks.Events;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.collect.ImmutableMap;

import me.clip.deluxetags.DeluxeTag;
import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;
import nl.svenar.PowerRanks.addons.PowerRanksPlayer;

public class OnChat implements Listener {

	PowerRanks m;

	public OnChat(PowerRanks m) {
		this.m = m;
	}

	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent e) {
//		final File configFile = new File(String.valueOf(PowerRanks.configFileLoc) + "config" + ".yml");
//		final File rankFile = new File(String.valueOf(PowerRanks.fileLoc) + "Ranks" + ".yml");
//		final File playerFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
//		final YamlConfiguration configYaml = new YamlConfiguration();
//		final YamlConfiguration rankYaml = new YamlConfiguration();
//		final YamlConfiguration playerYaml = new YamlConfiguration();
		final Player player = e.getPlayer();
		final String uuid = player.getUniqueId().toString();

		try {
//			configYaml.load(configFile);
			if (CachedConfig.getBoolean("chat.enabled")) {
//				rankYaml.load(rankFile);
//				playerYaml.load(playerFile);
				final String rank = CachedPlayers.getString("players." + player.getUniqueId() + ".rank");
				String format = CachedConfig.getString("chat.format");
				String prefix = (CachedRanks.getString("Groups." + rank + ".chat.prefix") != null) ? CachedRanks.getString("Groups." + rank + ".chat.prefix") : "";
				String suffix = (CachedRanks.getString("Groups." + rank + ".chat.suffix") != null) ? CachedRanks.getString("Groups." + rank + ".chat.suffix") : "";
				final String chatColor = (CachedRanks.getString("Groups." + rank + ".chat.chatColor") != null) ? CachedRanks.getString("Groups." + rank + ".chat.chatColor") : "";
				final String nameColor = (CachedRanks.getString("Groups." + rank + ".chat.nameColor") != null) ? CachedRanks.getString("Groups." + rank + ".chat.nameColor") : "";
				String subprefix = "";
				String subsuffix = "";
				String usertag = "";

				try {
					if (CachedPlayers.getConfigurationSection("players." + uuid + ".subranks") != null) {
						ConfigurationSection subranks = CachedPlayers.getConfigurationSection("players." + uuid + ".subranks");
						for (String r : subranks.getKeys(false)) {
							boolean in_world = false;
							if (!CachedPlayers.contains("players." + uuid + ".subranks." + r + ".worlds")) {
								in_world = true;

								ArrayList<String> default_worlds = new ArrayList<String>();
								default_worlds.add("All");
								CachedPlayers.set("players." + uuid + ".subranks." + r + ".worlds", default_worlds);
							}

							String player_current_world = player.getWorld().getName();
							List<String> worlds = CachedPlayers.getStringList("players." + uuid + ".subranks." + r + ".worlds");
							for (String world : worlds) {
								if (player_current_world.equalsIgnoreCase(world) || world.equalsIgnoreCase("all")) {
									in_world = true;
								}
							}

							if (in_world) {
								if (CachedPlayers.getBoolean("players." + uuid + ".subranks." + r + ".use_prefix")) {
									subprefix += (CachedRanks.getString("Groups." + r + ".chat.prefix") != null && CachedRanks.getString("Groups." + r + ".chat.prefix").length() > 0
											? ChatColor.RESET + CachedRanks.getString("Groups." + r + ".chat.prefix") + " "
											: "");
								}

								if (CachedPlayers.getBoolean("players." + uuid + ".subranks." + r + ".use_suffix")) {
									subsuffix += (CachedRanks.getString("Groups." + r + ".chat.suffix") != null && CachedRanks.getString("Groups." + r + ".chat.suffix").length() > 0
											? ChatColor.RESET + CachedRanks.getString("Groups." + r + ".chat.suffix") + " "
											: "");

								}
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

				if (CachedPlayers.contains("players." + uuid + ".usertag") && CachedPlayers.getString("players." + uuid + ".usertag").length() > 0) {
					String tmp_usertag = CachedPlayers.getString("players." + uuid + ".usertag");

					if (CachedRanks.getConfigurationSection("Usertags") != null) {
						ConfigurationSection tags = CachedRanks.getConfigurationSection("Usertags");
						for (String key : tags.getKeys(false)) {
							if (key.equalsIgnoreCase(tmp_usertag)) {
								usertag = CachedRanks.getString("Usertags." + key) + ChatColor.RESET;
								break;
							}
						}
					}
				}

				format = Util.powerFormatter(format,
						ImmutableMap.<String, String>builder().put("prefix", prefix).put("suffix", suffix).put("subprefix", subprefix).put("subsuffix", subsuffix)
								.put("usertag", !PowerRanks.plugin_hook_deluxetags ? usertag : DeluxeTag.getPlayerDisplayTag(player)).put("player", nameColor + "%1$s").put("msg", chatColor + "%2$s").put("format", e.getFormat()).put("world", player.getWorld().getName().replace("world_nether", "Nether").replace("world_the_end", "End")).build(),
						'[', ']');

				format = PowerRanks.chatColor(PowerRanks.colorChar.charAt(0), format, true);
				this.m.updateTablistName(player, prefix, suffix, subprefix, subsuffix, !PowerRanks.plugin_hook_deluxetags ? usertag : DeluxeTag.getPlayerDisplayTag(player), nameColor); // TODO: Remove (DeluxeTags workaround)

				for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
					PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
					format = prAddon.getValue().onPlayerChat(prPlayer, format, e.getMessage());
				}

				e.setFormat(format);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			e.setFormat("%1$s: %2$s");
		}
	}
}