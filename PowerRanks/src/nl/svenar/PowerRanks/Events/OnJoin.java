package nl.svenar.PowerRanks.Events;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import nl.svenar.PowerRanks.Main;
import nl.svenar.PowerRanks.Data.PermissibleInjector;

public class OnJoin implements Listener {
	Main m;

	public OnJoin(Main m) {
		this.m = m;
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e) {
		final Player player = e.getPlayer();
		final File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		final File playerFile = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
		final YamlConfiguration rankYaml = new YamlConfiguration();
		final YamlConfiguration playerYaml = new YamlConfiguration();
		
		this.m.playerInjectPermissible(player);
        		
		this.m.playerPermissionAttachment.put(player.getName(), player.addAttachment(this.m));		
		try {
			rankYaml.load(rankFile);
			playerYaml.load(playerFile);
			if (playerYaml.getString("players." + player.getUniqueId() + ".rank") == null) {
				playerYaml.set("players." + player.getUniqueId() + ".rank", rankYaml.get("Default"));
			}
			playerYaml.set("players." + player.getUniqueId() + ".name", player.getName());
			playerYaml.save(playerFile);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
		this.m.setupPermissions(player);
		this.m.updateTablistName(player);
		
        this.m.log.info("Injected: " + (PermissibleInjector.isInjected(player) ? "yes" : "no"));
	}

	@EventHandler
	public void onPlayerLeave(final PlayerQuitEvent e) {
		final Player player = e.getPlayer();
		this.m.playerUninjectPermissible(player);
		this.m.removePermissions(player);

		this.m.playerPermissionAttachment.remove(player.getName());
	}
}
