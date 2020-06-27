package nl.svenar.PowerRanks.Events;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import nl.svenar.PowerRanks.PowerRanks;

public class OnJoin implements Listener {
	PowerRanks m;

	public OnJoin(PowerRanks m) {
		this.m = m;
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e) {
		final Player player = e.getPlayer();
		final File rankFile = new File(String.valueOf(PowerRanks.fileLoc) + "Ranks" + ".yml");
		final File playerFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
		final YamlConfiguration rankYaml = new YamlConfiguration();
		final YamlConfiguration playerYaml = new YamlConfiguration();

		this.m.playerInjectPermissible(player);

		this.m.playerPermissionAttachment.put(player.getName(), player.addAttachment(this.m));
		try {
			rankYaml.load(rankFile);
			playerYaml.load(playerFile);
			
			playerYaml.set("players." + player.getUniqueId() + ".name", player.getName());
			
			if (playerYaml.getString("players." + player.getUniqueId() + ".rank") == null) {
				playerYaml.set("players." + player.getUniqueId() + ".rank", rankYaml.get("Default"));
			}

			if (playerYaml.getString("players." + player.getUniqueId() + ".permissions") == null) {
				playerYaml.set("players." + player.getUniqueId() + ".permissions", new ArrayList<>());
			}

			if (playerYaml.getString("players." + player.getUniqueId() + ".subranks") == null) {
				playerYaml.set("players." + player.getUniqueId() + ".subranks", "");
			}
			
			if (!playerYaml.isSet("players." + player.getUniqueId() + ".usertag"))
				playerYaml.set("players." + player.getUniqueId() + ".usertag", "");
			
			playerYaml.save(playerFile);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		this.m.setupPermissions(player);
		this.m.updateTablistName(player);

		long time = new Date().getTime();
		this.m.playerLoginTime.put(player, time);
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

	}
}