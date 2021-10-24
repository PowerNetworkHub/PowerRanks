package nl.svenar.powerranks.common.serializer;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

import nl.svenar.powerranks.common.TestDebugger;
import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;

public class TestPRSerializer {

    @Test
    public void testSerialization() {

        TestDebugger.log(this, "Setting up sample PR data");

        // Create rank with sample data
        PRRank rank = new PRRank();
        rank.setName("RankName");
        rank.setWeight(ThreadLocalRandom.current().nextInt(10, 100));
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(2, 10); i++) {
            rank.addProperty("test.rank.property." + i, "value" + i);
        }

        for (int i = 0; i < ThreadLocalRandom.current().nextInt(2, 10); i++) {
            // Create permission with sample data
            PRPermission permission = new PRPermission();
            permission.setName("permission.node." + i);
            permission.setValue(i % 2 == 0);
            permission.setExpires(123456789123L);
            for (int j = 0; j < ThreadLocalRandom.current().nextInt(2, 10); j++) {
                permission.addProperty("test.permission.property." + j, "value" + j);
            }
            rank.addPermission(permission);
        }

        UUID uuid = UUID.randomUUID();
        PRPlayer player = new PRPlayer();
        player.setUUID(uuid);
        player.setName("PlayerName");
        player.addRankName(rank.getName());
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(2, 10); i++) {
            player.addProperty("test.player.property." + i, "value" + i);
        }

        TestDebugger.log(this, "Initializing serializer");
        // Serialize and deserialize data
        PRSerializer serializer = new PRSerializer();

        TestDebugger.log(this, "Serializing rank");
        Map<String, Object> serializedRank = serializer.serialize(rank);
        TestDebugger.log(this, "Deserializing rank");
        PRRank deserializedRank = serializer.deserialize(serializedRank, PRRank.class);

        TestDebugger.log(this, "Serializing player");
        Map<String, Object> serializedPlayer = serializer.serialize(player);
        TestDebugger.log(this, "Deserializing player");
        PRPlayer deserializedPlayer = serializer.deserialize(serializedPlayer, PRPlayer.class);

        // Test data before and after serialization

        TestDebugger.log(this, "Validating data");
        // PRPlayer
        assertEquals(player.getUUID(), deserializedPlayer.getUUID());
        assertEquals(player.getName(), deserializedPlayer.getName());
        assertEquals(player.getRankNames().toString(), deserializedPlayer.getRankNames().toString());
        assertEquals(player.getProperties().toString(), deserializedPlayer.getProperties().toString());

        // PRRank
        assertEquals(rank.getName(), deserializedRank.getName());
        assertEquals(rank.getWeight(), deserializedRank.getWeight());
        assertEquals(rank.getProperties().toString(), deserializedRank.getProperties().toString());

        // PRPermission
        for (PRPermission permission : deserializedRank.getPermissions()) {
            assertEquals(rank.getPermission(permission.getName()).getValue(), permission.getValue());
            assertEquals(rank.getPermission(permission.getName()).getExpires(), permission.getExpires());
            assertEquals(rank.getPermission(permission.getName()).getProperties().toString(),
                    permission.getProperties().toString());
        }
    }

    @Test
    public void testSerializationPerformance() {
        TestDebugger.log(this, "PR serializer performance test");

        if (!TestDebugger.DOPERFORMANCETEST) {
            TestDebugger.log(this, "Performance test skipped!");
            return;
        }

        Instant initTime = Instant.now();
        Instant startTime = Instant.now();
        Instant stopTime = Instant.now();
        long maxCount = 0;
        int targetDuration = 1000; // ms
        int resolution = 10000;

        PRSerializer serializer = new PRSerializer();

        while (resolution > 1) {
            while (Duration.between(startTime, stopTime).toMillis() < targetDuration) {

                if (Duration.between(initTime, Instant.now()).toMillis() >= 1000) {
                    initTime = Instant.now();
                    TestDebugger.log(this, "Progress: " + maxCount + "/" + Duration.between(startTime, stopTime).toMillis() + "ms");
                }

                maxCount += resolution;
                ArrayList<PRPlayer> players = new ArrayList<PRPlayer>();
                for (int i = 0; i < maxCount; i++) {
                    PRPlayer player = new PRPlayer();
                    player.setUUID(UUID.randomUUID());
                    player.setName("PlayerName" + i);
                    for (int j = 0; j < ThreadLocalRandom.current().nextInt(2, 10); j++) {
                        player.addRankName("TestRank" + j);
                        player.addProperty("test.player.property." + j, "value" + j);
                    }
                    players.add(player);
                }

                startTime = Instant.now();

                for (PRPlayer player : players) {
                    serializer.deserialize(serializer.serialize(player), PRPlayer.class);
                }

                stopTime = Instant.now();
            }
            stopTime = startTime;
            TestDebugger.log(this, "Performance (serialization/deserialization) (resolution: " + resolution + "): " + maxCount + "/" + targetDuration + "ms");

            maxCount -= resolution >= 10000 ? 0 : (resolution > 1 ? resolution : 100);
            resolution /= 10;
        }
        TestDebugger.log(this, "Performance (serialization/deserialization) (resolution: " + resolution + "): " + maxCount + "/" + targetDuration + "ms");
    }
}
