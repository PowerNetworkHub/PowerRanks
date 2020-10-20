package nl.svenar.PowerRanks.Events;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;

import org.bukkit.event.Listener;

public class OnBuild implements Listener {
	PowerRanks m;

	public OnBuild(final PowerRanks m) {
		this.m = m;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerPlace(final BlockPlaceEvent e) {
		final Player player = e.getPlayer();

		if (CachedConfig.getBoolean("build_modification.enabled")) {
			final String rank = CachedPlayers.getString("players." + player.getUniqueId() + ".rank");
			final boolean canBuild = CachedRanks.getBoolean("Groups." + rank + ".build");
			e.setCancelled(!canBuild);
		}

	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerBreak(final BlockBreakEvent e) {
		final Player player = e.getPlayer();
		
		if (CachedConfig.getBoolean("build_modification.enabled")) {
			final String rank = CachedPlayers.getString("players." + player.getUniqueId() + ".rank");
			final boolean canBuild = CachedRanks.getBoolean("Groups." + rank + ".build");
			e.setCancelled(!canBuild);
		}
	}
}