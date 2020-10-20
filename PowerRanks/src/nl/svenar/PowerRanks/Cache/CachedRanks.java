package nl.svenar.PowerRanks.Cache;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.PowerRanks.PowerRanks;

public class CachedRanks {

	private static HashMap<String, Object> ranks_data = new HashMap<String, Object>();
	
	private static HashMap<String, ConfigurationSection> ranks_configuration_sections = new HashMap<String, ConfigurationSection>();
	private static HashMap<String, String> ranks_strings = new HashMap<String, String>();
	private static HashMap<String, List<String>> ranks_string_lists = new HashMap<String, List<String>>();
	private static HashMap<String, Boolean> ranks_booleans = new HashMap<String, Boolean>();
	private static HashMap<String, Integer> ranks_ints = new HashMap<String, Integer>();
	
	public CachedRanks(PowerRanks pr) {
		update();
	}
	
	public static void update() {
		final File ranksFile = new File(String.valueOf(PowerRanks.fileLoc) + "Ranks" + ".yml");
		final YamlConfiguration ranksYaml = new YamlConfiguration();
		
		ranks_data.clear();
		ranks_configuration_sections.clear();
		ranks_strings.clear();
		ranks_string_lists.clear();
		ranks_booleans.clear();
		ranks_ints.clear();
		
		try {
			ranksYaml.load(ranksFile);
			for (String field : ranksYaml.getConfigurationSection("").getKeys(true)) {
				ranks_data.put(field, ranksYaml.get(field));

				if (ranksYaml.isConfigurationSection(field)) {
					ranks_configuration_sections.put(field, ranksYaml.getConfigurationSection(field));
				} else if (ranksYaml.isString(field)) {
					ranks_strings.put(field, ranksYaml.getString(field));
				} else if (ranksYaml.isList(field)) {
					ranks_string_lists.put(field, ranksYaml.getStringList(field));
				} else if (ranksYaml.isBoolean(field)) {
					ranks_booleans.put(field, ranksYaml.getBoolean(field));
				} else if (ranksYaml.isInt(field)) {
					ranks_ints.put(field, ranksYaml.getInt(field));
				}
			}
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static Object get(String field) {
		return ranks_data.get(field);
	}
	
	public static ConfigurationSection getConfigurationSection(String field) {
		return ranks_configuration_sections.get(field);
	}
	
	public static String getString(String field) {
		return ranks_strings.get(field);
	}
	
	public static List<String> getStringList(String field) {
		return ranks_string_lists.get(field);
	}
	
	public static boolean getBoolean(String field) {
		return ranks_booleans.get(field);
	}

	public static boolean contains(String field) {
		return ranks_data.containsKey(field);
	}

	public static void set(String field, Object data) {
		final File ranksFile = new File(String.valueOf(PowerRanks.fileLoc) + "Ranks" + ".yml");
		final YamlConfiguration ranksYaml = new YamlConfiguration();

		try {
			ranksYaml.load(ranksFile);
			ranks_data.put(field, data);
			ranksYaml.set(field, data);
			ranksYaml.save(ranksFile);
			update();
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static int getInt(String field) {
		return ranks_ints.get(field);
	}
}
