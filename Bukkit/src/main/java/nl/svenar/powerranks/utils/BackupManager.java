package nl.svenar.powerranks.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import nl.svenar.powerranks.PowerRanks;

public class BackupManager {

    public BackupManager() {
    }

    public boolean backupFile(String targetDirectoryName, File fileToBackup) {
        PowerRanks plugin = PowerRanks.getInstance();
        File targetDirectory = new File(
                plugin.getDataFolder() + File.separator + "backups" + File.separator + targetDirectoryName);
        File target = new File(
            plugin.getDataFolder() + File.separator + "backups" + File.separator + targetDirectoryName, fileToBackup.getName());
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }

        try {
            Files.copy(fileToBackup.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
