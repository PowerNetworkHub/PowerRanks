package nl.svenar.powerranks.hooks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.data.PRPlayer;
import nl.svenar.powerranks.data.PRRank;
import nl.svenar.powerranks.handlers.BaseDataHandler;

public class PowerRanksPlaceholderExpansion extends PlaceholderExpansion {
    
    private PowerRanks plugin;

    /**
	 * Since we register the expansion inside our own plugin, we can simply use this
	 * method here to get an instance of our plugin.
	 *
	 * @param plugin The instance of our plugin.
	 */
	public PowerRanksPlaceholderExpansion(PowerRanks plugin) {
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

        PRPlayer prPlayer = BaseDataHandler.getPlayer(player.getName());

        if (prPlayer == null) {
			return "";
		}

        // Get the name of the rank with the highest weight
        if (identifier.equals("primary_rank")) {
            PRRank primaryRank = getPrimaryRank(prPlayer);
            return primaryRank.getName();
        }

        // Return a list of ranks sorted by weight from high to low
        // [rank1, rank2, ...]
        if (identifier.equals("all_ranks")) {
            List<PRRank> ranks = new ArrayList<PRRank>();
            
            for (PRRank playerRank : prPlayer.getRanks()) {
                ranks.add(playerRank);
            }

            Collections.sort(ranks, (left, right) -> right.getWeight() - left.getWeight());

            String[] rankList = new String[ranks.size()];

            int index = 0;
            for (PRRank rank : ranks) {
                rankList[index++] = rank.getName();
            }

            return "[" + String.join(", ", String.join(", ", rankList)) + "]";
        }

        // Get all prefixes of a rank (%powerranks_prefix%) joined by a space ([member] [moderator])
		// Get a specific amount of prefixes (%powerranks_prefix_X% where X is a positive number) this will join the top X prefixes with a space
        if (identifier.equals("prefix") || identifier.contains("prefix_")) {
            List<PRRank> sortedRanks = getSortedRanks(prPlayer);
			String prefix = "";
			int index = 0;
			int target = identifier.contains("prefix_") ? Integer.parseInt(identifier.split("_")[1]) : sortedRanks.size();
			for (PRRank rank : sortedRanks) {
				if (index >= target) {
					break;
				}

				prefix += rank.getPrefix() + (index < sortedRanks.size() ? " " : "");

				index++;
			}
            return prefix;
        }

        // Get all suffixes of a rank (%powerranks_suffix%) joined by a space ([member] [moderator])
		// Get a specific amount of suffixes (%powerranks_suffix_X% where X is a positive number) this will join the top X suffixes with a space
        if (identifier.equals("suffix") || identifier.contains("suffix_")) {
			List<PRRank> sortedRanks = getSortedRanks(prPlayer);
			String suffix = "";
			int index = 0;
			int target = identifier.contains("suffix_") ? Integer.parseInt(identifier.split("_")[1]) : sortedRanks.size();
			for (PRRank rank : sortedRanks) {
				if (index >= target) {
					break;
				}

				suffix += rank.getSuffix() + (index < sortedRanks.size() ? " " : "");

				index++;
			}
            return suffix;
            // PRRank primaryRank = getPrimaryRank(prPlayer);
            // return primaryRank.getSuffix();
        }

        // Get the worldname the player is in
        if (identifier.equals("player_world")) {
            return player.getWorld().getName();
        }

		return null;
	}

	private List<PRRank> getSortedRanks(PRPlayer prPlayer) {
		List<PRRank> ranks = new ArrayList<PRRank>();
            
        for (PRRank playerRank : prPlayer.getRanks()) {
            ranks.add(playerRank);
        }

        Collections.sort(ranks, (left, right) -> right.getWeight() - left.getWeight());

        return ranks;
	}

    private PRRank getPrimaryRank(PRPlayer prPlayer) {
        return getSortedRanks(prPlayer).get(0);
    }
}
