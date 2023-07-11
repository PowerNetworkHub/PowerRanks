package nl.svenar.powerranks.events;

import java.io.File;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.addons.PowerRanksAddon;
import nl.svenar.powerranks.addons.PowerRanksPlayer;

public class OnWorldChange implements Listener {
	PowerRanks powerRanks;

	public OnWorldChange(final PowerRanks powerRanks) {
		this.powerRanks = powerRanks;
	}

	@EventHandler
	public void onWorldChange(final PlayerChangedWorldEvent e) {
		Player player = e.getPlayer();
		this.powerRanks.updateTablistName(player);
		// this.powerRanks.setupPermissions(player);

		for (Entry<File, PowerRanksAddon> prAddon : this.powerRanks.addonsManager.addonClasses.entrySet()) {
			PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.powerRanks, player);
			prAddon.getValue().onPlayerWorldChange(prPlayer, e.getFrom(), player.getWorld());
		}

	}
}