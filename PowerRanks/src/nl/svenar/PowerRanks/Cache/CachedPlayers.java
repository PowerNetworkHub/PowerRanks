package nl.svenar.PowerRanks.Cache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Database.PowerDatabase;

public class CachedPlayers {

	private static PowerDatabase prdb;
	private static boolean is_ready = false;

	private static HashMap<String, Object> players_data = new HashMap<String, Object>();

	private static HashMap<String, PowerConfigurationSection> players_configuration_sections = new HashMap<String, PowerConfigurationSection>();
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

	@SuppressWarnings("unchecked")
	public static void update() {
		final File playersFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
		final YamlConfiguration playersYaml = new YamlConfiguration();

		players_data.clear();
		players_configuration_sections.clear();
		players_strings.clear();
		players_string_lists.clear();
		players_booleans.clear();
		players_ints.clear();
		players_longs.clear();

		if (!prdb.isDatabase()) {
			try {
				playersYaml.load(playersFile);
				for (String field : playersYaml.getConfigurationSection("").getKeys(true)) {
					players_data.put(field, playersYaml.get(field));

					if (playersYaml.isConfigurationSection(field)) {
						// players_configuration_sections.put(field,
						// playersYaml.getConfigurationSection(field));
						setupPowerConfigurationSectionFromYaml(field, playersYaml.getConfigurationSection(field));
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
			HashMap<String, Object> db_data = prdb.getAllFields(prdb.table_users);
			for (Entry<String, Object> entry : db_data.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				
//				PowerRanks.log.warning(key + ": " + value);
				players_data.put(key, value);
				
				if (value instanceof ArrayList) {
					PowerConfigurationSection pcs = new PowerConfigurationSection();
						for (String rankname : (ArrayList<String>)value) {
							pcs.set(rankname, null);
						}
						players_configuration_sections.put(key, pcs);
				} else if (value instanceof HashMap) {
					PowerConfigurationSection pcs = new PowerConfigurationSection();
					for (Entry<String, Object> cs_entry : ((HashMap<String, Object>)value).entrySet()) {
						pcs.set(cs_entry.getKey(), cs_entry.getValue());
					}
					players_configuration_sections.put(key, pcs);
				} else if (value instanceof String) {
					players_strings.put(key, (String) value);
				} else if (value instanceof List) {
					players_string_lists.put(key, (List<String>) value);
				} else if (value instanceof Boolean) {
					players_booleans.put(key, (boolean) value);
				} else if (value instanceof Integer) {
					players_ints.put(key, (int) value);
				} else if (value instanceof Long) {
					players_longs.put(key, (Long) value);
				} else if (value instanceof Double) {
					players_doubles.put(key, (Double) value);
				}
			}
			// TODO: Load from DB into Cache
		}

		is_ready = true;
	}

	public static void save() {
		if (prdb == null || !prdb.isDatabase()) {
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

	private static void setupPowerConfigurationSectionFromYaml(String field, ConfigurationSection cs) {
		PowerConfigurationSection pcs = new PowerConfigurationSection(cs);
		players_configuration_sections.put(field, pcs);
	}

	public static Object get(String field) {
		return players_data.get(field);
	}

	public static PowerConfigurationSection getConfigurationSection(String field) {
		return players_configuration_sections.get(field);
	}
	
	public static String getString(String field) {
		return players_strings.get(field);
//		return players_strings.get(field) != null ? players_strings.get(field) : (String) get(field);
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

	@SuppressWarnings("unchecked")
	public static void set(String field, Object data, boolean cache_only) {
		if (cache_only) {
			players_data.put(field, data);
			if (data instanceof String) {
				players_strings.put(field, field);
			} else if (data instanceof List) {
				players_string_lists.put(field, (List<String>) data);
			} else if (data instanceof Boolean) {
				players_booleans.put(field, (boolean) data);
			} else if (data instanceof Integer) {
				players_ints.put(field, (Integer) data);
			} else if (data instanceof Long) {
				players_longs.put(field, (Long) data);
			} else if (data instanceof Double) {
				players_doubles.put(field, (Double) data);
			}
		} else {
			if (!prdb.isDatabase()) {
				final File playersFile = new File(String.valueOf(PowerRanks.fileLoc) + "Players" + ".yml");
				final YamlConfiguration playersYaml = new YamlConfiguration();

				try {
					playersYaml.load(playersFile);
					playersYaml.set(field, data);
					playersYaml.save(playersFile);
					
					players_data.put(field, data);
					if (data instanceof String) {
						players_strings.put(field, field);
					} else if (data instanceof List) {
						players_string_lists.put(field, (List<String>) data);
					} else if (data instanceof Boolean) {
						players_booleans.put(field, (boolean) data);
					} else if (data instanceof Integer) {
						players_ints.put(field, (Integer) data);
					} else if (data instanceof Long) {
						players_longs.put(field, (Long) data);
					} else if (data instanceof Double) {
						players_doubles.put(field, (Double) data);
					}
					
					update();
				} catch (IOException | InvalidConfigurationException e) {
					e.printStackTrace();
				}
			} else {
				// TODO: Set field in DB
				players_data.put(field, data);
				if (data instanceof String) {
					players_strings.put(field, field);
				} else if (data instanceof List) {
					players_string_lists.put(field, (List<String>) data);
				} else if (data instanceof Boolean) {
					players_booleans.put(field, (boolean) data);
				} else if (data instanceof Integer) {
					players_ints.put(field, (Integer) data);
				} else if (data instanceof Long) {
					players_longs.put(field, (Long) data);
				} else if (data instanceof Double) {
					players_doubles.put(field, (Double) data);
				}
				
				prdb.setField(prdb.table_users, field, data);
				update();
//			PowerRanks.log.info("[CachedPlayers] set(3), field: '" + field + "' value: '" + data + "'");
			}
		}
	}

	public static void set(HashMap<String, Object> data, boolean cache_only) {
		if (data.size() == 0) {
			return;
		}

		if (cache_only) {
			for (Entry<String, Object> kv : data.entrySet()) {
				players_data.put(kv.getKey(), kv.getValue());
			}
		} else {
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
				for (Entry<String, Object> kv : data.entrySet()) {
					players_data.put(kv.getKey(), kv.getValue());
					prdb.setField(prdb.table_users, kv.getKey(), kv.getValue());
//					PowerRanks.log.info("[CachedPlayers] set(2), field: '" + kv.getKey() + "' value: '" + kv.getValue() + "'");
				}
			}
		}
	}
	
	public static void updatePlayer(Player player, HashMap<String, Object> data) {
		if (!prdb.isDatabase()) {
			set(data, false);
		} else {
			for (Entry<String, Object> kv : data.entrySet()) {
				players_data.put(kv.getKey(), kv.getValue());
				prdb.updatePlayer(player, kv.getKey(), (String) kv.getValue());
//				PowerRanks.log.info("[CachedPlayers] set(1), field: '" + kv.getKey() + "' value: '" + kv.getValue() + "'");
			}
		}
	}

	public static boolean is_ready() {
		return is_ready;
	}
}
