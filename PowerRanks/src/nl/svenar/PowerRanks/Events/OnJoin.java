package nl.svenar.PowerRanks.Events;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.addons.PowerRanksAddon;
import nl.svenar.PowerRanks.addons.PowerRanksPlayer;

public class OnJoin implements Listener {
	PowerRanks m;

	public OnJoin(PowerRanks m) {
		this.m = m;
	}

	@EventHandler(ignoreCancelled = false)
	public void onPlayerJoin(final PlayerJoinEvent e) {
		final Player player = e.getPlayer();

		if (!CachedPlayers.is_ready())
			CachedPlayers.update();

		validatePlayerData(player);

		this.m.playerInjectPermissible(player);
//		this.m.playerPermissionAttachment.put(player.getUniqueId(), player.addAttachment(this.m));

//		this.m.setupPermissions(player);
		this.m.updateTablistName(player);

		long time = new Date().getTime();
		this.m.playerLoginTime.put(player.getUniqueId(), time);

		for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
			PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
			prAddon.getValue().onPlayerJoin(prPlayer);
		}
		
		m.power_bungee_events.eventJoin(player);
	}

	@EventHandler(ignoreCancelled = false)
	public void onPlayerLeave(final PlayerQuitEvent e) {
		final Player player = e.getPlayer();
//		this.m.playerUninjectPermissible(player);
//		this.m.removePermissions(player);

		validatePlayerData(player);

		this.m.playerPermissionAttachment.remove(player.getUniqueId());

		long leave_time = new Date().getTime();
		long join_time = leave_time;
		try {
			join_time = this.m.playerLoginTime.get(player.getUniqueId());
		} catch (Exception e1) {
		}

		this.m.updatePlaytime(player, join_time, leave_time);

		for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
			PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
			prAddon.getValue().onPlayerLeave(prPlayer);
		}
		
		m.power_bungee_events.eventLeave(player);
	}

	private void validatePlayerData(Player player) {
		HashMap<String, Object> user_data = new HashMap<String, Object>();
		if (!CachedPlayers.contains("players." + player.getUniqueId() + ".name") || !CachedPlayers.getString("players." + player.getUniqueId() + ".name").equals(player.getName()))
			user_data.put("players." + player.getUniqueId() + ".name", player.getName());

		if (!CachedPlayers.contains("players." + player.getUniqueId() + ".rank"))
			user_data.put("players." + player.getUniqueId() + ".rank", CachedRanks.get("Default"));

		if (!CachedPlayers.contains("players." + player.getUniqueId() + ".permissions"))
			user_data.put("players." + player.getUniqueId() + ".permissions", new ArrayList<>());

		if (!CachedPlayers.contains("players." + player.getUniqueId() + ".subranks"))
			user_data.put("players." + player.getUniqueId() + ".subranks", "");

		if (!CachedPlayers.contains("players." + player.getUniqueId() + ".usertag"))
			user_data.put("players." + player.getUniqueId() + ".usertag", "");

		if (!CachedPlayers.contains("players." + player.getUniqueId() + ".playtime"))
			user_data.put("players." + player.getUniqueId() + ".playtime", 0);

//		for (Entry<String, Object> kv : user_data.entrySet()) {
//			PowerRanks.log.info(kv.getKey() + ": " + kv.getValue());
//		}
		
		if (user_data.size() > 0) {
			CachedPlayers.set(user_data, false);
		}
	}
}