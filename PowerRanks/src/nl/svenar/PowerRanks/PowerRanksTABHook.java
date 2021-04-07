package nl.svenar.PowerRanks;

import me.neznamy.tab.shared.permission.PermissionPlugin;
import me.neznamy.tab.shared.placeholders.PrefixSuffixProvider;
import me.neznamy.tab.api.TabPlayer;

import nl.svenar.PowerRanks.api.PowerRanksAPI;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PowerRanksTABHook implements PermissionPlugin, PrefixSuffixProvider {
	
	private PowerRanks plugin;
	private PowerRanksAPI prAPI;

	public PowerRanksTABHook(PowerRanks plugin) {
		this.plugin = plugin;
		this.prAPI = new PowerRanksAPI(plugin);
    }

	@Override
	public String getPrimaryGroup(TabPlayer tabPlayer) throws Throwable {
		return this.prAPI.getPlayerRank((Player) tabPlayer.getPlayer());
	}

	@Override
	public String[] getAllGroups(TabPlayer tabPlayer) throws Throwable {
		ArrayList<String> ranks = new ArrayList<String>();
		ranks.add(this.prAPI.getPlayerRank((Player) tabPlayer.getPlayer()));
		for (String rankname : this.prAPI.getSubranks(((Player) tabPlayer.getPlayer()))) {
			ranks.add(rankname);
		}
		return ranks.toArray(new String[0]);
	}

	@Override
	public String getPrefix(TabPlayer tabPlayer) {
		return this.prAPI.getPrefix(this.prAPI.getPlayerRank((Player) tabPlayer.getPlayer()));
	}

	@Override
	public String getSuffix(TabPlayer tabPlayer) {
		return this.prAPI.getSuffix(this.prAPI.getPlayerRank((Player) tabPlayer.getPlayer()));
	}

	@Override
	public String getVersion() {
		return this.plugin.getDescription().getVersion();
	}

	@Override
	public String getName() {
		return this.plugin.getDescription().getName();
	}
}
