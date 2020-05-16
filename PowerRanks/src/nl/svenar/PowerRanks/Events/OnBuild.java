package nl.svenar.PowerRanks.Events;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import org.bukkit.event.block.BlockPlaceEvent;
import nl.svenar.PowerRanks.Main;
import org.bukkit.event.Listener;

public class OnBuild implements Listener {
	Main m;

	public OnBuild(final Main m) {
		this.m = m;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerPlace(final BlockPlaceEvent e) {
		final Player player = e.getPlayer();
		final File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		final File playerFile = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
		final File configFile = new File(this.m.getDataFolder() + File.separator + "config" + ".yml");
		final YamlConfiguration rankYaml = new YamlConfiguration();
		final YamlConfiguration playerYaml = new YamlConfiguration();
		final YamlConfiguration configYaml = new YamlConfiguration();

		try {
			rankYaml.load(rankFile);
		} catch (Exception e1) {
			this.m.log.warning(this.m.plp + this.m.dark_red + "Error reading Ranks.yml Does it exist?");
			return;
		}

		try {
			playerYaml.load(playerFile);
		} catch (Exception e1) {
			this.m.log.warning(this.m.plp + this.m.dark_red + "Error reading Players.yml Does it exist?");
			return;
		}

		try {
			configYaml.load(configFile);
		} catch (Exception e1) {
			this.m.log.warning(this.m.plp + this.m.dark_red + "Error reading config.yml Does it exist?");
			return;
		}

		if (configYaml.getBoolean("build_modification.enabled")) {
			final String rank = playerYaml.getString("players." + player.getUniqueId() + ".rank");
			final boolean canBuild = rankYaml.getBoolean("Groups." + rank + ".build");
			e.setCancelled(!canBuild);
		}

	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerBreak(final BlockBreakEvent e) {
		final Player player = e.getPlayer();
		final File rankFile = new File(String.valueOf(this.m.fileLoc) + "Ranks" + ".yml");
		final File playerFile = new File(String.valueOf(this.m.fileLoc) + "Players" + ".yml");
		final File configFile = new File(this.m.getDataFolder() + File.separator + "config" + ".yml");
		final YamlConfiguration rankYaml = new YamlConfiguration();
		final YamlConfiguration playerYaml = new YamlConfiguration();
		final YamlConfiguration configYaml = new YamlConfiguration();

		try {
			rankYaml.load(rankFile);
		} catch (Exception e1) {
			this.m.log.warning(this.m.plp + this.m.dark_red + "Error reading Ranks.yml Does it exist?");
			return;
		}

		try {
			playerYaml.load(playerFile);
		} catch (Exception e1) {
			this.m.log.warning(this.m.plp + this.m.dark_red + "Error reading Players.yml Does it exist?");
			return;
		}

		try {
			configYaml.load(configFile);
		} catch (Exception e1) {
			this.m.log.warning(this.m.plp + this.m.dark_red + "Error reading config.yml Does it exist?");
			return;
		}

		if (configYaml.getBoolean("build_modification.enabled")) {
			final String rank = playerYaml.getString("players." + player.getUniqueId() + ".rank");
			final boolean canBuild = rankYaml.getBoolean("Groups." + rank + ".build");
			e.setCancelled(!canBuild);
		}
	}
}