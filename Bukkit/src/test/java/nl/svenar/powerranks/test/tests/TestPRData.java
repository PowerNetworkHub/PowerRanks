package nl.svenar.powerranks.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRPlayerRank;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.test.util.TestDebugger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPRData {

    @Test
    public void A_testPRCacheHandler() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[A_testPRCacheHandler] Setup...");

        final int numPlayers = 10000;
        final int numRanks = 1000;
        long rankCreationTimeNS = 0;
        long playerCreationTimeNS = 0;

        long startTime = System.nanoTime();
        for (int i = 0; i < numRanks; i++) {
            PRRank newRank = new PRRank();
            newRank.setName("rank" + i);
            newRank.setPrefix("[rank" + i + "]");
            newRank.setWeight(i);
            newRank.setDefault(i % 10 == 0);
            for (int j = 0; j < 10; j++) {
                PRPermission testPermission = new PRPermission("test.rank.permission.rank" + i + "." + j, i % 10 != 0);
                newRank.addPermission(testPermission);
            }
            PRCache.addRank(newRank);
        }
        long endTime = System.nanoTime();
        rankCreationTimeNS = (endTime - startTime);

        startTime = System.nanoTime();
        for (int i = 0; i < numPlayers; i++) {
            PRCache.createPlayer("player" + i, UUID.randomUUID());
            PRPlayer prPlayer = PRCache.getPlayer("player" + i);
            assertTrue(prPlayer != null);
            PRPermission testPermission = new PRPermission("test.player.permission." + prPlayer.getName(), i % 2 == 0);
            prPlayer.addPermission(testPermission);
        }
        endTime = System.nanoTime();
        playerCreationTimeNS = (endTime - startTime);

        TestDebugger
                .log(this,
                        "[A_testPRCacheHandler] created " + numRanks + " ranks in "
                                + ((int) ((float) rankCreationTimeNS / 1000000.0) < 100
                                        ? (int) ((float) rankCreationTimeNS / 1000.0) + "us ("
                                                + (double) Math
                                                        .round((((float) rankCreationTimeNS / 1000000.0) * 100.0))
                                                        / 100.0
                                                + "ms)"
                                        : (int) ((float) rankCreationTimeNS / 1000000.0) + "ms"));
        TestDebugger
                .log(this,
                        "[A_testPRCacheHandler] created " + numPlayers + " players in "
                                + ((int) ((float) playerCreationTimeNS / 1000000.0) < 100
                                        ? (int) ((float) playerCreationTimeNS / 1000.0) + "us ("
                                                + (double) Math
                                                        .round((((float) playerCreationTimeNS / 1000000.0) * 100.0))
                                                        / 100.0
                                                + "ms)"
                                        : (int) ((float) playerCreationTimeNS / 1000000.0) + "ms"));

        TestDebugger.log(this, "[A_testPRCacheHandler] Checking number of ranks...");
        assertEquals(PRCache.getRanks().size(), numRanks);
        TestDebugger.log(this, "[A_testPRCacheHandler] Number of ranks OK");

        TestDebugger.log(this, "[A_testPRCacheHandler] Checking number of players...");
        assertEquals(PRCache.getPlayers().size(), numPlayers);
        TestDebugger.log(this, "[A_testPRCacheHandler] Number of players OK");

        TestDebugger.log(this, "[A_testPRCacheHandler] Setup OK");
    }

    @Test
    public void B_testPRPlayerDefaultRanks() {
        TestDebugger.log(this, "[B_testPRPlayerDefaultRanks] Checking number of default ranks in players...");

        int numDefaultRanks = 0;

        for (PRRank rank : PRCache.getRanks()) {
            if (rank.isDefault()) {
                numDefaultRanks++;
            }
        }

        for (int i = 0; i < PRCache.getPlayers().size(); i += PRCache.getPlayers().size() / 100) {
            PRPlayer prPlayer = PRCache.getPlayer("player" + i);
            assertEquals(prPlayer.getDefaultRanks().size(), numDefaultRanks);
        }

        TestDebugger.log(this, "[B_testPRPlayerDefaultRanks] Number of default ranks OK");
    }

    @Test
    public void C_testPRPlayerPermissions() {
        TestDebugger.log(this, "[C_testPRPlayerPermissions] Checking playerpermissions in players...");
        for (int i = 0; i < PRCache.getPlayers().size(); i += PRCache.getPlayers().size() / 100) {
            PRPlayer prPlayer = PRCache.getPlayer("player" + i);
            List<PRPermission> permissions = prPlayer.getEffectivePermissions();
            boolean hasTestPermission = false;
            boolean testPermissionValue = false;
            for (PRPermission permission : permissions) {
                if (permission.getName().equals("test.player.permission." + prPlayer.getName())) {
                    hasTestPermission = true;
                    testPermissionValue = permission.getValue();
                    break;
                }
            }
            assertTrue(hasTestPermission);
            assertEquals(testPermissionValue, i % 2 == 0);
        }
        TestDebugger.log(this, "[C_testPRPlayerPermissions] Playerpermissions OK");
    }

    @Test
    public void D_testPRRankPermissionOrder() {
        TestDebugger.log(this, "[D_testPRRankPermissionOrder] Checking rank weight permission overriding...");

        PRRank newRank = new PRRank();
        PRPermission testPermission = new PRPermission("test.rank.permission", false);
        newRank.setName("testRank1");
        newRank.setPrefix("[testRank1]");
        newRank.setWeight(10);
        newRank.addPermission(testPermission);
        PRCache.addRank(newRank);

        newRank = new PRRank();
        testPermission = new PRPermission("test.rank.permission", true);
        newRank.setName("testRank2");
        newRank.setPrefix("[testRank2]");
        newRank.setWeight(50);
        newRank.addPermission(testPermission);
        PRCache.addRank(newRank);

        PRPlayer testPlayer = PRCache.createPlayer("TestPlayer", UUID.randomUUID());

        testPlayer.addRank(new PRPlayerRank("testRank1"));
        boolean permissionAllowed = false;
        for (PRPermission permission : testPlayer.getEffectivePermissions()) {
            if (permission.getName().equals("test.rank.permission")) {
                permissionAllowed = permission.getValue();
                break;
            }
        }
        assertFalse(permissionAllowed);

        testPlayer.addRank(new PRPlayerRank("testRank2"));

        permissionAllowed = false;
        for (PRPermission permission : testPlayer.getEffectivePermissions()) {
            if (permission.getName().equals("test.rank.permission")) {
                permissionAllowed = permission.getValue();
                break;
            }
        }
        assertTrue(permissionAllowed);

        TestDebugger.log(this, "[D_testPRRankPermissionOrder] Rank weight permission overriding OK");
    }
}