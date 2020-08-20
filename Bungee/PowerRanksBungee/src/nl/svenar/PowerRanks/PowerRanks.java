package nl.svenar.PowerRanks;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import nl.svenar.PowerRanks.commands.Commands;
import nl.svenar.PowerRanks.events.onJoin;

public class PowerRanks extends Plugin {
	public static PluginDescription pdf;
	public static String website_url = "https://svenar.nl/powerranks";
	
	@Override
	public void onEnable() {
		getProxy().getPluginManager().registerCommand(this, new Commands("powerranks"));
		getProxy().getPluginManager().registerCommand(this, new Commands("pr"));
		getProxy().getPluginManager().registerListener(this, new onJoin());
		pdf = this.getDescription();
	}
}