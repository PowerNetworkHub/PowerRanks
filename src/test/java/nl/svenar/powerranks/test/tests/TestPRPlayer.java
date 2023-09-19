package nl.svenar.powerranks.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import nl.svenar.common.structure.PRPlayer;
import nl.svenar.powerranks.test.util.TestDebugger;

public class TestPRPlayer {

    @Test
    public void testPerformancePRPlayers() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[testPerformancePRPlayers] Start");

        final int numPlayers = 100000;
        final int numRepeats = 100;
        final int maxDurationAverage = 10; // creating [numPlayers] PRPlayers may only block for XXms, test [numRepeats] times
        long averageTime = 0;

        for (int r = 0; r < numRepeats; r++) {
            List<PRPlayer> prPlayers = new ArrayList<PRPlayer>();

            long startTime = System.nanoTime();
            for (int i = 0; i < numPlayers; i++) {
                PRPlayer newPlayer = new PRPlayer();
                prPlayers.add(newPlayer);
            }
            long endTime = System.nanoTime();
            averageTime += (endTime - startTime) / 1000000;
            prPlayers.clear();
        }
        averageTime /= numRepeats;

        TestDebugger.log(this, "[testPerformancePRPlayers] Took " + averageTime + "ms");
        assertTrue(averageTime < maxDurationAverage);

        // assertEquals(numPlayers, prPlayers.size());

        TestDebugger.log(this, "[testPerformancePRPlayers] Done!");
    }
    
}
