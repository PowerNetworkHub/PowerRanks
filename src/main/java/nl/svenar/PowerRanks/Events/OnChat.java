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
import nl.svenar.PowerRanks.Cache.CacheManager;
// import nl.svenar.PowerRanks.Cache.CachedConfig;
// import nl.svenar.PowerRanks.Cache.CachedPlayers;
// import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.Data.PowerRanksChatColor;
import nl.svenar.PowerRanks.Util.Util;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;
import nl.svenar.PowerRanks.addons.PowerRanksPlayer;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRRank;
import nl.svenar.common.utils.PRUtil;

import com.google.common.collect.ImmutableMap;

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

				String format = PowerRanks.getConfigManager().getString("chat.format", "");

				List<String> ranknames = CacheManager.getPlayer(player.getUniqueId().toString()).getRanks();

				List<PRRank> ranks = new ArrayList<PRRank>();
				for (String rankname : ranknames) {
					PRRank rank = CacheManager.getRank(rankname);
					if (rank != null) {
						ranks.add(rank);
					}
				}

				ranks = PRUtil.reverseRanks(PRUtil.sortRanksByWeight(ranks));

				String formatted_prefix = "";
				String formatted_suffix = "";
				String chatColor = ranks.get(0).getChatcolor();
				String nameColor = ranks.get(0).getNamecolor();
				String usertag = "";

				for (PRRank rank : ranks) {
					formatted_prefix += rank.getPrefix() + " ";
					formatted_suffix += rank.getSuffix() + " ";
				}

				if (formatted_prefix.endsWith(" ")) {
					formatted_prefix = formatted_prefix.substring(0, formatted_prefix.length() - 1);
				}

				if (formatted_suffix.endsWith(" ")) {
					formatted_suffix = formatted_suffix.substring(0, formatted_suffix.length() - 1);
				}

				if (formatted_prefix.replaceAll(" ", "").length() == 0) {
					formatted_prefix = "";
				}

				if (formatted_suffix.replaceAll(" ", "").length() == 0) {
					formatted_suffix = "";
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

				// Dirty PremiumVanish work around
				if (Objects
						.nonNull(PowerRanks.getInstance().getServer().getPluginManager().getPlugin("PremiumVanish"))) {
					if (player_formatted_chat_msg.endsWith("/")) {
						player_formatted_chat_msg = player_formatted_chat_msg.substring(0,
								player_formatted_chat_msg.length() - 1);
					}
				}

				format = Util.powerFormatter(format, ImmutableMap.<String, String>builder().put("prefix", formatted_prefix)
						.put("suffix", formatted_suffix)
						.put("usertag",
								!PowerRanks.plugin_hook_deluxetags ? usertag : PowerRanks.getInstance().getDeluxeTagsHook().getPlayerDisplayTag(player))
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