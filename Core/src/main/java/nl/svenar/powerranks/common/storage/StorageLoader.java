package nl.svenar.powerranks.common.storage;

import nl.svenar.powerranks.common.storage.provided.JSONStorageManager;
import nl.svenar.powerranks.common.storage.provided.MySQLStorageManager;
import nl.svenar.powerranks.common.storage.provided.PSMStorageManager;
import nl.svenar.powerranks.common.storage.provided.SQLiteStorageManager;
import nl.svenar.powerranks.common.storage.provided.YAMLStorageManager;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;

public class StorageLoader {

    public PowerStorageManager getStorageManager(String dataDirectory, String storageType) {
        return getStorageManager(dataDirectory, storageType, null);
    }

    public PowerStorageManager getStorageManager(String dataDirectory, String storageType, PowerSQLConfiguration sqlConfiguration) {
        PowerStorageManager storageManager = null;
        storageType = storageType.toUpperCase();

        switch (storageType) {
            case "JSON":
                storageManager = new JSONStorageManager(dataDirectory, "ranks.json", "players.json");
                break;
            case "PSM":
                storageManager = new PSMStorageManager(dataDirectory, "ranks.psm", "players.psm");
                break;
            case "SQLITE":
                storageManager = new SQLiteStorageManager(dataDirectory, "ranks.db", "players.db");
                break;
            case "MYSQL":
                if (sqlConfiguration == null) {
                    throw new IllegalArgumentException("sqlConfiguration cannot be null when using MYSQL storage type");
                }
                storageManager = new MySQLStorageManager(sqlConfiguration);
                break;
            case "YML":
            case "YAML":
                storageManager = new YAMLStorageManager(dataDirectory, "ranks.yml", "players.yml");

            default:
                break;
        }

        return storageManager;
    }

    public void loadData(PowerStorageManager storageManager) {
        if (storageManager == null) {
            throw new IllegalArgumentException("storageManager cannot be null");
        }

        storageManager.loadAll();

        PRCache.setRanks(storageManager.getRanks());
        PRCache.setPlayers(storageManager.getPlayers());

        populateInitialData(storageManager);
    }

    public void saveData(PowerStorageManager storageManager) {
        storageManager.setRanks(PRCache.getRanks());
        storageManager.setPlayers(PRCache.getPlayers());

        storageManager.saveAll();
    }

    private void populateInitialData(PowerStorageManager storageManager) {
		if (PRCache.getRanks().size() > 0) {
			return;
		}

        PRRank rankMember = new PRRank();
		rankMember.setName("Member");
		rankMember.setDefault(true);
		rankMember.setWeight(0);
		rankMember.setPrefix("[gradient=#127e00,#3eaf18]MEMBER[/gradient]");

		PRRank rankModerator = new PRRank();
		rankModerator.setName("Moderator");
		rankModerator.setDefault(false);
		rankModerator.setWeight(50);
		rankModerator.setPrefix("[gradient=#9d1dff,#e22581]MODERATOR[/gradient]");

		PRRank rankAdmin = new PRRank();
		rankAdmin.setName("Admin");
		rankAdmin.setDefault(false);
		rankAdmin.setWeight(75);
		rankAdmin.setPrefix("[gradient=#ffff00,#ef3300]ADMIN[/gradient]");

		PRRank rankOwner = new PRRank();
		rankOwner.setName("Owner");
		rankOwner.setDefault(false);
		rankOwner.setWeight(100);
		rankOwner.setPrefix("[gradient=#ff00ff,#33ccff]OWNER[/gradient]");

		PRCache.addRank(rankMember);
		PRCache.addRank(rankModerator);
		PRCache.addRank(rankAdmin);
		PRCache.addRank(rankOwner);

		saveData(storageManager);
    }
}
