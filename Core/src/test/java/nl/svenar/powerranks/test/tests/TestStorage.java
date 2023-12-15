package nl.svenar.powerranks.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import nl.svenar.powerranks.common.storage.PowerSQLConfiguration;
import nl.svenar.powerranks.common.storage.PowerStorageManager;
import nl.svenar.powerranks.common.storage.provided.JSONStorageManager;
import nl.svenar.powerranks.common.storage.provided.PSMStorageManager;
import nl.svenar.powerranks.common.storage.provided.YAMLStorageManager;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.common.storage.provided.SQLiteStorageManager;
import nl.svenar.powerranks.common.storage.provided.MySQLStorageManager;
import nl.svenar.powerranks.test.util.TestDebugger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestStorage {

    private final String storageDirectory = "storage";
    private final int numPlayers = 1000;
    private final int numRanks = 1000;

    private static List<StorageSpeed> storageSpeeds = new ArrayList<StorageSpeed>();

    @Test
    public void setupCache() {
        PRCache.reset();

        for (int i = 0; i < numRanks; i++) {
            PRCache.createRank("Rank" + i);
        }

        Random random = new Random(0);
        for (int i = 0; i < numPlayers; i++) {
            PRCache.createPlayer("Player" + i, new UUID(random.nextLong(), random.nextLong()));
        }
    }

    @Test
    public void A_TestYAML() {
        setupCache();
        StorageSpeed storageSpeed = new StorageSpeed();
        storageSpeed.name = "YAML";
        TestDebugger.log(this, "[A_TestYAML] Setup...");

        PowerStorageManager storageManager = new YAMLStorageManager(storageDirectory, "ranks.yml", "players.yml");

        TestDebugger.log(this, "[A_TestYAML] Saving data...");
        storageManager.setRanks(PRCache.getRanks());
        storageManager.setPlayers(PRCache.getPlayers());


        Instant saveStartTimestamp = Instant.now();
        storageManager.saveAll();
        Instant saveEndTimestamp = Instant.now();
        storageSpeed.speedSave = (int) (saveEndTimestamp.toEpochMilli() - saveStartTimestamp.toEpochMilli());

        storageManager = new YAMLStorageManager(storageDirectory, "ranks.yml", "players.yml");

        TestDebugger.log(this, "[A_TestYAML] Loading data...");
        Instant loadStartTimestamp = Instant.now();
        storageManager.loadAll();
        Instant loadEndTimestamp = Instant.now();
        storageSpeed.speedLoad = (int) (loadEndTimestamp.toEpochMilli() - loadStartTimestamp.toEpochMilli());

        PRCache.reset();
        PRCache.setRanks(storageManager.getRanks());
        PRCache.setPlayers(storageManager.getPlayers());

        TestDebugger.log(this, "[A_TestYAML] Checking data...");
        assertEquals(numRanks, PRCache.getRanks().size());
        assertEquals(numPlayers, PRCache.getPlayers().size());

        storageSpeeds.add(storageSpeed);
        TestDebugger.log(this, "[A_TestYAML] OK");
    }

    @Test
    public void B_TestJSON() {
        setupCache();
        StorageSpeed storageSpeed = new StorageSpeed();
        storageSpeed.name = "JSON";
        TestDebugger.log(this, "[B_TestJSON] Setup...");

        PowerStorageManager storageManager = new JSONStorageManager(storageDirectory, "ranks.json", "players.json");

        TestDebugger.log(this, "[B_TestJSON] Saving data...");
        storageManager.setRanks(PRCache.getRanks());
        storageManager.setPlayers(PRCache.getPlayers());


        Instant saveStartTimestamp = Instant.now();
        storageManager.saveAll();
        Instant saveEndTimestamp = Instant.now();
        storageSpeed.speedSave = (int) (saveEndTimestamp.toEpochMilli() - saveStartTimestamp.toEpochMilli());

        storageManager = new JSONStorageManager(storageDirectory, "ranks.json", "players.json");

        TestDebugger.log(this, "[B_TestJSON] Loading data...");
        Instant loadStartTimestamp = Instant.now();
        storageManager.loadAll();
        Instant loadEndTimestamp = Instant.now();
        storageSpeed.speedLoad = (int) (loadEndTimestamp.toEpochMilli() - loadStartTimestamp.toEpochMilli());

        PRCache.reset();
        PRCache.setRanks(storageManager.getRanks());
        PRCache.setPlayers(storageManager.getPlayers());

        TestDebugger.log(this, "[B_TestJSON] Checking data...");
        assertEquals(numRanks, PRCache.getRanks().size());
        assertEquals(numPlayers, PRCache.getPlayers().size());

        storageSpeeds.add(storageSpeed);
        TestDebugger.log(this, "[B_TestJSON] OK");
    }

    @Test
    public void C_TestPSM() {
        setupCache();
        StorageSpeed storageSpeed = new StorageSpeed();
        storageSpeed.name = "PSM";
        TestDebugger.log(this, "[C_TestPSM] Setup...");

        PowerStorageManager storageManager = new PSMStorageManager(storageDirectory, "ranks.psm", "players.psm");

        TestDebugger.log(this, "[C_TestPSM] Saving data...");
        storageManager.setRanks(PRCache.getRanks());
        storageManager.setPlayers(PRCache.getPlayers());


        Instant saveStartTimestamp = Instant.now();
        storageManager.saveAll();
        Instant saveEndTimestamp = Instant.now();
        storageSpeed.speedSave = (int) (saveEndTimestamp.toEpochMilli() - saveStartTimestamp.toEpochMilli());

        storageManager = new PSMStorageManager(storageDirectory, "ranks.psm", "players.psm");

        TestDebugger.log(this, "[C_TestPSM] Loading data...");
        Instant loadStartTimestamp = Instant.now();
        storageManager.loadAll();
        Instant loadEndTimestamp = Instant.now();
        storageSpeed.speedLoad = (int) (loadEndTimestamp.toEpochMilli() - loadStartTimestamp.toEpochMilli());

        PRCache.reset();
        PRCache.setRanks(storageManager.getRanks());
        PRCache.setPlayers(storageManager.getPlayers());

        TestDebugger.log(this, "[C_TestPSM] Checking data...");
        assertEquals(numRanks, PRCache.getRanks().size());
        assertEquals(numPlayers, PRCache.getPlayers().size());

        storageSpeeds.add(storageSpeed);
        TestDebugger.log(this, "[C_TestPSM] OK");
    }

    @Test
    public void D_TestSQLite() {
        setupCache();
        StorageSpeed storageSpeed = new StorageSpeed();
        storageSpeed.name = "SQLite";
        TestDebugger.log(this, "[D_TestSQLite] Setup...");

        SQLiteStorageManager storageManager = new SQLiteStorageManager(storageDirectory, "ranks.db", "players.db");

        if (!storageManager.isConnected()) {
            TestDebugger.log(this, "[D_TestSQLite] Skipping test, not connected to MySQL");
            return;
        }

        TestDebugger.log(this, "[D_TestSQLite] Clearing database...");
        storageManager.removeAllData();
        storageManager.close();

        storageManager = new SQLiteStorageManager(storageDirectory, "ranks.db", "players.db");
        assertTrue(storageManager.isConnected());

        TestDebugger.log(this, "[D_TestSQLite] Saving data...");
        storageManager.setRanks(PRCache.getRanks());
        storageManager.setPlayers(PRCache.getPlayers());


        Instant saveStartTimestamp = Instant.now();
        storageManager.saveAll();
        Instant saveEndTimestamp = Instant.now();
        storageSpeed.speedSave = (int) (saveEndTimestamp.toEpochMilli() - saveStartTimestamp.toEpochMilli());
        storageManager.close();

        storageManager = new SQLiteStorageManager(storageDirectory, "ranks.db", "players.db");
        assertTrue(storageManager.isConnected());

        TestDebugger.log(this, "[D_TestSQLite] Loading data...");
        Instant loadStartTimestamp = Instant.now();
        storageManager.loadAll();
        Instant loadEndTimestamp = Instant.now();
        storageSpeed.speedLoad = (int) (loadEndTimestamp.toEpochMilli() - loadStartTimestamp.toEpochMilli());

        PRCache.reset();
        PRCache.setRanks(storageManager.getRanks());
        PRCache.setPlayers(storageManager.getPlayers());

        storageManager.close();

        TestDebugger.log(this, "[D_TestSQLite] Checking data...");
        assertEquals(numRanks, PRCache.getRanks().size());
        assertEquals(numPlayers, PRCache.getPlayers().size());

        storageSpeeds.add(storageSpeed);
        TestDebugger.log(this, "[D_TestSQLite] OK");
    }

    @Test
    public void E_TestMySQL() {
        setupCache();
        StorageSpeed storageSpeed = new StorageSpeed();
        storageSpeed.name = "MySQL";

        TestDebugger.log(this, "[E_TestMySQL] Setup...");

        PowerSQLConfiguration configuration = new PowerSQLConfiguration();
        configuration.setHost("0.0.0.0");
        configuration.setPort(3306);
        configuration.setDatabase("powerranks");
        configuration.setUsername("powerranks");
        configuration.setPassword("powerranks");
        configuration.setUseSSL(false);
        configuration.setTableRanks("ranks");
        configuration.setTablePlayers("players");
        configuration.setTableMessages("messages");
        configuration.setSilentErrors(true);

        MySQLStorageManager storageManager = new MySQLStorageManager(configuration);

        if (!storageManager.isConnected()) {
            TestDebugger.log(this, "[E_TestMySQL] Skipping test, not connected to MySQL");
            return;
        }

        TestDebugger.log(this, "[E_TestMySQL] Clearing database...");
        storageManager.removeAllData();
        storageManager.close();

        storageManager = new MySQLStorageManager(configuration);
        assertTrue(storageManager.isConnected());

        TestDebugger.log(this, "[E_TestMySQL] Saving data...");
        storageManager.setRanks(PRCache.getRanks());
        storageManager.setPlayers(PRCache.getPlayers());


        Instant saveStartTimestamp = Instant.now();
        storageManager.saveAll();
        Instant saveEndTimestamp = Instant.now();
        storageSpeed.speedSave = (int) (saveEndTimestamp.toEpochMilli() - saveStartTimestamp.toEpochMilli());
        storageManager.close();

        storageManager = new MySQLStorageManager(configuration);
        assertTrue(storageManager.isConnected());

        TestDebugger.log(this, "[E_TestMySQL] Loading data...");
        Instant loadStartTimestamp = Instant.now();
        storageManager.loadAll();
        Instant loadEndTimestamp = Instant.now();
        storageSpeed.speedLoad = (int) (loadEndTimestamp.toEpochMilli() - loadStartTimestamp.toEpochMilli());

        PRCache.reset();
        PRCache.setRanks(storageManager.getRanks());
        PRCache.setPlayers(storageManager.getPlayers());

        storageManager.close();

        TestDebugger.log(this, "[E_TestMySQL] Checking data...");
        assertEquals(numRanks, PRCache.getRanks().size());
        assertEquals(numPlayers, PRCache.getPlayers().size());

        storageSpeeds.add(storageSpeed);
        TestDebugger.log(this, "[E_TestMySQL] OK");
    }

    @Test
    public void Y_Cleanup() {
        File directory = new File(storageDirectory);
        assertTrue(deleteDirectory(directory));
    }

    @Test
    public void Z_ShowStorageStats() {
        assertTrue(storageSpeeds.size() > 0);

        TestDebugger.log(this, "---------------------------------");

        storageSpeeds.removeIf(Objects::isNull);
        storageSpeeds.sort(Comparator.comparingInt(StorageSpeed::totalDuration));

        for (StorageSpeed storageSpeed : storageSpeeds) {
            TestDebugger.log(this, storageSpeed.name);
            TestDebugger.log(this, "    - Saving: " + storageSpeed.speedSave + "ms");
            TestDebugger.log(this, "    - Loading: " + storageSpeed.speedLoad + "ms");
        }

        TestDebugger.log(this, "---------------------------------");
    }

    public boolean deleteDirectory(File directory) {
        if (!directory.exists()) {
            return true; // Directory doesn't exist, so it's already deleted.
        }

        if (!directory.isDirectory()) {
            return false; // Not a directory.
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (!deleteDirectory(file)) {
                        return false; // Failed to delete a subdirectory.
                    }
                } else {
                    if (!file.delete()) {
                        return false; // Failed to delete a file.
                    }
                }
            }
        }

        return directory.delete(); // Delete the empty directory.
    }

    private class StorageSpeed {
        public String name;
        public int speedSave;
        public int speedLoad;

        public int totalDuration() {
            return speedSave + speedLoad;
        }
    }
}
