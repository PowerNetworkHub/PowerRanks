package nl.svenar.PowerRanks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import nl.svenar.PowerRanks.commands.Commands;
import nl.svenar.PowerRanks.events.onJoin;

public class PowerRanks extends Plugin {
	public static PluginDescription pdf;
	public static Logger log;
	public static String website_url = "https://svenar.nl/powerranks";
	
	@Override
	public void onEnable() {
		log = ProxyServer.getInstance().getLogger();
		pdf = this.getDescription();
		
		getProxy().getPluginManager().registerCommand(this, new Commands("powerranks"));
		getProxy().getPluginManager().registerCommand(this, new Commands("pr"));
		getProxy().getPluginManager().registerListener(this, new onJoin());

		setup_config();
		new PowerRanksConfiguration(getDataFolder());
		new PowerRanksExceptionsHandler(getDataFolder());
	}

	private void setup_config() {
		if (!getDataFolder().exists())
            getDataFolder().mkdir();
		
		File file_config = new File(getDataFolder(), "config.yml");
		File file_lang = new File(getDataFolder(), "lang.yml");
		File file_ranks = new File(getDataFolder(), "ranks.yml");
		File file_players = new File(getDataFolder(), "players.yml");
		
		if (!file_config.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file_config.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
		if (!file_lang.exists()) {
            try (InputStream in = getResourceAsStream("lang.yml")) {
                Files.copy(in, file_lang.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
		if (!file_ranks.exists()) {
            try (InputStream in = getResourceAsStream("ranks.yml")) {
                Files.copy(in, file_ranks.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
		if (!file_players.exists()) {
            try (InputStream in = getResourceAsStream("players.yml")) {
                Files.copy(in, file_players.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
}