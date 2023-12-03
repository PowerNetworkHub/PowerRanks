package nl.svenar.powerranks.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.common.structure.PRRank;
import nl.svenar.powerranks.common.utils.PRCache;
import nl.svenar.powerranks.test.util.TestDebugger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPCCache {

    @Test
    public void A_Setup() {
        PRCache.reset();
    }
    
    @Test
    public void B_testCreateDuration() {
        final int numPlayers = 10000;
        final int numRanks = 10000;
        final float maxDurationEachRank = 0.5f;
        final float maxDurationEachPlayer = 0.5f;

        TestDebugger.log(this, "");
        TestDebugger.log(this, "[B_testCreateDuration] Setup...");

        Instant rankCreationStartTimestamp = Instant.now();
        for (int i = 0; i < numRanks; i++) {
            PRCache.createRank("Rank" + i);
        }
        Instant rankCreationEndTimestamp = Instant.now();

        Random random = new Random(0);
        Instant playerCreationStartTimestamp = Instant.now();
        for (int i = 0; i < numPlayers; i++) {
            PRCache.createPlayer("Player" + i, new UUID(random.nextLong(), random.nextLong()));
        }
        Instant playerCreationEndTimestamp = Instant.now();

        TestDebugger.log(this, "[testPerformance] Creating Ranks " + numRanks + "x " + (rankCreationEndTimestamp.toEpochMilli() - rankCreationStartTimestamp.toEpochMilli()) + "ms (max: " + Math.round(maxDurationEachRank * numRanks) + "ms)");
        assertTrue(rankCreationEndTimestamp.toEpochMilli() - rankCreationStartTimestamp.toEpochMilli() < maxDurationEachRank * numRanks);
        assertEquals(numRanks, PRCache.getRanks().size());

        TestDebugger.log(this, "[testPerformance] Creating Players " + numPlayers + "x " + (playerCreationEndTimestamp.toEpochMilli() - playerCreationStartTimestamp.toEpochMilli()) + "ms (max: " + Math.round(maxDurationEachPlayer * numPlayers) + "ms)");
        assertTrue(playerCreationEndTimestamp.toEpochMilli() - playerCreationStartTimestamp.toEpochMilli() < maxDurationEachPlayer * numPlayers);
        assertEquals(numPlayers, PRCache.getPlayers().size());


        TestDebugger.log(this, "[B_testCreateDuration] Done!");
    }


    @Test
    public void C_testRemoveDuration() {
        final int numPlayers = PRCache.getPlayers().size();
        final int numRanks = PRCache.getRanks().size();
        final float maxDurationEachPlayer = 0.0020f;
        final float maxDurationEachRank = 0.25f;

        Instant rankStartTimestamp = Instant.now();
        Iterator<PRRank> rankIterator = PRCache.getRanks().iterator();
        while (rankIterator.hasNext()) {
            PRCache.removeRank(rankIterator.next());
        }
        Instant rankEndTimestamp = Instant.now();

        Instant playerStartTimestamp = Instant.now();
        Iterator<PRPlayer> playerIterator = PRCache.getPlayers().iterator();
        while (playerIterator.hasNext()) {
            PRCache.removePlayer(playerIterator.next());
        }
        Instant playerEndTimestamp = Instant.now();


        TestDebugger.log(this, "[testPerformance] Removing Ranks " + numRanks + "x " + (rankEndTimestamp.toEpochMilli() - rankStartTimestamp.toEpochMilli()) + "ms (max: " + Math.round(maxDurationEachRank * numPlayers) + "ms)");
        assertTrue(rankEndTimestamp.toEpochMilli() - rankStartTimestamp.toEpochMilli() < maxDurationEachRank * numRanks);
        assertEquals(0, PRCache.getPlayers().size());


        TestDebugger.log(this, "[testPerformance] Removing Players " + numPlayers + "x " + (playerEndTimestamp.toEpochMilli() - playerStartTimestamp.toEpochMilli()) + "ms (max: " + Math.round(maxDurationEachPlayer * numPlayers) + "ms)");
        assertTrue(playerEndTimestamp.toEpochMilli() - playerStartTimestamp.toEpochMilli() < maxDurationEachPlayer * numPlayers);
        assertEquals(0, PRCache.getPlayers().size());
    }

}
