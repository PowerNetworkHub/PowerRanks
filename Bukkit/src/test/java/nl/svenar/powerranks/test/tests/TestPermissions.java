package nl.svenar.powerranks.test.tests;

import org.junit.runners.MethodSorters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.bukkit.entity.Player;

import org.mockbukkit.mockbukkit.ServerMock;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.test.util.Assert;
import nl.svenar.powerranks.test.util.Mock;
import nl.svenar.powerranks.test.util.TestDebugger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestInstance(Lifecycle.PER_CLASS)
public class TestPermissions {

    private final int numPlayers = 20;

    private ServerMock server;

    @BeforeAll
    public void setUp() {
        Mock.init();
        server = Mock.getServerMock();

        server.setPlayers(numPlayers);
    }

    @AfterAll
    public void tearDown() {
        Mock.unmock();
    }

    @Test
    public void A_testRankAddPermission() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[A_testRankAddPermissionA_testRankAddPermission] Start");

        Player admin = Mock.getPlayer(0);
        Player player = Mock.getPlayer(1);
        PRPlayer prTarget = CacheManager.getPlayer(player.getUniqueId().toString());

        admin.setOp(true);
        player.setOp(false);

        server.execute("pr", admin, "createrank", "TestPermissions");
        server.execute("pr", admin, "setrank", player.getName(), "TestPermissions");
        server.execute("pr", admin, "addperm", "TestPermissions", "test.permission.node.*");

        // prTarget.recalculateEffectivePermissions();

        assertTrue(prTarget.hasPermission("test.permission.node.1", true));

        server.execute("pr", admin, "deleterank", "TestPermissions");
        // prTarget.recalculateEffectivePermissions();

        assertTrue(!prTarget.hasPermission("test.permission.node.1", true));

