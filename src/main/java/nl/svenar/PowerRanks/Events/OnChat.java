package nl.svenar.PowerRanks.Events;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Util;
import nl.svenar.PowerRanks.Cache.CacheManager;
// import nl.svenar.PowerRanks.Cache.CachedConfig;
// import nl.svenar.PowerRanks.Cache.CachedPlayers;
// import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.Data.PowerRanksChatColor;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;
import nl.svenar.PowerRanks.addons.PowerRanksPlayer;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRRank;
import nl.svenar.common.structure.PRSubrank;

import com.google.common.collect.ImmutableMap;

import me.clip.deluxetags.DeluxeTag;
import me.clip.placeholderapi.PlaceholderAPI;

public class OnChat implements Listener {

	PowerRanks m;

	public OnChat(PowerRanks m) {
		this.m = m;
	}

	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent e) {
		final Player player = e.getPlayer();

		try {
			if (PowerRanks.getConfigManager().getBool("chat.enabled", true)) {
				final String rank = CacheManager.getPlayer(player.getUniqueId().toString()).getRank();
				String format = PowerRanks.getConfigManager().getString("chat.format", "");
				String prefix = CacheManager.getRank(rank).getPrefix();
				String suffix = CacheManager.getRank(rank).getSuffix();
				String chatColor = CacheManager.getRank(rank).getChatcolor();
				String nameColor = CacheManager.getRank(rank).getNamecolor();
				String subprefix = "";
				String subsuffix = "";
				String usertag = "";

				try {
					ArrayList<PRSubrank> subranks = CacheManager.getPlayer(player.getUniqueId().toString())
							.getSubRanks();
					for (PRSubrank subrank : subranks) {
						PRRank targetRank = CacheManager.getRank(subrank.getName());
						boolean in_world = false;

						String player_current_world = player.getWorld().getName();
						List<String> worlds = subrank.getWorlds();
						for (String world : worlds) {
							if (player_current_world.equalsIgnoreCase(world) || world.equalsIgnoreCase("all")) {
								in_world = true;
							}
						}

						if (Objects.nonNull(targetRank)) {
							if (in_world) {
								if (subrank.getUsingPrefix()) {
									subprefix += ChatColor.RESET + targetRank.getPrefix();
								}

								if (subrank.getUsingSuffix()) {
									subsuffix += ChatColor.RESET + targetRank.getSuffix();

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

				PRPlayer targetPlayer = CacheManager.getPlayer(player.getUniqueId().toString());
				Map<?, ?> availableUsertags = PowerRanks.getUsertagManager().getMap("usertags",
						new HashMap<String, String>());
				ArrayList<String> playerUsertags = targetPlayer.getUsertags();

				for (String playerUsertag : playerUsertags) {
					String value = "";
					for (Entry<?, ?> entry : availableUsertags.entrySet()) {
						if (entry.getKey().toString().equalsIgnoreCase(playerUsertag)) {
							value = entry.getValue().toString();
						}
					}

					if (value.length() > 0) {
						usertag += (usertag.length() > 0 ? " " : "") + value;
					}
				}

				// nameColor = nameColor.replaceAll("&i", "").replaceAll("&I",
				// "").replaceAll("&j", "").replaceAll("&J", "");
				// chatColor = chatColor.replaceAll("&i", "").replaceAll("&I",
				// "").replaceAll("&j", "").replaceAll("&J", "");
				// nameColor = "&r" + nameColor;
				// chatColor = "&r" + chatColor;

				String playersChatMessage = e.getMessage();
				if (!e.getPlayer().hasPermission("powerranks.chat.chatcolor")) {
					playersChatMessage = playersChatMessage.replaceAll("(&[0-9a-fA-FiIjJrRlLmMnNoO])|(#[0-9a-fA-F]{6})",
							"");
				}
				String player_formatted_name = (nameColor.length() == 0 ? "&r" : "")
						+ PowerRanks.applyMultiColorFlow(nameColor, player.getDisplayName());
				String player_formatted_chat_msg = (chatColor.length() == 0 ? "&r" : "")
						+ PowerRanks.applyMultiColorFlow(chatColor, playersChatMessage);

				format = Util.powerFormatter(format, ImmutableMap.<String, String>builder().put("prefix", prefix)
						.put("suffix", suffix).put("subprefix", subprefix).put("subsuffix", subsuffix)
						.put("usertag",
								!PowerRanks.plugin_hook_deluxetags ? usertag : DeluxeTag.getPlayerDisplayTag(player))
						.put("player", player_formatted_name).put("msg", player_formatted_chat_msg)
						.put("format", e.getFormat()).put("world", player.getWorld().getName()).build(), '[', ']');

				if (PowerRanks.placeholderapiExpansion != null) {
					format = PlaceholderAPI.setPlaceholders(player, format).replaceAll("" + ChatColor.COLOR_CHAR,
							"" + PowerRanksChatColor.unformatted_default_char);
				}

				for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
					PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
					format = prAddon.getValue().onPlayerChat(prPlayer, format, e.getMessage());
				}

				format = PowerRanks.chatColor(format, true);
				format = format.replaceAll("%", "%%");

				e.setFormat(format);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			e.setFormat("%1$s: %2$s");
		}
	}
}