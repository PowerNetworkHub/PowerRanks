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

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e) {
		final Player player = e.getPlayer();

		CachedPlayers.update();

		HashMap<String, Object> new_user_data = new HashMap<String, Object>();
		new_user_data.put("players." + player.getUniqueId() + ".name", player.getName());

		if (!CachedPlayers.contains("players." + player.getUniqueId())) {
			if (!CachedPlayers.contains("players." + player.getUniqueId() + ".rank"))
				new_user_data.put("players." + player.getUniqueId() + ".rank", CachedRanks.get("Default"));

			if (!CachedPlayers.contains("players." + player.getUniqueId() + ".permissions"))
				new_user_data.put("players." + player.getUniqueId() + ".permissions", new ArrayList<>());

			if (!CachedPlayers.contains("players." + player.getUniqueId() + ".subranks"))
				new_user_data.put("players." + player.getUniqueId() + ".subranks", "");

			if (!CachedPlayers.contains("players." + player.getUniqueId() + ".usertag"))
				new_user_data.put("players." + player.getUniqueId() + ".usertag", "");
			
			if (!CachedPlayers.contains("players." + player.getUniqueId() + ".playtime"))
				new_user_data.put("players." + player.getUniqueId() + ".playtime", 0);
		}

		CachedPlayers.set(new_user_data);

		this.m.playerInjectPermissible(player);
		this.m.playerPermissionAttachment.put(player.getName(), player.addAttachment(this.m));

		this.m.setupPermissions(player);
		this.m.updateTablistName(player);

		long time = new Date().getTime();
		this.m.playerLoginTime.put(player, time);

		for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
			PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
			prAddon.getValue().onPlayerJoin(prPlayer);
		}
	}

	@EventHandler
	public void onPlayerLeave(final PlayerQuitEvent e) {
		final Player player = e.getPlayer();
		this.m.playerUninjectPermissible(player);
		this.m.removePermissions(player);

		this.m.playerPermissionAttachment.remove(player.getName());

		long leave_time = new Date().getTime();
		long join_time = leave_time;
		try {
			join_time = this.m.playerLoginTime.get(player);
		} catch (Exception e1) {
		}

		this.m.updatePlaytime(player, join_time, leave_time);

		for (Entry<File, PowerRanksAddon> prAddon : this.m.addonsManager.addonClasses.entrySet()) {
			PowerRanksPlayer prPlayer = new PowerRanksPlayer(this.m, player);
			prAddon.getValue().onPlayerLeave(prPlayer);
		}
	}
}