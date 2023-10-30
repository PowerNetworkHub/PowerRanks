package nl.svenar.powerranks.bukkit.events.impl;

import java.io.File;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import nl.svenar.powerranks.bukkit.PowerRanks;
import nl.svenar.powerranks.bukkit.addons.PowerRanksAddon;
import nl.svenar.powerranks.bukkit.addons.PowerRanksPlayer;

public class OnMove implements Listener {

	PowerRanks m;

	public OnMove(PowerRanks m) {
		this.m = m;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerMove(final PlayerMoveEvent e) {
		final Player player = e.getPlayer();

		boolean cancelled = false;

		try {
			for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
				PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
				if (prAddon.getValue().onPlayerMove(prPlayer, e.getFrom(), e.getTo()))
					cancelled = true;
			}
		} catch (Exception ex) {
		}

		e.setCancelled(cancelled);
	}
}