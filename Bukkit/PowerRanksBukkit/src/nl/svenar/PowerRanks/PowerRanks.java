package nl.svenar.PowerRanks;

import org.bukkit.plugin.java.JavaPlugin;

import nl.svenar.powerranks.core.PowerRanksCore;

public class PowerRanks extends JavaPlugin {
	
	private PowerRanksCore powerranks_core = null;
	
	public void onEnable() {
		powerranks_core = new PowerRanksCore("Bukkit");
		powerranks_core.set_logger(new PowerRanksLogger(powerranks_core, this.getLogger()));
		powerranks_core.setup();
	}

	public void onDisable() {
	}
}