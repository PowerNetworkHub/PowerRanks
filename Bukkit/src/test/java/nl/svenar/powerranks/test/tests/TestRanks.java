package nl.svenar.powerranks.test.tests;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import org.bukkit.entity.Player;

import org.mockbukkit.mockbukkit.ServerMock;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.test.util.Assert;
import nl.svenar.powerranks.test.util.Mock;
import nl.svenar.powerranks.test.util.TestDebugger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestInstance(Lifecycle.PER_CLASS)
public class TestRanks {

    private final int numPlayers = 20;

    private ServerMock server;

    @BeforeAll
    public void setUp() {
        Mock.init();
        server = Mock.getServerMock();

        server.setPlayers(numPlayers);
        assumeTrue(numPlayers == CacheManager.getPlayers().size());
    }

    @AfterAll
    public void tearDown() {
        Mock.unmock();
    }

    @Test
    public void A_testCreateRank() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[A_testCreateRank] Start");

        Player player = Mock.getPlayer(0);

        player.setOp(true);

        int numRanks = CacheManager.getRanks().size();
        server.execute("pr", player, "createrank", "Test1");
        assertEquals(numRanks + 1, CacheManager.getRanks().size());

        TestDebugger.log(this, "[A_testCreateRank] OK");
    }

    @Test
    public void B_testSetrank() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[B_testSetrank] Start");

        Player player1 = Mock.getPlayer(0);
        Player player2 = Mock.getPlayer(1);
        PRPlayer prPlayer2 = CacheManager.getPlayer(player2.getUniqueId().toString());

        player1.setOp(true);
        player2.setOp(false);

        server.execute("pr", player1, "createrank", "testSetDelrank");
        server.execute("pr", player1, "setrank", player2.getName(), "testSetDelrank");

        assertTrue(prPlayer2.getRanks().stream().anyMatch(rank -> rank.getName().equals("testSetDelrank")));

        TestDebugger.log(this, "[B_testSetrank] OK");
    }

    @Test
    public void C_testDelrank() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[C_testDelrank] Start");

        Player player1 = Mock.getPlayer(0);
        Player player2 = Mock.getPlayer(1);
        PRPlayer prPlayer2 = CacheManager.getPlayer(player2.getUniqueId().toString());

        player1.setOp(true);
        player2.setOp(false);

        server.execute("pr", player1, "delrank", player2.getName(), "testSetDelrank");

        assertTrue(!prPlayer2.getRanks().stream().anyMatch(rank -> rank.getName().equals("testSetDelrank")));

        TestDebugger.log(this, "[C_testDelrank] OK");
    }

    @Test
    public void D_testDeleteRankUpdatePlayer() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[D_testDeleteRankUpdatePlayer] Start");

        Player player1 = Mock.getPlayer(0);
        Player player2 = Mock.getPlayer(1);
        PRPlayer prPlayer2 = CacheManager.getPlayer(player2.getUniqueId().toString());

        player1.setOp(true);
        player2.setOp(false);

        server.execute("pr", player1, "createrank", "testDeleteRankUpdatePlayer");
        server.execute("pr", player1, "setrank", player2.getName(), "testDeleteRankUpdatePlayer");

        assertTrue(prPlayer2.getRanks().stream().anyMatch(rank -> rank.getName().equals("testDeleteRankUpdatePlayer")));
        CacheManager.removeRank(CacheManager.getRank("testDeleteRankUpdatePlayer"));
        assertTrue(!prPlayer2.getRanks().stream().anyMatch(rank -> rank.getName().equals("testDeleteRankUpdatePlayer")));

        TestDebugger.log(this, "[D_testDeleteRankUpdatePlayer] OK");
    }

    @Test
    public void E_highWeightOverridesLowWeight() {
        TestDebugger.log(this, "[E_highWeightOverridesLowWeight] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(1);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());
        admin.setOp(true);

        server.execute("pr", admin, "createrank", "LowRank");
        server.execute("pr", admin, "createrank", "HighRank");
        server.execute("pr", admin, "addperm", "LowRank", "test.weighted.node");
        server.execute("pr", admin, "addperm", "HighRank", "-test.weighted.node");

        // Force weights
        CacheManager.getRank("LowRank").setWeight(1);
        CacheManager.getRank("HighRank").setWeight(100);

        server.execute("pr", admin, "addrank", target.getName(), "LowRank");
        server.execute("pr", admin, "addrank", target.getName(), "HighRank");

        // prTarget.recalculateEffectivePermissions();

        Assert.assertFalse("High weight deny should win", prTarget.isPermissionAllowed("test.weighted.node", true));

        TestDebugger.log(this, "[E_highWeightOverridesLowWeight] OK");
    }

    @Test
    public void F_deleteRankRemovesFromPlayer() {
        TestDebugger.log(this, "[F_deleteRankRemovesFromPlayer] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(1);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        server.execute("pr", admin, "createrank", "CleanupRank");
        server.execute("pr", admin, "addperm", "CleanupRank", "test.cleanup");
        server.execute("pr", admin, "setrank", target.getName(), "CleanupRank");

        // prTarget.recalculateEffectivePermissions();
        Assert.assertTrue("prTarget has permission 'test.cleanup'.", prTarget.isPermissionAllowed("test.cleanup", true));

        // Delete rank
        server.execute("pr", admin, "deleterank", "CleanupRank");
        // prTarget.recalculateEffectivePermissions();
        Assert.assertFalse("Rank deleted → no permission", prTarget.isPermissionAllowed("test.cleanup", true));

        TestDebugger.log(this, "[F_deleteRankRemovesFromPlayer] OK");
    }
}
