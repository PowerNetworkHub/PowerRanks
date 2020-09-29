package nl.svenar.PowerRanks.Cache;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.database.PowerDatabase;

public class CachedPlayers {

	private static PowerDatabase prdb;
	private static boolean is_ready = false;

	private static HashMap<String, Object> players_data = new HashMap<String, Object>();

	private static HashMap<String, ConfigurationSection> players_configuration_sections = new HashMap<String, ConfigurationSection>();
	private static HashMap<String, String> players_strings = new HashMap<String, String>();
	private static HashMap<String, List<String>> players_string_lists = new HashMap<String, List<String>>();
	private static HashMap<String, Boolean> players_booleans = new HashMap<String, Boolean>();
	private static HashMap<String, Integer> players_ints = new HashMap<String, Integer>();
	private static HashMap<String, Long> players_longs = new HashMap<String, Long>();
	private static HashMap<String, Double> players_doubles = new HashMap<String, Double>();

	public CachedPlayers(PowerRanks pr, PowerDatabase prdb) {
		CachedPlayers.prdb = prdb;
		update();
	}

	public static void update() {
		players_data.clear();
		players_configuration_sections.clear();
		players_strings.clear();
		players_string_lists.clear();
		players_booleans.clear();
		players_ints.clear();
		players_longs.clear();

		if (!prdb.isDatabase()) {
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
					} else if (playersYaml.isInt(field)) {
						players_ints.put(field, playersYaml.getInt(field));
					} else if (playersYaml.isLong(field)) {
						players_longs.put(field, playersYaml.getLong(field));
					} else if (playersYaml.isDouble(field)) {
						players_doubles.put(field, playersYaml.getDouble(field));
					}
				}
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			// TODO: Load from DB into Cache
		}

		is_ready = true;
	}

	public static void save() {
		if (!prdb.isDatabase()) {
			final File playersFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
			final YamlConfiguration playersYaml = new YamlConfiguration();

			try {
				playersYaml.load(playersFile);
				for (Entry<String, Object> kv : players_data.entrySet()) {
					playersYaml.set(kv.getKey(), kv.getValue());
				}
				playersYaml.save(playersFile);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			// TODO: Save to in DB
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

	public static int getInt(String field) {
		return players_ints.get(field);
	}

	public static Long getLong(String field) {
		return players_longs.get(field);
	}

	public static Double getDouble(String field) {
		return players_doubles.get(field);
	}

	public static void set(String field, Object data, boolean cache_only) {
		if (cache_only) {
			players_data.put(field, data);
		} else {
			if (!prdb.isDatabase()) {
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
			} else {
				// TODO: Set field in DB
			}
		}

	}

	public static void set(HashMap<String, Object> data) {
		if (data.size() == 0) {
			return;
		}
		if (!prdb.isDatabase()) {

			final File playersFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
			final YamlConfiguration playersYaml = new YamlConfiguration();

			try {
				playersYaml.load(playersFile);
				for (Entry<String, Object> kv : data.entrySet()) {
					players_data.put(kv.getKey(), kv.getValue());
					playersYaml.set(kv.getKey(), kv.getValue());
				}
				playersYaml.save(playersFile);
				update();
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			// TODO: Set field in DB
		}
	}

	public static boolean is_ready() {
		return is_ready;
	}
}
