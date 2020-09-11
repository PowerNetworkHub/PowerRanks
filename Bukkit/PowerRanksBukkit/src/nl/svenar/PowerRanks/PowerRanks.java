package nl.svenar.PowerRanks;

import org.bukkit.plugin.java.JavaPlugin;

import me.svenar.powerranks.core.PowerRanksCore;

public class PowerRanks extends JavaPlugin {
	
	private PowerRanksCore powerranks_core = null;
	
	public void onEnable() {
		powerranks_core = new PowerRanksCore("Bukkit");
	}

	public void onDisable() {
	}
}