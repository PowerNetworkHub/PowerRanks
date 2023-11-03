package nl.svenar.powerranks.bukkit.external;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRUtil;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

/**
 * This class will automatically register as a placeholder expansion when a jar
 * including this class is added to the directory
 * {@code /plugins/PlaceholderAPI/expansions} on your server. <br>
 * <br>
 * If you create such a class inside your own plugin, you have to register it
 * manually in your plugins {@code onEnable()} by using
 * {@code new YourExpansionClass().register();}
 */
public class PowerRanksExpansion extends PlaceholderExpansion {

	private PowerRanks plugin;

	/**
	 * Since we register the expansion inside our own plugin, we can simply use this
	 * method here to get an instance of our plugin.
	 *
	 * @param plugin The instance of our plugin.
	 */
	public PowerRanksExpansion(PowerRanks plugin) {
		this.plugin = plugin;
	}

	/**
	 * Because this is an internal class, you must override this method to let
	 * PlaceholderAPI know to not unregister your expansion class when
	 * PlaceholderAPI is reloaded
	 *
	 * @return true to persist through reloads
	 */
	@Override
	public boolean persist() {
		return true;
	}

	/**
	 * Because this is a internal class, this check is not needed and we can simply
	 * return {@code true}
	 *
	 * @return Always true since it's an internal class.
	 */
	@Override
	public boolean canRegister() {
		return true;
	}

	/**
	 * The name of the person who created this expansion should go here. <br>
	 * For convienience do we return the author from the plugin.yml
	 * 
	 * @return The name of the author as a String.
	 */
	@Override
	public String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	/**
	 * The placeholder identifier should go here. <br>
	 * This is what tells PlaceholderAPI to call our onRequest method to obtain a
	 * value if a placeholder starts with our identifier. <br>
	 * This must be unique and can not contain % or _
	 *
	 * @return The identifier in {@code %<identifier>_<value>%} as String.
	 */
	@Override
	public String getIdentifier() {
		return "powerranks";
	}

