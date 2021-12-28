package nl.svenar.PowerRanks.Events;

import java.io.File;
import java.util.Date;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CacheManager;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;
import nl.svenar.PowerRanks.addons.PowerRanksPlayer;

public class OnJoin implements Listener {
	PowerRanks plugin;

	public OnJoin(PowerRanks plugin) {
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = false)
	public void onPlayerJoin(final PlayerJoinEvent e) {
		final Player player = e.getPlayer();

		handleJoin(this.plugin, player);

		for (Entry<File, PowerRanksAddon> prAddon : plugin.addonsManager.addonClasses.entrySet()) {
			PowerRanksPlayer prPlayer = new PowerRanksPlayer(plugin, player);
			prAddon.getValue().onPlayerJoin(prPlayer);
			if (!prAddon.getValue().onPlayerJoinMessage(prPlayer)) {
				e.setJoinMessage("");
			}
			
		}
	}

	@EventHandler(ignoreCancelled = false)
	public void onPlayerLeave(final PlayerQuitEvent e) {
		final Player player = e.getPlayer();
		// this.plugin.playerUninjectPermissible(player);
		// this.plugin.removePermissions(player);

		validatePlayerData(player);

		this.plugin.playerPermissionAttachment.remove(player.getUniqueId());

		long leave_time = new Date().getTime();
		long join_time = leave_time;
		try {
			join_time = this.plugin.playerPlayTimeCache.get(player.getUniqueId());
		} catch (Exception e1) {
		}

		this.plugin.updatePlaytime(player, join_time, leave_time, true);

		for (Entry<File, PowerRanksAddon> prAddon : this.plugin.addonsManager.addonClasses.entrySet()) {
			PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.plugin, player);
			prAddon.getValue().onPlayerLeave(prPlayer);
		}
	}

	public static void handleJoin(PowerRanks plugin, Player player) {
		validatePlayerData(player);

		plugin.playerInjectPermissible(player);
		// this.plugin.playerPermissionAttachment.put(player.getUniqueId(), player.addAttachment(this.plugin));

		// this.plugin.setupPermissions(player);
		plugin.updateTablistName(player);

		long time = new Date().getTime();
		plugin.playerPlayTimeCache.put(player.getUniqueId(), time);

		if (PowerRanks.getConfigManager().getBool("general.disable-op", false)) {
			if (player.isOp()) {
				player.setOp(false);
			}
		}
	}

	private static void validatePlayerData(Player player) {
		if (CacheManager.getPlayer(player.getUniqueId().toString()) == null) {
			CacheManager.createPlayer(player);
		}
	}
}