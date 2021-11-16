package nl.svenar.PowerRanks;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.Data.Users;
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

		if (player == null) {
			return "";
		}

		Users users = new Users(null);

		if (identifier.equals("rank"))
			return users.getGroup(player);

		if (identifier.equals("prefix"))
			return PowerRanks.chatColor(users.getPrefix(player), true) + ChatColor.RESET;
		
		if (identifier.equals("suffix"))
			return ChatColor.RESET + PowerRanks.chatColor(users.getSuffix(player), true);
		
		if (identifier.equals("subrankprefix"))
			return PowerRanks.chatColor(users.getSubrankprefixes(player), true) + ChatColor.RESET;
		
		if (identifier.equals("subranksuffix"))
			return ChatColor.RESET + PowerRanks.chatColor(users.getSubranksuffixes(player), true);
		
		if (identifier.equals("chatcolor"))
			return users.getChatColor(player);
		
		if (identifier.equals("namecolor"))
			return users.getNameColor(player);
		
		if (identifier.equals("usertag"))
			return users.getUserTagValue(player);
		
		if (identifier.equals("world"))
			return player.getWorld().getName();
		
		if (identifier.equals("playtime")) {
			TimeZone tz = TimeZone.getTimeZone("UTC");
		    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		    df.setTimeZone(tz);
		    String time = df.format(new Date(CacheManager.getPlayer(player.getUniqueId().toString()).getPlaytime() * 1000));
		    // String time = df.format(new Date((CachedPlayers.getLong("players." + player.getUniqueId() + ".playtime") == null ? CachedPlayers.getInt("players." + player.getUniqueId() + ".playtime") : CachedPlayers.getLong("players." + player.getUniqueId() + ".playtime")) * 1000));
			return time;
		}

		return null;
	}
}