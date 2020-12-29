package nl.svenar.PowerRanks.Cache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Database.PowerDatabase;

public class CachedRanks {

	private static PowerDatabase prdb;

	private static HashMap<String, Object> ranks_data = new HashMap<String, Object>();

	private static HashMap<String, PowerConfigurationSection> ranks_configuration_sections = new HashMap<String, PowerConfigurationSection>();
	private static HashMap<String, String> ranks_strings = new HashMap<String, String>();
	private static HashMap<String, List<String>> ranks_string_lists = new HashMap<String, List<String>>();
	private static HashMap<String, Boolean> ranks_booleans = new HashMap<String, Boolean>();
	private static HashMap<String, Integer> ranks_ints = new HashMap<String, Integer>();

	public CachedRanks(PowerRanks pr, PowerDatabase prdb) {
		CachedRanks.prdb = prdb;
		update();
	}

	@SuppressWarnings("unchecked")
	public static void update() {
		final File ranksFile = new File(String.valueOf(PowerRanks.fileLoc) + "Ranks" + ".yml");
		final YamlConfiguration ranksYaml = new YamlConfiguration();

		ranks_data.clear();
		ranks_configuration_sections.clear();
		ranks_strings.clear();
		ranks_string_lists.clear();
		ranks_booleans.clear();
		ranks_ints.clear();

		if (!prdb.isDatabase()) {
			try {
				ranksYaml.load(ranksFile);
				for (String field : ranksYaml.getConfigurationSection("").getKeys(true)) {
					ranks_data.put(field, ranksYaml.get(field));

					if (ranksYaml.isConfigurationSection(field)) {
//					ranks_configuration_sections.put(field, ranksYaml.getConfigurationSection(field));
						setupPowerConfigurationSectionFromYaml(field, ranksYaml.getConfigurationSection(field));
					} else if (ranksYaml.isString(field)) {
						ranks_strings.put(field, ranksYaml.getString(field));
					} else if (ranksYaml.isList(field)) {
						ranks_string_lists.put(field, ranksYaml.getStringList(field));
					} else if (ranksYaml.isBoolean(field)) {
						ranks_booleans.put(field, ranksYaml.getBoolean(field));
					} else if (ranksYaml.isInt(field)) {
						ranks_ints.put(field, ranksYaml.getInt(field));
					} else if (ranksYaml.isDouble(field)) {
						ranks_ints.put(field, (int) ranksYaml.getDouble(field));
					}
				}
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		} else {
			// TODO: Load from DB into Cache
			HashMap<String, Object> db_data = prdb.getAllFields(prdb.table_ranks);
			for (Entry<String, Object> entry : db_data.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				ranks_data.put(key, value);

				String[] key_split = key.split("\\.");
				if (value instanceof ArrayList) {
					PowerConfigurationSection pcs = new PowerConfigurationSection();
					for (String rankname : (ArrayList<String>) value) {
						pcs.set(rankname, null);
					}
					ranks_configuration_sections.put(key, pcs);
				} else if (value instanceof HashMap) {
					PowerConfigurationSection pcs = new PowerConfigurationSection();
					for (Entry<String, Object> cs_entry : ((HashMap<String, Object>) value).entrySet()) {
						pcs.set(cs_entry.getKey(), cs_entry.getValue());
					}
					ranks_configuration_sections.put(key, pcs);
				} else if (value instanceof String) {
					//ranks_strings.put(key, (String) value);
					if (key_split.length > 0 && (key_split[key_split.length - 1].equals("permissions") || key_split[key_split.length - 1].equals("inheritance") || key_split[key_split.length - 1].equals("buyable"))) {
						ArrayList<String> value_list = new ArrayList<String>();
						String[] value_split = ((String) value).split(",");
						for (int i = 0; i < value_split.length; i++) {
							if (value_split[i].length() > 0) {
								value_list.add(value_split[i]);
							}
						}
//						String[] value_list = ((String) value).split(",");
						ranks_string_lists.put(key, value_list);
					} else {
						ranks_strings.put(key, (String) value);
					}
				} else if (value instanceof List) {
					ranks_string_lists.put(key, (List<String>) value);
				} else if (value instanceof Boolean) {
					ranks_booleans.put(key, (boolean) value);
				} else if (value instanceof Integer) {
					ranks_ints.put(key, (int) value);
				} else if (value instanceof String[]) {
					ranks_string_lists.put(key, Arrays.asList((String[]) value));
					PowerRanks.log.info("KEY ------>>> " + key + " '" + value + "' (" + Arrays.toString((String[])value) + ") (" + Arrays.asList((String[]) value).size() + ") str? " + (value instanceof String[]));
				} else {
//					PowerRanks.log.info("KEY ------>>> " + key + " (" + key_split.length + ") '" + value + "' (" + Arrays.toString((String[])value) + ") str? " + (value instanceof String[]));

				}
			}
			String default_rank = prdb.getDefaultRank();
			ranks_data.put("Default", default_rank);
			ranks_strings.put("Default", default_rank);
		}
	}

	private static void setupPowerConfigurationSectionFromYaml(String field, ConfigurationSection cs) {
		PowerConfigurationSection pcs = new PowerConfigurationSection(cs);
		ranks_configuration_sections.put(field, pcs);
	}

	public static Object get(String field) {
		return ranks_data.get(field);
	}

	public static PowerConfigurationSection getConfigurationSection(String field) {
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
		if (!prdb.isDatabase()) {
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
		} else {
			PowerRanks.log.info("--->>> STORE > " + field + ": " + data);
			prdb.setField(prdb.table_ranks, field, data);
			update();
		}
	}

	public static int getInt(String field) {
		return ranks_ints.get(field);
	}

	public static void removeRank(String rank) {
		if (!prdb.isDatabase()) {
			CachedRanks.set("Groups." + rank, (Object) null);
		} else {
			prdb.removeRank(rank);
			update();
		}
	}
}
