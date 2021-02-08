package nl.svenar.PowerRanks.Events;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.collect.ImmutableMap;

import me.clip.deluxetags.DeluxeTag;
import me.clip.placeholderapi.PlaceholderAPI;
import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.Data.PowerRanksChatColor;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;
import nl.svenar.PowerRanks.addons.PowerRanksPlayer;

public class OnChat implements Listener {

	PowerRanks m;

	public OnChat(PowerRanks m) {
		this.m = m;
	}

	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent e) {
		final Player player = e.getPlayer();
		final String uuid = player.getUniqueId().toString();

		try {
			if (CachedConfig.getBoolean("chat.enabled")) {
				final String rank = CachedPlayers.getString("players." + player.getUniqueId() + ".rank");
				String format = CachedConfig.getString("chat.format");
				String prefix = (CachedRanks.getString("Groups." + rank + ".chat.prefix") != null) ? CachedRanks.getString("Groups." + rank + ".chat.prefix") : "";
				String suffix = (CachedRanks.getString("Groups." + rank + ".chat.suffix") != null) ? CachedRanks.getString("Groups." + rank + ".chat.suffix") : "";
				String chatColor = (CachedRanks.getString("Groups." + rank + ".chat.chatColor") != null) ? CachedRanks.getString("Groups." + rank + ".chat.chatColor") : "";
				String nameColor = (CachedRanks.getString("Groups." + rank + ".chat.nameColor") != null) ? CachedRanks.getString("Groups." + rank + ".chat.nameColor") : "";
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
								CachedPlayers.set("players." + uuid + ".subranks." + r + ".worlds", default_worlds, true);
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
				
				// nameColor = nameColor.replaceAll("&i", "").replaceAll("&I", "").replaceAll("&j", "").replaceAll("&J", "");
				// chatColor = chatColor.replaceAll("&i", "").replaceAll("&I", "").replaceAll("&j", "").replaceAll("&J", "");
				// nameColor = "&r" + nameColor;
				// chatColor = "&r" + chatColor;

				String player_formatted_name = (nameColor.length() == 0 ? "&r" : "") + applyColor(nameColor, player.getDisplayName());
				String player_formatted_chat_msg = (chatColor.length() == 0 ? "&r" : "") + applyColor(chatColor, e.getMessage());

				format = Util.powerFormatter(format,
				ImmutableMap.<String, String>builder()
					.put("prefix", prefix)
					.put("suffix", suffix)
					.put("subprefix", subprefix)
					.put("subsuffix", subsuffix)
					.put("usertag", !PowerRanks.plugin_hook_deluxetags ? usertag : DeluxeTag.getPlayerDisplayTag(player))
					.put("player", player_formatted_name)
					.put("msg", player_formatted_chat_msg)
					.put("format", e.getFormat())
					.put("world", player.getWorld().getName()).build(),
				'[', ']');
				
				if (PowerRanks.placeholderapiExpansion != null) {
					format = PlaceholderAPI.setPlaceholders(player, format).replaceAll("" + ChatColor.COLOR_CHAR, "" + PowerRanksChatColor.unformatted_default_char);
				}

				for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
					PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
					format = prAddon.getValue().onPlayerChat(prPlayer, format, e.getMessage());
				}

				format = PowerRanks.chatColor(format, true);


				this.m.updateTablistName(player, prefix, suffix, subprefix, subsuffix, !PowerRanks.plugin_hook_deluxetags ? usertag : DeluxeTag.getPlayerDisplayTag(player), nameColor, true); // TODO: Remove (DeluxeTags workaround)

				
				e.setFormat(format);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			e.setFormat("%1$s: %2$s");
		}
	}

	private String applyColor(String rawColors, String text) {
		String regexColors = "(&[a-fA-F0-9])|(#[a-fA-F0-9]{6})";
		String output = "";

		Pattern p = Pattern.compile(regexColors);
		Matcher m = p.matcher(rawColors);
		ArrayList<String> colors = new ArrayList<String>();
		while (m.find()) {
			String color = m.group(0);
			colors.add(color);
		}

		String[] textSplit = text.split("");

		if (colors.size() > 1) {
			int index = 0;
			for (String character : textSplit) {
				output += colors.get(index % colors.size()) + character;
				index++;
			}
		} else {
			output = rawColors + text;
		}

		return output;
	}
}