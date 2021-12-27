package nl.svenar.PowerRanks.External;

import org.bukkit.scheduler.BukkitRunnable;

import me.neznamy.tab.api.TabAPI;
import nl.svenar.PowerRanks.PowerRanks;

public class TABHook {

    private boolean isReady = false;

    public void setup() {
        new BukkitRunnable() {
			@Override
			public void run() {
				if (PowerRanks.getInstance().getServer().getPluginManager().isPluginEnabled("TAB")) {
                    load();
					this.cancel();
				}
			}
		}.runTaskTimer(PowerRanks.getInstance(), 20, 20);
        
    }
    
    private void load() {
        if (isReady) {
            return;
        }

        isReady = true;
        TabAPI.getInstance().debug("Hooked into PowerRanks");
    }

    public void updateRanks() {
        // TabAPI.getInstance().getGroups()
    }
}
