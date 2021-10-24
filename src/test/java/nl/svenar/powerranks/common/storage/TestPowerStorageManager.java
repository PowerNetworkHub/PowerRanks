package nl.svenar.powerranks.common.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import nl.svenar.powerranks.common.TestDebugger;
import nl.svenar.powerranks.common.storage.provided.JSONStorageManager;
import nl.svenar.powerranks.common.storage.provided.MongoDBStorageManager;
import nl.svenar.powerranks.common.storage.provided.MySQLStorageManager;
import nl.svenar.powerranks.common.storage.provided.PSMStorageManager;
import nl.svenar.powerranks.common.storage.provided.SQLiteStorageManager;
import nl.svenar.powerranks.common.storage.provided.YAMLStorageManager;
import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;

public class TestPowerStorageManager {

    int testCountRanks = 50;
    int testCountPlayers = 50;

    @Test
    public void testYAMLStorageManager() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "Initializing YAML storage managers");

        PowerStorageManager storageManager = new YAMLStorageManager(".", "testRanks.yml", "testPlayers.yml");

        testStorageManager(storageManager, "YAML");
    }

    @Test
    public void testJSONStorageManager() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "Initializing JSON storage managers");

        PowerStorageManager storageManager = new JSONStorageManager(".", "testRanks.json", "testPlayers.json");

        testStorageManager(storageManager, "JSON");
    }

    @Test
    public void testSQLiteStorageManager() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "Initializing SQLite storage managers");

        PowerStorageManager storageManager = new SQLiteStorageManager(".", "testRanks.db", "testPlayers.db");

        testStorageManager(storageManager, "SQLite");
    }

    @Test
    public void testMySQLStorageManager() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "Initializing MySQL storage managers");

        String host = "prmysql";
        int port = 3306;
        String database = "powerranks";
        String username = "username";
        String password = "password";

        isHostReachable(host, "MySQL");

        PowerSQLConfiguration sqlConfig = new PowerSQLConfiguration(host, port, database, username, password, "ranks",
                "players");
        PowerStorageManager storageManager = new MySQLStorageManager(sqlConfig, true);

        testStorageManager(storageManager, "MySQL");
    }

    @Test
    public void testMongoDBStorageManager() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "Initializing MongoDB storage managers");

        String host = "prmysql";
        int port = 27017;
        String database = "powerranks";
        String username = "";
        String password = "";

        isHostReachable(host, "MongoDB");

        PowerSQLConfiguration sqlConfig = new PowerSQLConfiguration(host, port, database, username, password, "ranks",
                "players");
        PowerStorageManager storageManager = new MongoDBStorageManager(sqlConfig, true);

        testStorageManager(storageManager, "MongoDB");
    }

    private void isHostReachable(String host, String type) {
        try {
            InetAddress address = InetAddress.getByName(host);
            boolean isHostReachable = address.isReachable(250);
            if (!isHostReachable)
                TestDebugger.log(this, type + " not available... Skipping");
            assumeTrue(isHostReachable);
            return;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assumeTrue(false);
    }

    @Test
    public void testPSMStorageManager() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "Initializing PSM storage managers");

        PowerStorageManager storageManager = new PSMStorageManager(".", "ranks.kv", "players.kv");

        testStorageManager(storageManager, "PSM");
    }

    public void testStorageManager(PowerStorageManager storageManager, String type) {
        if (!storageManager.isConnected()) {
            TestDebugger.log(this, type + " not available... Skipping");
        }
        assumeTrue(storageManager.isConnected());

        ArrayList<PRRank> ranks = new ArrayList<PRRank>();
        ArrayList<PRPlayer> players = new ArrayList<PRPlayer>();

        storageManager.clearRanks();
        storageManager.clearPlayers();

        for (int i = 0; i < testCountRanks; i++) {
            PRRank rank = createRandomRank(i);
            storageManager.addRank(rank);
            ranks.add(rank);
        }

        for (int i = 0; i < testCountPlayers; i++) {
            PRPlayer player = createRandomPlayer(i);
            storageManager.addPlayer(player);
            players.add(player);
        }

        Instant startSaveTime = Instant.now();

        TestDebugger.log(this, "Saving data to " + type);
        storageManager.saveAll();

        Instant startLoadTime = Instant.now();
        TestDebugger.log(this, "Loading data from " + type);
        storageManager.loadAll();

        TestDebugger.log(this, "Saving and loading of " + ranks.size() + " ranks and " + players.size() + " players");
        TestDebugger.log(this,
                "Took " + Duration.between(startSaveTime, startLoadTime).toMillis() + "ms saving and "
                        + Duration.between(startLoadTime, Instant.now()).toMillis() + "ms loading ("
                        + Duration.between(startSaveTime, Instant.now()).toMillis() + "ms)");

        TestDebugger.log(this, "Validating data");
        validateData(ranks, players, storageManager);

        TestDebugger.log(this, "Cleaning " + type + " up");
        storageManager.removeAllData();
    }

    private PRRank createRandomRank(int number) {
        PRRank rank = new PRRank();
        rank.setName("RankName" + number);
        rank.setWeight(ThreadLocalRandom.current().nextInt(10, 100));
        rank.setExpires(ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L));
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(2, 10); i++) {
            rank.addProperty("test.rank.property." + i, "value" + i);
        }

        for (int i = 0; i < ThreadLocalRandom.current().nextInt(2, 10); i++) {
            // Create permission with sample data
            PRPermission permission = new PRPermission();
            permission.setName("permission.node." + i);
            permission.setValue(i % 2 == 0);
            permission.setExpires(ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L));
            for (int j = 0; j < ThreadLocalRandom.current().nextInt(2, 10); j++) {
                permission.addProperty("test.permission.property." + j, "value" + j);
            }
            rank.addPermission(permission);
        }
        return rank;
    }

    private PRPlayer createRandomPlayer(int number) {
        PRPlayer player = new PRPlayer();
        player.setUUID(UUID.randomUUID());
        player.setName("PlayerName" + number);
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(2, 10); i++) {
            player.addRankName("RankName" + i);
            player.addProperty("test.player.property." + i, "value" + i);
        }
        return player;
    }

    private void validateData(ArrayList<PRRank> ranks, ArrayList<PRPlayer> players,
            PowerStorageManager storageManager) {

        int validationCount = 0;

        assertEquals(ranks.size(), storageManager.getRanks().size());

        for (PRRank rank : ranks) {
            PRRank testRank = storageManager.getRank(rank.getName());
            assertNotNull(testRank);

            // PRRank
            assertEquals(rank.getName(), testRank.getName());
            assertEquals(rank.getWeight(), testRank.getWeight());
            assertEquals(rank.getProperties().toString(), testRank.getProperties().toString());

            // PRPermission
            for (PRPermission permission : testRank.getPermissions()) {
                assertEquals(rank.getPermission(permission.getName()).getValue(), permission.getValue());
                assertEquals(rank.getPermission(permission.getName()).getExpires(), permission.getExpires());
                assertEquals(rank.getPermission(permission.getName()).getProperties().toString(),
                        permission.getProperties().toString());
            }

            validationCount++;
        }

        TestDebugger.log(this, "Validated " + validationCount + " ranks");

        validationCount = 0;

        assertEquals(players.size(), storageManager.getPlayers().size());

        for (PRPlayer player : players) {
            PRPlayer testplayer = storageManager.getPlayer(player.getName());
            assertNotNull(testplayer);

            // PRPlayer
            assertEquals(player.getUUID(), testplayer.getUUID());
            assertEquals(player.getName(), testplayer.getName());
            assertEquals(player.getRankNames().toString(), testplayer.getRankNames().toString());
            assertEquals(player.getProperties().toString(), testplayer.getProperties().toString());

            validationCount++;
        }

        TestDebugger.log(this, "Validated " + validationCount + " players");
    }
}
