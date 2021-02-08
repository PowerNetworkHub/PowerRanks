package nl.svenar.PowerRanks.Cache;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.PowerRanks.PowerRanks;

public class CachedConfig {

	private static HashMap<String, Object> config_data = new HashMap<String, Object>();
	
	private static HashMap<String, ConfigurationSection> config_configuration_sections = new HashMap<String, ConfigurationSection>();
	private static HashMap<String, String> config_strings = new HashMap<String, String>();
	private static HashMap<String, List<String>> config_string_lists = new HashMap<String, List<String>>();
	private static HashMap<String, Integer> config_ints = new HashMap<String, Integer>();
	private static HashMap<String, Boolean> config_booleans = new HashMap<String, Boolean>();
	
	private static PowerRanks pr;
	
	public CachedConfig(PowerRanks pr) {
		CachedConfig.pr = pr;
		update();
	}
	
	public static void update() {
		final File configFile = new File(pr.getDataFolder() + File.separator + "config" + ".yml");
		final YamlConfiguration configYaml = new YamlConfiguration();
		
		config_data.clear();
		config_configuration_sections.clear();
		config_strings.clear();
		config_string_lists.clear();
		config_booleans.clear();
		
		try {
			configYaml.load(configFile);
			for (String field : configYaml.getConfigurationSection("").getKeys(true)) {
				config_data.put(field, configYaml.get(field));
				
				if (configYaml.isConfigurationSection(field)) {
					config_configuration_sections.put(field, configYaml.getConfigurationSection(field));
				} else if (configYaml.isString(field)) {
					config_strings.put(field, configYaml.getString(field));
				} else if (configYaml.isList(field)) {
					config_string_lists.put(field, configYaml.getStringList(field));
				} else if (configYaml.isBoolean(field)) {
					config_booleans.put(field, configYaml.getBoolean(field));
				} else if (configYaml.isInt(field)) {
					config_ints.put(field, configYaml.getInt(field));
				} else if (configYaml.isDouble(field)) {
					config_ints.put(field, (int) configYaml.getDouble(field));
				}
			}
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static Object get(String field) {
		return config_data.get(field);
	}
	
	public static ConfigurationSection getConfigurationSection(String field) {
		return config_configuration_sections.get(field);
	}
	
	public static String getString(String field) {
		return config_strings.get(field);
	}
	
	public static List<String> getStringList(String field) {
		return config_string_lists.get(field);
	}
	
	public static boolean getBoolean(String field) {
		return config_booleans.get(field);
	}

	public static boolean contains(String field) {
		return config_data.containsKey(field);
	}

	public static void set(String field, Object data) {
		final File configFile = new File(pr.getDataFolder() + File.separator + "config" + ".yml");
		final YamlConfiguration configYaml = new YamlConfiguration();

		try {
			configYaml.load(configFile);
			config_data.put(field, data);
			configYaml.set(field, data);
			configYaml.save(configFile);
			update();
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}
