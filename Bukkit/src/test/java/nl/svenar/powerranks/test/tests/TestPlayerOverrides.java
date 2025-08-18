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

import org.bukkit.entity.Player;

import org.mockbukkit.mockbukkit.ServerMock;

import nl.svenar.powerranks.bukkit.cache.CacheManager;
import nl.svenar.powerranks.common.structure.PRPlayer;
import nl.svenar.powerranks.test.util.Assert;
import nl.svenar.powerranks.test.util.Mock;
import nl.svenar.powerranks.test.util.TestDebugger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestInstance(Lifecycle.PER_CLASS)
public class TestPlayerOverrides {

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
    public void A_playerDenyBeatsRankAllow() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[A_playerDenyBeatsRankAllow] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(6);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "OverrideRankA");
        server.execute("pr", admin, "addperm", "OverrideRankA", "test.override.build.*");
        server.execute("pr", admin, "setrank", target.getName(), "OverrideRankA");
        prTarget.updatePermissionsFromRanks();
        Assert.assertTrue("prTarget should have 'test.override.build.place'.",
                prTarget.isPermissionAllowed("test.override.build.place", true));

        // Player explicit deny
        server.execute("pr", admin, "addplayerperm", target.getName(), "test.override.build.place", "false");
        prTarget.updatePermissionsFromRanks();
        Assert.assertFalse("prTarget should not have 'test.override.build.place'.",
                prTarget.isPermissionAllowed("test.override.build.place", true));

        TestDebugger.log(this, "[A_playerDenyBeatsRankAllow] OK");
    }

    @Test
    public void B_rankDenyBeatsPlayerAllow() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[B_rankDenyBeatsPlayerAllow] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(7);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "OverrideRankB");
        server.execute("pr", admin, "addperm", "OverrideRankB", "-test.override.break.block");
        server.execute("pr", admin, "setrank", target.getName(), "OverrideRankB");
        prTarget.updatePermissionsFromRanks();
        assertFalse(prTarget.isPermissionAllowed("test.override.break.block", true));

        // Player tries to allow
        server.execute("pr", admin, "addplayerperm", target.getName(), "test.override.break.block", "true");
        prTarget.updatePermissionsFromRanks();
        assertTrue("Rank allow should still win", prTarget.isPermissionAllowed("test.override.break.block", true));

        TestDebugger.log(this, "[B_rankDenyBeatsPlayerAllow] OK");
    }

    @Test
    public void C_playerWildcardAllow_withSpecificPlayerDeny() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[C_playerWildcardAllow_withSpecificPlayerDeny] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(8);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "OverrideRankC");
        server.execute("pr", admin, "setrank", target.getName(), "OverrideRankC");
        prTarget.updatePermissionsFromRanks();

        // Player wildcard allow
        server.execute("pr", admin, "addplayerperm", target.getName(), "test.ov.*", "true");
        prTarget.updatePermissionsFromRanks();
        Assert.assertTrue("prTarget should have 'test.ov.anything'.",
                prTarget.isPermissionAllowed("test.ov.anything", true));

        // Specific deny should override wildcard allow (both at player level)
        server.execute("pr", admin, "addplayerperm", target.getName(), "test.ov.block", "false");
        prTarget.updatePermissionsFromRanks();
        Assert.assertFalse("prTarget should not have 'test.ov.block'.",
                prTarget.isPermissionAllowed("test.ov.block", true));
        Assert.assertTrue("prTarget should have 'test.ov.other'.", prTarget.isPermissionAllowed("test.ov.other", true));

        TestDebugger.log(this, "[C_playerWildcardAllow_withSpecificPlayerDeny] OK");
    }

    @Test
    public void D_removePlayerPerm_revertsToInherited() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[D_removePlayerPerm_revertsToInherited] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(9);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "OverrideRankD");
        server.execute("pr", admin, "addperm", "OverrideRankD", "test.revert.inherited");
        server.execute("pr", admin, "setrank", target.getName(), "OverrideRankD");
        prTarget.updatePermissionsFromRanks();
        Assert.assertTrue("prTarget should have 'test.revert.inherited'.",
                prTarget.isPermissionAllowed("test.revert.inherited", true));

        server.execute("pr", admin, "addplayerperm", target.getName(), "test.revert.inherited", "false");
        prTarget.updatePermissionsFromRanks();

        Assert.assertFalse("prTarget should not have 'test.revert.inherited'.",
                prTarget.isPermissionAllowed("test.revert.inherited", true));

        server.execute("pr", admin, "delplayerperm", target.getName(), "test.revert.inherited");
        prTarget.updatePermissionsFromRanks();
        Assert.assertTrue("Should fall back to inherited allow",
                prTarget.isPermissionAllowed("test.revert.inherited", true));

        TestDebugger.log(this, "[D_removePlayerPerm_revertsToInherited] OK");
    }

    @Test
    public void E_removeNonExistingPlayerPerm_noChange() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[E_removeNonExistingPlayerPerm_noChange] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(10);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "OverrideRankE");
        server.execute("pr", admin, "addperm", "OverrideRankE", "test.none.base");
        server.execute("pr", admin, "setrank", target.getName(), "OverrideRankE");
        prTarget.updatePermissionsFromRanks();
        assertTrue(prTarget.isPermissionAllowed("test.none.base", true));

        // Try remove a non-existing deny
        server.execute("pr", admin, "delplayerperm", target.getName(), "test.none.base", "false");
        prTarget.updatePermissionsFromRanks();
        assertTrue("Base state should remain", prTarget.isPermissionAllowed("test.none.base", true));

        TestDebugger.log(this, "[E_removeNonExistingPlayerPerm_noChange] OK");
    }
}