	/**
	 * This is the version of the expansion. <br>
	 * You don't have to use numbers, since it is set as a String.
	 *
	 * For convienience do we return the version from the plugin.yml
	 *
	 * @return The version as a String.
	 */
	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}

	/**
	 * This is the method called when a placeholder with our identifier is found and
	 * needs a value. <br>
	 * We specify the value identifier in this method. <br>
	 * Since version 2.9.1 can you use OfflinePlayers in your requests.
	 *
	 * @param player     A {@link org.bukkit.Player Player}.
	 * @param identifier A String containing the identifier/value.
	 *
	 * @return possibly-null String of the requested identifier.
	 */
	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		boolean format_colors = PowerRanks.getConfigManager().getBool("placeholderapi.inject_powerranks_color", false);

		if (player == null) {
			return "";
		}

		PRPlayer prPlayer = CacheManager.getPlayer(player.getUniqueId().toString());
		if (prPlayer == null) {
			return "";
		}

		List<PRRank> playerRanks = getPlayerRanksSorted(prPlayer);

		// DEPRECATED
		if (identifier.equals("rank")) {
			return playerRanks.get(0).getName();
		}

		if (identifier.equals("prefix")) {
			if (format_colors) {
				return PowerRanks.chatColor(playerRanks.get(0).getPrefix(), true) + ChatColor.RESET;
			}
			return playerRanks.get(0).getPrefix() + ChatColor.RESET;
		}

		if (identifier.equals("suffix")) {
			if (format_colors) {
				return ChatColor.RESET + PowerRanks.chatColor(playerRanks.get(0).getSuffix(), true);
			}
			return ChatColor.RESET + playerRanks.get(0).getSuffix();
		}

		if (identifier.equals("subrankprefix")) {
			List<String> prefixes = new ArrayList<String>();
			for (PRRank rank : playerRanks) {
				if (format_colors) {
					prefixes.add(PowerRanks.chatColor(rank.getPrefix(), true) + ChatColor.RESET);
				} else {
					prefixes.add(rank.getPrefix() + ChatColor.RESET);
				}
			}
			return String.join(PowerRanks.getInstance().getConfigString("placeholderapi.multi_rank_separator", " "),
					prefixes);
		}

		if (identifier.equals("subranksuffix")) {
			List<String> suffixes = new ArrayList<String>();
			for (PRRank rank : playerRanks) {
				if (format_colors) {
					suffixes.add(ChatColor.RESET + PowerRanks.chatColor(rank.getSuffix(), true));
				} else {
					suffixes.add(ChatColor.RESET + rank.getSuffix());
				}
			}
			return String.join(PowerRanks.getInstance().getConfigString("placeholderapi.multi_rank_separator", " "),
					suffixes);
		}
		// DEPRECATED

		if (identifier.equals("primary_rank")) {
			return playerRanks.size() > 0 ? playerRanks.get(0).getName() : "";
		}

		if (identifier.equals("ranks")) {
			List<String> playerRankNames = new ArrayList<String>();
			for (PRRank rank : playerRanks) {
				playerRankNames.add(rank.getName());
			}
			return String.join(PowerRanks.getInstance().getConfigString("placeholderapi.multi_rank_separator", " "),
					playerRankNames);
		}

		if (identifier.equals("primary_prefix")) {
			return playerRanks.size() > 0
					? (format_colors ? PowerRanks.chatColor(playerRanks.get(0).getPrefix(), true)
							: playerRanks.get(0).getPrefix()) + ChatColor.RESET
					: "";
		}

		if (identifier.equals("primary_suffix")) {
			return playerRanks.size() > 0
					? ChatColor.RESET + (format_colors ? PowerRanks.chatColor(playerRanks.get(0).getSuffix(), true)
							: playerRanks.get(0).getSuffix())
					: "";
		}

		if (identifier.equals("prefixes")) {
			List<String> prefixes = new ArrayList<String>();
			for (PRRank rank : playerRanks) {
				if (format_colors) {
					prefixes.add(PowerRanks.chatColor(rank.getPrefix(), true) + ChatColor.RESET);
				} else {
					prefixes.add(rank.getPrefix() + ChatColor.RESET);
				}
			}
			return String.join(PowerRanks.getInstance().getConfigString("placeholderapi.multi_rank_separator", " "),
					prefixes);
		}

		if (identifier.equals("suffixes")) {
			List<String> suffixes = new ArrayList<String>();
			for (PRRank rank : playerRanks) {
				if (format_colors) {
					suffixes.add(ChatColor.RESET + PowerRanks.chatColor(rank.getSuffix(), true));
				} else {
					suffixes.add(ChatColor.RESET + rank.getSuffix());
				}
			}
			return String.join(PowerRanks.getInstance().getConfigString("placeholderapi.multi_rank_separator", " "),
					suffixes);
		}

		if (identifier.equals("chatcolor")) {
			return playerRanks.size() > 0
					? (format_colors ? PowerRanks.chatColor(playerRanks.get(0).getChatcolor(), true)
							: playerRanks.get(0).getChatcolor())
					: "";
		}

		if (identifier.equals("namecolor")) {
			return playerRanks.size() > 0
					? (format_colors ? PowerRanks.chatColor(playerRanks.get(0).getNamecolor(), true)
							: playerRanks.get(0).getNamecolor())
					: "";
		}

		if (identifier.equals("usertag")) {
			String usertag = "";
			Map<?, ?> availableUsertags = PowerRanks.getUsertagManager().getMap("usertags",
					new HashMap<String, String>());
			Set<String> playerUsertags = prPlayer.getUsertags();

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
			return (format_colors ? PowerRanks.chatColor(usertag, true) : usertag);
		}

		if (identifier.equals("world")) {
			return player.getWorld().getName();
		}

		if (identifier.equals("playtime")) {
			TimeZone tz = TimeZone.getTimeZone("UTC");
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			df.setTimeZone(tz);
			String time = df
					.format(new Date(CacheManager.getPlayer(player.getUniqueId().toString()).getPlaytime() * 1000));
			return time;
		}

		return null;
	}

	private List<PRRank> getPlayerRanksSorted(PRPlayer player) {
		List<PRRank> playerRanks = new ArrayList<PRRank>();

		for (PRPlayerRank playerRank : player.getRanks()) {
			PRRank rank = CacheManager.getRank(playerRank.getName());
			if (rank != null) {
				playerRanks.add(rank);
			}
		}

		PRUtil.sortRanksByWeight(playerRanks);
		PRUtil.reverseRanks(playerRanks);
		return playerRanks;
	}
}