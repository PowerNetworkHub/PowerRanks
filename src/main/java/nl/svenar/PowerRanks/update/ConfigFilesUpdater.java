package nl.svenar.PowerRanks.update;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

import nl.svenar.PowerRanks.PowerRanks;
import nl.svenar.common.storage.PowerConfigManager;
import nl.svenar.common.storage.provided.YAMLConfigManager;

// import nl.svenar.PowerRanks.Cache.CachedConfig;

public class ConfigFilesUpdater {

	private static File backupDir, backupRanks, backupConfig, backupLang, backupPlayers, oldRanksFile, newRanksFile,
			oldPlayersFile, newPlayersFile, usertagsFile, configFile, langFile;

	public static void updateConfigFiles() {
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

			PowerConfigManager newConfigManager = new YAMLConfigManager(PowerRanks.fileLoc, "config.yml");
			PowerConfigManager newLanguageManager = new YAMLConfigManager(PowerRanks.fileLoc, "lang.yml");
			PowerConfigManager usertagManager = new YAMLConfigManager(PowerRanks.fileLoc, "usertags.yml"); // TODO

			Map<String, Object> configData = newConfigManager.getRawData();
			for (Entry<String, Object> entry : oldConfigManager.getRawData().entrySet()) {
				configData.put(entry.getKey(), entry.getValue());
			}
			newConfigManager.setRawData(configData);

			Map<String, Object> languageData = newLanguageManager.getRawData();
			for (Entry<String, Object> entry : oldLanguageManager.getRawData().entrySet()) {
				languageData.put(entry.getKey(), entry.getValue());
			}
			newLanguageManager.setRawData(languageData);
		}
	}
}