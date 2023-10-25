package nl.svenar.powerranks.bukkit.events;

import java.io.File;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.addons.PowerRanksAddon;
import nl.svenar.powerranks.bukkit.addons.PowerRanksPlayer;
import nl.svenar.powerranks.bukkit.cache.CacheManager;

public class OnWorldChange implements Listener {
	PowerRanks powerRanks;

	public OnWorldChange(final PowerRanks powerRanks) {
		this.powerRanks = powerRanks;
	}

	@EventHandler
	public void onWorldChange(final PlayerChangedWorldEvent e) {
		Player player = e.getPlayer();
		PRPlayer prPlayer = CacheManager.getPlayer(player);
		prPlayer.updateTags(player.getLocation().getWorld().getName());
		this.powerRanks.updateTablistName(player);
		this.powerRanks.getTablistManager().updateSorting(player);
		// this.powerRanks.setupPermissions(player);

		PowerRanksPlayer prAddonPlayer = new PowerRanksPlayer(this.powerRanks, player);
		for (Entry<File, PowerRanksAddon> prAddon : this.powerRanks.addonsManager.addonClasses.entrySet()) {
			prAddon.getValue().onPlayerWorldChange(prAddonPlayer, e.getFrom(), player.getWorld());
		}

	}
}