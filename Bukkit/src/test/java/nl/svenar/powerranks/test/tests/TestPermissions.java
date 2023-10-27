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

import be.seeseemelk.mockbukkit.ServerMock;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.common.structure.PRPlayer;
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

        Player player1 = Mock.getPlayer(0);
        Player player2 = Mock.getPlayer(1);
        PRPlayer prPlayer2 = CacheManager.getPlayer(player2.getUniqueId().toString());

        player1.setOp(true);
        player2.setOp(false);

        server.execute("pr", player1, "createrank", "TestPermissions");
        server.execute("pr", player1, "setrank", player2.getName(), "TestPermissions");
        server.execute("pr", player1, "addperm", "TestPermissions", "test.permission.node.*");
        
        prPlayer2.updatePermissionsFromRanks();

        assertTrue(prPlayer2.hasPermission("test.permission.node.1", true));

        server.execute("pr", player1, "deleterank", "TestPermissions");
        prPlayer2.updatePermissionsFromRanks();

        assertTrue(!prPlayer2.hasPermission("test.permission.node.1", true));

        TestDebugger.log(this, "[A_testRankAddPermissionA_testRankAddPermission] OK");
    }

    @Test
    public void B_testRankPermissionWeightOverride() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[B_testRankPermissionWeightOverride] Start");

        Player player1 = Mock.getPlayer(0);
        Player player2 = Mock.getPlayer(1);
        PRPlayer prPlayer2 = CacheManager.getPlayer(player2.getUniqueId().toString());

        player1.setOp(true);
        player2.setOp(false);

        server.execute("pr", player1, "createrank", "TestPermissions1");
        server.execute("pr", player1, "createrank", "TestPermissions2");
        server.execute("pr", player1, "addperm", "TestPermissions1", "test.permission.node.*");
        server.execute("pr", player1, "addperm", "TestPermissions2", "-test.permission.node.denied");

        server.execute("pr", player1, "setrank", player2.getName(), "TestPermissions1");
        
        prPlayer2.updatePermissionsFromRanks();

        assertTrue(prPlayer2.isPermissionAllowed("test.permission.node.denied", true));

        server.execute("pr", player1, "addrank", player2.getName(), "TestPermissions2");
        prPlayer2.updatePermissionsFromRanks();

        assertTrue(!prPlayer2.isPermissionAllowed("test.permission.node.denied", true));

        server.execute("pr", player1, "deleterank", "TestPermissions1");
        server.execute("pr", player1, "deleterank", "TestPermissions2");

        TestDebugger.log(this, "[B_testRankPermissionWeightOverride] OK");
    }

    @Test
    public void C_testPermissionWildcard() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[C_testPermissionWildcard] Start");

        Player player1 = Mock.getPlayer(0);
        Player player2 = Mock.getPlayer(1);
        PRPlayer prPlayer2 = CacheManager.getPlayer(player2.getUniqueId().toString());

        player1.setOp(true);
        player2.setOp(false);

        server.execute("pr", player1, "createrank", "TestPermissions");
        server.execute("pr", player1, "setrank", player2.getName(), "TestPermissions");
        server.execute("pr", player1, "addperm", "TestPermissions", "test.permission.node1.*");
        server.execute("pr", player1, "addperm", "TestPermissions", "test.permission.node2.*");
        server.execute("pr", player1, "addperm", "TestPermissions", "test.permission.node3.*");
        prPlayer2.updatePermissionsFromRanks();

        assertEquals("test.permission.node1.*", prPlayer2.getPermission("test.permission.node1.abc", true).getName());
        assertEquals("test.permission.node2.*", prPlayer2.getPermission("test.permission.node2.def", true).getName());
        assertEquals("test.permission.node3.*", prPlayer2.getPermission("test.permission.node3.ghi", true).getName());
        assertNull(prPlayer2.getPermission("test.permission.node1.abc", false));

        TestDebugger.log(this, "[C_testPermissionWildcard] OK");
    }
    
}
