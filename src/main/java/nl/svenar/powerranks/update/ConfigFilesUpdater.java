package nl.svenar.powerranks.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.svenar.common.storage.PowerConfigManager;
import nl.svenar.common.storage.PowerStorageManager;
import nl.svenar.common.storage.provided.YAMLConfigManager;
import nl.svenar.common.storage.provided.YAMLStorageManager;
import nl.svenar.common.structure.PRPermission;
import nl.svenar.common.structure.PRPlayer;
import nl.svenar.common.structure.PRPlayerRank;
import nl.svenar.common.structure.PRRank;
import nl.svenar.powerranks.PowerRanks;
import nl.svenar.powerranks.cache.CacheManager;

public class ConfigFilesUpdater {

	private static File backupDir, backupRanks, backupConfig, backupLang, backupPlayers, oldRanksFile, newRanksFile,
			oldPlayersFile, newPlayersFile, usertagsFile, configFile, langFile;

	/**
	 * Update older PowerRanks <v1.9.9 data to the new PowerRanks v1.10+ format
	 */
	public static void updateOldDataFiles() {
		backupDir = new File(PowerRanks.fileLoc + File.separator + "backup" + File.separator + "old");
		backupRanks = new File(
				PowerRanks.fileLoc + File.separator + "backup" + File.separator + "old" + File.separator + "Ranks.yml");
		backupPlayers = new File(PowerRanks.fileLoc + File.separator + "backup" + File.separator + "old"
				+ File.separator + "Players.yml");
		backupConfig = new File(PowerRanks.fileLoc + File.separator + "backup" + File.separator + "old" + File.separator
				+ "config.yml");
		backupLang = new File(
				PowerRanks.fileLoc + File.separator + "backup" + File.separator + "old" + File.separator + "lang.yml");

		oldRanksFile = new File(PowerRanks.fileLoc + File.separator + "Ranks" + File.separator + "Ranks.yml");
		newRanksFile = new File(PowerRanks.fileLoc + File.separator + "ranks.yml");
		oldPlayersFile = new File(PowerRanks.fileLoc + File.separator + "Ranks" + File.separator + "Players.yml");
		newPlayersFile = new File(PowerRanks.fileLoc + File.separator + "players.yml");
		usertagsFile = new File(PowerRanks.fileLoc + File.separator + "usertags.yml");

		configFile = new File(PowerRanks.fileLoc + File.separator + "config.yml");
		langFile = new File(PowerRanks.fileLoc + File.separator + "lang.yml");

		if (Files.exists(oldRanksFile.toPath())) {
			PowerRanks.getInstance().getLogger().warning("Converting data from a previous installation!");

			PowerConfigManager oldConfigManager = new YAMLConfigManager(PowerRanks.fileLoc, "config.yml");
			PowerConfigManager oldLanguageManager = new YAMLConfigManager(PowerRanks.fileLoc, "lang.yml");

			// Create backup directory
			try {
				Files.createDirectories(backupDir.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Move old files to the backup directory
			try {
				if (Files.exists(oldRanksFile.toPath())) {
					Files.move(oldRanksFile.toPath(), backupRanks.toPath());
				}
				if (Files.exists(oldPlayersFile.toPath())) {
					Files.move(oldPlayersFile.toPath(), backupPlayers.toPath());
				}
				if (Files.exists(configFile.toPath())) {
					Files.move(configFile.toPath(), backupConfig.toPath());
				}
				if (Files.exists(langFile.toPath())) {
					Files.move(langFile.toPath(), backupLang.toPath());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Delete Ranks directory
			try {
				Files.delete(new File(PowerRanks.fileLoc + File.separator + "Ranks").toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}

			PowerRanks.getInstance().getLogger().warning("Finished backing up data from a previous version.");

			// Loading all files
			PowerStorageManager storageManager = new YAMLStorageManager(PowerRanks.fileLoc, newRanksFile.getName(),
					newPlayersFile.getName());
			storageManager.loadAll();

			ArrayList<PRRank> storedRanks = new ArrayList<PRRank>(); // (ArrayList<PRRank>) storageManager.getRanks();
			ArrayList<PRPlayer> storedPlayers = new ArrayList<PRPlayer>(); // (ArrayList<PRPlayer>)
																			// storageManager.getPlayers();

			PowerConfigManager newConfigManager = new YAMLConfigManager(PowerRanks.fileLoc, configFile.getName(),
					"config.yml");
			PowerConfigManager newLanguageManager = new YAMLConfigManager(PowerRanks.fileLoc, langFile.getName(),
					"lang.yml");
			PowerConfigManager usertagManager = new YAMLConfigManager(PowerRanks.fileLoc, usertagsFile.getName());

			YamlConfiguration ranksYaml = new YamlConfiguration();
			try {
				ranksYaml.load(backupRanks);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}

			YamlConfiguration playersYaml = new YamlConfiguration();
			try {
				playersYaml.load(backupPlayers);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}

			// Converting all files
			Map<String, Object> configData = newConfigManager.getRawData();
			for (Entry<String, Object> entry : oldConfigManager.getRawData().entrySet()) {
				if (!entry.getKey().contains("version")) {
					configData.put(entry.getKey(), entry.getValue());
				}
			}
			newConfigManager.setRawData(configData);
			@SuppressWarnings("unchecked")
			Map<String, Object> generalConfigData = (Map<String, Object>) newConfigManager.getMap("general",
					new HashMap<String, Object>());
			// generalConfigData.put("defaultrank", ranksYaml.getString("Default"));
			CacheManager.configConverterSetDefaultRank(ranksYaml.getString("Default"));
			newConfigManager.setMap("general", generalConfigData);
			newConfigManager.save();

			Map<String, Object> languageData = newLanguageManager.getRawData();
			for (Entry<String, Object> entry : oldLanguageManager.getRawData().entrySet()) {
				if (!entry.getKey().contains("version")) {
					languageData.put(entry.getKey(), entry.getValue());
				}
			}
			newLanguageManager.setRawData(languageData);
			newLanguageManager.save();

			if (!ranksYaml.isString("Usertags")) {
				for (String usertagKey : ranksYaml.getConfigurationSection("Usertags").getKeys(false)) {
					usertagManager.setString("usertags." + usertagKey, ranksYaml.getString("Usertags." + usertagKey));
				}
				usertagManager.save();
			}

			for (String rankName : ranksYaml.getConfigurationSection("Groups").getKeys(false)) {
				PRRank newRank = new PRRank();
				newRank.setName(rankName);

				for (String perm : new ArrayList<String>(
						ranksYaml.getStringList("Groups." + rankName + ".permissions"))) {
					PRPermission newPermission = new PRPermission();
					newPermission.setName(perm);
					newRank.addPermission(newPermission);
				}
				newRank.setInheritances(
						new ArrayList<String>(ranksYaml.getStringList("Groups." + rankName + ".inheritance")));

				newRank.setPrefix(ranksYaml.getString("Groups." + rankName + ".chat.prefix"));
				newRank.setSuffix(ranksYaml.getString("Groups." + rankName + ".chat.suffix"));
				newRank.setChatcolor(ranksYaml.getString("Groups." + rankName + ".chat.chatColor"));
				newRank.setNamecolor(ranksYaml.getString("Groups." + rankName + ".chat.nameColor"));

				// newRank.setPromoteRank(ranksYaml.getString("Groups." + rankName + ".level.promote"));
				// newRank.setDemoteRank(ranksYaml.getString("Groups." + rankName + ".level.demote"));

				newRank.setBuyableRanks(
						new ArrayList<String>(ranksYaml.getStringList("Groups." + rankName + ".economy.buyable")));
				newRank.setBuyCost(ranksYaml.getInt("Groups." + rankName + ".economy.cost"));
				newRank.setBuyDescription(ranksYaml.getString("Groups." + rankName + ".economy.description"));

				storedRanks.add(newRank);
			}

			for (String playerUUID : playersYaml.getConfigurationSection("players").getKeys(false)) {
				PRPlayer newPlayer = new PRPlayer();

				newPlayer.setUUID(UUID.fromString(playerUUID));
				newPlayer.setName(playersYaml.getString("players." + playerUUID + ".name"));
				newPlayer.setRank(new PRPlayerRank(playersYaml.getString("players." + playerUUID + ".rank")));
				newPlayer.setPlaytime(playersYaml.getInt("players." + playerUUID + ".playtime"));
				if (playersYaml.getString("players." + playerUUID + ".usertag").length() > 0) {
					newPlayer.addUsertag(playersYaml.getString("players." + playerUUID + ".usertag"));
				}

				for (String perm : new ArrayList<String>(
						playersYaml.getStringList("players." + playerUUID + ".permissions"))) {
					PRPermission newPermission = new PRPermission();
					newPermission.setName(perm);
					newPlayer.addPermission(newPermission);
				}

				if (!playersYaml.isString("players." + playerUUID + ".subranks")) {
					for (String playerSubrankName : playersYaml.getConfigurationSection("players." + playerUUID + ".subranks").getKeys(false)) {
						newPlayer.addRank(new PRPlayerRank(playerSubrankName));
				// 		PRSubrank newSubrank = new PRSubrank();

				// 		newSubrank.setName(playerSubrankName);
				// 		newSubrank.setUsingPrefix(playersYaml.getBoolean(
				// 				"players." + playerUUID + ".subranks." + playerSubrankName + ".use_prefix"));
				// 		newSubrank.setUsingSuffix(playersYaml.getBoolean(
				// 				"players." + playerUUID + ".subranks." + playerSubrankName + ".use_suffix"));
				// 		newSubrank.setUsingPermissions(playersYaml.getBoolean(
				// 				"players." + playerUUID + ".subranks." + playerSubrankName + ".use_permissions"));
				// 		newSubrank.setWorlds(new ArrayList<String>(playersYaml.getStringList(
				// 				"players." + playerUUID + ".subranks." + playerSubrankName + ".worlds")));

				// 		newPlayer.addSubrank(newSubrank);
					}
				}

				storedPlayers.add(newPlayer);
			}

			storageManager.setRanks(storedRanks);
			storageManager.setPlayers(storedPlayers);

			storageManager.saveAll();

			PowerRanks.getInstance().getLogger().warning("Finished converting data from a previous installation!");
		}
	}

	/**
	 * Check version of the config files and update accordingly
	 */
	public static void updateConfigFiles() {
		final PowerRanks plugin = PowerRanks.getInstance();

		boolean updateConfigYAML = checkVersion(PowerRanks.fileLoc, "config.yml");
		boolean updateLangYAML = true;// checkVersion(PowerRanks.fileLoc, "lang.yml");

		if (updateConfigYAML) {
			copyTmpFile("config.yml");
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
				if (yamlConf.isSet("defaultrank")) {
					CacheManager.configConverterSetDefaultRank(yamlConf.getString("defaultrank"));
				}
				for (String key : tmpYamlConf.getConfigurationSection("").getKeys(false)) {
					for (String key2 : tmpYamlConf.getConfigurationSection(key).getKeys(false)) {
						String field = key + "." + key2;
						if (!yamlConf.contains(field)) {
							yamlConf.set(field, tmpYamlConf.get(field));
						}
					}
				}
				yamlConf.set("version", PowerRanks.getVersion().replaceAll("[a-zA-Z ]", ""));
				yamlConf.save(file);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			deleteTmpFile("config.yml");
		}

		if (updateLangYAML) {
			copyTmpFile("lang.yml");
			final File file = new File(plugin.getDataFolder(), "lang.yml");
			final File tmpFile = new File(plugin.getDataFolder() + File.separator + "tmp", "lang.yml");
			final YamlConfiguration yamlConf = new YamlConfiguration();
			final YamlConfiguration tmpYamlConf = new YamlConfiguration();
			try {
				tmpYamlConf.load(tmpFile);
				yamlConf.load(file);
				yamlConf.set("version", null);
				yamlConf.set("commands.help", null);
				for (String key : tmpYamlConf.getConfigurationSection("").getKeys(true)) {
                    if (!yamlConf.contains(key)) {
                        if (tmpYamlConf.isString(key)) {
                            yamlConf.set(key, tmpYamlConf.get(key));
                        }
                    }
					// for (String key2 : tmpYamlConf.getConfigurationSection(key).getKeys(false)) {
					// 	String field = key + "." + key2;
					// 	if (!yamlConf.contains(field)) {
					// 		yamlConf.set(field, tmpYamlConf.get(field));
					// 	}
					// }
				}
				// yamlConf.set("commands.help", null);
				yamlConf.set("version", PowerRanks.getVersion().replaceAll("[a-zA-Z ]", ""));
				yamlConf.save(file);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			deleteTmpFile("lang.yml");
		}

		if (new File(plugin.getDataFolder() + File.separator + "tmp").exists()) {
			new File(plugin.getDataFolder() + File.separator + "tmp").delete();
		}
	}

	private static void copyTmpFile(String yamlFileName) {
		final PowerRanks plugin = PowerRanks.getInstance();

		File tmp_file = new File(plugin.getDataFolder() + File.separator + "tmp", yamlFileName);
		if (!tmp_file.exists())
			tmp_file.getParentFile().mkdirs();
		plugin.copy(plugin.getResource(yamlFileName), tmp_file);
	}

	private static void deleteTmpFile(String yamlFileName) {
		final PowerRanks plugin = PowerRanks.getInstance();

		File tmp_file = new File(plugin.getDataFolder() + File.separator + "tmp", yamlFileName);
		if (tmp_file.exists())
			tmp_file.delete();
	}

	private static boolean checkVersion(String file_path, String fileName) {
		final PowerRanks plugin = PowerRanks.getInstance();

		final File file = new File(file_path + fileName);
		final YamlConfiguration yamlConf = new YamlConfiguration();
		try {
			yamlConf.load(file);

			if (yamlConf.getString("version") == null) {
				yamlConf.set("version", PowerRanks.getVersion().replaceAll("[a-zA-Z ]", ""));
				yamlConf.save(file);
				PowerRanks.log.info("Setting up file: " + fileName);
				return !plugin.configContainsKey("updates.automatic_update_config_files")
						|| plugin.getConfigBool("updates.automatic_update_config_files", false);
			} else {
				if (!yamlConf.getString("version")
						.equalsIgnoreCase(PowerRanks.getVersion().replaceAll("[a-zA-Z ]", ""))) {
					if (!plugin.configContainsKey("updates.automatic_update_config_files")
							|| plugin.getConfigBool("updates.automatic_update_config_files", false)) {
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