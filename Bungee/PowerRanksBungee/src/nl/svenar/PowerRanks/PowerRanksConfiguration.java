package nl.svenar.PowerRanks;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class PowerRanksConfiguration {
	
	private static File data_directory;
	
	public PowerRanksConfiguration() {
	}
	
	public PowerRanksConfiguration(File data_directory) {
		PowerRanksConfiguration.set_data_directory(data_directory);
	}
	
	public Configuration load_config() throws IOException {
		return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(get_data_directory(), "config.yml"));
	}
	
	public Configuration load_lang() throws IOException {
		return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(get_data_directory(), "lang.yml"));
	}
	
	public Configuration load_ranks() throws IOException {
		return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(get_data_directory(), "ranks.yml"));
	}
	
	public Configuration load_players() throws IOException {
		return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(get_data_directory(), "players.yml"));
	}
	
	public void save_config(Configuration configuration) throws IOException {
		ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(get_data_directory(), "config.yml"));
	}
	
	public void save_lang(Configuration configuration) throws IOException {
		ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(get_data_directory(), "lang.yml"));
	}
	
	public void save_ranks(Configuration configuration) throws IOException {
		ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(get_data_directory(), "ranks.yml"));
	}
	
	public void save_players(Configuration configuration) throws IOException {
		ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(get_data_directory(), "players.yml"));
	}

	public static File get_data_directory() {
		return data_directory;
	}

	public static void set_data_directory(File data_directory) {
		PowerRanksConfiguration.data_directory = data_directory;
	}
}
