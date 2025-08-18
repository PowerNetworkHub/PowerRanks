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
public class TestInheritance {

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
    public void A_simpleInheritance_grantsFromParent() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[A_simpleInheritance_grantsFromParent] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(1);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "ParentA");
        server.execute("pr", admin, "createrank", "ChildA");
        server.execute("pr", admin, "addperm", "ParentA", "test.inherit.basic");
        server.execute("pr", admin, "addinheritance", "ChildA", "ParentA");
        server.execute("pr", admin, "setrank", target.getName(), "ChildA");

        prTarget.updatePermissionsFromRanks();
        Assert.assertTrue("prTarget has permission test.inherit.basic", prTarget.isPermissionAllowed("test.inherit.basic", true));

        TestDebugger.log(this, "[A_simpleInheritance_grantsFromParent] OK");
    }

    @Test
    public void B_multiLevelInheritance_conflictingDenyWins_ChildSameWeight() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[B_multiLevelInheritance_conflictingDenyWins_ChildSameWeight] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(2);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "LevelC");
        server.execute("pr", admin, "createrank", "LevelB");
        server.execute("pr", admin, "createrank", "LevelA");

        server.execute("pr", admin, "setweight", "LevelC", "1");
        server.execute("pr", admin, "setweight", "LevelB", "1");
        server.execute("pr", admin, "setweight", "LevelA", "1");

        server.execute("pr", admin, "addperm", "LevelC", "test.level.c"); // allow
        server.execute("pr", admin, "addperm", "LevelB", "test.level.b"); // allow
        server.execute("pr", admin, "addperm", "LevelB", "-test.level.c"); // deny overrides C

        server.execute("pr", admin, "addinheritance", "LevelB", "LevelC");
        server.execute("pr", admin, "addinheritance", "LevelA", "LevelB");

        server.execute("pr", admin, "setrank", target.getName(), "LevelA");
        prTarget.updatePermissionsFromRanks();

        Assert.assertTrue("prTarget has permission test.level.b", prTarget.isPermissionAllowed("test.level.b", true));
        Assert.assertFalse("prTarget does not have permission test.level.c", prTarget.isPermissionAllowed("test.level.c", true));

        TestDebugger.log(this, "[B_multiLevelInheritance_conflictingDenyWins_ChildSameWeight] OK");
    }

    @Test
    public void C_multiLevelInheritance_conflictingDenyWins_ChildLowerWeight() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[C_multiLevelInheritance_conflictingDenyWins_ChildLowerWeight] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(2);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "LevelC");
        server.execute("pr", admin, "createrank", "LevelB");
        server.execute("pr", admin, "createrank", "LevelA");

        server.execute("pr", admin, "setweight", "LevelC", "1");
        server.execute("pr", admin, "setweight", "LevelB", "2");
        server.execute("pr", admin, "setweight", "LevelA", "3");

        server.execute("pr", admin, "addperm", "LevelC", "test.level.c"); // allow
        server.execute("pr", admin, "addperm", "LevelB", "test.level.b"); // allow
        server.execute("pr", admin, "addperm", "LevelB", "-test.level.c"); // deny overrides C

        server.execute("pr", admin, "addinheritance", "LevelB", "LevelC");
        server.execute("pr", admin, "addinheritance", "LevelA", "LevelB");

        server.execute("pr", admin, "setrank", target.getName(), "LevelA");
        prTarget.updatePermissionsFromRanks();

        Assert.assertTrue("prTarget has permission test.level.b", prTarget.isPermissionAllowed("test.level.b", true));
        Assert.assertFalse("prTarget does not have permission test.level.c", prTarget.isPermissionAllowed("test.level.c", true));

        TestDebugger.log(this, "[C_multiLevelInheritance_conflictingDenyWins_ChildLowerWeight] OK");
    }

    @Test
    public void D_multiLevelInheritance_conflictingDenyWins_ChildHigherWeight() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[D_multiLevelInheritance_conflictingDenyWins_ChildHigherWeight] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(2);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "LevelC");
        server.execute("pr", admin, "createrank", "LevelB");
        server.execute("pr", admin, "createrank", "LevelA");

        server.execute("pr", admin, "setweight", "LevelC", "3");
        server.execute("pr", admin, "setweight", "LevelB", "2");
        server.execute("pr", admin, "setweight", "LevelA", "1");

        server.execute("pr", admin, "addperm", "LevelC", "test.level.c"); // allow
        server.execute("pr", admin, "addperm", "LevelB", "test.level.b"); // allow
        server.execute("pr", admin, "addperm", "LevelB", "-test.level.c"); // deny overrides C

        server.execute("pr", admin, "addinheritance", "LevelB", "LevelC");
        server.execute("pr", admin, "addinheritance", "LevelA", "LevelB");

        server.execute("pr", admin, "setrank", target.getName(), "LevelA");
        prTarget.updatePermissionsFromRanks();

        Assert.assertTrue("prTarget has permission test.level.b", prTarget.isPermissionAllowed("test.level.b", true));
        Assert.assertFalse("prTarget does not have permission test.level.c", prTarget.isPermissionAllowed("test.level.c", true));

        TestDebugger.log(this, "[D_multiLevelInheritance_conflictingDenyWins_ChildHigherWeight] OK");
    }

    @Test
    public void E_conflictingParents_denyBeatsAllow() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[E_conflictingParents_denyBeatsAllow] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(3);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "ParentAllow");
        server.execute("pr", admin, "createrank", "ParentDeny");
        server.execute("pr", admin, "createrank", "ChildC");

        server.execute("pr", admin, "addperm", "ParentAllow", "test.conflict.foo");
        server.execute("pr", admin, "addperm", "ParentDeny", "-test.conflict.foo");

        server.execute("pr", admin, "addinheritance", "ChildC", "ParentAllow");
        server.execute("pr", admin, "addinheritance", "ChildC", "ParentDeny");

        server.execute("pr", admin, "setrank", target.getName(), "ChildC");
        prTarget.updatePermissionsFromRanks();

        Assert.assertFalse("prTarget does not have permission test.conflict.foo", prTarget.isPermissionAllowed("test.conflict.foo", true));

        TestDebugger.log(this, "[E_conflictingParents_denyBeatsAllow] OK");
    }

    @Test
    public void F_removeInheritance_revokesPermissionsFromThatParentOnly() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[F_removeInheritance_revokesPermissionsFromThatParentOnly] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(4);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        server.execute("pr", admin, "createrank", "ParentX");
        server.execute("pr", admin, "createrank", "ParentY");
        server.execute("pr", admin, "createrank", "ChildD");

        server.execute("pr", admin, "addperm", "ParentX", "test.remove.x");
        server.execute("pr", admin, "addperm", "ParentY", "test.remove.y");

        server.execute("pr", admin, "addinheritance", "ChildD", "ParentX");
        server.execute("pr", admin, "addinheritance", "ChildD", "ParentY");

        server.execute("pr", admin, "setrank", target.getName(), "ChildD");
        prTarget.updatePermissionsFromRanks();
        assertTrue(prTarget.isPermissionAllowed("test.remove.x", true));
        assertTrue(prTarget.isPermissionAllowed("test.remove.y", true));

        server.execute("pr", admin, "delinheritance", "ChildD", "ParentX");
        prTarget.updatePermissionsFromRanks();
        assertFalse(prTarget.isPermissionAllowed("test.remove.x", true));
        assertTrue(prTarget.isPermissionAllowed("test.remove.y", true));

        TestDebugger.log(this, "[F_removeInheritance_revokesPermissionsFromThatParentOnly] OK");
    }

    @Test
    public void G_deepInheritanceChain_resolvesWithoutOverflow() {
        TestDebugger.log(this, "");
        TestDebugger.log(this, "[G_deepInheritanceChain_resolvesWithoutOverflow] Start");

        Player admin = Mock.getPlayer(0);
        Player target = Mock.getPlayer(5);
        PRPlayer prTarget = CacheManager.getPlayer(target.getUniqueId().toString());

        admin.setOp(true);
        target.setOp(false);

        final int depth = 12;
        String[] ranks = new String[depth];
        for (int i = 0; i < depth; i++) {
            ranks[i] = "Chain" + i;
            server.execute("pr", admin, "createrank", ranks[i]);
            if (i > 0) {
                server.execute("pr", admin, "addinheritance", ranks[i], ranks[i - 1]);
            }
        }
        server.execute("pr", admin, "addperm", ranks[0], "test.deep.start");
        server.execute("pr", admin, "addperm", ranks[depth - 1], "test.deep.end");

        server.execute("pr", admin, "setrank", target.getName(), ranks[depth - 1]);
        prTarget.updatePermissionsFromRanks();

        assertTrue(prTarget.isPermissionAllowed("test.deep.start", true));
        assertTrue(prTarget.isPermissionAllowed("test.deep.end", true));

        TestDebugger.log(this, "[G_deepInheritanceChain_resolvesWithoutOverflow] OK");
    }
}
