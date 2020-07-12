package nl.svenar.PowerRanks.Cache;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.PowerRanks.PowerRanks;

public class CachedPlayers {

	private static HashMap<String, Object> players_data = new HashMap<String, Object>();
	
	private static HashMap<String, ConfigurationSection> players_configuration_sections = new HashMap<String, ConfigurationSection>();
	private static HashMap<String, String> players_strings = new HashMap<String, String>();
	private static HashMap<String, List<String>> players_string_lists = new HashMap<String, List<String>>();
	private static HashMap<String, Boolean> players_booleans = new HashMap<String, Boolean>();
	private static HashMap<String, Long> players_longs = new HashMap<String, Long>();

	public CachedPlayers(PowerRanks pr) {
		update();
	}

	public static void update() {
		final File playersFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
		final YamlConfiguration playersYaml = new YamlConfiguration();

		try {
			playersYaml.load(playersFile);
			for (String field : playersYaml.getConfigurationSection("").getKeys(true)) {
				players_data.put(field, playersYaml.get(field));

				if (playersYaml.isConfigurationSection(field)) {
					players_configuration_sections.put(field, playersYaml.getConfigurationSection(field));
				} else if (playersYaml.isString(field)) {
					players_strings.put(field, playersYaml.getString(field));
				} else if (playersYaml.isList(field)) {
					players_string_lists.put(field, playersYaml.getStringList(field));
				} else if (playersYaml.isBoolean(field)) {
					players_booleans.put(field, playersYaml.getBoolean(field));
				}
			}
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static Object get(String field) {
		return players_data.get(field);
	}
	
	public static ConfigurationSection getConfigurationSection(String field) {
		return players_configuration_sections.get(field);
	}
	
	public static String getString(String field) {
		return players_strings.get(field);
	}
	
	public static List<String> getStringList(String field) {
		return players_string_lists.get(field);
	}
	
	public static boolean getBoolean(String field) {
		return players_booleans.get(field);
	}

	public static boolean contains(String field) {
		return players_data.containsKey(field);
	}

	public static void set(String field, Object data) {
		final File playersFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
		final YamlConfiguration playersYaml = new YamlConfiguration();

		try {
			playersYaml.load(playersFile);
			players_data.put(field, data);
			playersYaml.set(field, data);
			playersYaml.save(playersFile);
			update();
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static Long getLong(String field) {
		return players_longs.get(field);
	}
}