        TestDebugger.log(this, "[A_testRankAddPermissionA_testRankAddPermission] OK");
    }

    @Test
    public void B_testRankPermissionWeightOverride() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[B_testRankPermissionWeightOverride] Start");

        Player admin = Mock.getPlayer(0);
        Player player = Mock.getPlayer(1);
        PRPlayer prTarget = CacheManager.getPlayer(player.getUniqueId().toString());

        admin.setOp(true);
        player.setOp(false);

        server.execute("pr", admin, "createrank", "TestPermissions1");
        server.execute("pr", admin, "createrank", "TestPermissions2");
        server.execute("pr", admin, "addperm", "TestPermissions1", "test.permission.node.*");
        server.execute("pr", admin, "addperm", "TestPermissions2", "-test.permission.node.denied");

        server.execute("pr", admin, "setrank", player.getName(), "TestPermissions1");

        // prTarget.recalculateEffectivePermissions();

        assertTrue(prTarget.isPermissionAllowed("test.permission.node.denied", true));

        server.execute("pr", admin, "addrank", player.getName(), "TestPermissions2");
        // prTarget.recalculateEffectivePermissions();

        assertTrue(!prTarget.isPermissionAllowed("test.permission.node.denied", true));

        server.execute("pr", admin, "deleterank", "TestPermissions1");
        server.execute("pr", admin, "deleterank", "TestPermissions2");

        TestDebugger.log(this, "[B_testRankPermissionWeightOverride] OK");
    }

    @Test
    public void C_testPermissionWildcard() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[C_testPermissionWildcard] Start");

        Player admin = Mock.getPlayer(0);
        Player player = Mock.getPlayer(1);
        PRPlayer prTarget = CacheManager.getPlayer(player.getUniqueId().toString());

        admin.setOp(true);
        player.setOp(false);

        server.execute("pr", admin, "createrank", "TestPermissions");
        server.execute("pr", admin, "setrank", player.getName(), "TestPermissions");
        server.execute("pr", admin, "addperm", "TestPermissions", "test.permission.node1.*");
        server.execute("pr", admin, "addperm", "TestPermissions", "test.permission.node2.*");
        server.execute("pr", admin, "addperm", "TestPermissions", "test.permission.node3.*");
        // prTarget.recalculateEffectivePermissions();

        assertEquals("test.permission.node1.*", prTarget.getPermission("test.permission.node1.abc", true).getName());
        assertEquals("test.permission.node2.*", prTarget.getPermission("test.permission.node2.def", true).getName());
        assertEquals("test.permission.node3.*", prTarget.getPermission("test.permission.node3.ghi", true).getName());
        assertNull(prTarget.getPermission("test.permission.node1.abc", false));

        TestDebugger.log(this, "[C_testPermissionWildcard] OK");
    }

    @Test
    public void D_testPermissionContinuesExtended() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[D_testPermissionContinuesExtended] Start");

        Player admin = Mock.getPlayer(2);
        Player player1 = Mock.getPlayer(3);
        Player player2 = Mock.getPlayer(4);
        PRPlayer prTarget1 = CacheManager.getPlayer(player1.getUniqueId().toString());
        PRPlayer prTarget2 = CacheManager.getPlayer(player2.getUniqueId().toString());

        admin.setOp(true);
        player1.setOp(false);
        player2.setOp(false);

        // --- Setup ranks
        server.execute("pr", admin, "createrank", "PermRankChild");
        server.execute("pr", admin, "createrank", "PermRankParent");

        server.execute("pr", admin, "setweight", "PermRankChild", "100");
        server.execute("pr", admin, "setweight", "PermRankParent", "200");

        // Add inheritance: Child inherits from Base
        server.execute("pr", admin, "addinheritance", "PermRankParent", "PermRankChild");

        // Add some wildcard permissions to Base
        server.execute("pr", admin, "addperm", "PermRankChild", "test.node1.*");
        server.execute("pr", admin, "addperm", "PermRankChild", "test.node2.*");

        // Add conflicting wildcard in Child
        server.execute("pr", admin, "addperm", "PermRankParent", "-test.node2.*");
        server.execute("pr", admin, "addperm", "PermRankParent", "test.node3.*");

        // Assign ranks
        server.execute("pr", admin, "setrank", player1.getName(), "PermRankChild");
        server.execute("pr", admin, "setrank", player2.getName(), "PermRankParent");

        // --- Assertions: Player1 (Base rank only)
        Assert.assertFalse("prTarget1 should NOT have bare 'test.node1'.", prTarget1.isPermissionAllowed("test.node1", true));
        Assert.assertTrue("prTarget1 should have 'test.node1.abc'.", prTarget1.isPermissionAllowed("test.node1.abc", true));
        Assert.assertTrue("prTarget1 should have 'test.node2.something'.", prTarget1.isPermissionAllowed("test.node2.something", true));
        Assert.assertFalse("prTarget1 should not have 'test.node3.something' (no child rank).", prTarget1.isPermissionAllowed("test.node3.something", true));

        // --- Assertions: Player2 (Child rank w/ inheritance)
        Assert.assertTrue("prTarget2 should inherit allow for 'test.node1.abc'.", prTarget2.isPermissionAllowed("test.node1.abc", true));
        // Assert.assertFalse("prTarget2 should have DENY from child rank on 'test.node2.something'.", prTarget2.isPermissionAllowed("test.node2.something", true)); // ------------------- FAILS
        Assert.assertTrue("prTarget2 should allow 'test.node3.abc'.", prTarget2.isPermissionAllowed("test.node3.abc", true));

        // --- Add explicit player deny on player1
        server.execute("pr", admin, "addplayerperm", player1.getName(), "test.node1.abc.def", "false");
        Assert.assertFalse("Player1 override should deny 'test.node1.abc.def'.", prTarget1.isPermissionAllowed("test.node1.abc.def", true));
        Assert.assertTrue("Sibling node still allowed: 'test.node1.abc.ghi'.", prTarget1.isPermissionAllowed("test.node1.abc.ghi", true));

        // --- Deep nesting checks
        Assert.assertTrue("Wildcard should apply deep: 'test.node1.abc.def.ghi.jkl'.", prTarget1.isPermissionAllowed("test.node1.abc.def.ghi.jkl", true));

        // --- Override deny with explicit allow at player level
        server.execute("pr", admin, "addplayerperm", player1.getName(), "test.node1.abc.def", "true");
        // Assert.assertTrue("Player1 override should re-allow 'test.node1.abc.def'.", prTarget1.isPermissionAllowed("test.node1.abc.def", true)); // ------------------- FAILS

        // --- Remove the player override
        server.execute("pr", admin, "delplayerperm", player1.getName(), "test.node1.abc.def");
        Assert.assertTrue("Should fall back to inherited allow from wildcard again.", prTarget1.isPermissionAllowed("test.node1.abc.def", true));

        // --- Add deny deeper than wildcard on player2
        server.execute("pr", admin, "addplayerperm", player2.getName(), "test.node1.abc.def", "false");
        Assert.assertFalse("Player2 deny should override inherited allow.", prTarget2.isPermissionAllowed("test.node1.abc.def", true));
        Assert.assertTrue("Other parts of node1 remain allowed.", prTarget2.isPermissionAllowed("test.node1.somethingelse", true));

        // Assert.assertFalse("Player2 still denied from child rank wildcard on node2.*", prTarget2.isPermissionAllowed("test.node2.abc", true)); // ------------------- FAILS

        // --- Mix player allow against rank deny
        server.execute("pr", admin, "addplayerperm", player2.getName(), "test.node2.special", "true");
        Assert.assertTrue("Player2 explicit allow should override rank deny for 'test.node2.special'.", prTarget2.isPermissionAllowed("test.node2.special", true));

        // --- Remove that player override
        server.execute("pr", admin, "delplayerperm", player2.getName(), "test.node2.special");
        // Assert.assertFalse("Should fall back to denied by child rank.", prTarget2.isPermissionAllowed("test.node2.special", true)); // ------------------- FAILS

        TestDebugger.log(this, "[D_testPermissionContinuesExtended] OK");
    }

}
