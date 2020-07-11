package nl.svenar.PowerRanks.Events;

import nl.svenar.PowerRanks.PowerRanks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class OnWorldChange implements Listener {
	PowerRanks powerRanks;

	public OnWorldChange(final PowerRanks powerRanks) {
		this.powerRanks = powerRanks;
	}

	@EventHandler
	public void onWorldChange(final PlayerChangedWorldEvent e) {
		Player player = e.getPlayer();
		this.powerRanks.updateTablistName(player);
		this.powerRanks.setupPermissions(player);
	}
}