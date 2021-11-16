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
// import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;
import nl.svenar.PowerRanks.addons.PowerRanksPlayer;
import nl.svenar.common.structure.PRPlayer;

public class OnJoin implements Listener {
	PowerRanks m;

	public OnJoin(PowerRanks m) {
		this.m = m;
	}

	@EventHandler(ignoreCancelled = false)
	public void onPlayerJoin(final PlayerJoinEvent e) {
		final Player player = e.getPlayer();

		validatePlayerData(player);

		this.m.playerInjectPermissible(player);
		// this.m.playerPermissionAttachment.put(player.getUniqueId(), player.addAttachment(this.m));

		// this.m.setupPermissions(player);
		this.m.updateTablistName(player);

		long time = new Date().getTime();
		this.m.playerPlayTimeCache.put(player.getUniqueId(), time);

		for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
			PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
			prAddon.getValue().onPlayerJoin(prPlayer);
			if (!prAddon.getValue().onPlayerJoinMessage(prPlayer)) {
				e.setJoinMessage("");
			}
			
		}

		if (PowerRanks.getConfigManager().getBool("general.disable-op", true)) {
			if (player.isOp()) {
				player.setOp(false);
			}
		}
	}

	@EventHandler(ignoreCancelled = false)
	public void onPlayerLeave(final PlayerQuitEvent e) {
		final Player player = e.getPlayer();
		// this.m.playerUninjectPermissible(player);
		// this.m.removePermissions(player);

		validatePlayerData(player);

		this.m.playerPermissionAttachment.remove(player.getUniqueId());

		long leave_time = new Date().getTime();
		long join_time = leave_time;
		try {
			join_time = this.m.playerPlayTimeCache.get(player.getUniqueId());
		} catch (Exception e1) {
		}

		this.m.updatePlaytime(player, join_time, leave_time, true);

		for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
			PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
			prAddon.getValue().onPlayerLeave(prPlayer);
		}
	}

	private void validatePlayerData(Player player) {
		if (CacheManager.getPlayer(player.getUniqueId().toString()) == null) {
			PRPlayer prPlayer = new PRPlayer();
			prPlayer.setUUID(player.getUniqueId());
			prPlayer.setName(player.getName());
			prPlayer.setRank(CacheManager.getDefaultRank());
			CacheManager.addPlayer(prPlayer);
		}
	}
}