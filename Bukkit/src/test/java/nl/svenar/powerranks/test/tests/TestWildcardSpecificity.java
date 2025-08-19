package nl.svenar.powerranks.test.tests;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.bukkit.entity.Player;

import org.mockbukkit.mockbukkit.ServerMock;
import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.common.structure.PRPermission;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.test.util.Assert;
import nl.svenar.powerranks.test.util.Mock;
import nl.svenar.powerranks.test.util.TestDebugger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestInstance(Lifecycle.PER_CLASS)
public class TestWildcardSpecificity {

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
    public void A_specificDenyBeatsWildcardAllow() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[A_specificDenyBeatsWildcardAllow] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(11);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "SpecRankA");
        server.execute("pr", admin, "addperm", "SpecRankA", "test.cmd.*"); // wildcard allow
        server.execute("pr", admin, "addperm", "SpecRankA", "-test.cmd.extra.*"); // specific deny
        server.execute("pr", admin, "setrank", target.getName(), "SpecRankA");

        prTarget.updatePermissionsFromRanks();
        assertFalse(prTarget.isPermissionAllowed("test.cmd.extra.kick", true));
        assertTrue(prTarget.isPermissionAllowed("test.cmd.ban", true));

        // getPermission should resolve to the most specific node
        
        PRPermission kickPerm = prTarget.getPermission("test.cmd.extra.kick", false);
        Assert.assertNull("test.cmd.extra.kick should be null.", kickPerm);

        PRPermission banPerm = prTarget.getPermission("test.cmd.ban", true);
        Assert.assertEquals("test.cmd.ban included in 'test.cmd.*'.", "test.cmd.*", banPerm.getName());
        Assert.assertTrue("Permission value should be allow", banPerm.getValue());

        TestDebugger.log(this, "[A_specificDenyBeatsWildcardAllow] OK");
    }

    @Test
    public void B_specificAllowDoesNotOverrideSpecificDenySameDepth() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[B_specificAllowDoesNotOverrideSpecificDenySameDepth] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(12);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "SpecRankB1");
        server.execute("pr", admin, "createrank", "SpecRankB2");

        server.execute("pr", admin, "setweight", "SpecRankB1", "100");
        server.execute("pr", admin, "setweight", "SpecRankB2", "100");

        server.execute("pr", admin, "addperm", "SpecRankB1", "test.depth.same"); // allow
        server.execute("pr", admin, "addperm", "SpecRankB2", "-test.depth.same"); // deny
        server.execute("pr", admin, "setrank", target.getName(), "SpecRankB1");
        server.execute("pr", admin, "addrank", target.getName(), "SpecRankB2");

        prTarget.updatePermissionsFromRanks();
        Assert.assertFalse("prTarget does not have permission 'test.depth.same'.", prTarget.isPermissionAllowed("test.depth.same", true));

        TestDebugger.log(this, "[B_specificAllowDoesNotOverrideSpecificDenySameDepth] OK");
    }

    @Test
    public void C_wildcardSegmentBoundaries() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[C_wildcardSegmentBoundaries] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(13);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "SpecRankC");
        server.execute("pr", admin, "addperm", "SpecRankC", "test.command.*");
        server.execute("pr", admin, "setrank", target.getName(), "SpecRankC");
        prTarget.updatePermissionsFromRanks();

        // "test.command" (no trailing segment) should not match "test.command.*"
        assertNull(prTarget.getPermission("test.command", true));
        // Sibling path should not match
        assertNull(prTarget.getPermission("test.other", true));

        // Children should match
        assertEquals("test.command.*", prTarget.getPermission("test.command.help", true).getName());

        TestDebugger.log(this, "[C_wildcardSegmentBoundaries] OK");
    }

    @Test
    public void D_multipleWildcards_mostSpecificWins() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[D_multipleWildcards_mostSpecificWins] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(14);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "SpecRankD");
        server.execute("pr", admin, "addperm", "SpecRankD", "test.*");
        server.execute("pr", admin, "addperm", "SpecRankD", "-test.command.*");
        server.execute("pr", admin, "addperm", "SpecRankD", "test.command.kick.*");
        server.execute("pr", admin, "setrank", target.getName(), "SpecRankD");
        prTarget.updatePermissionsFromRanks();

        // "test.command.kick.temp" -> allow from most specific wildcard despite parent deny
        assertTrue(prTarget.isPermissionAllowed("test.command.kick.temp", true));
        // "test.command.ban" -> denied due to "-test.command.*"
        assertFalse(prTarget.isPermissionAllowed("test.command.ban", true));
        // "test.something" -> allowed due to "test.*"
        assertTrue(prTarget.isPermissionAllowed("test.something", true));

        TestDebugger.log(this, "[D_multipleWildcards_mostSpecificWins] OK");
    }

    @Test
    public void E_specificDenyBeatsRankWildcardAllow() {
        TestDebugger.log(this, "[E_specificDenyBeatsRankWildcardAllow] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(1);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        server.execute("pr", admin, "createrank", "WildRank");
        server.execute("pr", admin, "addperm", "WildRank", "test.wild.*");
        server.execute("pr", admin, "setrank", target.getName(), "WildRank");
        prTarget.updatePermissionsFromRanks();
        Assert.assertTrue("prTarget has permission 'test.wild.block'.", prTarget.isPermissionAllowed("test.wild.block", true));

        // Add specific deny
        server.execute("pr", admin, "addplayerperm", target.getName(), "test.wild.block", "false");
        prTarget.updatePermissionsFromRanks();
        Assert.assertFalse("Specific deny must override wildcard allow",
                prTarget.isPermissionAllowed("test.wild.block", true));

        TestDebugger.log(this, "[E_specificDenyBeatsRankWildcardAllow] OK");
    }
}
