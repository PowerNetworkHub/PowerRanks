package nl.svenar.PowerRanks.update;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.PowerRanks.Cache.CachedConfig;
import nl.svenar.PowerRanks.Cache.CachedPlayers;
import nl.svenar.PowerRanks.Cache.CachedRanks;
import nl.svenar.PowerRanks.Data.Users;

public class ConfigFilesUpdater {

	public static void updateConfigFiles(PowerRanks plugin) {
		boolean updateConfigYAML = checkVersion(PowerRanks.configFileLoc, "config.yml", plugin);
		boolean updateLangYAML = checkVersion(PowerRanks.configFileLoc, "lang.yml", plugin);
		boolean updateRanksYAML = checkVersion(PowerRanks.fileLoc, "Ranks.yml", plugin);
		boolean updatePlayersYAML = checkVersion(PowerRanks.fileLoc, "Players.yml", plugin);

		if (updateConfigYAML) {
			copyTmpFile(plugin, "config.yml");
			final File file = new File(plugin.getDataFolder(), "config.yml");
			final File tmpFile = new File(plugin.getDataFolder() + File.separator + "tmp", "config.yml");
			final YamlConfiguration yamlConf = new YamlConfiguration();
			final YamlConfiguration tmpYamlConf = new YamlConfiguration();
			try {
				tmpYamlConf.load(tmpFile);
				yamlConf.load(file);
				yamlConf.set("version", null);
				if (yamlConf.isSet("plugin_hook.vault")) {
					yamlConf.set("plugin_hook.vault_economy", yamlConf.getBoolean("plugin_hook.vault"));
					yamlConf.set("plugin_hook.vault", null);
				}
				for (String key : tmpYamlConf.getConfigurationSection("").getKeys(false)) {
					for (String key2 : tmpYamlConf.getConfigurationSection(key).getKeys(false)) {
						String field = key + "." + key2;
						if (!yamlConf.contains(field)) {
							yamlConf.set(field, tmpYamlConf.get(field));
						}
					}
				}
				yamlConf.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
				yamlConf.save(file);
				CachedConfig.update();
				//CachedConfig.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			deleteTmpFile(plugin, "config.yml");
		}

		if (updateLangYAML) {
			copyTmpFile(plugin, "lang.yml");
			final File file = new File(plugin.getDataFolder(), "lang.yml");
			final File tmpFile = new File(plugin.getDataFolder() + File.separator + "tmp", "lang.yml");
			final YamlConfiguration yamlConf = new YamlConfiguration();
			final YamlConfiguration tmpYamlConf = new YamlConfiguration();
			try {
				tmpYamlConf.load(tmpFile);
				yamlConf.load(file);
				yamlConf.set("version", null);
				yamlConf.set("commands.help", null);
				for (String key : tmpYamlConf.getConfigurationSection("").getKeys(false)) {
					for (String key2 : tmpYamlConf.getConfigurationSection(key).getKeys(false)) {
						String field = key + "." + key2;
						if (!yamlConf.contains(field)) {
							yamlConf.set(field, tmpYamlConf.get(field));
						}
					}
				}
//				yamlConf.set("commands.help", null);
				yamlConf.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
				yamlConf.save(file);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			deleteTmpFile(plugin, "lang.yml");
		}

		if (updateRanksYAML) {
			copyTmpFile(plugin, "Ranks.yml");
			final File file = new File(plugin.getDataFolder() + File.separator + "Ranks", "Ranks.yml");
			final File tmpFile = new File(plugin.getDataFolder() + File.separator + "tmp", "Ranks.yml");
			final YamlConfiguration yamlConf = new YamlConfiguration();
			final YamlConfiguration tmpYamlConf = new YamlConfiguration();
			try {
				tmpYamlConf.load(tmpFile);
				yamlConf.load(file);
				yamlConf.set("version", null);
				String tmp_group_name = tmpYamlConf.getConfigurationSection("Groups").getKeys(false).toArray()[0].toString();

				for (String key : yamlConf.getConfigurationSection("Groups").getKeys(false)) {
					for (String tmp_key : tmpYamlConf.getConfigurationSection("Groups." + tmp_group_name).getKeys(true)) {
						String tmp_field = "Groups." + tmp_group_name + "." + tmp_key;
						String field = "Groups." + key + "." + tmp_key;
						if (!yamlConf.contains(field)) {
							if (tmpYamlConf.isBoolean(tmp_field) || tmpYamlConf.isString(tmp_field) || tmpYamlConf.isInt(tmp_field) || tmpYamlConf.isDouble(tmp_field)) {
								yamlConf.set(field, tmpYamlConf.get(tmp_field));
							}
							if (tmpYamlConf.isList(tmp_field)) {
								List<String> list = new ArrayList<String>();
//								for (String line : tmpYamlConf.getStringList(tmp_field)) {
//									list.add(line);
//								}
//								yamlConf.set(field, (Object) list);

								try {
									System.arraycopy(tmpYamlConf.getStringList(tmp_field), 0, list, 0, tmpYamlConf.getStringList(tmp_field).size());
								} catch (Exception e) {
								}
								yamlConf.set(field, list);
							}
						}
						if ((tmpYamlConf.isBoolean(tmp_field) != yamlConf.isBoolean(field)) || (tmpYamlConf.isList(tmp_field) != yamlConf.isList(field)) || (tmpYamlConf.isString(tmp_field) != yamlConf.isString(field))
								|| (tmpYamlConf.isInt(tmp_field) != yamlConf.isInt(field)) || (tmpYamlConf.isDouble(tmp_field) != yamlConf.isDouble(field))) {
							yamlConf.set(field, tmpYamlConf.get(tmp_field));
						}
					}
				}

				if (!yamlConf.isSet("Usertags"))
					yamlConf.set("Usertags", tmpYamlConf.get("Usertags"));

				yamlConf.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
				yamlConf.save(file);
				CachedRanks.update();
				//CachedRanks.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			deleteTmpFile(plugin, "Ranks.yml");
		}

		if (updatePlayersYAML) {
			final File file = new File(plugin.getDataFolder() + File.separator + "Ranks", "Players.yml");
			final YamlConfiguration yamlConf = new YamlConfiguration();
			try {
				yamlConf.load(file);
				yamlConf.set("version", null);
				Users users = new Users(plugin);
				if (yamlConf.contains("players")) {
					try {
						for (String key : yamlConf.getConfigurationSection("players").getKeys(false)) {
							if (yamlConf.isString("players." + key)) {
								yamlConf.set("players." + key, null);
								yamlConf.set("players." + key + ".rank", users.getDefaultRanks());
								yamlConf.set("players." + key + ".name", "Unknown");
								yamlConf.set("players." + key + ".playtime", 0);
							}
						}
					} catch (Exception e) {
					}
				}
				yamlConf.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
				yamlConf.save(file);
				CachedPlayers.update();
				//CachedPlayers.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""), false);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}

		if (new File(plugin.getDataFolder() + File.separator + "tmp").exists()) {
			new File(plugin.getDataFolder() + File.separator + "tmp").delete();
		}
	}

	private static void copyTmpFile(PowerRanks plugin, String yamlFileName) {
		File tmp_file = new File(plugin.getDataFolder() + File.separator + "tmp", yamlFileName);
		if (!tmp_file.exists())
			tmp_file.getParentFile().mkdirs();
		plugin.copy(plugin.getResource(yamlFileName), tmp_file);
	}

	private static void deleteTmpFile(PowerRanks plugin, String yamlFileName) {
		File tmp_file = new File(plugin.getDataFolder() + File.separator + "tmp", yamlFileName);
		if (tmp_file.exists())
			tmp_file.delete();
	}

	private static boolean checkVersion(String file_path, String fileName, PowerRanks plugin) {
		final File file = new File(file_path + fileName);
		final YamlConfiguration yamlConf = new YamlConfiguration();
		try {
			yamlConf.load(file);

			if (yamlConf.getString("version") == null) {
				yamlConf.set("version", PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""));
				yamlConf.save(file);
				PowerRanks.log.info("Setting up file: " + fileName);
				return !plugin.configContainsKey("updates.automatic_update_config_files") || plugin.getConfigBool("updates.automatic_update_config_files");
			} else {
				if (!yamlConf.getString("version").equalsIgnoreCase(PowerRanks.pdf.getVersion().replaceAll("[a-zA-Z ]", ""))) {
					if (!plugin.configContainsKey("updates.automatic_update_config_files") || plugin.getConfigBool("updates.automatic_update_config_files")) {
						plugin.printVersionError(fileName, true);
						return true;
					} else {
						plugin.printVersionError(fileName, false);
						return false;
					}
				}
			}

		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		return false;
	}
}